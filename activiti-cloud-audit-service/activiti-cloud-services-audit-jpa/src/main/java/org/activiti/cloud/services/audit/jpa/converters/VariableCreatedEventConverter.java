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
package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.model.shared.event.VariableEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudVariableCreatedEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudRuntimeEventImpl;
import org.activiti.cloud.api.model.shared.impl.events.CloudVariableCreatedEventImpl;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.VariableCreatedEventEntity;

public class VariableCreatedEventConverter extends BaseEventToEntityConverter {

    public VariableCreatedEventConverter(EventContextInfoAppender eventContextInfoAppender) {
        super(eventContextInfoAppender);
    }

    @Override
    public String getSupportedEvent() {
        return VariableEvent.VariableEvents.VARIABLE_CREATED.name();
    }

    @Override
    protected VariableCreatedEventEntity createEventEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        return new VariableCreatedEventEntity((CloudVariableCreatedEvent) cloudRuntimeEvent);
    }

    @Override
    protected CloudRuntimeEventImpl<?, ?> createAPIEvent(AuditEventEntity auditEventEntity) {
        VariableCreatedEventEntity variableCreatedEventEntity = (VariableCreatedEventEntity) auditEventEntity;

        return new CloudVariableCreatedEventImpl(variableCreatedEventEntity.getEventId(),
                                                 variableCreatedEventEntity.getTimestamp(),
                                                 variableCreatedEventEntity.getVariableInstance());
    }
}
