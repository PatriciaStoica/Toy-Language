package Model.Statements;


import Exceptions.InterpreterException;
import Model.ProgramState.ProgramState;
import Model.Collections.IDictionary;
import Model.Types.IType;
import Model.Value.IValue;

public class DeclarationStatement implements IStatement {
    String name;
    IType type;

    public DeclarationStatement(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        if (symTable.isDefined(name)) {
            throw new InterpreterException("Variable " + name + " already exists in the symTable.");
        }
        symTable.put(name, type.defaultValue());
        state.setSymTable(symTable);
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        typeEnv.put(name, type);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new DeclarationStatement(name, type);
    }

    @Override
    public String toString() {
        return String.format("%s %s", type.toString(), name);
    }
}