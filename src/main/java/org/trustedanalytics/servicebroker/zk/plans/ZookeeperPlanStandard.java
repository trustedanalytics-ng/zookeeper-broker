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
package org.trustedanalytics.servicebroker.zk.plans;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.trustedanalytics.cfbroker.store.zookeeper.service.ZookeeperClient;
import org.trustedanalytics.servicebroker.framework.service.ServicePlanDefinition;
import org.trustedanalytics.servicebroker.zk.config.Qualifiers;
import org.trustedanalytics.servicebroker.zk.plans.binding.ZookeeperSimpleBindingOperations;

@Component("standard")
class ZookeeperPlanStandard implements ServicePlanDefinition {

  private static final String ZOOKEEPER_NODE = "broker";

  @Autowired
  @Qualifier(Qualifiers.BROKER_INSTANCE)
  private ZookeeperClient zookeeperClient;

  private ZookeeperSimpleBindingOperations zookeeperSimpleBindingOperations;

  @Autowired
  public ZookeeperPlanStandard(ZookeeperSimpleBindingOperations zookeeperSimpleBindingOperations) {
    this.zookeeperSimpleBindingOperations = zookeeperSimpleBindingOperations;
  }

  @Override
  public void provision(ServiceInstance serviceInstance, Optional<Map<String, Object>> parameters)
      throws ServiceInstanceExistsException, ServiceBrokerException {
    try {
      zookeeperClient.addZNode(String.format("%s/%s/%s", serviceInstance.getOrganizationGuid(),
          ZOOKEEPER_NODE, serviceInstance.getServiceInstanceId()), new byte[] {});
    } catch (IOException e) {
      throw new ServiceBrokerException(e);
    }
  }

  @Override
  public Map<String, Object> bind(ServiceInstance serviceInstance) throws ServiceBrokerException {
    String instanceId = serviceInstance.getServiceInstanceId();
    return zookeeperSimpleBindingOperations.createCredentialsMap(instanceId);
  }
}
