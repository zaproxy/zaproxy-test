package org.zaproxy.zap.extension.sse;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite for all integration tests of the Server-Sent Events ZAP Extensions.
 * Tests attached to this suite may depend on certain environmental settings or
 * preferences but should still be able to execute on any possible machine.
 */
@RunWith(Suite.class)
@SuiteClasses(EventStreamObserverIntegrationTest.class)
public final class SseExtensionIntegrationTestSuite {

}
