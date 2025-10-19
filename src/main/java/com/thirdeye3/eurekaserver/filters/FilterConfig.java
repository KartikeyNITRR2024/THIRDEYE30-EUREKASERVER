package com.thirdeye3.eurekaserver.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${thirdeye.api.key}")
    private String apiKey;

    @Value("${self.url}")
    private String selfUrl;

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        ApiKeyFilter apiKeyFilter = new ApiKeyFilter();
        apiKeyFilter.setApiKey(apiKey);
        apiKeyFilter.setSelfUrl(selfUrl);
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/eureka/*", "/es/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
