package Model.Statements;

import Exceptions.InterpreterException;
import Model.Expressions.IExpression;
import Model.ProgramState.ProgramState;
import Model.Types.IType;
import Model.Types.RefType;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Value.RefValue;
import Model.Value.IValue;

public class WriteHeapStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public WriteHeapStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (symTable.isDefined(varName)) {
            IValue value = symTable.lookUp(varName);
            if (value.getType() instanceof RefType) {
                RefValue refValue = (RefValue) value;
                if (heap.containsKey(refValue.getAddress())) {
                    IValue evaluated = expression.eval(symTable, heap);
                    if (evaluated.getType().equals(refValue.getLocationType())) {
                        heap.update(refValue.getAddress(), evaluated);
                        state.setHeap(heap);
                    } else
                        throw new InterpreterException(String.format("%s not of %s", evaluated, refValue.getLocationType()));
                } else
                    throw new InterpreterException(String.format("The RefValue %s is not defined in the heap", value));
            } else
                throw new InterpreterException(String.format("%s not of RefType", value));
        } else
            throw new InterpreterException(String.format("%s not present in the symTable", varName));
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        if (typeEnv.lookUp(varName).equals(new RefType(expression.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new InterpreterException("WriteHeap: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("WriteHeap(%s, %s)", varName, expression);
    }
}