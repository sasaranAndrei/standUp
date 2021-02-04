package view;

import model.Description;
import model.Goal;
import model.Task;
import model.Time;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Date;

public class StandUpView {
    /// frame & panels constants
    private static Dimension MAIN_PANEL_DIMENSION = new Dimension(400, 40);
    private static Dimension TASK_PANEL_DIMENSION = new Dimension(400, 40);
    private static Dimension FRAME_DIMENSION = new Dimension(400, 130); // height = 130 up to 200
    private static int FRAME_INCREASE_DIMENSION = 27;
    private static Dimension WORK_BUTTON_SIZE = new Dimension(79, 24); // height = 130 up to 200

    private static final int RIGHT_BOTTOM_CORNER_Y = 200;
    private static final int COMPONENT_TEXT_SIZE = 12;
    private static final int SPACE_BETWEEN_COMPONENTS = 2;


    // components color constants
    private static Color MAIN_PANEL_COLOR = Color.decode("#fece6c");
    private static Color BUTTON_COLOR = Color.decode("#003D59");
    private static Color BUTTON_BACKGROUND_COLOR = Color.decode("#cd9100");
    private static Color LABEL_COLOR = Color.decode("#001c2f");


    // frame & panels variables
    private JFrame frame;
    private MainPanel mainPanel;
    private TasksPanel descriptionPanel;
    private TasksPanel tasksPanel;

    //
    private static Dimension FRAME_LOCATION = new Dimension(400, 40);
    //

