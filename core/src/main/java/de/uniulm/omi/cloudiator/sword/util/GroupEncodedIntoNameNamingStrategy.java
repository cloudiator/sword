/*
 * Copyright (c) 2014-2016 University of Ulm
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

package de.uniulm.omi.cloudiator.sword.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.logging.InjectLogger;
import de.uniulm.omi.cloudiator.sword.logging.Logger;
import de.uniulm.omi.cloudiator.sword.logging.NullLogger;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;

/**
 * Created by daniel on 12.08.16.
 */
public class GroupEncodedIntoNameNamingStrategy implements NamingStrategy {

  private final static String DELIMITER = "-";
  private final String nodeGroup;
  private final PrefixGenerator prefixGenerator;
  private @InjectLogger
  Logger LOGGER = new NullLogger();


  @Inject
  GroupEncodedIntoNameNamingStrategy(Cloud cloud) {
    checkNotNull(cloud);
    this.nodeGroup = cloud.configuration().nodeGroup();
    this.prefixGenerator = new RandomPrefixGenerator();
  }

  @Override
  public String generateUniqueNameBasedOnName(@Nullable String name) {

    final String uniquePrefix = generateUniquePrefix();
    final String uniqueName;

    if (name == null) {
      uniqueName = uniquePrefix;
    } else {
      checkArgument(!name.isEmpty(), "name is empty.");
      uniqueName = uniquePrefix + DELIMITER + name;
    }

    LOGGER.debug(String.format("Generated unique name for name %s: %s", name, uniqueName));

    if (uniqueName.length() >= 64) {
      LOGGER.warn(
          "Generated unique name exceeds/matches host name limit of 64 characters, this may cause"
              + " problems with virtual machines. Actual length is " + uniqueName.length());
    }
    return uniqueName;
  }

  @Override
  public String generateNameBasedOnName(String name) {
    checkNotNull(name, "name is null");
    checkArgument(!name.isEmpty(), "name is empty");
    return nodeGroup + DELIMITER + name;
  }

  @Override
  public Predicate<String> belongsToNamingGroup() {
    return s -> s != null && s.startsWith(nodeGroup);
  }

  private String generateUniquePrefix() {
    return nodeGroup + DELIMITER + prefixGenerator.generatePrefix();
  }

  private interface PrefixGenerator {

    String generatePrefix();
  }

  private static class UUIDPrefixGenerator implements PrefixGenerator {

    @Override
    public String generatePrefix() {
      return UUID.randomUUID().toString();
    }
  }

  private static class RandomPrefixGenerator implements PrefixGenerator {

    private RandomString randomString = new RandomString(5);

    @Override
    public String generatePrefix() {
      return randomString.nextString();
    }

    private static class RandomString {

      private static final char[] symbols;

      static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) {
          tmp.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ++ch) {
          tmp.append(ch);
        }
        symbols = tmp.toString().toCharArray();
      }

      private static final Random RANDOM = new Random();

      private final char[] buf;

      private RandomString(int length) {
        if (length < 1) {
          throw new IllegalArgumentException("length < 1: " + length);
        }
        buf = new char[length];
      }

      private String nextString() {
        for (int idx = 0; idx < buf.length; ++idx) {
          buf[idx] = symbols[RANDOM.nextInt(symbols.length)];
        }
        return new String(buf);
      }
    }
  }
}
