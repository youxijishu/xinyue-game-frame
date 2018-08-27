package com.xinyue.web.app.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpErrorPageConfig {

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				// container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,
				// "/app/index"));
				// container.addErrorPages(new
				// ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/app/index"));
				// container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
				// "/app/index"));
			}
		};
	}

}
