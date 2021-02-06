package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ViewUtils {
    /// constants
    static final int RIGHT_BOTTOM_CORNER_Y = 200;
    static final int COMPONENT_TEXT_SIZE = 12;
    static final int SPACE_BETWEEN_COMPONENTS = 2;
    // components color constants
    static final Color MAIN_PANEL_COLOR = Color.decode("#fece6c");
    static final Color BUTTON_COLOR = Color.decode("#003D59");
    static final Color BUTTON_BACKGROUND_COLOR = Color.decode("#cd9100");
    static final Color LABEL_COLOR = Color.decode("#001c2f");
    /// frame & panels constants
    static final Dimension MAIN_PANEL_DIMENSION = new Dimension(400, 40);
    static final Dimension TASK_PANEL_DIMENSION = new Dimension(400, 40);
    static Dimension FRAME_DIMENSION = new Dimension(400, 130); // CAN BE MODIFIED -> not FINAL // height = 130 up to 200
    static final int FRAME_INCREASE_DIMENSION = 27;
    public static final Dimension WORK_BUTTON_SIZE = new Dimension(79, 24); // height = 130 up to 200


    public static void setFrameLocationBottomRightCorner(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - frame.getWidth();
        int y = (int) rect.getMaxY() - frame.getHeight() - RIGHT_BOTTOM_CORNER_Y;
        frame.setLocation(x, y);
    }

    public static void switchFrame (JFrame currentFrame, JFrame nextFrame){
        currentFrame.setVisible(false);
        nextFrame.setVisible(true);
    }


}

    // aux class
    // WindowsListener for saving the data before closing app.
    class MyWindowListener implements WindowListener {

        private JFrame frame;

        public MyWindowListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            //TODO SAVE THE WORK BEFORE CLOSING APP
            System.out.println("he click the X button");
            System.out.println("save the work PLS");
            this.frame.dispose();
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
