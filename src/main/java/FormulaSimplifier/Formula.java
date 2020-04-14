

package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;
import javax.swing.*;

import static FormulaSimplifier.SimplifierGUI.*;
import static java.lang.Math.abs;


public class Formula {

    private String originalFormula;
    public static String subFormula;
    private SubClause subClause;
    public static String[] questions;
    public static boolean resolved;
    public static String TRUE_FALSE_SEPARATOR = "<-TRUEFALSE->";

    public Formula(String originalFormula) {
        this.originalFormula = originalFormula;
    }

    public static String getSubFormula() {
        return subFormula;
    }

    public void setSubFormula(String subFormula) {
        Formula.subFormula = subFormula;
        setupQuestions();
    }

    public static String[] getQuestions() {
        return questions;
    }

    public String getOriginalFormula() {
        return originalFormula;
    }

    void setOriginalFormula(String originalFormula) {
        this.originalFormula = originalFormula;
    }

    protected boolean verify() {
        return checkColonsAndQuestionMarks(originalFormula) && checkParentheses(originalFormula);
    }

    public static boolean checkParentheses(String formula) {
       Verification verification = Verification.PARENTHESES;
       return checkOpenersAndClosers(formula, verification);
    }

    public static boolean checkColonsAndQuestionMarks(String formula) {
        Verification verification = Verification.QUESTION_COLON;
        return checkOpenersAndClosers(formula, verification);
    }

    public static boolean checkOpenersAndClosers(String formula, Verification verification) {
        String opener = verification.opener;
        String closer = verification.closer;
        int noOpeners = StringUtils.countMatches(formula, opener);
        int noClosers = StringUtils.countMatches(formula, closer);
        int indexOfOpenersBeforeClosers = areThereClosersBeforeOpeners(formula, opener, closer);
        StringBuilder validationMessage = new StringBuilder("Please double check your formula: \n");
        if (noOpeners != noClosers || (verification.mandatory && (noOpeners == 0 || noClosers == 0)) || indexOfOpenersBeforeClosers != -1) {

            if (noOpeners != noClosers) {
                if (abs(noOpeners - noClosers) > 1) {
                    if (noOpeners > noClosers) {
                        validationMessage.append("There are " + (noOpeners - noClosers) + " more " + verification.openerNamePlural + " than " + verification.closerNamePlural);
                    } else validationMessage.append("There are " + (noClosers - noOpeners) + " more " + verification.closerNamePlural + " than " + verification.openerNamePlural + "s\n");
                } else if (noOpeners > noClosers) {
                    validationMessage.append("There is one more " + verification.openerName + " than " + verification.closerNamePlural + "\n");
                } else {
                    validationMessage.append("There is one more " + verification.closerName + " than " + verification.openerNamePlural + "\n");
                }
            }

            if (verification.mandatory && (noOpeners == 0 || noClosers == 0)) {
                validationMessage.append("Formula should contain at least 1 " + verification.openerName + " and 1 " + verification.closerName + ".\n");
            }
            if (indexOfOpenersBeforeClosers != -1) {
                validationMessage.append(capitalise(verification.closerName) + " " + indexOfOpenersBeforeClosers + " comes before " + verification.openerName + " " + indexOfOpenersBeforeClosers + ".\n");
            }

            JOptionPane.showMessageDialog(f, validationMessage.toString());
            return false;
        }
        return true;
    }

    static String capitalise(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    void handleUserResponse(Boolean userResponse) {
        Test.Response response = this.subClause.test.answerQuestion(userResponse, this.subClause.test.currentCondition);
        if (response.resolvedOutcome != null) {
            String returnedPart = response.resolvedOutcome ? subClause.truePart : subClause.falsePart;
            int i = StringUtils.countMatches(returnedPart, "?");
            if (i > 0) {
                setSubFormula(returnedPart);
                askQuestion();
            } else {
            returnAnswer(returnedPart);
            }
        } else {
            this.subClause.test.currentCondition = response.newQuestion;
            askQuestion(response.newQuestion.conditionString);
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


    public static int areThereClosersBeforeOpeners(String formula, String opener, String closer) {
        int questionMarkCount = StringUtils.countMatches(formula, opener);
        int i = 0;

        while (i <= questionMarkCount) {
            if (StringUtils.ordinalIndexOf(formula, closer, i) < StringUtils.ordinalIndexOf(formula, opener, i)) {
                return i;
            }
            i++;
        }
        return -1;
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

    // TODO move into GUI ?
    protected void askQuestion() {
        questionLabel.setText("Is " + subClause.test.currentCondition.conditionString + " true?");
    }

    protected void askQuestion(String question) {
        questionLabel.setText("Is " + question + " true?");
    }

    public void setupQuestions() {
        this.subClause = new SubClause(findQuestion(subFormula), findTrue(subFormula), findFalse(subFormula));
    }

    public class SubClause {
        private Test test;
        private String truePart;
        private String falsePart;

        public SubClause(String conditionString, String truePart, String falsePart) {
            this.test = new Test(conditionString);
            this.truePart = truePart;
            this.falsePart = falsePart;
        }
    }
}

                
