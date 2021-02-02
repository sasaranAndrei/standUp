package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class StandUpView {
    private JFrame frame;
    private JButton buttons[] = new JButton[2];
    private JLabel labels[] = new JLabel[3];
    Timer globalTimer = new Timer();
    int passedTime[] = new int[2];


    private boolean buttonClicked[] = new boolean[2];

    public StandUpView() {
        frame = new JFrame();
        buttons[0] = new JButton("first");
        buttons[1] = new JButton("second");
        labels[0] = new JLabel("timeFirst");
        labels[1] = new JLabel("timeSecond");
        labels[2] = new JLabel("total");

        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("before " + buttonClicked[0] + " / " + buttonClicked[1]);
                if (buttonClicked[0] == false && buttonClicked[1] == false){ // daca suntem in status PAUZA
                    buttonClicked[0] = true;
                    buttonClicked[1] =  false; // pornim 0 si oprim 1
                    launchTimer(0);
                }
                else { // daca nu suntem in modul pauza

                    if (buttonClicked[0] == true){ // inseamna ca lucrez la task 0
                        //labels[1].setText(String.valueOf(passedTime[1]));
                        // si vreau sa pun stop
                        buttonClicked[0] = false;
                        buttonClicked[1] = false;
                        buttons[1].setEnabled(true);

                    }
                    else { // inseamna ca lucrez la task 1
                        //labels[0].setText(String.valueOf(passedTime[0]));

                        // si vreau sa pun stop
                        buttonClicked[0] = false;
                        buttonClicked[1] = false;

                        buttons[0].setEnabled(true);
                    }

                    //launchTimer(0);
                }
                System.out.println("after " + buttonClicked[0] + " / " + buttonClicked[1]);
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonClicked[1] == false){
                    buttonClicked[1] = true;
                    launchTimer(1);
                    buttonClicked[0] =  false;
                }
                else {

                    buttonClicked[0] = false;
                    buttonClicked[1] = false;

                    //launchTimer(0);
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100,100);
        frame.setResizable(false);
        frame.setVisible(true);
        //frame.setLocation(1109, 200);
        frame.setLayout(new FlowLayout());
        frame.add(buttons[0]);
        frame.add(buttons[1]);
        frame.add(labels[0]);
        frame.add(labels[1]);
        frame.add(labels[2]);

    }

    void launchTimer (int timer){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                passedTime[timer]++;
//                System.out.println(timer + " : " + passedTime[timer]);
                labels[timer].setText(String.valueOf(passedTime[timer]));
                labels[1 - timer].setText(String.valueOf(passedTime[1 - timer]));
                //buttons[1 - timer].setEnabled(buttonClicked[1-timer]);
            }
        };
        globalTimer.schedule(task, 0, 1000);
        System.out.println("launch timer " + timer);
//        labels[timer].setText("timer " + timer);
//        labels[1 - timer].setText("stop");
    }

    public void test (){
        System.out.println("testing VIEW");

    }
}
