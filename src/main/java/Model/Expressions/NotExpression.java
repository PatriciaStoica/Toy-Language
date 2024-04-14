package Model.Expressions;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Types.BoolType;
import Model.Types.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;

public class NotExpression implements IExpression {
    IExpression expression;

    public NotExpression(IExpression expression) {
        this.expression = expression;
    }

    public IType typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType t1;
        t1 = expression.typeCheck(typeEnv);
        if(t1.equals(new BoolType())) {
            return new BoolType();
        } else {
            throw new InterpreterException("First operand is not boolean");
        }
    }

    public IValue eval(IDictionary<String, IValue> table, IHeap heap) throws InterpreterException {
        IValue v1;
        v1 = expression.eval(table, heap);
        if(v1.getType().equals(new BoolType())) {
            BoolValue b1 = (BoolValue) v1;
            boolean firstBool = b1.getValue();
            return new BoolValue(!firstBool);
        } else {
            throw new InterpreterException("First operand must be boolean");
        }
    }

    public IExpression deepCopy() {
        return new NotExpression(expression.deepCopy());
    }

    public String toString() {
        return "!" + expression.toString();
    }
}
