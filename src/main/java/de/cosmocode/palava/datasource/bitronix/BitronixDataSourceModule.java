package de.cosmocode.palava.datasource.bitronix;

import de.cosmocode.palava.datasource.AbstractDataSourceModule;
import de.cosmocode.palava.datasource.DataSourceProvider;

import java.lang.annotation.Annotation;

/**
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
	protected Class<? extends DataSourceProvider> getDataSourceProvider() {
		return BitronixDataSource.class;
	}
}
