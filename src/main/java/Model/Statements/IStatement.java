package Model.Statements;


import Exceptions.InterpreterException;
import Model.ProgramState.ProgramState;
import Model.Collections.IDictionary;
import Model.Types.IType;

public interface IStatement {
    ProgramState execute(ProgramState state) throws InterpreterException;
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException;
    IStatement deepCopy();
}