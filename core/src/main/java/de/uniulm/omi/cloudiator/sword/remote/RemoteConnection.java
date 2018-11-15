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

package de.uniulm.omi.cloudiator.sword.remote;

import java.io.Closeable;
import java.io.File;

/**
 * Represents a connection to a remote node, e.g. via SSH.
 */
public interface RemoteConnection extends Closeable {

  /**
   * @param command the command to be executed by the RemoteConnection
   * @return the {@link RemoteConnectionResponse} of the command.
   * @throws RemoteException if an error occurs during communication.
   */
  RemoteConnectionResponse executeCommand(String command) throws RemoteException;

  File downloadFile(String path) throws RemoteException;

  /**
   * Writes content to the specified file, creates the file if it doesn't exist
   *
   * @param pathAndFilename to the file
   * @param content of the file to write
   * @param setExecutable if the file is executable
   * @return the exit value when the file is written
   * @throws RemoteException if an error occurs during communication.
   */
  int writeFile(String pathAndFilename, String content, boolean setExecutable)
      throws RemoteException;

  @Override
  void close();
}
