package com.api.challenge.service;

import com.api.challenge.model.Dataset;
import org.springframework.stereotype.Component;

@Component
public class DatasetService extends BasicService {

    public Dataset getDataset() {
        return doGet("http://api.coxauto-interview.com/api/datasetId", Dataset.class);
    }
}
