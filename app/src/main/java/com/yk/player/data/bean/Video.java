package com.yk.player.data.bean;

import java.io.Serializable;

public class Video implements Serializable {
    private String name;
    private String path;
    private long duration;
    private long createTime;
    private long updateTime;

    public Video(String name, String path, long duration, long createTime, long updateTime) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Video{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
