package model;

import java.util.Timer;

public class StandUpModel {

    public void test (){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask();
        timer.schedule(timerTask, 0, 1000);
    }
}
