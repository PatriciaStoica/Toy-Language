package Model.Types;

import Model.Value.IntValue;
import Model.Value.IValue;

public class IntType implements IType {
    @Override
    public boolean equals(IType anotherType) {
        return anotherType instanceof IntType;
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }

    @Override
    public IType deepCopy() {
        return new IntType();
    }

    @Override
    public String toString() {
        return "int";
    }
}