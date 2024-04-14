package Model.Value;

import Model.Types.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}