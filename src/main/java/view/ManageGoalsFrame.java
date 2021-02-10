package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

//todo add manually all the listeners from controller
import controller.StandUpController.AddGoalListener;
import controller.StandUpController.EditGoalListener;
import controller.StandUpController.CreateTaskListener;
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
    private CreateTaskListener createTaskListener;

    //TODO poate i schimb numele in INIT addGoalListeenr...
    public void initAddGoalListener(AddGoalListener addGoalListener){
        // il salvez aici pentru ca addGoal apare doar daca suntem in modul AddGoal din display panel

        addGoalPanel.addGoalButton.addActionListener(addGoalListener);
//        this.addGoalListener = addGoalListener;
    }

    public void addEditGoalListener(EditGoalListener editGoalListener) {
        //this.editGoalListener = editGoalListener;
        this.mainPanel.editGoalButton.addActionListener(editGoalListener);
        /*
        for (Goal goal : goals){
            goalsString.add(goal.getShortDescription());
        }

         */
    }

    public void initCreateTaskListener(CreateTaskListener createTaskListener) {
        this.createTaskListener = createTaskListener;
    }
    //****************************** LISTENERS **************************************


    //[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ data from Controller]]]]]]]]]]]]]]]]]]]]]]]]]]]]

    //private ArrayList<Goal> goals;
    private ArrayList<String> goalsString;

    // and their setters

    public void setGoalsString(ArrayList<String> goalsString) {
        this.goalsString = goalsString;
    }

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

    public void updateSelectGoalCombobox() {
        editGoalPanel.selectGoal // combobox
             .setModel(new DefaultComboBoxModel<>(goalsString.toArray(new String[0])));
    }

    public void updateGoalConstructor() {
        ///// set the data used in controller.
        descriptionString = addGoalPanel.enterDescription.getText();
        selectedDate = addGoalPanel.calendar.getDate();
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
                displayPanel = addGoalPanel;
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
                displayPanel = editGoalPanel;
                frame.add(displayPanel, BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();
            });
            editGoalButton.addActionListener(editGoalListener);
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

            addGoalButton = new JButton("FINISH");
            // todo -> buton calumea
            /// ACTION LISTENER
            addGoalButton.addActionListener(addGoalListener);

            estimatedDatePanel.add(addGoalButton);



            return estimatedDatePanel;
        }

    }

    private class EditGoalPanel extends JPanel {

        public JComboBox<String> selectGoal;
        private JButton createTask;
        private JButton deleteTask;

        private JPanel taskDisplayPanel;

        public EditGoalPanel() {
            // size & layout
            this.setSize(ViewUtils.MAIN_PANEL_DIMENSION);
            this.setLayout(new BorderLayout());

            // components
            initComponents();
            frame.repaint();
            frame.validate();

        }

        private void initComponents() {
           this.add(createSelectPanel(), BorderLayout.NORTH);
        }

        private JPanel createSelectPanel (){
            JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            //todo => MAKE COMBOBOX SMALLER
            selectGoal = new JComboBox<>();
            if (goalsString != null){ // initialized when Edit is clicked
                selectGoal.setModel(new DefaultComboBoxModel<>(goalsString.toArray(new String[0])));
            }
            //nu cred ca are nevoie de actionListener pentru ca odata ce da pe Add/Rmv Task
            // se ia infromatia despre Goalul selectatat.
            selectPanel.add(selectGoal);

            selectPanel.add(new JLabel(" "));

            createTask = new JButton("ADD TASK");
            createTask.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            createTask.setForeground(ViewUtils.BUTTON_COLOR);
            createTask.setOpaque(true);
            createTask.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            //createTask.addActionListener(createTaskListener); ///// listener that was init
            createTask.addActionListener(e -> {
                // comutare de la DEL TASK -> ADD TASK
                if (taskDisplayPanel != null) this.remove(taskDisplayPanel);
                taskDisplayPanel = new CreateTaskPanel();// / editGoalPanel
                this.add(taskDisplayPanel, BorderLayout.CENTER);
                this.revalidate();
                this.repaint();
            });
            selectPanel.add(createTask);

            selectPanel.add(new JLabel(" "));

            deleteTask = new JButton("DEL TASK");
            deleteTask.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            deleteTask.setForeground(ViewUtils.BUTTON_COLOR);
            deleteTask.setOpaque(true);
            deleteTask.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            //deleteTask.addActionListener(); ///// listener that was init
            deleteTask.addActionListener(e -> {
                // comutare de la ADD TASK -> DEL TASK
                if (taskDisplayPanel != null) this.remove(taskDisplayPanel);
                taskDisplayPanel = new DeleteTaskPanel();// / editGoalPanel
                this.add(taskDisplayPanel, BorderLayout.CENTER);
                this.revalidate();
                this.repaint();
            });

            selectPanel.add(deleteTask);

            return selectPanel;
        }


        private class CreateTaskPanel extends JPanel {
            private JTextField enterDescription;
            private JTextField enterHours;
            private JTextField enterMinutes;
            private JButton finishAddTaskButton;

            public CreateTaskPanel() {
                ViewUtils.makeFrameBigger(frame);
                this.setLayout(new GridBagLayout());

                initComponents();

                this.repaint();
                this.validate();

            }

            private void initComponents (){
                //private JLabel descriptionLabel;
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;

                JLabel descriptionLabel = new JLabel("Description:", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 0;
                c.gridwidth = 3;
                this.add(descriptionLabel, c);

                enterDescription = new JTextField(30);
                c.gridx = 0; c.gridy = 1;
                c.gridwidth = 3;
                this.add(enterDescription, c);

                c.gridwidth = 1;

                JLabel estimatedTimeLabel = new JLabel("Estimated Time:", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 2;
                this.add(estimatedTimeLabel, c);

                JLabel hoursLabel   = new JLabel("Hours: ", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 3;
                this.add(hoursLabel, c);

                JLabel minutesLabel = new JLabel("Minutes: ", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 4;
                this.add(minutesLabel, c);

                enterHours = new JTextField(5);
                c.gridx = 1; c.gridy = 3;
                this.add(enterHours, c);

                enterMinutes = new JTextField(5);
                c.gridx = 1; c.gridy = 4;
                this.add(enterMinutes, c);

                finishAddTaskButton = new JButton("CREATE TASK");
                finishAddTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.BIGGER_COMPONENT_TEXT_SIZE));
                finishAddTaskButton.setForeground(ViewUtils.BUTTON_COLOR);
                finishAddTaskButton.setOpaque(true);
                finishAddTaskButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
                c.gridx = 2; c.gridy = 3;
                c.ipady = 25;
                c.gridheight = 2;
                ////// ACTION LISTENER + clear the enterFields...
                finishAddTaskButton.addActionListener(createTaskListener);
                this.add(finishAddTaskButton, c);

            }
        }

        private class DeleteTaskPanel extends JPanel {

            private JComboBox<String> selectTask;
            private JButton removeTaskButton;

            public DeleteTaskPanel() {
                ViewUtils.makeFrameBigger(frame);
                this.setLayout(new GridBagLayout());

                initComponents();

                this.repaint();
                this.validate();
            }

            private void  initComponents (){
                /*
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;

                JLabel descriptionLabel = new JLabel("Description:", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 0;
                c.gridwidth = 3;
                this.add(descriptionLabel, c);

                enterDescription = new JTextField(30);
                c.gridx = 0; c.gridy = 1;
                c.gridwidth = 3;
                this.add(enterDescription, c);

                c.gridwidth = 1;

                JLabel estimatedTimeLabel = new JLabel("Estimated Time:", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 2;
                this.add(estimatedTimeLabel, c);

                JLabel hoursLabel   = new JLabel("Hours: ", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 3;
                this.add(hoursLabel, c);

                JLabel minutesLabel = new JLabel("Minutes: ", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 4;
                this.add(minutesLabel, c);

                enterHours = new JTextField(5);
                c.gridx = 1; c.gridy = 3;
                this.add(enterHours, c);

                enterMinutes = new JTextField(5);
                c.gridx = 1; c.gridy = 4;
                this.add(enterMinutes, c);

                finishAddTaskButton = new JButton("CREATE TASK");
                finishAddTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.BIGGER_COMPONENT_TEXT_SIZE));
                finishAddTaskButton.setForeground(ViewUtils.BUTTON_COLOR);
                finishAddTaskButton.setOpaque(true);
                finishAddTaskButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
                c.gridx = 2; c.gridy = 3;
                c.ipady = 25;
                c.gridheight = 2;
                ////// ACTION LISTENER + clear the enterFields...
                finishAddTaskButton.addActionListener(createTaskListener);
                this.add(finishAddTaskButton, c);

                 */
            }

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


