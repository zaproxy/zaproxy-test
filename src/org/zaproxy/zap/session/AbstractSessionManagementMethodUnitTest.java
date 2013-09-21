package org.zaproxy.zap.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.model.Session;
import org.zaproxy.zap.model.InMemoryContextDataMockSession;
import org.zaproxy.zap.session.SessionManagementMethodType.UnsupportedSessionManagementMethodException;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractSessionManagementMethodUnitTest {

	protected abstract SessionManagementMethodType getMethodTypeImplementation();

	@Test
	public void isTypeForMethodShouldReturnProperResult() {
		// Given
		SessionManagementMethod method = getMethodTypeImplementation().createSessionManagementMethod(0);
		SessionManagementMethod wrongMethod = Mockito.mock(SessionManagementMethod.class);

		// When/Then
		assertTrue(getMethodTypeImplementation().isTypeForMethod(method));
		assertFalse(getMethodTypeImplementation().isTypeForMethod(wrongMethod));
	}

	@Test
	public void shouldReturnProperType() {
		// Given
		SessionManagementMethod method = getMethodTypeImplementation().createSessionManagementMethod(0);
		// When/Then
		assertTrue(method.getType().getClass().equals(getMethodTypeImplementation().getClass()));
	}

	@Test
	public void shouldImplementCloneEqualsAndHashProperly() {
		// Given
		SessionManagementMethod method = getMethodTypeImplementation().createSessionManagementMethod(0);
		// When
		SessionManagementMethod clone = method.clone();
		// Then
		assertEquals(method, clone);
		assertEquals(method.hashCode(), clone.hashCode());
	}

	@Test
	@Ignore
	public void shouldPersistAndLoadFromSession() throws UnsupportedSessionManagementMethodException,
			SQLException {

		// Given
		Constant.getInstance();
		SessionManagementMethod method = getMethodTypeImplementation().createSessionManagementMethod(0);
		Session session = new InMemoryContextDataMockSession();

		// When
		getMethodTypeImplementation().persistMethodToSession(session, 0, method);
		SessionManagementMethod restoredMethod = getMethodTypeImplementation().loadMethodFromSession(session, 0);

		// Then
		assertEquals(method, restoredMethod);
	}
}
