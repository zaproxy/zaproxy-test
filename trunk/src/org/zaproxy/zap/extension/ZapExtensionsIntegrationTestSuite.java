package org.zaproxy.zap.extension;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.zaproxy.zap.extension.sse.SseExtensionIntegrationTestSuite;

/**
 * Suite for all integration tests of ZAP Extensions. Tests attached to this
 * suite may depend on certain environmental settings or preferences but should
 * still be able to execute on any possible machine.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({SseExtensionIntegrationTestSuite.class})
public final class ZapExtensionsIntegrationTestSuite {

}
