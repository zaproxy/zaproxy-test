package org.zaproxy.zap.authentication;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.Constant;
import org.zaproxy.zap.TestWebAppUtils;
import org.zaproxy.zap.authentication.HttpAuthenticationMethodType.HttpAuthenticationMethod;
import org.zaproxy.zap.session.SessionManagementMethod;
import org.zaproxy.zap.session.WebSession;
import org.zaproxy.zap.users.User;

/**
 * A set of unit tests for {@link HttpAuthenticationMethod}. In order for the test to properly work,
 * the following configuration must be done:
 * <ul>
 * <li>The test-webapp (test-webapp/zap-test-webapp.war) should be started and deployed on localhost
 * (more specifically deployed on the path specified by {@link TestWebAppUtils#APP_BASE_URL}).</li>
 * </ul>
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpAuthenticationMethodIntegrationTest extends AbstractAuthenticationMethodUnitTest {

	private static HttpAuthenticationMethodType type;

	@BeforeClass
	public static void classSetup() {
		// Make sure Constant is loaded for messages
		Constant.getInstance();

		type = new HttpAuthenticationMethodType();
	}

	@Override
	protected AuthenticationMethodType getMethodTypeImplementation() {
		return type;
	}

	private User getMockedUser() {
		UsernamePasswordAuthenticationCredentials mockedCredentials = Mockito
				.mock(UsernamePasswordAuthenticationCredentials.class);
		when(mockedCredentials.getUsername()).thenReturn(TestWebAppUtils.HTTP_AUTH_USERNAME);
		when(mockedCredentials.getPassword()).thenReturn(TestWebAppUtils.HTTP_AUTH_USERPASS);

		User mockedUser = Mockito.mock(User.class);
		when(mockedUser.getAuthenticationCredentials()).thenReturn(mockedCredentials);

		return mockedUser;
	}

	private HttpAuthenticationMethod createMethod() {
		HttpAuthenticationMethod method = type.createAuthenticationMethod(0);
		method.hostname = TestWebAppUtils.HTTP_AUTH_HOSTNAME;
		method.realm = TestWebAppUtils.HTTP_AUTH_REALM;
		method.port = TestWebAppUtils.HTTP_AUTH_PORT;
		return method;
	}

	@Test
	public void typeShouldReturnProperImplementation() {
		// Given
		AuthenticationMethod method = type.createAuthenticationMethod(0);
		// When/Then
		assertThat(method, notNullValue());
		assertTrue(method instanceof HttpAuthenticationMethod);
	}

	@Test
	public void shouldSetDefaultValuesForAuthenticationFields() {
		// Given
		HttpAuthenticationMethod method = type.createAuthenticationMethod(0);
		method.hostname = TestWebAppUtils.HTTP_AUTH_HOSTNAME;

		HttpState state = new HttpState();

		WebSession mockedSession = Mockito.mock(WebSession.class);
		when(mockedSession.getHttpState()).thenReturn(state);

		SessionManagementMethod mockedSessionManagementMethod = Mockito.mock(SessionManagementMethod.class);
		doReturn(mockedSession).when(mockedSessionManagementMethod).createEmptyWebSession();

		User mockedUser = getMockedUser();

		// When
		method.authenticate(mockedSessionManagementMethod, mockedUser.getAuthenticationCredentials(),
				mockedUser);
		AuthScope scope = new AuthScope(TestWebAppUtils.HTTP_AUTH_HOSTNAME, 80, AuthScope.ANY_REALM);
		UsernamePasswordCredentials configuredCredentials = (UsernamePasswordCredentials) state
				.getCredentials(scope);

		// Then
		assertNotNull(configuredCredentials);
	}

	@Test
	public void shouldProperlySetAuthenticationInHttpState() {
		// Given
		HttpState state = new HttpState();

		WebSession mockedSession = Mockito.mock(WebSession.class);
		when(mockedSession.getHttpState()).thenReturn(state);

		SessionManagementMethod mockedSessionManagementMethod = Mockito.mock(SessionManagementMethod.class);
		doReturn(mockedSession).when(mockedSessionManagementMethod).createEmptyWebSession();

		User mockedUser = getMockedUser();

		// When
		createMethod().authenticate(mockedSessionManagementMethod, mockedUser.getAuthenticationCredentials(),
				mockedUser);
		AuthScope scope = new AuthScope(TestWebAppUtils.HTTP_AUTH_HOSTNAME, TestWebAppUtils.HTTP_AUTH_PORT,
				TestWebAppUtils.HTTP_AUTH_REALM);
		UsernamePasswordCredentials configuredCredentials = (UsernamePasswordCredentials) state
				.getCredentials(scope);

		// Then
		assertNotNull(configuredCredentials);
		assertEquals(configuredCredentials.getUserName(), TestWebAppUtils.HTTP_AUTH_USERNAME);
		assertEquals(configuredCredentials.getPassword(), TestWebAppUtils.HTTP_AUTH_USERPASS);
	}
}
