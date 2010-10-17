/*
 * Object-Oriented Programming
 * Copyright (C) 2010 Robert Grimm
 * Edited by Patrick Hammer
<<<<<<< HEAD
 * Test Edited by Paige
=======
>>>>>>> 5da2b25a04c1f9340af211ca5b1ab8296bcb9d2b
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package xtc.oop;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import xtc.lang.JavaFiveParser;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 *
 * @author P.Hammer
 * @author A.Krebs
 * @author L. Pelka
 * @author P.Ponzeka
 * @version 1
 */
public class Translator extends Tool {
	public final boolean DEBUG=false;

	File inputFile = null;

	/** Create a new translator. */
	public Translator() {
		// Nothing to do.
	}

	public String getCopy() {
		return "(C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka";
	}

	public String getName() {
		return "Java to C++ Translator";
	}

	public String getExplanation() {
		return "This tool translates a subset of Javat to a subset of C++.";
	}

	public void init() {
		super.init();
		
		// Declare command line arguments.
		runtime.
			bool("printJavaAST", "printJavaAST", false,
				 "Print the Java AST.").
			bool("countMethods", "optionCountMethods", false,
				 "Print the number of method declarations.").
			bool("translate", "translate", false,
				 "Translate .java file to c++.").
			bool("file", "file", false,
				 "Output to a file");
	}

	public void prepare() {
		super.prepare();

		// Perform consistency checks on command line arguments.
	}

	public File locate(String name) throws IOException {
		File file = super.locate(name);
		if (Integer.MAX_VALUE < file.length()) {
			throw new IllegalArgumentException(file + ": file too large");
		}
		inputFile = file;
		System.out.println("using this method");
		return file;
	}

	public Node parse(Reader in, File file) throws IOException, ParseException {
		JavaFiveParser parser =
			new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}

