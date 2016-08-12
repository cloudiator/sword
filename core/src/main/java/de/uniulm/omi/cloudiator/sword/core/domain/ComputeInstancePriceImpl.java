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
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by daniel on 09.08.16.
 */
public class ComputeInstancePriceImpl implements ComputeInstancePrice {

    private final PriceModel priceModel;
    private final BigDecimal normalizedPrice;
    @Nullable private final BigDecimal normalizedTerm;

    public ComputeInstancePriceImpl(PriceModel priceModel, BigDecimal normalizedPrice,
        @Nullable BigDecimal normalizedTerm) {
        checkNotNull(priceModel);
        this.priceModel = priceModel;
        checkNotNull(normalizedPrice, "normalized price is null.");
        checkArgument(normalizedPrice.compareTo(BigDecimal.ZERO) >= 0,
            "normalized price needs to be larger than zero");
        this.normalizedPrice = normalizedPrice;
        if (normalizedTerm != null) {
            checkArgument(normalizedTerm.compareTo(BigDecimal.ZERO) > 0,
                "normalized term needs to be larger than zero");
        }
        this.normalizedTerm = normalizedTerm;
    }

    @Override public PriceModel priceModel() {
        return priceModel;
    }

    @Override public BigDecimal normalizedPrice() {
        return normalizedPrice;
    }

    @Nullable @Override public Optional<BigDecimal> normalizedTerm() {
        return Optional.ofNullable(normalizedTerm);
    }
}
