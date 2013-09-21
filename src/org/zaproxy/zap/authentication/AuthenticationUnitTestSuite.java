package org.zaproxy.zap.authentication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all Authentication related unit tests of ZAP. Tests attached to this suite must run
 * independent of any environmental settings or preferences on any possible machine.
 * 
 * @author cosminstefanxp@gmail.com
 */
@RunWith(Suite.class)
@SuiteClasses({
		// In alphabetical order
		AuthenticationMethodIndicatorsUnitTest.class,
		ManualAuthenticationMethodUnitTest.class})
public final class AuthenticationUnitTestSuite {

}
