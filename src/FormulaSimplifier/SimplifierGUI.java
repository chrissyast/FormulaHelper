package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static FormulaSimplifier.FormulaCode.*;



public class SimplifierGUI  {

    public static JLabel questionLabel = new JLabel();
    public static JButton yesButton = new JButton("Yes");
    public static JButton noButton = new JButton("No");
    public static JTextArea textArea = new JTextArea(10, 40);
    public static JPanel yesNoPanel = new JPanel();
    public static JButton restartButton = new JButton("Restart");
    public static Frame f = new JFrame("Formula Simplifier");

    public static void main(String[] args) {

        f.setSize(500, 500);
        f.setLocation(300,200);

        initialise();

        JPanel questionsAndAnswers  = new JPanel();


        textArea.setEditable(true);
        f.add(BorderLayout.WEST, yesNoPanel);
        f.add(BorderLayout.CENTER, questionsAndAnswers);

        questionsAndAnswers.add(questionLabel);
        questionsAndAnswers.add(textArea);
        questionLabel.setText("What is the formula?");


        ActionListener yesListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i = StringUtils.countMatches(findTrue(formula), "?");
                if (i > 1) {

                    setFormula(findTrue(formula));
                    askQuestion(formula);

                }
               else {returnAnswer(findTrue(formula));
               }
            }};


            ActionListener noListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i = StringUtils.countMatches(findFalse(formula), "?");
                    if (i > 1) {

                        setFormula(findFalse(formula));
                        askQuestion(formula);

                    } else {
                        returnAnswer(findFalse(formula));
                    }
                }};

        ActionListener restartListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        };


        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    setFormula(textArea.getText().replace("/n", ""));
                    analyse(formula);


                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        textArea.addKeyListener(keyListener);
        yesButton.addActionListener(yesListener);
        noButton.addActionListener(noListener);
        restartButton.addActionListener(restartListener);



        f.setVisible(true);

    }

    public static void setQuestionText(String message) {
        questionLabel.setText(message);
    }


}
