package org.zaproxy.zap.authentication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all Authentication related unit tests of ZAP. Tests attached to
 * this suite must run independent of any environmental settings or preferences
 * on any possible machine.
 * 
 * @author cosmin stefan
 */
@RunWith(Suite.class)
@SuiteClasses({ AuthenticationMethodIndicatorsUnitTest.class })
public final class AuthenticationUnitTestSuite {

}
