package controller;

import model.Description;
import model.Excel;
import model.Goal;
import model.StandUpModel;
import view.StandUpView;
import view.ViewUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class StandUpController {

    private StandUpModel model;
    private StandUpView  view;

    public StandUpController(StandUpModel theModel, StandUpView theView) {
        model = theModel;
        view = theView;

        /// init data in the model
        //this.model.loadData();

        //todo a method for adding all the listeners...
        //todo methods view.addXListener => in View avem view.component.addActionListener(XListener)
        view.addManageGoalsListener(new ManageGoalListener());
        view.manageGoalsFrame.initAddGoalListener(new AddGoalListener());
        view.manageGoalsFrame.initEditGoalListener(new EditGoalListener(), model.getGoals());
        /// i-am trimis si goals urile ca sa le poata afisa comboBoxul
        //in mod normal n-ar avea ce sa caute modelul in view.

        view.manageGoalsFrame.initCreateTaskListener(new CreateTaskListener());
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
            Date date = view.manageGoalsFrame.getSelectedDate();
            String descriptionString = view.manageGoalsFrame.getDescriptionString();
            Description description = new Description(descriptionString, date);

            Goal goal = new Goal(description);
            System.out.println("goal that we established " + goal);
            Excel.insertGoal(goal);

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
            //TODO
        }
    }

    public class CreateTaskListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }

    public class SelectGoalListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //todo

        }
    }

}
