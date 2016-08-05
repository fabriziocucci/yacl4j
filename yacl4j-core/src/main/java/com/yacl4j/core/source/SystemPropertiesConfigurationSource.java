package com.yacl4j.core.source;

import java.util.Properties;

class SystemPropertiesConfigurationSource extends AbstractPropertiesConfigurationSource {
	
	@Override
	protected Properties getProperties() {
		return (Properties) System.getProperties().clone();
	}
	
}