    public StandUpView() {
        frame = new JFrame(); // viewFrame
        frame.setTitle("StandApp");
        // save and close operations
        frame.addWindowListener(new MyWindowListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // size & location of frame
        frame.setSize(FRAME_DIMENSION);
        setFrameLocationBottomRightCorner(frame);
        frame.setResizable(false);
        frame.setVisible(true);

        // frame layout & panels
        frame.setLayout(new BorderLayout());
        mainPanel = new MainPanel();
        //descriptionPanel = new TasksPanel();
        tasksPanel = new TasksPanel();
        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(tasksPanel, BorderLayout.CENTER);
        //frame.add(tasksPanel, BorderLayout.SOUTH);

        //frame.pack();
        frame.validate();
    }

    private static void setFrameLocationBottomRightCorner(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - frame.getWidth();
        int y = (int) rect.getMaxY() - frame.getHeight() - RIGHT_BOTTOM_CORNER_Y;
        frame.setLocation(x, y);
    }

    public void test (){
        System.out.println("testing VIEW");
    }

    // JPanel for the main (header part of the app)
    private class MainPanel extends JPanel {
        private JButton manageGoalsButton;
        private JLabel globalTimeLabel;
        private JLabel globalTimeValueLabel;

        public MainPanel() {
            // size
            this.setSize(MAIN_PANEL_DIMENSION);
            // layout & components
            //this.setLayout(new GridLayout(0,3,1,1));
            this.setLayout(new FlowLayout());
            //this.setLayout(new BorderLayout());
            createComponents();


            this.setBackground(MAIN_PANEL_COLOR);
            this.validate();
        }

        void createComponents (){
            manageGoalsButton = new JButton("MANAGE GOALS");
            manageGoalsButton.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            manageGoalsButton.setForeground(BUTTON_COLOR);
            manageGoalsButton.setOpaque(true);
            manageGoalsButton.setBackground(BUTTON_BACKGROUND_COLOR);
            this.add(manageGoalsButton);
            //this.add(manageGoalsButton, 0,0);

            globalTimeLabel = new JLabel("TOTAL WORK TIME : ");
            globalTimeLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            globalTimeLabel.setForeground(LABEL_COLOR);
            this.add(globalTimeLabel);
            //this.add(globalTimeLabel, 0, 1);

            globalTimeValueLabel = new JLabel("00:00");
            globalTimeValueLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            globalTimeValueLabel.setForeground(LABEL_COLOR);
            //this.add(globalTimeValueLabel, 0, 2);
            this.add(globalTimeValueLabel);

            //this.add(new JLabel());
            /*
            activeTasksLabel = new JLabel("ACTIVE TASKS");
            activeTasksLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, 13));
            activeTasksLabel.setForeground(LABEL_COLOR);
            //this.add(activeTasksLabel, 1, 0);
            this.add(activeTasksLabel);


            private JLabel ;
            private JLabel workLabel;
            private JLabel timeLabel;
            private JLabel progressLabel;

             */
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

//        GridBagConstraints constraints;
//        Insets insets;

        public TasksPanel() {
            // model
            tasks = new ArrayList<>();

            // view
            this.setSize(TASK_PANEL_DIMENSION);

            // layout & components
            //this.setLayout(new GridLayout(0,3,1,1));
            //this.setLayout(new GridBagLayout());
            this.setLayout(new BorderLayout());
            initComponents();

            this.setBackground(MAIN_PANEL_COLOR);
            this.validate();
        }

        private JPanel createDescriptionPanel (){
            ///TODO set colors and stuff
            JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            /// active tasks label
            activeTasksLabel = new JLabel("TASKS");
            activeTasksLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            activeTasksLabel.setForeground(LABEL_COLOR);
            descriptionPanel.add(activeTasksLabel);

            descriptionPanel.add(new JLabel("  "));
            /// add task button
            addTaskButton = new JButton("ADD TASK");
            addTaskButton.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            addTaskButton.setForeground(BUTTON_COLOR);
            addTaskButton.setOpaque(true);
            addTaskButton.setBackground(BUTTON_BACKGROUND_COLOR);
            addTaskButton.addActionListener(e -> addTaskLinePanel()); // !!!!!!!!
            descriptionPanel.add(addTaskButton);

            descriptionPanel.add(new JLabel("   "));
            /// workLabel
            workLabel = new JLabel("WORK");
            workLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            workLabel.setForeground(LABEL_COLOR);
            descriptionPanel.add(workLabel);

            descriptionPanel.add(new JLabel("   "));
            /// timeLabel
            timeLabel = new JLabel("TIME");
            timeLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            timeLabel.setForeground(LABEL_COLOR);
            descriptionPanel.add(timeLabel);

            /// progressLabel
            progressLabel = new JLabel("PROGRESS");
            progressLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            progressLabel.setForeground(LABEL_COLOR);
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
            resizeWindow();

            TaskLinePanel taskLinePanel = new TaskLinePanel();
            tasks.add(taskLinePanel.task); // model
            taskRowsPanel.add(taskLinePanel);
            validate();
        }

        private void resizeWindow (){
            FRAME_DIMENSION = new Dimension(FRAME_DIMENSION.width, FRAME_DIMENSION.height + FRAME_INCREASE_DIMENSION);
            frame.setSize(FRAME_DIMENSION);
            frame.validate();

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

            this.setBackground(MAIN_PANEL_COLOR);
            this.validate();

        }

        public TaskLinePanel(Task newTask) {
            task = newTask;

        }

        private void initComponents (){
            ///TODO : put 'spaces' between elements of panel, for keeping an alignment
            taskDescriptionLabel = new JLabel(task.getTaskDescription(), SwingConstants.LEFT);
            taskDescriptionLabel.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            taskDescriptionLabel.setForeground(LABEL_COLOR);
            this.add(taskDescriptionLabel);

            taskWorkButton = new JButton("WORK");
            taskWorkButton.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            taskWorkButton.setForeground(BUTTON_COLOR);
            taskWorkButton.setOpaque(true);
            taskWorkButton.setBackground(BUTTON_BACKGROUND_COLOR);
            taskWorkButton.setPreferredSize(WORK_BUTTON_SIZE);
            this.add(taskWorkButton);

            taskTime = new JLabel(currentWorkTime.toString());
            taskTime.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            taskTime.setForeground(LABEL_COLOR);
            this.add(taskTime);

            taskProgress = new JTextField(3);
            taskProgress.setText(task.getProcentValue() + "%");
            this.add(taskProgress);


            ImageIcon saveIcon = new ImageIcon("resources/saveIcon.png");
            Image image = saveIcon.getImage();
            Image resizedImage = image.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
            saveIcon = new ImageIcon(resizedImage);
            saveProgressButton = new JButton(saveIcon);
            saveProgressButton.setFont(new Font("Bodoni MT Black", Font.BOLD, COMPONENT_TEXT_SIZE));
            saveProgressButton.setForeground(BUTTON_COLOR);
            saveProgressButton.setOpaque(true);

//            saveProgressButton.setSize(100,100);
            saveProgressButton.setBackground(BUTTON_BACKGROUND_COLOR);



//            saveProgressButton.setIcon();
            this.add(saveProgressButton);
        }
    }

    // WindowsListener for saving the data before closing app.
    private class MyWindowListener implements WindowListener{

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            //TODO SAVE THE WORK BEFORE CLOSING APP
            System.out.println("he click the X button");
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
}
