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

public class IfStatement implements IStatement {
    IExpression expression;
    IStatement thenStatement;
    IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IStack<IStatement> stack = state.getExeStack();
        IValue result = this.expression.eval(state.getSymTable(), state.getHeap());
        if (result.getType().equals(new BoolType())){
            boolean condition = ((BoolValue) result).getValue();
            if(condition)
                stack.push(thenStatement);
            else
                stack.push(elseStatement);;
            state.setExeStack(stack);
        } else{
            throw new InterpreterException("The Expressions cannot be evaluated as true or false!");
        }
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            thenStatement.typeCheck(typeEnv.deepCopy());
            elseStatement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new InterpreterException("The condition of IF does not have the Types Bool.");
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(expression.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("if(%s){%s}else{%s}", expression.toString(), thenStatement.toString(), elseStatement.toString());
    }
}