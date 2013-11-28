package org.zaproxy.zap.extension.api;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.zaproxy.zap.network.HttpRequestBody;
import org.zaproxy.zap.network.HttpResponseBody;

@RunWith(MockitoJUnitRunner.class)
public class ApiResponseConversionUtilsUnitTest {
	
	@Mock
	HttpMessage message;
	
	@Mock
	HttpRequestHeader requestHeader;
	
	@Mock
	HttpRequestBody requestBody;

	@Mock
	HttpResponseHeader responseHeader;

	@Mock
	HttpResponseBody responseBody;

	@Before
	public void prepareMessage() {
		given(message.getRequestHeader()).willReturn(requestHeader);
		given(message.getRequestBody()).willReturn(requestBody);
		given(message.getResponseHeader()).willReturn(responseHeader);
		given(message.getResponseBody()).willReturn(responseBody);
	}
	
	@Test
	public void nameOfApiResponseShouldBeConstant() {
		ApiResponseSet response = ApiResponseConversionUtils.httpMessageToSet(0, message);

		assertThat(response.getName(), is("message"));
	}

	@Test
	public void historyIdShouldBecomeIdOfApiResponse() {
		ApiResponseSet response = ApiResponseConversionUtils.httpMessageToSet(42, message);

		assertThat(response.getValues(), hasEntry("id", "42"));
	}

	@Test
	public void propertiesFromGivenHttpMessageShouldReflectInApiResponse() {
		given(message.getCookieParamsAsString()).willReturn("testCookies");
		given(message.getNote()).willReturn("testNote");
		given(requestHeader.toString()).willReturn("testRequestHeader");
		given(requestBody.toString()).willReturn("testRequestBody");
		given(responseHeader.toString()).willReturn("testResponseHeader");
		
		ApiResponseSet response = ApiResponseConversionUtils.httpMessageToSet(0, message);
		
		assertThat(response.getValues(), hasEntry("cookieParams", "testCookies"));
		assertThat(response.getValues(), hasEntry("note", "testNote"));
		assertThat(response.getValues(), hasEntry("requestHeader", requestHeader.toString()));
		assertThat(response.getValues(), hasEntry("requestBody", requestBody.toString()));
		assertThat(response.getValues(), hasEntry("responseHeader", responseHeader.toString()));
		
	}

	@Test
	@Ignore
	public void compressedResponseBodyShouldBeDeflatedIntoApiResponse() {
	}

	@Test
	public void brokenCompressedResponseBodyShouldBeStoredAsStringRepresentationInApiResponse() {
		given(responseHeader.getHeader(HttpHeader.CONTENT_ENCODING)).willReturn(HttpHeader.GZIP);
		given(responseBody.getBytes()).willReturn(new byte[] {0,0,0});

		ApiResponseSet response = ApiResponseConversionUtils.httpMessageToSet(0, message);
		
		assertThat(response.getValues(), hasEntry("responseBody", responseBody.toString()));
	}

}
