package com.thirdeye3.eurekaserver.services;

import java.util.List;

import com.thirdeye3.eurekaserver.dtos.EurekaServiceInfo;

public interface EurekaSummary {

	List<EurekaServiceInfo> getEurekaSummary();

}
