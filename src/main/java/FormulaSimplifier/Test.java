package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {

    List<Condition> ors;
    Condition currentCondition;

    public Test(List<Condition> ors) {
        this.ors = ors;
    }

    public Test (String testString) {
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

        if (answer && currentCondition instanceof Condition.Or) {
            if (currentCondition.parent != null) {
                int parentIndex = currentCondition.parent.siblings.indexOf(currentCondition.parent);
                List<Condition> parentSiblings = currentCondition.parent.siblings;
                if (parentIndex + 1 < parentSiblings.size()) {
                    return new Response(parentSiblings.get(parentIndex + 1).getDeepestDescendant());
                }
            }
            return new Response(true);
        }

        if (!answer && currentCondition instanceof Condition.Or) {
            int currentConditionIndex = currentCondition.siblings.indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.siblings.size()) {
                return new Response(currentCondition.siblings.get(currentConditionIndex + 1).getDeepestDescendant());
            }
            return new Response(false);
        }

        if (answer && currentCondition instanceof Condition.And) {
            int currentConditionIndex = currentCondition.siblings.indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.siblings.size()) {
                return new Response(currentCondition.siblings.get(currentConditionIndex + 1).getDeepestDescendant());
            }
            return new Response(true);
        }

        if (!answer && currentCondition instanceof Condition.And) {
            int parentIndex = currentCondition.parent.siblings.indexOf(currentCondition.parent);
            List<Condition> parentSiblings = currentCondition.parent.siblings;
            if (parentIndex + 1 < parentSiblings.size()) {
                return new Response(parentSiblings.get(parentIndex + 1).getDeepestDescendant());
            }
            return new Response(false);
        }

        return null;
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
                        output.add(new Condition.And(trimAndStripParentheses(segment)));
                    }
                    else {
                        output.add(new Condition.Or(trimAndStripParentheses(segment)));
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
                    if (condition instanceof Condition.And) {
                        ((Condition.And) condition).ors = separateSiblings(condition.conditionString, condition, otherSeparator);
                    } else {
                        ((Condition.Or) condition).ands = separateSiblings(condition.conditionString, condition, otherSeparator);
                    }
                }
            });
        }
        return output;
    }

    private static void addToOutput(List<Condition> output, String conditionString, String separator) {
        if (separator.equals("&&")) {
            output.add(new Condition.And(conditionString));
        } else {
            output.add(new Condition.Or(conditionString));
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

    public static class Response {
        Boolean resolvedOutcome;
        Condition newQuestion;

        public Response(Condition newQuestion) {
            this.newQuestion = newQuestion;
        }

        public Response(boolean resolvedOutcome) {
            this.resolvedOutcome = resolvedOutcome;
        }
    }
}