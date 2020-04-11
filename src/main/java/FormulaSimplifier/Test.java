package FormulaSimplifier;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Test {

    List<Or> ors;
    String testString;

    public Test(List<Or> ors) {
        this.ors = ors;
    }

    public Test (String testString) {
        this.testString = testString;
        this.ors = separateOrs(testString);
        this.ors.sort((Condition a1, Condition a2) -> {
            return a1.complexity() - a2.complexity();
        });
    }

    public String nextQuestion() {
        return getMostBasicQuestion();
    }

    private String getMostBasicQuestion() {
        return this.ors.get(0).conditionString;
    }

    private static List<Or> separateOrs(String testString) {
        List<Or> output = new ArrayList<>();
        int prevOrIndex = 0;
        String remainingString = testString;
        int numberOfOrs = StringUtils.countMatches(testString, "||");
        for (int orSequence = 1; orSequence <= numberOfOrs; orSequence++ ) {
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
            if (condition.conditionString.indexOf("&&") > -1) {
                condition.ands = separateAnds(condition.conditionString);
            }
        });
         return output;
    }

        private static List<And> separateAnds(String testString) {
        List<And> output = new ArrayList<>();
        int prevOrIndex = 0;
        String remainingString = testString;
        int numberOfOrs = StringUtils.countMatches(testString, "&&");
        for (int orSequence = 1; orSequence <= numberOfOrs; orSequence++ ) {
            int orInd = StringUtils.ordinalIndexOf(testString, "&&", orSequence);
            String segment = remainingString.substring(prevOrIndex, orInd);
            if (!isWithinParentheses(segment)) {
                output.add(new And(trimAndStripParentheses(segment)));
                prevOrIndex = orInd + 2;
            }
            if (orSequence == numberOfOrs) {
                output.add(new And(trimAndStripParentheses(remainingString.substring(prevOrIndex))));
            }
        }
        output.forEach(condition -> {
            if (condition.conditionString.indexOf("||") > -1) {
                condition.ors = separateOrs(condition.conditionString);
            }
        });
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

           public int complexity(){
               return this.conditionString.length();
           }

    }

    public static class Or extends Condition {
        private List<And> ands;


        public Or(String condition) {
            super();
            this.conditionString = condition;
        }

        public Or(List<And> ands) {
            this.ands = ands;
        }

    }

    public static class And extends Condition {
        private List<Or> ors;
        private String conditionString;

        public And(List<Or> ors) {
            this.ors = ors;
        }

        public And(String conditionString) {
            this.conditionString = conditionString;
        }
    }
}