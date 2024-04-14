package Model.Expressions;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Types.IType;
import Model.Value.IValue;

public class VariableExpression implements IExpression {
    String key;

    public VariableExpression(String key) {
        this.key = key;
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        return typeEnv.lookUp(key);
    }

    @Override
    public IValue eval(IDictionary<String, IValue> table, IHeap heap) throws InterpreterException {
        return table.lookUp(key);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(key);
    }

    @Override
    public String toString() {
        return key;
    }
}