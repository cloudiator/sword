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

package de.uniulm.cloudiator.sword.drivers.simulation.state;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Supplier;
import com.google.inject.Singleton;
import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Singleton
public class VirtualMachineStore implements Supplier<Set<VirtualMachine>> {

  private final Map<String, VirtualMachine> storedMachines = new HashMap<>();

  @Override
  public Set<VirtualMachine> get() {
    return Collections.emptySet();
  }

  @Nullable
  public VirtualMachine get(String id) {
    return storedMachines.get(id);
  }

  public void delete(String id) {
    checkState(storedMachines.get(id) != null,
        String.format("VirtualMachine with the id %s does not exist.", id));
    storedMachines.remove(id);
  }

  /**
   * @param virtualMachine vm to update
   * @return fluent interface
   */
  public VirtualMachine store(VirtualMachine virtualMachine) {
    checkArgument(storedMachines.get(virtualMachine.id()) == null,
        String.format("VirtualMachine with id %s is already stored.", virtualMachine.id()));
    storedMachines.put(virtualMachine.id(), virtualMachine);
    return virtualMachine;
  }

  /**
   * @param virtualMachine vm to update
   * @return fluent interface
   */
  public VirtualMachine update(VirtualMachine virtualMachine) {

    final VirtualMachine stored = storedMachines.get(virtualMachine.id());
    checkArgument(stored != null,
        String.format("Virtual machine %s was never stored.", virtualMachine));

    storedMachines.put(virtualMachine.id(), virtualMachine);
    return virtualMachine;
  }
}
