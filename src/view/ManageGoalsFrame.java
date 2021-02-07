package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import controller.*;

public class ManageGoalsFrame {

    // frame & panels
    private JFrame frame;
    private JFrame parentFrame;

    private MainPanel mainPanel;
    private DisplayPanel displayPanel;

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
        frame.setSize(ViewUtils.FRAME_DIMENSION);
        ViewUtils.setFrameLocationBottomRightCorner(frame);
        frame.setResizable(false);
        frame.setVisible(true);

        // frame layout & panels
        frame.setLayout(new BorderLayout());
        mainPanel = new MainPanel();
        displayPanel = new DisplayPanel();

        // add components
        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(displayPanel, BorderLayout.CENTER);

        frame.validate();
    }

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
            //addGoalButton.addActionListener();
            this.add(addGoalButton);
        }
    }

    private class DisplayPanel extends JPanel {
        private JButton addGoalButton;
        private JButton editGoalButton;

        public DisplayPanel() {

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



