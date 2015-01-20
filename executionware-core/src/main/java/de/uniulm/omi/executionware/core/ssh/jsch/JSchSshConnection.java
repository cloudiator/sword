/*
 * Copyright (c) 2014-2015 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.uniulm.omi.executionware.core.ssh.jsch;

import com.google.common.net.HostAndPort;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import de.uniulm.omi.executionware.api.domain.LoginCredential;
import de.uniulm.omi.executionware.api.exceptions.SshException;
import de.uniulm.omi.executionware.api.ssh.SshConnection;
import de.uniulm.omi.executionware.api.ssh.SshResponse;
import de.uniulm.omi.executionware.core.ssh.SshResponseImpl;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by daniel on 15.01.15.
 */
public class JSchSshConnection implements SshConnection {

    private final HostAndPort hostAndPort;
    private final LoginCredential loginCredential;
    private final JSchSessionFactory jSchSessionFactory;
    private Session session;

    public JSchSshConnection(final HostAndPort hostAndPort, final LoginCredential loginCredential, final JSchSessionFactory jSchSessionFactory) {
        this.hostAndPort = hostAndPort;
        this.loginCredential = loginCredential;
        this.jSchSessionFactory = jSchSessionFactory;
    }

    private Session getSession() throws JSchException {
        if (this.session == null || !this.session.isConnected()) {
            this.session = this.jSchSessionFactory.getSession(this.hostAndPort, this.loginCredential);
        }
        return this.session;
    }

    @Override
    public SshResponse execute(String command) {

        String result = "";
        int exitStatus;

        try {
            final ChannelExec exec = (ChannelExec) this.getSession().openChannel("exec");
            exec.setCommand(command);

            final InputStream inputStream = exec.getInputStream();

            exec.connect();

            final byte[] tmp = new byte[1024];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    result += new String(tmp, 0, i);
                }
                if (exec.isClosed()) {
                    if (inputStream.available() > 0) {
                        continue;
                    }
                    exitStatus = exec.getExitStatus();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            exec.disconnect();
        } catch (JSchException | IOException e) {
            throw new SshException(e);
        }
        return new SshResponseImpl(result, exitStatus);
    }

    @Override
    public void close() {
        this.session.disconnect();
    }
}
