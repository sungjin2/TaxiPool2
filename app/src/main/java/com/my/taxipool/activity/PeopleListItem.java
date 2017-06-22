package com.my.taxipool.activity;

/**
 * Created by KITRI on 2017-06-21.
 */

public class PeopleListItem {
    private String info_id;
    private String NicName;

    public String getinfo_id(){return info_id;}
    public String getNicName(){return NicName;}

    public PeopleListItem(String info_id,String NicName){
        this.info_id=info_id;
        this.NicName=NicName;
    }
}
