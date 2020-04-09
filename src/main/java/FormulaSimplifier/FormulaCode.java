

package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;
import javax.swing.*;

import static FormulaSimplifier.SimplifierGUI.*;
import static java.lang.Math.abs;


public class FormulaCode {

    private String originalFormula;
    public static String subFormula;
    private SubClause subClause;
    public static String[] questions;
    public static boolean resolved;
    public static String TRUE_FALSE_SEPARATOR = "<-TRUEFALSE->";

    public FormulaCode(String originalFormula) {
        this.originalFormula = originalFormula;
    }

    public static String getSubFormula() {
        return subFormula;
    }

    public void setSubFormula(String subFormula) {
        FormulaCode.subFormula = subFormula;
        setupQuestions();
    }

    public static String[] getQuestions() {
        return questions;
    }

    public static void setQuestions(String[] questions) {
        FormulaCode.questions = questions;
    }
    public static void setResolved(boolean resolved) {
        FormulaCode.resolved = resolved;
    }


    public String getOriginalFormula() {
        return originalFormula;
    }


    void setOriginalFormula(String originalFormula) {
        this.originalFormula = originalFormula;
    }

    protected boolean verify() {
        return checkColonsAndQuestionMarks(originalFormula);
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

    void handleYesResponse() {
        int i = StringUtils.countMatches(subClause.truePart, "?");
        if (i > 0) {
            setSubFormula(subClause.truePart);
            askQuestion();
        } else {
            returnAnswer(subClause.truePart);
        }
    }

    void handleNoResponse() {
        int i = StringUtils.countMatches(subClause.falsePart, "?");
        if (i > 0) {
            setSubFormula(subClause.falsePart);
            askQuestion();
        } else {
            returnAnswer(subClause.falsePart);
        }
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
        int i = 0;

        while (i <= questionMarkCount) {
            if (StringUtils.ordinalIndexOf(formula, ":", i) < StringUtils.ordinalIndexOf(formula, "?", i)) {
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


    public String findFalse(String formula) {
        String tfOut = splitTrueFalse(formula);
        int start = tfOut.indexOf(TRUE_FALSE_SEPARATOR) + (TRUE_FALSE_SEPARATOR).length();
        String falsePath = tfOut.substring(start);
        return falsePath;
    }


    private String findQuestion(String formula) {
        int s = 0;
        int questMarkInd = 1;
        int x = StringUtils.ordinalIndexOf(formula, "?", questMarkInd);
        String question = formula.substring(s, x);
        return (question);
    }
        
    protected void askQuestion()    {
            questionLabel.setText("Is " + subClause.condition + " true?");
      }



    public void setupQuestions() {
        this.subClause = new SubClause(findQuestion(subFormula), findTrue(subFormula), findFalse(subFormula));
    }

    public class SubClause {
        private String condition;
        private String truePart;
        private String falsePart;

        public SubClause(String condition, String truePart, String falsePart) {
            this.condition = condition;
            this.truePart = truePart;
            this.falsePart = falsePart;
        }
    }
}

                
