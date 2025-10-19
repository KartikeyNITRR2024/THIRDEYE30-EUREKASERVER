package com.thirdeye3.eurekaserver.filters;

import com.thirdeye3.eurekaserver.dtos.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyFilter extends OncePerRequestFilter {

    private String selfUrl;
    private String apiKey;
    private String bypassDiscoveryIdentityName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setBypassDiscoveryIdentityName(String bypassDiscoveryIdentityName) {
        this.bypassDiscoveryIdentityName = bypassDiscoveryIdentityName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String discoveryIdentity = request.getHeader("discoveryidentity-name");
        if (discoveryIdentity != null && discoveryIdentity.equalsIgnoreCase(bypassDiscoveryIdentityName)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestApiKey = request.getHeader("THIRDEYE-API-KEY");
        if (apiKey != null && apiKey.equals(requestApiKey)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Response<String> res = new Response<>(false, 401, "Invalid API Key", null);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        }
    }
}
