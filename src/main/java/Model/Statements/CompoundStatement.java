package Model.Statements;


import Exceptions.InterpreterException;
import Model.ProgramState.ProgramState;
import Model.Collections.IDictionary;
import Model.Collections.IStack;
import Model.Types.IType;

public class CompoundStatement implements IStatement {
    private final IStatement firstStatement;
    private final IStatement secondStatement;

    public CompoundStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    @Override
    public ProgramState execute(ProgramState state){
        IStack<IStatement> stack = state.getExeStack();
        stack.push(secondStatement);
        stack.push(firstStatement);
        state.setExeStack(stack);
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        return secondStatement.typeCheck(firstStatement.typeCheck(typeEnv));
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("(%s; %s)", firstStatement.toString(), secondStatement.toString());
    }
}