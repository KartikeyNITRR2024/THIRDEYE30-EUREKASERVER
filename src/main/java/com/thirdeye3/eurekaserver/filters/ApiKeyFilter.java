package com.thirdeye3.eurekaserver.filters;

import com.thirdeye3.eurekaserver.dtos.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiKeyFilter extends OncePerRequestFilter {

    private String selfUrl;
    private String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSelfUrl(String selfUrl) {
        this.selfUrl = selfUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader("THIRDEYE-API-KEY");
        String remoteAddr = request.getRemoteAddr();
        URL url = new URL(selfUrl);
        InetAddress selfHost = InetAddress.getByName(url.getHost());
        InetAddress remoteHost = InetAddress.getByName(remoteAddr);
        System.out.println("selfUrl "+selfUrl);
        System.out.println("url.getHost() "+url.getHost());
        System.out.println("requestApiKey "+requestApiKey);
        System.out.println("remoteAddr "+remoteAddr);
        if ((requestApiKey == null && remoteHost.equals(selfHost)) || (apiKey != null && apiKey.equals(requestApiKey))) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Response<String> res = new Response<>(false, 401, "Invalid API Key", null);
            response.getWriter().write(objectMapper.writeValueAsString(res));
        }
    }
}
