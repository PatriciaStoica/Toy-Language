package Model.Statements;


import Exceptions.InterpreterException;
import Model.Expressions.IExpression;
import Model.ProgramState.ProgramState;
import Model.Types.BoolType;
import Model.Collections.IDictionary;
import Model.Collections.IStack;
import Model.Types.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;

public class WhileStatement implements IStatement {
    private final IExpression expression;
    private final IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        IStack<IStatement> stack = state.getExeStack();
        if (!value.getType().equals(new BoolType()))
            throw new InterpreterException(String.format("%s is not of BoolType", value));
        if (!(value instanceof BoolValue))
            throw new InterpreterException(String.format("%s is not a BoolValue", value));
        BoolValue boolValue = (BoolValue) value;
        if (boolValue.getValue()) {
            stack.push(this.deepCopy());
            stack.push(statement);
        }
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new InterpreterException("The condition of WHILE does not have the Types Bool.");
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("while(%s){%s}", expression, statement);
    }
}