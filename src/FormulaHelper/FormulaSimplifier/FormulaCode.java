/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
 
package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;
import java.util.Scanner;

import static FormulaSimplifier.SimplifierGUI.*;



public class FormulaCode {

    public static String formula;


    public static String getFormula() {
        return formula;
    }

    public static void setFormula(String formula) {
        FormulaCode.formula = formula;
    }



    public static void analyse(String formula) {

        if (checkColonsAndQuestionMarks(formula)) {

            textArea.setVisible(false);
            yesButton.setVisible(true);
            noButton.setVisible(true);
            askQuestion(formula);
        }
    }



    public static boolean checkColonsAndQuestionMarks (String formula) {

        int i = StringUtils.countMatches(formula, "?");
        int j = StringUtils.countMatches(formula, ":");

        StringBuilder validationMessage = new StringBuilder("Please double check your formula \n");
        if (i != j || (formula.indexOf(":") < formula.indexOf("?")) || i == 0 || j == 0) {

            if (i != j) {
                validationMessage.append("There is an uneven number of colons and question marks\n");
            }

            if (formula.indexOf(":") < formula.indexOf("?")) {
                validationMessage.append("The first colon comes before the first question mark. Please double check your formula\n");
            }

            if (i == 0 || j == 0) {
                validationMessage.append("Formula should contain at least 1 colon and 1 question mark.");
            }

            questionLabel.setText(validationMessage.toString());
            return false;
        }
        return true;
    }


    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        Boolean goOn;
        int i = 0;
        int reps = 0;

    while (i > 0)  ; // "While there is a question mark in the formula
        String formula = reader.nextLine();
            {
                boolean trueOrFalse = /* answerQuestion(askQuestion(getQuestion(formula)));*/ true;
                    
                if (trueOrFalse == true)
                {
                formula=findTrue(formula);                           
                }else {
                formula=findFalse(formula);
                       }
                i =  StringUtils.countMatches(formula,"?");
            }
                                   
                    System.out.println("Formula will return the result: " + formula);
                    goOn =playAgain();
                    reps++;
                    
               
             while (goOn);
                    if (reps==1) {System.out.println("You have simplified " + reps + " formula in this session!");}
                    else {System.out.println("You have simplified " + reps + " formulae in this session!");}
                    System.out.println("Have a nice day!");
                    reader.nextLine();
                
        }
    
    public static String splitTrueFalse(String formula) {
    int colonOrder = 1;
    int colonInd = StringUtils.ordinalIndexOf(formula,":",colonOrder);
    String toColon = formula.substring(0,colonInd+1);
    String afterColon = formula.substring(colonInd+1);
    String trueFalseOutput;
    int questionMarkCount = StringUtils.countMatches(toColon,"?"); // how many questions marks are before the colonOrder-th colon ?
    int colonCount = StringUtils.countMatches(toColon,":"); // how many colons are there to (and including) the colonOrder-th colon ?
   
    while (questionMarkCount > colonCount) 
            {
            colonOrder++;
            colonInd = StringUtils.ordinalIndexOf(formula,":",colonOrder);                  //  what this is doing is looking at the substring
            toColon = formula.substring(0,colonInd+1);                                      //  up to the first colon. if there are more question marks
            afterColon = formula.substring(colonInd+1);                                     //  than colons in this section, it will repeat until : and ?    
            questionMarkCount = StringUtils.countMatches(toColon,"?");                      //  are even
            colonCount = StringUtils.countMatches(toColon,":");
            } 
            String truePart = toColon.substring(toColon.indexOf("?")+1,toColon.length()-1);
            trueFalseOutput = (truePart + "<-TRUEFALSE->" + afterColon);
    return trueFalseOutput;
    }
    
    
    public static String findTrue(String formula) {
        
 
        String tfOut = splitTrueFalse(formula);
        int end = tfOut.indexOf("<-TRUEFALSE->");
        int start = tfOut.indexOf("?");
     String truePath = "";

if (start == -1) {
                truePath= tfOut.substring(0,end);
                
                }
else {truePath = tfOut.substring(0,end); }
    return truePath;

    }
    

    
    public static String findFalse(String formula) {
        
           String tfOut = splitTrueFalse(formula);
            int start = tfOut.indexOf("<-TRUEFALSE->")+("<-TRUEFALSE->").length();
            String falsePath = tfOut.substring(start);
                   return falsePath;
        
    }
              
            

    public static String getQuestion(String formula) {
        
          int s = 0;
        int questMarkInd = 1;
        int x = StringUtils.ordinalIndexOf(formula,"?",questMarkInd);
        String question = formula.substring(s,x);
                  
    return (question);
        
}
        
    protected static void askQuestion(String formula)    {

             questionLabel.setText("Is " + getQuestion(formula) + " true?");
      }

      // TODO double check that this is redundant and delete if not needed.
  /*  public static Boolean answerQuestion (String question){
        boolean trueOrFalse = true;
         Scanner reader = new Scanner(System.in);
      String input = reader.nextLine();
        if ((!input.equals("true"))&&(!input.equals("false"))){
            System.out.println("Please type 'true' or 'false'");
        }
        return Boolean.valueOf(input);
    } */

    //TODO make this into a GUI-friendly thing
    public static Boolean playAgain ()   {
        Scanner reader = new Scanner(System.in);

    {
      System.out.println("Thanks for playing! Do you want to try another formula?");

      String input = reader.nextLine();
      return input.equals("Y");
              }}
 
            
        }


                
