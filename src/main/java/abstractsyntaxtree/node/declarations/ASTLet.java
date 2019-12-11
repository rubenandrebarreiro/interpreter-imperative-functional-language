package main.java.abstractsyntaxtree.node.declarations;

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

import java.util.List;

import main.java.abstractsyntaxtree.exceptions.ASTInvalidIdentifierException;
import main.java.abstractsyntaxtree.node.ASTNode;
import main.java.scopes.Environment;
import main.java.scopes.compiler.EnvironmentCompiler;
import main.java.scopes.compiler.instructions.CodeBlockInstructionsSet;
import main.java.scopes.structures.heap.HeapStackFrame;
import main.java.types.IType;
import main.java.values.atomics.IValue;
import main.java.values.utils.exceptions.NumberArgumentsErrorException;
import main.java.values.utils.exceptions.TypeErrorException;

/**
 * Class for the Node of an Abstract Syntax Tree (A.S.T.),
 * performing the association of its descendants {ID -> Value}.
 * 
 * @supervisor Prof. Luis Manuel Caires - lcaires@fct.unl.pt
 * 
 * @author Eduardo Bras Silva (no. 41798) - emf.silva@campus.fct.unl.pt
 * @author Ruben Andre Barreiro (no. 42648) - r.barreiro@campus.fct.unl.pt
 *
 */
public class ASTLet implements ASTNode {
	
	// Global Variables:
	
	/**
	 * The Identifier of A.S.T. Let Node descendant
	 */
	private List<ASTNode> associations;
	
	/**
	 * The Body Expression of A.S.T. Let Node descendant
	 */
	private ASTNode bodyASTLetNodeDescendant;
	
	
	// Constructors:
	
	/**
	 * Constructor #1:
	 * - The Constructor of a Node of an Abstract Syntax Tree (A.S.T.).
	 * 
	 * @param leftASTNodeDescedant the left side Descendant of the A.S.T. Node
	 * @param rightASTNodeDescedant the left side Descendant of the A.S.T. Node
	 */
	public ASTLet(List<ASTNode> associations, ASTNode bodyASTLetNodeDescendant) {
		
		this.associations = associations;
		
		this.bodyASTLetNodeDescendant = bodyASTLetNodeDescendant;
	}
	
	
	// Methods:
	
	/**
	 * Evaluates the Expression of the current Node of an Abstract Syntax Tree (A.S.T.),
	 * given the Environment (Scope), where the current A.S.T. Node it's inside, performing its association.
	 * 
	 * @param environment the Environment (Scope), where the current A.S.T. Node it's inside
	 * 
	 * @return the evaluation of the Expression of the current Node of an Abstract Syntax Tree (A.S.T.),
	 *  	   given the Environment (Scope), where the current A.S.T. Node it's inside, performing its association        
	 * 
	 * @throws ASTInvalidIdentifierException an Invalid Identifier Exception thrown,
	 * 		   in the case of an Identifier it's completely unknown in the
	 * 		   Environment's ancestor on the Stack of Environments (Scopes)
	 * 
	 * @throws TypeErrorException 
	 * @throws NumberArgumentsErrorException 
	 */
	@Override
	public IValue eval(Environment<IValue> environment)
		   throws ASTInvalidIdentifierException, TypeErrorException, NumberArgumentsErrorException {
		
		// Stars the Scope (Environment) of the declared expression
		Environment<IValue> newEnv = environment.beginScope();
		
		try {
			// The addition of the association between the left side Descendant
			// and the expression evaluated of the right side
			for (ASTNode astNode : associations) {
				astNode.eval(newEnv);
			}
		}
		catch (ASTInvalidIdentifierException astInvalidIdentifierException) {
			astInvalidIdentifierException.printStackTrace();
		}
		catch (TypeErrorException typeErrorException) {
			typeErrorException.printStackTrace();
		}

		// The evaluation of the right side Descendant
		IValue expressionEvaluated = this.bodyASTLetNodeDescendant.eval(newEnv);
		
		// Ends the Scope (Environment) of the previously declared expression
		newEnv.endScope();
		
		// Returns the expression evaluated
		return expressionEvaluated;
	}


