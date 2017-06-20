package com.my.taxipool.vo;

/**
 * Created by Hyeon on 2017-06-02.
 */
public class CustomerInfo {

    String info_id;
    String image;
    String phone_no;
    String info_name;
    String nickname;
    String info_gender;

    public CustomerInfo(String info_id, String phone_no, String info_name, String nickname, String info_gender) {
        this.info_id = info_id;
        this.phone_no = phone_no;
        this.info_name = info_name;
        this.nickname = nickname;
        this.info_gender = info_gender;
    }

    public CustomerInfo(String info_id, String image, String phone_no, String info_name, String nickname, String info_gender) {
        this.info_id = info_id;
        this.image = image;
        this.phone_no = phone_no;
        this.info_name = info_name;
        this.nickname = nickname;
        this.info_gender = info_gender;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "info_id='" + info_id + '\'' +
                ", image='" + image + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", info_name='" + info_name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", info_gender='" + info_gender + '\'' +
                '}';
    }

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }

    public String getImage(){return image;}

    public void setImage(String image){
        this.image = image;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getInfo_name() {
        return info_name;
    }

    public void setInfo_name(String info_name) {
        this.info_name = info_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInfo_gender() {
        return info_gender;
    }

    public void setInfo_gender(String info_gender) {
        this.info_gender = info_gender;
    }
}