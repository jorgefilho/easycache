package com.netshoes.easycache.configuration.properties.bean;

import java.io.Serializable;

public class EasyCacheProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean enable;
	
	private String redisHost;
	
	private Integer redisPort;
	
	private Integer redisTimeout;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(final boolean enable) {
		this.enable = enable;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(final String redisHost) {
		this.redisHost = redisHost;
	}

	public Integer getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(final Integer redisPort) {
		this.redisPort = redisPort;
	}

	public Integer getRedisTimeout() {
		return redisTimeout;
	}

	public void setRedisTimeout(final Integer redisTimeout) {
		this.redisTimeout = redisTimeout;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enable ? 1231 : 1237);
		result = prime * result + ((redisHost == null) ? 0 : redisHost.hashCode());
		result = prime * result + ((redisPort == null) ? 0 : redisPort.hashCode());
		result = prime * result + ((redisTimeout == null) ? 0 : redisTimeout.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EasyCacheProperties other = (EasyCacheProperties) obj;
		if (enable != other.enable)
			return false;
		if (redisHost == null) {
			if (other.redisHost != null)
				return false;
		} else if (!redisHost.equals(other.redisHost))
			return false;
		if (redisPort == null) {
			if (other.redisPort != null)
				return false;
		} else if (!redisPort.equals(other.redisPort))
			return false;
		if (redisTimeout == null) {
			if (other.redisTimeout != null)
				return false;
		} else if (!redisTimeout.equals(other.redisTimeout))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EasyCacheProperties [enable=" + enable + ", redisHost=" + redisHost + ", redisPort=" + redisPort
				+ ", redisTimeout=" + redisTimeout + "]";
	}
}