package controller;

import javax.swing.Timer;

public class TimerTask extends java.util.TimerTask {

    private String description;

    private Timer timer;

    public TimerTask (String description){
        this.description = description;

    }

    @Override
    public void run() {

        //System.out.println("Timer " + description + " : " + hours + "|" + minutes);
    }

    public void resumeTimer() {
        // sa incrementeze dupa fiecare minut
        //timer.schedule(this, 0, 60000);
    }

    public void pauseTimer() {

    }
}
