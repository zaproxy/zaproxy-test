package org.zaproxy.zap.control;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZapReleaseUnitTest {

	private static final String DEV_BUILD = "Dev Build";
	private static final String VER_1_4_1 = "1.4.1";
	private static final String VER_1_4_2 = "1.4.2";
	private static final String VER_1_4_2_1 = "1.4.2.1";
	private static final String VER_1_5_1 = "1.5.1";

	@Test
	public void testDevBuildLaterThan1_4_1() {
		ZapRelease rel = new ZapRelease();
		rel.setVersion(DEV_BUILD);
		assertTrue(rel.isNewerThan(VER_1_4_1, false));
	}

	@Test
	public void test1_4_2LaterThan1_4_1() {
		ZapRelease rel = new ZapRelease();
		rel.setVersion(VER_1_4_2);
		assertTrue(rel.isNewerThan(VER_1_4_1, false));
	}

	@Test
	public void test1_5_1LaterThan1_4_2() {
		ZapRelease rel = new ZapRelease();
		rel.setVersion(VER_1_5_1);
		assertTrue(rel.isNewerThan(VER_1_4_2, false));
	}

	@Test
	public void test1_5_1LaterThan1_4_2_1() {
		ZapRelease rel = new ZapRelease();
		rel.setVersion(VER_1_5_1);
		assertTrue(rel.isNewerThan(VER_1_4_2_1, false));
	}

	// TODO Implement more tests


}
