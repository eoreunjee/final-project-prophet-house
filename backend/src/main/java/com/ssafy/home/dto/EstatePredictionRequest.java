package com.ssafy.home.dto;

public class EstatePredictionRequest {
	private String regionDongName;
    private int targetYear;

    public EstatePredictionRequest() {}

    public EstatePredictionRequest(String regionDongName, int targetYear) {
        this.regionDongName = regionDongName;
        this.targetYear = targetYear;
    }

    public String getRegionDongName() {
        return regionDongName;
    }

    public void setRegionDongName(String regionDongName) {
        this.regionDongName = regionDongName;
    }

    public int getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(int targetYear) {
        this.targetYear = targetYear;
    }

}
