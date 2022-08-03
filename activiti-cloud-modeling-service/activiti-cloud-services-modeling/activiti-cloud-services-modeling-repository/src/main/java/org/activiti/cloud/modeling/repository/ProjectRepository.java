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
package org.activiti.cloud.modeling.repository;

import org.activiti.cloud.modeling.api.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface for {@link Project} entities repository
 */
public interface ProjectRepository<P extends Project> {

    Page<P> getProjects(Pageable pageable,
                        String nameToFilter,
                        List<String> filteredProjectIds);

    Optional<P> findProjectById(String projectId);

    P createProject(P project);

    P updateProject(P projectToUpdate);

    P copyProject(P projectToCopy, String newProjectName);

    void deleteProject(P project);
}
