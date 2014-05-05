/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2014 The ZAP Development Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.ant.zap.taskdefs;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.zaproxy.clientapi.ant.ZapTask;

/**
 * Helper task that stops ZAP, using the API, waiting up to the given seconds before failing.
 * <p>
 * It tries to check if ZAP is ready to receive API calls by attempting to establish a connection to ZAP's proxy, in a given
 * time, failing if the connection is not successful. If the connection is successful it calls the shutdown API action.
 * </p>
 */
public class StopZapTimeout extends ZapTask {

    private static final int MILLISECONDS_IN_SECOND = 1000;

    private static final int DEFAULT_SLEEP_TIME_BETWEEN_CONNECTION_POOLING_IN_MS = getMilliseconds(1);

    private int timeout;
    private int pollingIntervalInMs;

    public StopZapTimeout() {
        pollingIntervalInMs = DEFAULT_SLEEP_TIME_BETWEEN_CONNECTION_POOLING_IN_MS;
    }

    public void setTimeout(int seconds) {
        if (seconds <= 0) {
            throw new BuildException("Attribute timeout must be greater than zero.");
        }
        this.timeout = seconds;
    }

    public void setPollingInterval(int seconds) {
        if (seconds <= 0) {
            throw new BuildException("Attribute pollinginterval must be greater than zero.");
        }
        this.pollingIntervalInMs = getMilliseconds(seconds);
    }

    @Override
    public void execute() throws BuildException {
        if (timeout == 0) {
            throw new BuildException("Attribute timeout must be set.");
        }

        log("Waiting for a successful connection to ZAP...", Project.MSG_VERBOSE);
        waitForSuccessfulConnectionToZap(timeout);

        try {
            log("Calling shutdown API action...", Project.MSG_VERBOSE);
            this.getClientApi().core.shutdown(null);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void waitForSuccessfulConnectionToZap(int timeout) {
        int timeoutInMs = getMilliseconds(timeout);
        int connectionTimeoutInMs = timeoutInMs;
        boolean connectionSuccessful = false;
        long startTime = System.currentTimeMillis();
        do {
            try (Socket socket = new Socket()) {
                try {
                    socket.connect(new InetSocketAddress(getZapAddress(), getZapPort()), connectionTimeoutInMs);
                    connectionSuccessful = true;
                } catch (SocketTimeoutException ignore) {
                    throw new BuildException("Unable to connect to ZAP's proxy after " + timeout + " seconds.");
                } catch (IOException ignore) {
                    // and keep trying but wait some time first...
                    try {
                        Thread.sleep(pollingIntervalInMs);
                    } catch (InterruptedException e) {
                        throw new BuildException("The task was interrupted while sleeping between connection polling.", e);
                    }

                    long ellapsedTime = System.currentTimeMillis() - startTime;
                    if (ellapsedTime >= timeoutInMs) {
                        throw new BuildException("Unable to connect to ZAP's proxy after " + timeout + " seconds.");
                    }
                    connectionTimeoutInMs = (int) (timeoutInMs - ellapsedTime);
                }
            } catch (IOException ignore) {
                // the closing state doesn't matter.
            }
        } while (!connectionSuccessful);
    }

    private static int getMilliseconds(int seconds) {
        return seconds * MILLISECONDS_IN_SECOND;
    }
}
