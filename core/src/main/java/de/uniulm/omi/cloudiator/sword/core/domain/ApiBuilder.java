/*
 * Copyright (c) 2014-2017 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.cloudiator.sword.core.domain;

import com.google.common.base.MoreObjects;
import de.uniulm.omi.cloudiator.sword.api.domain.Api;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 18.01.17.
 */
public class ApiBuilder {

    @Nullable private String providerName;

    private ApiBuilder() {

    }

    private ApiBuilder(Api api) {
        this.providerName = api.providerName();
    }

    public static ApiBuilder newBuilder() {
        return new ApiBuilder();
    }

    public static ApiBuilder of(Api api) {
        checkNotNull(api, "api is null");
        return new ApiBuilder(api);
    }

    public ApiBuilder providerName(String providerName) {
        this.providerName = providerName;
        return this;
    }

    public Api build() {
        return new ApiImpl(providerName);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }
}
