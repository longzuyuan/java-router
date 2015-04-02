# RipRouter sample #
RipRouter sample can talk to other RipRouters to build up routing table
  * RipProcessor simplified the RIP protocol by just sending and receiving periodic update messages
  * RipProcessor does not handle loop current
  * DHCP and ICMP are also included for easier testing from network clients

## Run ##
```
sudo LD_LIBRARY_PATH=build/deploy/lib java -cp "build/deploy/lib/*" code.messy.sample.RipRouter eth1 10.1.0.1 24 eth2 10.2.0.1 24
```

## Flow diagram ##
![https://java-router.googlecode.com/svn/wiki/RipRouter.png](https://java-router.googlecode.com/svn/wiki/RipRouter.png)