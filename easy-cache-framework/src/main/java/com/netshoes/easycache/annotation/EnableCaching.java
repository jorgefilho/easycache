package com.netshoes.easycache.annotation;

import static com.netshoes.easycache.constants.ExpireTime.ETERNAL;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

@InterceptorBinding
@Retention(RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface EnableCaching {
	@Nonbinding
	int expireInSeconds() default ETERNAL;
}