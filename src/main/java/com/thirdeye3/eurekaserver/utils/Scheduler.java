package com.thirdeye3.eurekaserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thirdeye3.eurekaserver.dtos.Response;
import com.thirdeye3.eurekaserver.externalcontrollers.SelfClient;
@Component
public class Scheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
    @Autowired
    SelfClient selfClient;
    
    @Value("${thirdeye.uniqueId}")
    private Integer uniqueId;

    @Value("${thirdeye.uniqueCode}")
    private String uniqueCode;
	
	@Scheduled(fixedRate = 30000)
    public void checkStatusTask() {
        Response<String> response = selfClient.statusChecker(uniqueId, uniqueCode);
        logger.info("Status check response is {}", response.getResponse());
    }

}
