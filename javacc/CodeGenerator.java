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
            System.exit(-1);
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

        if(!this.symbolTable.getGlobal_variables().isEmpty())
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

        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (node.jjtGetChild(i) instanceof ASTArgument) {
            SimpleNode arg = (SimpleNode) node.jjtGetChild(i);

            ret.append(convertType(arg.getType()));

            i++;
        }

        return ret.toString();
    }

    // TODO: Calcular stack
    private void writeMethodBody(SimpleNode node) {
        this.printWriter.printf("\t.limit stack 99\n");                         // TODO: Calcular

        Method method = this.symbolTable.getMethod(node.getIdentity());
        int numLocals = method.getNumLocalVars() + method.getNumParameters();
        this.printWriter.printf("\t.limit locals %d\n\n", numLocals);

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if(node.jjtGetChild(i) instanceof ASTArgument || node.jjtGetChild(i) instanceof ASTVarDeclaration)
                continue;

            writeStatement((SimpleNode) node.jjtGetChild(i));
        }

        this.printWriter.printf("\t%s\n", convertReturnType(node.getReturnType()));
    }

    private void writeStatement(SimpleNode node) {
        switch (node.getId()) {
            case JavammTreeConstants.JJTIF:
                writeIfStatement(node);
                break;

            case JavammTreeConstants.JJTWHILE:
                writeWhileStatement(node);
                break;

            case JavammTreeConstants.JJTASSIGNEMENT:
                writeAssignStatement(node);
                break;

            default:
                this.printWriter.printf("\t;Expression Id: %d\n", node.getId());
                writeExpression(node);
                break;
        }

        this.printWriter.printf("\n");
    }

    // TODO: Gerar codigo do if block e da condition
    // Codigo body e else já está
    private void writeIfStatement(SimpleNode node) {
        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        SimpleNode body = (SimpleNode) node.jjtGetChild(1);
        SimpleNode then = (SimpleNode) node.jjtGetChild(2);

        this.printWriter.printf("\t;Condition\n");
        writeExpression(condition);

        this.printWriter.printf("\n\t;If Body\n");
        for (int i = 0; i < body.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) body.jjtGetChild(i));

        this.printWriter.printf("\t;Else\n");
        for (int i = 0; i < then.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) then.jjtGetChild(i));
    }

    // TODO: Gerar codigo do while block
    // Codigo body já está
    private void writeWhileStatement(SimpleNode node) {
        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);

        this.printWriter.printf("\t;Condition\n");
        writeExpression(condition);

        this.printWriter.printf("\n\t;While Body\n");
        for (int i = 1; i < node.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) node.jjtGetChild(i));
    }

    private void writeAssignStatement(SimpleNode node) {
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        writeExpression(right);

        if(left instanceof ASTArrayInit) {
            this.printWriter.printf("\t;Array Init\n");
//            writeArrayInit(left);
            return;
        }

        String varName = left.getIdentity();

        if(this.symbolTable.getMethod(this.scope).checkVariable(varName)) {
            Symbol var = this.symbolTable.getMethod(this.scope).getVariable(varName);

            switch (var.getType()) {
                case "int":
                case "boolean":
                    this.printWriter.printf("\tistore%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
                default:
                    this.printWriter.printf("\tastore%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
            }
        } else if (this.symbolTable.globalVariableExists(varName)) {
            this.printWriter.printf("\taload_0\n");
            this.printWriter.printf("\tputfield %s/%s %s\n", this.classNode.getIdentity(), varName, convertType(this.symbolTable.getGlobalVarType(varName)));
        }
    }

    // TODO: Array Init
    private void writeArrayInit(SimpleNode node) {

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

            case JavammTreeConstants.JJTFUNCTIONCALLARGUMENTS:
                writeFunctionCallArguments(node);
                break;

            case JavammTreeConstants.JJTINTEGER:
                writeInteger(node);
                break;

            case JavammTreeConstants.JJTTRUE:
                this.printWriter.printf("\ticonst_1\n");
                break;

            case JavammTreeConstants.JJTFALSE:
                this.printWriter.printf("\ticonst_0\n");
                break;

            case JavammTreeConstants.JJTINDEX:
                writeExpression((SimpleNode) node.jjtGetChild(0));
                break;

            case JavammTreeConstants.JJTID:
                writeID(node);
                break;

            case JavammTreeConstants.JJTARRAYACCESS:
                this.printWriter.printf("\t;Array Access\n");
//                writeArrayAccess(node);
                break;

            case JavammTreeConstants.JJTEXPRESSIONNEW:
                writeNew(node);
                break;

            default:
                this.printWriter.printf(";EXPRESSION DEFAULT: %d\n", node.getId());
                break;
        }
    }

    private  void writeBoolean(SimpleNode node) {

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

    // TODO: invoke de métodos locais ou de imports. this.<METHOD> já está
    private void writeDot(SimpleNode node){
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        if(left instanceof ASTThis) {
            this.printWriter.printf("\taload_0\n");

            writeExpression((SimpleNode) right.jjtGetChild(0));

            Method method = this.symbolTable.getMethod(right.getIdentity());
            String args = genArgs(method);
            this.printWriter.printf("\tinvokevirtual %s/%s(%s)%s\n", this.classNode.getIdentity(), right.getIdentity(), args, convertType(method.getReturnType()));
            return;
        }

        if(left instanceof ASTID && right instanceof ASTExpressionDot)
            this.printWriter.printf("\t;Local or Import method\n");
    }

    private String genArgs(Method method) {
        StringBuilder ret = new StringBuilder();

        if(method.getNumLocalVars() == 0)
            return ret.toString();

        Hashtable<String, Symbol> vars = method.getLocalVariables();

        for (Symbol s : vars.values()) {
            ret.append(convertType(s.getType()));
        }

        return ret.toString();
    }

    private void writeFunctionCallArguments(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            writeExpression((SimpleNode) node.jjtGetChild(i));
        }
    }

    // TODO: Optimizar load de valores > 5
    private void writeInteger(SimpleNode node) {
        int val = Integer.parseInt(node.getIdentity());

        if(val < 6)
            this.printWriter.printf("\ticonst_%d\n", val);
        else
            this.printWriter.printf("\tldc %d\n", val);     // TODO: Mudar para ser mais optimizado
    }

    private void writeID(SimpleNode node){
        String varName = node.getIdentity();

        if(this.symbolTable.getMethod(this.scope).checkVariable(varName)) {
            Symbol var = this.symbolTable.getMethod(this.scope).getVariable(varName);

            switch (var.getType()) {
                case "int":
                case "boolean":
                    this.printWriter.printf("\tiload%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
                case "int[]":
                default:
                    this.printWriter.printf("\taload%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
            }

        } else if (this.symbolTable.globalVariableExists(varName)) {
            this.printWriter.printf("\taload_0\n");
            this.printWriter.printf("\tgetfield %s/%s %s\n", this.classNode.getIdentity(), varName, convertType(this.symbolTable.getGlobalVarType(varName)));
        }
    }

    // TODO: CP3
    private void writeArrayAccess(SimpleNode node) {

    }

    private void writeNew(SimpleNode node) {
        if(node.jjtGetNumChildren() > 0){
            writeExpression((SimpleNode) node.jjtGetChild(0));
            this.printWriter.printf("\tnewarray int\n");
        } else {
            this.printWriter.printf("\tnew %s\n", node.getReturnType());
            this.printWriter.printf("\tdup\n");
            this.printWriter.printf("\tinvokespecial %s/<init>()V\n", node.getReturnType());
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
