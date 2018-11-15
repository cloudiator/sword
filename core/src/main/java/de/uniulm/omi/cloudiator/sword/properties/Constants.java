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

package de.uniulm.omi.cloudiator.sword.properties;

/**
 * Created by daniel on 27.02.15.
 */
public class Constants {

  public static final String IGNORE_LOGIN_PASSWORD = "sword.login.password.ignore";
  public static final String IGNORE_LOGIN_USERNAME = "sword.login.username.ignore";
  public static final String IGNORE_LOGIN_KEYPAIR = "sword.login.keypair.ignore";
  public static final String SSH_FIXED_WAIT_SECONDS = "sword.ssh.fixed.wait.seconds";
  public static final String SSH_MAX_RETRIES = "sword.ssh.max.retries";
  public static final String REQUEST_TIMEOUT = "sword.request.timeout";
  public static final String SWORD_REGIONS = "sword.regions";
  public static final String DEFAULT_SECURITY_GROUP = "sword.default.securityGroup";
  public static final String PROVIDERID_WHITELIST = "sword.providerId.whitelist";
  public static final String PROVIDERID_BLACKLIST = "sword.providerId.blacklist";

  private Constants() {
    throw new AssertionError("constants class");
  }


}
