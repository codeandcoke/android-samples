package com.codeandcoke.bizistations.dto;

import com.codeandcoke.bizistations.domain.BiziStation;

public class StationInfoDTO {

    public String id;
    public String street;
    public String state;
    public int availableBikes;
    public int availableSlots;
    public long distance;

    public static StationInfoDTO from(BiziStation station) {
        StationInfoDTO stationInfoDTO = new StationInfoDTO();
        stationInfoDTO.id = station.getId();
        stationInfoDTO.availableSlots = station.getAvailableSlots();
        stationInfoDTO.availableBikes = station.getAvailableBikes();
        stationInfoDTO.state = station.getStreet();

        return stationInfoDTO;
    }
}
