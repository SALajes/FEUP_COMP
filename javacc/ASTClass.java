/* Generated By:JJTree: Do not edit this line. ASTClass.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTClass extends SimpleNode {

  public ASTClass(int id) {
    super(id);
  }

  public ASTClass(Javamm p, int id) {
    super(p, id);
  }

  public void setClassName(String className) {
    this.identity = className;
  }

  public void setExt(String ext) {
    this.extend = ext;
  }

  public String toString() {
    String ret = "Class " + this.identity;

    if(this.extend != null)
      ret += " extends " + this.extend;

    return ret;
  }
}
/* JavaCC - OriginalChecksum=67de14fafdec5c778c9a10a8762ed7e6 (do not edit this line) */
