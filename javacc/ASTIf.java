/* Generated By:JJTree: Do not edit this line. ASTIf.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTIf extends SimpleNode {
  private static int counter = 0;
  private int count;

  public ASTIf(int id) {
    super(id);
    this.count = counter;
    counter++;
  }

  public ASTIf(Javamm p, int id) {
    super(p, id);
    this.count = counter;
    counter++;
  }

  @Override
  public int getCount() {
    return this.count;
  }

  @Override
  public String toString() {
    return "Statement if";
  }

  @Override
  protected void checkNodeSemantics(SymbolTable symbol_table) {
    SimpleNode child = (SimpleNode) this.jjtGetChild(0);

    if(child.getReturnType(symbol_table) != "boolean"){
      SemanticErrorHandler.getInstance().printError(this.getScope(), "Condition for if statement must return a boolean");
    }

  }
}
/* JavaCC - OriginalChecksum=04ee7893447596dd3d1759e2d7db89ef (do not edit this line) */
