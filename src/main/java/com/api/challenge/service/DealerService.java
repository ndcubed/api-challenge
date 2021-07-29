package com.api.challenge.service;

import com.api.challenge.model.Dataset;
import com.api.challenge.model.Dealer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class DealerService extends BasicService {

    public List<Dealer> getDealersForIds(Dataset dataset, long[] dealerIds) throws InterruptedException {
        List<Callable<Dealer>> callables = createCallables(Arrays.stream(dealerIds).mapToObj(id -> (Supplier<Dealer>) () -> doGet("http://api.coxauto-interview.com/api/" + dataset.getDatasetId() + "/dealers/" + id, Dealer.class)).collect(Collectors.toList()));
        return processRequests(callables);
    }
}
