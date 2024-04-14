package Model.Statements;


import Exceptions.InterpreterException;
import Model.ProgramState.ProgramState;
import Model.Collections.IDictionary;
import Model.Types.IType;

public class NopStatement implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NopStatement();
    }

    @Override
    public String toString() {
        return "no operation";
    }
}