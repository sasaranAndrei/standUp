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
import controller.StandUpController;
import controller.StandUpController.AddGoalListener;
import controller.StandUpController.EditGoalListener;
import controller.StandUpController.CreateTaskListener;
import controller.StandUpController.TaskChangedListener;
import controller.StandUpController.SelectTaskListener;
import controller.StandUpController.RemoveTaskListener;


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

    public void addAddGoalListener(AddGoalListener addGoalListener){
        // il salvez aici pentru ca addGoal apare doar daca suntem in modul AddGoal din display panel

        addGoalPanel.addGoalButton.addActionListener(addGoalListener);
//        this.addGoalListener = addGoalListener;
    }

    public void addEditGoalListener(EditGoalListener editGoalListener) {
        //this.editGoalListener = editGoalListener;
        mainPanel.editGoalButton.addActionListener(editGoalListener);
        /*
        for (Goal goal : goals){
            goalsString.add(goal.getShortDescription());
        }

         */
    }

    public void addCreateTaskListener(CreateTaskListener createTaskListener) {
        //editGoalPanel.taskDisplayPanel .finishAddTaskButton.addActionListener(createTaskListener);
        editGoalPanel.createTaskPanel.finishAddTaskButton.addActionListener(createTaskListener);
    }

    public void addSelectTaskListener(SelectTaskListener selectTaskListener) {
        editGoalPanel.deleteTask.addActionListener(selectTaskListener);
    }

    public void addTaskChangedListener(TaskChangedListener taskChangedListener) {
        editGoalPanel.deleteTaskPanel.selectTask.addActionListener(taskChangedListener);
    }

    public void addRemoveTaskListener(RemoveTaskListener removeTaskListener) {
    }

    //****************************** LISTENERS **************************************


    //[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ data from Controller]]]]]]]]]]]]]]]]]]]]]]]]]]]]

    private ArrayList<String> goalsString;
    private ArrayList<String> tasksString;
    // and their setters

    public void setGoalsString(ArrayList<String> goalsString) {
        this.goalsString = goalsString;
    }

    public void setTasksString(ArrayList<String> tasksString) {
        this.tasksString = tasksString;
    }
    //[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[ data from Controller]]]]]]]]]]]]]]]]]]]]]]]]]]]]


    //{{{{{{{{{{{{{data from components from this view (used in controller)}}}}}}}}}}}}
    //todo:      !!   GOAL !!
    private Date goalSelectedDate;
    private String goalDescriptionString;
    private int selectedGoalIndex;
    // updates
    public void updateSelectGoalCombobox() {
        editGoalPanel.selectGoal // combobox
                .setModel(new DefaultComboBoxModel<>(goalsString.toArray(new String[0])));
    }

    public void updateGoalConstructor() {
        ///// set the data used in controller.
        goalSelectedDate = addGoalPanel.goalCalendar.getDate();
        goalDescriptionString = addGoalPanel.enterDescription.getText();
    }

    public void updateSelectedGoalIndex() {
        selectedGoalIndex = editGoalPanel.selectGoal.getSelectedIndex();
    }

    public void clearGoalConstructor() {
        addGoalPanel.clearPanel();
    } // nu functioneaza

    // getters
    public Date getGoalSelectedDate() {
        return goalSelectedDate;
    }
    public String getGoalDescriptionString() {
        return goalDescriptionString;
    }
    public int getSelectedGoalIndex() {
        return selectedGoalIndex;
    }

    //todo:      !!   TASK !!
    private Date taskSelectedDate;
    private String taskDescriptionString;
    private int taskHoursString;
    private int taskMinutesString;
    private int selectedTaskIndex;

    // updates
    public void updateSelectTaskCombobox() {
        editGoalPanel.deleteTaskPanel.selectTask // combobox
                .setModel(new DefaultComboBoxModel<>(tasksString.toArray(new String[0])));
    }

    public void updateTaskConstructor() {
        taskSelectedDate = editGoalPanel.createTaskPanel.taskCalendar.getDate();
        taskDescriptionString = editGoalPanel.createTaskPanel.enterDescription.getText();
        ///// VALIDARE VALIDARE UNDE ESTI TU OARE
        taskHoursString = Integer.parseInt(editGoalPanel.createTaskPanel.enterHours.getText());
        taskMinutesString = Integer.parseInt(editGoalPanel.createTaskPanel.enterMinutes.getText());
        selectedGoalIndex = editGoalPanel.selectGoal.getSelectedIndex();
    }

    public void updateSelectedTaskIndex() {
        selectedTaskIndex = editGoalPanel.deleteTaskPanel.selectTask.getSelectedIndex();
    }

    public void updateTaskInfoPanel(String selectedTaskDate, String selectedTaskProgress, String selectedTaskEstimatedHours, String selectedTaskEstimatedMinutes, String selectedTaskRealizedHours, String selectedTaskRealizedMinutes) {
        editGoalPanel.deleteTaskPanel.enterEstimatedDate.setText(selectedTaskDate);
        editGoalPanel.deleteTaskPanel.enterProgress.setText(selectedTaskProgress);
        editGoalPanel.deleteTaskPanel.enterEstimatedHours.setText(selectedTaskEstimatedHours);
        editGoalPanel.deleteTaskPanel.enterEstimatedMinutes.setText(selectedTaskEstimatedMinutes);
        editGoalPanel.deleteTaskPanel.enterRealizedHours.setText(selectedTaskRealizedHours);
        editGoalPanel.deleteTaskPanel.enterRealizedMinutes.setText(selectedTaskRealizedMinutes);
        editGoalPanel.deleteTaskPanel.validate();
    }

    public void clearTaskConstructor() {
        editGoalPanel.createTaskPanel.clearPanel();
    }

    // getters
    public Date getTaskSelectedDate() {
        return taskSelectedDate;
    }
    public String getTaskDescriptionString() {
        return taskDescriptionString;
    }
    public int getTaskHoursString() {
        return taskHoursString;
    }
    public int getTaskMinutesString() {
        return taskMinutesString;
    }
    public int getSelectedTaskIndex() {
        return selectedTaskIndex;
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
                //addGoalPanel.clearPanel();
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

        private JDateChooser goalCalendar;

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

            goalCalendar = new JDateChooser();
            goalCalendar.setPreferredSize(new Dimension(110,25)); //todo -> VIEWUTILS
            estimatedDatePanel.add(goalCalendar);

            addGoalButton = new JButton("FINISH");
            // todo -> buton calumea
            /// ACTION LISTENER
            addGoalButton.addActionListener(addGoalListener);

            estimatedDatePanel.add(addGoalButton);
            return estimatedDatePanel;
        }

        public void clearPanel() {
            enterDescription.setText("");
            goalCalendar.setCalendar(null);
            repaint();
            validate();
        }
    }

    private class EditGoalPanel extends JPanel {

        public JComboBox<String> selectGoal;
        private JButton createTask;
        private JButton deleteTask;

        private JPanel taskDisplayPanel;

        private CreateTaskPanel createTaskPanel;
        private DeleteTaskPanel deleteTaskPanel;


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
            createTaskPanel = new CreateTaskPanel();
            deleteTaskPanel = new DeleteTaskPanel();
            this.add(createSelectPanel(), BorderLayout.NORTH);
        }

        private JPanel createSelectPanel (){
            JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            selectGoal = new JComboBox<>();
            // 18 litere
            selectGoal.setPrototypeDisplayValue("123456789012345678");
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
                taskDisplayPanel = createTaskPanel;
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
                taskDisplayPanel = deleteTaskPanel;
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

            private JDateChooser taskCalendar;
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

                JLabel estimatedDateLabel = new JLabel("Estimated Date:", SwingConstants.LEFT);
                c.gridx = 2; c.gridy = 2;
                this.add(estimatedDateLabel, c);

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

                taskCalendar = new JDateChooser();
                taskCalendar.setPreferredSize(new Dimension(110,25)); //todo -> VIEWUTILS
                c.gridx = 2; c.gridy = 3;
                this.add(taskCalendar, c);

                finishAddTaskButton = new JButton("CREATE TASK");
                finishAddTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.BIGGER_COMPONENT_TEXT_SIZE));
                finishAddTaskButton.setForeground(ViewUtils.BUTTON_COLOR);
                finishAddTaskButton.setOpaque(true);
                finishAddTaskButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
                c.gridx = 2; c.gridy = 4;
                ////// ACTION LISTENER + clear the enterFields...
                this.add(finishAddTaskButton, c);

            }

            public void clearPanel() {
                enterDescription.setText("");
                enterHours.setText("");
                enterMinutes.setText("");
                taskCalendar.setCalendar(null);
                repaint();
                validate();
            }
        }

        private class DeleteTaskPanel extends JPanel {

            private JComboBox<String> selectTask;
            private JButton removeTaskButton;

            private JLabel enterDescription;
            private JLabel enterEstimatedDate;
            private JLabel enterProgress;
            private JLabel enterEstimatedHours;
            private JLabel enterEstimatedMinutes;
            private JLabel enterRealizedHours;
            private JLabel enterRealizedMinutes;

            public DeleteTaskPanel() {
                ViewUtils.makeFrameBigger(frame);
                this.setLayout(new GridBagLayout());

                initComponents();

                this.repaint();
                this.validate();
            }

            private void  initComponents (){
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;


                c.gridwidth = 2;


                JLabel estimatedDateLabel = new JLabel("Estimated Date:", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 3;
                this.add(estimatedDateLabel, c);

                JLabel progressLabel = new JLabel("Progress:", SwingConstants.LEFT);
                c.gridx = 2; c.gridy = 3;
                this.add(progressLabel, c);

                JLabel estimatedTimeLabel = new JLabel("Estimated Time:", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 5;
                this.add(estimatedTimeLabel, c);

                JLabel realizedTimeLabel = new JLabel("Realized Time:", SwingConstants.LEFT);
                c.gridx = 2; c.gridy = 5;
                this.add(realizedTimeLabel, c);


                c.gridwidth = 1;


                JLabel estimatedHoursLabel = new JLabel("Hours: ", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 6;
                this.add(estimatedHoursLabel, c);

                JLabel estimatedMinutesLabel = new JLabel("Minutes: ", SwingConstants.LEFT);
                c.gridx = 0; c.gridy = 7;
                this.add(estimatedMinutesLabel, c);

                JLabel realizedHoursLabel = new JLabel("Hours: ", SwingConstants.LEFT);
                c.gridx = 2; c.gridy = 6;
                this.add(realizedHoursLabel, c);

                JLabel realizedMinutesLabel = new JLabel("Minutes: ", SwingConstants.LEFT);
                c.gridx = 2; c.gridy = 7; //???
                this.add(realizedMinutesLabel, c);

                //// dynamic fields
                enterEstimatedHours = new JLabel();
                c.gridx = 1; c.gridy = 6;
                this.add(enterEstimatedHours, c);

                enterEstimatedMinutes = new JLabel();
                c.gridx = 1; c.gridy = 7;
                this.add(enterEstimatedMinutes, c);

                enterRealizedHours = new JLabel();
                c.gridx = 3; c.gridy = 6;
                this.add(enterRealizedHours, c);

                enterRealizedMinutes = new JLabel();
                c.gridx = 3; c.gridy = 7;
                this.add(realizedMinutesLabel, c);


                c.gridwidth = 2;


                enterEstimatedDate = new JLabel();
                c.gridx = 0; c.gridy = 4;
                this.add(enterEstimatedDate, c);

                enterProgress = new JLabel();
                c.gridx = 2; c.gridy = 4;
                this.add(enterProgress, c);

                enterDescription = new JLabel();
                c.gridx = 0; c.gridy = 2;
                c.gridwidth = 4;
                this.add(enterDescription, c);

                // comboBox
                c.gridx = 0; c.gridy = 0;
                c.gridwidth  = 2;
                selectTask = new JComboBox<>();
                // 18 litere
                selectTask.setPrototypeDisplayValue("123456789012345678");
                if (tasksString != null){ // initialized when Edit is clicked
                    selectTask.setModel(new DefaultComboBoxModel<>(tasksString.toArray(new String[0])));
                }
                this.add(selectTask, c);

                // button
                removeTaskButton = new JButton("REMOVE TASK");
                removeTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.BIGGER_COMPONENT_TEXT_SIZE));
                removeTaskButton.setForeground(ViewUtils.BUTTON_COLOR);
                removeTaskButton.setOpaque(true);
                removeTaskButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
                c.gridx = 2; c.gridy = 0;
                ////// ACTION LISTENER + clear the enterFields...
                this.add(removeTaskButton, c);
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



