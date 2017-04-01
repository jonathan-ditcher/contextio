package com.ditcherj.contextio.dto;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public class Match {

    private String email;
    private Integer count;
    private String name;
    private String thumbnail;

    public Match() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Match{" +
                "count=" + count +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }

}
