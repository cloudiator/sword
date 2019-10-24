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

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6Range;
import java.util.Objects;

/**
 * Created by daniel on 04.07.16.
 */
public class CidrImpl implements Cidr {


  public static final Cidr ALL_IP4 = new CidrImpl("0.0.0.0/0");
  public static final Cidr ALL_IP6 = new CidrImpl("::/0");

  private final String cidr;

  private CidrImpl(String cidr) {
    checkNotNull(cidr, "cidr is null");
    final boolean validate = validate(cidr);

    if (!validate) {
      throw new IllegalArgumentException("Cidr " + cidr + " is illegal.");
    }

    this.cidr = cidr;
  }

  public static Cidr of(String cidr) {
    return new CidrImpl(cidr);
  }

  private boolean validate(String cidr) {

    boolean isV4 = false;
    boolean isV6 = false;

    try {
      Ipv4Range.parseCidr(cidr);
      isV4 = true;
    } catch (IllegalArgumentException e) {
      //left empty
    }

    try {
      Ipv6Range.parseCidr(cidr);
      isV6 = true;
    } catch (IllegalArgumentException e) {
      //do nothing
    }

    return isV4 || isV6;
  }

  @Override
  public String toString() {
    return cidr();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CidrImpl cidr1 = (CidrImpl) o;
    return cidr.equals(cidr1.cidr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cidr);
  }

  @Override
  public String cidr() {
    return cidr;
  }
}
