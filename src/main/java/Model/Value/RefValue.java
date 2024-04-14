package Model.Value;

import Model.Types.RefType;
import Model.Types.IType;

public class RefValue implements IValue {
    private final int address;
    private final IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }
    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    public int getAddress() {
        return address;
    }

    public IType getLocationType() {
        return locationType;
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(address, locationType.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("ref(%d, %s)", address, locationType);
    }
}