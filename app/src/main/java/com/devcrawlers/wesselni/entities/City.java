package com.devcrawlers.wesselni.entities;

public class City {

    private long id;
    private  String name;
    private long poaple;

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

    public City(long id, String name, long poaple) {
        this.id = id;
        this.name = name;
        this.poaple = poaple;
    }
}
