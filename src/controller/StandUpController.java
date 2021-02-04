package controller;

import model.StandUpModel;
import view.StandUpView;

public class StandUpController {

    private StandUpModel model;
    private StandUpView  view;

    public StandUpController(StandUpModel model, StandUpView view) {
        this.model = model;
        this.view = view;
    }

    public static void main(String[] args) {
        StandUpModel model  = new StandUpModel();
        StandUpView  view   = new StandUpView();
        StandUpController  controller = new StandUpController(model, view);
    }
}
