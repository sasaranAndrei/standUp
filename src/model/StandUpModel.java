package model;

import java.util.Timer;

public class StandUpModel {

    public void test (){
        //Timer timer = new Timer();
        //TimerTask timerTask = new TimerTask();
        //timer.schedule(timerTask, 0, 1000);

        Progress progress = new Progress();
        progress.updateProgress(0.19f);
        progress.updateProgress(0.29f);
        progress.updateProgress(0.39f);
        progress.updateProgress(0.49f);
        progress.updateProgress(0.59f);
        progress.updateProgress(0.69f);
        progress.updateProgress(0.79f);
        progress.updateProgress(0.89f);
        progress.updateProgress(0.99f);
        progress.updateProgress(1.00f);

    }
}
