package Model.Statements;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Collections.IStack;
import Model.Expressions.IExpression;
import Model.Expressions.NotExpression;
import Model.ProgramState.ProgramState;
import Model.Types.BoolType;
import Model.Types.IType;

public class RepeatUntilStatement implements IStatement {
    IStatement statement;
    IExpression expression;

    public RepeatUntilStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    public ProgramState execute(ProgramState state) throws InterpreterException {
        IStack<IStatement> exeStack = state.getExeStack();
        IStatement whileStatement = new CompoundStatement(statement, new WhileStatement(new NotExpression(expression), statement));
        exeStack.push(whileStatement);
        return null;
    }

    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType expressionType = expression.typeCheck(typeEnv);
        statement.typeCheck(typeEnv);
        if(expressionType.equals(new BoolType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new InterpreterException("The expression was not boolean");
        }
    }

    public IStatement deepCopy() {
        return new RepeatUntilStatement(statement.deepCopy(), expression.deepCopy());
    }

    public String toString() {
        return String.format("repeat(%s) until %s", statement.toString(), expression.toString());
    }
}
