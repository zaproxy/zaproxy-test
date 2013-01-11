package org.zaproxy.zap.extension.sse;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all unit tests of the Server-Sent Events ZAP Extension. Tests
 * attached to this suite must run independent of any environmental settings or
 * preferences on any possible machine.
 */
@RunWith(Suite.class)
@SuiteClasses({EventStreamProxyUnitTest.class, EventStreamListenerUnitTest.class})
public final class SseExtensionUnitTestSuite {

}
