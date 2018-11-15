/*
 * Copyright (c) 2014-2018 University of Ulm
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by daniel on 30.07.15.
 */
public class VirtualMachineTemplateImplTest {

  String imageId = "imageId";
  String locationId = "locationId";
  String hardwareId = "hardwareId";
  String testName = "name";
  TemplateOptions templateOptions =
      TemplateOptionsBuilder.newBuilder().keyPairName("keyPairName").addOption("key", "value")
          .build();

  VirtualMachineTemplate validVirtualMachineTemplate;
  VirtualMachineTemplateBuilder validVirtualMachineTemplateBuilder;


  @Before
  public void setUp() throws Exception {
    validVirtualMachineTemplateBuilder =
        VirtualMachineTemplateBuilder.newBuilder().name(testName).hardwareFlavor(hardwareId)
            .image(imageId).location(locationId).templateOptions(templateOptions);
    validVirtualMachineTemplate = validVirtualMachineTemplateBuilder.build();
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorDisallowsNullImageId()
      throws Exception {
    validVirtualMachineTemplateBuilder.image(null).build();
  }

  @Test
  public void testConstructorAllowsNullName() throws Exception {
    VirtualMachineTemplate nullNameTemplate =
        validVirtualMachineTemplateBuilder.name(null).build();
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorDisallowsNullLocationId() throws Exception {
    validVirtualMachineTemplateBuilder.location(null).build();
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorDisallowsNullHardwareId() throws Exception {
    validVirtualMachineTemplateBuilder.hardwareFlavor(null).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDisallowsEmptyImageId() throws Exception {
    validVirtualMachineTemplateBuilder.image("").build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDisallowsEmptyLocationId() throws Exception {
    validVirtualMachineTemplateBuilder.location("").build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDisallowsEmptyHardwareId() throws Exception {
    validVirtualMachineTemplateBuilder.hardwareFlavor("").build();
  }

  @Test
  public void testName() throws Exception {
    //todo test that this works with a null name
    //todo test that this is really unique
    assertThat(validVirtualMachineTemplate.name(), is(notNullValue()));
    assertThat(validVirtualMachineTemplate.name(), containsString(testName));
  }


  @Test
  public void testImageId() throws Exception {
    assertThat(validVirtualMachineTemplate.imageId(), equalTo(imageId));
  }

  @Test
  public void testHardwareFlavorId() throws Exception {
    assertThat(validVirtualMachineTemplate.hardwareFlavorId(), equalTo(hardwareId));
  }

  @Test
  public void testLocationId() throws Exception {
    assertThat(validVirtualMachineTemplate.locationId(), equalTo(locationId));
  }

  @Test
  public void testTemplateOptions() throws Exception {
    assertThat(validVirtualMachineTemplate.templateOptions().get(), equalTo(templateOptions));
    assertThat(
        validVirtualMachineTemplateBuilder.templateOptions(null).build().templateOptions()
            .isPresent(), is(false));
  }
}
