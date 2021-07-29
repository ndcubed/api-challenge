package com.api.challenge.service;

import com.api.challenge.model.Dataset;
import com.api.challenge.model.Vehicle;
import com.api.challenge.model.VehicleIdList;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class VehicleService extends BasicService {

    public VehicleIdList getVehicleIdsForDataset(Dataset dataset) {
        return doGet("http://api.coxauto-interview.com/api/" + dataset.getDatasetId() + "/vehicles", VehicleIdList.class);
    }

    public List<Vehicle> getAllVehiclesForIds(Dataset dataset, long[] vehicleIds) throws InterruptedException {
        List<Callable<Vehicle>> callables = createCallables(Arrays.stream(vehicleIds).mapToObj(id -> (Supplier<Vehicle>) () -> doGet("http://api.coxauto-interview.com/api/" + dataset.getDatasetId() + "/vehicles/" + id, Vehicle.class)).collect(Collectors.toList()));
        return processRequests(callables);
    }
}
