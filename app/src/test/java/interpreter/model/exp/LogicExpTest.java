package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp(){
        symTable = new MyDictionary<>();
    }

    private static IExp bool(boolean b){
        return new ValueExp(new BoolValue(b));
    }

    @Test
    void andOfTrueAndFalseIsFalse() throws MyException{
        IExp e = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void orOfFalseAndFalseIsFalse() throws MyException {
        IExp e = new LogicExp(LogicOp.OR, bool(false), bool(false));
        assertEquals(new BoolValue(false), e.eval(symTable));
    }

    @Test
    void orOfTrueAndFalseIsTrue() throws MyException {
        IExp e = new LogicExp(LogicOp.OR, bool(true), bool(false));
        assertEquals(new BoolValue(true), e.eval(symTable));
    }

    @Test
    void nonBooleanFirstOperandThrows(){
        IExp e = new LogicExp(LogicOp.AND, new ValueExp(new IntValue(1)), bool(true));
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }

    @Test
    void nonBooleanSecondOperandThrows() {
        IExp e = new LogicExp(LogicOp.AND, bool(true), new ValueExp(new IntValue(1)));
        assertThrows(ExpressionException.class, () -> e.eval(symTable));
    }
}