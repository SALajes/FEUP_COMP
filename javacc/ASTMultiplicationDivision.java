import javacc.SemanticErrorHandler;

/* Generated By:JJTree: Do not edit this line. javacc.ASTMultiplicationDivision.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTMultiplicationDivision extends SimpleNode {
  protected String operator;

  public ASTMultiplicationDivision(int id) {
    super(id);
    this.return_type = "int";
    this.binary_operator=true;
  }

  public ASTMultiplicationDivision(Javamm p, int id) {
    super(p, id);
    this.return_type = "int";
    this.binary_operator=true;
  }

  public String getOperator(){
    return this.operator;
  }

  public void setOperator(String operator){
    this.operator = operator;
  }

  @Override
  public String toString(){
    return this.operator;
  }

  @Override
  protected void checkNodeSemantics(SymbolTable symbol_table) {
    if(this.jjtGetNumChildren() == 2) {
      SimpleNode left_child = (SimpleNode) this.jjtGetChild(0);
      SimpleNode right_child = (SimpleNode) this.jjtGetChild(1);

      if(!left_child.isBinaryOperator()){
        if(!(left_child.getType() == "int" || symbol_table.checkVariable(this.getScope(), left_child.getIdentity(), "int"))){
          SemanticErrorHandler.getInstance().printError(this.getScope(), "Left child of operator " + operator + " has to be an integer or a integer variable");
        }
      }
      if(!right_child.isBinaryOperator()){
        if(!(right_child.getType() == "int" || symbol_table.checkVariable(this.getScope(), right_child.getIdentity(), "int"))){
          SemanticErrorHandler.getInstance().printError(this.getScope(), "Right child of operator " + operator + " has to be an integer or a integer variable");
        }
      }
    }
  }
}
/* JavaCC - OriginalChecksum=fe073846e3de52a6d7fa39761df70aab (do not edit this line) */
