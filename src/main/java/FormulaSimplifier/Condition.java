package FormulaSimplifier;

import java.util.List;

public class Condition {
    private String conditionString;
    private Condition parent;
    private List<Condition> siblings;

    Condition getParent() {
        return parent;
    }

    void setParent(Condition parent) {
        this.parent = parent;
    }

    List<Condition> getSiblings() {
        return siblings;
    }

    void setSiblings(List<Condition> siblings) {
        this.siblings = siblings;
    }

    String getConditionString() {
        return conditionString;
    }

    void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }

    //TODO make this into a better representation of complexity
    int complexity(){
        int comp =  this.parent == null ? 1 : 0;
        if (!this.hasChildren()) return comp;
        comp++;
        comp += this.children().stream().mapToInt(condition -> {
                if (condition.hasChildren()) {
                 return condition.children().size();
                }
                return 0;
            }).sum();
        comp += this.children().stream().mapToInt(Condition::complexity).sum();
        return comp;
    }

    private List<Condition> children() {
           if (this instanceof Or) {return ((Or) this).ands;}
           else return ((And) this).ors;
    }

    private boolean hasChildren() {
       return this.children() != null;
    }

    Condition getDeepestDescendant() {
           Condition condition = this;
           while (condition.hasChildren()) {
               condition = condition.children().get(0);
           }
           return condition;
    }

    static class Or extends Condition {
        List<Condition> ands;

        Or(String conditionString) {
            setConditionString(conditionString);
        }
    }

    static class And extends Condition {
        List<Condition> ors;

        And(String conditionString) {
            setConditionString(conditionString);
        }
    }
}


