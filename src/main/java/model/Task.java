package model;

public class Task extends AbstractGoal{
    // abstractGoal fields:
    private Description description;
    private Time estimatedTime;
    private Time realizedTime;
    private Progress progress;

    // make the connection back to advertise when a task progress is changing, so I can change goal progress too
    private Goal goal;

    public Task(Goal goal, Description description, Time estimatedTime) {
        // constr param
        this.description = description;
        this.estimatedTime = estimatedTime;
        // empty objects
        this.realizedTime = new Time(0,0);
        this.progress = new Progress();
        // connection to goal
        this.goal = goal;
    }

    public float getProgressValue(){
        return progress.getValue();
    }

    public Goal getGoal() {
        return goal;
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


    public void updateProgress(float newValue){
        progress.updateProgress(newValue);
        //goal.recalculateAndUpdateProgress();
        goal.updateProgress();
    }
    // probabil o sa trebuiasca sa fac la fel si pt realizedTime

    public int getProcentValue (){
        int procentValue = (int) getProgressValue() * 100;
        return procentValue;
    }

    @Override
    public String toString() {
        return "Task about " + description.getDescription() + "\n/./ estimatedWorkTime : " + estimatedTime
                + " /./ realizedWorkTime : " + realizedTime + " /./ progress " + progress + "\n";
    }

    public String getTaskDescription() {
        ///TODO : primele 20 de caracatere din description sa apara.
        return "12345678901234567890";
    }
}
