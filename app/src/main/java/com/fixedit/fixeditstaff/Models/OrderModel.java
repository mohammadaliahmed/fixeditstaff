package com.fixedit.fixeditstaff.Models;

import java.util.ArrayList;

public class OrderModel {
    long orderId;
    long time;
    User user;
    ArrayList<ServiceCountModel> countModelArrayList;
    long totalPrice;
    String instructions;
    String date, chosenTime;
    String orderStatus;
    String orderAddress, googleAddress;
    double lat, lon;
    long billNumber;
    String buildingType;
    long totalHours;
    String serviceName;
    boolean assigned;
    String assignedTo;
    String assignedToName;

    public OrderModel(long orderId, long time, User user, ArrayList<ServiceCountModel> countModelArrayList,
                      long totalPrice, long totalHours, String instructions, String date, String chosenTime,
                      String orderStatus, String orderAddress, String googleAddress,
                      double lat, double lon,
                      String buildingType, String serviceName) {
        this.orderId = orderId;
        this.time = time;
        this.user = user;
        this.countModelArrayList = countModelArrayList;
        this.totalPrice = totalPrice;
        this.instructions = instructions;
        this.date = date;
        this.chosenTime = chosenTime;
        this.orderStatus = orderStatus;
        this.orderAddress = orderAddress;
        this.googleAddress = googleAddress;
        this.lat = lat;
        this.lon = lon;
        this.buildingType = buildingType;
        this.totalHours = totalHours;
        this.serviceName = serviceName;
    }

    public OrderModel() {
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours = totalHours;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ServiceCountModel> getCountModelArrayList() {
        return countModelArrayList;
    }

    public void setCountModelArrayList(ArrayList<ServiceCountModel> countModelArrayList) {
        this.countModelArrayList = countModelArrayList;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getGoogleAddress() {
        return googleAddress;
    }

    public void setGoogleAddress(String googleAddress) {
        this.googleAddress = googleAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    public long getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(long billNumber) {
        this.billNumber = billNumber;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
}
