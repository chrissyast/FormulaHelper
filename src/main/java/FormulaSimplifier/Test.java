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

        if (answer && currentCondition instanceof Or) {
            if (currentCondition.parent != null) {
                int parentIndex = currentCondition.parent.siblings.indexOf(currentCondition.parent);
                List<Condition> parentSiblings = currentCondition.parent.siblings;
                if (parentIndex + 1 < parentSiblings.size()) {
                    return new Response(parentSiblings.get(parentIndex + 1).getDeepestDescendant());
                }
            }
            return new Response(true);
        }

        if (!answer && currentCondition instanceof Or) {
            int currentConditionIndex = currentCondition.siblings.indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.siblings.size()) {
                return new Response(currentCondition.siblings.get(currentConditionIndex + 1).getDeepestDescendant());
            }
            return new Response(false);
        }

        if (answer && currentCondition instanceof And) {
            int currentConditionIndex = currentCondition.siblings.indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.siblings.size()) {
                return new Response(currentCondition.siblings.get(currentConditionIndex + 1).getDeepestDescendant());
            }
            return new Response(true);
        }

        if (!answer && currentCondition instanceof And) {
            int parentIndex = currentCondition.parent.siblings.indexOf(currentCondition.parent);
            List<Condition> parentSiblings = currentCondition.parent.siblings;
            if (parentIndex + 1 < parentSiblings.size()) {
                return new Response(parentSiblings.get(parentIndex + 1).getDeepestDescendant());
            }
            return new Response(false);
        }

        return null;
    }

    public Condition firstQuestion() {
        return getMostBasicQuestion();
    }

    public String nextQuestion() {
        return "nextQuestion ?";
    }

    private Condition getMostBasicQuestion() {
        return this.ors.get(0).getDeepestDescendant();
    }

    private static List<Condition> separateSiblings(String testString, Condition parent, String separator) {
        String otherSeparator = separator.equals("&&") ? "||" : "&&";
        List<Condition> output = new ArrayList<>();
        int prevOrIndex = 0;
        String remainingString = testString;
        int numberOfSiblings = StringUtils.countMatches(testString, separator);
        if (numberOfSiblings == 0) {
            addToOutput(output, trimAndStripParentheses(testString), separator);
        } else {
            for (int orSequence = 1; orSequence <= numberOfSiblings; orSequence++) {
                int orInd = StringUtils.ordinalIndexOf(testString, separator, orSequence);
                String segment = remainingString.substring(prevOrIndex, orInd);
                if (!isWithinParentheses(segment)) {
                    if (separator.equals("&&")) {
                        output.add(new And(trimAndStripParentheses(segment)));
                    }
                    else {
                        output.add(new Or(trimAndStripParentheses(segment)));
                    }
                    prevOrIndex = orInd + 2;
                }
                if (orSequence == numberOfSiblings) {
                    addToOutput(output, trimAndStripParentheses(remainingString.substring(prevOrIndex)), separator);
                }
            }
            output.forEach(condition -> {
                condition.parent = parent;
                condition.siblings = output;
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

    private static void addToOutput(List<Condition> output, String conditionString, String separator) {
        if (separator.equals("&&")) {
            output.add(new And(conditionString));
        } else {
            output.add(new Or(conditionString));
        }
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

            //TODO make this into a better representation of complexity
           public int complexity(){
               return this.conditionString.length();
           }

           public List<Condition> children() {
               if (this instanceof Or) {return ((Or) this).ands;}
               else return ((And) this).ors;
           }

           public boolean hasChildren() {
               return this.children() != null;
           }

           public Condition getDeepestDescendant() {
               Condition condition = this;
               while (condition.hasChildren()) {
                   condition = condition.children().get(0);
               }
               return condition;
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