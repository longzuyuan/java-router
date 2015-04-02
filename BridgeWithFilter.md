# BridgeWithFilter sample #
Bridge with filtering
  * Based on LearningBridge
  * Shows how to intercept packets
  * Prints all broadcast packets

## Run ##
```
sudo LD_LIBRARY_PATH=build/deploy/lib java -cp "build/deploy/lib/*" code.messy.sample.BridgeWithFilter eth1 eth2
```

## Flow ##
![https://java-router.googlecode.com/svn/wiki/BridgeWithFilter.png](https://java-router.googlecode.com/svn/wiki/BridgeWithFilter.png)