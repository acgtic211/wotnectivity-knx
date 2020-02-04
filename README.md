# WoTnectivity-knx
Implementation of WoTnectivity Requester to manage KNX requests.

## Install WoTnectivity-knx

> :warning: **Requirements**: You need to have installed [Java 11+](https://openjdk.java.net/projects/jdk/11/) and [Maven 3.6.3+](http://maven.apache.org).

If you want to compile your own source code, you will need to add the generated .jar to the local mvn repository.

```console
foo@bar:~$ mvn install:install-file "-Dfile=wotnectivity-knx-0.0.1-ALPHA-SNAPSHOT.jar" "-DgroupId=es.ual.acg" "-DartifactId=wotnectivity-knx" "-Dversion=0.0.1-ALPHA-SNAPSHOT" "-Dpackaging=jar"
```

After installing it to your local maven repository you will only need to add it to the dependencies of the project where you want to use it.

```xml
<dependency>
    <groupId>es.ual.acg</groupId>
    <artifactId>wotnectivity-knx</artifactId>
    <version>0.0.1-ALPHA-SNAPSHOT</version>
</dependency>
```

## Use Example

In the next fragment of code you can see a use case of `KnxReq`.

```java
String address = "192.168.1.62";
String payload = "0.1";
KnxReq tester = new KnxReq();

var configuration = new HashMap<String, Object>();

configuration.put("requestType", "write");
configuration.put("dataType", "5.001");
configuration.put("group", "1/0/1");

try{
    this.tester.sendRequest(address, configuration, payload)
    var response = "Payload written correctly";
}catch(Exception e){
    System.out.println(e.getMessage());
}
```

The configuration parameter of the request needs to have at least three parameters: 

* `requestType` where the type of request will be declared. The request type can be of three different types:

    * `read`: Sends a request to read a value in the group address.
    * `write`: Write a value in the declared group address.
    * `subscribe`: Subscribes to the group address executing the behaviour of the listener declared.

* `group`: Defines the group address of the particular interaction that we want to execute.

* `dataType`: Declares the data type of the group address. To see the compatible dataTypes please visit [Calimero Core Library](https://github.com/calimero-project/calimero-core).

The example request is done with the default listener that is implemented in `KnxListener.java`. This listener just shows in the console the changes in the group address subscribed. This implementation is very straight forward, if you want to give your application other behaviour when listening messages in the queue you have to implement a clase like that one and declare your class when you instantiate the `KnxReq`.

```java
KnxReq tester = new KnxReq(new YourListenerKnx());
```

## Disclaimer

This library was developed with the [Calimero project](https://github.com/calimero-project/introduction) as a base.