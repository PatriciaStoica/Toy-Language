package Model.Statements.SemaphoreStatements;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Expressions.IExpression;
import Model.Statements.IStatement;
import Model.Types.IType;
import Model.Types.IntType;
import Model.Value.IValue;
import Model.Value.IntValue;
import Model.ProgramState.ProgramState;

import javafx.util.Pair;

import java.util.Vector;

public class NewSemaphoreStatement implements IStatement {
    private String variableName;
    private IExpression expression;

    public NewSemaphoreStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        try {
            IType varType = typeEnv.lookUp(variableName);
            if (varType == null)
                throw new InterpreterException(String.format("Variable '%s' is not declared!", variableName));
            if (!varType.equals(new IntType()))
                throw new InterpreterException(String.format("Variable '%s' is not int type!", variableName));
            if (!expression.typeCheck(typeEnv).equals(new IntType()))
                throw new InterpreterException(String.format("Expression '%s' is not evaluated as int!", expression.toString()));
        } catch (InterpreterException e) {
            throw new InterpreterException(e.getMessage());
        }
        return typeEnv;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        try {
            IValue expressionValue = expression.eval(state.getSymTable(), state.getHeap());
            if (!expressionValue.getType().equals(new IntType()))
                throw new InterpreterException(String.format("Expression '%s' is not evaluated as int!", expression.toString()));

            int expression_int = ((IntValue) expressionValue).getValue();
            ProgramState.semaphoreLock.lock();

            IValue variable_value = state.getSymTable().lookUp(variableName);
            if (variable_value == null)
                throw new InterpreterException(String.format("Variable '%s' has not been declared!", variableName));
            if (!variable_value.getType().equals(new IntType()))
                throw new InterpreterException(String.format("Variable '%s' should be int!", variableName));

            int new_location = state.getSemaphoreTable().put(new Pair<>(expression_int, new Vector<>()));
            state.getSymTable().put(variableName, new IntValue(new_location));
            ProgramState.semaphoreLock.unlock();
        } catch (InterpreterException e) {
            throw new InterpreterException(e.getMessage());
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewSemaphoreStatement(variableName, expression);
    }

    @Override
    public String toString() {
        return String.format("newSemaphore(%s, %s)", variableName, expression.toString());
    }
}