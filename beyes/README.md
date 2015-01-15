TEST on Raspberry Pi and Firebase https://crackling-heat-6124.firebaseio.com/beyes/t150115.json

```
Linux raspberrypi 3.12.35+ #730 PREEMPT Fri Dec 19 18:31:24 GMT 2014 armv6l
pi@raspberrypi ~ $ java -version
java version "1.8.0"
Java(TM) SE Runtime Environment (build 1.8.0-b132)
Java HotSpot(TM) Client VM (build 25.0-b70, mixed mode)

pi@raspberrypi ~/beyes-0.0.1 $ cat bin/beyes | grep JVM_OPTS
DEFAULT_JVM_OPTS="-Dfb.token=xxxxxxxxxxxxxxxxxxxxxxxxxx"

pi@raspberrypi ~/beyes-0.0.1 $ bin/beyes &
```