/**
 * Copyright 2010 CosmoCode GmbH
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

package de.cosmocode.palava.datasource.bitronix;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.cosmocode.palava.core.lifecycle.Disposable;
import de.cosmocode.palava.core.lifecycle.Initializable;
import de.cosmocode.palava.core.lifecycle.LifecycleException;
import de.cosmocode.palava.datasource.DataSourceConfig;
import de.cosmocode.palava.datasource.ForwardingDataSource;
import de.cosmocode.palava.jndi.JndiContextBinderUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * A serice which loads a datasource and binds it in jndi.
 * 
 * @author Tobias Sarnowski
 */
final class BitronixDataSource extends ForwardingDataSource implements Initializable, Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(BitronixDataSource.class);

    private JndiContextBinderUtility jndiContextBinderUtility;

    private final String unique;
    private final String jndiName;
    private final String driver;
    private final Properties properties;
    private final int poolMax;

    private final int poolMin;
    private final PoolingDataSource dataSource = new PoolingDataSource();

    @Inject
    public BitronixDataSource(
        JndiContextBinderUtility jndiContextBinderUtility,
        @Named(DataSourceConfig.UNIQUE) String unique,
        @Named(DataSourceConfig.JNDI_NAME) String jndiName,
        @Named(DataSourceConfig.DRIVER) String driver,
        @Named(DataSourceConfig.PROPERTIES) Properties properties,
        @Named(DataSourceConfig.POOL_MAX) int poolMax,
        @Named(DataSourceConfig.POOL_MIN) int poolMin) {

        this.jndiContextBinderUtility = jndiContextBinderUtility;
        this.unique = Preconditions.checkNotNull(unique, "Unique");
        this.jndiName = Preconditions.checkNotNull(jndiName, "JndiName");
        this.driver = Preconditions.checkNotNull(driver, "Driver");
        this.properties = Preconditions.checkNotNull(properties, "Properties");
        this.poolMax = poolMax;
        this.poolMin = poolMin;
    }

    @Override
    public void initialize() throws LifecycleException {
        dataSource.setUniqueName(unique);
        dataSource.setClassName(driver);
        dataSource.setMaxPoolSize(poolMax);
        dataSource.setMinPoolSize(poolMin);
        dataSource.getDriverProperties().putAll(properties);
        dataSource.init();

        try {
            LOG.info("Binding DataSource {} to {} [{}]", new Object[] {
                unique, jndiName, dataSource
            });
            jndiContextBinderUtility.bind(jndiName, dataSource);
        } catch (NamingException e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public void dispose() throws LifecycleException {
        dataSource.close();
    }

    @Override
    protected DataSource delegate() {
        return dataSource;
    }
}
