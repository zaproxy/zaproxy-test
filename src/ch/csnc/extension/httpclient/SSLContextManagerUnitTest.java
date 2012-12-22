package ch.csnc.extension.httpclient;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SSLContextManagerUnitTest {
	
	private SSLContextManager sslContextManager;
	
	@Before
	public void setUp() throws Exception {
		sslContextManager = new SSLContextManager();
	}

	@Test
	public void shouldReturnAvailabilityOfPKCS11Provider() {
		// Given
		boolean pkcs11ProdivderAvailable = true;
		try {
			Class.forName("sun.security.pkcs11.SunPKCS11");
		} catch (ClassNotFoundException e) {
			pkcs11ProdivderAvailable = false;			
		}
		// When
		boolean result = sslContextManager.isProviderAvailable("PKCS11");
		// Then
		assertThat(result, is(equalTo(pkcs11ProdivderAvailable)));
	}
	
	@Test
	public void shouldReturnAvailabilityOfMsksProvider() {
		// Given
		boolean msks11ProdivderAvailable = true;
		try {
			Class.forName("se.assembla.jce.provider.ms.MSProvider");
		} catch (ClassNotFoundException e) {
			msks11ProdivderAvailable = false;			
		}
		// When
		boolean result = sslContextManager.isProviderAvailable("msks");
		// Then
		assertThat(result, is(equalTo(msks11ProdivderAvailable)));
	}
	
	@Test
	public void shouldAlwaysReturnTrueForOtherThanPKCS11AndMsksProvider() {
		// Given
		// When
		boolean result = sslContextManager.isProviderAvailable("thisProviderDoesNotExist");
		// Then
		assertThat(result, is(true));
	}
	

}
