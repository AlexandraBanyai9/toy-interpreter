package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public interface IRepository {
    PrgState getCrtPrg();

    //A3
    void logPrgStateExec() throws MyException;
}
