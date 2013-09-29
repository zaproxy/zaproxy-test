package org.zaproxy.zap.authentication;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.network.HttpStatusCode;
import org.zaproxy.zap.TestWebAppUtils;
import org.zaproxy.zap.authentication.AuthenticationMethod.UnsupportedAuthenticationCredentialsException;
import org.zaproxy.zap.authentication.FormBasedAuthenticationMethodType.FormBasedAuthenticationMethod;
import org.zaproxy.zap.session.SessionManagementMethod;
import org.zaproxy.zap.session.WebSession;
import org.zaproxy.zap.users.User;

/**
 * A set of unit tests for {@link FormBasedAuthenticationMethod}. In order for the test to properly
 * work, the following configuration must be done:
 * <ul>
 * <li>The test-webapp (test-webapp/zap-test-webapp.war) should be started and deployed on localhost
 * (more specifically deployed on the path specified by {@link TestWebAppUtils#APP_BASE_URL}).</li>
 * </ul>
 */
@RunWith(MockitoJUnitRunner.class)
public class FormBasedAuthenticationMethodUnitTest extends AbstractAuthenticationMethodUnitTest {

	private static FormBasedAuthenticationMethodType type;
	private static final String LOGGED_IN_INDICATOR = "Welcome [^\\s]*";
	private static final String LOGIN_REQUEST_URL = "http://localhost:8080/zap-test-webapp/form-based-auth/loginCheck.jsp";
	private static final String LOGIN_REQUEST_BODY = "username="
			+ FormBasedAuthenticationMethod.MSG_USER_PATTERN + "&password="
			+ FormBasedAuthenticationMethod.MSG_PASS_PATTERN;
	private static final String USER_NAME = "user1";
	private static final String USER_PASS = "user1";
	private static final String WRONG_USER_PASS = "testing";

	@BeforeClass
	public static void classSetUp() {
		// Make sure Constant is loaded for messages
		Constant.getInstance();

		type = new FormBasedAuthenticationMethodType();
	}

	private UsernamePasswordAuthenticationCredentials getMockedCredentials() {
		UsernamePasswordAuthenticationCredentials mockedCredentials = Mockito
				.mock(UsernamePasswordAuthenticationCredentials.class);
		when(mockedCredentials.getUsername()).thenReturn(USER_NAME);
		when(mockedCredentials.getPassword()).thenReturn(USER_PASS);
		return mockedCredentials;
	}

	private FormBasedAuthenticationMethod createMethod(String loginRequestUrl, String loginRequestBody)
			throws Exception {
		FormBasedAuthenticationMethod method = type.createAuthenticationMethod(0);
		method.setLoggedInIndicatorPattern(LOGGED_IN_INDICATOR);
		method.setLoginRequest(loginRequestUrl, loginRequestBody);
		return method;
	}

	@Test
	public void shouldBuildCorrectPostAuthenticationMessage() throws Exception {
		// Given
		HttpSender mockedSender = mock(HttpSender.class);
		doAnswer(new Answer<HttpMessage>() {
			@Override
			public HttpMessage answer(InvocationOnMock invocation) throws Throwable {
				HttpMessage authMessage = ((HttpMessage) invocation.getArguments()[0]);

				// Then
				assertEquals(authMessage.getRequestHeader().getMethod(), HttpRequestHeader.POST);
				assertEquals(authMessage.getRequestHeader().getURI().toString(), LOGIN_REQUEST_URL);
				assertThat(authMessage.getRequestBody().toString(), containsString("username=" + USER_NAME
						+ "&password=" + USER_PASS));
				return (HttpMessage) invocation.getArguments()[0];
			}
		}).when(mockedSender).sendAndReceive((HttpMessage) anyObject());

		FormBasedAuthenticationMethod spiedMethod = spy(createMethod(LOGIN_REQUEST_URL, LOGIN_REQUEST_BODY));
		when(spiedMethod.getHttpSender()).thenReturn(mockedSender);

		SessionManagementMethod mockedSessionManagementMethod = Mockito.mock(SessionManagementMethod.class);
		User mockedUser = mock(User.class);

		// When
		spiedMethod.authenticate(mockedSessionManagementMethod, getMockedCredentials(), mockedUser);
	}

