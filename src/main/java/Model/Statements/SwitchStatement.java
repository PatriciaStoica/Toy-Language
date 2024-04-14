package Model.Statements;

import Exceptions.InterpreterException;
import Model.Collections.IDictionary;
import Model.Collections.IStack;
import Model.Expressions.IExpression;
import Model.Expressions.RelationalExpression;
import Model.ProgramState.ProgramState;
import Model.Types.IType;

public class SwitchStatement implements IStatement{
    IExpression exp;
    IExpression exp1;
    IExpression exp2;
    IStatement stmt;
    IStatement stmt1;
    IStatement stmt2;

    public SwitchStatement(IExpression exp, IExpression exp1, IExpression exp2, IStatement stmt, IStatement stmt1, IStatement stmt2) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt = stmt;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws InterpreterException {
        IType expressionType = exp.typeCheck(typeEnv);
        IType expressionType1 = exp1.typeCheck(typeEnv);
        IType expressionType2 = exp2.typeCheck(typeEnv);

        if (expressionType.equals(expressionType1) && expressionType2.equals(expressionType1)) {
            stmt.typeCheck(typeEnv.deepCopy());
            stmt1.typeCheck(typeEnv.deepCopy());
            stmt2.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new InterpreterException("The expressions of switch does not have the same time");
        }
    }

    public ProgramState execute(ProgramState state) throws InterpreterException {
        IStack<IStatement> exeStack = state.getExeStack();
        IStatement newStmt = new IfStatement(
                new RelationalExpression("==", exp, exp1),
                stmt,
                new IfStatement(
                        new RelationalExpression("==", exp, exp2),
                        stmt1,
                        stmt2
                )
        );
        exeStack.push(newStmt);
        state.setExeStack(exeStack);
        return null;
    }

    public IStatement deepCopy() {
        return new SwitchStatement(exp.deepCopy(), exp1.deepCopy(), exp2.deepCopy(), stmt.deepCopy(), stmt1.deepCopy(), stmt2.deepCopy());
    }

    public String toString() {
        return "\n(switch(" + exp.toString() + ")\n (case(" + exp1.toString()
                + "): " + stmt.toString() + ")\n (case (" + exp2.toString() + "): " +
                stmt1.toString() + ")\n (default: " + stmt2.toString() + "));\n";
    }
}
