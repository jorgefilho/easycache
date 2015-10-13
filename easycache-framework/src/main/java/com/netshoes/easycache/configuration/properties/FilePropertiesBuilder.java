package com.netshoes.easycache.configuration.properties;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netshoes.easycache.configuration.properties.exception.FilePropertiesBuilderException;

@RequestScoped
public class FilePropertiesBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FilePropertiesBuilder.class);
	private static final Integer REFRESH_TIME_IN_MILLISECONDS = 600000;

	private PropertiesConfiguration propertyConfiguration = null;
	private File file = null;
	private long refreshTime = REFRESH_TIME_IN_MILLISECONDS;

	public FilePropertiesBuilder refreshTime(final long refreshTime) {
		this.refreshTime = refreshTime;
		return this;
	}

	public String getString(final String key) {
		return isValidKey(key) ? this.propertyConfiguration.getString(key) : null;
	}
	
	public List<String> getListOfString(final String key) {
		List<String> listOfString = new ArrayList<String>();
		if (isValidKey(key)) {
			listOfString = asList(this.propertyConfiguration.getStringArray(key));
		}
		return listOfString;
	}

	public FilePropertiesBuilder build() {
		try {
			
			String sourceFile = format("%s%s", ApplicationPropertiesConstants.ABSOLUTE_PATH, FileProperties.APPLICATION.getFilename());
			String propertyPath = System.getProperty(EXTERNAL_PATH_PROPERTY);
			
			if (StringUtils.isNotBlank(propertyPath)){
				sourceFile = propertyPath;
			}
			
			LOGGER.info("Load external file: {}", sourceFile);
			this.file = new File(sourceFile);
			
			if (this.file == null || !this.file.exists()) {
				LOGGER.error("File not found");
				throw new FilePropertiesBuilderException("Fail in build file properties, file not found.");
			}
			processPropertyConfiguration();
		} catch (Exception e) {
			throw new FilePropertiesBuilderException("Fail in build file properties.", e);
		}
		return this;
	}

	private boolean isValidKey(final String key) {
		return key != null && this.propertyConfiguration != null;
	}

	private void processPropertyConfiguration() {
		try {
			this.propertyConfiguration = new PropertiesConfiguration(this.file);
			final FileChangedReloadingStrategy reload = new FileChangedReloadingStrategy();
			reload.setConfiguration(this.propertyConfiguration);
			reload.setRefreshDelay(this.refreshTime);
			this.propertyConfiguration.setReloadingStrategy(reload);
		} catch (ConfigurationException e) {
			LOGGER.error("Fail in configuration of reloading file strategy.", e);
			throw new FilePropertiesBuilderException("Fail in configuration of reloading file strategy.", e);
		}
	}
}