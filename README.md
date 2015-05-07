# zaproxy-test
This project was exported from https://code.google.com/p/zaproxy-test

> Note that the plan is to move all of the tests into the relevant projects, at which point this project will be frozen and just maintained for historical purposes.

The zaproxy-test project contains all developer's test code for the OWASP Zed Attack Proxy (ZAP) and its extensions. This includes (but is not limited to)

* Unit Tests for ensuring the intended behavior of individual classes
* Integration Tests verifying correct interaction between classes or components
* Supporting classes like builders, matchers etc. 

## Goal

The main goal of zaproxy-test is to make sure that changes, fixes and refactorings on the ZAP code base can be done fearlessly and without causing harm to existing functionality! Adequate developer's tests and a clean code base can help keeping ZAP an active project with a low entrance barrier for new contributors!
Dependencies

This project is directly depending on the projects containing the classes and components under test

* [zaproxy](https://github.com/zaproxy/zaproxy)
* [zap-extensions](https://github.com/zaproxy/zap-extensions)

## Must reads before working on zaproxy-test

Please refer to the [GettingStarted](https://github.com/zaproxy/zaproxy-test/blob/wiki/GettingStarted.md) page to set-up zaproxy-test alongside its parent projects.

Prior to committing to zaproxy-test please make sure you have read the [TestingGuidelines](https://github.com/zaproxy/zaproxy-test/blob/wiki/TestingGuidelines.md).

_Happy testing!_
