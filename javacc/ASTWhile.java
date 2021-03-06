/* Generated By:JJTree: Do not edit this line. ASTWhile.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTWhile extends SimpleNode {
  private static int counter = 0;
  private int count;

  public ASTWhile(int id) {
    super(id);
    this.count = counter;
    counter++;
  }

  public ASTWhile(Javamm p, int id) {
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
    return "Statement while";
  }

  @Override
  protected void checkNodeSemantics(SymbolTable symbol_table) {
    SimpleNode child = (SimpleNode) this.jjtGetChild(0);

    if(child.getReturnType(symbol_table) != "boolean"){
      SemanticErrorHandler.getInstance().printError(this.getScope(), "Condition for while statement must return a boolean");
    }

  }
}
/* JavaCC - OriginalChecksum=2706f5e4eb3caeff77af14b38670635b (do not edit this line) */
