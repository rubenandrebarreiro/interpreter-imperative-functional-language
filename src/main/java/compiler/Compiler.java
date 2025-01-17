package main.java.compiler;

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

import java.io.IOException;

import main.java.abstractsyntaxtree.exceptions.ASTInvalidIdentifierException;
import main.java.abstractsyntaxtree.node.ASTNode;
import main.java.abstractsyntaxtree.node.operators.unary.print.ASTPrint;
import main.java.scopes.compiler.EnvironmentCompiler;
import main.java.scopes.compiler.instructions.codeblocks.CodeBlockInstructionsSet;

/**
 * Class for the Compiler.
 * 
 * @supervisor Prof. Luis Manuel Caires - lcaires@fct.unl.pt
 * 
 * @author Eduardo Bras Silva (no. 41798) - emf.silva@campus.fct.unl.pt
 * @author Ruben Andre Barreiro (no. 42648) - r.barreiro@campus.fct.unl.pt
 *
 */
public class Compiler {
	
	// Global Instance Variables:
	
	/**
	 * The Environment Compiler.
	 */
	private EnvironmentCompiler environment;
	
	/**
	 * The Code Block Instructions.
	 */
	private CodeBlockInstructionsSet codeBlockIntructions;
	
	private boolean isString;
	
	
	// Constructors:
	
	/**
	 * Constructor #1:
	 * - Compiles a Code Block Instructions, in a determined Environment (Scope/Frame),
	 *   in order to generate the respectively Scope/Frame file, where will be written the Java Byte Code,
	 *   based on J.V.M. Instructions set;
	 * 
	 * @param rootNode the Root Node of the A.S.T. (Abstract Syntax Tree)
	 * 
	 * @throws ASTInvalidIdentifierException an Invalid Identifier Exception thrown,
	 * 		   in the case of an Identifier it's completely unknown in the
	 * 		   Environment's ancestor on the Heap Stack of Environments (Scopes/Frames)
	 */
	public Compiler(ASTNode rootNode) throws ASTInvalidIdentifierException {
		
		this.environment = new EnvironmentCompiler();
		this.codeBlockIntructions = new CodeBlockInstructionsSet();
		
		if(rootNode instanceof ASTPrint)
			isString = true;
		rootNode.compile(environment, codeBlockIntructions);
	}
	
	
	// Methods/Functions:
	
	/**
	 * Generates a file, where will be written the Java Byte Code, based on J.V.M. Instructions set.
	 * 
	 * @param filename the name of the file, where will be written the Java Byte Code,
	 *        based on J.V.M. Instructions set.
	 * 
	 * @throws IOException an Input/Output Exception to be thrown
	 */
	public void generateJavaByteCodeJVMInstructions(String filename) throws IOException {
		this.codeBlockIntructions.dump(filename, isString);
	}
	
}