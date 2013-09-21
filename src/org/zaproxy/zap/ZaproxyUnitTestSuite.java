package org.zaproxy.zap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.parosproxy.paros.core.scanner.KbUnitTest;
import org.parosproxy.paros.core.scanner.UtilUnitTest;
import org.parosproxy.paros.core.scanner.VariantODataUnitTest;
import org.parosproxy.paros.model.FileCopierTest;
import org.zaproxy.zap.authentication.AuthenticationMethodIndicatorsUnitTest;
import org.zaproxy.zap.control.AddOnCollectionUnitTest;
import org.zaproxy.zap.control.AddOnUnitTest;
import org.zaproxy.zap.control.ZapReleaseUnitTest;
import org.zaproxy.zap.extension.auth.ContextAuthUnitTest;
import org.zaproxy.zap.extension.httppanel.view.hex.HttpPanelHexModelUnitTest;
import org.zaproxy.zap.extension.pscan.PluginPassiveScannerUnitTest;
import org.zaproxy.zap.model.ContextUnitTest;
import org.zaproxy.zap.spider.URLCanonicalizerUnitTest;
import org.zaproxy.zap.spider.URLResolverRfc1808ExamplesUnitTest;
import org.zaproxy.zap.spider.URLResolverUnitTest;
import org.zaproxy.zap.utils.ByteBuilderUnitTest;
import org.zaproxy.zap.utils.XMLStringUtilUnitTest;
import org.zaproxy.zap.view.LayoutHelperUnitTest;

import ch.csnc.extension.httpclient.AliasCertificateUnitTest;
import ch.csnc.extension.httpclient.AliasKeyManagerUnitTest;
import ch.csnc.extension.httpclient.SSLContextManagerUnitTest;
import ch.csnc.extension.util.EncodingUnitTest;
import ch.csnc.extension.util.NullComparatorUnitTest;

import com.sittinglittleduck.DirBuster.EasySSLProtocolSocketFactoryUnitTest;

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
		AliasCertificateUnitTest.class,
		AliasKeyManagerUnitTest.class,
		AuthenticationMethodIndicatorsUnitTest.class,
		ByteBuilderUnitTest.class,
		ContextAuthUnitTest.class,
		ContextUnitTest.class,
		EasySSLProtocolSocketFactoryUnitTest.class,
		EncodingUnitTest.class,
		FileCopierTest.class,
		HttpPanelHexModelUnitTest.class,
		KbUnitTest.class,
		LayoutHelperUnitTest.class,
		NullComparatorUnitTest.class,
		PluginPassiveScannerUnitTest.class,
		SSLContextManagerUnitTest.class,
		URLCanonicalizerUnitTest.class,
		URLResolverRfc1808ExamplesUnitTest.class,
		URLResolverUnitTest.class,
		UtilUnitTest.class,
		VariantODataUnitTest.class,
		XMLStringUtilUnitTest.class,
		ZapReleaseUnitTest.class
		})
public final class ZaproxyUnitTestSuite {

}
