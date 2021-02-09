package model;

public class Time {
    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }



    public void addTime(Time addedTime) {
        int totalHours      = hours     + addedTime.hours;
        int totalMinutes    = minutes   + addedTime.minutes;
        hours   = totalHours + totalMinutes / 60; // 1h = 60 min
        minutes = totalMinutes % 60; // all the minutes that wasn't converted
    }


    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes); // 00:00 - 2decimal format
    }

}
