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

}
