package com.thirdeye3.eurekaserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EurekaServiceInfo {
    private String name;
    private int totalInstances;
    private int upInstances;
}