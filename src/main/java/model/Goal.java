package model;

import java.util.ArrayList;
/*
    Goal este well-formed daca:
    estimatedTime   = Suma(tasks(i).estimatedTime)              [Time]
    realizedTime    = Suma(tasks(i).realizedTime)               [Time]
    progress.value  = Suma(tasks(i).progressValue) / noOfTasks  [float]
        ; pentru i = 0..noOfTasks - 1
 */
public class Goal extends AbstractGoal{
    // abstractGoal fields:
    private Description description;
    private Time estimatedTime;
    private Time realizedTime;
    private Progress progress;
    // a goal has a list of tasks:
    private ArrayList<Task> tasks;

    public Goal(Description description) {
        // constr param
        this.description = description;
        // empty objects
        this.estimatedTime  = new Time(0, 0); // no tasks
        this.realizedTime   = new Time(0, 0); // init.
        this.progress       = new Progress();               // so, no progress
        this.tasks          = new ArrayList<>();            // no tasks init.
    }

    //TODO A FUCKING GET DESCRIPTION STRING CA M AM SATURAT SA SCRIU
    // GOAL.GETDESCRIPTION.GETDESCRIPTION!!!!

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        //todo process all information based on this new Tasks
    }

    public void addTask (Task newTask){
        tasks.add(newTask); // adaugam taskul la lista goalului
        // si actualizam timpii
        estimatedTime.addTime(newTask.getEstimatedTime());
        realizedTime.addTime(newTask.getRealizedTime());
        // progresul nu l actualizam pt ca la adaugarea unui task nou inseamna ca acesta are progres = 0.
    }

    /// pt ca cine apeleaza asta sa nu faca goal.getTasks().size().
    public int numberOfTasks (){
        return tasks.size();
    }

    /// GETTERS - override methods from AbstractGoal
    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public Time getEstimatedTime() {
        return estimatedTime;
    }

    @Override
    public Time getRealizedTime() {
        return realizedTime;
    }

    @Override
    public Progress getProgress() {
        return progress;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    private void updateEstimatedTime() {
        Time newEstimatedTime = new Time(0,0);
        for (Task task : tasks){
            newEstimatedTime.addTime(task.getEstimatedTime());
        }
        estimatedTime = newEstimatedTime;
    }

    private void updateRealiedTime() {
        Time newRealizedTime = new Time(0,0);
        for (Task task : tasks){
            newRealizedTime.addTime(task.getRealizedTime());
        }
        realizedTime = newRealizedTime;
    }

    public void updateProgress() {
        float updatedValue = computeNewProgressValue();
        progress.updateProgress(updatedValue);
    }

    private float computeNewProgressValue() {
        //progress.value[n + 1] = [(n * progress.value[n]) / n+1]  +  [newValue / n + 1]    <=>   [(v(1) + v(2) + ... + v(n) / n+1]  +  [v(n+1) / n+1]
        //progress.value = Suma(tasks(i).progressValue) / noOfTasks
        float sumOfProgressValues = 0.0f;
        for (Task task : tasks) sumOfProgressValues += task.getProgressValue();

        return sumOfProgressValues / numberOfTasks();
    }

    @Override
    public String toString() {
        return "Goal about : " + description.getDescription() + " /./ estimated date : " + description.getEstimatedDate()
                + "\n/./ estimatedWorkTime : " + estimatedTime + " /./ realizedWorkTime : "  + realizedTime
                + "\n/./ the list of task : \n" + tasks;
    }

    public void updateGoal() {
        updateEstimatedTime();
        updateRealiedTime();
        updateProgress();
    }



}
