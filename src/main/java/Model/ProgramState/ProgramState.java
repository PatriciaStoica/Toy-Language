package Model.ProgramState;

import Exceptions.InterpreterException;
import Model.Collections.*;
import Model.Statements.IStatement;
import Model.Value.IValue;

import java.io.BufferedReader;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProgramState {
    private IStack<IStatement> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<String, BufferedReader> fileTable;
    private IHeap heap;
    private ILockTable lockTable;
    private ISemaphoreTable semaphoreTable;
    static public Lock semaphoreLock = new ReentrantLock();
    private IStatement originalProgram;
    private int id;
    private static int lastId = 0;

    public ProgramState(IStack<IStatement> stack, IDictionary<String, IValue> symTable, IList<IValue> out,
                        IDictionary<String, BufferedReader> fileTable, IHeap heap,
                        ILockTable lockTable, ISemaphoreTable semaphoreTable, IStatement program) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.semaphoreTable = semaphoreTable;
        this.originalProgram = program.deepCopy();
        this.exeStack.push(this.originalProgram);
        this.id = setId();
    }

    public ProgramState(IStack<IStatement> stack, IDictionary<String, IValue> symTable, IList<IValue> out,
                        IDictionary<String, BufferedReader> fileTable, IHeap heap, ILockTable lockTable, ISemaphoreTable semaphoreTable) {
        this.exeStack = stack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.semaphoreTable = semaphoreTable;
        this.lockTable = lockTable;
        this.id = setId();
    }

    public synchronized int setId() {
        lastId++;
        return lastId;
    }

    public int getId() {
        return this.id;
    }

    public void setExeStack(IStack<IStatement> newStack) {
        this.exeStack = newStack;
    }
    public void setSymTable(IDictionary<String, IValue> newSymTable) {
        this.symTable = newSymTable;
    }
    public void setOut(IList<IValue> newOut) {
        this.out = newOut;
    }
    public void setFileTable(IDictionary<String, BufferedReader> newFileTable) {
        this.fileTable = newFileTable;
    }
    public void setHeap(IHeap newHeap) {
        this.heap = newHeap;
    }
    public void setLockTable(ILockTable newLockTable) {
        this.lockTable = newLockTable;
    }
    public void setSemaphoreTable(ISemaphoreTable semaphoreTable) {
        this.semaphoreTable = semaphoreTable;
    }

    public IStack<IStatement> getExeStack() {
        return exeStack;
    }
    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }
    public IList<IValue> getOut() {
        return out;
    }
    public IDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }
    public IHeap getHeap() {
        return heap;
    }
    public ILockTable getLockTable() {
        return lockTable;
    }
    public ISemaphoreTable getSemaphoreTable() {
        return semaphoreTable;
    }

    public boolean isNotCompleted() {
        return exeStack.isEmpty();
    }

    public ProgramState oneStep() throws InterpreterException {
        if (exeStack.isEmpty() )
            throw new InterpreterException("Program state stack is empty!");
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public String exeStackToString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        List<IStatement> stack = exeStack.getReversed();
        for (IStatement statement: stack) {
            exeStackStringBuilder.append(statement.toString()).append("\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symTableToString() throws InterpreterException {
        StringBuilder symTableStringBuilder = new StringBuilder();
        for (String key: symTable.keySet()) {
            symTableStringBuilder.append(String.format("%s -> %s\n", key, symTable.lookUp(key).toString()));
        }
        return symTableStringBuilder.toString();
    }

    public String outToString() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (IValue elem: out.getList()) {
            outStringBuilder.append(String.format("%s\n", elem.toString()));
        }
        return outStringBuilder.toString();
    }

    public String fileTableToString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String key: fileTable.keySet()) {
            fileTableStringBuilder.append(String.format("%s\n", key));
        }
        return fileTableStringBuilder.toString();
    }

    public String heapToString() throws InterpreterException {
        StringBuilder heapStringBuilder = new StringBuilder();
        for (int key: heap.keySet()) {
            heapStringBuilder.append(String.format("%d -> %s\n", key, heap.get(key)));
        }
        return heapStringBuilder.toString();
    }

    public String lockTableToString() throws InterpreterException {
        StringBuilder lockTableStringBuilder = new StringBuilder();
        for (int key: lockTable.keySet()) {
            lockTableStringBuilder.append(String.format("%d -> %d\n", key, lockTable.get(key)));
        }
        return lockTableStringBuilder.toString();
    }

    public String semaphoreTableToString() throws InterpreterException {
        StringBuilder semaphoreTableStringBuilder = new StringBuilder();
        for (int key: semaphoreTable.keySet()) {
            semaphoreTableStringBuilder.append(String.format("%d -> %d: %s\n", key, semaphoreTable.lookUp(key).getKey(), semaphoreTable.lookUp(key).getValue().toString()));
        }
        return semaphoreTableStringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Id: " + id + "\nExecution stack: \n" + exeStack.getReversed() + "\nSymbol table: \n" + symTable.toString() + "\nOutput list: \n" + out.toString() + "\nFile table:\n" + fileTable.toString() + "\nHeap memory:\n" + heap.toString() + "\nLock Table:\n" + lockTable.toString() + "\nLatch Table:\n" + semaphoreTable.toString() + "\n";
    }

    public String programStateToString() throws InterpreterException {
        return "Id: " + id + "\nExecution stack: \n" + exeStackToString() + "Symbol table: \n" + symTableToString() + "Output list: \n" + outToString() + "File table:\n" + fileTableToString() + "Heap memory:\n" + heapToString() + "Lock Table:\n" + lockTableToString() + "\nLatch Table:\n" + semaphoreTableToString();
    }
}