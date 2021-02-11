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

    public StandUpController(StandUpModel theModel, StandUpView theView) {
        model = theModel;
        view = theView;

        //todo a method for adding all the listeners...
        //todo methods view.addXListener => in View avem view.component.addActionListener(XListener)
        /// MAIN FRAME ActionListeners
        view.addManageGoalsListener(new ManageGoalListener());

        /// MANAGE GOALS ActionListeners
        view.manageGoalsFrame.addAddGoalListener(new AddGoalListener());
        view.manageGoalsFrame.addEditGoalListener(new EditGoalListener());
        view.manageGoalsFrame.addCreateTaskListener(new CreateTaskListener());
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

        }
    }

    public class WorkListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
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
            //Excel.shiftRow();
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

            //todo => rename sa nu mai fie String.
            int hours = view.manageGoalsFrame.getTaskHoursString();
            int minutes = view.manageGoalsFrame.getTaskMinutesString();
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

    public class SelectGoalListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //todo

        }
    }

}
