package org.zaproxy.zap.authentication;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.parosproxy.paros.Constant;
import org.zaproxy.zap.authentication.HttpAuthenticationMethodType.HttpAuthenticationMethod;
import org.zaproxy.zap.session.SessionManagementMethod;
import org.zaproxy.zap.session.WebSession;
import org.zaproxy.zap.users.User;

public class HttpAuthenticationMethodUnitTest extends AbstractAuthenticationMethodUnitTest {

	private static final String HOSTNAME = "localhost";
	private static final String REALM = "Zaproxy";
	private static final int PORT = 8080;
	private static final String USER_NAME = "tomcat";
	private static final String USER_PASS = "tomcat";

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
		when(mockedCredentials.getUsername()).thenReturn(USER_NAME);
		when(mockedCredentials.getPassword()).thenReturn(USER_PASS);

		User mockedUser = Mockito.mock(User.class);
		when(mockedUser.getAuthenticationCredentials()).thenReturn(mockedCredentials);

		return mockedUser;
	}

	private HttpAuthenticationMethod createMethod() {
		HttpAuthenticationMethod method = type.createAuthenticationMethod(0);
		method.hostname = HOSTNAME;
		method.realm = REALM;
		method.port = PORT;
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
		method.hostname = HOSTNAME;

		HttpState state = new HttpState();

		WebSession mockedSession = Mockito.mock(WebSession.class);
		when(mockedSession.getHttpState()).thenReturn(state);

		SessionManagementMethod mockedSessionManagementMethod = Mockito.mock(SessionManagementMethod.class);
		doReturn(mockedSession).when(mockedSessionManagementMethod).createEmptyWebSession();

		User mockedUser = getMockedUser();

		// When
		method.authenticate(mockedSessionManagementMethod, mockedUser.getAuthenticationCredentials(),
				mockedUser);
		AuthScope scope = new AuthScope(HOSTNAME, 80, AuthScope.ANY_REALM);
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
		AuthScope scope = new AuthScope(HOSTNAME, PORT, REALM);
		UsernamePasswordCredentials configuredCredentials = (UsernamePasswordCredentials) state
				.getCredentials(scope);

		// Then
		assertNotNull(configuredCredentials);
		assertEquals(configuredCredentials.getUserName(), USER_NAME);
		assertEquals(configuredCredentials.getPassword(), USER_PASS);
	}
}
