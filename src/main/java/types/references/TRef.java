package main.java.types.references;

/**
 * Interpreter for Imperative/Functional Language
 * 
 * Interpretation and Compilation of Programming Languages
 * 
 * Faculty of Science and Technology of New University of Lisbon
 * (FCT NOVA | FCT/UNL)
 * 
 * Integrated Master of Computer Science and Engineering
 * (BSc. + MSc. Bologna Degree)
 * 
 * Academic Year 2019/2020
 * 
 */

import main.java.types.IType;

/**
 * Class for Type of a Reference Value, implementing a Type of a Value.
 * 
 * @supervisor Prof. Luis Manuel Caires - lcaires@fct.unl.pt
 * 
 * @author Eduardo Bras Silva (no. 41798) - emf.silva@campus.fct.unl.pt
 * @author Ruben Andre Barreiro (no. 42648) - r.barreiro@campus.fct.unl.pt
 *
 */
public class TRef implements IType {
	
	private IType referenceType;
	
	
	public TRef(IType referenceType) {
		this.referenceType = referenceType;
	}
	
	@Override
	public String getTypeName() {
		return String.format("Reference for Type: %s", this.referenceType.getTypeName());
	}

	@Override
	public String getHeapStackFrameName() {
		return "Ljava/lang/Object";
	}

	@Override
	public String getHeapStackFrameReferenceName() {
		return "ref_class";
	}
	
	@Override
	public boolean equals(IType other) {
		return other instanceof TRef;
	}

	public IType getReferenceType() {
		return this.referenceType;
	}
	
}
