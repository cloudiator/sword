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

import com.google.common.io.ByteStreams;
import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.OverthereFile;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnection;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionResponse;
import de.uniulm.omi.cloudiator.sword.remote.RemoteException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by daniel on 19.08.15.
 */
//todo remove duplicated code
public class OverthereWinRMConnection implements RemoteConnection {

  private final SwordOverthereConnection delegate;

  OverthereWinRMConnection(SwordOverthereConnection delegate) {
    this.delegate = delegate;
  }

  @Override
  public RemoteConnectionResponse executeCommand(String command)
      throws RemoteException {
    //TODO: check why the Overthere encoding doesn't work for Windows and Linux!
    //split the command into separate commands otherwise Windows commands can't be recognized

    String[] splittedCommands = command.split("\\s+");

    OverthereRemoteConnectionResponse response = new OverthereRemoteConnectionResponse();
    response.setExitStatus(delegate.execute(response.getStdOutExecutionOutputHandler(),
        response.getStdErrExecutionOutputHandler(), CmdLine.build(splittedCommands)));
    return response;

  }

  @Override
  public File downloadFile(String path) throws RemoteException {
    OverthereFile overthereFile = this.delegate.getFile(path);
    //todo refactor, is duplicate of unix logic.
    try {
      File tempFile = File.createTempFile("sword.overthere", "tmp");
      tempFile.deleteOnExit();
      ByteStreams.copy(overthereFile.getInputStream(), new FileOutputStream(tempFile));
      return tempFile;
    } catch (IOException e) {
      throw new RemoteException(e);
    }
  }

  @Override
  public int writeFile(String pathAndFilename, String content, boolean setExecutable)
      throws RemoteException {

    //todo: ignores setExecutable?

    //spilt at newline
    String[] splittedContent = content.split("\\r?\\n");

    //use powershell Add-Content to avoid line break issues with windows in config file
    for (String line : splittedContent) {
      this.executeCommand(
          "powershell -command  Add-Content " + pathAndFilename + " '" + line + "'");
    }

    //todo check this return value
    return 0;
  }

  @Override
  public void close() {
    delegate.close();
  }
}
