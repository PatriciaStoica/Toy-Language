package Model.Expressions;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Types.IType;
import Model.Value.IValue;

public class ValueExpression implements IExpression {
    IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        return value.getType();
    }

    @Override
    public IValue eval(IDictionary<String, IValue> table, IHeap heap) {
        return this.value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}