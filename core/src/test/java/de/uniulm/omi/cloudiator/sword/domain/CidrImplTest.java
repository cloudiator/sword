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

import org.junit.Test;

/**
 * Created by daniel on 12.01.17.
 */
public class CidrImplTest {
  
  @Test(expected = IllegalArgumentException.class)
  public void cidrWithoutSlashRejected() {
    CidrImpl.of("0.0.0.0");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cidrWithMultipleSlashRejected() {
    CidrImpl.of("0.0.0.0/24/24");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cidrWithNonIntegerSlashRejected() {
    CidrImpl.of("0.0.0.0/test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cidrWithToHighSlashRejected() {
    CidrImpl.of("0.0.0.0/33");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cidrWithToLowSlashRejected() {
    CidrImpl.of("0.0.0.0/-1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptySlashIsRejected() {
    CidrImpl.of("0.0.0.0/");
  }

  @Test(expected = IllegalArgumentException.class)
  public void addressWithMissingOctetRejected() {
    CidrImpl.of("0.0.0/24");
  }

  @Test(expected = IllegalArgumentException.class)
  public void addressWithAdditionalOctetRejected() {
    CidrImpl.of("0.0.0.0.0/24");
  }

  @Test(expected = IllegalArgumentException.class)
  public void addressWithToLowOctetRejected() {
    CidrImpl.of("0.-1.0.0/24");
  }

  @Test(expected = IllegalArgumentException.class)
  public void addressWithToHighOctetRejected() {
    CidrImpl.of("0.256.0.0/24");
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyAddressRejected() {
    CidrImpl.of("/24");
  }

  @Test(expected = IllegalArgumentException.class)
  public void addressWithNonIntegerOctetRejected() {
    CidrImpl.of("0.String.0.0/24");
  }
}
