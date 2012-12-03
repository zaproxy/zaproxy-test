package org.zaproxy.zap;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.parosproxy.paros.core.proxy.HttpProxyConnectionIntegrationTest;
import org.parosproxy.paros.core.proxy.WebSocketsConnectionIntegrationTest;
import org.zaproxy.clientapi.core.ReflectedXssDetectionIntegrationTest;

/**
 * Suite for all integration tests of ZAP. Tests attached to this suite may
 * depend on certain environmental settings or preferences but should still be
 * able to execute on any possible machine.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({ ReflectedXssDetectionIntegrationTest.class,
		HttpProxyConnectionIntegrationTest.class,
		WebSocketsConnectionIntegrationTest.class,
		DaemonWaveIntegrationTest.class })
public final class ZaproxyIntegrationTestSuite {

}