	//-----------------------------------------------------------------------
	public void process(Node node) {
		
		// Handle the translate option
		if (runtime.test("translate")) {
			runtime.console().p("translating...").pln().flush();
			
			/****EDIT LIZ**********/
			
			final InheritanceBuilder inherit = new InheritanceBuilder(inputFile);
		
			/***********************/
			
			/*________________________EDITED BY PAIGE PONZEKA 10/13/10____________________________*/
			cppClass cppReader = new cppClass(node);
			
			
			/*
			 Visits the CompilationUnit
				-UNKNOWN
				-ImportDeclaration
				-ClassDeclaration
			 */
			new Visitor() {
				//public void visitCompilationUnit(GNode n) {
			//		visit(n);
					//runtime.console().p("Number of methods: ").p(count).pln().flush();
			//	}//end of visitCompilationUnit Method


				/*
				 
					CHILD VISIT MISSING HERE!!!
				 
				 
				 */
			//new Visitor(){
				
				
				//visit the ImportDeclartion
				public void visitImportDeclaration(GNode n) {
					
					//While debugging... Print the name of the current tree node
					if(DEBUG){runtime.console().p(n.getName()).pln().flush();}
					
					//creates a new visitor to visit the ImportDeclaration subtree
					/*
						Visits ImportDeclaration:
							UNKNOWN
							-QualifiedIdentifier
							UNKNOWN
					 */
					new Visitor(){
						
						public void visitQualifiedIdentifier(GNode n) {
							
							//While debugging... Print the name of the current tree node
							if(DEBUG){runtime.console().p(n.getName()).pln().flush();}
							
							//runs a for loop for ALL the children of the QualifiedIdentifier Subtree
							for(int i=0;i<(n.size());i++)
							{
								//n.getString(i) gets all the string names of the subtree	
								if(DEBUG){runtime.console().p(n.getString(i)).pln().flush(); }
							}
							visit(n);
						}//end of visitQualifiedIdentifier Method
						
						public void visit(Node n) {
							for (Object o : n) if (o instanceof Node) dispatch((Node)o);
						} //end of visit method
						
					}.dispatch(n);//end of ImportDeclaration Subtree
					
					visit(n);
				}//end of visitImportDeclaration Method
				
				
				
				//visit Class Declaration
				public void visitClassDeclaration(GNode n){
				/****EDIT LIZ**********/
					
					inherit.addClassdef(n);
				/***********************/
					/*
					 Visit Each ClassDeclaration Subtree
						-Modifiers
						ClassName
						-UNKNOWN
						-UNKNOWN
						-UNKNOWN
						-Class Body
					 */
					
					//While debugging... Print the name of the current tree node
					if(DEBUG){runtime.console().p(n.getName()).pln().flush();}
					
					//create a new visitor for the Modifier Subtree
					new Visitor(){
						
						//visit ClassDeclaration->modifier
						public void visitModifier(GNode n) {
							
							/*************ISSUE****************
							 While this does get the modifer for the class it 
							 also visits the modifer for any 
							 subtree values  in the ClassBody ( A Child of ClassDeclaration)
							 *********************************/
							
							//While debugging... Print the name of the current tree node
							if(DEBUG){runtime.console().p(n.getName()).pln().flush();}
							
							for(int i=0;i<(n.size());i++)
							{
							    //print the name of each modifier
								if(DEBUG){runtime.console().p(n.getString(i)).pln().flush();}
							}
							visit(n);
						}//end of visitModifier Method
						
						public void visit(Node n) {
							for (Object o : n) if (o instanceof Node) dispatch((Node)o);
						}//end of visit methods
						
					}.dispatch(n); //end of Modifer subtree visitor
						
					//print the ClassName
					if(DEBUG){runtime.console().p(n.getString(1)).pln().flush();}
					
					
					/*
					 
					 CHILD VISIT MISSING HERE!!!
					 
					 
					 */
					
					/*
					 
					 CHILD VISIT MISSING HERE!!!
					 
					 
					 */
					
					/*
					 
					 CHILD VISIT MISSING HERE!!!
					 
					 
					 */
					
					
					
					
					//visit ClassDeclaration->ClassBody
					/*
					 Visit ClassBody subtree
						-constructorDeclaration
						-MethodDeclaration
						
					 */
					new Visitor(){
						public void visitClassBody(GNode n)
						{
						
							//While debugging... Print the name of the current tree node
							if(DEBUG){runtime.console().p(n.getName()).pln().flush();}
						
							/*
							 Visit ClassBody Subtree
							 -MethodDeclaration
							 */
							new Visitor(){
								//visit ClassBody-> ConstructionDeclaration
								public void visitConstructorDeclaration(GNode n) {
								
									//While debugging... Print the name of the current tree node
									if(DEBUG){runtime.console().p(n.getName()).pln().flush();}
								
									/*
									 Visit ConstructorDeclaration Subtree
									 -Modifiers
									 UNKNOWN
									 ConstructorName
									 -FormalParameters
									 UNKNOWN
									 Block()
									 */
									
									//print out the name of the contructor
									if(DEBUG){runtime.console().p(n.getString(2)).pln().flush();}
									
									new Visitor(){
										
										//Visit ConstructionDeclaration-> Modifier
										public void visitModifier(GNode n) {
											//runs a for loop for ALL the children of the Qualified Identifier Subtree
											for(int i=0;i<(n.size());i++)
											{
												//here we have to insert file readers and create the subfolders for the import declaration
												if(DEBUG){runtime.console().p(n.getString(i)).pln().flush();} //xtc.tree.node
											}
											visit(n);
										}//end of visitModifier method
									
										/*
									 
										 CHILD VISIT MISSING HERE!!!
									 
									 
										 */
									
										
									
										public void visitFormalParameter(GNode n)
										{
											//While debugging... Print the name of the current tree node
											if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
											
										
											/*
											 Visit FormalParameter Subtree
												Modifiers()
												-Type
												UNKNOWN
												parameterName
												UNKNOWN
											 */
											new Visitor(){
												/*
											 
												 CHILD VISIT MISSING HERE!!!
												 What to do with Motifiers()?
											 
												 
												 */
												public void visitType(GNode n){
													
													//While debugging... Print the name of the current tree node
													if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
												
													//NEEDS TO PUT IN SUPPORT FOR ADDITIONAL TYPES IN HERE
													/*
													 Visit Type Subtree
															-PrimitiveType
															UNKNOWN
													 */
													new Visitor(){
													
														public void visitPrimitiveType(GNode n){
														
															//While debugging... Print the name of the current tree node
															if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
														
															//gets all the type children 
															for(int i=0;i<(n.size());i++)
															{
																//print the type 
																if(DEBUG){runtime.console().p(n.getString(i)).pln().flush();} 
															}
														
															visit(n);
														}//end of visitPrimitiveType method
													
													
														/*
													 
														 CHILD VISIT MISSING HERE!!!
													 
													 
														 */
													
													
														public void visit(Node n) {
															for (Object o : n) if (o instanceof Node) dispatch((Node)o);
														}
													}.dispatch(n); //end of type subtree
												
													visit(n);
												}//end of visitType method
											
												
											
												
												public void visit(Node n) {
													for (Object o : n) if (o instanceof Node) dispatch((Node)o);
												}//end of visit method
												
											}.dispatch(n); //end of FormalParameter subtree Visitor
											
											
											/*
											 
											 CHILD VISIT MISSING HERE!!!
											 
											 
											 */
											
											
											//print out ParameterName
											if(DEBUG){runtime.console().p(n.getString(3)).pln().flush(); }
											
											
											/*
											 
											 CHILD VISIT MISSING HERE!!!
											 
											 
											 */
											
											
											visit(n);
										}//end of visitFormalParameter Method
										
										
										
									
										/*
									 
										 CHILD VISIT MISSING HERE!!!
									 
									 
										 */
									
										
										
										
										/*
									 
										 CHILD VISIT MISSING HERE!!!
										 What to do for Block()?
									 
										 */
										
										
										public void visit(Node n) {
											for (Object o : n) if (o instanceof Node) dispatch((Node)o);
										}//end of visit method
										
									}.dispatch(n);//end of ConstructorDeclaration subtree 
									visit(n);
								}//end of visitConstructorDeclaration methiod
							
								//visit classBody-> MethodDeclaration
								public void visitMethodDeclaration(GNode n) {
									
									//While debugging... Print the name of the current tree node
									if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
								
									/*
									 Visit MethodDeclaration Subtree
									 -Modifiers
									 UNKNOWN
									 VoidType()-Other Types
									 methodNAme
									 -FormalParameters
									 UNKNOWN
									 UNKNOWN
									 Block()
									 */
								
									new Visitor(){
										//Visit MethodDeclaration-> Modifier
										public void visitModifier(GNode n) {
									
											//While debugging... Print the name of the current tree node
											if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
											
											//runs a for loop for ALL the children of the ModifierSubtree
											for(int i=0;i<(n.size());i++)
											{
												//here we have to insert file readers and create the subfolders for the import declaration
												if(DEBUG){runtime.console().p(n.getString(i)).pln().flush();} //xtc.tree.node
											}
											visit(n);
										}//end of visitModifier Method
										public void visit(Node n) {
											for (Object o : n) if (o instanceof Node) dispatch((Node)o);
										}
									}.dispatch(n); //end of Modifier Subtree visitor
									
									
									
									/*
								 
									 CHILD VISIT MISSING HERE!!!
								 
								 
									 */
								
									
									
									
									
									
									/*********ISSUE***************
									 
									 CHILD VISIT MISSING HERE!!!
									 //print out the name of the type
									 //can't print out VoidType
									 runtime.console().p(n.getString(2)).pln().flush(); //<--Throws a ClassCastException: Not a Token
									 
									 
									 ****************************/
									 
									 
									 
									 
									
								
									//print out the methodName
									if(DEBUG){runtime.console().p(n.getString(3)).pln().flush();}
								
									/*
									 Visit FormalParameter Subtree
										Modifiers()
										-Type
										UNKNOWN
										parameterName
										UNKNOWN
									 */
									new Visitor(){
										/*
									 
										 CHILD VISIT MISSING HERE!!!
										 What to do with Motifiers()?
									 
									 
										 */
										public void visitType(GNode n){
										
											//While debugging... Print the name of the current tree node
											if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
										
											//NEED TO PUT IN SUPPORT FOR ADDITIONAL TYPES IN HERE
											/*
											 Visit Type Subtree
												-QualifiedIdentifier
												-Dimensions
										 
											*/
											new Visitor(){
											
												public void visitQualifiedIdentifier(GNode n){
												
													//While debugging... Print the name of the current tree node
													if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
												
													//gets all the children 
													for(int i=0;i<(n.size());i++)
													{
														//print the children
														if(DEBUG){runtime.console().p(n.getString(i)).pln().flush(); }
													}
												
													visit(n);
												}//end of visitQualifiedIdentifier method
											
												public void visitDimensions(GNode n){
											
													//While debugging... Print the name of the current tree node
													if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 
												
													//gets all the children 
													for(int i=0;i<(n.size());i++)
													{
														//print the children
														if(DEBUG){runtime.console().p(n.getString(i)).pln().flush();} 
													}
													visit(n);
												}//end of visitDimensions method											
												public void visit(Node n) {
													for (Object o : n) if (o instanceof Node) dispatch((Node)o);
												}
											}.dispatch(n); //end of type subtree
										
											visit(n);
										}//end of visitType method
									
										
									
									
									
										public void visit(Node n) {
											for (Object o : n) if (o instanceof Node) dispatch((Node)o);
										}
									}.dispatch(n); //end of methodDeclaration->FormalParameter subtree Visitor
								
									
									/*
									 
									 CHILD VISIT MISSING HERE!!!
									 
									 
									 */
									
									
									/************************ISSUE*****************
									//print out ParameterName
									runtime.console().p(n.getString(2)).pln().flush(); <--Returns java.lang.ClassCastException: not a token
									***************************************/
									
									
									/*
									 
									 CHILD VISIT MISSING HERE!!!
									 
									 
									 */
									
									
									/*********************************************************/
									/*
								 
									 CHILD VISIT MISSING HERE!!!
								 
								 
									 */
									/*
								 
									 CHILD VISIT MISSING HERE!!!
								 
								 
									 */
								
									/*
								 
									 CHILD VISIT MISSING HERE!!!
									 What to Do with Block()?
								 
									 */
									visit(n);
								}//end of visitMethodDeclaration method	
							
								public void visit(Node n) {
									for (Object o : n) if (o instanceof Node) dispatch((Node)o);
								} // end of visit method
							
							}.dispatch(n);//end of methodDeclaration subtree visitor
								
							
							visit(n);
							}//end of visitClassBody method
						
						
							public void visit(Node n) {
								for (Object o : n) if (o instanceof Node) dispatch((Node)o);
							}
						
						}.dispatch(n); //end of ClassBody Subtree visitor
					
					visit(n);
					}//end of visitClassDeclaration Method
				
				
				
				
				
				//end of edited by paige 10/11/10
				
				
				public void visit(Node n) {
					for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				}
			}.dispatch(node); //end of main dispatch
			//two cases of using the CppCreator class
			//one uses a filepath and the other uses the source .java file as an arg.
			
			/****EDIT LIZ******/
			inherit.close(); // when all nodes are visited and inheritance files are made close files
			/****EDIT LIZ******/

			String path = "/users/Paige/Desktop/test.java";
			CppCreator toC = new CppCreator (path);
			toC.write("Testing 1,2,3...\n");
			toC.write("Now testing");
			if (runtime.test("optionVerbose")) {
				runtime.console().p("creating file at" + path).pln().flush();
			}
			toC.close();
			if (runtime.test("optionVerbose")) {
				runtime.console().p("Your file has been written...").pln().flush();
			}

			CppCreator dow = new CppCreator (inputFile);
			dow.write("Testing on a different file\n");
			dow.write("Now testing again");

			if (runtime.test("optionVerbose")) {
				runtime.console().p("creating file at " + inputFile.getAbsolutePath()).pln().flush();
			}
			dow.close();
			if (runtime.test("optionVerbose")) {
				runtime.console().p("Your file has been written...").pln().flush();
			}
		}
		//-----------------------------------------------------------------------

		if (runtime.test("printJavaAST")) {
			runtime.console().format(node).pln().flush();
		}

		if (runtime.test("optionCountMethods")) {
			new Visitor() {
				int count = 0;

				public void visitCompilationUnit(GNode n) {
					visit(n);
					runtime.console().p("Number of methods: ").p(count).pln().flush();
				}

				public void visitMethodDeclaration(GNode n) {
					count++;
					visit(n);
				}

				public void visit(Node n) {
					for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				}

			}.dispatch(node);
		}//end of runtime.test("Translate") test

	}//end of process method
	
