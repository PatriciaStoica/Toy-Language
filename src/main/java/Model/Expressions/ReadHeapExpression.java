package Model.Expressions;

import Exceptions.InterpreterException;
import Model.Types.IType;
import Model.Types.RefType;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Value.RefValue;
import Model.Value.IValue;

public class ReadHeapExpression implements IExpression {
    private final IExpression expression;

    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType type = expression.typeCheck(typeEnv);
        if (type instanceof RefType) {
            RefType refType = (RefType) type;
            return refType.getInner();
        } else
            throw new InterpreterException("The rH argument is not a RefType.");
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws InterpreterException {
        IValue value = expression.eval(symTable, heap);
        if (value instanceof RefValue) {
            RefValue refValue = (RefValue) value;
            if (heap.containsKey(refValue.getAddress()))
                return heap.get(refValue.getAddress());
            else
                throw new InterpreterException("The address is not defined on the heap!");
        } else
            throw new InterpreterException(String.format("%s not of RefType", value));
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("ReadHeap(%s)", expression);
    }
}