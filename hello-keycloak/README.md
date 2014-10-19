## ref

* [github keycloak](https://github.com/keycloak/keycloak)
* [jboss/keycloak Repository | Docker Hub Registry - Repositories of Docker Images](https://registry.hub.docker.com/u/jboss/keycloak/)

## log

```
$ sudo docker run -it -p 8080:8080 -p 9090:9090 jboss/keycloak
=========================================================================

  JBoss Bootstrap Environment

  JBOSS_HOME: /opt/jboss/wildfly

  JAVA: /usr/lib/jvm/java/bin/java

  JAVA_OPTS:  -server -Xms64m -Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true

=========================================================================

java.lang.IllegalArgumentException: Failed to instantiate class "org.jboss.logmanager.handlers.PeriodicRotatingFileHandler" for handler "FILE"
        at org.jboss.logmanager.config.AbstractPropertyConfiguration$ConstructAction.validate(AbstractPropertyConfiguration.java:119)

$ sudo docker images | grep jboss
jboss/keycloak              latest              f8a1d8781ab3        39 hours ago        1.015 GB

```
