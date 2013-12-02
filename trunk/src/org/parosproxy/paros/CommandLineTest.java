/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
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
package org.parosproxy.paros;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.parosproxy.paros.extension.CommandLineArgument;

public class CommandLineTest {
	
	private CommandLine cmdLine;
	private Vector<CommandLineArgument[]> clArgs;
	
	@Before
	public void setUp() throws Exception {
		clArgs = new Vector<CommandLineArgument[]>();
	}

	@Test
	public void emptyCommandLine() throws Exception {
		cmdLine = new CommandLine(new String[] {});
		cmdLine.parse(clArgs);
		assertTrue(cmdLine.isGUI());
		assertFalse(cmdLine.isDaemon());
		assertFalse(cmdLine.isReportVersion());
	}

	@Test
	public void daemonFlag() throws Exception {
		cmdLine = new CommandLine(new String[] {"-daemon"});
		cmdLine.parse(clArgs);
		assertFalse(cmdLine.isGUI());
		assertTrue(cmdLine.isDaemon());
		assertFalse(cmdLine.isReportVersion());
	}

	@Test
	public void unsupportedFlag() throws Exception {
		cmdLine = new CommandLine(new String[] {"-unsupported"});
		try {
			cmdLine.parse(clArgs);
			fail("Expected an exception");
		} catch (Exception e) {
			// Expected
		}
	}

	@Test
	public void claWithoutArgs() throws Exception {
		cmdLine = new CommandLine(new String[] {"-a", "-b"});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-a", 0, null, null, null)});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-b", 0, null, null, null)});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-c", 0, null, null, null)});
		cmdLine.parse(clArgs);

		assertTrue(clArgs.get(0)[0].isEnabled());
		assertTrue(clArgs.get(1)[0].isEnabled());
		assertFalse(clArgs.get(2)[0].isEnabled());
	}

	@Test
	public void claWithArgs() throws Exception {
		cmdLine = new CommandLine(new String[] {"-a", "aaa", "-b", "bbb", "BBB"});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-a", 1, null, null, null)});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-b", 2, null, null, null)});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-c", 3, null, null, null)});
		cmdLine.parse(clArgs);

		assertTrue(clArgs.get(0)[0].isEnabled());
		assertThat(clArgs.get(0)[0].getArguments().size(), is(equalTo(1)));
		assertTrue(clArgs.get(0)[0].getArguments().contains("aaa"));
		assertFalse(clArgs.get(0)[0].getArguments().contains("bbb"));
		
		assertTrue(clArgs.get(1)[0].isEnabled());
		assertThat(clArgs.get(1)[0].getArguments().size(), is(equalTo(2)));
		assertFalse(clArgs.get(1)[0].getArguments().contains("aaa"));
		assertTrue(clArgs.get(1)[0].getArguments().contains("bbb"));
		assertTrue(clArgs.get(1)[0].getArguments().contains("BBB"));
		
		assertFalse(clArgs.get(2)[0].isEnabled());
	}

	@Test
	public void claWithMissingArgs() throws Exception {
		cmdLine = new CommandLine(new String[] {"-a", "aaa", "-b", "bbb"});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-a", 1, null, null, null)});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-b", 2, null, null, null)});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-c", 3, null, null, null)});
		try {
			cmdLine.parse(clArgs);
			fail("Expected an exception");
		} catch (Exception e) {
			// Expected
		}
	}

	@Test
	public void claWithPattern() throws Exception {
		cmdLine = new CommandLine(new String[] {"-script", "aaa", "bbb", "ccc"});
		clArgs.add(new CommandLineArgument[] {new CommandLineArgument("-script", -1, ".*", null, null)});
		cmdLine.parse(clArgs);
		assertTrue(clArgs.get(0)[0].isEnabled());
		assertThat(clArgs.get(0)[0].getArguments().size(), is(equalTo(3)));
	}

}
