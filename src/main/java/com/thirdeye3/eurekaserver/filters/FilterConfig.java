package com.thirdeye3.eurekaserver.filters;

import com.thirdeye3.eurekaserver.filters.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${thirdeye.api.key}")
    private String apiKey;

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        ApiKeyFilter apiKeyFilter = new ApiKeyFilter();
        apiKeyFilter.setApiKey(apiKey);
        
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/eureka/*");
        return registrationBean;
    }
}
