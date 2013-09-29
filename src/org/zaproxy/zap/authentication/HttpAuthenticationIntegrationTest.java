package org.zaproxy.zap.authentication;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.network.HttpSender;
import org.parosproxy.paros.network.HttpStatusCode;
import org.zaproxy.zap.TestWebAppUtils;
import org.zaproxy.zap.TestWebAppUtils.TestPage;
import org.zaproxy.zap.authentication.HttpAuthenticationMethodType.HttpAuthenticationMethod;
import org.zaproxy.zap.model.Context;
import org.zaproxy.zap.session.HttpAuthSessionManagementMethodType;
import org.zaproxy.zap.session.SessionManagementMethod;
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
public class HttpAuthenticationIntegrationTest {

	private static final List<TestPage> restrictedPages = TestWebAppUtils.httpAuthRestrictedPages;
	private static final List<TestPage> unrestrictedPages = TestWebAppUtils.httpAuthUnrestrictedPages;

	private User user;
	private static HttpAuthenticationMethod method;
	private static SessionManagementMethod sessionManagementMethod;
	private static HttpAuthenticationMethodType type;
	private static Context mockedContext;
	private static HttpSender httpSender;

	@BeforeClass
	public static void classSetUp() throws Exception {
		// Make sure Constant is loaded for messages
		Constant.getInstance();

		type = new HttpAuthenticationMethodType();

		// Prepare session management method
		sessionManagementMethod = new HttpAuthSessionManagementMethodType().createSessionManagementMethod(0);

		// Prepare authentication method
		method = spy(type.createAuthenticationMethod(0));
		method.hostname = TestWebAppUtils.HTTP_AUTH_HOSTNAME;
		method.realm = TestWebAppUtils.HTTP_AUTH_REALM;
		method.port = TestWebAppUtils.HTTP_AUTH_PORT;

		// Prepare mocked context
		mockedContext = Mockito.mock(Context.class);
		when(mockedContext.getSessionManagementMethod()).thenReturn(sessionManagementMethod);
		when(mockedContext.getAuthenticationMethod()).thenReturn(method);

		// Prepare HttpSender
		httpSender = new HttpSender(Model.getSingleton().getOptionsParam().getConnectionParam(), false, 0);
	}

	@Before
	public void setup() {
		// Prepare a test user
		user = spy(new User(0, TestWebAppUtils.HTTP_AUTH_USERNAME));
		UsernamePasswordAuthenticationCredentials credentials = new UsernamePasswordAuthenticationCredentials(
				TestWebAppUtils.HTTP_AUTH_USERNAME, TestWebAppUtils.HTTP_AUTH_USERPASS);
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
		HttpMessage msg = buildMessage(restrictedPages.get(1).url);
		// When
		httpSender.sendAndReceive(msg, true);
		// Then
		assertEquals(HttpStatusCode.UNAUTHORIZED, msg.getResponseHeader().getStatusCode());
		assertNotNull(msg.getResponseHeader().getHeader(HttpResponseHeader.WWW_AUTHENTICATE));
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
	public void shouldNotAuthenticateWhereNotNeeded() throws NullPointerException, IOException,
			InterruptedException {
		for (TestPage p : unrestrictedPages) {
			// Given/When
			HttpMessage msg = sendMessage(p.url, true);
			// Then
			assertNull(msg.getRequestHeader().getHeader(HttpRequestHeader.AUTHORIZATION));
		}
	}
}
