package model;

/*
    Clasa parinte pt Goal / Task.
    Folosirea abstractizarii e justificata de implementarea diferita a anumitor metode.
    Nu s-a folosit clasa pt. ca nu se descrie un comportament, ci o structura comuna.
 */
public abstract class AbstractGoal {
    public abstract Description getDescription();
    public abstract Time getEstimatedTime();
    public abstract Time getRealizedTime();
    public abstract Progress getProgress();
    public boolean isDone (){
        return getProgress().getLevel() == Progress.LAST_LEVEL;
    }
    public String getProcent () {
        int procent = (int) getProgress().getValue() * 100;
        String result = procent + "%";
        return result;
    }
    public String getShortDescription (){
        String description = getDescription().getDescription();
        int descriptionLength = description.length();

        int maxLength = 20;

        String shortDescription;
        if (descriptionLength > maxLength){
            shortDescription = description.substring(0,maxLength);
        }
        else shortDescription = description;
        return shortDescription;
    }
}
