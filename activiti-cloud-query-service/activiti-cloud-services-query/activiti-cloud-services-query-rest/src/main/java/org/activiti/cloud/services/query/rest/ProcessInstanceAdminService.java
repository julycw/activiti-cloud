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
package org.activiti.cloud.services.query.rest;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.activiti.cloud.services.query.app.repository.EntityFinder;
import org.activiti.cloud.services.query.app.repository.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.ProcessInstanceEntity;
import org.hibernate.Filter;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;


public class ProcessInstanceAdminService {

    private final ProcessInstanceRepository processInstanceRepository;

    private final EntityFinder entityFinder;

    @PersistenceContext
    private EntityManager entityManager;

    public ProcessInstanceAdminService(ProcessInstanceRepository processInstanceRepository,
        EntityFinder entityFinder){
        this.processInstanceRepository = processInstanceRepository;
        this.entityFinder = entityFinder;
    }

    public Page<ProcessInstanceEntity> findAll(Predicate predicate, Pageable pageable) {

        return processInstanceRepository.findAll(Optional.ofNullable(predicate)
                .orElseGet(BooleanBuilder::new),
            pageable);

    }

    @Transactional
    public Page<ProcessInstanceEntity> findAllWithVariables(Predicate predicate, List<String> variableKeys, Pageable pageable) {

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("variablesFilter");
        filter.setParameterList("variableKeys", variableKeys);
        Page<ProcessInstanceEntity> processInstanceEntities = findAll(predicate, pageable);
        // Due to performance issues (e.g. https://github.com/Activiti/Activiti/issues/3139)
        // we have to explicitly initialize the lazy loaded field to be able to work with disabled Open Session in View
        processInstanceEntities.forEach(processInstanceEntity -> Hibernate.initialize(processInstanceEntity.getVariables()));
        return processInstanceEntities;

    }

    public  ProcessInstanceEntity findById(@PathVariable String processInstanceId) {

        return entityFinder.findById(processInstanceRepository,
            processInstanceId,
            "Unable to find task for the given id:'" + processInstanceId + "'");

    }


}
