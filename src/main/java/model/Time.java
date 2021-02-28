package model;

public class Time {
    private static final int WORK_TIME_PERIOD = 30; // o data la X minute tre sa ridici

    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public Time (String time){
        int index = time.indexOf("|");
        this.hours = Integer.parseInt(time.substring(0, index));
        this.minutes = Integer.parseInt(time.substring(index+1));
    }

    public void addTime(Time addedTime) {
        int totalHours      = hours     + addedTime.hours;
        int totalMinutes    = minutes   + addedTime.minutes;
        hours   = totalHours + totalMinutes / 60; // 1h = 60 min
        minutes = totalMinutes % 60; // all the minutes that wasn't converted
    }

    @Override
    public String toString() {
        //return String.format("%02d:%02d", hours, minutes); // 00:00 - 2decimal format
        return hours + "|" + minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void incrementMinute (){
        if (minutes == 59) {
            hours++;
            minutes = 0;
        }
        else minutes++;
    }

    public boolean haveToStandUp() {
        return minutes % WORK_TIME_PERIOD == 0;
    }
}
