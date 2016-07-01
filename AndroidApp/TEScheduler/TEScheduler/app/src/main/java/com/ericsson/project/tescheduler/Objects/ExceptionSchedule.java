package com.ericsson.project.tescheduler.Objects;

import java.util.Calendar;
import java.util.Date;

public class ExceptionSchedule {
    private Calendar day;
    private DaySchedule schedule;

    public ExceptionSchedule(Calendar day, Date openingTime, Date closingTime) {
        this.day = day;
        this.schedule = new DaySchedule(openingTime, closingTime);
    }

    public Calendar getDay() {
        return day;
    }

    public void setDay(Calendar day) {
        this.day = day;
    }

    public DaySchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Date openingTime, Date closingTime) {
        this.schedule = new DaySchedule(openingTime, closingTime);
    }
}
