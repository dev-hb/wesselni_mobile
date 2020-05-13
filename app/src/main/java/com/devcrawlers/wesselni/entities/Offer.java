package com.devcrawlers.wesselni.entities;

import java.io.Serializable;
import java.util.Date;

public class Offer implements Serializable {
    private int id;
    private String startCity,targetCity,addrese;
    private Date dateTime;
    private boolean state;
    private int User_id,nbPlace;
    private String loglat;
    private int nbplaceResever;
    private double prix;
    private User user;

    public Offer(){}
    public Offer(int id, String startCity, String targetCity, String addrese, Date dateTime, boolean state, int user_id, int nbPlace, String loglat,double prix,int nbplaceResever) {
        this.id = id;
        this.startCity = startCity;
        this.targetCity = targetCity;
        this.addrese = addrese;
        this.dateTime = dateTime;
        this.state = state;
        User_id = user_id;
        this.nbPlace = nbPlace;
        this.loglat = loglat;
        this.nbplaceResever=nbplaceResever;
        this.prix=prix;
    }
    public Offer(int id, String startCity, String targetCity, String addrese, Date dateTime, boolean state,User user, int nbPlace, String loglat,double prix,int nbplaceResever) {
        this.id = id;
        this.startCity = startCity;
        this.targetCity = targetCity;
        this.addrese = addrese;
        this.dateTime = dateTime;
        this.state = state;
        this.user=user;
        this.nbPlace = nbPlace;
        this.loglat = loglat;
        this.nbplaceResever=nbplaceResever;
        this.prix=prix;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNbplaceResever() {
        return nbplaceResever;
    }

    public void setNbplaceResever(int nbplaceResever) {
        this.nbplaceResever = nbplaceResever;
    }

    public Offer(int id, String startCity, String targetCity, String addrese, Date dateTime, boolean state, int user_id, int nbPlace, String loglat, double prix) {
        this.id = id;
        this.startCity = startCity;
        this.targetCity = targetCity;
        this.addrese = addrese;
        this.dateTime = dateTime;
        this.state = state;
        User_id = user_id;
        this.nbPlace = nbPlace;
        this.loglat = loglat;
        this.prix=prix;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
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

    public String getAddrese() {
        return addrese;
    }

    public void setAddrese(String addrese) {
        this.addrese = addrese;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
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
        return "Offer{" +
                "id=" + id +
                ", startCity='" + startCity + '\'' +
                ", targetCity='" + targetCity + '\'' +
                ", addrese='" + addrese + '\'' +
                ", dateTime=" + dateTime +
                ", state=" + state +
                ", User_id=" + User_id +
                ", nbPlace=" + nbPlace +
                ", loglat='" + loglat + '\'' +
                '}';
    }

    public boolean comper(Offer offerKey) {
        return startCity.toLowerCase().contains(offerKey.startCity.toLowerCase()) && targetCity.toLowerCase().contains(offerKey.targetCity.toLowerCase());
    }
}
