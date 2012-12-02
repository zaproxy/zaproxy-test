package org.zaproxy.zap.extension;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all tests of ZAP Extensions.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({ZapExtensionsUnitTestSuite.class, ZapExtensionsIntegrationTestSuite.class })
public final class ZapExtensionsAllTestSuite {

}
