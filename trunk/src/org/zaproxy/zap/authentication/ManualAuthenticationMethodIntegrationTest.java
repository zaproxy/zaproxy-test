package org.zaproxy.zap.authentication;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.Constant;
import org.zaproxy.zap.authentication.ManualAuthenticationMethodType.ManualAuthenticationMethod;

@RunWith(MockitoJUnitRunner.class)
public class ManualAuthenticationMethodIntegrationTest extends AbstractAuthenticationMethodUnitTest {

	private static ManualAuthenticationMethodType type;

	@BeforeClass
	public static void classSetUp() {
		// Make sure Constant is loaded for messages
		Constant.getInstance();
		
		type = new ManualAuthenticationMethodType();
	}

	@Test
	public void typeShouldReturnProperImplementation() {
		// Given
		AuthenticationMethod method = type.createAuthenticationMethod(0);
		// When/Then
		assertThat(method, notNullValue());
		assertTrue(method instanceof ManualAuthenticationMethod);

	}

	@Override
	protected AuthenticationMethodType getMethodTypeImplementation() {
		return type;
	}

}
