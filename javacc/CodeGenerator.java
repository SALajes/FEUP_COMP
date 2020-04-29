import com.sun.nio.sctp.AbstractNotificationHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class CodeGenerator {
    private SymbolTable symbolTable;

    private SimpleNode root;
    private SimpleNode classNode;

    private PrintWriter printWriter;

    private String scope;

    public CodeGenerator(SimpleNode root, SymbolTable symbolTable) {
        this.root = root;
        this.symbolTable = symbolTable;
    }

    public void generateCode() {
        int numberImports = getNumImports();
        this.classNode = (SimpleNode) this.root.jjtGetChild(numberImports);

        createFile();

        writeClass();
        writeGlobalVars();

        writeInitializer();

        writeMethods();

        printWriter.close();
    }

    private int getNumImports() {
        int i = 0;

        do {
            if (this.root.jjtGetChild(i) instanceof ASTImport)
                i++;

        } while (!(this.root.jjtGetChild(i) instanceof ASTClass));

        return i;
    }

    private void createFile() {
        String fileName = this.classNode.getIdentity() + ".j";

        new File("Jasmin").mkdirs();

        File file = new File("Jasmin/" + fileName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.printWriter = new PrintWriter(fileWriter);
    }

    private void writeClass() {
        this.printWriter.printf(".class public %s\n", this.classNode.getIdentity());
        if(this.classNode.getExtend() != null)
            this.printWriter.printf(".super %s\n\n", this.classNode.getExtend());
        else
            this.printWriter.print(".super java/lang/Object\n\n");
    }

    private void writeGlobalVars() {
        // TODO: Verificar se o nome das variaveis não é um nome reservado em Jasmin
        for (int i = 0; i < this.classNode.jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) this.classNode.jjtGetChild(i);
            if(node instanceof ASTMethod || node instanceof ASTMain)
                break;

            this.printWriter.printf(".fileld %s %s\n", node.getIdentity(), convertType(node.getType()));
        }
        this.printWriter.printf("\n");
    }

    private void writeInitializer() {
        this.printWriter.print(".method public <init> ()V\n");
        this.printWriter.print("\taload_0\n");
        if(this.classNode.getExtend() != null)
            this.printWriter.printf("\tinvokespecial " + this.classNode.getExtend()+"\n");
        else
            this.printWriter.print("\tinvokenonvirtual java/lang/Object/<ini>()V\n");
        this.printWriter.print("\treturn\n");
        this.printWriter.print(".end method\n\n");
    }

    private void writeMethods() {
        for (int i = 0; i < this.classNode.jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) this.classNode.jjtGetChild(i);

            if(node instanceof ASTMethod || node instanceof ASTMain)
                writeMethod(node);
        }
    }

    private void writeMethod(SimpleNode node) {
        String args = genArgs(node);

        this.printWriter.printf(".method public %s(%s) %s\n", node.getIdentity().equals("main") ? "static main" : node.getIdentity(), args, convertType(node.getReturnType()));
        this.scope = node.getIdentity();
        writeMethodBody(node);

        this.printWriter.printf(".end method\n\n");
    }

    private String genArgs(SimpleNode node) {
        if(node instanceof ASTMain)
            return convertType("String[]");

        String ret = "";
        int i = 0;
        while (node.jjtGetChild(i) instanceof ASTArgument) {
            SimpleNode arg = (SimpleNode) node.jjtGetChild(i);

            ret += convertType(arg.getType());

            i++;
        }

        return ret;
    }

    private void writeMethodBody(SimpleNode node) {
        this.printWriter.printf("\t.limit stack 99\n");                         // TODO: Calcular

        Method method = this.symbolTable.getMethod(node.getIdentity());
        int numLocals = method.getNumLocalVars() + method.getNumParameters();
        this.printWriter.printf("\t.limit locals %d\n\n", numLocals);           // NumLocalVars + NumParameters

        /*for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            this.printWriter.printf("\tId: %d\n", node.jjtGetChild(i).getId());
            for (int j = 0; j < node.jjtGetChild(i).jjtGetNumChildren(); j++) {
                this.printWriter.printf("\t\tId: %d\n", node.jjtGetChild(i).jjtGetChild(j).getId());
            }
        }*/

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if(node.jjtGetChild(i) instanceof ASTArgument)
                continue;

            if(node.jjtGetChild(i) instanceof ASTVarDeclaration)
                // writeVarDeclaration(node.jjtGetChild(i);
                continue;

            if(node.jjtGetChild(i) != null)
                writeStatement((SimpleNode) node.jjtGetChild(i));
        }

        this.printWriter.printf("\t%s\n", convertReturnType(node.getReturnType()));
    }

    private void writeVarDeclaration(SimpleNode node) {
        switch (node.getType()) {
            case "int":
            case "int[]":
            case "boolean":
        }
    }

    private void writeStatement(SimpleNode node) {
        switch (node.getId()) {
            case JavammTreeConstants.JJTIF:
                this.printWriter.printf("\t;If Statement\n");
//                writeIfStatement(node);
                break;

            case JavammTreeConstants.JJTWHILE:
                this.printWriter.printf("\t;While Statement\n");
//                writeWhileStatement(node);
                break;

            case JavammTreeConstants.JJTASSIGNEMENT:
                this.printWriter.printf("\t;Assigment Statement\n");
                break;

            default:
                this.printWriter.printf("\t;Expression Id: %d\n", node.getId());
                writeExpression(node);
                break;
        }

        this.printWriter.printf("\n");
    }

    private void writeIfStatement(SimpleNode node) {
        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        SimpleNode body = (SimpleNode) node.jjtGetChild(1);
        SimpleNode then = node.jjtGetNumChildren() == 3 ? (SimpleNode) node.jjtGetChild(2) : null;

        /*
        this.printWriter.printf("\t\tCondition: %d\n", condition.getId());
        for (int i = 0; i < body.jjtGetNumChildren(); i++)
            this.printWriter.printf("\t\t\t Id: %d\n", body.jjtGetChild(i).getId());
        if(then != null) {
            this.printWriter.printf("\t\tThen: \n");
            for (int i = 0; i < then.jjtGetNumChildren(); i++)
                this.printWriter.printf("\t\t\t Id: %d\n", then.jjtGetChild(i).getId());
            this.printWriter.printf("\n");
        }
        */

        writeExpression(condition);

        for (int i = 0; i < body.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) body.jjtGetChild(i));

        if(then != null) {
            for (int i = 0; i < then.jjtGetNumChildren(); i++)
                writeStatement((SimpleNode) then.jjtGetChild(i));
        }
    }

    private void writeWhileStatement(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            this.printWriter.printf("\t\tId: %d\n", node.jjtGetChild(i).getId());
        }
    }

    private void writeExpression(SimpleNode node) {
        switch (node.getId()) {
            case JavammTreeConstants.JJTAND:
                this.printWriter.printf("\t;And\n");
                break;

            case JavammTreeConstants.JJTLESSTHAN:
                this.printWriter.printf("\t;Less Than\n");
                break;

            case JavammTreeConstants.JJTMULTIPLICATIONDIVISION:
            case JavammTreeConstants.JJTADDITIONSUBTRACTION:
                writeArithmetic(node);
                break;

            case JavammTreeConstants.JJTNOT:
                this.printWriter.printf("\t;Not\n");
                break;

            case JavammTreeConstants.JJTDOT:
                this.printWriter.printf("\t;Dot\n");
                writeDot(node);
                break;

            case JavammTreeConstants.JJTINTEGER:
                this.printWriter.printf("\t;Integer\n");
                break;

            case JavammTreeConstants.JJTTRUE:
                this.printWriter.printf("\t;True\n");
                break;

            case JavammTreeConstants.JJTFALSE:
                this.printWriter.printf("\t;False\n");
                break;

            case JavammTreeConstants.JJTTHIS:
                this.printWriter.printf("\t;This\n");
                break;

            case JavammTreeConstants.JJTID:
                this.printWriter.printf("\t;ID\n");
                writeID(node);
                break;

            case JavammTreeConstants.JJTARRAYACCESS:
                this.printWriter.printf("\t;Array Access\n");
                break;

            case JavammTreeConstants.JJTEXPRESSIONDOT:
                this.printWriter.printf("\t;Expression Dot\n");
                break;

            case JavammTreeConstants.JJTEXPRESSIONNEW:
                this.printWriter.printf("\t;Expression New\n");
                break;

            default:
                this.printWriter.printf(";EXPRESSION DEFAULT: %d\n", node.getId());
                break;
        }
    }

    private void writeArithmetic(SimpleNode node){
        String op = node.getOperator();

        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        writeExpression(left);
        writeExpression(right);

        switch (op) {
            case "+":
                this.printWriter.printf("\tiadd\n");
                break;

            case "-":
                this.printWriter.printf("\tisub\n");
                break;

            case "*":
                this.printWriter.printf("\timul\n");
                break;

            case "/":
                this.printWriter.printf("\tidiv\n");
                break;
        }
    }

    private void writeDot(SimpleNode node){
        /*for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            this.printWriter.printf("\tId: %d\n", node.jjtGetChild(i).getId());
            for (int j = 0; j < node.jjtGetChild(i).jjtGetNumChildren(); j++) {
                this.printWriter.printf("\t\t Id: %d\n", node.jjtGetChild(i).jjtGetChild(j).getId());
            }
        }*/
        // All nodes Dot have 2 children, guaranteed

        if(node.jjtGetChild(0) instanceof ASTThis) {
            this.printWriter.printf("\t;this\n");
//            this.printWriter.printf("\taload_0\n");
//            writeExpression((SimpleNode) node.jjtGetChild(1));
            return;
        }

        if(node.jjtGetChild(0) instanceof ASTID && node.jjtGetChild(1) instanceof ASTExpressionDot) {
            String left = ((ASTID) node.jjtGetChild(0)).getIdentity();
            String right= ((ASTExpressionDot) node.jjtGetChild(1)).getIdentity();

            if(this.symbolTable.checkImportMethod(left, right)){
                this.printWriter.printf("\t;import method\n");
                // Explorar no da direita se der
                // invoke
            } else {
                this.printWriter.printf("\t;local method\n");
                // Explorar no da direita se der
                // invoke
            }
            return;
        }
    }

    private void writeID(SimpleNode node){
        String varName = node.getIdentity();

        if(this.symbolTable.getMethod(this.scope).checkVariable(varName))
            this.printWriter.printf("\t;Local Var\n");
        else if (this.symbolTable.globalVariableExists(varName)) {
            this.printWriter.printf("\taload_0\n");
            this.printWriter.printf("\tgetfield %s/%s %s\n", this.classNode.getIdentity(), varName, convertType(this.symbolTable.getGlobalVariable(varName).getType()));
        }
    }

    // -- Convert types and return --
    private String convertType(String type) {
        switch (type) {
            case "int":
                return "I";

            case "int[]":
                return "[I";

            case "boolean":
                return "Z";

            case "void":
            case "":
                return "V";

            case "String[]":
                return "[Ljava/lang/String;";

            default:
                return "L" + type +";";
        }
    }

    private String convertReturnType(String returnType) {
        switch (returnType) {
            case "int":
            case "boolean":
                return "ireturn";

            case "int[]":
                return "areturn";

            case "void":
            case "":
            default:
                return "return";
        }
    }
}