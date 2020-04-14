package FormulaSimplifier;

public enum Verification {
    QUESTION_COLON("?","question mark", "question marks",":", "colon", "colons",true),
    PARENTHESES("(","opening parenthesis","opening parentheses",")","closing parenthesis","closing parentheses", false);

    public final String opener;
    public final String openerName;
    public final String openerNamePlural;
    public final String closer;
    public final String closerName;
    public final String closerNamePlural;
    public final Boolean mandatory;


    Verification(String opener, String openerName, String openerNamePlural, String closer, String closerName, String closerNamePlural, Boolean mandatory) {
        this.opener = opener;
        this.openerName = openerName;
        this.openerNamePlural = openerNamePlural;
        this.closer = closer;
        this.closerName = closerName;
        this.closerNamePlural = closerNamePlural;
        this.mandatory = mandatory;
    }

}