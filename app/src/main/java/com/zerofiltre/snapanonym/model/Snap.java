package com.zerofiltre.snapanonym.model;

import java.util.Date;

public class Snap extends Data {

    private Picture picture;
    private SimpleLocation postedAt;
    private Date postedOn;
    private int reportsNumber;
    private boolean visible;
    private double milesAway;


    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public SimpleLocation getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(SimpleLocation postedAt) {
        this.postedAt = postedAt;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }

    public int getReportsNumber() {
        return reportsNumber;
    }

    public void setReportsNumber(int reportsNumber) {
        this.reportsNumber = reportsNumber;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public int getMilesAway() {
        return (int) (milesAway * 1609.34);
    }

    public void setMilesAway(double milesAway) {
        this.milesAway = milesAway;
    }
}


