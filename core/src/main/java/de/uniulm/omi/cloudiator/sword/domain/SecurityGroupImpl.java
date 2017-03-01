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

package de.uniulm.omi.cloudiator.sword.domain;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 01.07.16.
 */
public class SecurityGroupImpl extends ResourceImpl implements SecurityGroup {

    private final Set<SecurityGroupRule> securityGroupRules;

    public SecurityGroupImpl(String id, String providerId, String name, @Nullable Location location,
        Set<SecurityGroupRule> securityGroupRules) {
        super(id, providerId, name, location);
        checkNotNull(securityGroupRules, "securityGroupRules is null.");
        this.securityGroupRules = securityGroupRules;
    }

    @Override public Set<SecurityGroupRule> rules() {
        return ImmutableSet.copyOf(securityGroupRules);
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id()).add("providerId", providerId())
            .add("name", name()).add("location", location())
            .add("securityGroupRules", Arrays.toString(securityGroupRules.toArray())).toString();
    }
}
