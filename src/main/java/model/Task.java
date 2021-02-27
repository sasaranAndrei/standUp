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
        float procentValue = getProgressValue() * 100.0f;
        return (int) procentValue;
    }

    @Override
    public String toString() {
        return "Task about " + description.getDescription() + "\n/./ estimatedWorkTime : " + estimatedTime
                + " /./ realizedWorkTime : " + realizedTime + " /./ progress " + progress + "\n";
    }

    public String getFixedDescription() {
        // exact 21 de carcatere
        String description = getDescription().getDescription();
        int descriptionLength = description.length();

        int maxLength = 21;

        String fixedDescription;
        if (descriptionLength > maxLength){
            fixedDescription = description.substring(0,maxLength);
        }
        else {
            fixedDescription = description;
            for (int l = descriptionLength; l < maxLength; l++){
                fixedDescription += " ";
            }
        }
        return fixedDescription;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public void setRealizedTime(Time realizedTime) {
        this.realizedTime = realizedTime;
    }
}
