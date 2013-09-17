/*
 * Copyright (c) 2013 Red Hat, Inc. and/or its affiliates.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Cheng Fang - Initial API and implementation
 */

package org.jberet.se;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.jberet.spi.ArtifactFactory;
import org.jberet.spi.BatchEnvironment;
import org.jberet.spi.ThreadContextSetup;
import org.jberet.spi.ThreadContextSetup.TearDownHandle;
import org.jberet.util.BatchLogger;

/**
 * Represents the Java SE batch runtime environment and its services.
 */
public final class BatchSEEnvironment implements BatchEnvironment {
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new BatchThreadFactory());

    private static final TearDownHandle NO_OP_THREAD_CONTEXT_TEAR_DOWN = new TearDownHandle() {
        @Override
        public void tearDown() {
            // no-op
        }
    };
    private static final ThreadContextSetup NO_OP_THREAD_CONTEXT_SETUP = new ThreadContextSetup() {
        @Override
        public TearDownHandle setup() {
            return NO_OP_THREAD_CONTEXT_TEAR_DOWN;
        }
    };

    public static final String CONFIG_FILE_NAME = "jberet.properties";

    private volatile Properties configProperties;

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = BatchSEEnvironment.class.getClassLoader();
        }
        return cl;
    }

    @Override
    public ArtifactFactory getArtifactFactory() {
        return new SEArtifactFactory();
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public UserTransaction getUserTransaction() {
        return com.arjuna.ats.jta.UserTransaction.userTransaction();
    }

    @Override
    public Properties getBatchConfigurationProperties() {
        Properties result = configProperties;
        if (result == null) {
            synchronized (this) {
                result = configProperties;
                if (result == null) {
                    result = new Properties();
                    final InputStream configStream = getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                    if (configStream != null) {
                        try {
                            result.load(configStream);
                        } catch (IOException e) {
                            throw BatchLogger.LOGGER.failToLoadConfig(e, CONFIG_FILE_NAME);
                        }
                    } else {
                        BatchLogger.LOGGER.useDefaultJBeretConfig(CONFIG_FILE_NAME);
                    }
                    configProperties = result;
                }
            }
        }
        return result;
    }

    @Override
    public ThreadContextSetup getThreadContextSetup() {
        return NO_OP_THREAD_CONTEXT_SETUP;
    }

    @Override
    public <T> T lookup(final String name) throws NamingException {
        return InitialContext.doLookup(name);
    }
}