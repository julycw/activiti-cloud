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
package org.activiti.cloud.services.query.events.handlers;

import org.activiti.api.process.model.events.ProcessCandidateStarterGroupEvent;
import org.activiti.api.runtime.model.impl.ProcessCandidateStarterGroupImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCandidateStarterGroupAddedEventImpl;
import org.activiti.cloud.services.query.model.ProcessCandidateStarterGroupEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProcessCandidateStarterGroupAddedEventHandlerTest {

    @InjectMocks
    private ProcessCandidateStarterGroupAddedEventHandler handler;

    @Mock
    private EntityManager entityManager;

    @Test
    public void handleShouldStoreNewProcessCandidateGroupUser() {
        //given
        ProcessCandidateStarterGroupImpl candidateGroup = new ProcessCandidateStarterGroupImpl(UUID.randomUUID().toString(),
                                                                        UUID.randomUUID().toString());
        CloudProcessCandidateStarterGroupAddedEventImpl event = new CloudProcessCandidateStarterGroupAddedEventImpl(candidateGroup);

        //when
        handler.handle(event);

        //then
        ArgumentCaptor<ProcessCandidateStarterGroupEntity> captor = ArgumentCaptor.forClass(ProcessCandidateStarterGroupEntity.class);
        verify(entityManager).persist(captor.capture());
        assertThat(captor.getValue().getProcessDefinitionId()).isEqualTo(event.getEntity().getProcessDefinitionId());
        assertThat(captor.getValue().getGroupId()).isEqualTo(event.getEntity().getGroupId());
    }

    @Test
    public void getHandledEventShouldReturnProcessCandidateStarterGroupAddedEvent() {
        //when
        String handledEvent = handler.getHandledEvent();

        //then
        assertThat(handledEvent)
            .isEqualTo(ProcessCandidateStarterGroupEvent
                       .ProcessCandidateStarterGroupEvents.PROCESS_CANDIDATE_STARTER_GROUP_ADDED.name());
    }

}