	/**
	 * Run the translator with the specified command line arguments.
	 *
	 * Uses xtc.util.tool run();
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		new Translator().run(args);
	}	
}//end of Translator.java


//looks into write to method to .cpp file... write to method that will take a file
//tweek methods to take a single given GNode
//functionality for arrays
//System.out.print
//write to a file

/*
 Takes a classDeclaration GNode and  generates the basic class values
 */
class cppClass extends Visitor{ 
	
	//public string className;
	public final boolean DEBUG = true;
	private StringBuilder classString = new StringBuilder();
	cppClass(Node n)
	{
		visit(n);
		if(DEBUG){System.out.println(classString);}
	
	}//end of cppClass constructor
	public void visitClassDeclaration(GNode n) {
		
		
		classString.append("Class "+ getClassName(n)+ "{ \n");
		
		//call setMethods
		StringBuilder methods=setMethods(n);
		classString.append(methods+ "\n");

		classString.append("} \n");
		
		
	}//end of visitClassDeclaration Method
	
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public boolean isClassDeclaration(GNode n)
	/*
	 checks to make sure the given GNode is actually an instance of ClassDeclaration
	 */
	{
		return true;
	}//end of isClassDeclaration method
	
	public String getClassName(GNode n)
	/*
	 returns the className of the given classDeclaration's node
	 */
	{
		return n.getString(1);
		
	}//end of getClassName method
	
