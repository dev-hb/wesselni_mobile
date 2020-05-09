package com.devcrawlers.wesselni.entities;

public class City {

    private long id;
    private  String name;
    private long poaple;
    private String description;
    private long start; // offres de départ
    private long target; // offres d'arrivée

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoaple() {
        return poaple;
    }

    public void setPoaple(long poaple) {
        this.poaple = poaple;
    }

    @Override
    public String toString() {
        return  name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getTarget() {
        return target;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public City(long id, String name, long poaple) {
        this.id = id;
        this.name = name;
        this.poaple = poaple;
    }

    public City(long id, String name, long poaple, String description, long start, long target) {
        this.id = id;
        this.name = name;
        this.poaple = poaple;
        this.description = description;
        this.start = start;
        this.target = target;
    }
}
