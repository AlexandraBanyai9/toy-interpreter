package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;

import java.util.Scanner;

public class Menu {
    private static final String[] DESCRIPTIONS = {
            "int v; v = 2; print(v)",
            "int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)",
            "bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)"
    };
    private static final IStmt[] PROGRAMS = {
            Examples.EX1, Examples.EX2, Examples.EX3
    };

    private final Scanner scanner = new Scanner(System.in);

    private int readInt(int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                int op = scanner.nextInt();
                if (op >= min && op <= max)
                    return op;
            } else {
                scanner.next();
            }
            System.out.println("Invalid option, try again.");
        }
    }

    private void run(IStmt program) {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), program);
        Controller controller = new Controller(new Repository(state), true);
        try {
            controller.allStep();
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void menu() {
        int selected = -1;

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Select a program");
            System.out.println("2. Run the selected program");
            System.out.println("0. Exit");
            if (selected != -1)
                System.out.println("(selected: Ex" + (selected + 1) + ")");

            int op = readInt(0, 2);

            if (op == 0) {
                System.out.println("Exiting...");
                return;
            }
            if (op == 1) {
                System.out.println("\nAvailable programs:");
                for (int i = 0; i < DESCRIPTIONS.length; i++)
                    System.out.println("  " + (i + 1) + ". " + DESCRIPTIONS[i]);
                selected = readInt(1, PROGRAMS.length) - 1;
            } else {
                if (selected == -1) {
                    System.out.println("You must select a program first.");
                } else {
                    System.out.println("\n>>> Running Ex" + (selected + 1) + ": "
                            + DESCRIPTIONS[selected] + "\n");
                    run(PROGRAMS[selected]);
                    selected = -1;
                }
            }
        }
    }
}