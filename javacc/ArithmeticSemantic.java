public final class ArithmeticSemantic {
    private ArithmeticSemantic(){}

    public static void checkNodeSemantics(SymbolTable symbol_table, SimpleNode node, String operator) {
        if(node.jjtGetNumChildren() == 2) {
            SimpleNode left_child = (SimpleNode) node.jjtGetChild(0);
            SimpleNode right_child = (SimpleNode) node.jjtGetChild(1);

            String code_fragment = left_child.toString() + operator + right_child.toString();

            checkChildConditions(left_child, "left", symbol_table, node.getScope(), operator, code_fragment);
            checkChildConditions(right_child, "right", symbol_table, node.getScope(), operator, code_fragment);
        }
    }

    private static void checkChildConditions(SimpleNode node, String child, SymbolTable symbol_table, String scope, String operator,  String code_fragment){
        if(!node.isBinaryOperator()){
            if(!((node.getIdentity()=="." && node.getReturnType(symbol_table) == "int") ||
                    node.getIdentity()=="ArrayAccess" ||
                    node.getType() == "int")){

                if(symbol_table.checkVariableType(scope, node.getIdentity(), "int")){//check if variable is an int
                   if(!symbol_table.isVariableInitialized(node.getIdentity(), node.getScope())) {//checks if variable is not initialized

                       SemanticErrorHandler.getInstance().printError(scope,
                               child + " child of operator " + operator + " is not initialized: " + node.getIdentity());
                   }
                   return;
                }
                printSemanticError(child, scope, operator, code_fragment);
            }
        }
        else if(node.getReturnType() != "int")
            printSemanticError(child, scope, operator, code_fragment);
    }

    private static void printSemanticWarning(String node, String scope, String operator, String identity) {
        SemanticErrorHandler.getInstance().printWarning(scope,
                node + " child of operator " + operator + " is not initialized: " + identity);
    }

    private static void printSemanticError(String node, String scope, String operator, String code_fragment) {
        SemanticErrorHandler.getInstance().printError(scope,
                node + " child of operator " + operator + " has to be an integer or return an integer",
                code_fragment);
    }
}
