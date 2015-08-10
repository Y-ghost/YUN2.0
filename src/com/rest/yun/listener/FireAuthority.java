package com.rest.yun.listener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @project:					yun 
 * @Title: 						FireAuthority.java 		
 * @Package 					com.rest.yun.listener		
 * @Description: 				建立annotation类, 用于标注到需要权限验证的地方 
 * @author 						杨贵松   
 * @date 						2014年2月21日 上午12:00:14 
 * @version 					V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FireAuthority {
	AuthorityType[] authorityTypes();

	ResultTypeEnum resultType() default ResultTypeEnum.json;
}
