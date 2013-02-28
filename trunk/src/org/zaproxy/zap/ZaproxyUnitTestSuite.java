package org.zaproxy.zap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.parosproxy.paros.core.scanner.KbUnitTest;
import org.parosproxy.paros.core.scanner.UtilUnitTest;
import org.parosproxy.paros.model.FileCopierTest;
import org.zaproxy.zap.control.AddOnCollectionUnitTest;
import org.zaproxy.zap.control.AddOnUnitTest;
import org.zaproxy.zap.control.ZapReleaseUnitTest;
import org.zaproxy.zap.extension.auth.ContextAuthUnitTest;
import org.zaproxy.zap.extension.httppanel.view.hex.HttpPanelHexModelUnitTest;
import org.zaproxy.zap.model.ContextUnitTest;
import org.zaproxy.zap.spider.URLResolverRfc1808ExamplesUnitTest;
import org.zaproxy.zap.spider.URLResolverUnitTest;
import org.zaproxy.zap.view.LayoutHelperUnitTest;

import com.sittinglittleduck.DirBuster.EasySSLProtocolSocketFactoryUnitTest;

import ch.csnc.extension.httpclient.AliasCertificateUnitTest;
import ch.csnc.extension.httpclient.AliasKeyManagerUnitTest;
import ch.csnc.extension.httpclient.SSLContextManagerUnitTest;
import ch.csnc.extension.util.EncodingUnitTest;
import ch.csnc.extension.util.NullComparatorUnitTest;

/**
 * Suite for all unit tests of ZAP. Tests attached to this suite must run
 * independent of any environmental settings or preferences on any possible
 * machine.
 *
 * @author bjoern.kimminich@gmx.de
 */
@RunWith(Suite.class)
@SuiteClasses({
		// In alphabetical order
		AddOnCollectionUnitTest.class,
		AddOnUnitTest.class,
		AliasKeyManagerUnitTest.class,
		ContextAuthUnitTest.class,
		ContextUnitTest.class,
		EncodingUnitTest.class,
		HttpPanelHexModelUnitTest.class,
		KbUnitTest.class,
		NullComparatorUnitTest.class,
		URLResolverRfc1808ExamplesUnitTest.class,
		URLResolverUnitTest.class,
		UtilUnitTest.class,
		ZapReleaseUnitTest.class,
		EasySSLProtocolSocketFactoryUnitTest.class,
		AliasCertificateUnitTest.class,
		SSLContextManagerUnitTest.class,
		FileCopierTest.class,
		LayoutHelperUnitTest.class
		})
public final class ZaproxyUnitTestSuite {

}
