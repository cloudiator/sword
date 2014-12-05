/*
 * Copyright (c) 2014 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.executionware.core.domain.builders;

import de.uniulm.omi.executionware.core.domain.impl.ImageImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class ImageBuilderTest {

    ImageBuilder imageBuilder;

    @Before
    public void before() {
        this.imageBuilder = new ImageBuilder();
    }

    @Test
    public void builderTest() {
        String id = "abcdefg";
        String description = "This is a very fine image.";
        ImageImpl image = this.imageBuilder.id(id).description(description).build();
        assertThat(image.getId(), equalTo(id));
        assertThat(image.getDescription(), equalTo(description));
    }

}
