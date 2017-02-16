zookeeper-broker
================

Broker for creation znodes on Zookeeper.

# How to use it?
To use zookeeper-broker, you need to build it from sources configure, deploy, create instance and bind it to your app. Follow steps described below. 

## Build 
Run command for compile and package.: 
```
mvn clean package
```
Run optional command for create docker image:
```
mvn docker:build
```

## Plans

  * Standard : Create private znode within space shared across your organization

## Configure

###Profiles 
Each profile describes authentication used during communication with Hadoop, at least one is required: 

  * simple
  * kerberos
  
### Broker library
Broker library is java spring library, which simplifies broker store implementation. Currently zookeeper-broker uses zookeeper-broker store implementation, which stores information about every instance in secured znode.

* obligatory
  * simple
    * STORE_CLUSTER : zookeeper quorum address (example: host1:2181,host2:2181,host:2181)
    * STORE_USER : user used to authenticate with zookeeper broker store
    * STORE_PASSWORD : password for store user
  * kerberos
    * STORE_KEYTABPATH : path to the keytab file, which will be used to authenticate STORE_USER in kerberos

### Other
* obligatory
  * simple
    * USER_PASSWORD - password to interact with service broker Rest API
    * BASE_GUID - base id for catalog plan creation
    * CATALOG_SERVICENAME - service name in catalog (default: zookeeper)
    * CATALOG_SERVICEID - service id in catalog (default: zookeeper)
    * SYSTEM_USER : name of the regular user which will be used to create znodes
    * ZK_CLUSTER_URL - comma separated ip addresses of zookeeper nodes (in case of Kerberos, domain names should be used) for instance : host-1.domain:2181,host-2.domain:2181
    * ZK_BRK_ROOT - root directory where znode will be created
  * kerberos
    * KRB_REALM : Kerberos Realm (kerberos profile required)
    * KRB_KDC : Key Distribution Center Adddress (kerberos profile required)
    * SYSTEM_USER_KEYTAB_PATH : path to the keytab file, which will be used to authenticate SYSTEM_USER in kerberos


## Useful links

Offering template for TAP platform:
 * https://github.com/intel-data/tap-deploy/blob/master/roles/tap-marketplace-offerings/templates/zookeeper/offering.json

Broker library:
 * https://github.com/intel-data/broker-lib    

Cloud foundry resources that are helpful when troubleshooting service brokers : 
 * http://docs.cloudfoundry.org/services/api.html
 * http://docs.cloudfoundry.org/devguide/services/managing-services.html#update_service
 * http://docs.cloudfoundry.org/services/access-control.html

## On the app side

For spring applications use https://github.com/trustedanalytics/hadoop-spring-utils. 

For regular java applications use https://github.com/trustedanalytics/hadoop-utils. 