	public StringBuilder setMethods(GNode n)
	/*
	  sets the methods for the ClassDeclaration at GNode n
	 */
	{
		//create a new cppMethod Class
		cppMethod aMethod = new cppMethod(n);
		return aMethod.getString();
	}//end of setMethods method
}//end of cppClass
//change to take just one method GNode
class cppMethod extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder methodString;
	cppMethod(GNode n)
	{
		//test(n);
		//if(DEBUG){System.out.println("Inside cppMethod");}
		methodString=new StringBuilder();
		visit(n);
		

	}//end of cppMethod constructor
	public void visitMethodDeclaration(GNode n) {
		
		methodString.append("\t" + setType(n)+" "+ getMethodName(n)+"("+getParameters(n)+")" +"{ \n");
		StringBuilder fields= setFields(n);
		methodString.append(fields+"\n");
		methodString.append("\t} \n");
		getExpressionStatements(n);
	}//end of visitClassDeclaration Method
	/*public void test(GNode n)
	{
		Node node= (Node) n;
		Object o=node.get(0);
		System.out.println("TESTING!!!!!! "+o.toString());
		if (o instanceof Modifiers)
		{
			System.out.println("RAWR I'm A Dinosaur");
		}
		
	}*/
	public StringBuilder setType(GNode n)
	{
		StringBuilder type= new StringBuilder();
		
		Node node=n.getNode(2); //VoidType, Type 
		Object o=n.get(2);
		/*
		 if node is a Primitrive Type
			do Something
		 if node is a Qualified Identifier
			do something
		 else
			printout the name at the current position
		 */
		StringBuilder s= new StringBuilder();
		if(node.hasName("Type"))
		{
			s.append(getPrimitiveType((GNode)node));
			s.append(getQualIden((GNode)node));
		}  
 		else
			s.append(node.getName());
		
		return s;
		
		
	}
	public StringBuilder getParameters(GNode n)
	{
		cppParameters param = new cppParameters(n);
		return param.getString();
		
	}//end of getParameters method
	public StringBuilder getPrimitiveType(GNode n)
	{
		cppPrimitiveType pType = new cppPrimitiveType(n);
		return pType.getPrimString(n);
	}
	public StringBuilder getQualIden(GNode n)
	{
		cppQualifiedIde qi= new cppQualifiedIde(n);
		return qi.getString();
	}
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public boolean isMethodDeclaration(GNode n)
	/*
	 checks to make sure the given GNode is actually an instance of methodDeclaration
	 */
	{
		return true;
	}//end of isMethodDeclaration Method
	public StringBuilder getString()
	/*
	 returns the entire string generated for the method
	 */
	{
		return methodString;
	}
	public String getMethodName(GNode n)
	{
		return n.getString(3);
	}//end of getMethodName
	
