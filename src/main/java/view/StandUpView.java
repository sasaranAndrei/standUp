package view;

//todo add manually all the listeners from controller
import controller.StandUpController.ManageGoalListener;

import model.Description;
import model.Goal;
import model.Task;
import model.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    }

    public void addManageGoalsListener(ManageGoalListener manageGoalListener) {
        // Manage Goals BUTTON
        mainPanel.manageGoalsButton.addActionListener(manageGoalListener);
    }

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
        // model
        private ArrayList<Task> tasks;

        // view
        private JPanel descriptionPanel; // the first row (indicators)
        private JPanel taskRowsPanel; // matrix of tasks

        private JLabel activeTasksLabel;
        private JButton addTaskButton;
        private JLabel workLabel;
        private JLabel timeLabel;
        private JLabel progressLabel;

        public TasksPanel() {
            // model
            tasks = new ArrayList<>();

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

            descriptionPanel.add(new JLabel("  "));
            /// add task button
            addTaskButton = new JButton("ADD TASK");
            addTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            addTaskButton.setForeground(ViewUtils.BUTTON_COLOR);
            addTaskButton.setOpaque(true);
            addTaskButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            addTaskButton.addActionListener(e -> addTaskLinePanel()); // !!!!!!!!
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

        private void addTaskLinePanel (){
            System.out.println("ADD TASK");
            // view
            ViewUtils.resizeWindow(frame);

            TaskLinePanel taskLinePanel = new TaskLinePanel();
            tasks.add(taskLinePanel.task); // model
            taskRowsPanel.add(taskLinePanel);
            validate();
        }


    }

    private class TaskLinePanel extends JPanel {
        // model
        private Task task;
        private Description testDescription = new Description("goalDesc", new Date());
        private Goal testGoal = new Goal(testDescription);
        private Time testEstimatedTime = new Time(2,30);

        private Time currentWorkTime;
        // view
        private JLabel taskDescriptionLabel;
        private JButton taskWorkButton;
        private JLabel taskTime;
        private JTextField taskProgress;
        private JButton saveProgressButton;

        public TaskLinePanel (){
            // model
            task = new Task(testGoal, testDescription, testEstimatedTime);
            currentWorkTime = new Time(0,0);

            // view
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            initComponents();

            this.setBackground(ViewUtils.MAIN_PANEL_COLOR);
            this.validate();

        }

        public TaskLinePanel(Task newTask) {
            task = newTask;

        }

        private void initComponents (){
            ///TODO : put 'spaces' between elements of panel, for keeping an alignment
            taskDescriptionLabel = new JLabel(task.getTaskDescription(), SwingConstants.LEFT);
            taskDescriptionLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskDescriptionLabel.setForeground(ViewUtils.LABEL_COLOR);
            this.add(taskDescriptionLabel);

            taskWorkButton = new JButton("WORK");
            taskWorkButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskWorkButton.setForeground(ViewUtils.BUTTON_COLOR);
            taskWorkButton.setOpaque(true);
            taskWorkButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            taskWorkButton.setPreferredSize(ViewUtils.WORK_BUTTON_SIZE);
            this.add(taskWorkButton);

            taskTime = new JLabel(currentWorkTime.toString());
            taskTime.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskTime.setForeground(ViewUtils.LABEL_COLOR);
            this.add(taskTime);

            taskProgress = new JTextField(3);
            taskProgress.setText(task.getProcentValue() + "%");
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


