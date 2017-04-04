package com.example.com.zhuangbi.bean;

/**
 * Created by Administrator on 2017/2/21.
 * 左侧菜单的javabean对象
 */

public class NewsMenuItem {

    public String color=null;
    public String thumbnail;
    public String description=null;
    public String id=null;  //这里  只用到了2个对象
    public String name=null;

    public NewsMenuItem(){}
    public NewsMenuItem(String color, String thumbnail, String description, String id, String name) {
        this.color = color;
        this.thumbnail = thumbnail;
        this.description = description;
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "NewsMenuItem{" +
                "color='" + color + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
