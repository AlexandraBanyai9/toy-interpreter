package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.value.IValue;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;


public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private final IStmt originalProgram;

    private IDictionary<StringValue, BufferedReader> fileTable;

    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out, IDictionary<StringValue,
                    BufferedReader> fileTable, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = program;
        exeStack.push(program);
    }

    @Override
    public String toString(){
        return "ExeStack: " + exeStack + "\n"
                + "SymTable: " + symTable + "\n"
                + "Out: " + out + "\n"
                + "FileTable: " + fileTable.toMap().keySet() + "\n";
    }

    // getters, setters

    public IStack<IStmt> getExeStack(){
        return exeStack;
    }
    public IDictionary<String, IValue> getSymTable(){
        return symTable;
    }

    public IList<IValue> getOut(){
        return out;
    }

    public IStmt getOriginalProgram(){
        return originalProgram;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() { return fileTable; }

    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) { this.fileTable = fileTable; }

    public void setOut(IList<IValue> out){
        this.out=out;
    }

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    //NEW
    public static PrgState of(IStmt program) {
        return new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(),
                new MyDictionary<>(), program);
    }
}
