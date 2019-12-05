package main.java.types.references;

import main.java.types.IType;

public class TRef implements IType {

	
	private IType referenceType;
	
	
	
	public TRef(IType referenceType) {
		this.referenceType = referenceType;
	}
	
	@Override
	public String getTypeName() {
		return "Reference";
	}

	@Override
	public String getStackFrameName() {
		return "Ljava/lang/String";
	}

	@Override
	public boolean equals(IType other) {
		return other instanceof TRef;
	}

	public IType getReferenceType() {
		return this.referenceType;
	}
	
}