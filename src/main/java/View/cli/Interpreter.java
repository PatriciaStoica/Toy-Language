package View.cli;

import Controller.Controller;
import Exceptions.InterpreterException;
import Model.Collections.*;
import Model.Expressions.*;
import Model.ProgramState.ProgramState;
import Model.Statements.*;
import Model.Statements.SemaphoreStatements.AcquireSemaphoreStatement;
import Model.Statements.SemaphoreStatements.NewSemaphoreStatement;
import Model.Statements.SemaphoreStatements.ReleaseSemaphoreStatement;
import Model.Types.BoolType;
import Model.Types.IntType;
import Model.Types.RefType;
import Model.Types.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Repository.IRepository;
import Repository.Repository;

import java.io.IOException;

public class Interpreter {
    public static void main(String[] args) throws InterpreterException {
        TextMenu menu = new TextMenu();

        menu.addCommand(new ExitCommand("0", "exit"));

        IStatement ex1 = new CompoundStatement(new DeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        try {
            ex1.typeCheck(new MyDictionary<>());
            ProgramState prg1 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex1);
            IRepository repo1 = new Repository(prg1, "log1.txt");
            Controller controller1 = new Controller(repo1);
            menu.addCommand(new RunExampleCommand("1", ex1.toString(), controller1));
        }  catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex2 = new CompoundStatement(new DeclarationStatement("a",new IntType()),
                new CompoundStatement(new DeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),new
                                ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignmentStatement("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new
                                        IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));
        try {
            ex2.typeCheck(new MyDictionary<>());
            ProgramState prg2 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex2);
            IRepository repo2 = new Repository(prg2, "log2.txt");
            Controller controller2 = new Controller(repo2);
            menu.addCommand(new RunExampleCommand("2", ex2.toString(), controller2));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex3 = new CompoundStatement(new DeclarationStatement("a", new BoolType()),
                new CompoundStatement(new DeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
        try {
            ex3.typeCheck(new MyDictionary<>());
            ProgramState prg3 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex3);
            IRepository repo3 = new Repository(prg3, "log3.txt");
            Controller controller3 = new Controller(repo3);
            menu.addCommand(new RunExampleCommand("3", ex3.toString(), controller3));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex4 = new CompoundStatement(new DeclarationStatement("varf", new StringType()),
                new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("test.txt"))),
                        new CompoundStatement(new OpenReadFile(new VariableExpression("varf")),
                                new CompoundStatement(new DeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFile(new VariableExpression("varf"))))))))));

        try {
            ex4.typeCheck(new MyDictionary<>());
            ProgramState prg4 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex4);
            IRepository repo4 = new Repository(prg4, "log4.txt");
            Controller controller4 = new Controller(repo4);
            menu.addCommand(new RunExampleCommand("4", ex4.toString(), controller4));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex5 = new CompoundStatement(new DeclarationStatement("a", new IntType()),
                new CompoundStatement(new DeclarationStatement("b", new IntType()),
                        new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new IntValue(5))),
                                new CompoundStatement(new AssignmentStatement("b", new ValueExpression(new IntValue(7))),
                                        new IfStatement(new RelationalExpression(">", new VariableExpression("a"),
                                                new VariableExpression("b")),new PrintStatement(new VariableExpression("a")),
                                                new PrintStatement(new VariableExpression("b")))))));

        try {
            ex5.typeCheck(new MyDictionary<>());
            ProgramState prg5 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex5);
            IRepository repo5 = new Repository(prg5, "log5.txt");
            Controller controller5 = new Controller(repo5);
            menu.addCommand(new RunExampleCommand("5", ex5.toString(), controller5));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex6 = new CompoundStatement(new DeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignmentStatement("v",new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));

        try {
            ex6.typeCheck(new MyDictionary<>());
            ProgramState prg6 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex6);
            IRepository repo6 = new Repository(prg6, "log6.txt");
            Controller controller6 = new Controller(repo6);
            menu.addCommand(new RunExampleCommand("6", ex6.toString(), controller6));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex7 = new CompoundStatement(new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new DeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new VariableExpression("a")))))));
        try {
            ex7.typeCheck(new MyDictionary<>());
            ProgramState prg7 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex7);
            IRepository repo7 = new Repository(prg7, "log7.txt");
            Controller controller7 = new Controller(repo7);
            menu.addCommand(new RunExampleCommand("7", ex7.toString(), controller7));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex8 = new CompoundStatement(new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new DeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression('+',new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)))))))));
        try {
            ex8.typeCheck(new MyDictionary<>());
            ProgramState prg8 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex8);
            IRepository repo8 = new Repository(prg8, "log8.txt");
            Controller controller8 = new Controller(repo8);
            menu.addCommand(new RunExampleCommand("8", ex8.toString(), controller8));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex9 = new CompoundStatement(new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(new WriteHeapStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(new ArithmeticExpression('+', new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5))))))));
        try {
            ex9.typeCheck(new MyDictionary<>());
            ProgramState prg9 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex9);
            IRepository repo9 = new Repository(prg9, "log9.txt");
            Controller controller9 = new Controller(repo9);
            menu.addCommand(new RunExampleCommand("9", ex9.toString(), controller9));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex10 = new CompoundStatement(new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new DeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));

        try {
            ex10.typeCheck(new MyDictionary<>());
            ProgramState prg10 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex10);
            IRepository repo10 = new Repository(prg10, "log10.txt");
            Controller controller10 = new Controller(repo10);
            menu.addCommand(new RunExampleCommand("10", ex10.toString(), controller10));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        IStatement ex11 = new CompoundStatement(new DeclarationStatement("v", new IntType()),
                new CompoundStatement(new DeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("a", new ValueExpression(new IntValue(30))),
                                                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));

        try {
            ex11.typeCheck(new MyDictionary<>());
            ProgramState prg11 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex11);
            IRepository repo11 = new Repository(prg11, "log11.txt");
            Controller controller11 = new Controller(repo11);
            menu.addCommand(new RunExampleCommand("11", ex11.toString(), controller11));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        //Switch statement
        //int a; int b; int c;
        //a=1;b=2;c=5;
        //(switch(a*10)
        // (case (b*c) : print(a);print(b))
        // (case (10) : print(100);print(200))
        // (default : print(300)));
        //print(300)
        //The final Out should be {1,2,300}
        IStatement ex12 = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new DeclarationStatement("b", new IntType()),
                        new CompoundStatement(
                                new DeclarationStatement("c", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ValueExpression(new IntValue(1))),
                                        new CompoundStatement(
                                                new AssignmentStatement("b", new ValueExpression(new IntValue(2))),
                                                new CompoundStatement(
                                                        new AssignmentStatement("c", new ValueExpression(new IntValue(5))),
                                                        new CompoundStatement(
                                                                new SwitchStatement(
                                                                        new ArithmeticExpression('*', new VariableExpression("a"), new ValueExpression(new IntValue(10))),
                                                                        new ArithmeticExpression('*', new VariableExpression("b"), new VariableExpression("c")),
                                                                        new ValueExpression(new IntValue(10)),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("a")),
                                                                                new PrintStatement(new VariableExpression("b"))
                                                                        ),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new ValueExpression(new IntValue(100))),
                                                                                new PrintStatement(new ValueExpression(new IntValue(200)))
                                                                        ),
                                                                        new PrintStatement(new ValueExpression(new IntValue(300)))
                                                                ),
                                                                new PrintStatement(new ValueExpression(new IntValue(300)))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        try {
            ex12.typeCheck(new MyDictionary<>());
            ProgramState prg12 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex12);
            IRepository repo12 = new Repository(prg12, "log12.txt");
            Controller controller12 = new Controller(repo12);
            menu.addCommand(new RunExampleCommand("12", ex12.toString(), controller12));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        //Semaphore statement
        //Ref int v1; int cnt;
        //new(v1,1);createSemaphore(cnt,rH(v1));
        //fork(acquire(cnt);wh(v1,rh(v1)*10));print(rh(v1));release(cnt));
        //fork(acquire(cnt);wh(v1,rh(v1)*10));wh(v1,rh(v1)*2));print(rh(v1));release(cnt
        //));
        //acquire(cnt);
        //print(rh(v1)-1);
        //release(cnt)
        //The final Out should be {10,200,9} or {10,9,200}.
        IStatement ex13 =
                new CompoundStatement(
                        new DeclarationStatement("v1", new RefType(new IntType())),
                        new CompoundStatement(
                                new DeclarationStatement("cnt", new IntType()),
                                new CompoundStatement(
                                        new NewStatement("v1", new ValueExpression(new IntValue(1))),
                                        new CompoundStatement(
                                                new NewSemaphoreStatement("cnt", new ReadHeapExpression(new VariableExpression("v1"))),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new AcquireSemaphoreStatement("cnt"),
                                                                        new CompoundStatement(
                                                                                new WriteHeapStatement("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                        new ReleaseSemaphoreStatement("cnt")
                                                                                )
                                                                        )

                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new ForkStatement(
                                                                        new CompoundStatement(
                                                                                new AcquireSemaphoreStatement("cnt"),
                                                                                new CompoundStatement(
                                                                                        new WriteHeapStatement("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                        new CompoundStatement(
                                                                                                new WriteHeapStatement("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)))),
                                                                                                new CompoundStatement(
                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                        new ReleaseSemaphoreStatement("cnt")
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )

                                                                ),
                                                                new CompoundStatement(
                                                                        new AcquireSemaphoreStatement("cnt"),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                                                                new ReleaseSemaphoreStatement("cnt")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
        try {
            ex13.typeCheck(new MyDictionary<>());
            ProgramState prg13 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), new MyLockTable(), new MySemaphoreTable(), ex13);
            IRepository repo13 = new Repository(prg13, "log13.txt");
            Controller controller13 = new Controller(repo13);
            menu.addCommand(new RunExampleCommand("13", ex13.toString(), controller13));
        } catch (IOException | InterpreterException e) {
            System.out.println(e.getMessage());
        }

        menu.show();
    }
}