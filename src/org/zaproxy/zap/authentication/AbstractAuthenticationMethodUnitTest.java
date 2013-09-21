package org.zaproxy.zap.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.zaproxy.zap.authentication.AuthenticationMethodType.UnsupportedAuthenticationMethodException;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractAuthenticationMethodUnitTest {

	protected abstract AuthenticationMethodType getMethodTypeImplementation();

	@Test
	public void isTypeForMethodShouldReturnProperResult() {
		// Given
		AuthenticationMethod method = getMethodTypeImplementation().createAuthenticationMethod(0);
		AuthenticationMethod wrongMethod = Mockito.mock(AuthenticationMethod.class);

		// When/Then
		assertTrue(getMethodTypeImplementation().isTypeForMethod(method));
		assertFalse(getMethodTypeImplementation().isTypeForMethod(wrongMethod));
	}

	@Test
	public void shouldReturnProperType() {
		// Given
		AuthenticationMethod method = getMethodTypeImplementation().createAuthenticationMethod(0);
		// When/Then
		assertTrue(method.getType().getClass().equals(getMethodTypeImplementation().getClass()));
	}

	@Test
	public void shouldImplementCloneEqualsAndHashProperly() {
		// Given
		AuthenticationMethod method = getMethodTypeImplementation().createAuthenticationMethod(0);
		// When
		AuthenticationMethod clone = method.clone();
		// Then
		assertEquals(method, clone);
		assertEquals(method.hashCode(), clone.hashCode());
	}

	@Test
	@Ignore
	public void shouldPersistAndLoadFromSession() throws UnsupportedAuthenticationMethodException,
			SQLException {
		// Given
		AuthenticationMethod method = getMethodTypeImplementation().createAuthenticationMethod(0);
		Session session = Model.getSingleton().newSession();

		// When
		getMethodTypeImplementation().persistMethodToSession(session, 0, method);
		AuthenticationMethod restoredMethod = getMethodTypeImplementation().loadMethodFromSession(session, 0);

		// Then
		assertEquals(method, restoredMethod);
	}
}
