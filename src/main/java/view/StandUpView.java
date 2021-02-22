package view;

//todo add manually all the listeners from controller
import controller.StandUpController;
import controller.StandUpController.ManageGoalListener;
import controller.StandUpController.AddTaskListener;
import controller.StandUpController.AddTaskToActiveTasks;
import controller.StandUpController.TaskChangedComboboxListener;
import controller.StandUpController.WorkListener;
import controller.StandUpController.IncrementTimeListener;

import model.Description;
import model.Goal;
import model.Task;
import model.Time;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;

public class StandUpView {
    // child frame
    public ManageGoalsFrame manageGoalsFrame;

    // frame & panels
    private JFrame frame;
    private MainPanel mainPanel;
    private TasksPanel tasksPanel;

    private InsertionTaskPanel insertionTaskPanel;

    private ArrayList<JButton> workButtons;

    private Timer globalTimer;

    public StandUpView() {
        // frames stuff
        frame = new JFrame();
        frame.setTitle("StandApp");
        manageGoalsFrame = new ManageGoalsFrame(frame); // la inceput le pornim pe ambele
        ViewUtils.switchFrame(manageGoalsFrame.getFrame(), frame);  // iar butonul de manage goals doar le comuta.

        // save and close operations
        frame.addWindowListener(new MyWindowListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EXIT!!!!!

        // size & location of frame
        frame.setSize(ViewUtils.FRAME_DIMENSION);
        ViewUtils.setFrameLocationBottomRightCorner(frame);
        frame.setResizable(false);
        frame.setVisible(true);

        // frame layout & panels
        frame.setLayout(new BorderLayout());
        mainPanel = new MainPanel();
        tasksPanel = new TasksPanel();

        // add components
        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(tasksPanel, BorderLayout.CENTER);

        frame.validate();

        insertionTaskPanel = new InsertionTaskPanel();
        workButtons = new ArrayList<>();
    }

    public void addGlobalTimerListener(IncrementTimeListener incrementTimeListener){
        globalTimer = new Timer(1000, incrementTimeListener);
        // sa se incrementeze o data la fiecare minut
    }

    public void addManageGoalsListener(ManageGoalListener manageGoalListener) {
        // Manage Goals BUTTON
        mainPanel.manageGoalsButton.addActionListener(manageGoalListener);
    }

    public void addTaskListener(AddTaskListener addTaskListener){
        tasksPanel.addTaskButton.addActionListener(addTaskListener);
    }

    public void addTaskToActiveTasks(AddTaskToActiveTasks addTaskToActiveTasks) {
        insertionTaskPanel.addTaskToActiveTasks.addActionListener(addTaskToActiveTasks);
    }

    public void addTaskChangedComboboxListener (TaskChangedComboboxListener taskChangedComboboxListener){
        insertionTaskPanel.goalComboBox.addActionListener(taskChangedComboboxListener);
    }

    public void insertSelectionTaskPanel() {
        ViewUtils.resizeWindowPlus(frame);
        tasksPanel.taskRowsPanel.add(insertionTaskPanel);
    }

    private ArrayList<String> goalsString;
    private ArrayList<String> tasksString;
    // and their setters

    public void updateSelectGoalCombobox() {
        insertionTaskPanel.goalComboBox
                .setModel(new DefaultComboBoxModel<>(goalsString.toArray(new String[0])));
    }

    public void setGoalsString(ArrayList<String> goalsString) {
        this.goalsString = goalsString;
    }

    public void setTasksString(ArrayList<String> tasksString) {
        this.tasksString = tasksString;
    }

    // goal
    private int selectedGoalIndex;
    public int getSelectedGoalIndex() {
        return selectedGoalIndex;
    }
    public void updateSelectedGoalIndex() {
        selectedGoalIndex = insertionTaskPanel.goalComboBox.getSelectedIndex();
    }

    // task
    private int selectedTaskIndex;
    public int getSelectedTaskIndex() {
        return selectedTaskIndex;
    }
    public void updateSelectedTaskIndex() {
        selectedTaskIndex = insertionTaskPanel.taskComboBox.getSelectedIndex();
    }

    public void updateSelectTaskCombobox() {
        insertionTaskPanel.taskComboBox
                .setModel(new DefaultComboBoxModel<>(tasksString.toArray(new String[0])));
    }

    public void hideInsertionTaskPanel (){
        tasksPanel.taskRowsPanel.remove(insertionTaskPanel);
        ViewUtils.resizeWindowMinus(frame);
        frame.validate();
        frame.repaint();
    }

    public void insertTaskRowPanel(String selectedTaskDescription,
                                   String selectedTaskRealizedTime,
                                   String selectedTaskProgressValue){
        ViewUtils.resizeWindowPlus(frame);
        TaskLinePanel taskLinePanel = new TaskLinePanel(selectedTaskDescription, selectedTaskRealizedTime, selectedTaskProgressValue);
        tasksPanel.taskRowsPanel.add(taskLinePanel);
        frame.validate();
        frame.repaint();
    }

    public void makeWorkButtonsClickable() {
        for (JButton workButton : workButtons){
            workButton.setEnabled(true);
        }
    }

    public void makeOtherButtonsUnclickable(Object source) {
        JButton sourceButton = null;
        if (source instanceof JButton) sourceButton = (JButton) source;

        for (JButton workButton : workButtons) {
            if (workButton != sourceButton) {
                workButton.setEnabled(false);
            }
        }
    }

    WorkListener workGlobalListener;
    public void insertWorkListener(WorkListener workListener) {
        workGlobalListener = workListener;
    }

    public void updateGlobalTime(String timeString) {
        mainPanel.globalTimeValueLabel.setText(timeString);
    }

    public void resumeGlobalTimer() {
        globalTimer.start();
    }

    public void pauseGlobalTimer() {
        globalTimer.stop();
    }


    /*


         */


    // JPanel for the main (header part of the app)
    private class MainPanel extends JPanel {
        private JButton manageGoalsButton;
        private JLabel globalTimeLabel;
        private JLabel globalTimeValueLabel;

        public MainPanel() {
            // size
            this.setSize(ViewUtils.MAIN_PANEL_DIMENSION);
            // layout & components
            this.setLayout(new FlowLayout());
            initComponents();

            this.setBackground(ViewUtils.MAIN_PANEL_COLOR);
            this.validate();
        }

        void initComponents(){
            manageGoalsButton = new JButton("MANAGE GOALS");
            manageGoalsButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            manageGoalsButton.setForeground(ViewUtils.BUTTON_COLOR);
            manageGoalsButton.setOpaque(true);
            manageGoalsButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            this.add(manageGoalsButton);

            globalTimeLabel = new JLabel("TOTAL WORK TIME : ");
            globalTimeLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            globalTimeLabel.setForeground(ViewUtils.LABEL_COLOR);
            this.add(globalTimeLabel);

            globalTimeValueLabel = new JLabel("00:00");
            globalTimeValueLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            globalTimeValueLabel.setForeground(ViewUtils.LABEL_COLOR);
            this.add(globalTimeValueLabel);

            validate();
        }
    }

    private class TasksPanel extends JPanel{
        // view
        private JPanel descriptionPanel; // the first row (indicators)
        private JPanel taskRowsPanel; // matrix of tasks

        private JLabel activeTasksLabel;
        private JButton addTaskButton;
        private JLabel workLabel;
        private JLabel timeLabel;
        private JLabel progressLabel;

        public TasksPanel() {
            // view
            this.setSize(ViewUtils.TASK_PANEL_DIMENSION);

            // layout & components
            //this.setLayout(new GridLayout(0,3,1,1));
            //this.setLayout(new GridBagLayout());
            this.setLayout(new BorderLayout());
            initComponents();
            this.setBackground(ViewUtils.MAIN_PANEL_COLOR);
            this.validate();
        }

        private JPanel createDescriptionPanel (){
            ///TODO set colors and stuff
            JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            /// active tasks label
            activeTasksLabel = new JLabel("TASKS");
            activeTasksLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            activeTasksLabel.setForeground(ViewUtils.LABEL_COLOR);
            descriptionPanel.add(activeTasksLabel);

            descriptionPanel.add(new JLabel(" "));
            /// add task button
            addTaskButton = new JButton("ADD TASK");
            addTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            addTaskButton.setForeground(ViewUtils.BUTTON_COLOR);
            addTaskButton.setOpaque(true);
            addTaskButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);

            //addTaskButton.addActionListener(e -> addTaskLinePanel()); // !!!!!!!!
            descriptionPanel.add(addTaskButton);

            descriptionPanel.add(new JLabel("   "));
            /// workLabel
            workLabel = new JLabel("WORK");
            workLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            workLabel.setForeground(ViewUtils.LABEL_COLOR);
            descriptionPanel.add(workLabel);

            descriptionPanel.add(new JLabel("   "));
            /// timeLabel
            timeLabel = new JLabel("TIME");
            timeLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            timeLabel.setForeground(ViewUtils.LABEL_COLOR);
            descriptionPanel.add(timeLabel);

            /// progressLabel
            progressLabel = new JLabel("PROGRESS");
            progressLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            progressLabel.setForeground(ViewUtils.LABEL_COLOR);
            descriptionPanel.add(progressLabel);

            return descriptionPanel;
        }

        private JPanel createTaskRowsPanel (){
            ///TODO set colors and stuff
            JPanel taskRowsPanel = new JPanel(new GridLayout(0, 1));
            return taskRowsPanel;
        }

        void initComponents (){
            descriptionPanel = createDescriptionPanel();
            this.add(descriptionPanel, BorderLayout.NORTH);
            taskRowsPanel = createTaskRowsPanel();
            this.add(taskRowsPanel, BorderLayout.CENTER);
            validate();
        }




    }

