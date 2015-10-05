/*
 * Copyright (c) 2014-2015 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.core.domain;

import de.uniulm.omi.cloudiator.sword.core.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.sword.core.domain.ImageImpl;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by daniel on 03.12.14.
 */
public class ImageImplTest {

    private final String idTest = "123456";
    private final String nameTest = "name";

    @Test(expected = NullPointerException.class) public void idNotNullableTest() {
        ImageBuilder.newBuilder().id(null).name(nameTest).build();
    }

    @Test(expected = IllegalArgumentException.class) public void idNotEmptyTest() {
        ImageBuilder.newBuilder().id("").name(nameTest).build();
    }

    @Test(expected = NullPointerException.class) public void nameNotNullableTest() {
        ImageBuilder.newBuilder().id(idTest).name(null).build();
    }

    @Test(expected = IllegalArgumentException.class) public void nameNotEmptyTest() {
        ImageBuilder.newBuilder().id(idTest).name("").build();
    }


    @Test public void getIdTest() {
        final ImageImpl image = ImageBuilder.newBuilder().id(idTest).name(nameTest).build();
        assertThat(image.id(), equalTo(idTest));
    }

    @Test public void getNameTest() {
        final ImageImpl image = ImageBuilder.newBuilder().id(idTest).name(nameTest).build();
        assertThat(image.name(), equalTo(nameTest));
    }

}
