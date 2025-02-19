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

public class UnlockStatement implements IStatement {
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public UnlockStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        lock.lock();
        IDictionary<String, IValue> symTable = state.getSymTable();
        ILockTable lockTable = state.getLockTable();
        if (symTable.isDefined(var)) {
            if (symTable.lookUp(var).getType().equals(new IntType())) {
                IntValue fi = (IntValue) symTable.lookUp(var);
                int foundIndex = fi.getValue();
                if (lockTable.containsKey(foundIndex)) {
                    if (lockTable.get(foundIndex) == state.getId())
                        lockTable.update(foundIndex, -1);
                } else {
                    throw new InterpreterException("Index not in the lock table!");
                }
            } else {
                throw new InterpreterException("Var is not of int Types!");
            }
        } else {
            throw new InterpreterException("Variable is not defined!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        if (typeEnv.lookUp(var).equals(new IntType()))
            return typeEnv;
        else
            throw new InterpreterException("Var is not of Types int!");
    }

    @Override
    public IStatement deepCopy() {
        return new UnlockStatement(var);
    }

    @Override
    public String toString() {
        return String.format("unlock(%s)", var);
    }
}