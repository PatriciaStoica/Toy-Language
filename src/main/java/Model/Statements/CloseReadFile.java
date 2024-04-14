package Model.Statements;


import Exceptions.InterpreterException;

import Model.Expressions.IExpression;
import Model.ProgramState.ProgramState;
import Model.Types.IType;
import Model.Types.StringType;
import Model.Collections.IDictionary;
import Model.Value.StringValue;
import Model.Value.IValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements IStatement {
    private final IExpression expression;

    public CloseReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            IDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if (fileTable.isDefined(fileName.getValue())) {
                BufferedReader br = fileTable.lookUp(fileName.getValue());
                try {
                    br.close();
                } catch (IOException e) {
                    throw new InterpreterException(String.format("Unexpected error in closing %s", value));
                }
                fileTable.remove(fileName.getValue());
                state.setFileTable(fileTable);
            } else
                throw new InterpreterException(String.format("%s is not present in the FileTable", value));
        } else
            throw new InterpreterException(String.format("%s does not evaluate to StringValue", expression));
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new InterpreterException("CloseReadFile requires a string Expressions.");
    }

    @Override
    public IStatement deepCopy() {
        return new CloseReadFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("CloseReadFile(%s)", expression.toString());
    }
}