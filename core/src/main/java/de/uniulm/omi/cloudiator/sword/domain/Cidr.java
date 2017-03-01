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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by daniel on 04.07.16.
 */
public interface Cidr {

    /**
     * Network address in dotted form (e.g. 0.0.0.0)
     *
     * @return the network address
     */
    String address();

    /**
     * The slash value
     *
     * @return the value behind the slash
     */
    int slash();

}
