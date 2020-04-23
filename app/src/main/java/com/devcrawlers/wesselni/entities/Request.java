package com.devcrawlers.wesselni.entities;

import java.util.Date;

public class Request {
    private int id;
    private String startCity,targetCity;
    private Date startdateTime, enddateTime;
    private boolean state;
    private int user_id,nbPlace;
    private String loglat;


    public Request(int id, String startCity, String targetCity, Date startdateTime, Date enddateTime, boolean state, int user_id, int nbPlace, String loglat) {
        this.id = id;
        this.startCity = startCity;
        this.targetCity = targetCity;
        this.startdateTime = startdateTime;
        this.enddateTime = enddateTime;
        this.state = state;
        this.user_id = user_id;
        this.nbPlace = nbPlace;
        this.loglat = loglat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getTargetCity() {
        return targetCity;
    }

    public void setTargetCity(String targetCity) {
        this.targetCity = targetCity;
    }

    public Date getStartdateTime() {
        return startdateTime;
    }

    public void setStartdateTime(Date startdateTime) {
        this.startdateTime = startdateTime;
    }

    public Date getEnddateTime() {
        return enddateTime;
    }

    public void setEnddateTime(Date enddateTime) {
        this.enddateTime = enddateTime;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public String getLoglat() {
        return loglat;
    }

    public void setLoglat(String loglat) {
        this.loglat = loglat;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", startCity='" + startCity + '\'' +
                ", targetCity='" + targetCity + '\'' +
                ", startdateTime=" + startdateTime +
                ", enddateTime=" + enddateTime +
                ", state=" + state +
                ", user_id=" + user_id +
                ", nbPlace=" + nbPlace +
                ", loglat='" + loglat + '\'' +
                '}';
    }
}
