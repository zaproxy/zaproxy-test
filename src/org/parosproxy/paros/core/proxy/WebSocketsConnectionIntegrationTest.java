/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2010 psiinon@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.parosproxy.paros.core.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.junit.BeforeClass;
import org.junit.Test;
import org.parosproxy.paros.control.Control;
import org.zaproxy.zap.ZapGetMethod;
import org.zaproxy.zap.ZapHttpConnectionManager;
import org.zaproxy.zap.extension.websocket.ExtensionWebSocket;

/**
 * This test uses the Echo Server from websockets.org
 * for testing a valid WebSockets connection.
 */
public class WebSocketsConnectionIntegrationTest extends WithBasicInfrastructureIntegrationTest {

	private static String ECHO_SERVER = "http://echo.websocket.org/?encoding=text";

	@BeforeClass
	public static void setup() throws Exception {
		System.getProperties().setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		WithBasicInfrastructureIntegrationTest.setup();

		// load WebSockets extension
		Control.initSingletonForTesting();
		Control.getSingleton().getExtensionLoader().addExtension(new ExtensionWebSocket());
	}

	@Test
	public void doWebSocketsHandshakeViaClient() throws Exception {
		// use HTTP-client with custom connection manager
		// that allows us to expose the SocketChannel
		HttpClient client = new HttpClient(new ZapHttpConnectionManager());
	    client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);

	    // create minimal HTTP request
	    ZapGetMethod method = new ZapGetMethod(ECHO_SERVER);
		method.addRequestHeader("Connection", "upgrade");
		method.addRequestHeader("Upgrade", "websocket");
		method.addRequestHeader("Sec-WebSocket-Version", "13");
		method.addRequestHeader("Sec-WebSocket-Key", "5d5NazNjJ5hafSgFYJ7SOw==");
		client.executeMethod(method);

		int status = method.getStatusCode();
		assertEquals("HTTP status code of WebSockets-handshake response should be 101.", 101, status);

		Socket socket = method.getUpgradedConnection();

		assertWorkingWebSocket(socket);

		// send hello message a second time to ensure that socket is not closed after first time
		assertWorkingWebSocket(socket);

		properlyCloseWebSocket(socket);
	}

	@Test
	public void doSecureWebSocketsHandshake() throws Exception {
		// use HTTP-client with custom connection manager
		// that allows us to expose the SocketChannel
		HttpClient client = new HttpClient(new ZapHttpConnectionManager());
	    client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);

	    // create minimal HTTP handshake request
	    ZapGetMethod method = new ZapGetMethod("https://echo.websocket.org/?encoding=text");
		method.addRequestHeader("Connection", "upgrade");
		method.addRequestHeader("Upgrade", "websocket");
		method.addRequestHeader("Sec-WebSocket-Version", "13");
		method.addRequestHeader("Sec-WebSocket-Key", "5d5NazNjJ5hafSgFYJ7SOw==");
		client.executeMethod(method);

		assertEquals("HTTP status code of WebSockets-handshake response should be 101.", 101, method.getStatusCode());

		Socket socket = method.getUpgradedConnection();

		assertWorkingWebSocket(socket);

		// send hello message a second time to ensure that socket is not closed after first time
		assertWorkingWebSocket(socket);

		properlyCloseWebSocket(socket);
	}

	@Test
	public void getAutobahnCaseCount() throws HttpException {
		// use HTTP-client with custom connection manager
		// that allows us to expose the SocketChannel

		HttpConnectionManagerParams connectionParams = new HttpConnectionManagerParams();
		connectionParams.setTcpNoDelay(true);
		connectionParams.setStaleCheckingEnabled(false);
		connectionParams.setSoTimeout(500);

		ZapHttpConnectionManager connectionManager = new ZapHttpConnectionManager();
		connectionManager.setParams(connectionParams);

		HttpClient client = new HttpClient(connectionManager);
		client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);

		// create minimal HTTP handshake request
		ZapGetMethod method = new ZapGetMethod("http://localhost:9001/getCaseCount");
		method.addRequestHeader("Connection", "upgrade");
		method.addRequestHeader("Upgrade", "websocket");
		method.addRequestHeader("Sec-WebSocket-Version", "13");
		method.addRequestHeader("Sec-WebSocket-Key", "5d5NazNjJ5hafSgFYJ7SOw==");
		try {
			client.executeMethod(method);
		} catch (IOException e) {
			assertTrue("executing HTTP method failed", false);
		}

		assertEquals(
				"HTTP status code of WebSockets-handshake response should be 101.",
				101, method.getStatusCode());

		InputStream remoteInput = method.getUpgradedInputStream();

		byte[] caseCountFrame = new byte[3];
		int readBytes = 0;

		try {
			readBytes = remoteInput.read(caseCountFrame);
		} catch (IOException e) {
			assertTrue("reading websocket frame failed", false);
		}

		assertEquals("Expected some bytes in the first frame.", 3, readBytes);
		assertEquals("First WebSocket frame is text message with the case count.", 0x1, caseCountFrame[0] & 0x0f);

		byte[] closeFrame = new byte[2];
		readBytes = 0;
		try {
			readBytes = remoteInput.read(closeFrame);
		} catch (IOException e) {
			assertTrue("reading websocket frame failed: " + e.getMessage(), false);
		}

		assertEquals("Expected some bytes in the second frame.", 2, readBytes);

		assertEquals("Second WebSocket frame is a close message.", 0x8, closeFrame[0] & 0x0f);

		// now I would send back a close frame and close the physical socket connection
	}
