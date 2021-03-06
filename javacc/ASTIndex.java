
/* Generated By:JJTree: Do not edit this line. javacc.ASTIndex.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTIndex extends SimpleNode {
  private boolean already_calculated_return;

  public ASTIndex(int id) {
    super(id);
    return_type = "int";
  }

  public ASTIndex(Javamm p, int id) {
    super(p, id);
    return_type = "int";
  }

  @Override
  public void checkNodeSemantics(SymbolTable symbol_table) {
    if(this.jjtGetNumChildren() == 1) {
      SimpleNode child = (SimpleNode) this.jjtGetChild(0);

      if (!child.isBinaryOperator()) {
        if (!((child.getIdentity() == "." && child.getReturnType(symbol_table) == "int") ||
                child.getIdentity() == "ArrayAccess" ||
                child.getType() == "int" ||
                symbol_table.checkVariableType(this.getScope(), child.getIdentity(), "int"))) {

          SemanticErrorHandler.getInstance().printError(this.getScope(),
                  "An index must be an integer or an expression that returns an integer.");
        }
      } else if (child.getReturnType() != "int") {
        SemanticErrorHandler.getInstance().printError(this.getScope(),
                "An index must be an integer or an expression that returns an integer.");
      }
    }
  }

  @Override
  public String toString(){
    return "Index";
  }

}
/* JavaCC - OriginalChecksum=b7796a22346b260640cf30c42b33fc6d (do not edit this line) */
