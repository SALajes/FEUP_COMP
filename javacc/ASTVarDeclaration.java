import java.util.Arrays;
import java.util.List;

/* Generated By:JJTree: Do not edit this line. ASTVarDeclaration.java Version 6.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTVarDeclaration extends SimpleNode {
  public ASTVarDeclaration(int id) {
    super(id);
  }

  public ASTVarDeclaration(Javamm p, int id) {
    super(p, id);
  }

  public String toString() {
    return this.type + " " + this.identity;
  }

  @Override
  protected void checkNodeSemantics(SymbolTable symbol_table) {
    List<String> types = Arrays.asList(new String[]{"int", "boolean","int[]"});

    if(!( types.contains(this.type) || symbol_table.checkImportClass(this.type) || symbol_table.class_name.equals(this.type)))
    {
      SemanticErrorHandler.getInstance().printError(scope,
              this + " type of variable " + this.varName + " is not valid",
              this.toString());
    }
  }
}
/* JavaCC - OriginalChecksum=8f72e5c259da2033e8a451a9ce073fa3 (do not edit this line) */
