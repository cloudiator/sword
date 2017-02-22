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

import com.xebialabs.overthere.OverthereExecutionOutputHandler;
import de.uniulm.omi.cloudiator.sword.remote.RemoteConnectionResponse;

/**
 * Created by daniel on 22.09.15.
 */
public class OverthereRemoteConnectionResponse implements RemoteConnectionResponse {

    private final OverthereExecutionOutputHandler stdOutHandler;
    private final OverthereExecutionOutputHandler stdErrHandler;
    private String stdOut = "";
    private String stdErr = "";
    private int exitStatus;

    OverthereRemoteConnectionResponse() {
        stdOutHandler = new OverthereExecutionOutputHandler() {
            @Override public void handleChar(char c) {
                stdOut += c;
            }

            @Override public void handleLine(String line) {
                stdOut += line;
            }
        };


        stdErrHandler = new OverthereExecutionOutputHandler() {
            @Override public void handleChar(char c) {
                stdErr += c;
            }

            @Override public void handleLine(String line) {
                stdErr += line;
            }
        };
    }

    OverthereExecutionOutputHandler getStdOutExecutionOutputHandler() {
        return stdOutHandler;
    }

    OverthereExecutionOutputHandler getStdErrExecutionOutputHandler() {
        return stdErrHandler;
    }

    @Override public String stdOut() {
        return stdOut;
    }

    @Override public String stdErr() {
        return stdErr;
    }

    @Override public int getExitStatus() {
        return exitStatus;
    }

    void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }
}
