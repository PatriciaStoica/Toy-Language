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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFile implements IStatement {
    private final IExpression expression;

    public OpenReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            IDictionary<String, BufferedReader> fileTable = state.getFileTable();
            if (!fileTable.isDefined(fileName.getValue())) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(fileName.getValue()));
                } catch (FileNotFoundException e) {
                    throw new InterpreterException(String.format("%s could not be opened", fileName.getValue()));
                }
                fileTable.put(fileName.getValue(), br);
                state.setFileTable(fileTable);
            } else {
                throw new InterpreterException(String.format("%s is already opened", fileName.getValue()));
            }
        } else {
            throw new InterpreterException(String.format("%s does not evaluate to StringType", expression.toString()));
        }
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException{
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new InterpreterException("OpenReadFile requires a string Expressions.");
    }

    @Override
    public IStatement deepCopy() {
        return new OpenReadFile(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("OpenReadFile(%s)", expression.toString());
    }
}