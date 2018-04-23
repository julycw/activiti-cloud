/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.connectors.starter.channels;

import org.activiti.cloud.connectors.starter.configuration.ConnectorProperties;
import org.activiti.cloud.connectors.starter.model.IntegrationResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class IntegrationResultSenderImpl implements IntegrationResultSender {

    private final BinderAwareChannelResolver resolver;

    private ConnectorProperties connectorProperties;

    @Autowired
    public IntegrationResultSenderImpl(BinderAwareChannelResolver resolver, ConnectorProperties connectorProperties) {
        this.resolver = resolver;
        this.connectorProperties = connectorProperties;
    }

    @Override
    public void send(Message<IntegrationResultEvent> message) {

        message.getPayload().setAppName(connectorProperties.getActivitiAppName());
        message.getPayload().setAppVersion(connectorProperties.getActivitiAppVersion());
        message.getPayload().setServiceName(connectorProperties.getServiceName());
        message.getPayload().setServiceFullName(connectorProperties.getServiceFullName());
        message.getPayload().setServiceType(connectorProperties.getServiceType());
        message.getPayload().setServiceVersion(connectorProperties.getServiceVersion());

        resolver.resolveDestination("integrationResult:" + message.getPayload().getTargetApplication()).send(message);

    }

}
