package org.zaproxy.zap.authentication;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpSender;
import org.zaproxy.zap.TestWebAppUtils;
import org.zaproxy.zap.TestWebAppUtils.TestPage;
import org.zaproxy.zap.authentication.FormBasedAuthenticationMethodType.FormBasedAuthenticationMethod;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.session.CookieBasedSessionManagementMethodType;
import org.zaproxy.zap.session.SessionManagementMethod;
import org.zaproxy.zap.users.User;

/**
 * A set of integration tests for {@link FormBasedAuthenticationMethod}. In order for the test to
 * properly work, the following configuration must be done:
 * <ul>
 * <li>The test-webapp (test-webapp/zap-test-webapp.war) should be started and deployed on localhost
 * (more specifically deployed on the path specified by {@link TestWebAppUtils#APP_BASE_URL}).</li>
 * </ul>
 */
@RunWith(MockitoJUnitRunner.class)
public class FormBasedAuthenticationIntegrationTest {

	private static final String LOGGED_IN_INDICATOR = "Welcome:\\s*[^\\s]*";
	private static final String LOGIN_REQUEST_URL = TestWebAppUtils.FORM_BASED_AUTH_APP_BASE_URL
			+ "loginCheck.jsp";
	private static final String LOGIN_REQUEST_BODY = "username="
			+ FormBasedAuthenticationMethod.MSG_USER_PATTERN + "&password="
			+ FormBasedAuthenticationMethod.MSG_PASS_PATTERN;
	private static final String USER_NAME = "user1";
	private static final String USER_PASS = "user1";
	private static FormBasedAuthenticationMethodType type;

	private User user;
	private static SessionManagementMethod sessionManagementMethod;
	private static Context mockedContext;
	private static FormBasedAuthenticationMethod method;
	private static HttpSender httpSender;

	private static final List<TestPage> restrictedPages = TestWebAppUtils.formBasedAuthRestrictedPages;
	private static final List<TestPage> unrestrictedPages = TestWebAppUtils.formBasedAuthUnrestrictedPages;
	private static final TestPage logoutPage = TestWebAppUtils.formBasedAuthLogoutPage;

	@BeforeClass
	public static void classSetUp() throws Exception {
		// Make sure Constant is loaded for messages
		Constant.getInstance();
		type = new FormBasedAuthenticationMethodType();

		// Prepare session management method
		sessionManagementMethod = new CookieBasedSessionManagementMethodType()
				.createSessionManagementMethod(0);

		// Prepare authentication method
		method = spy(type.createAuthenticationMethod(0));
		method.setLoggedInIndicatorPattern(LOGGED_IN_INDICATOR);
		method.setLoginRequest(LOGIN_REQUEST_URL, LOGIN_REQUEST_BODY);

		// Prepare mocked context
		mockedContext = Mockito.mock(Context.class);
		when(mockedContext.getSessionManagementMethod()).thenReturn(sessionManagementMethod);
		when(mockedContext.getAuthenticationMethod()).thenReturn(method);

		// Prepare HttpSender
		httpSender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(), false,
				HttpSender.SPIDER_INITIATOR);
	}

	@Before
	public void setup() {
		// Prepare a test user
		user = spy(new User(0, USER_NAME));
		UsernamePasswordAuthenticationCredentials credentials = type.createAuthenticationCredentials();
		credentials.username = USER_NAME;
		credentials.password = USER_PASS;
		user.setAuthenticationCredentials(credentials);
		doReturn(mockedContext).when(user).getContext();
	}

	private HttpMessage buildMessage(String url) throws URIException, HttpMalformedHeaderException,
			NullPointerException {
		HttpMessage msg = new HttpMessage();
		msg.setRequestHeader(new HttpRequestHeader(HttpRequestHeader.GET, new URI(url, false),
				HttpRequestHeader.HTTP10));
		return msg;
	}

	private HttpMessage sendMessage(String url, boolean asUser) throws NullPointerException, IOException {
		HttpMessage msg = buildMessage(url);
		if (asUser)
			msg.setRequestingUser(user);
		httpSender.sendAndReceive(msg);
		return msg;
	}

	private void visitAndCheckPagesSet(List<TestPage> pages) throws NullPointerException, IOException {
		for (TestPage pageEntry : pages)
			visitAndCheckCorrectPage(pageEntry);
	}

	private void visitAndCheckCorrectPage(TestPage page) throws NullPointerException, IOException {
		visitAndCheckCorrectPage(page, true);
	}

	private void visitAndCheckCorrectPage(TestPage page, boolean asUser) throws NullPointerException,
			IOException {
		assertThat(sendMessage(page.url, asUser).getResponseBody().toString(),
				containsString(page.identifier));
	}

	@Test
	public void shouldHaveThingsConfigured() {
		// Given/When/Then
		assertNotNull(method.authenticate(sessionManagementMethod, user.getAuthenticationCredentials(), user));
	}

	@Test
	public void shouldHaveEverythingSetupToSendMessages() throws Exception {
		// Given/When/Then
		visitAndCheckCorrectPage(unrestrictedPages.get(0), false);
	}

	@Test
	public void shouldNotAccessRestrictedAreaUnauthenticated() throws NullPointerException, IOException {
		// Given
		HttpMessage msg = buildMessage(restrictedPages.get(0).url);
		// When
		httpSender.sendAndReceive(msg, true);
		// Then
		assertThat(msg.getResponseBody().toString(),
				containsString(TestWebAppUtils.formBasedAuthErrorPage.identifier));
		assertThat(msg.getResponseBody().toString(), containsString("Unauthorized"));
	}

	@Test
	public void shouldAuthenticateInRestrictedArea() throws Exception {
		// Given/When/Then
		visitAndCheckPagesSet(restrictedPages);
	}

	@Test
	public void shouldAuthenticateOnlyOnceOnTheSite() throws NullPointerException, IOException {
		// Given/When
		visitAndCheckPagesSet(restrictedPages);
		visitAndCheckPagesSet(unrestrictedPages);

		// Then
		verify(method, times(1)).authenticate(sessionManagementMethod, user.getAuthenticationCredentials(),
				user);
	}

	@Test
	public void shouldAuthenticateAfterLogout() throws NullPointerException, IOException {
		// Given/When
		visitAndCheckCorrectPage(restrictedPages.get(0));
		visitAndCheckCorrectPage(logoutPage);
		visitAndCheckCorrectPage(restrictedPages.get(1));

		// Then
		verify(method, times(3)).authenticate(sessionManagementMethod, user.getAuthenticationCredentials(),
				user); // 3 times as after the first logout it tries again to authenticate
	}

	@Test
	public void shouldNotAuthenticateMoreThanNecessary() throws NullPointerException, IOException {
		// Given/When
		visitAndCheckCorrectPage(restrictedPages.get(0));
		visitAndCheckCorrectPage(restrictedPages.get(1));
		visitAndCheckCorrectPage(logoutPage);
		visitAndCheckCorrectPage(restrictedPages.get(2));
		visitAndCheckCorrectPage(logoutPage);
		visitAndCheckCorrectPage(restrictedPages.get(3));
		visitAndCheckCorrectPage(unrestrictedPages.get(0));

		// Then
		verify(method, times(5)).authenticate(sessionManagementMethod, user.getAuthenticationCredentials(),
				user); // 5 Times as after each logout it tries again to authenticate
	}

}
