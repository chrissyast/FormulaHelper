package FormulaSimplifier;

import static org.junit.jupiter.api.Assertions.*;

class ConditionTest {

    @org.junit.jupiter.api.Test
    void complexity() {
        // A test with two children, one of which also has a test with two children
        Test test = new Test("foorab || (foo && (bar || rab))");
        assertEquals (5, test.getOrs().get(1).complexity());
        // A test with two children, one of which has a test with three children
        test = new Test("foorab || (foo && (bar || rab || bra))");
        assertEquals (6, test.getOrs().get(1).complexity());
        // A test with two children, both of which have tests with two children
        test = new Test("foorab || ((foo || oof) && (bar || bra))");
        assertEquals (7, test.getOrs().get(1).complexity());
    }
}