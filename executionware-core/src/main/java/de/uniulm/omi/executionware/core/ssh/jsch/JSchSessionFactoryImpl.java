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
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import de.uniulm.omi.executionware.api.domain.LoginCredential;

/**
 * Created by daniel on 16.01.15.
 */
public class JSchSessionFactoryImpl implements JSchSessionFactory {

    private final JSch jSch;

    public JSchSessionFactoryImpl() {
        jSch = new JSch();
    }

    @Override
    public Session getSession(HostAndPort hostAndPort, LoginCredential loginCredential) throws JSchException {
        if (loginCredential.isPrivateKeyCredential()) {
            this.jSch.addIdentity(loginCredential.getPrivateKey().get());
        }
        Session session = this.jSch.getSession(loginCredential.getUsername(), hostAndPort.getHostText(), hostAndPort.getPort());
        if (loginCredential.isPasswordCredential()) {
            session.setPassword(loginCredential.getPassword().get());
        }
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        return session;
    }
}
