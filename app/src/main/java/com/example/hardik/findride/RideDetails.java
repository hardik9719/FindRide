package com.example.hardik.findride;

public class RideDetails {
    private String startAddress;
    private String endAddress;
    private String TypeOfRide;

    public RideDetails(String startAddress, String endAddress, String typeOfRide) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        TypeOfRide = typeOfRide;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getTypeOfRide() {
        return TypeOfRide;
    }

    public void setTypeOfRide(String typeOfRide) {
        TypeOfRide = typeOfRide;
    }



}
