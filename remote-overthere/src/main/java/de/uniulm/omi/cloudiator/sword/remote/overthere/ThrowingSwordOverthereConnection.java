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

package de.uniulm.omi.cloudiator.sword.remote.overthere;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereExecutionOutputHandler;
import com.xebialabs.overthere.OverthereFile;
import com.xebialabs.overthere.OverthereProcess;
import com.xebialabs.overthere.RuntimeIOException;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;
import java.util.concurrent.Callable;

/**
 * Created by daniel on 23.09.15.
 */
public class ThrowingSwordOverthereConnection implements SwordOverthereConnection {

  private final OverthereConnection overthereConnection;

  ThrowingSwordOverthereConnection(OverthereConnection overthereConnection) {
    this.overthereConnection = overthereConnection;
  }

  private <T> T callAndRethrowAsRemoteException(Callable<T> callable) throws RemoteException {
    try {
      // try the execution
      return callable.call();
    } catch (RuntimeIOException e) {
      // if a runtime exception occurred this is normally a communication error
      // we therefore give the possibility to react on this...
      throw new RemoteException(e);
    } catch (Exception e) {
      // something else happened, we rethrow this as runtime exception
      // as we currently have no clue what to do...
      throw new RuntimeException(e);
    }
  }

  @Override
  public OperatingSystemFamily getHostOperatingSystem() throws RemoteException {
    return callAndRethrowAsRemoteException(overthereConnection::getHostOperatingSystem);
  }

  @Override
  public OverthereFile getFile(String hostPath) throws RemoteException {
    return callAndRethrowAsRemoteException(() -> overthereConnection.getFile(hostPath));
  }

  @Override
  public OverthereFile getFile(OverthereFile parent, String child)
      throws RemoteException {
    return callAndRethrowAsRemoteException(() -> overthereConnection.getFile(parent, child));
  }

  @Override
  public OverthereFile getTempFile(String nameTemplate) throws RemoteException {
    return callAndRethrowAsRemoteException(() -> overthereConnection.getTempFile(nameTemplate));
  }

  @Override
  public OverthereFile getTempFile(String prefix, String suffix)
      throws RemoteException {
    return callAndRethrowAsRemoteException(
        () -> overthereConnection.getTempFile(prefix, suffix));
  }

  @Override
  public OverthereFile getWorkingDirectory() throws RemoteException {
    return callAndRethrowAsRemoteException(overthereConnection::getWorkingDirectory);
  }

  @Override
  public void setWorkingDirectory(OverthereFile workingDirectory)
      throws RemoteException {
    callAndRethrowAsRemoteException(() -> {
      overthereConnection.setWorkingDirectory(workingDirectory);
      return null;
    });
  }

  @Override
  public int execute(CmdLine commandLine) throws RemoteException {
    return callAndRethrowAsRemoteException(() -> overthereConnection.execute(commandLine));
  }

  @Override
  public int execute(OverthereExecutionOutputHandler stdoutHandler,
      OverthereExecutionOutputHandler stderrHandler, CmdLine commandLine) throws RemoteException {
    return callAndRethrowAsRemoteException(
        () -> overthereConnection.execute(stdoutHandler, stderrHandler, commandLine));
  }

  @Override
  public OverthereProcess startProcess(CmdLine commandLine) throws RemoteException {
    return callAndRethrowAsRemoteException(() -> overthereConnection.startProcess(commandLine));
  }

  @Override
  public boolean canStartProcess() {
    return overthereConnection.canStartProcess();
  }

  @Override
  public void close() {
    overthereConnection.close();
  }

  @Override
  public ConnectionOptions getOptions() {
    return overthereConnection.getOptions();
  }
}
