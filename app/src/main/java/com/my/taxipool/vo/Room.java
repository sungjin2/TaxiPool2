package com.my.taxipool.vo;

import java.util.Date;

/**
 * Created by KITRI on 2017-06-13.
 */

public class Room {
    /*
     - 방번호
     - 출발지/도착지
     - 출발 시간
     - 남/녀
     - 결제방법
     - 음주
    */
    private int room_no;
    private String admin_id;
    private int max_cnt;
    private String payment;
    private String room_gender;
    private String alcohol;
    private String start_spot;
    private String end_spot;
    private String start_x;
    private String start_y;
    private String end_x;
    private String end_y;
    private Date start_time;
    private String room_state;
    private int current_cnt; //해당 방에 신청/수락된 사람

    private double distance_one;
    private double distance_two;

    public Room(){	}

    public Room(int room_no, String admin_id, int max_cnt, String payment, String room_gender, String alcohol, String start_spot,
                String end_spot, String start_x, String start_y, String end_x, String end_y, Date start_time,
                String room_state) {
        super();
        this.room_no = room_no;
        this.admin_id = admin_id;
        this.max_cnt = max_cnt;
        this.payment = payment;
        this.room_gender = room_gender;
        this.alcohol = alcohol;
        this.start_spot = start_spot;
        this.end_spot = end_spot;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.start_time = start_time;
        this.room_state = room_state;
    }

    public Room(int room_no, String admin_id, int max_cnt, String payment, String room_gender, String alcohol,
                String start_spot, String end_spot, String start_x, String start_y, String end_x, String end_y,
                Date start_time, String room_state, int current_cnt) {
        this.room_no = room_no;
        this.admin_id = admin_id;
        this.max_cnt = max_cnt;
        this.payment = payment;
        this.room_gender = room_gender;
        this.alcohol = alcohol;
        this.start_spot = start_spot;
        this.end_spot = end_spot;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.start_time = start_time;
        this.room_state = room_state;
        this.current_cnt = current_cnt;
    }

    public Room(int room_no, String admin_id, int max_cnt, String payment, String room_gender, String alcohol,
                String start_spot, String end_spot, String start_x, String start_y, String end_x, String end_y,
                Date start_time, String room_state, double distance_one, double distance_two) {
        super();
        this.room_no = room_no;
        this.admin_id = admin_id;
        this.max_cnt = max_cnt;
        this.payment = payment;
        this.room_gender = room_gender;
        this.alcohol = alcohol;
        this.start_spot = start_spot;
        this.end_spot = end_spot;
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        this.start_time = start_time;
        this.room_state = room_state;
        this.distance_one = distance_one;
        this.distance_two = distance_two;
    }

    @Override
    public String toString() {
        return "Room [room_no=" + room_no + ", admin_id=" + admin_id + ", max_cnt=" + max_cnt + ", payment=" + payment
                + ", room_gender=" + room_gender + ", alcohol=" + alcohol + ", start_spot=" + start_spot + ", end_spot="
                + end_spot + ", start_x=" + start_x + ", start_y=" + start_y + ", end_x=" + end_x + ", end_y=" + end_y
                + ", start_time=" + start_time + ", room_state=" + room_state + "]";
    }
    public String toQuery(){
        return "admin_id=" + admin_id + "&max_cnt=" + max_cnt + "&payment=" +payment
                + "&room_gender=" + room_gender+ "&alcohol=" + alcohol+ "&start_spot=" + start_spot+ "&end_spot="
                + end_spot+ "&start_x=" + start_x+ "&start_y=" + start_y + "&end_x=" + end_x+ "&end_y=" + end_y+", start_time=" + start_time;
    }

    public double getDistance_one() {
        return distance_one;
    }

    public void setDistance_one(double distance_one) {
        this.distance_one = distance_one;
    }

    public double getDistance_two() {
        return distance_two;
    }

    public void setDistance_two(double distance_two) {
        this.distance_two = distance_two;
    }

    public int getRoom_no() {
        return room_no;
    }

    public void setRoom_no(int room_no) {
        this.room_no = room_no;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public int getMax_cnt() {
        return max_cnt;
    }

    public void setMax_cnt(int max_cnt) {
        this.max_cnt = max_cnt;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getRoom_gender() {
        return room_gender;
    }

    public void setRoom_gender(String room_gender) {
        this.room_gender = room_gender;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getStart_spot() {
        return start_spot;
    }

    public void setStart_spot(String start_spot) {
        this.start_spot = start_spot;
    }

    public String getEnd_spot() {
        return end_spot;
    }

    public void setEnd_spot(String end_spot) {
        this.end_spot = end_spot;
    }

    public String getStart_x() {
        return start_x;
    }

    public void setStart_x(String start_x) {
        this.start_x = start_x;
    }

    public String getStart_y() {
        return start_y;
    }

    public void setStart_y(String start_y) {
        this.start_y = start_y;
    }

    public String getEnd_x() {
        return end_x;
    }

    public void setEnd_x(String end_x) {
        this.end_x = end_x;
    }

    public String getEnd_y() {
        return end_y;
    }

    public void setEnd_y(String end_y) {
        this.end_y = end_y;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public String getRoom_state() {
        return room_state;
    }

    public void setRoom_state(String room_state) {
        this.room_state = room_state;
    }

    public int getCurrent_cnt() {
        return current_cnt;
    }

    public void setCurrent_cnt(int current_cnt) {
        this.current_cnt = current_cnt;
    }
}
