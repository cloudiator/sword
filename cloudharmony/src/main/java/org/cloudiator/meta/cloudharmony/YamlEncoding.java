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

package org.cloudiator.meta.cloudharmony;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;

public class YamlEncoding implements ObjectEncoding {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

  @Override
  public String encode(Object o) throws EncodingException {
    try {
      return OBJECT_MAPPER.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new EncodingException(e);
    }
  }

  @Override
  public <T> T decode(String s, Class<T> tClass) throws EncodingException {
    try {
      return OBJECT_MAPPER.readValue(s, tClass);
    } catch (IOException e) {
      throw new EncodingException(e);
    }
  }
}