	public StringBuilder setFields(GNode n)
	{
		cppFieldDeclaration methodFields= new cppFieldDeclaration(n);
		return methodFields.getFieldString();
	}
	public void getExpressionStatements(GNode n)
	{
		cppExpressionStatement cppEx= new cppExpressionStatement(n);
	}
	
}//end of cppMethod class
//put in code for Constructors
class cppExpressionStatement extends Visitor{
	
	public final boolean DEBUG = true;
	private StringBuilder pString;	
	private boolean isOut;
	private String print;
	private boolean isSystem;
	private boolean isSystemOutPrint;
	private GNode arguments;
	cppExpressionStatement(GNode n)
	{
		pString=new StringBuilder();
		isOut=false;
		isSystem=false;
		isSystemOutPrint=false;
		arguments=null;
		visit(n);
		testSystemOut();
	}
	public void visitCallExpression(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(2));}
		pString.append(n.getString(2));
		print=n.getString(2);
		getSelectionExpression(n);
		cppArguments cppargs = new cppArguments(n);
		arguments=cppargs.getArguments();
	}//end of visitCallExpression method
	/*public void visitPrimaryIdentifier(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
	}//end of visitPrimaryIdentifier Method
	public void visitSelectionExpression(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(1));}
	}//end of visitSelectionExpression Method
	*/
	public void getSelectionExpression(GNode n)
	{
		cppSelectionExpression cppCall = new cppSelectionExpression(n);
		isOut=cppCall.isOut();
		isSystem=cppCall.isSystem();
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	/*public void visitArguments(GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}
	}*/
	
	public StringBuilder getString()
	{
		return pString;
	}
	public boolean isOut()
	{
		return isOut();
	}
	public boolean isSystem()
	{
		return isSystem();
	}
	public boolean isSystemOutPrint()
	{
		return isSystemOutPrint;
	}
	public String getPrint()
	{
		return print;
	}
	public void testSystemOut()
	{
		//if(DEBUG){System.out.println("SYSTEM:"+isSystem +"OUT:"+isOut+"Print:"+print);}
		if(isSystem)
		{
			if(isOut)
			{
				if(print.compareTo("println")==0)
				{
					isSystemOutPrint=true;
					//call constructor for System.out.println
					if(DEBUG){System.out.println(arguments.getName());}
				}
				else if(print.compareTo("print")==0)
				{
					isSystemOutPrint=true;
					if(DEBUG){System.out.println(arguments.getName());}
					//call constructor for System.out.println
				}
				else {
					//something is wrong
					System.out.println("Error");
				}

			}
		}
		
	}
}//end of cppExpressionStatemnet class
class cppArguments extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder aString;
	private GNode arguments;
	cppArguments(GNode n)
	{
		aString=new StringBuilder();
		visit(n);
	}
	public GNode getArguments()
	{
		return arguments;
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	public void visitArguments (GNode n)
	{
		if(DEBUG){System.out.println(n.getName());}
		arguments=n;
	}
}//end of cppArguments method
class cppSelectionExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder sString;
	private boolean isOut;
	private boolean isSystem;
	private GNode arguments; 
	cppSelectionExpression(GNode n)
	{
		sString=new StringBuilder();
		isOut=false;
		isSystem=false;
		visit(n);
	}
	public void visitSelectionExpression(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(1));}
		String s = n.getString(1);
		if(s.compareTo("out")==0)
		{
			isOut=true;
		}
		getPrimaryIdentifer(n);
		sString.append(n.getString(1));
	}//end of visitSelectionExpression Method
	
	public GNode getArguments()
	{
		if (arguments!=null)
		{
			return arguments;
		}
		else {
			System.out.println("ERROR!");
			return null;
		}

	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	public void getPrimaryIdentifer(GNode n)
	{
		cppPrimaryIdentifier PrimId= new cppPrimaryIdentifier(n);
			isSystem=PrimId.isSystem();
	}
	public boolean isSystem()
	{
			return isSystem;
	}
    public boolean isOut()
	{
			return isOut;
	}
	public StringBuilder getString()
	{
		return sString;
	}
}//end of cppCallExpression Class
class cppPrimaryIdentifier extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder pString;
	private boolean isSystem;
	cppPrimaryIdentifier(GNode n)
	{
		pString= new StringBuilder();
		isSystem=false;
		visit(n);
	}
	public void visitPrimaryIdentifier(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		String s=n.getString(0);
		pString.append(n.getString(0));
		if(s.compareTo("System")==0)
		{
			isSystem=true;
		}
	}
	public void visit(Node n){
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);	
	}
	public StringBuilder getString()
	{
		return pString;
	}
	public boolean isSystem()
	{
		return isSystem;
	}
}//end of cppPrimaryIdentifer class
class cppParameters extends Visitor{
	
