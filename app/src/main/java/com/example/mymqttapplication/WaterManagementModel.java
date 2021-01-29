package com.example.mymqttapplication;

public class WaterManagementModel {
    private int id;
    private String currentDate;
    private String val;

    public WaterManagementModel(int id,String currentDate, String val) {
        this.id = id;
        this.currentDate=currentDate;
        this.val = val;
    }

    @Override
    public String toString() {
        return "WaterManagementModel{" +
                "id=" + id +
                ", currentDate='" + currentDate + '\'' +
                ", val='" + val + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
