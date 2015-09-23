/*
 * Copyright (c) 2014-2015 University of Ulm
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

import com.xebialabs.overthere.*;
import de.uniulm.omi.cloudiator.sword.api.remote.RemoteException;

import java.io.Closeable;

/**
 * Created by daniel on 23.09.15.
 */
public interface SwordOverthereConnection extends Closeable {

    OperatingSystemFamily getHostOperatingSystem() throws RemoteException;

    OverthereFile getFile(String hostPath) throws RemoteException;

    OverthereFile getFile(OverthereFile parent, String child) throws RemoteException;

    OverthereFile getTempFile(String nameTemplate) throws RemoteException;

    OverthereFile getTempFile(String prefix, String suffix) throws RemoteException;

    OverthereFile getWorkingDirectory() throws RemoteException;

    void setWorkingDirectory(OverthereFile workingDirectory) throws RemoteException;

    int execute(CmdLine commandLine) throws RemoteException;

    int execute(OverthereExecutionOutputHandler stdoutHandler,
        OverthereExecutionOutputHandler stderrHandler, CmdLine commandLine) throws RemoteException;

    OverthereProcess startProcess(CmdLine commandLine) throws RemoteException;

    boolean canStartProcess();

    @Override void close();

    ConnectionOptions getOptions();
}
