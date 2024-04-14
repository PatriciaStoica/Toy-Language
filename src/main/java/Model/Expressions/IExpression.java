package Model.Expressions;


import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Types.IType;
import Model.Value.IValue;

public interface IExpression {
    IType typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException;
    IValue eval(IDictionary<String, IValue> table, IHeap heap) throws InterpreterException;
    IExpression deepCopy();
}