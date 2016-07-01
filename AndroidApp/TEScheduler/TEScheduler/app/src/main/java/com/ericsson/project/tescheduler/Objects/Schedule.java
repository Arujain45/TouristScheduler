package com.ericsson.project.tescheduler.Objects;

import java.util.ArrayList;
import java.util.Calendar;

public class Schedule {
    private final DaySchedule[] daySchedule;
    private ArrayList<ExceptionSchedule> exceptionSchedule;

    //the array has exactly 7 elements; one schedule per day, starting Monday at index 0
    public Schedule() {
        this.daySchedule = new DaySchedule[7];
        this.exceptionSchedule = new ArrayList<>();
    }

    public Schedule(DaySchedule[] daySchedule) {
        this.daySchedule = daySchedule;
        this.exceptionSchedule = new ArrayList<>();
    }

    public DaySchedule[] getWeeklySchedule() {
        return daySchedule;
    }

    public DaySchedule getTodaySchedule(){
        Calendar today = Calendar.getInstance();
        for(ExceptionSchedule exceptionDay : exceptionSchedule){
            if(exceptionDay.getDay().compareTo(today) == 0){
                return exceptionDay.getSchedule();
            }
        }
        // DAY_OF_WEEK starts with value 1 for Sunday, hence the use of "today - 2"
        return daySchedule[today.get(Calendar.DAY_OF_WEEK)-2];
    }

    public DaySchedule getDaySchedule(Calendar day){
        for(ExceptionSchedule exceptionDay : exceptionSchedule){
            if(exceptionDay.getDay().compareTo(day) == 0){
                return exceptionDay.getSchedule();
            }
        }
        // DAY_OF_WEEK starts with value 1 for Sunday, hence the use of "today - 2"
        return daySchedule[day.get(Calendar.DAY_OF_WEEK)-2];
    }

    public DaySchedule getWeekdaySchedule(int day){
        return daySchedule[day];
    }

    public void setDayScheduleAt(DaySchedule daySchedule, int index) {
        this.daySchedule[index] = daySchedule;
    }

    public ArrayList<ExceptionSchedule> getExceptionSchedule() {
        return exceptionSchedule;
    }

    public void setExceptionSchedule(ArrayList<ExceptionSchedule> exceptionSchedule) {
        this.exceptionSchedule = exceptionSchedule;
    }

    public void addToExceptionSchedule(ExceptionSchedule exceptionSchedule) {
        this.exceptionSchedule.add(exceptionSchedule);
    }

    public String[] getFormattedScheduleAt(Calendar tripStart, Calendar tripEnd) {
        String[] result = new String[2];
        result[0] = "["; result[1] = "[";
        DaySchedule scheduleHolder;

        //TODO 1: check if any exception day in the trip range
        //TODO 2: find DAY OF WEEK for tripStart, increment array index for other days with modulo 7

        for(Calendar temp = tripStart; temp.compareTo(tripEnd) != 0; temp.add(Calendar.DAY_OF_YEAR, 1)){
            scheduleHolder = getDaySchedule(temp);
            result[0] += scheduleHolder.getOpeningTime() + ",";
            result[1] += scheduleHolder.getClosingTime() + ",";
        }

        //Remove the last comma and append the right bracket
        result[0] = result[0].substring(0, result[0].length()-1) + "]";
        result[1] = result[1].substring(1, result[1].length()-1) + "]";

        return result;
    }
}
