package Model.Statements;

import Exceptions.InterpreterException;
import Model.Collections.IList;
import Model.Expressions.IExpression;
import Model.ProgramState.ProgramState;
import Model.Collections.IDictionary;
import Model.Types.IType;
import Model.Value.IValue;

public class PrintStatement implements IStatement {
    IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IList<IValue> out = state.getOut();
        out.add(expression.eval(state.getSymTable(), state.getHeap()));
        state.setOut(out);
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("print(%s)", expression.toString());
    }
}