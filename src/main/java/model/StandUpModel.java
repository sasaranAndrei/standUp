package model;

import java.util.ArrayList;

public class StandUpModel {
    /// fields
    private ArrayList<Goal> allGoals;

    public void loadData (){
        allGoals = Excel.loadGoals();
    }

    public ArrayList<Goal> getGoals() {
        System.out.println("there are " + allGoals.size() + " goals.");
        return allGoals;
    }

    public ArrayList<String> getGoalsString() {
        ArrayList<String> goalsString = new ArrayList<>();
        for (Goal goal : allGoals){
            goalsString.add(goal.getShortDescription());
        }
        return goalsString;
    }

    /* teoretic al trebui sa l gaseasca pt ca ordinea normala e ori
        1) intra-n aplicatie si da pe add task in MAIN sau edit task in MANAGE [caz in care inainte se citesc corect goalurile din excel]
        2) da add goal, si ca sa i bage taskuri goalului respectiv da pe edit task in MANAGE [...]

     */
    public Goal findGoalByIndex(int selectedGoalIndex) {
        if (selectedGoalIndex < 0 || selectedGoalIndex >= allGoals.size()) {
            System.out.println("NU EXISTA FRA GOALU ASTA EJ DILAU");
            return null;
        }
        System.out.println("A fost selectat goalul : " + selectedGoalIndex);
        System.out.println(allGoals.get(selectedGoalIndex));
        return allGoals.get(selectedGoalIndex);
    }

    public ArrayList<String> getTasksStringOfGoal (Goal selectedGoal) {
        ArrayList<String> tasksString = new ArrayList<>();
        for (Task task : selectedGoal.getTasks()){
            tasksString.add(task.getShortDescription());
        }
        return tasksString;
    }

    public Task findTaskByDescription(String taskWork) {
        for (Goal goal : allGoals){
            for (Task task : goal.getTasks()){
                if(task.getFixedDescription().equals(taskWork)){
                    return task;
                }
            }
        }
        return null;
    }

    public void updateGoalData() {
        //TODO: update the GOALs information based on his TASKs information in excel
        for (Goal goal: allGoals){
            goal.updateGoal();
        }
    }
}
