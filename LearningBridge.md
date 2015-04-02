# LearningBridge sample #
Simple bridge
  * Very small code to show basic functionality
  * Bridges and learns mac addresses from each port
  * Simplified, so does not handle loops

## Run ##
```
sudo LD_LIBRARY_PATH=build/deploy/lib java -cp "build/deploy/lib/*" code.messy.sample.LearningBridge eth1 eth2
```

## Flow ##
![https://java-router.googlecode.com/svn/wiki/LearningBridge.png](https://java-router.googlecode.com/svn/wiki/LearningBridge.png)