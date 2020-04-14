package FormulaSimplifier;

import java.util.List;

public class Condition {
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

    public static class Or extends Condition {
        List<Condition> ands;

        public Or(String conditionString) {
            this.conditionString = conditionString;
        }

        public Or(List<Condition> ands) {
            this.ands = ands;
        }

    }

    public static class And extends Condition {
        List<Condition> ors;

        public And(List<Condition> ors) {
            this.ors = ors;
        }

        public And(String conditionString) {
            this.conditionString = conditionString;
        }
    }
}


