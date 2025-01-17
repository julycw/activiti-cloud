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
package org.activiti.cloud.services.modeling.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.activiti.cloud.modeling.api.ModelValidationErrorProducer;
import org.activiti.cloud.modeling.api.Project;
import org.activiti.cloud.modeling.api.ProjectConfiguration;
import org.activiti.cloud.services.modeling.jpa.audit.AuditableEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Project model entity
 */
@Table(name = "Project", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "createdBy"}))
@Entity(name = "Project")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class ProjectEntity extends AuditableEntity<String> implements Project<String>,
                                                                      ModelValidationErrorProducer {

    @JsonIgnore
    @ManyToMany(mappedBy = "projects")
    private Set<ModelEntity> models = new HashSet<>();

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String description;

    private String version;

    @OneToOne(mappedBy = "project", optional = false, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ProjectConfigurationEntity configuration;

    public ProjectEntity() {  // for JPA
    }

    public ProjectEntity(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Set<ModelEntity> getModels() {
        return models;
    }

    public void setModels(Set<ModelEntity> models) {
        this.models = models;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

     @Override
    public void setConfiguration(ProjectConfiguration configuration) {
         if (configuration == null) {
             if (this.configuration != null) {
                 this.configuration.setProject(null);
             }
         }
         else {
             configuration.setProject(this);
         }
         this.configuration = (ProjectConfigurationEntity) configuration;
    }

   @Override
    public ProjectConfiguration getConfiguration() {
        return configuration;
    }

    public void addModel(ModelEntity model){
        if(! models.contains(model)){
            models.add(model);
            model.addProject(this);
        }
    }

    public void removeModel(ModelEntity model){
        if(models.contains(model)){
            models.remove(model);
            model.removeProject(this);
        }
    }
}
