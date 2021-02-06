package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageGoalsFrame {

    // frame & panels
    private JFrame frame;
    private JFrame pframe;

    public ManageGoalsFrame(JFrame parentFrame) {
        parentFrame.setVisible(false);
        pframe = parentFrame;

        frame = new JFrame(); // viewFrame
        frame.setTitle("Goal Manager");
        // save and close operations
        frame.addWindowListener(new MyWindowListener(frame));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE!!!!!
        // size & location of frame
        frame.setSize(ViewUtils.FRAME_DIMENSION);
        ViewUtils.setFrameLocationBottomRightCorner(frame);

        createComponents();

        frame.setResizable(false);
        frame.setVisible(true);


    }
    void createComponents (){
        JButton manageGoalsButton;
        manageGoalsButton = new JButton("MANAGE GOALS");
        manageGoalsButton.setFont(new Font("Bodoni MT Black", Font.BOLD, ViewUtils.COMPONENT_TEXT_SIZE));
        manageGoalsButton.setForeground(ViewUtils.BUTTON_COLOR);
        manageGoalsButton.setOpaque(true);
        manageGoalsButton.setBackground(ViewUtils.BUTTON_BACKGROUND_COLOR);
        //// ACTION LISTENER
        //TODO write that shit GOODD!!!!
        manageGoalsButton.addActionListener(e -> {
            pframe.setVisible(true);
            frame.dispose();
        });
        frame.add(manageGoalsButton);
    }
}
