package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {

    List<Condition> ors;
    String testString;
   // String question;
   // Response response;
    Condition currentCondition;

    public Test(List<Condition> ors) {
        this.ors = ors;
    }

    public Test (String testString) {
        this.testString = testString;
        this.ors = separateSiblings(testString, null, "||");
        this.ors.sort((Condition a1, Condition a2) -> {
            return a1.complexity() - a2.complexity();
        });
        this.ors.forEach(o -> {
            o.siblings = this.ors;
        });
            this.currentCondition = getMostBasicQuestion();

    }

    public Response answerQuestion(boolean answer, Condition currentCondition) {
        if (answer && currentCondition instanceof Or && currentCondition.parent == null) {
            return new Response(true);
        }
        if (!answer && currentCondition instanceof Or && currentCondition.siblings == null) {
            return new Response(false);
        }
        if (!answer && currentCondition instanceof Or && currentCondition.siblings != null) {
            int currentConditionIndex = currentCondition.siblings.indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.siblings.size()) {
                return new Response(currentCondition.siblings.get(currentConditionIndex + 1));
            }
            else return new Response(false);
        }
        else return null;
    }

    public Condition firstQuestion() {
        return getMostBasicQuestion();
    }

    public String nextQuestion() {
        return "nextQuestion ?";
    }

    private Condition getMostBasicQuestion() {
        return this.ors.get(0);
    }
/*
    private static List<Condition> separateOrs(String testString, Condition parent) {
        List<Condition> output = new ArrayList<>();
        int prevOrIndex = 0;
        String remainingString = testString;
        int numberOfOrs = StringUtils.countMatches(testString, "||");
        if (numberOfOrs == 0) {
            output.add(new Or(trimAndStripParentheses(testString)));
        } else {
            for (int orSequence = 1; orSequence <= numberOfOrs; orSequence++) {
                int orInd = StringUtils.ordinalIndexOf(testString, "||", orSequence);
                String segment = remainingString.substring(prevOrIndex, orInd);
                if (!isWithinParentheses(segment)) {
                    output.add(new Or(trimAndStripParentheses(segment)));
                    prevOrIndex = orInd + 2;
                }
                if (orSequence == numberOfOrs) {
                    output.add(new Or(trimAndStripParentheses(remainingString.substring(prevOrIndex))));
                }
            }
            output.forEach(condition -> {
                ((Or) condition).parent = parent;
                if (parent != null) {
                    ((Or) condition).siblings = parent.children();
                }
                if (condition.conditionString.indexOf("&&") > -1) {
                    ((Or) condition).ands = separateAnds(condition.conditionString, condition);
                }
            });
        }
         return output;
    }

    private static List<Condition> separateAnds(String testString, Condition parent) {
        List<Condition> output = new ArrayList<>();
        int prevOrIndex = 0;
        String remainingString = testString;
        int numberOfAnds = StringUtils.countMatches(testString, "&&");
        if (numberOfAnds == 0) {
            output.add(new Or(trimAndStripParentheses(testString)));
        } else {
            for (int orSequence = 1; orSequence <= numberOfAnds; orSequence++) {
                int orInd = StringUtils.ordinalIndexOf(testString, "&&", orSequence);
                String segment = remainingString.substring(prevOrIndex, orInd);
                if (!isWithinParentheses(segment)) {
                    output.add(new And(trimAndStripParentheses(segment)));
                    prevOrIndex = orInd + 2;
                }
                if (orSequence == numberOfAnds) {
                    output.add(new And(trimAndStripParentheses(remainingString.substring(prevOrIndex))));
                }
            }
            output.forEach(condition -> {
                ((And) condition).parent = parent;
                if (parent != null) {
                    ((And) condition).siblings = parent.children();
                }
                if (condition.conditionString.indexOf("||") > -1) {
                    ((And) condition).ors = separateOrs(condition.conditionString, condition);

                }
            });
        }
     return output;
    }
*/
    private static List<Condition> separateSiblings(String testString, Condition parent, String separator) {
        String otherSeparator = separator.equals("&&") ? "||" : "&&";
        List<Condition> output = new ArrayList<>();
        int prevOrIndex = 0;
        String remainingString = testString;
        int numberOfSiblings = StringUtils.countMatches(testString, separator);
        if (numberOfSiblings == 0) {
            output.add(new Or(trimAndStripParentheses(testString)));
        } else {
            for (int orSequence = 1; orSequence <= numberOfSiblings; orSequence++) {
                int orInd = StringUtils.ordinalIndexOf(testString, separator, orSequence);
                String segment = remainingString.substring(prevOrIndex, orInd);
                if (!isWithinParentheses(segment)) {
                    output.add(new And(trimAndStripParentheses(segment)));
                    prevOrIndex = orInd + 2;
                }
                if (orSequence == numberOfSiblings) {
                    output.add(new And(trimAndStripParentheses(remainingString.substring(prevOrIndex))));
                }
            }
            output.forEach(condition -> {
                condition.parent = parent;
                if (parent != null) {
                    condition.siblings = parent.children();
                }
                if (condition.conditionString.indexOf(otherSeparator) > -1) {
                    if (condition instanceof And) {
                        ((And) condition).ors = separateSiblings(condition.conditionString, condition, otherSeparator);
                    } else {
                        ((Or) condition).ands = separateSiblings(condition.conditionString, condition, otherSeparator);
                    }
                }
            });
        }
     return output;
    }

    private static String trimAndStripParentheses(String string) {
            string = string.trim();
            while (string.startsWith("(") && string.endsWith(")")) {
                string = string.substring(1, string.length()-1).trim();
            }
            return string;
    }

    private static boolean isWithinParentheses(String string) {
        return StringUtils.countMatches(string, "(") > StringUtils.countMatches(string, ")");
    }

    public static class Condition {
        public String conditionString;
        public Condition parent;
        public List<Condition> siblings;

           public int complexity(){
               return this.conditionString.length();
           }

           public List<Condition> children() {
               if (this instanceof Or) {return ((Or) this).ands;}
               else return ((And) this).ors;
           }

    }

    public static class Or extends Condition {
        private List<Condition> ands;

        public Or(String condition) {
            this.conditionString = condition;
        }

        public Or(List<Condition> ands) {
            this.ands = ands;
        }

    }

    public static class And extends Condition {
        private List<Condition> ors;
      //  private String conditionString;

        public And(List<Condition> ors) {
            this.ors = ors;
        }

        public And(String conditionString) {
            this.conditionString = conditionString;
        }
    }

    public static class Response {
        boolean resolved;
        Boolean resolvedOutcome;
        Condition newQuestion;

        public Response(Condition newQuestion) {
            this.newQuestion = newQuestion;
        }

        public Response(boolean resolvedOutcome) {
          //  this.resolved = resolved;
            this.resolvedOutcome = resolvedOutcome;
          //  this.newQuestion = newQuestion;
        }
    }
}