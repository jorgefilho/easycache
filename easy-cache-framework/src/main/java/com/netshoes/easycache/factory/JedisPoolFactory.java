package com.netshoes.easycache.factory;

import static com.netshoes.integration.cache.factory.constants.RedisConstants.DEFAULT_HOST;
import static com.netshoes.integration.cache.factory.constants.RedisConstants.DEFAULT_PORT;
import static com.netshoes.integration.cache.factory.constants.RedisConstants.DEFAULT_TIMEOUT;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netshoes.integration.api.properties.ApplicationProperties;
import com.netshoes.integration.api.properties.CacheProperties;

import redis.clients.jedis.JedisPool;

@ApplicationScoped
public class JedisPoolFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolFactory.class);
	
	@Inject
	private ApplicationProperties applicationProperties;
	
    private JedisPool jedisPool;
    
    @ApplicationScoped
    @Produces
    public  JedisPool getJedisPool(){
    	final CacheProperties cacheProperties = applicationProperties.getCacheProperties();
    	
    	String  host = DEFAULT_HOST;
    	Integer port = DEFAULT_PORT;
    	Integer timeout = DEFAULT_TIMEOUT;
    	
    	
    	if (cacheProperties == null){
    		LOGGER.error("It wasn't possible load the cache properties. Application is assumed the DEFAULT settings.");
    	}
    	else{
    		host = cacheProperties.getHost();
    		port = cacheProperties.getPort();
    		timeout = cacheProperties.getTimeout();
    	}
    	
        this.jedisPool = new JedisPool(new GenericObjectPoolConfig(), host, port, timeout);
        return this.jedisPool;
    }
 
    public void detroy(@Disposes JedisPool jedisPool){
        this.jedisPool = jedisPool;
		jedisPool.destroy();
    }
}
