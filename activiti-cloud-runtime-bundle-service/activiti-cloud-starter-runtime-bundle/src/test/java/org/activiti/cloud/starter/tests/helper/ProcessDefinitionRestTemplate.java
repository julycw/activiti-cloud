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
package org.activiti.cloud.starter.tests.helper;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import org.activiti.cloud.api.process.model.CloudProcessDefinition;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

@TestComponent
public class ProcessDefinitionRestTemplate {

    private static final String PROCESS_DEFINITIONS_URL = "/v1/process-definitions/";
    public static final LinkedMultiValueMap<String, String> CONTENT_TYPE_HEADER =
        new LinkedMultiValueMap<>(Map.of("Content-type", List.of("application/json")));
    private static final ParameterizedTypeReference<PagedModel<CloudProcessDefinition>> PAGED_DEFINITIONS_RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };

    private TestRestTemplate testRestTemplate;

    public ProcessDefinitionRestTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    public ResponseEntity<PagedModel<CloudProcessDefinition>> getProcessDefinitions() {
        ResponseEntity<PagedModel<CloudProcessDefinition>> responseEntity = testRestTemplate.exchange(
            PROCESS_DEFINITIONS_URL,
            HttpMethod.GET,
            null,
            PAGED_DEFINITIONS_RESPONSE_TYPE);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseEntity;
    }

    public ResponseEntity<Map<String, String>> getProcessModelStaticValuesMappingForStartEvent(String id) {
        ResponseEntity<Map<String, String>> responseEntity = testRestTemplate.exchange(
            PROCESS_DEFINITIONS_URL + id + "/static-values",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseEntity;
    }

}
