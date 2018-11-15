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

import de.uniulm.omi.cloudiator.sword.domain.VirtualMachine;
import de.uniulm.omi.cloudiator.sword.service.ComputeService;
import java.util.function.Supplier;

/**
 * An example showing the steps needed to create a virtual machine using the compute service.
 */
public class CreateVirtualMachineExample implements Supplier<VirtualMachine> {

  private final ComputeService computeService;

  public CreateVirtualMachineExample(
      ComputeService computeService) {
    this.computeService = computeService;
  }

  @Override
  public VirtualMachine get() {
    //todo fix example
    return computeService.createVirtualMachine(null);
  }
}
