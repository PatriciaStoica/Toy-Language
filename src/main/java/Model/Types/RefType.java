package Model.Types;

import Model.Value.RefValue;
import Model.Value.IValue;

public class RefType implements IType {
    private final IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(IType anotherType) {
        if (anotherType instanceof RefType)
            return inner.equals(((RefType) anotherType).getInner());
        else
            return false;
    }

    @Override
    public IValue defaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("Ref(%s)", inner);
    }
}