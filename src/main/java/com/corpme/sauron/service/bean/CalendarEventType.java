package com.corpme.sauron.service.bean;

/**
 * Created by ahg on 25/12/14.
 */
public enum CalendarEventType {

    INFO("calendar-info"),
    WARNING("calendar-warning"),
    SUCCESS("calendar-success"),
    DANGER("calendar-danger"),
    GRAY("calendar-gray");

    private String id = null;

    private CalendarEventType(String id) {
        this.id = id;
    }

    public String getValue() {
        return id;
    }

}
