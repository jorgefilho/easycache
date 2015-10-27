package com.netshoes.easycache.repository.impl;

import javax.inject.Inject;

import com.netshoes.easycache.repository.CacheRepository;

import redis.clients.jedis.Jedis;

public class CacheRepositoryImpl implements CacheRepository {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Jedis jedis;
	
	@Override
	public String setex(String key, int timeToExpire, String json){
		final String statusCode = jedis.setex(key, timeToExpire, json);
		return statusCode;
	}
	
	@Override
	public String set(String key, String json){
		final String statusCode = jedis.set(key, json);
		return statusCode;
	}

	@Override
	public String get(String key) {
		return jedis.get(key);
	}
}
