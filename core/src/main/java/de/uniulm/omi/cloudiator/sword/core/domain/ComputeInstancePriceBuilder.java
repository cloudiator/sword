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

package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.api.domain.PriceModel;
import de.uniulm.omi.cloudiator.sword.api.extensions.ComputeInstancePrice;

import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * Created by daniel on 09.08.16.
 */
public class ComputeInstancePriceBuilder {

    private PriceModel priceModel;
    private BigDecimal normalizedPrice;
    @Nullable private BigDecimal normalizedTerm;

    private ComputeInstancePriceBuilder() {

    }

    public static ComputeInstancePriceBuilder newBuilder() {
        return new ComputeInstancePriceBuilder();
    }

    public ComputeInstancePriceBuilder priceModel(PriceModel priceModel) {
        this.priceModel = priceModel;
        return this;
    }

    public ComputeInstancePriceBuilder normalizedPrice(BigDecimal normalizedPrice) {
        this.normalizedPrice = normalizedPrice;
        return this;
    }

    public ComputeInstancePriceBuilder normalizedTerm(@Nullable BigDecimal normalizedTerm) {
        this.normalizedTerm = normalizedTerm;
        return this;
    }

    public ComputeInstancePrice build() {
        return new ComputeInstancePriceImpl(priceModel, normalizedPrice, normalizedTerm);
    }

}
