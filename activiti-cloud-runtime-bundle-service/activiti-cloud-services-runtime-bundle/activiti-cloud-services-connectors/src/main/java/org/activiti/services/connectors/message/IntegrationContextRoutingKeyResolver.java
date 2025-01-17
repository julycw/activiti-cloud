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
package org.activiti.services.connectors.message;

import java.util.Map;

import org.activiti.cloud.services.events.message.AbstractMessageHeadersRoutingKeyResolver;
import org.activiti.cloud.services.events.message.RuntimeBundleInfoMessageHeaders;

public class IntegrationContextRoutingKeyResolver extends AbstractMessageHeadersRoutingKeyResolver {

    private static final String INTEGRATION_CONTEXT = "integrationContext";

    public final String[] HEADER_KEYS = {RuntimeBundleInfoMessageHeaders.SERVICE_NAME,
                                         RuntimeBundleInfoMessageHeaders.APP_NAME,
                                         IntegrationContextMessageHeaders.CONNECTOR_TYPE,
                                         IntegrationContextMessageHeaders.PROCESS_INSTANCE_ID,
                                         IntegrationContextMessageHeaders.BUSINESS_KEY};
    @Override
    public String resolve(Map<String, Object> headers) {
        return build(headers, HEADER_KEYS);
    }

    @Override
    public String getPrefix() {
        return INTEGRATION_CONTEXT;
    }
}
