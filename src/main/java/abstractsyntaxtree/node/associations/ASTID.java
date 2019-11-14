package main.java.abstractsyntaxtree.node.associations;

import main.java.abstractsyntaxtree.exceptions.ASTInvalidIdentifierException;
import main.java.abstractsyntaxtree.node.ASTNode;
import main.java.abstractsyntaxtree.scopes.Environment;
import main.java.abstractsyntaxtree.scopes.compiler.EnvironmentCompiler;
import main.java.abstractsyntaxtree.scopes.compiler.instructions.CodeBlockInstructionsSet;
import main.java.abstractsyntaxtree.scopes.heap.structures.FieldAddress;
import main.java.values.atomic.IValue;

/**
 * Class for the Node of an Abstract Syntax Tree (A.S.T.),
 * performing the keeping of an identifier for an expression.
 * 
 * @supervisor Prof. Luis Manuel Caires - lcaires@fct.unl.pt
 * 
 * @author Eduardo Bras Silva (no. 41798) - emf.silva@campus.fct.unl.pt
 * @author Ruben Andre Barreiro (no. 42648) - r.barreiro@campus.fct.unl.pt
 *
 */
public class ASTID implements ASTNode {
	
	// Global Variables:
	/**
	 * The Expression's Identifier
	 */
	private String expressionID;
	
	
	// Constructors:
	/**
	 * Constructor #1:
	 * - The Constructor of a Node of an Abstract Syntax Tree (A.S.T.).
	 * 
	 * @param leftASTNodeDescedant the left side Descendant of the A.S.T. Node
	 * @param rightASTNodeDescedant the left side Descendant of the A.S.T. Node
	 */
	public ASTID(String expressionID) {
		this.expressionID = expressionID;
	}
	
	
	// Methods:
	/**
	 * Evaluates the Expression of the current Node of an Abstract Syntax Tree (A.S.T.),
	 * given the Environment (Scope/Frame), where the current A.S.T. Node it's inside, performing its association.
	 * 
	 * @param environment the Environment (Scope/Frame), where the current A.S.T. Node it's inside
	 * 
	 * @return the evaluation of the Expression of the current Node of an Abstract Syntax Tree (A.S.T.),
	 *  	   given the Environment (Scope/Frame), where the current A.S.T. Node it's inside, performing its association
	 *      
	 * @throws ASTInvalidIdentifierException an Invalid Identifier Exception thrown,
	 * 		   in the case of an Identifier it's completely unknown in the
	 * 		   Environment's ancestor on the Stack of Environments (Scopes/Frames) 
	 */
	@Override
	public IValue<?> eval(Environment environment) throws ASTInvalidIdentifierException {
		
		// Returns the value associated to the Expression's ID,
		// in the current Environment (Scope)
		return environment.find(this.expressionID);
	}


	/**
	 * Compiles the List of Code Instructions of the current Node of an Abstract Syntax Tree (A.S.T.),
	 * given the Environment (Scope/Frame), where the current A.S.T. Node it's inside and
	 * the List of the Code Instructions of the current Node of an
	 * Abstract Syntax Tree (A.S.T.) will be kept, writing J.V.M. instructions,
	 * in order to, perform Identification.
	 * 
	 * @param environment the Environment (Scope/Frame), where the current Code Instructions of
	 *        the current Node of an Abstract Syntax Tree (A.S.T.) will be kept
	 * 
	 * @param codeInstructions the List of the Code Instructions to be compiled
	 * 
	 * @throws ASTInvalidIdentifierException an Invalid Identifier Exception thrown,
	 * 		   in the case of an Identifier it's completely unknown in the
	 * 		   Environment's ancestor on the Stack of Environments (Scopes/Frames) 
	 */
	@Override
	public void compile(EnvironmentCompiler environmentCompiler,
						CodeBlockInstructionsSet codeBlockInstructions) throws ASTInvalidIdentifierException {
		
		FieldAddress valueFieldAddress = null;
		EnvironmentCompiler currentEnvironment = environmentCompiler;
		
		
		codeBlockInstructions.addCodeInstruction("aload 0");
		
		valueFieldAddress = environmentCompiler.find(this.expressionID);
		
		while(valueFieldAddress == null) {
			
			// If it was no found the pretended value and there's no more Ancestors in
			// the Heap Stack of Environments (Scopes/Frames)
			if(currentEnvironment.getAncestor() == null) {
				throw new ASTInvalidIdentifierException("Invalid Identifier!!!\n"
						+ "It was used an free occurrence not defined at any Heap Stack Frame!!!\n\n");
			}
			
			codeBlockInstructions.addCodeInstruction("getfield f" + currentEnvironment.getFrameID() + 
					 "/sl Lf" + currentEnvironment.getAncestor().getFrameID() + ";");
			currentEnvironment = currentEnvironment.getAncestor();
			
			valueFieldAddress = currentEnvironment.find(expressionID);
		}
		
		codeBlockInstructions.addCodeInstruction("getfield f" + valueFieldAddress.getHeapStackFrameLevel() + 
				 "/x" + valueFieldAddress.getOffsetField() + " I");
	
	}
}