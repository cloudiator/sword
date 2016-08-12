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


import com.google.common.collect.Lists;
import de.uniulm.omi.cloudiator.common.OneWayConverter;
import de.uniulm.omi.cloudiator.sword.api.domain.PriceModel;
import de.uniulm.omi.cloudiator.sword.api.extensions.ComputeInstancePrice;
import de.uniulm.omi.cloudiator.sword.core.domain.ComputeInstancePriceBuilder;
import io.github.cloudiator.cloudharmony.model.ComputeInstanceTypePrice;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by daniel on 09.08.16.
 */
public class ComputeInstanceTypePriceToComputeInstancePrice
    implements OneWayConverter<ComputeInstanceTypePrice, ComputeInstancePrice> {

    private final PriceModelFunction priceModelFunction;

    public ComputeInstanceTypePriceToComputeInstancePrice() {
        priceModelFunction = new PriceModelFunction();
    }

    @Override public ComputeInstancePrice apply(ComputeInstanceTypePrice computeInstanceTypePrice) {
        return ComputeInstancePriceBuilder.newBuilder()
            .normalizedPrice(computeInstanceTypePrice.getNormalizedPrice())
            .normalizedTerm(computeInstanceTypePrice.getNormalizedTerm())
            .priceModel(priceModelFunction.apply(computeInstanceTypePrice)).build();
    }


    private static class PriceModelFunction
        implements Function<ComputeInstanceTypePrice, PriceModel> {

        private static Map<PriceModel, List<String>> keyWords;

        private static void registerKeyWord(PriceModel priceModel, String keyword) {
            if (keyWords.get(priceModel) == null) {
                keyWords.put(priceModel, Lists.newArrayList(keyword));
            } else {
                keyWords.get(priceModel).add(keyword);
            }
        }

        static {
            registerKeyWord(PriceModel.SPOT, "preemptible");
            registerKeyWord(PriceModel.SPOT, "spot");
            registerKeyWord(PriceModel.FIXED, "fixed duration");
            registerKeyWord(PriceModel.PAYG, "on demand");
            registerKeyWord(PriceModel.RESERVED, "reserve");
            registerKeyWord(PriceModel.RESERVED, "sustained");
        }

        @Override public PriceModel apply(ComputeInstanceTypePrice computeInstanceTypePrice) {
            Set<PriceModel> candidates = new HashSet<>(PriceModel.values().length);
            //try to match keywords
            keyWords.forEach((priceModel, keywords) -> keywords.forEach(keyword -> {
                if (computeInstanceTypePrice.getLabel().toLowerCase().contains(keyword)
                    || computeInstanceTypePrice.getNotes().contains(keyword)) {
                    candidates.add(priceModel);
                }
            }));
            if (candidates.size() == 1) {
                return candidates.iterator().next();
            } else if (candidates.size() == 0) {
                return PriceModel.UNKNOWN;
            } else {
                throw new IllegalStateException("Found multiple candidates.");
            }
        }
    }
}
