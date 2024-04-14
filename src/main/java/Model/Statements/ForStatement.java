package Model.Statements;

import Exceptions.InterpreterException;
import Model.Expressions.IExpression;
import Model.Expressions.RelationalExpression;
import Model.Expressions.VariableExpression;
import Model.ProgramState.ProgramState;
import Model.Types.IType;
import Model.Types.IntType;
import Model.Collections.IDictionary;
import Model.Collections.IStack;

public class ForStatement implements IStatement {
    private final String variable;
    private final IExpression expression1;
    private final IExpression expression2;
    private final IExpression expression3;
    private final IStatement statement;

    public ForStatement(String variable, IExpression expression1, IExpression expression2, IExpression expression3, IStatement statement) {
        this.variable = variable;
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IStack<IStatement> exeStack = state.getExeStack();
        IStatement toWhile = new CompoundStatement(new AssignmentStatement("v", expression1),
                new WhileStatement(new RelationalExpression("<", new VariableExpression("v"), expression2),
                        new CompoundStatement(statement, new AssignmentStatement("v", expression3))));
        exeStack.push(toWhile);
        state.setExeStack(exeStack);
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType type1 = expression1.typeCheck(typeEnv);
        IType type2 = expression2.typeCheck(typeEnv);
        IType type3 = expression3.typeCheck(typeEnv);

        if (type1.equals(new IntType()) && type2.equals(new IntType()) && type3.equals(new IntType()))
            return typeEnv;
        else
            throw new InterpreterException("The for Statements is invalid!");
    }

    @Override
    public IStatement deepCopy() {
        return new ForStatement(variable, expression1.deepCopy(), expression2.deepCopy(), expression3.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("for(%s=%s; %s<%s; %s=%s) {%s}", variable, expression1, variable, expression2, variable, expression3, statement);
    }
}