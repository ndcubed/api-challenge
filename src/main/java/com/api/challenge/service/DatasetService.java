package com.api.challenge.service;

import com.api.challenge.model.Dataset;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatasetService {

    final RestTemplate restTemplate;

    public DatasetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Dataset getDataset() {
        return restTemplate.getForObject("http://api.coxauto-interview.com/api/datasetId", Dataset.class);
    }
}
