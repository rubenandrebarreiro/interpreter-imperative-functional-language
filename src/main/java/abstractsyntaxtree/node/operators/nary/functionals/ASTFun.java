package main.java.abstractsyntaxtree.node.operators.nary.functionals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.abstractsyntaxtree.exceptions.ASTDuplicatedIdentifierException;
import main.java.abstractsyntaxtree.exceptions.ASTInvalidIdentifierException;
import main.java.abstractsyntaxtree.node.ASTNode;
import main.java.scopes.Environment;
import main.java.scopes.compiler.EnvironmentCompiler;
import main.java.scopes.compiler.instructions.CodeBlockInstructionsSet;
import main.java.types.IType;
import main.java.values.atomics.IValue;
import main.java.values.closures.VClosure;
import main.java.values.utils.exceptions.TypeErrorException;

public class ASTFun implements ASTNode {
	
	private List<String> functionArgumentsIDs;
	
	private ASTNode functionBody;
	
	
	public ASTFun(List<String> functionArgumentsIDs, ASTNode functionBody) {
		this.functionArgumentsIDs = functionArgumentsIDs;
		this.functionBody = functionBody;
	}
	
	
	public List<String> getFunctionArgumentsIDs() {
		return this.functionArgumentsIDs;
	}
	
	public ASTNode getFunctionBody() {
		return this.functionBody;
	}
	
	@Override
	public IValue eval(Environment<IValue> environment) throws ASTInvalidIdentifierException, TypeErrorException {
	
		return new VClosure(environment, this.functionArgumentsIDs, this.functionBody);
	
	}

	@Override
	public void compile(EnvironmentCompiler environmentCompiler, CodeBlockInstructionsSet codeBlockInstructionsSet)
			throws ASTInvalidIdentifierException {
		
		codeBlockInstructionsSet.addCodeInstruction(String.format("new closure_f000%d", 1 /* TODO */));
		codeBlockInstructionsSet.addCodeInstruction(String.format("dup"));
		codeBlockInstructionsSet.addCodeInstruction(String.format("aload SL"));
		codeBlockInstructionsSet.addCodeInstruction(String.format("putfield closure_f000%d/SL type;", 1 /* TODO */));
		
	}

	@Override
	public IType typecheck(Environment<IType> environment) throws TypeErrorException, ASTDuplicatedIdentifierException {
		// TODO Auto-generated method stub
		
		Set<String> argumentsTypecheckingVerified = new HashSet<>();
		
		for(String argumentID : this.functionArgumentsIDs) {
			
			if(!argumentsTypecheckingVerified.contains(argumentID)) {
				
				argumentsTypecheckingVerified.add(argumentID);
				
			}
			else {
				
				throw new ASTDuplicatedIdentifierException("Duplicated Identifier found in Function's declaration!!!");
				
			}
		}
		
		
		
		return null;
	}
}