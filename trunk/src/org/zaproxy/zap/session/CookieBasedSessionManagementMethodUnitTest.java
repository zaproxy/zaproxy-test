package org.zaproxy.zap.session;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.zaproxy.zap.session.CookieBasedSessionManagementMethodType.CookieBasedSessionManagementMethod;

@RunWith(MockitoJUnitRunner.class)
public class CookieBasedSessionManagementMethodUnitTest extends AbstractSessionManagementMethodUnitTest {

	private static CookieBasedSessionManagementMethodType type;

	@BeforeClass
	public static void classSetUp() {
		type = new CookieBasedSessionManagementMethodType();
	}

	@Test
	public void typeShouldReturnProperImplementation() {
		// Given
		SessionManagementMethod method = type.createSessionManagementMethod(0);
		// When/Then
		assertThat(method, notNullValue());
		assertTrue(method instanceof CookieBasedSessionManagementMethod);

	}

	@Override
	protected CookieBasedSessionManagementMethodType getMethodTypeImplementation() {
		return type;
	}

}
