package org.zaproxy.zap.extension;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.zaproxy.zap.extension.sse.SseExtensionUnitTestSuite;

/**
 * Suite for all unit tests of ZAP Extensions. Tests attached to this suite must
 * run independent of any environmental settings or preferences on any possible
 * machine.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({SseExtensionUnitTestSuite.class})
public final class ZapExtensionsUnitTestSuite {

}
