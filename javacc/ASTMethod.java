/* Generated By:JJTree: Do not edit this line. ASTMethod.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTMethod extends SimpleNode {
  public ASTMethod(int id) {
    super(id);
  }

  public ASTMethod(Javamm p, int id) {
    super(p, id);
  }

  public String toString() {
    return "Method " + identity + " " + return_type;
  }
}
/* JavaCC - OriginalChecksum=959391f8f71ac4092a93282428213e76 (do not edit this line) */
