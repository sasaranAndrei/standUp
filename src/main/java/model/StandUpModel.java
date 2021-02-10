package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class StandUpModel {
    /// fields
    private ArrayList<Goal> allGoals;

    //TODO: S-ar putea sa nu mai fie valabil ce am scris mai jos
    //pt ca la fiecare insereare citesc / scriu in excel.
    private ArrayList<Task> currentTasks; // tasks that will be modified in View, and will get modified from Controller
    private ArrayList<Goal> newGoals; // goals  created from the View
    private ArrayList<Task> deleteTasks; // tasks that must be deleted (View)

    ///TODO met : storeData, loadData,

    public void loadData (){
        allGoals = Excel.loadGoals();
    }

    public void test (){
        //Timer timer = new Timer();
        //TimerTask timerTask = new TimerTask();
        //timer.schedule(timerTask, 0, 1000);

        Goal goal = new Goal(new Description("my first goal", new Date()));
        Task task1 = new Task(goal, new Description("task1", new Date()), new Time(23,50));
        Task task2 = new Task(goal, new Description("task2", new Date()), new Time(1,20));
        goal.addTask(task1);
        goal.addTask(task2);

        System.out.println("before:");
        System.out.println(goal);

        task1.updateProgress(0.34f);
        System.out.println("after1:");
        System.out.println(goal);

        task2.updateProgress(0.74f);
        System.out.println("after2:");
        System.out.println(goal);


    }

    public ArrayList<Goal> getGoals() {
        loadData();
        System.out.println("there are " + allGoals.size() + " goals.");
        return allGoals;
    }
}