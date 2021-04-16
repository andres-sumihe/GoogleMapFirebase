package com.andflow.googlemapfirebase;

public class LocationModel {
    private Double latitude;
    private Double langitude;

    public Double getLangitude() {
        return langitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLangitude(Double langitude) {
        this.langitude = langitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public LocationModel(){

    }
    public LocationModel(Double latitude, Double langitude){
        this.langitude = langitude;
        this.latitude = latitude;
    }

}