	@Test
	public void shouldAuthenticateWithRightCredentials()
			throws UnsupportedAuthenticationCredentialsException, Exception {
		// Given
		SessionManagementMethod mockedSessionManagementMethod = mock(SessionManagementMethod.class);
		doAnswer(new Answer<WebSession>() {

			@Override
			public WebSession answer(InvocationOnMock invocation) throws Throwable {
				HttpMessage authMessage = ((HttpMessage) invocation.getArguments()[0]);
				// Then
				assertTrue(HttpStatusCode.isRedirection(authMessage.getResponseHeader().getStatusCode()));
				assertEquals(authMessage.getResponseHeader().getHeader(HttpResponseHeader.LOCATION),
						"http://localhost:8080/zap-test-webapp/form-based-auth/restricted/home.jsp");
				return mock(WebSession.class);
			}
		}).when(mockedSessionManagementMethod).extractWebSession((HttpMessage) anyObject());

		User mockedUser = mock(User.class);

		// When
		createMethod(LOGIN_REQUEST_URL, LOGIN_REQUEST_BODY).authenticate(mockedSessionManagementMethod,
				getMockedCredentials(), mockedUser);
	}

	@Test
	public void shouldNotAuthenticateWithWrongCredentials()
			throws UnsupportedAuthenticationCredentialsException, Exception {
		// Given
		SessionManagementMethod mockedSessionManagementMethod = mock(SessionManagementMethod.class);
		doAnswer(new Answer<WebSession>() {

			@Override
			public WebSession answer(InvocationOnMock invocation) throws Throwable {
				HttpMessage authMessage = ((HttpMessage) invocation.getArguments()[0]);
				// Then
				assertTrue(HttpStatusCode.isRedirection(authMessage.getResponseHeader().getStatusCode()));
				assertEquals(authMessage.getResponseHeader().getHeader(HttpResponseHeader.LOCATION),
						"http://localhost:8080/zap-test-webapp/form-based-auth/error.jsp");
				return mock(WebSession.class);
			}
		}).when(mockedSessionManagementMethod).extractWebSession((HttpMessage) anyObject());

		UsernamePasswordAuthenticationCredentials mockedCredentials = getMockedCredentials();
		when(mockedCredentials.getPassword()).thenReturn(WRONG_USER_PASS);

		User mockedUser = mock(User.class);

		// When
		createMethod(LOGIN_REQUEST_URL, LOGIN_REQUEST_BODY).authenticate(mockedSessionManagementMethod,
				mockedCredentials, mockedUser);
	}

	@Test
	public void shouldBuildCorrectGetAuthenticationMessage() throws Exception {
		// Given
		HttpSender mockedSender = mock(HttpSender.class);
		doAnswer(new Answer<HttpMessage>() {
			@Override
			public HttpMessage answer(InvocationOnMock invocation) throws Throwable {
				// Then
				HttpMessage authMessage = ((HttpMessage) invocation.getArguments()[0]);
				assertEquals(authMessage.getRequestHeader().getMethod(), HttpRequestHeader.GET);
				assertEquals(authMessage.getRequestHeader().getURI().toString(), LOGIN_REQUEST_URL);
				return (HttpMessage) invocation.getArguments()[0];
			}
		}).when(mockedSender).sendAndReceive((HttpMessage) anyObject());

		FormBasedAuthenticationMethod spiedMethod = spy(createMethod(LOGIN_REQUEST_URL, null));
		when(spiedMethod.getHttpSender()).thenReturn(mockedSender);

		User mockedUser = mock(User.class);
		SessionManagementMethod mockedSessionManagementMethod = Mockito.mock(SessionManagementMethod.class);

		// When
		spiedMethod.authenticate(mockedSessionManagementMethod, getMockedCredentials(), mockedUser);
	}

	@Test
	public void typeShouldReturnProperImplementation() {
		// Given
		AuthenticationMethod method = type.createAuthenticationMethod(0);
		// When/Then
		assertThat(method, notNullValue());
		assertTrue(method instanceof FormBasedAuthenticationMethod);

	}

	@Override
	protected AuthenticationMethodType getMethodTypeImplementation() {
		return type;
	}

}
