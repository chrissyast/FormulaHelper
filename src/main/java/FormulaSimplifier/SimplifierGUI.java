package FormulaSimplifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static FormulaSimplifier.Formula.*;



public class SimplifierGUI  {

    public static JLabel questionLabel = new JLabel();
    public static JButton yesButton = new JButton("Yes");
    public static JButton noButton = new JButton("No");
    public static JTextArea textArea = new JTextArea(10, 40);
    public static JPanel yesNoPanel = new JPanel();
    // TODO implement one button for restart with old formula, one to restart with brand new formula
    public static JButton restartButton = new JButton("Restart");
    public static Frame f = new JFrame("Formula Simplifier");
    public static Formula formula;

    public static void main(String[] args) {

        f.setSize(500, 500);
        f.setLocation(300,200);

        initialise();

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand().equals("Yes")) {
                    formula.handleYesResponse();
                }
                if (actionEvent.getActionCommand().equals("No")) {
                    formula.handleNoResponse();
                }
            }
        };

        ActionListener restartListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
                textArea.setText(formula.getOriginalFormula());
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
                    formula = new Formula(textArea.getText().replace("/n", ""));
                    subFormula = formula.getOriginalFormula();
                    if (formula.verify()) {
                        formula.setupQuestions();
                        setUIForQuestions();
                        formula.askQuestion();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        textArea.addKeyListener(keyListener);
      //  yesButton.addActionListener(yesListener);
        yesButton.addActionListener(actionListener);
      //  noButton.addActionListener(noListener);
        noButton.addActionListener(actionListener);
      //  restartButton.addActionListener(restartListener);
        restartButton.addActionListener(restartListener);
        f.setVisible(true);
    }

    static void returnAnswer(String answer) {
        questionLabel.setText("The formula will return " + answer);
        yesNoPanel.add(restartButton);
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
    }

    private static void setUIForQuestions() {
        textArea.setVisible(false);
        yesButton.setVisible(true);
        noButton.setVisible(true);
        yesButton.setEnabled(true);
        noButton.setEnabled(true);
        yesNoPanel.setVisible(true);
    }

    public static void initialise() {
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
    }

    public static void restart() {
        questionLabel.setText("What is the formula?");
        questionLabel.setVisible(true);
        textArea.setText("");
        textArea.setVisible(true);
        yesNoPanel.setVisible(false);

    }


}
