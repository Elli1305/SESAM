package com.gpse.sesam;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Responsible for serving the SPA.
 * <p>
 * Serves the single page application (the vue frontend).
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * Redirects necessary routes to frontend.
     * <p>
     * Redirects all routes not set to the frontend index.html.
     * Explicitly excludes '/index.html' to avoid self looping callback.
     * Explicitly excludes '/static/**' files to load .css and .js files properly.
     */
    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(final ViewControllerRegistry registry) {
                registry.addViewController("{_:^(?!static|index\\.html).*$}/**")
                        .setViewName("forward:/");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.GERMAN);

        return resolver;
    }
}
