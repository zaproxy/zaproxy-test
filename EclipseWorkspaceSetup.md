# Prerequisites #

You have an Eclipse workspace (e.g. /_workspace\_zap_) containing **both** of the following projects
  * [zaproxy](http://code.google.com/p/zaproxy/source/checkout)
  * [zap-extensions](http://code.google.com/p/zap-extensions/source/checkout)

# Adding zaproxy-test to your workspace #

  1. Checkout _zaproxy-test_...
    * ...[via command-line](http://code.google.com/p/zaproxy-test/source/checkout) and import it as a new Java project into your existing ZAP workspace (via _File_ > _New_ > _Project..._ > _Java Project_)
    * ...or via _SVN Repository Exploring_ perspective from Eclipse using https://zaproxy-test.googlecode.com/svn as _Repository root url_ and performing _Checkout..._ > _Checkout as project configured using the New Project Wizard_ on the 'trunk' folder
  1. Add all testing libraries contained in _zaproxy-test/lib_ to the projects .classpath (via _Properties_ > _Java Build Path_ > _Libraries_ > _Add JARs..._)
  1. Add the projects _zaproxy_ and/or _zap-extensions_ to the .classpath of _zaproxy-test_ (via _Properties_ > _Java Build Path_ > _Projects_ > _Add..._)
  1. Export all libraries from the .classpaths of _zaproxy_ and _zap-extensions_ (except zap.jar) to make them available for _zaproxy-test_ (via _Properties_ > _Java Build Path_ > _Order and Export_)
  1. In _zaproxy-test_ order the dependencies as depicted in the screenshot below (via _Properties_ > _Java Build Path_ > _Order and Export_)
  1. All classes in _zaproxy-test_ should now compile without errors

![http://zaproxy-test.googlecode.com/svn/wiki/images/zaproxy-test_build-path-order_eclipse.png](http://zaproxy-test.googlecode.com/svn/wiki/images/zaproxy-test_build-path-order_eclipse.png)