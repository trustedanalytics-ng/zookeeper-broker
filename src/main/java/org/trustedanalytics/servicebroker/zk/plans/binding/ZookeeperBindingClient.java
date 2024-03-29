/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.servicebroker.zk.plans.binding;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trustedanalytics.servicebroker.zk.config.ExternalConfiguration;

@Component
class ZookeeperBindingClient implements ZookeeperSimpleBindingOperations {

  private static final String ZNODE_KEY = "zk.node";

  private final String userspacePathTemplate;

  @Autowired
  public ZookeeperBindingClient(ExternalConfiguration configuration) {
    this.userspacePathTemplate = configuration.getBrokerRootNode();
  }

  @Override
  public Map<String, Object> createCredentialsMap(String instanceId) {
    Map<String, Object> credentialsCopy = new HashMap<>();
    credentialsCopy.put(ZNODE_KEY, String.format("%s/%s", userspacePathTemplate, instanceId));
    return credentialsCopy;
  }

}