	public final boolean DEBUG = false;
	private StringBuilder pString;	
	
	cppParameters(GNode n)
	{
		pString= new StringBuilder();
		visit(n);
	}//end cppParameters constructor
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	
	public void visitFormalParameters(GNode n) {
		if(DEBUG){System.out.println(n.getName());}
		//types
		
		if(n.size() > 0)
		{
			if(DEBUG){System.out.println("Greater Than Zero");}
			cppSubParameters subPar = new cppSubParameters(n);
			 pString.append(subPar.getString());			
		}
	
	}//end of visitClassDeclaration Method	
	
	public StringBuilder getString(){
		return pString;
	}//end getString method
}//end of cppParameters class
class cppSubParameters extends Visitor{
	
	public final boolean DEBUG = false;
	private StringBuilder pString;	
	
	cppSubParameters (GNode n)
	{
		pString=new StringBuilder();
		visit(n);
		
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method	
	
	public void visitFormalParameter(GNode n) {
		if(DEBUG){System.out.println(n.getName());}
		//Modifiers SubTree
		cppModifier mod= new cppModifier(n);
		pString.append(mod.getString()+ " ");
		//Type SubTree
		cppType type = new cppType(n);
		pString.append(type.getString()+ " ");
		//parameterName
		pString.append(n.getString(3));
		
		
		
	}//end of visitClassDeclaration Method
	
	public StringBuilder getString()
	{
		return pString;
	}//end of getString method
}//end of cppSubParameters class
class cppFieldDeclaration extends Visitor{
	
	public final boolean DEBUG = true;
	private StringBuilder fieldString;
	
	
	/*Takes Block and sets the fields */
	cppFieldDeclaration(GNode n){
		
		fieldString=new StringBuilder();
		visit (n);
	}//end of cppField Constructor
	public void visitFieldDeclaration(GNode n) {
		
		//fix set modifier
		fieldString.append("\t\t"+setModifier(n)+" "+setType(n)+" "+setDeclarator(n)+"; \n");
		//setType(n);
		//setModifier(n;)
		//methodString.append("\t Method Insert_Type "+ getMethodName(n)+ "{ \n");
		//methodString.append("\t \t//method body here \n");
		//methodString.append("\t} \n");
		
	}//end of visitClassDeclaration Method
	
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public StringBuilder getFieldString()
	{
		return fieldString;
	}
	public boolean isFieldDeclaration(GNode n)
	/*
	 checks to make sure the given GNode is a FieldDeclaration
	 */
	{
		return true;
	}
	public String getFieldName(GNode n)
	{
			//inside Declarator
		return " ";
	}
	public StringBuilder setType(GNode n)
	{
			//new class
		//different options for class types and primitive types
			//return " ";
		cppType classType = new cppType(n);
		return classType.getType();
		
	}
	public StringBuilder setModifier(GNode n)
	{
			//new class
		cppModifier modifiers=new cppModifier(n);
		return modifiers.getString();
		//first child of field Declaration
		
	}
	public StringBuilder setDeclarator(GNode n)
	{
		cppDeclarator decl= new cppDeclarator(n);
		return decl.getString();
	}
	
}//end of cppFieldDeclaration Class
class cppDeclarator extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder declaratorString;

	cppDeclarator(GNode n)
	{
		declaratorString=new StringBuilder();
		declaratorString.append(getDeclarator(n));
		visit(n);
	}//end of cppDeclarator constructor
	
	public void visitDeclarators(GNode n) {
		//typeString.append(getPrimitiveType(n));
		if(DEBUG){System.out.println("Declarators");};
		
	}//end of visitClassDeclaration Method
	public StringBuilder getDeclarator(GNode n)
	{
		cppSubDeclarator subDecl = new cppSubDeclarator(n);
		return subDecl.getString();
		
	}//end of setDeclarator method
	
	public void setLiterals(GNode n)
	{
		
		
		//new class (Generics?) 
		
		
		
		
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	
	public StringBuilder getString()
	{
		return declaratorString;
	}
	

}//end of cppDeclarator type
class cppSubDeclarator extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder declaratorString;
	
