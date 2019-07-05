/*
 * Copyright (c) 2014-2019 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.strategy;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import de.uniulm.omi.cloudiator.domain.OperatingSystem;
import de.uniulm.omi.cloudiator.domain.OperatingSystemFamily;
import de.uniulm.omi.cloudiator.domain.OperatingSystems;
import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import java.util.UUID;
import org.junit.Test;

public class NameSubstringBasedOperatingSystemDetectionStrategyTest {

  private static final NameSubstringBasedOperatingSystemDetectionStrategy SUT = new NameSubstringBasedOperatingSystemDetectionStrategy();

  @Test
  public void testUbuntu() {
    String ubuntu = "ubuntu";
    final OperatingSystem apply = apply(ubuntu);
    assertThat(apply.operatingSystemFamily(), equalTo(OperatingSystemFamily.UBUNTU));
  }

  @Test
  public void testUbuntuWithVersion() {
    String ubuntu = "Ubuntu Server 14.04.5 LTS";
    final OperatingSystem apply = apply(ubuntu);
    assertThat(apply.operatingSystemFamily(), equalTo(OperatingSystemFamily.UBUNTU));
    assertThat(apply.operatingSystemVersion().version(), equalTo(1404));
  }

  @Test
  public void testUbuntuWithShortVersion() {
    String ubuntu = "ubuntu-1604";
    final OperatingSystem apply = apply(ubuntu);
    assertThat(apply.operatingSystemFamily(), equalTo(OperatingSystemFamily.UBUNTU));
    assertThat(apply.operatingSystemVersion().version(), equalTo(1604));
  }

  @Test
  public void testUbuntuTwoVersionsContained() {
    String ubuntu = "ubuntu-1804";
    final OperatingSystem apply = apply(ubuntu);
    assertThat(apply.operatingSystemFamily(), equalTo(OperatingSystemFamily.UBUNTU));
    assertThat(apply.operatingSystemVersion().version(), equalTo(1804));
  }

  @Test
  public void testUbuntuTwoVersionsContainedWithDot() {
    String ubuntu = "Ubuntu 18.04";
    final OperatingSystem apply = apply(ubuntu);
    assertThat(apply.operatingSystemFamily(), equalTo(OperatingSystemFamily.UBUNTU));
    assertThat(apply.operatingSystemVersion().version(), equalTo(1804));
  }

  private Image generate(String name) {
    checkNotNull(name, "name is null");
    return ImageBuilder.newBuilder().id(UUID.randomUUID().toString())
        .providerId(UUID.randomUUID().toString()).name(name).os(OperatingSystems.unknown()).build();
  }

  private OperatingSystem apply(String name) {
    return SUT.detectOperatingSystem(generate(name));
  }

}
