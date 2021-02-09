package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import controller.*;

//todo add manually all the listeners from controller
import controller.StandUpController.AddGoalListener;
import controller.StandUpController.EditGoalListener;
import controller.StandUpController.SelectGoalListener;
import model.Goal;


public class ManageGoalsFrame {

    // frame & panels
    private JFrame frame;
    private JFrame parentFrame;

    private MainPanel mainPanel;
    private JPanel displayPanel;

    private AddGoalPanel addGoalPanel;
    private EditGoalPanel editGoalPanel;

    public JFrame getFrame() {
        return frame;
    }

    public ManageGoalsFrame(JFrame parentFrame) {
        //TODO stop the counters when you entry this activity
        this.parentFrame = parentFrame;

        frame = new JFrame(); // viewFrame
        frame.setTitle("Goal Manager");

        // save and close operations
        frame.addWindowListener(new GoalWindowListener());

        // size & location of frame
        frame.setSize(ViewUtils.GOAL_MANAGER_FRAME_DIMENSION);
        ViewUtils.setFrameLocationBottomRightCorner(frame);
        frame.setResizable(false);
        frame.setVisible(true);

        // frame layout & panels
        frame.setLayout(new BorderLayout());
        mainPanel = new MainPanel();
        //displayPanel = new DisplayPanel();

        // le initiem dar nu le aduagam
        addGoalPanel = new AddGoalPanel();
        editGoalPanel = new EditGoalPanel();

        // add components
        frame.add(mainPanel, BorderLayout.NORTH);
        //frame.add(displayPanel, BorderLayout.CENTER);

        frame.validate();
    }

    //****************************** LISTENERS **************************************
    /// listeners that will be established later... on other click events.
    /// for now, we just save them
    private AddGoalListener addGoalListener;
    private EditGoalListener editGoalListener;

    //TODO poate i schimb numele in INIT addGoalListeenr...
    public void initAddGoalListener(AddGoalListener addGoalListener){
        // il salvez aici pentru ca addGoal apare doar daca suntem in modul AddGoal din display panel
        this.addGoalListener = addGoalListener;
    }

    public void initEditGoalListener(EditGoalListener editGoalListener, ArrayList<Goal> goals) {
        this.editGoalListener = editGoalListener;
        for (Goal goal : goals){
            goalsString.add(goal.getShortDescription());
        }
    }
    //****************************** LISTENERS **************************************


    //[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ data from Controller]]]]]]]]]]]]]]]]]]]]]]]]]]]]

    //private ArrayList<Goal> goals;
    private ArrayList<String> goalsString = new ArrayList<>();
    //[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ data from Controller]]]]]]]]]]]]]]]]]]]]]]]]]]]]


    //{{{{{{{{{{{{{data from components from this view (used in controller)}}}}}}}}}}}}
    private Date selectedDate;
    private String descriptionString;
    // and their getters
    public Date getSelectedDate() {
        return selectedDate;
    }
    public String getDescriptionString() {
        return descriptionString;
    }

    //{{{{{{{{{{{{{data from components from this view (used in controller)}}}}}}}}}}}}




    private class MainPanel extends JPanel {
        private JButton addGoalButton;
        private JButton editGoalButton;

        public MainPanel() {
            this.setSize(ViewUtils.MAIN_PANEL_DIMENSION);
            this.setLayout(new FlowLayout());
            createComponents();

            this.setBackground(ViewUtils.MAIN_PANEL_COLOR);
            this.validate();

        }

        private void createComponents() {
            addGoalButton  = new JButton("ADD GOAL");
            addGoalButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            addGoalButton.setForeground(ViewUtils.BUTTON_COLOR);
            addGoalButton.setOpaque(true);
            addGoalButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            //// ACTION LISTENER
            addGoalButton.addActionListener(e -> {
                // comutare de la AddGoal ->  EditGoal
                if (displayPanel != null) frame.remove(displayPanel);
                displayPanel = new AddGoalPanel(); // / addGoalPanel
                frame.add(displayPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();

            }); // displayPanel = ADD GOAL BUTTON
            this.add(addGoalButton);

            this.add(new JLabel("       "));

            editGoalButton = new JButton("EDIT GOAL");
            editGoalButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            editGoalButton.setForeground(ViewUtils.BUTTON_COLOR);
            editGoalButton.setOpaque(true);
            editGoalButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            //// ACTION LISTENER
            editGoalButton.addActionListener(e -> {
                // comutare de la AddGoal ->  EditGoal
                if (displayPanel != null) frame.remove(displayPanel);
                displayPanel = new EditGoalPanel();// / editGoalPanel
                frame.add(displayPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            });
            this.add(editGoalButton);
        }
    }

    private class AddGoalPanel extends JPanel {
        private JLabel descriptionLabel;
        private JTextField enterDescription;
        private JPanel estimatedDatePanel;
        private JButton addGoalButton;

        private JDateChooser calendar;

        //todo => use colors on the compoentns

        public AddGoalPanel() {
            this.removeAll();
            // size & layout
            this.setSize(ViewUtils.MAIN_PANEL_DIMENSION);
            this.setLayout(new BorderLayout());

            // components
            initComponents();
            frame.repaint();
            frame.validate();
        }

        private void initComponents() {
            descriptionLabel = new JLabel("Description:", SwingConstants.LEFT);
            enterDescription = new JTextField(30);
            estimatedDatePanel = createEstimatedDatePanel();

            this.add(descriptionLabel, BorderLayout.NORTH);
            this.add(enterDescription, BorderLayout.CENTER);
            this.add(estimatedDatePanel, BorderLayout.SOUTH);
        }

        private JPanel createEstimatedDatePanel() {
            JPanel estimatedDatePanel = new JPanel(new FlowLayout());

            estimatedDatePanel.add(new JLabel("Estimated Date"));

            calendar = new JDateChooser();
            calendar.setPreferredSize(new Dimension(110,25)); //todo -> VIEWUTILS
            estimatedDatePanel.add(calendar);

            //estimatedDatePanel.add(new JLabel("aici o sa bag calendaru"));
            addGoalButton = new JButton("FINISH");
            addGoalButton.addActionListener(addGoalListener);
            addGoalButton.addActionListener(e -> {
                ///// set the data used in controller.
                descriptionString = enterDescription.getText();
                selectedDate = calendar.getDate();
            });

            estimatedDatePanel.add(addGoalButton);



            return estimatedDatePanel;
        }

    }

    private class EditGoalPanel extends JPanel {

        private JComboBox<String> selectGoal;
        private JButton addTask;
        private JButton deleteTask;

        private JPanel displayPanelEdit;

        public EditGoalPanel() {
            // size & layout
            this.setSize(ViewUtils.MAIN_PANEL_DIMENSION);
            this.setLayout(new BorderLayout());

            this.add(new JLabel("PLM"));

            // components
            //initComponents();
            //frame.repaint();
            frame.validate();

        }

        private void initComponents() {
           this.add(createSelectPanel());

        }

        private JPanel createSelectPanel (){
            System.out.println("here in create select panel");
            JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            selectGoal = new JComboBox<>();
            selectGoal.setModel(new DefaultComboBoxModel<>(goalsString.toArray(new String[0])));
            selectGoal.addActionListener(new StandUpController.SelectGoalListener());

            return selectPanel;
        }
    }




    // WindowsListener for saving the data before closing app.
    class GoalWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            //TODO SAVE THE WORK BEFORE CLOSING APP
            System.out.println("X button in GOAL MANAGER");
            System.out.println("save the work PLS");
            ViewUtils.switchFrame(frame, parentFrame);
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}



