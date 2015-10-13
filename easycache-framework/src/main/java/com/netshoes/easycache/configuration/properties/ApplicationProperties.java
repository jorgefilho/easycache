package com.netshoes.easycache.configuration.properties;

import java.io.Serializable;

import com.netshoes.easycache.configuration.properties.bean.EasyCacheProperties;

public interface ApplicationProperties extends Serializable {
	int REFRESH_TIME_IN_MILLISECONDS = 600000;
	EasyCacheProperties getEasyCacheProperties();
}