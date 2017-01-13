/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.suppliers;

import com.google.common.base.Supplier;
import de.uniulm.omi.cloudiator.sword.api.domain.Image;

import java.util.Collections;
import java.util.Set;

/**
 * Created by daniel on 14.11.16.
 */
public class EmptyImageSupplier implements Supplier<Set<Image>> {

    @Override public Set<Image> get() {
        return Collections.emptySet();
    }
}