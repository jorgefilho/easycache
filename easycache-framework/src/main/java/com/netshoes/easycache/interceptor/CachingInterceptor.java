package com.netshoes.easycache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netshoes.easycache.adapter.ClassTypeAdapter;
import com.netshoes.easycache.annotation.EnableCaching;
import com.netshoes.easycache.configuration.properties.ApplicationProperties;
import com.netshoes.easycache.constants.ExpireTime;
import com.netshoes.easycache.domain.Envelope;
import com.netshoes.easycache.repository.CacheRepository;

import redis.clients.jedis.exceptions.JedisConnectionException;

@Interceptor
@EnableCaching
public class CachingInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LoggerFactory.getLogger(CachingInterceptor.class);

	private Gson gson;

	@Inject
	private CacheRepository cacheRepository;

	@Inject
	private ApplicationProperties applicationProperties;

	public CachingInterceptor() {
		gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassTypeAdapter()).create();
	}

	@AroundInvoke
	public Object perform(final InvocationContext ctx) throws Exception {
		Object objectToReturn = null;
		try {
			if (applicationProperties.getEasyCacheProperties().isEnable()) {
				objectToReturn = getReturnOfCache(ctx);
			} else {
				objectToReturn = ctx.proceed();
			}
		} catch (JedisConnectionException ex) {
			LOGGER.error("Problems to connect in the Redis Server.");
			objectToReturn = ctx.proceed();
		}

		return objectToReturn;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getReturnOfCache(final InvocationContext ctx) throws Exception {
		Object objectToReturn = null;

		final String key = getKey(ctx.getMethod(), ctx.getParameters());
		final String json = cacheRepository.get(key);

		if (json == null) {
			objectToReturn = ctx.proceed();

			final int timeToExpire = getTime(ctx.getMethod());

			final Envelope envelope = new Envelope(gson.toJson(objectToReturn), objectToReturn.getClass());

			String statusCode;
			if (timeToExpire == ExpireTime.ETERNAL) {
				statusCode = cacheRepository.set(key, gson.toJson(envelope));
			} else {
				statusCode = cacheRepository.setex(key, timeToExpire, gson.toJson(envelope));
			}

			if (!statusCode.equals("OK")) {
				LOGGER.warn("Problems in recording cache - status code {}", statusCode);
			}
		} else {
			final Envelope envelope = gson.fromJson(json, Envelope.class);
			final Class type = envelope.getTypeOfJson();

			objectToReturn = gson.fromJson(envelope.getJson(), type);

			if (objectToReturn == null) {
				objectToReturn = ctx.proceed();
				LOGGER.warn("Problems whith the object type - Type Envelop {}", type);
			}
		}

		return objectToReturn;
	}

	private int getTime(Method method) {
		EnableCaching enableCaching = method.getAnnotation(EnableCaching.class);

		if (enableCaching == null) {
			enableCaching = method.getDeclaringClass().getAnnotation(EnableCaching.class);
		}
		final int timeToExpire = enableCaching.expireInSeconds();

		return timeToExpire;
	}

	private String getKey(Method method, Object[] parameters) {
		final String parametersInLineCustom = Arrays.toString(parameters).replace(" ", "").replace("null", "");

		final String key = method.getDeclaringClass().getSimpleName() + method.getName() + parametersInLineCustom;

		return key;
	}
}