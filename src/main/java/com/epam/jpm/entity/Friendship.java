package com.epam.jpm.entity;

import java.sql.Date;

public class Friendship extends Entity {
    private int userId1;
    private int userId2;
    private Date timestamp;

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "userId1=" + userId1 +
                ", userId2=" + userId2 +
                ", timestamp=" + timestamp +
                '}';
    }
}
