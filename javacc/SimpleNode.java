
/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 7.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Javamm parser;

  protected String identity;
  protected String extend;
  protected String type;
  protected String return_type;

  protected boolean binary_operator = false;

  protected String scope;

  private static SymbolTable symbol_table;

  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(Javamm p, int i) {
    this(i);
    parser = p;
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() {
    return JavammTreeConstants.jjtNodeName[id];
  }

  public String toString(String prefix) {
      return prefix + toString();
  }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));

    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + "   ");
        }
      }
    }
  }

  public int getId() {
    return id;
  }

  //------ IMPLEMENTED CODE -----
  public String getIdentity(){
    return this.identity;
  }

  public boolean isBinaryOperator(){
    return this.binary_operator;
  }

  public String getScope(){
    if(scope == null)
      scope = ((SimpleNode) this.parent).getScope();

    return scope;
  }

  public void setScope(String scope){
    this.scope = scope;
  }

  public String getType(){
    return this.type;
  }

  public String getReturnType(){
    return this.return_type;
  }
  public String getReturnType(SymbolTable symbol_table){
    return this.return_type;
  }

  public void setReturnType(String return_type){
    this.return_type = return_type;
  }

  public void checkSemantics(SymbolTable symbol_table){
    if(scope == null)
      scope = ((SimpleNode) this.parent).scope;

    this.checkNodeSemantics(symbol_table);

    if(this.children != null) {
      for(Node node : this.children) {
        ((SimpleNode) node).checkSemantics(symbol_table);
      }
    }
  }

  protected void checkNodeSemantics(SymbolTable symbol_table) {}
}

/* JavaCC - OriginalChecksum=64dd3e3b2acf6bee9393de7b2d2f7b42 (do not edit this line) */
