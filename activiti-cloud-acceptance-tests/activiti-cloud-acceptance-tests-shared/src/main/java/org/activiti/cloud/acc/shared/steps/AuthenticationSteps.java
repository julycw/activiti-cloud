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
package org.activiti.cloud.acc.shared.steps;

import net.thucydides.core.annotations.Step;
import org.activiti.cloud.acc.shared.rest.feign.EnableFeignContext;
import org.activiti.cloud.acc.shared.model.AuthToken;
import org.activiti.cloud.acc.shared.rest.TokenHolder;
import org.activiti.cloud.acc.shared.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

/**
 * User authentication steps
 */
@EnableFeignContext
public class AuthenticationSteps {

    private static final String AUTH_CLIENT_ID = "activiti";
    private static final String AUTH_GRANT_TYPE = "password";
    private static String AUTH_PASSWORD = "password";

    @Autowired
    private AuthenticationService authenticationService;

    @Step
    public void authenticateUser(String authUsername){
        if(authUsername.equals("admin")){
            AUTH_PASSWORD = "admin";
        }
        AuthToken authToken = authenticationService
                .authenticate(AUTH_CLIENT_ID,
                        AUTH_GRANT_TYPE,
                        authUsername,
                        AUTH_PASSWORD);
        TokenHolder.setAuthToken(authToken);
        TokenHolder.setUserName(authUsername);
    }

    @Step
    public void ensureUserIsAuthenticated() {
        AuthToken authToken = TokenHolder.getAuthToken();
        assertThat(authToken).isNotNull();
        assertThat(authToken.getAccess_token()).isNotNull();
    }
}
