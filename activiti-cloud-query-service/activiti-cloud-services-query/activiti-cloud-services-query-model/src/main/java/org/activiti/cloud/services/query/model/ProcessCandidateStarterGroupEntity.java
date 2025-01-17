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
package org.activiti.cloud.services.query.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Objects;

@Entity(name="ProcessCandidateStarterGroup")
@IdClass(ProcessCandidateStarterGroupId.class)
@Table(name="PROCESS_CANDIDATE_STARTER_GROUP", indexes= {
		@Index(name="pcsg_groupId_idx", columnList="groupId", unique=false),
		@Index(name="pcsg_processDefinition_idx", columnList="processDefinitionId", unique=false)
	}
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@DynamicInsert
@DynamicUpdate
public class ProcessCandidateStarterGroupEntity {

    @Id
    private String processDefinitionId;

    @Id
    private String groupId;

    @JsonIgnore
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "processDefinitionId", referencedColumnName = "id", insertable = false, updatable = false, nullable = true
            , foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private ProcessDefinitionEntity processDefinition;

    @JsonCreator
    public ProcessCandidateStarterGroupEntity(@JsonProperty("processDefinitionId") String processDefinitionId,
                                              @JsonProperty("groupId") String groupId) {
        this.processDefinitionId = processDefinitionId;
        this.groupId = groupId;
    }

    public ProcessCandidateStarterGroupEntity() {
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ProcessDefinitionEntity getProcessDefinition() {
        return this.processDefinition;
    }

    public void setProcessDefinition(ProcessDefinitionEntity processDefinition) {
        this.processDefinition = processDefinition;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProcessCandidateStarterGroupEntity other = (ProcessCandidateStarterGroupEntity) obj;
        return Objects.equals(processDefinitionId, other.processDefinitionId) &&
                Objects.equals(groupId, other.groupId);
    }
}
