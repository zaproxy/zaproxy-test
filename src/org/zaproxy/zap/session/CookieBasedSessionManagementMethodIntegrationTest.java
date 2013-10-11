package org.zaproxy.zap.session;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.Constant;
import org.zaproxy.zap.session.CookieBasedSessionManagementMethodType.CookieBasedSessionManagementMethod;

@RunWith(MockitoJUnitRunner.class)
public class CookieBasedSessionManagementMethodIntegrationTest extends AbstractSessionManagementMethodIntegrationTest {

	private static CookieBasedSessionManagementMethodType type;

	@BeforeClass
	public static void classSetUp() {
		// Make sure Constant is loaded for messages
		Constant.getInstance();
		
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
