package org.zaproxy.zap.authentication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all Authentication related integration tests of ZAP. Tests attached
 * to this suite may depend on certain environmental settings or preferences but
 * should still be able to execute on any possible machine.
 * 
 * @author cosmin stefan
 */
@RunWith(Suite.class)
@SuiteClasses({ FormBasedAuthenticationIntegrationTest.class,
		HttpAuthenticationIntegrationTest.class,
		ManualAuthenticationMethodIntegrationTest.class,
		FormBasedAuthenticationMethodIntegrationTest.class,
		HttpAuthenticationMethodIntegrationTest.class })
public final class AuthenticationIntegrationTestSuite {

}
