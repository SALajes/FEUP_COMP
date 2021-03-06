import java.util.ArrayList;

/* Generated By:JJTree: Do not edit this line. ASTFunctionCallArguments.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTFunctionCallArguments extends SimpleNode {
  private ArrayList<String> arguments;
  private  boolean already_calculated_return;

  public ASTFunctionCallArguments(int id) {
    super(id);
    already_calculated_return = false;
    arguments = new ArrayList<>();
  }

  public ASTFunctionCallArguments(Javamm p, int id) {
    super(p, id);
    already_calculated_return = false;
    arguments = new ArrayList<>();
  }

  @Override
  public ArrayList<String> getParameters(SymbolTable symbol_table){
    if(already_calculated_return == false){
      setParameters(symbol_table);
    }
    return arguments;
  }

  private void setParameters(SymbolTable symbol_table) {
    for(int i = 0; i < this.jjtGetNumChildren(); i++){
      SimpleNode child = (SimpleNode) this.jjtGetChild(i);
      arguments.add(child.getReturnType(symbol_table));
    }
    already_calculated_return=true;
  }
}
/* JavaCC - OriginalChecksum=8f8c939a6cefa37f69efdba8086ca6bb (do not edit this line) */
