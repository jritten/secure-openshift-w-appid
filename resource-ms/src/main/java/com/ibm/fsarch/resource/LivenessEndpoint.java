package com.ibm.fsarch.resource;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

// Simple custom liveness check
@Endpoint(id = "liveness")
@Component
public class LivenessEndpoint {

    @ReadOperation
    public String testLiveness() {
        return "{\"status\":\"UP\"}";
    }

}
