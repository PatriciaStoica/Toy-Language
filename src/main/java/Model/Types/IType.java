package Model.Types;

import Model.Value.IValue;

public interface IType {
    boolean equals(IType anotherType);
    IValue defaultValue();
    IType deepCopy();
}