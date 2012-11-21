package ch.csnc.extension.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Unit test for {@link Encoding}.
 *
 * @author bjoern.kimminich@gmx.de
 */
public class EncodingUnitTest {

	@Test
	public void shouldCovertDataIntoCorrectBase64String() {
		assertThat(Encoding.base64encode("Hello World".getBytes()), is(equalTo("SGVsbG8gV29ybGQ=")));
	}

	@Test
	public void shouldBase64StringIntoCorrectCovertData() {
		assertThat(Encoding.base64decode("SGVsbG8gV29ybGQ="), is(equalTo("Hello World".getBytes())));
	}

}
