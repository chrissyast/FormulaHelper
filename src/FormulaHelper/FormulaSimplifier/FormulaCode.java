/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
 
package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;
import java.util.Scanner;

public class FormulaCode {

    /**
     * @param args the command line arguments
     */

    public static boolean checkColonsAndQuestionMarks (String formula) {

        int i = StringUtils.countMatches(formula, "?");
        int j = StringUtils.countMatches(formula, ":");

        if (i != j) {
            System.out.println("There is an uneven number of colons and question marks. Please double check your formula");
            System.out.println("Press enter to continue...");
            return false;
        }

        if (formula.indexOf(":") < formula.indexOf("?")) {
            System.out.println("The first colon comes before the first question mark. Please double check your formula");
            System.out.println("Press enter to continue...");
            return false;
        }
        return true;
    }


    public static String validateColonsAndQuestionMarks(String formula){
        int i = StringUtils.countMatches(formula, "?");
        int j = StringUtils.countMatches(formula, ":");
        StringBuilder validationMessage = new StringBuilder();

        if (i != j) {
            validationMessage.append("There is an uneven number of colons and question marks. Please double check your formula");
        }

        if (formula.indexOf(":") < formula.indexOf("?")) {
            validationMessage.append("The first colon comes before the first question mark. Please double check your formula");
        }

        return validationMessage.toString();
    }

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        Boolean goOn;
        int i = 0;
        int reps = 0;
    /*    do {  //Scanner reader = new Scanner(System.in);
            System.out.println("What is the formula?");
            String formula = reader.nextLine();

        }  */
    while (i > 0)  ; // "While there is a question mark in the formula
        String formula = reader.nextLine();
            {

                boolean trueOrFalse =  answerQuestion(askQuestion(getQuestion(formula)));
                    
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
    //String beforeColon = formula.substring(0,colonInd);
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
//else {truePath = tfOut.substring(start-1,end); }
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
        
    protected static String askQuestion(String formula)    {
   // Scanner reader = new Scanner(System.in);
    //boolean trueOrFalse = true;
    {
      /*while (true)*/ //{System.out.println("Is " + question + " true?");

      /*String input = reader.nextLine();
      if ((!input.equals("true"))&&(!input.equals("false"))){
          System.out.println("Please type 'true' or 'false'");
              }
      else {trueOrFalse = Boolean.valueOf(input);
              break;
             */ }
             return ("Is " + getQuestion(formula) + " true?");
      }
       
    //return trueOrFalse;
    //}


    public static Boolean answerQuestion (String question){
        boolean trueOrFalse = true;
         Scanner reader = new Scanner(System.in);
      String input = reader.nextLine();
        if ((!input.equals("true"))&&(!input.equals("false"))){
            System.out.println("Please type 'true' or 'false'");
        }
        return Boolean.valueOf(input);
    }

    
    public static Boolean playAgain ()   {
        Scanner reader = new Scanner(System.in);

    {
      System.out.println("Thanks for playing! Do you want to try another formula?");
                    System.out.println("Press Y to play again, any other key to quit");
              
       String input = reader.nextLine();
      return input.equals("Y");
              }}
 
            
        }


                
