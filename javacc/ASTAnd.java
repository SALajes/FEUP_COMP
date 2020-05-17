
/* Generated By:JJTree: Do not edit this line. javacc.ASTAnd.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTAnd extends SimpleNode {
  protected final String operator = "&&";

  public ASTAnd(int id) {
    super(id);
    this.return_type = "boolean";
    this.binary_operator=true;
  }

  public ASTAnd(Javamm p, int id) {
    super(p, id);
    this.return_type = "boolean";
    this.binary_operator=true;
  }

  @Override
  public String toString(){
    return "&&";
  }

  @Override
  public void checkNodeSemantics(SymbolTable symbol_table) {
    if(this.jjtGetNumChildren() == 2) {
      SimpleNode left_child = (SimpleNode) this.jjtGetChild(0);
      SimpleNode right_child = (SimpleNode) this.jjtGetChild(1);

      String code_fragment = left_child.toString() + operator + right_child.toString();

      checkChildConditions(left_child, "left", symbol_table, code_fragment);
      checkChildConditions(right_child, "right", symbol_table, code_fragment);
    }
  }

  private void checkChildConditions(SimpleNode node, String child, SymbolTable symbol_table, String code_fragment){
    if(!node.isBinaryOperator()){
      if(!((node.getIdentity()=="." && node.getReturnType(symbol_table) == "boolean") ||
              node.getReturnType() == "boolean" ||
              node.getType() == "boolean" ||
              symbol_table.checkVariableType(scope, node.getIdentity(), "boolean"))){
        printSemanticError(child, scope, operator, code_fragment);
      }
    }
    else if(node.getReturnType() != "boolean")
      printSemanticError(child, scope, operator, code_fragment);
  }

  private void printSemanticError(String node, String scope, String operator, String code_fragment) {
    SemanticErrorHandler.getInstance().printError(scope,
            node + " child of operator " + operator + " has to be an boolean or return an boolean",
            code_fragment);
  }
}
/* JavaCC - OriginalChecksum=237089df49022529c77790c4eb238346 (do not edit this line) */