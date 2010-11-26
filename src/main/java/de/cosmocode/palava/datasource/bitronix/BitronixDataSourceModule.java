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

import java.lang.annotation.Annotation;

import javax.sql.DataSource;

import de.cosmocode.palava.datasource.AbstractDataSourceModule;

/**
 * Bitronix specific {@link AbstractDataSourceModule}.
 * 
 * @author Tobias Sarnowski
 */
public final class BitronixDataSourceModule extends AbstractDataSourceModule {

    public BitronixDataSourceModule(String name) {
        super(name);
    }

    public BitronixDataSourceModule(Class<? extends Annotation> annotation, String name) {
        super(annotation, name);
    }

    public BitronixDataSourceModule(Annotation annotation, String name) {
        super(annotation, name);
    }

    
    @Override
    protected Class<? extends DataSource> getDataSourceFactory() {
        return BitronixDataSource.class;
    }
    
}

