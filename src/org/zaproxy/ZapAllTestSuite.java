package org.zaproxy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.zaproxy.zap.ZaproxyAllTestSuite;
import org.zaproxy.zap.extension.ZapExtensionsAllTestSuite;

/**
 * Suite for all tests of ZAP and ZAP Extensions. This is the top of the suite
 * stack, so there must never be a suite above this one.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({ ZaproxyAllTestSuite.class, ZapExtensionsAllTestSuite.class })
public final class ZapAllTestSuite {

}