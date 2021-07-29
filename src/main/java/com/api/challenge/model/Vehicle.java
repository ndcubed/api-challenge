package com.api.challenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vehicle {
    private long vehicleId;
    private int year;
    private String make;
    private String model;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long dealerId;

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }
}
