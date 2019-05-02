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

import com.google.common.base.MoreObjects.ToStringHelper;
import de.uniulm.omi.cloudiator.domain.OperatingSystem;
import javax.annotation.Nullable;

/**
 * Created by daniel on 01.12.14.
 */
public class ImageImpl extends ResourceImpl implements Image {

  private final OperatingSystem operatingSystem;

  ImageImpl(String id, String providerId, String name, @Nullable Location location,
      OperatingSystem operatingSystem) {
    super(id, providerId, name, location);
    checkNotNull(operatingSystem, "operating system is null");
    this.operatingSystem = operatingSystem;
  }

  @Override
  public OperatingSystem operatingSystem() {
    return operatingSystem;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    ImageImpl image = (ImageImpl) o;

    return operatingSystem.equals(image.operatingSystem);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + operatingSystem.hashCode();
    return result;
  }

  @Override
  protected ToStringHelper toStringHelper() {
    return super.toStringHelper().add("os", operatingSystem);
  }
}
