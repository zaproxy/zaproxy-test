package org.zaproxy.zap;

import java.util.ArrayList;

/**
 * Helper class for storing various data and constants related to the test webapp (zap-test-webapp)
 */
public class TestWebAppUtils {
	
	public static final String APP_BASE_URL = "http://localhost:8080/zap-test-webapp/";

	// FORM BASED AUTHENTICATION SECTION
	public static final String FORM_BASED_AUTH_APP_BASE_URL = APP_BASE_URL + "form-based-auth/";
	public static final String FORM_BASED_AUTH_RESTRICTED_APP_BASE_URL = APP_BASE_URL
			+ "form-based-auth/restricted/";
	public static ArrayList<TestPage> formBasedAuthRestrictedPages;
	public static ArrayList<TestPage> formBasedAuthUnrestrictedPages;
	public static TestPage formBasedAuthLogoutPage = new TestPage(
			FORM_BASED_AUTH_APP_BASE_URL + "logout.jsp", "fb-unrestricted-logout-512873");
	public static TestPage formBasedAuthErrorPage = new TestPage(
			FORM_BASED_AUTH_APP_BASE_URL + "error.jsp", "fb-unrestricted-error-498762");
	static {
		formBasedAuthRestrictedPages = new ArrayList<>();
		formBasedAuthRestrictedPages.add(new TestPage(FORM_BASED_AUTH_RESTRICTED_APP_BASE_URL + "home.jsp",
				"fb-restricted-home-269039"));
		formBasedAuthRestrictedPages.add(new TestPage(FORM_BASED_AUTH_RESTRICTED_APP_BASE_URL + "a.jsp",
				"fb-restricted-a-382641"));
		formBasedAuthRestrictedPages.add(new TestPage(FORM_BASED_AUTH_RESTRICTED_APP_BASE_URL + "ac.jsp",
				"fb-restricted-ac-863602"));
		formBasedAuthRestrictedPages.add(new TestPage(FORM_BASED_AUTH_RESTRICTED_APP_BASE_URL + "b.jsp",
				"fb-restricted-b-972631"));

		formBasedAuthUnrestrictedPages = new ArrayList<>();
		formBasedAuthUnrestrictedPages.add(new TestPage(FORM_BASED_AUTH_APP_BASE_URL + "",
				"fb-unrestricted-index-198734"));
		formBasedAuthUnrestrictedPages.add(new TestPage(FORM_BASED_AUTH_APP_BASE_URL + "d.jsp",
				"fb-unrestricted-d-629742"));
	}

	public static class TestPage {
		public String url;
		public String identifier;

		public TestPage(String page, String identifier) {
			this.url = page;
			this.identifier = identifier;
		}
	}
}
