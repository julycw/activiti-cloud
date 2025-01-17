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
package org.activiti.cloud.services.common.security.jwt;

import java.security.Principal;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JwtAccessTokenProvider {

    private final Function<Jwt, JwtAdapter> function;

    public JwtAccessTokenProvider(Function<Jwt, JwtAdapter> supplier) {
        this.function = supplier;
    }

    public JwtAdapter accessToken(@NonNull Jwt jwt) {
        return function.apply(jwt);
    }

    public Optional<JwtAdapter> accessToken(@NonNull Principal principal) {
        return Optional.of(principal)
            .filter(JwtAuthenticationToken.class::isInstance)
            .map(JwtAuthenticationToken.class::cast)
            .map(jwtAuthenticationToken -> accessToken(jwtAuthenticationToken.getToken()));
    }

}
