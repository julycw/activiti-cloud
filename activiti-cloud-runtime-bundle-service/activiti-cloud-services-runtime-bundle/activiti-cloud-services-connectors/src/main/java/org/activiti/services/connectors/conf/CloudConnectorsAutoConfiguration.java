/*
 * Copyright 2017-2020 Alfresco Software, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.services.connectors.conf;

import org.activiti.cloud.services.events.configuration.RuntimeBundleProperties;
import org.activiti.cloud.services.events.converter.RuntimeBundleInfoAppender;
import org.activiti.cloud.services.events.listeners.ProcessEngineEventsAggregator;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.bpmn.behavior.VariablesPropagator;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.persistence.entity.integration.IntegrationContextManager;
import org.activiti.engine.integration.IntegrationContextService;
import org.activiti.runtime.api.conf.ConnectorsAutoConfiguration;
import org.activiti.runtime.api.connector.DefaultServiceTaskBehavior;
import org.activiti.runtime.api.connector.IntegrationContextBuilder;
import org.activiti.services.connectors.IntegrationRequestSender;
import org.activiti.services.connectors.behavior.MQServiceTaskBehavior;
import org.activiti.services.connectors.channel.IntegrationRequestBuilder;
import org.activiti.services.connectors.channel.IntegrationRequestReplayer;
import org.activiti.services.connectors.channel.ProcessEngineIntegrationChannels;
import org.activiti.services.connectors.channel.ServiceTaskIntegrationErrorEventHandler;
import org.activiti.services.connectors.channel.ServiceTaskIntegrationResultEventHandler;
import org.activiti.services.connectors.message.IntegrationContextMessageBuilderFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@AutoConfigureBefore(value = ConnectorsAutoConfiguration.class)
@PropertySource("classpath:config/integration-result-stream.properties")
@EnableBinding(ProcessEngineIntegrationChannels.class)
public class CloudConnectorsAutoConfiguration {

    private static final String LOCAL_SERVICE_TASK_BEHAVIOUR_BEAN_NAME = "localServiceTaskBehaviour";

    @Bean
    @ConditionalOnMissingBean
    public ServiceTaskIntegrationResultEventHandler serviceTaskIntegrationResultEventHandler(
        RuntimeService runtimeService,
        IntegrationContextService integrationContextService,
        RuntimeBundleProperties runtimeBundleProperties,
        ManagementService managementService,
        ProcessEngineEventsAggregator processEngineEventsAggregator,
        VariablesPropagator variablesPropagator) {
        return new ServiceTaskIntegrationResultEventHandler(runtimeService, integrationContextService,
            runtimeBundleProperties, managementService, processEngineEventsAggregator, variablesPropagator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServiceTaskIntegrationErrorEventHandler serviceTaskIntegrationErrorEventHandler(RuntimeService runtimeService,
                                                                                           IntegrationContextService integrationContextService,
                                                                                           ManagementService managementService,
                                                                                           RuntimeBundleProperties runtimeBundleProperties,
                                                                                           ProcessEngineEventsAggregator processEngineEventsAggregator) {
        return new ServiceTaskIntegrationErrorEventHandler(runtimeService,
                                                           integrationContextService,
                                                           managementService,
                                                           runtimeBundleProperties,
                                                           processEngineEventsAggregator);
    }

    @Bean
    @ConditionalOnMissingBean
    public IntegrationRequestSender integrationRequestSender(BinderAwareChannelResolver resolver,
                                                             IntegrationContextMessageBuilderFactory messageBuilderFactory) {
        return new IntegrationRequestSender(resolver,
                                            messageBuilderFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public IntegrationRequestBuilder integrationRequestBuilder(RuntimeBundleInfoAppender runtimeBundleInfoAppender,  BindingServiceProperties bindingServiceProperties) {
        return  new IntegrationRequestBuilder(runtimeBundleInfoAppender, bindingServiceProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public IntegrationRequestReplayer integrationRequestReplayer(RuntimeService runtimeService,
                                                                 ManagementService managementService,
                                                                 MQServiceTaskBehavior mqServiceTaskBehavior){
        return new IntegrationRequestReplayer(runtimeService,
                                              managementService,
                                              mqServiceTaskBehavior);
    }

    @Bean
    @ConditionalOnMissingBean
    public IntegrationContextMessageBuilderFactory integrationContextMessageBuilderFactory(RuntimeBundleProperties properties) {
        return new IntegrationContextMessageBuilderFactory(properties);
    }

    @Bean(name = LOCAL_SERVICE_TASK_BEHAVIOUR_BEAN_NAME)
    @ConditionalOnMissingBean(name = LOCAL_SERVICE_TASK_BEHAVIOUR_BEAN_NAME)
    public DefaultServiceTaskBehavior localServiceTaskBehavior(ApplicationContext applicationContext,
        IntegrationContextBuilder integrationContextBuilder, VariablesPropagator variablesPropagator) {
        // this bean is exposed under two different names (LOCAL_SERVICE_TASK_BEHAVIOUR_BEAN_NAME and
        // DefaultActivityBehaviorFactory.DEFAULT_SERVICE_TASK_BEAN_NAME) to allow MQServiceTaskBehavior
        // to use composition instead of inheritance, this will make maintenance easier as changes in constructor
        // of DefaultServiceTaskBehavior will not impact the constructor of MQServiceTaskBehavior.
        // LOCAL_SERVICE_TASK_BEHAVIOUR_BEAN_NAME will be injected in MQServiceTaskBehavior;
        // DefaultActivityBehaviorFactory.DEFAULT_SERVICE_TASK_BEAN_NAME will be available only in non-cloud environment:
        // MQServiceTaskBehavior will replace it for cloud environment.
        return new DefaultServiceTaskBehavior(applicationContext, integrationContextBuilder, variablesPropagator);
    }

    @Bean(name = DefaultActivityBehaviorFactory.DEFAULT_SERVICE_TASK_BEAN_NAME)
    @ConditionalOnMissingBean(name = DefaultActivityBehaviorFactory.DEFAULT_SERVICE_TASK_BEAN_NAME)
    public MQServiceTaskBehavior mqServiceTaskBehavior(
        IntegrationContextManager integrationContextManager,
        ApplicationEventPublisher eventPublisher,
        IntegrationContextBuilder integrationContextBuilder,
        DefaultServiceTaskBehavior defaultServiceTaskBehavior,
        ProcessEngineEventsAggregator processEngineEventsAggregator,
        RuntimeBundleProperties runtimeBundleProperties,
        IntegrationRequestBuilder integrationRequestBuilder) {
        return new MQServiceTaskBehavior(integrationContextManager, eventPublisher, integrationContextBuilder,
            defaultServiceTaskBehavior, processEngineEventsAggregator, runtimeBundleProperties,
            integrationRequestBuilder);
    }
}
