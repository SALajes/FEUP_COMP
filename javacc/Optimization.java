import java.io.*;
import java.util.*;

public class Optimization {
    private SymbolTable symbol_table;
    private SimpleNode root;
    private SimpleNode classNode;
    private String scope;
    private Method method;
    private boolean so_a_bere;

    public Optimization(SymbolTable symbol_table, SimpleNode root){
        this.symbol_table = symbol_table;
        this.root = root;
        this.classNode = (SimpleNode) this.root.jjtGetChild(getNumImports());
        this.scope = this.classNode.getIdentity();
        this.so_a_bere = false;
    }

    public void constantPropagation(){
        for (int i = 0; i < this.classNode.jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) this.classNode.jjtGetChild(i);

            if(node instanceof ASTMethod || node instanceof ASTMain) {
                checkMethod(node);
            }
        }
    }

    private int getNumImports() {
        int i = 0;

        do {
            if (this.root.jjtGetChild(i) instanceof ASTImport)
                i++;
        } while (!(this.root.jjtGetChild(i) instanceof ASTClass));

        return i;
    }

    private void checkMethod(SimpleNode node){
        getMethod(node);

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if(node.jjtGetChild(i) instanceof ASTArgument || node.jjtGetChild(i) instanceof ASTVarDeclaration)
                continue;

            checkStatement((SimpleNode) node.jjtGetChild(i));
        }
    }

    private void getMethod(SimpleNode node) {
        this.scope = node.getIdentity();

        if(node instanceof ASTMain) {
            this.method = this.symbol_table.getMethod(node.getIdentity());
        }

        ArrayList<String> argTypes = new ArrayList<>();
        int i = 0;

        while (node.jjtGetChild(i) instanceof ASTArgument) {
            SimpleNode arg = (SimpleNode) node.jjtGetChild(i);

            argTypes.add(arg.getType());

            i++;
        }

        this.method = this.symbol_table.getMethod(node.getIdentity(), argTypes);
    }

    private void checkStatement(SimpleNode node) {
        switch (node.getId()) {
            case JavammTreeConstants.JJTIF:
                checkIfStatement(node);
                break;

            case JavammTreeConstants.JJTWHILE:
                checkWhileStatement(node);
                break;

            case JavammTreeConstants.JJTASSIGNEMENT:
                checkAssignStatement(node);
                break;

            default:
                checkExpression(node);
                break;
        }
    }

    private void checkIfStatement(SimpleNode node){
        String previous_scope = this.scope;
        this.scope = "IF";

        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        SimpleNode body = (SimpleNode) node.jjtGetChild(1);
        SimpleNode then = (SimpleNode) node.jjtGetChild(2);

        checkExpression(condition);

        for (int i = 0; i < body.jjtGetNumChildren(); i++)
            checkStatement((SimpleNode) body.jjtGetChild(i));

        for (int i = 0; i < then.jjtGetNumChildren(); i++)
            checkStatement((SimpleNode) then.jjtGetChild(i));

        this.scope = previous_scope;
    }

    private void checkWhileStatement(SimpleNode node) {
        String previous_scope = this.scope;
        this.scope = "WHILE";
        this.so_a_bere = true;

        //Verify if local variables are assigned inside WHILE's body
        for (int i = 1; i < node.jjtGetNumChildren(); i++)
            checkStatement((SimpleNode) node.jjtGetChild(i));

        //Now it will iterate, again, in order to define which nodes can have constant propagation
        this.so_a_bere = false;

        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        checkExpression(condition);

        for (int i = 1; i < node.jjtGetNumChildren(); i++)
            checkStatement((SimpleNode) node.jjtGetChild(i));

        this.scope = previous_scope;
    }

    private void checkAssignStatement(SimpleNode node) {
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        if(left instanceof ASTArrayInit) {
            checkID(left);                                              // Var name
            checkExpression((SimpleNode) left.jjtGetChild(0));          // Index
            checkExpression(right);
            return;
        }

        String return_value = checkExpression(right);

        String varName = left.getIdentity();

        if(this.method.localVariableExists(varName)) {
            if(return_value != null && !this.scope.equals("IF") && !this.scope.equals("WHILE")){
                Symbol var = this.method.getVariable(varName);

                switch (var.getType()) {
                    case "int":
                    case "boolean":
                        this.method.setConstant(varName, return_value);
                        break;
                    default:
                        break;
                }
            }
            else{
                this.method.removeConstant(varName);
            }
        }
    }

    private String checkExpression(SimpleNode node) {
        switch (node.getId()) {
            case JavammTreeConstants.JJTLESSTHAN:
                return checkLessThanCondition(node);

            case JavammTreeConstants.JJTAND:
                return checkAndCondition(node);

            case JavammTreeConstants.JJTMULTIPLICATIONDIVISION:
            case JavammTreeConstants.JJTADDITIONSUBTRACTION:
                return checkArithmetic(node);

            case JavammTreeConstants.JJTNOT:
                return checkNot(node);

            case JavammTreeConstants.JJTDOT:
                checkDot(node);
                return null;

            case JavammTreeConstants.JJTARRAYACCESS:
                checkArrayAccess(node);
                return null;

            case JavammTreeConstants.JJTEXPRESSIONNEW:
                checkNew(node);
                return null;

            case JavammTreeConstants.JJTFUNCTIONCALLARGUMENTS:
                checkFunctionCallArguments(node);
                return null;

            case JavammTreeConstants.JJTINTEGER:
                return node.getIdentity();

            case JavammTreeConstants.JJTTRUE:
                return "true";

            case JavammTreeConstants.JJTFALSE:
                return "false";

            case JavammTreeConstants.JJTINDEX:
            case JavammTreeConstants.JJTRETURN:
                return checkExpression((SimpleNode) node.jjtGetChild(0));

            case JavammTreeConstants.JJTID:
                return checkID(node);

            default:
                return null;
        }
    }

    private String checkLessThanCondition(SimpleNode node) {
        String left = checkExpression((SimpleNode) node.jjtGetChild(0));
        String right = checkExpression((SimpleNode) node.jjtGetChild(1));

        if(left == null || right == null)
            return null;

        if(left.compareTo(right) < 0){
            return "true";
        }
        else return "false";
    }

    private String checkAndCondition(SimpleNode node) {
        String left = checkExpression((SimpleNode) node.jjtGetChild(0));

        String right = checkExpression((SimpleNode) node.jjtGetChild(1));

        if(left == null || right == null)
            return null;
        else if(left.equals("true") && right.equals("true"))
            return "true";
        else return "false";
    }

    private String checkArithmetic(SimpleNode node){
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        String value1 = checkExpression(left);
        String value2 = checkExpression(right);

        if(value1 == null || value2 == null)
            return null;

        int var1 = Integer.parseInt(value1);
        int var2 = Integer.parseInt(value2);

        int result;

        switch (node.getOperator()) {
            case "+":
                result = var1 + var2;
                return "" + result;

            case "-":
                result = var1 - var2;
                return "" + result;

            case "*":
                result = var1 * var2;
                return "" + result;

            case "/":
                result = var1 / var2;
                return "" + result;

            default:
                return null;
        }
    }

    private String checkNot(SimpleNode node) {
        String child = checkExpression((SimpleNode) node.jjtGetChild(0));

        if(child == null)
            return null;
        else if(child.equals("true"))
            return "false";
        else return "true";
    }

    private void checkFunctionCallArguments(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            checkExpression((SimpleNode) node.jjtGetChild(i));
        }
    }

    private void checkDot(SimpleNode node){
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        if(right.getIdentity().equals("length")) {
            return;
        }

        checkExpression((SimpleNode) right.jjtGetChild(0));
    }

    private void checkArrayAccess(SimpleNode node) {
        checkID((SimpleNode) node.jjtGetChild(0));              // Var name
        checkExpression((SimpleNode) node.jjtGetChild(1));      // Index
    }

    private void checkNew(SimpleNode node) {
        if(node.jjtGetNumChildren() > 0){
            checkExpression((SimpleNode) node.jjtGetChild(0));
        }
    }

    private String checkID(SimpleNode node){
        String varName = node.getIdentity();

        if(this.method.localVariableExists(varName) && this.method.isConstant(varName) && !this.so_a_bere) {
            Symbol var = this.method.getVariable(varName);
            String value = this.method.getConstantValue(varName);

            switch (var.getType()) {
                case "int":
                    node.setId(23);
                    node.setReturnType("int");
                    node.setIdentity(value);
                    return value;
                case "boolean":
                    node.setReturnType("boolean");
                    if(value.equals("true"))
                        node.setId(24);
                    else node.setId(25);
                    return value;
                default:
                    break;
            }

        }

        return null;
    }
}