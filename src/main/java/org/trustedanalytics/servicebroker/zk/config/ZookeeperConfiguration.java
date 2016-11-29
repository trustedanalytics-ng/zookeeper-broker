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
package org.trustedanalytics.servicebroker.zk.config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.trustedanalytics.cfbroker.store.zookeeper.service.ZookeeperClient;
import org.trustedanalytics.cfbroker.store.zookeeper.service.ZookeeperClientBuilder;
import org.trustedanalytics.hadoop.kerberos.KrbLoginManager;
import org.trustedanalytics.hadoop.kerberos.KrbLoginManagerFactory;
import org.trustedanalytics.servicebroker.framework.Profiles;
import org.trustedanalytics.servicebroker.framework.kerberos.KerberosProperties;
import sun.security.krb5.KrbException;

@Configuration
public class ZookeeperConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConfiguration.class);

  @Autowired
  private ExternalConfiguration config;

  @Autowired
  private KerberosProperties kerberosProperties;

  @Bean(initMethod = "init", destroyMethod = "destroy")
  @Profile(Profiles.KERBEROS)
  @Qualifier(Qualifiers.BROKER_INSTANCE)
  public ZookeeperClient getSecureZKClient() throws IOException, LoginException, KrbException, NoSuchAlgorithmException {
    LOGGER.info("Found kerberos profile configuration - trying to authenticate.");
    String user = config.getUser();

    KrbLoginManager loginManager =
        KrbLoginManagerFactory.getInstance().getKrbLoginManagerInstance(
            kerberosProperties.getKdc(), kerberosProperties.getRealm());
    loginManager.loginWithKeyTab(user, config.getKeytabPath());

    return new ZookeeperClientBuilder(config.getZkClusterHosts(), config.getUser(),
        config.getPassword(), config.getBrokerRootNode()).build();
  }

  @Bean(initMethod = "init", destroyMethod = "destroy")
  @Profile(Profiles.SIMPLE)
  @Qualifier(Qualifiers.BROKER_INSTANCE)
  public ZookeeperClient getInsecureZKClient() throws IOException, NoSuchAlgorithmException {
    LOGGER.info("Found non-kerberos profile configuration.");
    return new ZookeeperClientBuilder(config.getZkClusterHosts(), config.getUser(),
        config.getPassword(), config.getBrokerRootNode()).build();
  }

}
