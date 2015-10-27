package com.netshoes.easycache.repository;

import java.io.Serializable;

public interface CacheRepository extends Serializable{

	String setex(String key, int timeToReload, String json);

	String get(String string);

	String set(String key, String json);
}
