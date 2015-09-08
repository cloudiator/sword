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

import de.uniulm.omi.cloudiator.sword.api.remote.RemoteConnection;

/**
 * Created by daniel on 08.09.15.
 */
public class AutoClosingRemoteConnection implements RemoteConnection {

    private final RemoteConnection remoteConnection;

    public AutoClosingRemoteConnection(RemoteConnection remoteConnection) {
        this.remoteConnection = remoteConnection;
    }

    @Override public void close() {
        this.remoteConnection.close();
    }

    @Override public int executeCommand(String command) {
        try {
            return remoteConnection.executeCommand(command);
        } catch (Exception e) {
            remoteConnection.close();
            throw e;
        }
    }

    @Override public int writeFile(String pathAndFilename, String content, boolean setExecutable) {
        try {
            return remoteConnection.writeFile(pathAndFilename, content, setExecutable);
        } catch (Exception e) {
            remoteConnection.close();
            throw e;
        }
    }
}