	cppSubDeclarator(GNode n)
	{
		declaratorString = new StringBuilder();
		visit(n);
	}
	public void visitDeclarator(GNode n) {
		declaratorString.append(n.getString(0));
		if(DEBUG){System.out.println("Declarator");};
		//getLiteral(n);	
		getExpression(n);
	}//end of visitClassDeclaration Method
	public void getExpression(GNode n)
	{
		
		if(n.getNode(2)!=null)
		{
			Node sub=n.getNode(2);
			
			//System.out.println(sub.getName());
			if(DEBUG){System.out.println("size: "+sub.size());}
			
			if(sub.size()>1)
			{
			cppExpression cppEx = new cppExpression(n);
				
			if(DEBUG){System.out.println("Expression:"+cppEx.getString()+"\n");}
			declaratorString.append("= "+cppEx.getString());
			}
			else {
					getLiteral(n);			
			}
		}
	}
	public void getLiteral(GNode n)
	{
		if(DEBUG){System.out.println("PRENODE:"+n.getName());}
			cppLiteral cppLit =new cppLiteral(n);
			declaratorString.append("= "+cppLit.getString());		
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public StringBuilder getString()
	{
		return declaratorString;
	}
	
}//end of cppSubDeclarator method
class cppLiteral extends Visitor{
	public final boolean DEBUG =false;
	private StringBuilder lString;
	private boolean foundLiteral;
	private int location;
	private int count;
	private String litString;
	cppLiteral(GNode n)
	{
		foundLiteral=false;
		lString = new StringBuilder();
		count=0;
		location=1;
		visit(n);
		
	}//end of cppLiteral Constructor
	cppLiteral(GNode n, int value)
	{
		foundLiteral=false;
		lString = new StringBuilder();
		location=value;
		count=0;
		visit(n);
		
	}//end of cppLiteral Constructor	
	public StringBuilder getString()
	{
		return lString;
	}//end of getString
	public void getValue (GNode n)
	{
		//if(DEBUG){System.out.println("cppLiteral: "+n.getName());}
	}
	public boolean checkBreak()
	{
		if(DEBUG){System.out.println("Count: "+count);}
		if(DEBUG){System.out.println("Location: "+location);}		
		if((foundLiteral) && (count==location))
		{
			if(DEBUG){System.out.println("String: "+litString);}
			lString.append(litString);
			return true;
		}
		else {
			return false;
		}

		
	}
	public void visitBooleanLiteral(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);		
		if(checkBreak()){return;}
			
		//cppExpression cppEx = new cppExpression(n);
	}
	public void visitIntegerLiteral(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);	
		if(checkBreak()){return;}		
		//cppExpression cppEx = new cppExpression(n);
	}
	public void visitStringLiteral(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);	
		if(checkBreak()){return;}		
		//cppExpression cppEx = new cppExpression(n);
	}	
	public void visitPrimaryIdentifier(GNode n)
	{
		if(DEBUG){System.out.println(n.getString(0));}
		foundLiteral=true;
		count++;
		litString=n.getString(0);	
		if(checkBreak()){return;}		
	}	
	public void visit(Node n) {
		//count++;
		for (Object o : n){
			//count++;
			//Node test= n.getNode(0);
			
			//if(DEBUG){System.out.println(test.getName());}
			//if(DEBUG){System.out.println("Count: "+count);}
			//if(DEBUG){System.out.println("Location: "+location);}
			/*if((foundLiteral) && (count==location))
			{
				if(DEBUG){System.out.println("String: "+litString);}
				lString.append(litString);
				break;
			}*/
			 if (o instanceof Node) dispatch((Node)o);
		}
		
	} //end of visit method	
	/*public void interface Visitor<R, E extends Throwable> {
		
		R visitLiteral(GNode n) throws E;
		R visitAdditionExpression(GNode n) throws E;
		R visitMultiplicativeExpression(GNode n) throws E;
		
	}*/	
	
	
	/*
	 For Declarations: 
		-StringLiteral (literally ="thisString")
		BooleanLiteral
		-IntegerLiteral
		-FloatingPointLiteral
		-CharacterLiteral
		NullLiteral
		Literal?
		MultiplicativeExpression -Even for Division (just changes sign in the middle)
			IntergerLiteral(4) 
			*
			IntegerLiteral(3)
		AdditiveExpression
			IntegerLiteral(4)
			+
			IntergerLiteral(3)
		AdditiveExpression - even for Subtraction (just chages sign in the middle
			PrimaryIdentifier("additionTest")
			+
			PrimaryIdentifier("multiplicationTest")
		AdditiveExpression
			IntergerLiteral(12)
			+
			MultiplicativeExpression
				IntegerLiteral(3)
			/
			IntegerLiteral(2)
		AdditiveExpression
			StringLiteral("te")
			+
			StringLiteral("xt")
		 
		 //create method that takes the GNode of the expression
			//make a visitor for the literals (base)
			//make a visitor for Expression
	 */
	
}//end of cppLiteral class
class cppExpression extends Visitor{
	public final boolean DEBUG = false;
	private StringBuilder eString;
	cppExpression(GNode n)
	{
		eString=new StringBuilder();
		visit(n);
	}
	public void visitAdditiveExpression(GNode n)
	{
		//if(DEBUG){System.out.println("+ Expression");};
		//cppLiteral cppLit =new cppLiteral(n);
		cppLiteral cppLit =new cppLiteral(n);	
		eString.append(cppLit.getString());
		
		if(DEBUG){System.out.println(cppLit.getString());};
		
		cppExpression cppExp=new cppExpression(n);
		
		if(DEBUG){System.out.println(n.getString(1));}
		eString.append(n.getString(1));
		
		cppLiteral cppLit2 =new cppLiteral(n,2);
		eString.append(cppLit2.getString());
		if(DEBUG){System.out.println(cppLit2.getString());};
		
		cppExpression cppExp2=new cppExpression(n);	
		eString.append(cppExp2.getString());	
	}
	public void visitMultiplicativeExpression(GNode n)
	{
		//if(DEBUG){System.out.println("/ Expression");};
		
		cppLiteral cppLit =new cppLiteral(n);	
		eString.append(cppLit.getString());
		
		if(DEBUG){System.out.println(cppLit.getString());};
		
		cppExpression cppExp=new cppExpression(n);
		
		if(DEBUG){System.out.println(n.getString(1));}
		eString.append(n.getString(1));
		
		cppLiteral cppLit2 =new cppLiteral(n,2);
		eString.append(cppLit2.getString());
		if(DEBUG){System.out.println(cppLit2.getString());};
		
		cppExpression cppExp2=new cppExpression(n);	
		eString.append(cppExp2.getString());	
	}	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	}	
	
	public StringBuilder getString()
	{
		return eString;
	}//end of getString Method
}//end of cppExpression Class
class cppType extends Visitor{

