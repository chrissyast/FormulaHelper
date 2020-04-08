

package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;
import javax.swing.*;
import java.util.Scanner;

import static FormulaSimplifier.SimplifierGUI.*;
import static java.lang.Math.abs;


public class FormulaCode {

    public static String originalFormula;

    public static String getSubFormula() {
        return subFormula;
    }

    public static void setSubFormula(String subFormula) {
        FormulaCode.subFormula = subFormula;
    }

    public static String[] getQuestions() {
        return questions;
    }

    public static void setQuestions(String[] questions) {
        FormulaCode.questions = questions;
    }

    public static String subFormula;
    public static String[] questions;

    public static void setResolved(boolean resolved) {
        FormulaCode.resolved = resolved;
    }

    public static boolean resolved;
    public static String TRUE_FALSE_SEPARATOR = "<-TRUEFALSE->";


    public static String getOriginalFormula() {
        return originalFormula;
    }

    public static void initialise() {

        yesButton.setVisible(false);
        noButton.setVisible(false);
        yesNoPanel.add(yesButton);
        yesNoPanel.add(noButton);
    }

    public static void restart() {
        questionLabel.setText("What is the formula?");
        questionLabel.setVisible(true);
        textArea.setText("");
        textArea.setVisible(true);
        yesNoPanel.setVisible(false);

    }

    public static void setOriginalFormula(String originalFormula) {
        FormulaCode.originalFormula = originalFormula;
    }


    public static void analyse(String formula) {

        if (checkColonsAndQuestionMarks(formula)) {

            setUIForQuestion();
            setQuestions(StringUtils.split(findQuestion(formula), "&&"));
            //   if (StringUtils.countMatches(formula, "&&") > 0) {
            //       separateAndsAndOrs(findQuestion(formula));
            //   }
            //  while (!resolved)
            for (String question : questions) {
                askDirectQuestion(question);
            }
        }
    }

    private static void setUIForQuestion() {
        textArea.setVisible(false);
        yesButton.setVisible(true);
        noButton.setVisible(true);
        yesButton.setEnabled(true);
        noButton.setEnabled(true);
        yesNoPanel.setVisible(true);
    }

    private static void awaitQuestionResponse(String question) {
        while (resolved) {

        }

    }

  /*  public static void separateAndsAndOrs(String formula) {

        if (StringUtils.countMatches(formula, "&&") > 0) {
            String[] clauses = StringUtils.split(formula, "&&");

        }

    }*/

    public static boolean checkColonsAndQuestionMarks(String formula) {

        int i = StringUtils.countMatches(formula, "?");
        int j = StringUtils.countMatches(formula, ":");
        StringBuilder validationMessage = new StringBuilder("Please double check your formula: \n");
        if (i != j || (formula.indexOf(":") < formula.indexOf("?")) || i == 0 || j == 0 || areThereColonsBeforeQuestionMarks(formula)) {

            if (i != j) {
                if (abs(i - j) > 1) {
                    if (i > j) {
                        validationMessage.append("There are " + (i - j) + " more question marks than colons\n");
                    } else validationMessage.append("There are " + (j - i) + " more question marks than colons\n");
                } else if (i > j) {
                    validationMessage.append("There is one more question mark than colons\n");
                } else {
                    validationMessage.append("There is one more colon than question marks\n");
                }
            }

            if (i == 0 || j == 0) {
                validationMessage.append("Formula should contain at least 1 colon and 1 question mark.\n");
            }
            if ((i == j) && areThereColonsBeforeQuestionMarks(formula)) {
                validationMessage.append("Colon " + i + " comes before question mark " + i + ".\n");
            }

            JOptionPane.showMessageDialog(f, validationMessage.toString());
            return false;
        }
        return true;
    }

    public static void handleYesResponse(String subFormula) {
        int i = StringUtils.countMatches(findTrue(subFormula), "?");
        if (i > 0) {

            setSubFormula(findTrue(subFormula));
            askDirectQuestion(subFormula);

        } else {
            returnAnswer(findTrue(subFormula));
        }
        //  setResolved(false);
    }

    public static void handleNoResponse(String subFormula) {
        int i = StringUtils.countMatches(findFalse(originalFormula), "?");
        if (i > 0) {

            setOriginalFormula(findFalse(originalFormula));
            askDirectQuestion(originalFormula);

        } else {
            returnAnswer(findFalse(originalFormula));
        }
        // setResolved(false);
    }

    public static String splitTrueFalse(String formula) {
        int colonOrder = 1;
        int colonInd = StringUtils.ordinalIndexOf(formula, ":", colonOrder);
        String toColon = formula.substring(0, colonInd + 1);
        String afterColon = formula.substring(colonInd + 1);
        String trueFalseOutput;
        int questionMarkCount = StringUtils.countMatches(toColon, "?"); // how many questions marks are before the colonOrder-th colon ?
        int colonCount = StringUtils.countMatches(toColon, ":"); // how many colons are there to (and including) the colonOrder-th colon ?

        while (questionMarkCount > colonCount) {
            colonOrder++;
            colonInd = StringUtils.ordinalIndexOf(formula, ":", colonOrder);                  //  what this is doing is looking at the substring
            toColon = formula.substring(0, colonInd + 1);                                      //  up to the first colon. if there are more question marks
            afterColon = formula.substring(colonInd + 1);                                     //  than colons in this section, it will repeat until : and ?
            questionMarkCount = StringUtils.countMatches(toColon, "?");                      //  are even
            colonCount = StringUtils.countMatches(toColon, ":");
        }
        String truePart = toColon.substring(toColon.indexOf("?") + 1, toColon.length() - 1);
        trueFalseOutput = (truePart + TRUE_FALSE_SEPARATOR + afterColon);
        return trueFalseOutput;
    }


    public static boolean areThereColonsBeforeQuestionMarks(String formula) {
        int questionMarkCount = StringUtils.countMatches(formula, "?");
        // int x;
        int i = 0;

        while (i <= questionMarkCount) {
            if (StringUtils.ordinalIndexOf(formula, ":", i) < StringUtils.ordinalIndexOf(formula, "?", i)) {
                //   x = i;
                return true;
            }
            i++;
        }
        return false;
    }

    public static String findTrue(String formula) {


        String tfOut = splitTrueFalse(formula);
        int end = tfOut.indexOf(TRUE_FALSE_SEPARATOR);
        int start = tfOut.indexOf("?");
        String truePath = "";

        if (start == -1) {
            truePath = tfOut.substring(0, end);

        } else {
            truePath = tfOut.substring(0, end);
        }
        return truePath;

    }


    public static String findFalse(String formula) {

        String tfOut = splitTrueFalse(formula);
        int start = tfOut.indexOf(TRUE_FALSE_SEPARATOR) + (TRUE_FALSE_SEPARATOR).length();
        String falsePath = tfOut.substring(start);
        return falsePath;

    }


    public static String findQuestion(String formula) {

        int s = 0;
        int questMarkInd = 1;
        int x = StringUtils.ordinalIndexOf(formula, "?", questMarkInd);
        String question = formula.substring(s, x);

        return (question);

    }
        
   /* protected static void askQuestion(String formula)    {

             questionLabel.setText("Is " + findQuestion(formula) + " true?");
      }*/

    protected static void askDirectQuestion(String question) {
        questionLabel.setText("Is " + question + " true?");
        // setResolved(true);
    }


    protected static void returnAnswer(String answer) {
        questionLabel.setText("The formula will return " + answer);
        yesNoPanel.add(restartButton);
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
    }
}

                
