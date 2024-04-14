package Model.Statements;

import Exceptions.InterpreterException;
import Model.ProgramState.ProgramState;
import Model.Types.IType;
import Model.Types.IntType;
import Model.Collections.IDictionary;
import Model.Collections.ILockTable;
import Model.Value.IntValue;
import Model.Value.IValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLockStatement implements IStatement {
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public NewLockStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        lock.lock();
        ILockTable lockTable = state.getLockTable();
        IDictionary<String, IValue> symTable = state.getSymTable();
        int freeAddress = lockTable.getFreeValue();
        lockTable.put(freeAddress, -1);
        if (symTable.isDefined(var) && symTable.lookUp(var).getType().equals(new IntType())) {
            symTable.update(var, new IntValue(freeAddress));
        }
        else
            throw new InterpreterException("Variable not declared!");
        lock.unlock();
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        if (typeEnv.lookUp(var).equals(new IntType()))
            return typeEnv;
        else
            throw new InterpreterException("Var is not of int Types!");
    }

    @Override
    public IStatement deepCopy() {
        return new NewLockStatement(var);
    }

    @Override
    public String toString() {
        return String.format("newLock(%s)", var);
    }
}