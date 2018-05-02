package com.example.hardik.findride;

import android.support.v7.widget.RecyclerView;

public class MyRideDetails  {
    String startAddress;
    String endAddress;
    String starTime;
    String typeOfRide;
    public  MyRideDetails(){

    }
    public MyRideDetails(String startAddress, String endAddress, String starTime, String typeOfRide) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.starTime = starTime;
        this.typeOfRide = typeOfRide;
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

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getTor() {
        return typeOfRide;
    }

    public void setTor(String typeOfRide) {
        this.typeOfRide = typeOfRide;
    }


}
