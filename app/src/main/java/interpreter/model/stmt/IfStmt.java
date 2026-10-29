package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException{
        IValue condValue = cond.eval(state.getSymTable());
        if(!(condValue instanceof BoolValue(boolean c)))
            throw new StatementException("condition is not a boolean");
        if(c){
            state.getExeStack().push(thenS);
        }
        else{
            state.getExeStack().push(elseS);
        }
        return state;
    }

    @Override
    public String toString() {
        return "(if " + cond + " then " + thenS + " else " + elseS + ")";
    }
}
