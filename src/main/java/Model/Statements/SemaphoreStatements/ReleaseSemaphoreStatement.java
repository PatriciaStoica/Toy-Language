package Model.Statements.SemaphoreStatements;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.ProgramState.ProgramState;
import Model.Statements.IStatement;
import Model.Types.IntType;
import Model.Types.IType;
import Model.Value.IntValue;
import Model.Value.IValue;
import javafx.util.Pair;

import java.util.List;

public class ReleaseSemaphoreStatement implements IStatement {
    private String varName;

    public ReleaseSemaphoreStatement(String varName) {
        this.varName = varName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        try {
            IValue variableValue = state.getSymTable().lookUp(varName);
            if (variableValue == null)
                throw new InterpreterException(String.format("Variable '%s' is not declared!", varName));
            if (!variableValue.getType().equals(new IntType()))
                throw new InterpreterException(String.format("Variable '%s' should be int!", varName));

            int semLocation = ((IntValue) variableValue).getValue();
            ProgramState.semaphoreLock.lock();
            Pair<Integer, List<Integer>> semaphoreEntry = state.getSemaphoreTable().lookUp(semLocation);
            if (semaphoreEntry == null) {
                ProgramState.semaphoreLock.unlock();
                throw new InterpreterException("Invalid semaphore location!");
            }

            //Integer semaphore = semaphoreEntry.getKey();
            List<Integer> acquiredPrograms = semaphoreEntry.getValue();

            Integer programId = state.getId();
            if (acquiredPrograms.contains(programId))
                acquiredPrograms.remove(programId);

            ProgramState.semaphoreLock.unlock();
            return null;
        } catch (InterpreterException e) {
            throw new InterpreterException(e.getMessage());
        }
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        try {
            IType var_type = typeEnv.lookUp(varName);
            if (var_type == null)
                throw new InterpreterException(String.format("Variable '%s' has not been defined!", varName));
            if (!var_type.equals(new IntType()))
                throw new InterpreterException(String.format("Variable '%s' should have integer type!", varName));
        } catch (InterpreterException e) {
            throw new InterpreterException(e.getMessage());
        }
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReleaseSemaphoreStatement(varName);
    }

    @Override
    public String toString() {
        return String.format("releaseSemaphore(%s)", varName);
    }
}