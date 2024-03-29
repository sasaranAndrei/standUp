package view;

//todo add manually all the listeners from controller
import controller.StandUpController.ManageGoalListener;
import controller.StandUpController.AddTaskListener;
import controller.StandUpController.AddTaskToActiveTasks;
import controller.StandUpController.TaskChangedComboboxListener;
import controller.StandUpController.WorkListener;
import controller.StandUpController.GlobalTimeListener;
import controller.StandUpController.WorkTimeListener;
import controller.StandUpController.SaveListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class StandUpView {
    // child frame
    public ManageGoalsFrame manageGoalsFrame;

    // frame & panels
    private JFrame frame;
    private MainPanel mainPanel;
    private TasksPanel tasksPanel;
    private InsertionTaskPanel insertionTaskPanel;

    // arraylists of components that will be updated
    private ArrayList<JLabel> activeTasksLabels;
    private ArrayList<JButton> workButtons;
    private ArrayList<JLabel> workTimeLabels;
    private ArrayList<JTextField> progressTextFields;
    private ArrayList<JButton> saveButtons;

    // TIMERS
    private ArrayList<Timer> workTimers;
    private Timer globalTimer;

    /// initialize the GUI & create the components that will be updated
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

        activeTasksLabels = new ArrayList<>();
        workButtons = new ArrayList<>();
        progressTextFields = new ArrayList<>();
        workTimers = new ArrayList<>();
        workTimeLabels = new ArrayList<>();
        saveButtons = new ArrayList<>();
    }

    /*
        ACTION LISTENERS
    1)
    if a method is called : addXListener(X) =>
        a specific component will get that X. ex: parentComponent.component.set(X)

    2)
    if a method is called : insertYListener(Y) =>
        a local variable will store that value Y
        and when the specific component is created, that component will get that localY.
        ex:
        localY = Y
        creatingComponent(){
            component = new()
            parentComponent.component.sete(localY)
        }

    1) XListener is used for STATIC components (that will be always there)
    2) YListener is used for DYNAMIC components (that might appear or don't)
     */

    //// for creating GLOBAL TIMER
    public void addGlobalTimerListener(GlobalTimeListener incrementTimeListener){
        globalTimer = new Timer(ViewUtils.TIME_PERIOD, incrementTimeListener);
    }

    //// for opening MANAGE GOALS FRAME
    public void addManageGoalsListener(ManageGoalListener manageGoalListener) {
        mainPanel.manageGoalsButton.addActionListener(manageGoalListener);
    }

    //// for selecting a TASK in order to add it to the mainFrame
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

    // workListner - for the logic of start / stop working -> timers, labels, ...
    WorkListener workListener;
    public void insertWorkListener(WorkListener wListener) {
        workListener = wListener;
    }

    // workTimeListener - listener for a TASK
    WorkTimeListener workTimeListener;
    public void insertWorkTimeListener(WorkTimeListener wTimeListener) {
        workTimeListener = wTimeListener;
    }

    SaveListener saveListener;
    public void insertSaveListener(SaveListener sListener) {
        saveListener = sListener;
    }

    /// TIME AND TIMERS
    // GLOBAL timer
    public void updateGlobalTime(String timeString) {
        mainPanel.globalTimeValueLabel.setText(timeString);
    }

    public void resumeGlobalTimer() {
        globalTimer.start();
    }

    public void pauseGlobalTimer() {
        globalTimer.stop();
    }

    // WORK timer
    public void resumeWorkTimer (Object source){
        //TODO: verifica al catelea buton s o apasat ca sa stii ce label sa modifici
        for (JButton workButton : workButtons){
            if (workButton == source){
                int index = workButtons.indexOf(workButton);
                workTimers.get(index).start();
            }
        }
    }

    public void pauseWorkTimer (Object source){
        //TODO: verifica al catelea buton s o apasat ca sa stii ce label sa modifici
        for (JButton workButton : workButtons){
            if (workButton == source){
                int index = workButtons.indexOf(workButton);
                workTimers.get(index).stop();
            }
        }
    }

    public void updateWorkTime(int index, String timeString) {
        workTimeLabels.get(index).setText(timeString);
    }




    //todo:      !!   TASK !!
    private String taskWorkString;
    private String timeWorkString;
    private String progressWorkString;

    public void updateWorkInformation (Object source){
        for (JButton saveButton : saveButtons){
            if (saveButton == source){
                int index = saveButtons.indexOf(saveButton);
                taskWorkString = activeTasksLabels.get(index).getText();
                timeWorkString = workTimeLabels.get(index).getText();
                progressWorkString = progressTextFields.get(index).getText();
            }
        }
    }

    public String getTaskWorkString() {
        return taskWorkString;
    }

    public String getTimeWorkString() {
        return timeWorkString;
    }

    public String getProgressWorkString() {
        return progressWorkString;
    }







    /// number of active tasks displayed on the mainFrame
    private static int NO_OF_ACTIVE_TASKS = 0;

    /// with this method we remove the current taskline after we save the progress
    public void removeCurrentTaskLine(Object source) {
        int index = 0;
        for (JButton saveButton : saveButtons) {
            if (saveButton == source) {
                index = saveButtons.indexOf(saveButton);
                break;
            }
        }
        JPanel taskLinePanel = (JPanel) saveButtons.get(index).getParent();
        JPanel tasksPanel = (JPanel) taskLinePanel.getParent();
        tasksPanel.remove(taskLinePanel);

        activeTasksLabels.remove(index);
        workButtons.remove(index);
        workTimeLabels.remove(index);
        progressTextFields.remove(index);
        saveButtons.remove(index);
        resetActionListenersIndex(index);
        workTimers.remove(index);
        NO_OF_ACTIVE_TASKS--;

        ViewUtils.resizeWindowMinus(frame);

        frame.repaint();
        frame.validate();
    }

    /// when a taskline is removed from the tasksPanel, the indexes of the others
    /// action listeners get scrambled, so we have to reset them
    public void resetActionListenersIndex (int index){
        for (int i = index + 1; i < workTimers.size(); i++){
            ActionListener workTimeListener = workTimers.get(i).getActionListeners()[0];
            if (workTimeListener instanceof WorkTimeListener){
                ((WorkTimeListener) workTimeListener).setIndex(i-1);
            }
        }
    }

    /// when a WORK_TIME_PERIOD goes, we have to print a message, so the user will standUp
    public void showStandUpMessage() {
        java.awt.Toolkit.getDefaultToolkit().beep(); // make an audio advertise
        JOptionPane.showMessageDialog(null,"Stand Up! Take a break. Change your position.");
    }

    /// when the user is CURRENTLY_WORKING, the global time is highlight
    public void highlightGlobalTime() {
        mainPanel.globalTimeValueLabel.setForeground(ViewUtils.HIGHLIGHT_LABEL_COLOR);
    }

    /// when the user pause working, we have to UNhighlight the global time
    public void unHighlightGlobalTime() {
        mainPanel.globalTimeValueLabel.setForeground(ViewUtils.LABEL_COLOR);
    }

    /// also, when the user is pause working, we stop all the counters
    public void stopAllCounters() {
        globalTimer.stop();
        for (Timer timer : workTimers){
            timer.stop();
        }
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

            globalTimeValueLabel = new JLabel("0|0");
            globalTimeValueLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            globalTimeValueLabel.setForeground(ViewUtils.LABEL_COLOR);
            this.add(globalTimeValueLabel);

            validate();
        }
    }

    // JPanel for the tasksPanel, where we ADD ACTIVE TASKS, WORK ON THEM and SAVE THEM
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

    // a JPanel with TaskLabel, WorkButton, TimeLabel, ProgressLabel and SaveButton for each task
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
            activeTasksLabels.add(taskDescriptionLabel);

            taskWorkButton = new JButton("WORK");
            taskWorkButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            taskWorkButton.setForeground(ViewUtils.BUTTON_COLOR);
            taskWorkButton.setOpaque(true);
            taskWorkButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            taskWorkButton.setPreferredSize(ViewUtils.WORK_BUTTON_SIZE);
            //////////// MOUSE LISTENER ???
            addTimerActionListener();
            taskWorkButton.addActionListener(workListener);
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
            workTimeLabels.add(taskTime);

            taskProgress = new JTextField(3);
            taskProgress.setText(selectedTaskProgressValue + "%");
            this.add(taskProgress);
            progressTextFields.add(taskProgress);

            ImageIcon saveIcon = new ImageIcon("resources/saveIcon.png");
            Image image = saveIcon.getImage();
            Image resizedImage = image.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            saveIcon = new ImageIcon(resizedImage);
            saveProgressButton = new JButton(saveIcon);
            saveProgressButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
            saveProgressButton.setForeground(ViewUtils.BUTTON_COLOR);
            saveProgressButton.setOpaque(true);
            saveProgressButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
            saveProgressButton.addActionListener(saveListener);
            this.add(saveProgressButton);
            saveButtons.add(saveProgressButton);
        }

        public void addTimerActionListener() {
            if (workTimeListener == null) System.out.println("HOPA");
            else {
                try {
                    WorkTimeListener newWorkTimeListener = (WorkTimeListener) workTimeListener.clone(); // copy
                    newWorkTimeListener.setIndex(NO_OF_ACTIVE_TASKS);
                    newWorkTimeListener.makeTimeZero();
                    Timer timer = new Timer(ViewUtils.TIME_PERIOD, newWorkTimeListener); // 60.000
                    workTimers.add(timer);
                    NO_OF_ACTIVE_TASKS++;
                }
                catch (Exception e){
                    System.out.println("HOPA");
                }
            }
        }
    }

    // a JPanel for ADDING A NEW ACTIVE TASK to the mainFrame
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

    // WindowsListener for comuting thru mainFrame and manageGoalsFrame
    class MyWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
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

    /// getters for the 2 frames
    public JFrame getFrame() {
        return frame;
    }

    public JFrame getChildFrame() {
        return manageGoalsFrame.getFrame();
    }

}


