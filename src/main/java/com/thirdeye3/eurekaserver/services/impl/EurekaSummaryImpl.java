package com.thirdeye3.eurekaserver.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thirdeye3.eurekaserver.dtos.EurekaServiceInfo;
import com.thirdeye3.eurekaserver.exceptions.EurekaSummaryException;
import com.thirdeye3.eurekaserver.services.EurekaSummary;

@Service
public class EurekaSummaryImpl implements EurekaSummary {

    private static final Logger logger = LoggerFactory.getLogger(EurekaSummaryImpl.class);

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServerUrl;

    @Value("${thirdeye.api.key}")
    private String apiKey;

    @Override
    public List<EurekaServiceInfo> getEurekaSummary() {
        logger.info("Fetching Eureka summary from: {}", eurekaServerUrl);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = eurekaServerUrl.replace("/eureka/", "/eureka/apps");
            logger.debug("Final Eureka apps endpoint: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("THIRDEYE-API-KEY", apiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
            logger.trace("Raw Eureka JSON Response: {}", result);

            JSONObject json = new JSONObject(result);
            JSONArray appsArray = json.getJSONObject("applications").optJSONArray("application");
            if (appsArray == null) {
                logger.warn("No 'application' array found in Eureka response.");
                return new ArrayList<>();
            }

            List<EurekaServiceInfo> list = new ArrayList<>();
            for (int i = 0; i < appsArray.length(); i++) {
                JSONObject app = appsArray.getJSONObject(i);
                String name = app.optString("name", "UNKNOWN");

                Object instanceObj = app.opt("instance");
                JSONArray instances = (instanceObj instanceof JSONArray)
                        ? app.getJSONArray("instance")
                        : new JSONArray().put(instanceObj);

                int total = instances.length();
                int upCount = 0;

                for (int j = 0; j < instances.length(); j++) {
                    JSONObject instance = instances.getJSONObject(j);
                    String status = instance.optString("status", "UNKNOWN");
                    if ("UP".equalsIgnoreCase(status)) upCount++;
                }

                list.add(new EurekaServiceInfo(name, total, upCount));
                logger.info("Service: {}, Total: {}, UP: {}", name, total, upCount);
            }

            logger.info("Eureka summary fetched successfully. Total services: {}", list.size());
            return list;
        } catch (Exception e) {
            logger.error("Error while fetching Eureka summary: {}", e.getMessage(), e);
            throw new EurekaSummaryException("Unable to fetch Eureka summary: " + e.getMessage());
        }
    }
}
