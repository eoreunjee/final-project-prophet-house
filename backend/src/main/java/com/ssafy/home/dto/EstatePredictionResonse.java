package com.ssafy.home.dto;

import java.util.List;

public class EstatePredictionResonse {
	private String regionDongName;
    private int targetYear;
    private List<Double> predictedPrices;
    private List<String> dates;
    private double meanPrice;

    public EstatePredictionResonse() {}

    public EstatePredictionResonse(String regionDongName, int targetYear, List<Double> predictedPrices, List<String> dates, double meanPrice) {
        this.regionDongName = regionDongName;
        this.targetYear = targetYear;
        this.predictedPrices = predictedPrices;
        this.dates = dates;
        this.meanPrice = meanPrice;
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

    public List<Double> getPredictedPrices() {
        return predictedPrices;
    }

    public void setPredictedPrices(List<Double> predictedPrices) {
        this.predictedPrices = predictedPrices;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public double getMeanPrice() {
        return meanPrice;
    }

    public void setMeanPrice(double meanPrice) {
        this.meanPrice = meanPrice;
    }
}
