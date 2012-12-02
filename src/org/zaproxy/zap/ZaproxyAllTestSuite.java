package org.zaproxy.zap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all tests of ZAP.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({ZaproxyUnitTestSuite.class, ZaproxyIntegrationTestSuite.class })
public final class ZaproxyAllTestSuite {

}
