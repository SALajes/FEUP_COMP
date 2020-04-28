import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

class CodeGenerator {
    private SymbolTable symbolTable;

    private SimpleNode root;
    private SimpleNode classNode;

    private PrintWriter printWriter;

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
            this.printWriter.printf(".super %s\n", this.classNode.getExtend());

        this.printWriter.print(".super java/lang/Object\n\n");
    }

    private void writeGlobalVars() {
        Hashtable<String, Symbol> vars = this.symbolTable.getGlobal_variables();

        for (String key : vars.keySet()) {
            this.printWriter.printf(".fileld %s %s\n", key, convertType(vars.get(key).getType()));
        }

        if(!vars.isEmpty())
            this.printWriter.printf("\n");
    }

    private void writeInitializer() {
        this.printWriter.print(".method public <init> ()V\n");
        this.printWriter.print("\taload_0\n");
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
        this.printWriter.printf("\t.limit stack 99\n");     // TODO: Calcular

        Method method = this.symbolTable.getMethod(node.getIdentity());
        int numLocals = method.getNumLocalVars() + method.getNumParameters();   // NumLocalVars + NumParameters
        this.printWriter.printf("\t.limit locals %d\n\n", numLocals);

        /*for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            this.printWriter.printf("\tId: %d\n", node.jjtGetChild(i).getId());
            for (int j = 0; j < node.jjtGetChild(i).jjtGetNumChildren(); j++) {
                this.printWriter.printf("\t\tId: %d\n", node.jjtGetChild(i).jjtGetChild(j).getId());
            }
        }*/

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if(node.jjtGetChild(i) instanceof ASTVarDeclaration || node.jjtGetChild(i) instanceof ASTArgument)
                continue;

            if(node.jjtGetChild(i) != null)
                writeStatement((SimpleNode) node.jjtGetChild(i));
        }

        this.printWriter.printf("\t%s\n", convertReturnType(node.getReturnType()));
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

            case JavammTreeConstants.JJTVARIABLEINIT:
                this.printWriter.printf("\t;Variable Init Statement\n");
                break;

            default:
                this.printWriter.printf("\tId: %d\n", node.getId());
                this.printWriter.printf("\t;Expression\n");
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
            case JavammTreeConstants.JJTLESSTHAN:
                this.printWriter.printf("\t;Less Than\n");
                break;

            case JavammTreeConstants.JJTMULTIPLICATIONDIVISION:
            case JavammTreeConstants.JJTADDITIONSUBTRACTION:
                writeArithmetic(node);
                break;

            case JavammTreeConstants.JJTNOT:
                this.printWriter.printf("\tNot\n");
                break;

            case JavammTreeConstants.JJTDOT:
                this.printWriter.printf("\tDot\n");
                break;

            case JavammTreeConstants.JJTINTEGER:
                this.printWriter.printf("\tInteger\n");
                break;

            case JavammTreeConstants.JJTTRUE:
                this.printWriter.printf("\tTrue\n");
                break;

            case JavammTreeConstants.JJTFALSE:
                this.printWriter.printf("\tFalse\n");
                break;

            case JavammTreeConstants.JJTTHIS:
                this.printWriter.printf("\tThis\n");
                break;

            case JavammTreeConstants.JJTID:
                this.printWriter.printf("\tID\n");
                break;

            case JavammTreeConstants.JJTARRAYACCESS:
                this.printWriter.printf("\tArray Access\n");
                break;

            case JavammTreeConstants.JJTEXPRESSIONDOT:
                this.printWriter.printf("\tExpression Dot\n");
                break;

            case JavammTreeConstants.JJTEXPRESSIONNEW:
                this.printWriter.printf("\tExpression New\n");
                break;

            default:
                this.printWriter.printf("EXPRESSION DEFAULT: %d\n", node.getId());
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