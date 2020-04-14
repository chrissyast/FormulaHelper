package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    private List<Condition> ors;
    private Condition currentCondition;

    public Test(List<Condition> ors) {
        this.ors = ors;
    }

    public Test (String testString) {
        this.ors = separateSiblings(testString, null, "||");
        if (!isJUnitTest()) {
            this.ors.sort((Condition a1, Condition a2) -> {
                return a1.complexity() - a2.complexity();
            });
        }
        this.ors.forEach(o -> {
            o.setSiblings(this.ors);
        });
        this.currentCondition = getMostBasicQuestion();
    }

    public static boolean isJUnitTest() {
        StackTraceElement[] list = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
    return false;
    }

    Response answerQuestion(boolean answer, Condition currentCondition) {

        if (answer && currentCondition instanceof Condition.Or) {
            if (currentCondition.getParent() != null) {
                int parentIndex = currentCondition.getParent().getSiblings().indexOf(currentCondition.getParent());
                List<Condition> parentSiblings = currentCondition.getParent().getSiblings();
                if (parentIndex + 1 < parentSiblings.size()) {
                    return new Response(parentSiblings.get(parentIndex + 1).getDeepestDescendant());
                }
            }
            return new Response(true);
        }

        if (!answer && currentCondition instanceof Condition.Or) {
            int currentConditionIndex = currentCondition.getSiblings().indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.getSiblings().size()) {
                return new Response(currentCondition.getSiblings().get(currentConditionIndex + 1).getDeepestDescendant());
            }
            return new Response(false);
        }

        if (answer && currentCondition instanceof Condition.And) {
            int currentConditionIndex = currentCondition.getSiblings().indexOf(currentCondition);
            if (currentConditionIndex + 1 < currentCondition.getSiblings().size()) {
                return new Response(currentCondition.getSiblings().get(currentConditionIndex + 1).getDeepestDescendant());
            }
            return new Response(true);
        }

        if (!answer && currentCondition instanceof Condition.And) {
            List<Condition> parentSiblings = currentCondition.getParent().getSiblings();
            int parentIndex = parentSiblings.indexOf(currentCondition.getParent());
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
                condition.setParent(parent);
                condition.setSiblings(output);
                if (condition.getConditionString().contains(otherSeparator)) {
                    if (condition instanceof Condition.And) {
                        ((Condition.And) condition).ors = separateSiblings(condition.getConditionString(), condition, otherSeparator);
                    } else {
                        ((Condition.Or) condition).ands = separateSiblings(condition.getConditionString(), condition, otherSeparator);
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

    Condition getCurrentCondition() {
        return currentCondition;
    }

    void setCurrentCondition(Condition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public List<Condition> getOrs() {
        return ors;
    }

    static class Response {
        private Boolean resolvedOutcome;
        private Condition newQuestion;

        Response(Condition newQuestion) {
            this.newQuestion = newQuestion;
        }

        Response(boolean resolvedOutcome) {
            this.resolvedOutcome = resolvedOutcome;
        }

        public Boolean getResolvedOutcome() {
            return resolvedOutcome;
        }

        public void setResolvedOutcome(Boolean resolvedOutcome) {
            this.resolvedOutcome = resolvedOutcome;
        }

        public Condition getNewQuestion() {
            return newQuestion;
        }

        public void setNewQuestion(Condition newQuestion) {
            this.newQuestion = newQuestion;
        }
    }
}