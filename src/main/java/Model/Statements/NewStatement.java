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

public class NewStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public NewStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (symTable.isDefined(varName)) {
            IValue varValue = symTable.lookUp(varName);
            if ((varValue.getType() instanceof RefType)) {
                IValue evaluated = expression.eval(symTable, heap);
                IType locationType = ((RefValue) varValue).getLocationType();
                if (locationType.equals(evaluated.getType())) {
                    int newPosition = heap.add(evaluated);
                    symTable.put(varName, new RefValue(newPosition, locationType));
                    state.setSymTable(symTable);
                    state.setHeap(heap);
                } else
                    throw new InterpreterException(String.format("%s not of %s", varName, evaluated.getType()));
            } else {
                throw new InterpreterException(String.format("%s in not of RefType", varName));
            }
        } else {
            throw new InterpreterException(String.format("%s not in symTable", varName));
        }
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException{
        IType typeVar = typeEnv.lookUp(varName);
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr)))
            return typeEnv;
        else
            throw new InterpreterException("NEW Statements: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("new(%s, %s)", varName, expression);
    }
}