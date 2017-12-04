# Messaging test client
Projects contains MDBs and rest endpoints to deploy as a single war on a full profile compliant application server.

## Prerequisites
* Java 7 (annoying but needed to make tests with clients which have no Java 8 / Java 9 support)
* Maven 3
* A running message broker that understands HornetQ Core protocol

## Build the application
Build with maven in the project root directory
```
mvn package
```

## Configuring the broker
The destinations used by the tests must be created manually on the broker side.  

1. Connect to the JBoss CLI
    ```
    ben@blackbox /opt/jboss-eap-7.1/bin
    > $ ./jboss-cli.sh -c
    ```

2. Execute the following commands in the CLI to create and configure the destinations  
    *JBoss EAP >= 7.0*
    ```
    jms-queue add --queue-address=Queue1 --entries=[java:/jms/queue/queue-1] --durable=true
    /subsystem=messaging-activemq/server=default/address-setting=jms.queue.Queue1/:add(redelivery-delay=30000,max-delivery-attempts=2)
    jms-queue add --queue-address=Queue2 --entries=[java:/jms/queue/queue-2] --durable=true
    /subsystem=messaging-activemq/server=default/address-setting=jms.queue.Queue2/:add(redelivery-delay=2000,max-delivery-attempts=5)
    jms-queue add --queue-address=Queue3 --entries=[java:/jms/queue/queue-3] --durable=true
    /subsystem=messaging-activemq/server=default/address-setting=jms.queue.Queue3/:add(redelivery-delay=0,max-delivery-attempts=1)
    jms-queue add --queue-address=Queue4 --entries=[java:/jms/queue/queue-4] --durable=true
    /subsystem=messaging-activemq/server=default/address-setting=jms.queue.Queue4/:add(redelivery-delay=30000,max-delivery-attempts=2)
    
    ```
    *JBoss EAP <= 7*  
    ```
    jms-queue add --queue-address=Queue1 --entries=[java:/jms/queue/queue-1] --durable=true
    /subsystem=messaging/hornetq-server=default/address-setting=jms.queue.Queue1/:add(redelivery-delay=30000,max-delivery-attempts=2)
    jms-queue add --queue-address=Queue2 --entries=[java:/jms/queue/queue-2] --durable=true
    /subsystem=messaging/hornetq-server=default/address-setting=jms.queue.Queue2/:add(redelivery-delay=2000,max-delivery-attempts=5)
    jms-queue add --queue-address=Queue3 --entries=[java:/jms/queue/queue-3] --durable=true
    /subsystem=messaging/hornetq-server=default/address-setting=jms.queue.Queue3/:add(redelivery-delay=0,max-delivery-attempts=1)
    jms-queue add --queue-address=Queue4 --entries=[java:/jms/queue/queue-4] --durable=true
    /subsystem=messaging/hornetq-server=default/address-setting=jms.queue.Queue4/:add(redelivery-delay=30000,max-delivery-attempts=2)
    ```
    

## Example configuration
Working standalone configs for Broker (EAP 7.1.Beta) and client (EAP 6.4)
```
/messaging-test-client/src/main/jboss-configuration
```

## Exposed endpoints
* Show persisted records in the H2 database
    http://localhost:8080/messaging-test-client/records

* Browse Queues without consuming the messages
    http://localhost:8080/messaging-test-client/inspect?queue={queue-name}

* Testcase3
    http://localhost:8080/messaging-test-client/testcase3?rollback={true|false}
