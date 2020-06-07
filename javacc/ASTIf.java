/* Generated By:JJTree: Do not edit this line. ASTIf.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
import java.util.*;

public
class ASTIf extends SimpleNode {
  private static int counter = 0;
  private int count;
  private Set<String> initialized_variables;
  private boolean checked;

  public ASTIf(int id) {
    super(id);
    this.count = counter;
    counter++;
    this.initialized_variables = null;
    this.checked = false;
  }

  public ASTIf(Javamm p, int id) {
    super(p, id);
    this.count = counter;
    counter++;
    this.initialized_variables = null;
    this.checked = false;
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

   if(checked)
      return;

    this.initialized_variables = checkInitializedVariables(symbol_table);

    for(String identifier : this.initialized_variables){
      symbol_table.initializeVariable(identifier, this.getScope());
    }
  }

  public Set<String> checkInitializedVariables(SymbolTable symbol_table){
    this.initialized_variables = new HashSet<String>();

    if(this.jjtGetParent() instanceof ASTWhile)
      return this.initialized_variables;

    SimpleNode _then = (SimpleNode) this.jjtGetChild(1);
    SimpleNode _else = (SimpleNode) this.jjtGetChild(2);

    Set<String> result = checkInitializedVariables(_then, symbol_table);
    result.retainAll(checkInitializedVariables(_else, symbol_table));

    this.checked = true;

    return result;
  }

  private Set<String> checkInitializedVariables(SimpleNode node, SymbolTable symbol_table){
    Set<String> result = new HashSet<String>();

    for(int i = 0; i < node.jjtGetNumChildren(); i++){
      SimpleNode statement = (SimpleNode) node.jjtGetChild(i);

      if(statement instanceof ASTIf){
        Set<String> aux = ((ASTIf) statement).checkInitializedVariables(symbol_table);
        result.addAll(aux);
      }
      else if(statement instanceof ASTAssignement){
        SimpleNode left_child = (SimpleNode) statement.jjtGetChild(0);

        if(symbol_table.doesVariableExist(this.getScope(), left_child.identity)
            && !symbol_table.isVariableInitialized(left_child.identity , this.getScope())){
          result.add(left_child.identity);
        }
      }
    }

    return result;
  }
}
/* JavaCC - OriginalChecksum=04ee7893447596dd3d1759e2d7db89ef (do not edit this line) */
