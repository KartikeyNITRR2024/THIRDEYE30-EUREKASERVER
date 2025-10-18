package com.thirdeye3.eurekaserver.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdeye3.eurekaserver.dtos.EurekaServiceInfo;
import com.thirdeye3.eurekaserver.dtos.Response;
import com.thirdeye3.eurekaserver.services.EurekaSummary;

@RestController
@RequestMapping("/es/eurekasummary")
public class EurekaSummaryController {
	
	private static final Logger logger = LoggerFactory.getLogger(EurekaSummaryController.class);
	
	@Autowired
	private EurekaSummary eurekaSummary;
	
    @GetMapping()
    public Response<List<EurekaServiceInfo>> getEurekaSummary() {
    	logger.info("Going to get eureka summary");
        return new Response<>(true,0,null,eurekaSummary.getEurekaSummary());
    }
}
