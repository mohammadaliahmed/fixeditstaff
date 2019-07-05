package com.fixedit.fixeditstaff.Models;

public class ServiceModel {
    private String id,name,description;
    boolean active,deleted;
    int serviceBasePrice,peakPrice;
    String imageUrl;

    public ServiceModel() {
    }

    public ServiceModel(String id, String name, String description, boolean active, boolean deleted, int serviceBasePrice, int peakPrice, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.serviceBasePrice = serviceBasePrice;
        this.peakPrice = peakPrice;
        this.imageUrl = imageUrl;
    }

    public int getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(int peakPrice) {
        this.peakPrice = peakPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getServiceBasePrice() {
        return serviceBasePrice;
    }

    public void setServiceBasePrice(int serviceBasePrice) {
        this.serviceBasePrice = serviceBasePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
