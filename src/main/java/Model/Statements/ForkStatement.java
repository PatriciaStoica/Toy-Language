package Model.Statements;

import Exceptions.InterpreterException;
import Model.ProgramState.ProgramState;
import Model.Types.IType;
import Model.Collections.MyDictionary;
import Model.Collections.IDictionary;
import Model.Collections.IStack;
import Model.Collections.MyStack;
import Model.Value.IValue;

import java.util.Map;

public class ForkStatement implements IStatement {
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }
    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IStack<IStatement> newStack = new MyStack<>();
        newStack.push(statement);
        IDictionary<String, IValue> newSymTable = new MyDictionary<>();
        for (Map.Entry<String, IValue> entry: state.getSymTable().getContent().entrySet()) {
            newSymTable.put(entry.getKey(), entry.getValue().deepCopy());
        }

        return new ProgramState(newStack, newSymTable, state.getOut(), state.getFileTable(), state.getHeap(), state.getLockTable(), state.getSemaphoreTable());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("fork(%s)", statement.toString());
    }
}