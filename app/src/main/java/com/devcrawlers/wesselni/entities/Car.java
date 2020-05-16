package com.devcrawlers.wesselni.entities;

public class Car {
    private int id;
    private String idNumber;
    private String mark;
    private String model;
    private String color;
    private int nbPlace;
    private String pathPicture;
    private String pathReCard;
    private boolean state;
    private int userId;

    public Car() { }

    public Car(int id, String idNumber, String mark, String model, String color, int nbPlace, String pathPicture, String pathReCard, boolean state, int userId) {
        this.id = id;
        this.idNumber = idNumber;
        this.mark = mark;
        this.model = model;
        this.color = color;
        this.nbPlace = nbPlace;
        this.pathPicture = pathPicture;
        this.pathReCard = pathReCard;
        this.state = state;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    public String getPathPicture() {
        return pathPicture;
    }

    public void setPathPicture(String pathPicture) {
        this.pathPicture = pathPicture;
    }

    public String getPathReCard() {
        return pathReCard;
    }

    public void setPathReCard(String pathReCard) {
        this.pathReCard = pathReCard;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
