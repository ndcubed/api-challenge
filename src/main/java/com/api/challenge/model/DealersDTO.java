package com.api.challenge.model;

import java.util.List;

public class DealersDTO {
    private List<Dealer> dealers;

    public DealersDTO() {}

    public DealersDTO(List<Dealer> dealers) {
        this.dealers = dealers;
    }

    public List<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }
}
