package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    //A3
    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException { // fără static
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    //A3
    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(List.of(new IntValue(15), new IntValue(50)), s.getOut().toList());
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithEmptyFilePrintsZeroZero() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(List.of(new IntValue(0), new IntValue(0)), s.getOut().toList());
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithMissingFileThrowsStatementException() {
        String missing = dir.resolve("does-not-exist.in").toString();
        assertThrows(StatementException.class,
                () -> run(Examples.fileExample(missing)));
    }

    @Test
    void relationalExampleEx5PrintsFive() throws Exception {
        PrgState s = run(Examples.EX5);
        assertEquals(List.of(new IntValue(5)), s.getOut().toList());
    }

    @Test
    void logFileContainsTheFiveSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:"),
                e = log.indexOf("Heap:");
        assertTrue(0 <= a && a < b && b < c && c < d && d < e);
    }

    @Test
    void logFileIsWrittenAfterEveryStep() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        // initial state + one state after each executed step: at least 2 blocks
        assertTrue(log.split("ExeStack:", -1).length - 1 >= 2);
    }



    // A4

    @Test
    void example6PrintsTheTwoReferences() throws Exception {
        PrgState s = run(Examples.EX6);
        assertEquals(new RefValue(1, new IntType()), s.getOut().get(0));
        assertEquals(new RefValue(2, new RefType(new IntType())), s.getOut().get(1));
        assertEquals(new IntValue(20), s.getHeap().get(1));
        assertEquals(new RefValue(1, new IntType()), s.getHeap().get(2));
    }

    @Test
    void example7PrintsTwentyAndTwentyFive() throws Exception {
        PrgState s = run(Examples.EX7);
        assertEquals(new IntValue(20), s.getOut().get(0));
        assertEquals(new IntValue(25), s.getOut().get(1));
    }

    @Test
    void example8PrintsTwentyAndThirtyFive() throws Exception {
        PrgState s = run(Examples.EX8);
        assertEquals(new IntValue(20), s.getOut().get(0));
        assertEquals(new IntValue(35), s.getOut().get(1));
        assertEquals(new IntValue(30), s.getHeap().get(1));
    }

    @Test
    void example9PrintsTwentyWithTheSafeGarbageCollector() throws Exception {
        PrgState s = run(Examples.EX9);
        assertEquals(new IntValue(20), s.getOut().get(0));
    }

    @Test
    void example10CountsDownFromFourToZero() throws Exception {
        PrgState s = run(Examples.EX10);
        assertEquals(new IntValue(4), s.getOut().get(0));
        assertEquals(new IntValue(3), s.getOut().get(1));
        assertEquals(new IntValue(2), s.getOut().get(2));
        assertEquals(new IntValue(1), s.getOut().get(3));
        assertEquals(new IntValue(0), s.getOut().get(4));
    }
}