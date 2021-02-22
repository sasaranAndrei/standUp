package controller;

import model.*;
import view.StandUpView;
import view.ViewUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

public class StandUpController {

    private StandUpModel model;
    private StandUpView  view;

    private boolean currentlyWorking;
    //private TimerTask globalTimer;

    public StandUpController(StandUpModel theModel, StandUpView theView) {
        model = theModel;
        view = theView;

        //todo a method for adding all the listeners...
        //todo methods view.addXListener => in View avem view.component.addActionListener(XListener)
        /// MAIN FRAME ActionListeners
        view.addManageGoalsListener(new ManageGoalListener());
        view.addTaskListener(new AddTaskListener());
        view.addTaskChangedComboboxListener(new TaskChangedComboboxListener());
        view.addTaskToActiveTasks(new AddTaskToActiveTasks());
        view.insertWorkListener(new WorkListener());
        view.addGlobalTimerListener(new IncrementTimeListener());

        /// MANAGE GOALS ActionListeners
        /// ADD GOAL
        view.manageGoalsFrame.addAddGoalListener(new AddGoalListener());
        /// EDIT GOAL
        view.manageGoalsFrame.addEditGoalListener(new EditGoalListener());
        view.manageGoalsFrame.addCreateTaskListener(new CreateTaskListener());
        view.manageGoalsFrame.addSelectTaskListener(new SelectTaskListener());
        view.manageGoalsFrame.addTaskChangedListener(new TaskChangedListener());
        view.manageGoalsFrame.addRemoveTaskListener(new RemoveTaskListener());

        // timers
        //globalTimer = new TimerTask("global");
        currentlyWorking = false;
    }

    public static void main(String[] args) {
        StandUpModel model  = new StandUpModel();
        StandUpView  view   = new StandUpView();
        StandUpController controller = new StandUpController(model, view);
    }

