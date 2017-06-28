package com.my.taxipool.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hyeon on 2017-06-13.
 */

public class TmpRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    double startLat;
    double startLon;
    double endLat;
    double endLon;
    String startSpot;
    String endSpot;
    int peopleWith;
    Date time;
    int way;

    public TmpRoom() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    public String getStartSpot() {
        return startSpot;
    }

    public void setStartSpot(String startSpot) {
        this.startSpot = startSpot;
    }

    public String getEndSpot() {
        return endSpot;
    }

    public void setEndSpot(String endSpot) {
        this.endSpot = endSpot;
    }

    public int getPeopleWith() {
        return peopleWith;
    }

    public void setPeopleWith(int peopleWith) {
        this.peopleWith = peopleWith;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public TmpRoom(double startLat, double startLon, double endLat, double endLon, String startSpot, String endSpot, int peopleWith, Date time, int way) {
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
        this.startSpot = startSpot;
        this.endSpot = endSpot;
        this.peopleWith = peopleWith;
        this.time = time;
        this.way = way;
    }

    @Override
    public String toString() {
        return "TmpRoom{" +
                "startLat=" + startLat +
                ", startLon=" + startLon +
                ", endLat=" + endLat +
                ", endLon=" + endLon +
                ", startSpot='" + startSpot + '\'' +
                ", endSpot='" + endSpot + '\'' +
                ", peopleWith=" + peopleWith +
                ", time='" + time + '\'' +
                ", way=" + way +
                '}';
    }
}
