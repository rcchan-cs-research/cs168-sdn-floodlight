This is a version of the Floodlight controller with support for OpenFlow
1.3. The original code from Big Switch was forked off and updated. This
repository is a work in progress.

The current code base is functional for the available applications /
modules like topology visualization, virtual networking, static flow
pushers, firewall, and loadbalancer.

# Maintainers
This code base is maintained by SDN Hub [a link](http://sdnhub.org). The
author is Srini Seetharaman (srini.seetharaman@gmail.com)

# Release notes

*Dependency on JOpenFlow*: This code depends on the JOpenFlow repository also maintained by SDN Hub [a link](http://bitbucket.org/sdnhub/jopenflow). However, there are slight differences with OFError and OFExperimenter. All "Experimenter" keyword is replaced with "Vendor" in this repository.

*Support for some features still under works*: Although JOpenFlow
suppotrs multiple tables, meters, groups and IPv6, the Floodlight
implementation does not use them to its advantage. 

*Unit tests still under development*
