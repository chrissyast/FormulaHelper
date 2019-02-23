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

    public static void main(String[] args) {

        JFrame f = new JFrame("A JFrame");
        JPanel yesNoPanel = new JPanel();
        f.setSize(500, 500);
        f.setLocation(300,200);


        yesButton.setVisible(false);
        noButton.setVisible(false);
        yesNoPanel.add(yesButton);
        yesNoPanel.add(noButton);

        JPanel questionsAndAnswers  = new JPanel();


        textArea.setEditable(true);
        f.add(BorderLayout.WEST, yesNoPanel);
        f.add(BorderLayout.CENTER, questionsAndAnswers);

        questionsAndAnswers.add(questionLabel);
        questionsAndAnswers.add(textArea);
        questionLabel.setText("What is the formula?");

        if(!textArea.getText().isEmpty())
         {
          final String formula = textArea.getText();
        }


        ActionListener yesListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String formula = textArea.getText();
                int i = StringUtils.countMatches(formula, "?");
                if (i > 1) {
                    String newFormula = "";
                    newFormula = findTrue(formula);
                    questionLabel.setText(askQuestion(newFormula));
                }
               else {questionLabel.setText("The formula will return" + findTrue(formula));}
            }};


            ActionListener noListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //int i = StringUtils.countMatches(formula, "?");
                    //         findFalse(formula);
                }};




        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    String formula = textArea.getText();
                    analyse(formula);

                 /*   if(!FormulaCode.checkColonsAndQuestionMarks(formula)) {
                        validateColonsAndQuestionMarks(formula);
                    }
                        textArea.setVisible(false);
                        questionLabel.setText(FormulaCode.askQuestion(textArea.getText()));
*/

                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        textArea.addKeyListener(keyListener);
        yesButton.addActionListener(yesListener);
        noButton.addActionListener(noListener);




        f.setVisible(true);

    }

    public static void setQuestionText(String message) {

        questionLabel.setText(message);

    }


}
