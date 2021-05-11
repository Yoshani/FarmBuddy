package com.huawei.farmfinder;

import com.huawei.hms.site.api.model.Coordinate;

public class Farm {
    private int id;
    private String name;
    private String owner;
    private Coordinate location;
    private String imageUrl;
    private String description;

    public Farm(int id, String name, String owner, Coordinate location, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", location=" + location +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
