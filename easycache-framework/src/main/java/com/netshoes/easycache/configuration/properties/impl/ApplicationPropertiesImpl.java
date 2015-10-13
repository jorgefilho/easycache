package com.netshoes.easycache.configuration.properties.impl;

import static com.netshoes.easycache.configuration.properties.constants.EasyCacheConstants.ENABLE;
import static com.netshoes.easycache.configuration.properties.constants.EasyCacheConstants.REDIS_HOST;
import static com.netshoes.easycache.configuration.properties.constants.EasyCacheConstants.REDIS_PORT;
import static com.netshoes.easycache.configuration.properties.constants.EasyCacheConstants.REDIS_TIMEOUT;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netshoes.easycache.configuration.properties.ApplicationProperties;
import com.netshoes.easycache.configuration.properties.FilePropertiesBuilder;
import com.netshoes.easycache.configuration.properties.bean.EasyCacheProperties;

@ApplicationScoped
public class ApplicationPropertiesImpl implements ApplicationProperties {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationPropertiesImpl.class);
	
	private FilePropertiesBuilder filePropertiesBuilder;
	
	@PostConstruct
	public void init(){
		this.filePropertiesBuilder = new FilePropertiesBuilder().build();
	}

	public EasyCacheProperties getEasyCacheProperties() {
		
		EasyCacheProperties easyCacheProperties = new EasyCacheProperties();

		try {
			final String cacheEnable = getProperties(ENABLE);
			final String redisPort = getProperties(REDIS_PORT);
			final String redisTimeout = getProperties(REDIS_TIMEOUT);

			easyCacheProperties.setEnable(Boolean.parseBoolean(cacheEnable));
			easyCacheProperties.setRedisHost(getProperties(REDIS_HOST));
			easyCacheProperties.setRedisPort(Integer.parseInt(redisPort));
			easyCacheProperties.setRedisTimeout(Integer.parseInt(redisTimeout));
			
		} catch (Exception e) {
			LOGGER.error("Can not load EasyCacheProperties on application properties. Will use default values. {}", e.getMessage(), e);
		}
		return easyCacheProperties;
	}
	public String getProperties(final String key, final String... replaces) throws IOException {
		String value = this.filePropertiesBuilder.getString(key);
		
		if (value != null && isNotEmpty(Arrays.asList(replaces))){
			value = String.format(value, (Object[])replaces);
		}
		return value;
	}
}