    /// listener for ManageGoals Button
    public class ManageGoalListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ViewUtils.switchFrame(view.getFrame(), view.getChildFrame());
        }
    }

    /// listeners for MainFrame
    public class AddTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
            model.loadData();

            ArrayList<String> goalsString = model.getGoalsString();
            /// make link with GUI
            view.setGoalsString(goalsString);
            view.updateSelectGoalCombobox();

            view.insertSelectionTaskPanel();
        }
    }

    public class TaskChangedComboboxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.updateSelectedGoalIndex();

            int selectedGoalIndex = view.getSelectedGoalIndex();
            Goal selectedGoal = model.findGoalByIndex(selectedGoalIndex);

            System.out.println("Selected goal index : " + selectedGoalIndex);

            ArrayList<String> tasksString = model.getTasksStringOfGoal(selectedGoal);
            /// make link with GUI
            view.setTasksString(tasksString);
            view.updateSelectTaskCombobox();
            //todo sa nu poti sa adaugi taskuri deja adaugate
        }
    }

    public class AddTaskToActiveTasks implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.updateSelectedGoalIndex();
            view.updateSelectedTaskIndex();
            int selectedGoalIndex = view.getSelectedGoalIndex();
            int selectedTaskIndex = view.getSelectedTaskIndex();

            Task task = model.getGoals().get(selectedGoalIndex).getTasks().get(selectedTaskIndex);
            String selectedTaskDescription = task.getFixedDescription();
            System.out.println(selectedTaskDescription + "|");
            String selectedTaskProgressValue = String.valueOf(task.getProcentValue()); // + "% " + task.getProgress().getLabel();
            String selectedTaskRealizedTime = task.getRealizedTime().toString();

            view.hideInsertionTaskPanel();

            view.insertTaskRowPanel(
                    selectedTaskDescription,
                    selectedTaskRealizedTime,
                    selectedTaskProgressValue
            );

            //todo create a timer for this task

        }
    }

    public class WorkListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
            /*
            if (itsPressed) => pause
            else work
             */
            //System.out.println("work button este ???");

            if (currentlyWorking) {
                view.makeWorkButtonsClickable();
                // stop timer
                //globalTimer.pauseTimer();
                view.pauseGlobalTimer();
            }
            else {
                view.makeOtherButtonsUnclickable(e.getSource());
                // start timer
                view.resumeGlobalTimer();
                //globalTimer.resumeTimer();
            }

            currentlyWorking = !currentlyWorking;
        }
    }

    public class IncrementTimeListener implements ActionListener {
        private Time time;

        public IncrementTimeListener() {
            time = new Time(0,0);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("global time : " + time);
            time.incrementMinute();
            // display global time on view
            view.updateGlobalTime(time.toString());
        }
    }

    public class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }

    /// listeners for Goal Manager Frame
    /// addGoal
    public class AddGoalListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) { // it kinda works!
            System.out.println("Add the goal");
            //MOMENTAN PREUSPUNEM CA DATELES VALIDE
            //todo format like : "dd/MM/yyyy"
            view.manageGoalsFrame.updateGoalConstructor();

            Date date = view.manageGoalsFrame.getGoalSelectedDate();
            String descriptionString = view.manageGoalsFrame.getGoalDescriptionString();
            Description description = new Description(descriptionString, date);

            Goal goal = new Goal(description);
            System.out.println("goal that we established " + goal);
            // in mom asta nu avem ce date sa procesam pt goal pt ca nu avem taskuri la el
            // pt a lucra la un task, tre sa dai la add task =>
            view.manageGoalsFrame.clearGoalConstructor();

            Excel.insertGoalRow(goal);
            //todo: dupa ce dau add, sa curat descriptionu si dateul


            //System.out.println(date);
            //TODO PROCESS DATE. maybe I'll do a ParserClass for all the parsing stuff in the project.
//
  //          System.out.println(calendar.getDateFormatString());
        }
    }

    // editGoal
    public class EditGoalListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.loadData();
            /// convert goals to strings
            ArrayList<String> goalsString = model.getGoalsString();
            /// make link with GUI
            view.manageGoalsFrame.setGoalsString(goalsString);
            view.manageGoalsFrame.updateSelectGoalCombobox();
        }
    }

    public class CreateTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // in caz ca nu mai da o data pe edit
            model.loadData();

            //TODO -> get info from view
            view.manageGoalsFrame.updateTaskConstructor();

            // ai nevoie ca sa stii unde scrii tasku (la care goal).
            int selectedGoalIndex = view.manageGoalsFrame.getSelectedGoalIndex();
            Goal selectedGoal = model.findGoalByIndex(selectedGoalIndex);

            System.out.println("selected index : " + selectedGoalIndex);
            System.out.println("and the goal is : " + selectedGoal.getDescription().getDescription());

            Date date = view.manageGoalsFrame.getTaskSelectedDate();
            String descriptionString = view.manageGoalsFrame.getTaskDescriptionString();
            Description description = new Description(descriptionString, date);

            int hours = view.manageGoalsFrame.getTaskHours();
            int minutes = view.manageGoalsFrame.getTaskMinutes();
            Time estimatedTime = new Time(hours, minutes);

            Task task = new Task(selectedGoal, description, estimatedTime);
            System.out.println("task that we established " + task);
            // in mom asta nu avem ce date sa procesam pt goal pt ca nu avem taskuri la el
            // pt a lucra la un task, tre sa dai la add task =>

            Excel.insertTaskRow(task);

            //todo: dupa ce dau add, sa curat descriptionu si dateul
            view.manageGoalsFrame.clearTaskConstructor();
            //TODO PROCESS DATE. maybe I'll do a ParserClass for all the parsing stuff in the project.
            // and insert a task row in the excel file
        }
    }

    public class SelectTaskListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            model.loadData();

            view.manageGoalsFrame.updateSelectedGoalIndex();

            // ai nevoie ca sa stii unde scrii tasku (la care goal).
            int selectedGoalIndex = view.manageGoalsFrame.getSelectedGoalIndex();
            Goal selectedGoal = model.findGoalByIndex(selectedGoalIndex);

            ArrayList<String> tasksString = model.getTasksStringOfGoal(selectedGoal);

            /// make link with GUI
            view.manageGoalsFrame.setTasksString(tasksString);
            view.manageGoalsFrame.updateSelectTaskCombobox();
        }
    }

    public class TaskChangedListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            view.manageGoalsFrame.updateSelectedGoalIndex();
            view.manageGoalsFrame.updateSelectedTaskIndex();
            int goalIndex = view.manageGoalsFrame.getSelectedGoalIndex();
            int taskIndex = view.manageGoalsFrame.getSelectedTaskIndex();
            Task task = model.getGoals().get(goalIndex).getTasks().get(taskIndex);
            // set data in the view
            String selectedTaskDate = task.getDescription().getStringDate();
            String selectedTaskProgress = task.getProcentValue() + "% " + task.getProgress().getLabel();
            String selectedTaskEstimatedHours = String.valueOf(task.getEstimatedTime().getHours());
            String selectedTaskEstimatedMinutes = String.valueOf(task.getEstimatedTime().getMinutes());
            String selectedTaskRealizedHours = String.valueOf(task.getRealizedTime().getHours());
            String selectedTaskRealizedMinutes = String.valueOf(task.getRealizedTime().getMinutes());

            view.manageGoalsFrame.updateTaskInfoPanel(
                    selectedTaskDate,
                    selectedTaskProgress,
                    selectedTaskEstimatedHours,
                    selectedTaskEstimatedMinutes,
                    selectedTaskRealizedHours,
                    selectedTaskRealizedMinutes
            );
        }
    }

    public class RemoveTaskListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //todo
            // in caz ca nu mai da o data pe edit
            model.loadData();

            //TODO -> get info from view
            //view.manageGoalsFrame.updateTaskConstructor();
            view.manageGoalsFrame.updateSelectedGoalIndex();
            view.manageGoalsFrame.updateSelectedTaskIndex();

            // ai nevoie ca sa stii unde scrii tasku (la care goal).
            int selectedGoalIndex = view.manageGoalsFrame.getSelectedGoalIndex();
            Goal selectedGoal = model.findGoalByIndex(selectedGoalIndex);
            int selectedTaskIndex =  view.manageGoalsFrame.getSelectedTaskIndex();
            Task selectedTask = model.getGoals().get(selectedGoalIndex).getTasks().get(selectedTaskIndex);

            System.out.println("Delete task " + selectedTask + " from goal : " + selectedGoal);

            Excel.deleteTaskRow(selectedTask);

            //todo: dupa ce dau add, sa curat descriptionu si dateul
            view.manageGoalsFrame.resetDisplayPanel();
        }
    }


}