	public final boolean DEBUG=false;
	private StringBuilder typeString;
	cppType(GNode n)
	{
		typeString=new StringBuilder();
		visit(n);
		
	}//end of cppType Constructior
	public void visitType(GNode n) {
		
		
		typeString.append(getPrimitiveType(n));
		typeString.append(getQualIden(n));		
		if(DEBUG){System.out.println("TYPEString!:"+ typeString);};
		
		
	}//end of visitClassDeclaration Method
	
	public StringBuilder getString()
	{
		//StringBuilder s;
		//s.append(getPrimitiveType(n));
		//s.append(getQualIden(n));
		return typeString;
	}
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public StringBuilder getPrimitiveType(GNode n)
	{
		cppPrimitiveType pType = new cppPrimitiveType(n);
		return pType.getPrimString(n);
	}
	public StringBuilder getQualIden(GNode n)
	{
			cppQualifiedIde qi= new cppQualifiedIde(n);
			return qi.getString();
	}
	public StringBuilder getType()
	{
		return typeString;
	}
	
	/******** DO SOMETHING HERE FOR OTHER TYPES   *********/
}//end of cppType class
class cppQualifiedIde extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder qString;
	cppQualifiedIde(GNode n)
	{
		qString=new StringBuilder();
		visit(n);
	}
	public void visitQualifiedIdentifier(GNode n) {
		if(DEBUG){System.out.println("Q I");};
		
		qString.append(n.getString(0));
		
	}//end of visitClassDeclaration Method
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	public StringBuilder getString()
	{
		return qString;
	}
}//end of cppQualifiedIde class
class cppPrimitiveType extends Visitor{
	public final boolean DEBUG=false;
	private StringBuilder primitiveTypeString;
	cppPrimitiveType(GNode n)
	{
		primitiveTypeString=new StringBuilder();
		visit(n);
		
	}//end of cppType Constructior
	public void visitPrimitiveType(GNode n) {
		if(DEBUG){System.out.println("Prim TYPE");};
		
		primitiveTypeString.append(n.getString(0));
		
	}//end of visitClassDeclaration Method
	
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public StringBuilder getPrimString(GNode n)
	{
		return primitiveTypeString;
	}
	
}
class cppModifier extends Visitor{
	
	public final boolean DEBUG=false;
	private StringBuilder modifierString;
	
	cppModifier(GNode n)
	{
		if(DEBUG){System.out.println("IN CppModifier");}
		modifierString=new StringBuilder();
		visit(n);
	}//end of cppModifier Constructor
	
	public void visitModifiers(GNode n) {
		cppSubModifier submod=new cppSubModifier(n);
		modifierString.append(submod.getSubModifierString());
	}//end of visitClassDeclaration Method
	
	
	public void visit(Node n) {
		//if(DEBUG){System.out.println(n.getName());}
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public StringBuilder getString()
	{
		return modifierString;
	}
	/*public void setModifierName(GNode n)
	{	//StringBuilder modifiers = new StringBuilder();
		if(DEBUG){System.out.println(n.getName());}
		for(int i=0; i<n.size();i++)
		{
			modifierString.append(n.getString(i));
		}
		//return modifiers;
	}*/
	
}//end of cppModifier class
/***NOTTTTTT WORKINGGGGGGGG :( ****/
class cppSubModifier extends Visitor{
	
	public final boolean DEBUG=false;
	private StringBuilder subModifierString;
	
	cppSubModifier(GNode n)
	{
		//if(DEBUG){System.out.println("IN CppModifier");}
		subModifierString=new StringBuilder();
		visit(n);
	}//end of cppModifier Constructor
	
	public void visitModifier(GNode n) {
		setSubModifierName(n);
	}//end of visitClassDeclaration Method
	
	
	public void visit(Node n) {
		if(DEBUG){System.out.println(n.getName());}
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	} //end of visit method
	
	public StringBuilder getSubModifierString()
	{
		return subModifierString;
	}
	
	public void setSubModifierName(GNode n){
		if(DEBUG){System.out.println(n.getName());}
		for(int i=0; i<n.size();i++)
		{
			subModifierString.append(n.getString(i));
		}
	}
	
}//end of cppsubModifier class