	/**
	 * Compiles the List of Code Instructions of the current Node of an Abstract Syntax Tree (A.S.T.),
	 * given the Environment (Scope/Frame), where the current A.S.T. Node it's inside and
	 * the List of the Code Instructions of the current Node of an
	 * Abstract Syntax Tree (A.S.T.) will be kept, writing J.V.M. instructions,
	 * in order to, perform the Let/Declaration.
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
		
		EnvironmentCompiler newEnvironment = environmentCompiler.beginScope();
		EnvironmentCompiler ancestor = newEnvironment.getAncestor();

		System.err.println("Ancestor is " + ancestor);
		
		int currentFrameID = codeBlockInstructions.getCurrentFrameID();
		
		if(ancestor == null) {
			codeBlockInstructions.addHeapStackFrame(
					new HeapStackFrame(
							currentFrameID,
							null,
							associations.size()));
		}
		else {
			codeBlockInstructions.addHeapStackFrame(
					new HeapStackFrame(
							currentFrameID,
							codeBlockInstructions.getHeapStackFrameByID(currentFrameID - 1),
							associations.size()));
		}
		
		
		this.createFrame(ancestor, codeBlockInstructions, currentFrameID);
		
		for(ASTNode associationASTNode : this.associations) {
			associationASTNode.compile(newEnvironment, codeBlockInstructions);
		}
		
		this.bodyASTLetNodeDescendant.compile(newEnvironment, codeBlockInstructions);
		
		this.removeFrame(ancestor, codeBlockInstructions, currentFrameID);
	}
	
	private void createFrame(EnvironmentCompiler ancestor, CodeBlockInstructionsSet codeInstructions, int currentFrameID) {
		int ancestorFrameID = 0;
		if(ancestor != null) ancestorFrameID = currentFrameID - 1;
		
		codeInstructions.addCodeInstruction(";------------------Start new frame------------------");
		codeInstructions.addCodeInstruction("new f" + currentFrameID);
		codeInstructions.addCodeInstruction("dup");
		codeInstructions.addCodeInstruction("invokespecial f" + currentFrameID + "/<init>()V");
		codeInstructions.addCodeInstruction("dup");
		codeInstructions.addCodeInstruction("aload 0");
		
		if(ancestor == null) {
			System.err.println("Ancestor is null!");
			codeInstructions.addCodeInstruction("putfield f" + currentFrameID + "/sl Ljava/lang/Object;");
		}
		else {
			System.err.println("Ancestor is NOT null!");
			codeInstructions.addCodeInstruction("putfield f" + currentFrameID + "/sl Lf" + ancestorFrameID + ";");
		}
		
		codeInstructions.addCodeInstruction("astore 0");
		codeInstructions.addCodeInstruction(";------------------End new frame------------------");
		codeInstructions.addCodeInstruction("\n");
		

	}
	
	private void removeFrame(EnvironmentCompiler ancestor, CodeBlockInstructionsSet codeInstructions, int currentFrameID) {
		int ancestorFrameID = 0;
		if(ancestor != null) ancestorFrameID = currentFrameID - 1;
		
		codeInstructions.addCodeInstruction("\n");
		codeInstructions.addCodeInstruction(";------------------Start remove frame------------------");
		codeInstructions.addCodeInstruction("aload 0");
		
		if(ancestor == null) {
			codeInstructions.addCodeInstruction("getfield f" + currentFrameID + "/sl Ljava/lang/Object;");
		}
		else {
			codeInstructions.addCodeInstruction("getfield f" + currentFrameID + "/sl Lf" + ancestorFrameID + ";");
		}
		
		codeInstructions.addCodeInstruction("astore 0\n");
		codeInstructions.addCodeInstruction(";------------------End remove frame------------------");
		
		codeInstructions.addCodeInstruction("\n");
	}


	@Override
	public IType typecheck(Environment<IType> environment) throws TypeErrorException {
		// TODO Auto-generated method stub
		return null;
	}
}