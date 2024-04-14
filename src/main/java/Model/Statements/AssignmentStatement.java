package Model.Statements;

import Exceptions.InterpreterException;
import Model.Expressions.IExpression;
import Model.ProgramState.ProgramState;
import Model.Collections.IDictionary;
import Model.Types.IType;
import Model.Value.IValue;

public class AssignmentStatement implements IStatement {
    private final String key;
    private final IExpression expression;

    public AssignmentStatement(String key, IExpression expression) {
        this.key = key;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {
        IDictionary<String, IValue> symbolTable = state.getSymTable();

        if (symbolTable.isDefined(key)) {
            IValue value = expression.eval(symbolTable, state.getHeap());
            IType typeId = (symbolTable.lookUp(key)).getType();
            if (value.getType().equals(typeId)) {
                symbolTable.update(key, value);
            } else {
                throw new InterpreterException("Declared Types of variable " + key + " and Types of the assigned Expressions do not match.");
            }
        } else {
            throw new InterpreterException("The used variable " + key + " was not declared before.");
        }
        state.setSymTable(symbolTable);
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType typeVar = typeEnv.lookUp(key);
        IType typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(typeExpr))
            return typeEnv;
        else
            throw new InterpreterException("Assignment: right hand side and left hand side have different types.");
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(key, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s = %s", key, expression.toString());
    }
}