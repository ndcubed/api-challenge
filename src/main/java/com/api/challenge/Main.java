package com.api.challenge;

import com.api.challenge.model.*;
import com.api.challenge.service.DatasetService;
import com.api.challenge.service.DealerService;
import com.api.challenge.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class Main {

    private DatasetService datasetService;
    private VehicleService vehicleService;
    private DealerService dealerService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            Dataset dataset = restTemplate.getForObject("http://api.coxauto-interview.com/api/datasetId", Dataset.class);

            if (dataset != null) {
                // Obtain List of Vehicle in parallel.
                VehicleIdList vehicleIdList = vehicleService.getVehicleIdsForDataset(dataset);
                List<Vehicle> vehicleList = vehicleService.getAllVehiclesForIds(dataset, vehicleIdList.getVehicleIds());

                /*
                Obtain List of dealer in parallel and group Vehicle List by dealer
                to allow for easy mapping of Vehicle Objects to appropriate Dealer.
                 */
                long[] dealerIds = vehicleList.stream().mapToLong(Vehicle::getDealerId).distinct().toArray();
                Map<Long, Dealer> dealers = dealerService.getDealersForIds(dataset, dealerIds).stream().collect(Collectors.toMap(Dealer::getDealerId, Function.identity()));
                Map<Long, List<Vehicle>> vehiclesGrouped = vehicleList.stream().collect(Collectors.groupingBy(Vehicle::getDealerId));
                for (Map.Entry<Long, List<Vehicle>> entry : vehiclesGrouped.entrySet()) {
                    Dealer dealer = dealers.get(entry.getKey());
                    dealer.setVehicles(entry.getValue());
                }

                // Send results to answer endpoint.
                List<Dealer> retVal = new ArrayList<>(dealers.values());
                AnswerResponse response = restTemplate.postForObject(new URI("http://api.coxauto-interview.com/api/" + dataset.getDatasetId() + "/answer"), new DealersDTO(retVal), AnswerResponse.class);

                // Output AnswerResponse.
                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
            }
            System.exit(0);
        };
    }

    public DealerService getDealerService() {
        return dealerService;
    }

    @Autowired
    public void setDealerService(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    @Autowired
    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public DatasetService getDatasetService() {
        return datasetService;
    }

    @Autowired
    public void setDatasetService(DatasetService datasetService) {
        this.datasetService = datasetService;
    }
}
