package com.devcrawlers.wesselni.entities;

import java.util.Date;

public class Request {
    private int id;
    private String startCity,targetCity;
    private Date startdateTime, enddateTime;
    private boolean state;
    private int user_id,nbPlace;



    public Request(int id, String startCity, String targetCity, Date startdateTime, Date enddateTime, int state, int user_id, int nbPlace) {
        this.id = id;
        this.startCity = startCity;
        this.targetCity = targetCity;
        this.startdateTime = startdateTime;
        this.enddateTime = enddateTime;
        if (state==1){
            this.state = true;
        }else {
            this.state = false;
        }

        this.user_id = user_id;
        this.nbPlace = nbPlace;
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
                ", nbPlace=" + nbPlace + '\'' +
                '}';
    }
}
