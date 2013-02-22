package org.zaproxy.zap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all tests of ZAP.
 * 
 * ZaproxyIntegrationTestSuite is currently commented out as it needs a target server and probably needs reworking anyway ;) 
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({ZaproxyUnitTestSuite.class /*, ZaproxyIntegrationTestSuite.class */})
public final class ZaproxyAllTestSuite {

}
