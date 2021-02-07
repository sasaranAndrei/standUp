package controller;

import model.StandUpModel;
import view.StandUpView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StandUpController {

    private StandUpModel model;
    private StandUpView  view;

    public StandUpController(StandUpModel model, StandUpView view) {
        this.model = model;
        this.view = view;
        //this.view.
    }

    public static void main(String[] args) {
        StandUpModel model  = new StandUpModel();
        StandUpView  view   = new StandUpView();
        StandUpController controller = new StandUpController(model, view);
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
    public static class AddGoalListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }

    public static class EditGoalListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }

    public static class SelectGoalListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //todo

        }
    }

}
