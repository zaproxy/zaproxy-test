package org.zaproxy.zap.extension.api;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
	public void historyIdShouldBecomeIdOfApiResponse() {
		ApiResponseSet response = ApiResponseConversionUtils.httpMessageToSet(42, message);
		assertThat(response.getValues(), hasEntry("id", "42"));
	}

	@Test
	@Ignore
	public void propertiesFromGivenHttpMessageShouldReflectInApiResponse() {
	}

	@Test
	@Ignore
	public void compressedResponseBodyShouldBeDeflatedIntoApiResponse() {
	}

	@Test
	@Ignore
	public void brokenCompressedResponseBodyShouldBeStoredAsStringRepresentationInApiResponse() {
	}

}
