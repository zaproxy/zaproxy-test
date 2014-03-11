package org.zaproxy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.zaproxy.zap.ZaproxyIntegrationTestSuite;
import org.zaproxy.zap.extension.ZapExtensionsIntegrationTestSuite;

/**
 * Suite for all tests of ZAP and ZAP Extensions. This is the top of the suite
 * stack, so there must never be a suite above this one.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({ ZaproxyIntegrationTestSuite.class, ZapExtensionsIntegrationTestSuite.class })
public final class ZapAllTestSuite {

}