// requires Autobahn to be running via "wstest -m fuzzingserver"
	@Test
	public void doAutobahnTest() throws HttpException, SocketException {
		// use HTTP-client with custom connection manager
		// that allows us to expose the SocketChannel
		HttpClient client = new HttpClient(new ZapHttpConnectionManager());
		client.getHostConfiguration().setProxy(PROXY_HOST, PROXY_PORT);

		// create minimal HTTP handshake request
		ZapGetMethod method = new ZapGetMethod(
				"http://localhost:9001/runCase?case=1&agent=Proxy");
		method.addRequestHeader("Connection", "upgrade");
		method.addRequestHeader("Upgrade", "websocket");
		method.addRequestHeader("Sec-WebSocket-Version", "13");
		method.addRequestHeader("Sec-WebSocket-Key", "5d5NazNjJ5hafSgFYJ7SOw==");
		try {
			client.executeMethod(method);
		} catch (IOException e) {
			assertTrue("executing HTTP method failed", false);
		}

		assertEquals(
				"HTTP status code of WebSockets-handshake response should be 101.",
				101, method.getStatusCode());

		Socket socket = method.getUpgradedConnection();
		socket.setTcpNoDelay(true);
		socket.setSoTimeout(500);

		byte[] dst = new byte[20];
		try {
			socket.getInputStream().read(dst);
		} catch (IOException e) {
			assertTrue("reading websocket frame failed: " + e.getMessage(), false);
		}
	}

//	/**
//	 * Cannot use this SOCKS approach, as ZAP does not support SOCKS.
//	 * So I had to use the HttpClient for that purpose. Another try
//	 * to work with UrlConnection failed, as I was not able to set
//   * custom HTTP headers (I was only able to override existing ones).
//   *
//	 * @throws IOException
//	 */
//	@Test
//	public void doWebSocketsHandshakeViaSocks() throws IOException {
//		Socket socket = new Socket(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)));
//		socket.connect(new InetSocketAddress("echo.websocket.org", 80), 1000);
//
//		PrintWriter writer = new PrintWriter(socket.getOutputStream ());
//	    BufferedReader reader = new BufferedReader(new InputStreamReader (socket.getInputStream ()));
//
//	    writer.print("GET /?encoding=text HTTP/1.1\r\n");
//	    writer.print("Host: echo.websocket.org\r\n");
//	    writer.print("Connection: keep-alive, Upgrade\r\n");
//	    writer.print("Sec-WebSocket-Version: 13\r\n");
//	    writer.print("Sec-WebSocket-Key: 5d5NazNjJ5hafSgFYJ7SOw==\r\n");
//	    writer.print("Upgrade: websocket\r\n");
//	    writer.print("\r\n");
//	    writer.flush();
//
//	    assertEquals("HTTP/1.1 101 Web Socket Protocol Handshake", reader.readLine());
//	    while(!reader.readLine().isEmpty()) {
//	    	// do something with response
//	    }
//
//	    socket.close();
//	    reader.close();
//	    writer.close();
//	}

	private void properlyCloseWebSocket(Socket socket) throws IOException {
		assertTrue("Retrieved SocketChannel should not be null.", socket != null);

		byte[] maskedClose = {(byte) 0x88, (byte) 0x82, 0x46, 0x59, (byte) 0xdc, 0x4a, 0x45, (byte) 0xb1};

		socket.getOutputStream().write(maskedClose);
		socket.close();
	}

	/**
	 * Sends a Hello message into the channel and asserts that
	 * the same message is returned by the Echo-Server.
	 * The outgoing message is masked, while the incoming
	 * contains the message in cleartext.
	 *
	 * @param socket
	 * @throws IOException
	 */
	private void assertWorkingWebSocket(Socket socket) throws IOException {
		assertTrue("Retrieved SocketChannel should not be null.", socket != null);
        socket.setSoTimeout(500);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);

		byte[] maskedHelloMessage = {(byte) 0x81, (byte) 0x85, 0x37, (byte) 0xfa, 0x21, 0x3d, 0x7f, (byte) 0x9f, 0x4d, 0x51, 0x58};
		byte[] unmaskedHelloMessage = {(byte) 0x81, 0x05, 0x48, 0x65, 0x6c, 0x6c, 0x6f};

		InputStream inpStream = new BufferedInputStream(socket.getInputStream());

		OutputStream out = socket.getOutputStream();
		out.write(maskedHelloMessage);
		out.flush();

		byte[] dst = new byte[7];
		inpStream.read(dst);

		// use Arrays class to compare two byte arrays
		// returns true if it contains the same elements in same order
		assertTrue("Awaited unmasked hello message from echo server.", Arrays.equals(unmaskedHelloMessage, dst));
	}
}
