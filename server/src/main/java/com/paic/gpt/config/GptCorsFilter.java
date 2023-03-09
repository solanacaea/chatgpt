package com.paic.gpt.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GptCorsFilter extends CorsFilter {

	public GptCorsFilter() {
		super(configurationSource());
	}

	private static UrlBasedCorsConfigurationSource configurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest");
		List<String> exposedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest");
		List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
		List<String> allowedOrigins = Collections.singletonList("*");
		corsConfig.setAllowedHeaders(allowedHeaders);
		corsConfig.setAllowedMethods(allowedMethods);
//		corsConfig.setAllowedOrigins(allowedOrigins);
		corsConfig.setAllowedOriginPatterns(allowedOrigins);
		corsConfig.setExposedHeaders(exposedHeaders);
		corsConfig.setMaxAge(36000L);
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

}
