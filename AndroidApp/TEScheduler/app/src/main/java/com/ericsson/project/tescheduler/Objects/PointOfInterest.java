package com.ericsson.project.tescheduler.Objects;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PointOfInterest {
    private double locationLat;
    private double locationLong;
    private ArrayList<Double> areaLat;
    private ArrayList<Double> areaLong;
    private ArrayList<Price> priceList;
    private int visitTimeInMinutes;
    private Schedule schedule;
    private String name;
    private String type;
    private String website;
    private String phone;
    private String address;
    private String descriptionShort;
    private String getDescriptionLong;
    private Calendar visitTimeStartInMinutes;

    public PointOfInterest(double locationLat, double locationLong, ArrayList<Double> areaLat, ArrayList<Double> areaLong,
                           ArrayList<Price> priceList, int visitTimeInMinutes,
                           Schedule schedule, String name, String type, String website, String phone,
                           String address, String descriptionShort, String getDescriptionLong) {
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.areaLat = areaLat;
        this.areaLong = areaLong;
        this.priceList = priceList;
        this.visitTimeInMinutes = visitTimeInMinutes;
        this.schedule = schedule;
        this.name = name;
        this.type = type;
        this.website = website;
        this.phone = phone;
        this.address = address;
        this.descriptionShort = descriptionShort;
        this.getDescriptionLong = getDescriptionLong;
        this.visitTimeStartInMinutes = Calendar.getInstance();
    }

    public void setVisitTimeStart(int visitTimeStartInMinutes){
        this.visitTimeStartInMinutes = Request.getTripStart();
        this.visitTimeStartInMinutes.add(Calendar.MINUTE, visitTimeStartInMinutes);;
    }

    public Calendar getVisitTimeStart(){
        return this.visitTimeStartInMinutes;
    }

    public boolean isOpenNow(){
        DaySchedule todaySchedule = this.getSchedule().getTodaySchedule();
        Calendar now = Calendar.getInstance();
        Calendar opening = Calendar.getInstance();
        opening.setTime(todaySchedule.getOpeningTime());
        opening.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        Calendar closing = Calendar.getInstance();
        closing.setTime(todaySchedule.getClosingTime());
        closing.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        return opening.before(Calendar.getInstance())
                && closing.after(Calendar.getInstance());
    }

    public boolean isOpenAt(Calendar dayTime){
        DaySchedule daySchedule = this.getSchedule().getDaySchedule(dayTime);
        Calendar opening = Calendar.getInstance();
        opening.setTime(daySchedule.getOpeningTime());
        Calendar closing = Calendar.getInstance();
        closing.setTime(daySchedule.getClosingTime());
        return opening.before(dayTime)
                && closing.after(dayTime);
    }

    public double getLocationLat() {
        return locationLat;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public String getFormattedLocation() {
        return "(" + locationLat + "," + locationLong + ")";
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }
    public double getPriceByCategory(String category) {
        for(Price temp : priceList) {
            if(temp.getCategory().equals(category)){
                return temp.getPrice();
            }
        }
        return priceList.get(0).getPrice();
    }

    public boolean isFree(){
        for(Price priceCategory : priceList){
            if (priceCategory.getPrice() > 0){
                return false;
            }
        }
        return true;
    }

    public ArrayList<Price> getPriceList(){
        return priceList;
    }

    public void setPriceList(ArrayList<Price> priceList) {
        this.priceList = priceList;
    }

    public String getFormattedVisitTime() {
        if(visitTimeInMinutes < 60){
            return String.valueOf(visitTimeInMinutes);
        }
        else if (visitTimeInMinutes == 60){
            return "1 hour";
        }
        else {
            int hours = visitTimeInMinutes / 60;
            int minutes = visitTimeInMinutes % 60;
            if (minutes == 0) {
                return hours + " hours";
            } else {
                Log.w(AppData.LOG_MESSAGE_HEADER, "Visit time is: " + (visitTimeInMinutes / 60) + ":" + (visitTimeInMinutes % 60));
                return hours + ":" + String.format(Locale.getDefault(), "%02d", minutes) + " hours";
            }
        }
    }

    public int getVisitTimeInMinutes(){
        return visitTimeInMinutes;
    }

    public void setVisitTimeInMinutes(int visitTimeInMinutes) {
        this.visitTimeInMinutes = visitTimeInMinutes;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    // Mon-Sun is 0-6
    public String getScheduleByDay(int day) {
        return schedule.getWeeklySchedule()[day].getOpeningTime()
                + "-" + schedule.getWeeklySchedule()[day].getClosingTime();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getGetDescriptionLong() {
        return getDescriptionLong;
    }

    public void setGetDescriptionLong(String getDescriptionLong) {
        this.getDescriptionLong = getDescriptionLong;
    }

    public String getFormattedScheduleToday() {
        DaySchedule today = this.schedule.getTodaySchedule();
        SimpleDateFormat formattedTime = new SimpleDateFormat(AppData.TIME_FORMAT, Locale.getDefault());
        return formattedTime.format(today.getOpeningTime()) + "-"
                + formattedTime.format(today.getClosingTime());
    }

    // Mon-Sun is 0-6
    public String getFormattedSchedule(int dayOfWeek) {
        DaySchedule day = this.schedule.getWeekdaySchedule(dayOfWeek);
        SimpleDateFormat formattedTime = new SimpleDateFormat(AppData.TIME_FORMAT, Locale.getDefault());
        return formattedTime.format(day.getOpeningTime()) + "-"
                + formattedTime.format(day.getClosingTime());
    }


    public ArrayList<Double> getAreaLat() {
        return areaLat;
    }

    public ArrayList<Double> getAreaLong() {
        return areaLong;
    }
}
