package com.figli.shopingassistance.model;

import android.graphics.AvoidXfermode;

import com.figli.shopingassistance.R;

import java.util.Date;

/**
 * Created by Figli on 12.02.2016.
 */
public class ModelTask implements Item {

    private static final int PRIORITY_LOW = 0;
    private static final int PRIORITY_NORMAL = 1;
    private static final int PRIORITY_HIGH = 2;

    public static final int STATUS_OVERDUE = 3;
    public static final int STATUS_CURRENT = 4;
    public static final int STATUS_DONE = 5;

    private String title;
    private long date;
    private int status;
    private int priority;
    private long timeStamp;
    private String quantity;

    private String description;

    public ModelTask() {
        this.status = -1;
        this.timeStamp = new Date().getTime();
    }

    public ModelTask(String title) {
        this.title = title;
    }

    public ModelTask(String title, long date, int priority, int status, long timeStamp, String quantity, String description) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;
        this.quantity = quantity;
        this.description = description;
    }

    public ModelTask(String title, long date, int priority, int status, long timeStamp) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public int getPriorityColor() {
        switch (getPriority()) {
            case PRIORITY_HIGH:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.prioriy_hight;
                } else {
                    return R.color.prioriy_hight_selected;
                }
            case PRIORITY_NORMAL:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.prioriy_normal;
                } else {
                    return R.color.prioriy_normal_selected;
                }
            case PRIORITY_LOW:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.prioriy_low;
                } else {
                    return R.color.prioriy_low_selected;
                }
            default:
                return 0;
        }
    }


    @Override
    public boolean isTask() {
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