    private class TaskLinePanel extends JPanel {

        // view
        private JLabel taskDescriptionLabel;
        private JButton taskWorkButton;
        private JLabel taskTime;
        private JTextField taskProgress;
        private JButton saveProgressButton;

        public TaskLinePanel(String selectedTaskDescription, String selectedTaskRealizedTime, String selectedTaskProgressValue) {

            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            taskDescriptionLabel = new JLabel(selectedTaskDescription, SwingConstants.LEFT);
            taskDescriptionLabel.setMinimumSize(ViewUtils.TEXTLINE_LABEL_DIMENSION);
            taskDescriptionLabel.setPreferredSize(ViewUtils.TEXTLINE_LABEL_DIMENSION);
            taskDescriptionLabel.setMaximumSize(ViewUtils.TEXTLINE_LABEL_DIMENSION);
            taskDescriptionLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskDescriptionLabel.setForeground(ViewUtils.LABEL_COLOR);
            this.add(taskDescriptionLabel);

            taskWorkButton = new JButton("WORK");
            taskWorkButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskWorkButton.setForeground(ViewUtils.BUTTON_COLOR);
            taskWorkButton.setOpaque(true);
            taskWorkButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            taskWorkButton.setPreferredSize(ViewUtils.WORK_BUTTON_SIZE);
            //////////// MOUSE LISTENER ???
            taskWorkButton.addActionListener(workGlobalListener);
            this.add(taskWorkButton);
            // make the link
            workButtons.add(taskWorkButton);

            taskTime = new JLabel(selectedTaskRealizedTime, SwingConstants.LEFT);
            taskTime.setMinimumSize(ViewUtils.TIME_LABEL_DIMENSION);
            taskTime.setPreferredSize(ViewUtils.TIME_LABEL_DIMENSION);
            taskTime.setMaximumSize(ViewUtils.TIME_LABEL_DIMENSION);
            taskTime.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskTime.setForeground(ViewUtils.LABEL_COLOR);
            this.add(taskTime);

            taskProgress = new JTextField(3);
            taskProgress.setText(selectedTaskProgressValue + "%");
            this.add(taskProgress);

            ImageIcon saveIcon = new ImageIcon("resources/saveIcon.png");
            Image image = saveIcon.getImage();
            Image resizedImage = image.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            saveIcon = new ImageIcon(resizedImage);
            saveProgressButton = new JButton(saveIcon);
            saveProgressButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            saveProgressButton.setForeground(ViewUtils.BUTTON_COLOR);
            saveProgressButton.setOpaque(true);

//            saveProgressButton.setSize(100,100);
            saveProgressButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);



//            saveProgressButton.setIcon();
            this.add(saveProgressButton);



        }



    }

    private class InsertionTaskPanel extends JPanel {
        //todo 2 comboBoxuri si un buton de validare
        // care odata apasat dispare InsertionTaskPanelu

        private JComboBox<String> goalComboBox;
        private JComboBox<String> taskComboBox;
        private JButton addTaskToActiveTasks;

        public InsertionTaskPanel() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            // goals
            goalComboBox = new JComboBox<>();
            goalComboBox.setPrototypeDisplayValue("123456789012345678");
            if (goalsString != null){ // initialized when Edit is clicked
                goalComboBox.setModel(new DefaultComboBoxModel<>(goalsString.toArray(new String[0])));
            }
            //nu cred ca are nevoie de actionListener pentru ca odata ce da pe Add/Rmv Task
            // se ia infromatia despre Goalul selectatat.
            this.add(goalComboBox);

            // tasks
            taskComboBox = new JComboBox<>();
            taskComboBox.setPrototypeDisplayValue("123456789012345678");
            this.add(taskComboBox);

            addTaskToActiveTasks = new JButton("WORK");
            addTaskToActiveTasks.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            addTaskToActiveTasks.setForeground(ViewUtils.BUTTON_COLOR);
            addTaskToActiveTasks.setOpaque(true);
            addTaskToActiveTasks.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            this.add(addTaskToActiveTasks);

            validate();
            repaint();

        }
    }


    // WindowsListener for saving the data before closing app.
    class MyWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            //TODO SAVE THE WORK BEFORE CLOSING APP
            System.out.println("X button => QUIT");
            System.out.println("save the work PLS");
            frame.dispose();
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

    public JFrame getFrame() {
        return frame;
    }

    public JFrame getChildFrame() {
        return manageGoalsFrame.getFrame();
    }


}


