import java.io.*;
import java.util.*;

class CodeGenerator {
    private SymbolTable symbolTable;
    private SimpleNode root;
    private boolean optO;

    private SimpleNode classNode;

    private PrintWriter printWriter;

    private HashMap<String, String> lutSwappedWords = new HashMap<>();
    private Method currMethod;

    private StackCalculator stackCalculator = new StackCalculator();
    private List<Integer> stackValues = new ArrayList<>();

    public CodeGenerator(SimpleNode root, SymbolTable symbolTable, boolean optO) {
        this.root = root;
        this.symbolTable = symbolTable;
        this.optO = optO;
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

        if(!stackValues.isEmpty())
            updateStack();
    }

    private void updateStack() {
        String fileName = "Jasmin/" + this.classNode.getIdentity() + ".j";
        int i = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder inputBuffer = new StringBuilder();
            String line;

            while((line = br.readLine()) != null) {
                if(line.equals("\t.LIMIT STACK 99")) {
                    line = line.replace(".LIMIT STACK 99", ".limit stack " + this.stackValues.get(i));
                    i++;
                }

                inputBuffer.append(line);
                inputBuffer.append("\n");
            }
            br.close();

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(inputBuffer.toString().getBytes());
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
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

        if(this.classNode.getExtend() != null) {
            this.printWriter.printf(".super %s\n\n", this.classNode.getExtend());
            boolean extend = true;
        } else
            this.printWriter.print(".super java/lang/Object\n\n");
    }

    private void writeGlobalVars() {
        for (int i = 0; i < this.classNode.jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) this.classNode.jjtGetChild(i);
            if(node instanceof ASTMethod || node instanceof ASTMain)
                break;

            this.printWriter.printf(".field private %s %s\n", getVarName(node.getIdentity()), convertType(node.getType()));
        }

        if(!this.symbolTable.getGlobal_variables().isEmpty())
            this.printWriter.printf("\n");
    }

    private void writeInitializer() {
        this.printWriter.print(".method public <init>()V\n");
        this.printWriter.print("\taload_0\n");

        if(this.classNode.getExtend() != null)
            this.printWriter.printf("\tinvokespecial " + this.classNode.getExtend()+"/<init>()V\n");
        else
            this.printWriter.print("\tinvokenonvirtual java/lang/Object/<init>()V\n");

        this.printWriter.print("\treturn\n");
        this.printWriter.print(".end method\n\n");
    }

    private void writeMethods() {
        for (int i = 0; i < this.classNode.jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) this.classNode.jjtGetChild(i);

            if(node instanceof ASTMethod || node instanceof ASTMain) {
                writeMethod(node);

                this.stackCalculator.reset();
            }
        }
    }

    private void writeMethod(SimpleNode node) {
        String args = genArgs(node);

        this.printWriter.printf(".method public %s(%s)%s\n", node.getIdentity().equals("main") ? "static main" : node.getIdentity(), args, convertType(node.getReturnType()));

        writeMethodBody(node);

        this.stackValues.add(this.stackCalculator.getMaxStack());
        this.printWriter.printf(".end method\n\n");
    }

    private String genArgs(SimpleNode node) {
        if(node instanceof ASTMain) {
            this.currMethod = this.symbolTable.getMethod(node.getIdentity());
            return convertType("String[]");
        }
        ArrayList<String> argTypes = new ArrayList<>();
        StringBuilder ret = new StringBuilder();
        int i = 0;

        while (node.jjtGetChild(i) instanceof ASTArgument) {
            SimpleNode arg = (SimpleNode) node.jjtGetChild(i);

            ret.append(convertType(arg.getType()));
            argTypes.add(arg.getType());

            i++;
        }

        this.currMethod = this.symbolTable.getMethod(node.getIdentity(), argTypes);

        return ret.toString();
    }

    private void writeMethodBody(SimpleNode node) {
        this.printWriter.printf("\t.LIMIT STACK 99\n");

        int numLocals = this.currMethod.getNumLocalVars() + this.currMethod.getNumParameters() +1;
        if(this.currMethod.getName().equals("main")) {
            numLocals--;
            this.currMethod.updateVarIndex();
        }

        this.printWriter.printf("\t.limit locals %d\n\n", numLocals);

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if(node.jjtGetChild(i) instanceof ASTArgument || node.jjtGetChild(i) instanceof ASTVarDeclaration)
                continue;

            writeStatement((SimpleNode) node.jjtGetChild(i));
        }

        String ret = convertReturnType(node.getReturnType());

        this.stackCalculator.addInstruction(ret);
        this.printWriter.printf("\t%s\n", ret);
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
                writeExpression(node);
                break;
        }

        this.printWriter.printf("\n");
    }

    private void writeIfStatement(SimpleNode node) {
        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        SimpleNode body = (SimpleNode) node.jjtGetChild(1);
        SimpleNode then = (SimpleNode) node.jjtGetChild(2);

        writeExpression(condition);

        this.stackCalculator.addInstruction("ifeq");
        this.printWriter.printf("\tifeq ELSE%d\n", node.getCount());

        for (int i = 0; i < body.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) body.jjtGetChild(i));

        this.stackCalculator.addInstruction("goto");
        this.printWriter.printf("\tgoto ICONT%d\n", node.getCount());
        this.printWriter.printf("\tELSE%d:\n", node.getCount());

        for (int i = 0; i < then.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) then.jjtGetChild(i));

        this.printWriter.printf("\tICONT%d:\n", node.getCount());
    }

    private void writeWhileStatement(SimpleNode node) {
        SimpleNode condition = (SimpleNode) node.jjtGetChild(0);
        boolean atleastOnce = checkTemplate(condition);

        if(atleastOnce) {
            writeExpression(condition);

            this.stackCalculator.addInstruction("ifeq");
            this.printWriter.printf("\tifeq WCONT%d\n", node.getCount());

            this.printWriter.printf("\tWHILE%d:\n", node.getCount());
        } else {
            this.printWriter.printf("\tWHILE%d:\n", node.getCount());
            writeExpression(condition);

            this.stackCalculator.addInstruction("ifeq");
            this.printWriter.printf("\tifeq WCONT%d\n", node.getCount());
        }

        for (int i = 1; i < node.jjtGetNumChildren(); i++)
            writeStatement((SimpleNode) node.jjtGetChild(i));

        if(atleastOnce) {
            writeExpression(condition);

            this.stackCalculator.addInstruction("ifne");
            this.printWriter.printf("\tifne WHILE%d\n", node.getCount());
        } else {
            this.stackCalculator.addInstruction("goto");
            this.printWriter.printf("\tgoto WHILE%d\n", node.getCount());
        }

        this.printWriter.printf("\tWCONT%d:\n", node.getCount());
    }

    private boolean checkTemplate(SimpleNode node) {
        if((node instanceof ASTTrue || node instanceof ASTFalse) || (node instanceof ASTID && node.jjtGetNumChildren() == 0))
            return true;

        if(node instanceof ASTAnd || node instanceof ASTLessThan) {
            SimpleNode left = (SimpleNode) node.jjtGetChild(0);
            SimpleNode right = (SimpleNode) node.jjtGetChild(1);

            boolean leftCheck = left instanceof ASTInteger || left instanceof ASTTrue || left instanceof ASTFalse || (left instanceof ASTID && left.jjtGetNumChildren() == 0);
            boolean rightCheck = right instanceof ASTInteger || right instanceof ASTTrue || right instanceof ASTFalse || (right instanceof ASTID && right.jjtGetNumChildren() == 0);

            return leftCheck && rightCheck;
        }

        return false;
    }

    private void writeAssignStatement(SimpleNode node) {
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        if(left instanceof ASTArrayInit) {
            writeArrayInit(node);
            return;
        }

        if(right instanceof ASTAdditionSubtraction){
            if(checkInc(node)) {
                writeIncrement(node);
                return;
            }
        }

        writeExpression(right);

        String varName = left.getIdentity();

        if(this.currMethod.checkVariable(varName)) {
            Symbol var = this.currMethod.getVariable(varName);

            switch (var.getType()) {
                case "int":
                case "boolean":
                    this.stackCalculator.addInstruction("istore");
                    this.printWriter.printf("\tistore%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
                default:
                    this.stackCalculator.addInstruction("astore");
                    this.printWriter.printf("\tastore%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
            }
        } else if (this.symbolTable.globalVariableExists(varName)) {
            this.stackCalculator.addInstruction("aload_0");
            this.printWriter.printf("\taload_0\n");

            this.stackCalculator.addInstruction("swap");
            this.printWriter.printf("\tswap\n");

            this.stackCalculator.addInstruction("putfield");
            this.printWriter.printf("\tputfield %s/%s %s\n", this.classNode.getIdentity(), getVarName(varName), convertType(this.symbolTable.getGlobalVarType(varName)));
        }
    }

    private boolean checkInc(SimpleNode node) {
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);
        SimpleNode child0 = (SimpleNode) right.jjtGetChild(0);
        SimpleNode child1 = (SimpleNode) right.jjtGetChild(1);

        // Instrucao iinc e so para variavesi locais, nao globais
        if(!this.currMethod.checkVariable(left.getIdentity()) && this.symbolTable.globalVariableExists(left.getIdentity()))
            return false;

        if(left.getIdentity().equals(child0.getIdentity()) || left.getIdentity().equals(child1.getIdentity()))
            return right.getOperator().equals("+");

        return false;
    }

    private void writeIncrement(SimpleNode node) {
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        Symbol var = this.currMethod.getVariable(left.getIdentity());

        this.stackCalculator.addInstruction("iinc");
        this.printWriter.printf("\tiinc %d 1\n", var.getIndex());
    }

    private void writeArrayInit(SimpleNode node) {
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        writeID(left);                                              // Var name
        writeExpression((SimpleNode) left.jjtGetChild(0));          // Index
        writeExpression(right);                                     // = smth

        this.stackCalculator.addInstruction("iastore");
        this.printWriter.printf("\tiastore\n");
    }

    private void writeExpression(SimpleNode node) {
        switch (node.getId()) {
            case JavammTreeConstants.JJTAND:
                writeAndCondition(node);
                break;

            case JavammTreeConstants.JJTLESSTHAN:
                writeLessThanCondition(node);
                break;

            case JavammTreeConstants.JJTMULTIPLICATIONDIVISION:
            case JavammTreeConstants.JJTADDITIONSUBTRACTION:
                writeArithmetic(node);
                break;

            case JavammTreeConstants.JJTNOT:
                writeNot(node);
                break;

            case JavammTreeConstants.JJTDOT:
                writeDot(node);
                break;

            case JavammTreeConstants.JJTFUNCTIONCALLARGUMENTS:
                writeFunctionCallArguments(node);
                break;

            case JavammTreeConstants.JJTINTEGER:
                writeInteger(node);
                break;

            case JavammTreeConstants.JJTTRUE:
                this.stackCalculator.addInstruction("iconst_1");
                this.printWriter.printf("\ticonst_1\n");
                break;

            case JavammTreeConstants.JJTFALSE:
                this.stackCalculator.addInstruction("iconst_0");
                this.printWriter.printf("\ticonst_0\n");
                break;

            case JavammTreeConstants.JJTTHIS:
                this.stackCalculator.addInstruction("aload_0");
                this.printWriter.printf("\taload_0\n");
                break;

            case JavammTreeConstants.JJTINDEX:
            case JavammTreeConstants.JJTRETURN:
                writeExpression((SimpleNode) node.jjtGetChild(0));
                break;

            case JavammTreeConstants.JJTID:
                writeID(node);
                break;

            case JavammTreeConstants.JJTARRAYACCESS:
                writeArrayAccess(node);
                break;

            case JavammTreeConstants.JJTEXPRESSIONNEW:
                writeNew(node);
                break;

            default:
                this.printWriter.printf(";EXPRESSION DEFAULT: %d\n", node.getId());
                break;
        }
    }

    private void writeLessThanCondition(SimpleNode node) {
        writeExpression((SimpleNode) node.jjtGetChild(0));
        writeExpression((SimpleNode) node.jjtGetChild(1));

        this.stackCalculator.addInstruction("isub");
        this.printWriter.printf("\tisub\n");

        this.stackCalculator.addInstruction("ifge");
        this.printWriter.printf("\tifge LT%d\n", node.getCount());

        this.stackCalculator.addInstruction("iconst_1");
        this.printWriter.printf("\ticonst_1\n");

        this.stackCalculator.addInstruction("goto");
        this.printWriter.printf("\tgoto LTCONT%d\n", node.getCount());

        this.printWriter.printf("\tLT%d:\n", node.getCount());

        this.stackCalculator.addInstruction("iconst_0");
        this.printWriter.printf("\ticonst_0\n");

        this.printWriter.printf("\tLTCONT%d:\n", node.getCount());
    }

    private void writeAndCondition(SimpleNode node) {
        writeExpression((SimpleNode) node.jjtGetChild(0));

        this.stackCalculator.addInstruction("ifne");
        this.printWriter.printf("\tifne AND%d\n", node.getCount());

        this.stackCalculator.addInstruction("iconst_0");
        this.printWriter.printf("\ticonst_0\n");

        this.stackCalculator.addInstruction("goto");
        this.printWriter.printf("\tgoto ACONT%d\n", node.getCount());

        this.printWriter.printf("\tAND%d:\n", node.getCount());

        writeExpression((SimpleNode) node.jjtGetChild(1));

        this.printWriter.printf("\tACONT%d:\n", node.getCount());
    }

    private void writeArithmetic(SimpleNode node){
        String op = node.getOperator();

        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        writeExpression(left);
        writeExpression(right);

        switch (op) {
            case "+":
                this.stackCalculator.addInstruction("iadd");
                this.printWriter.printf("\tiadd\n");
                break;

            case "-":
                this.stackCalculator.addInstruction("isub");
                this.printWriter.printf("\tisub\n");
                break;

            case "*":
                this.stackCalculator.addInstruction("imul");
                this.printWriter.printf("\timul\n");
                break;

            case "/":
                this.stackCalculator.addInstruction("idiv");
                this.printWriter.printf("\tidiv\n");
                break;
        }
    }

    // O not está a ser feito fazendo xor do valor do booleano inserido com o número 1.
    // Desta forma, se o input for 1 o resultado é 0, e se o input for 0 o resultado é 1.
    private void writeNot(SimpleNode node) {
        writeExpression((SimpleNode) node.jjtGetChild(0));

        this.stackCalculator.addInstruction("iconst_1");
        this.printWriter.printf("\ticonst_1\n");

        this.stackCalculator.addInstruction("ixor");
        this.printWriter.printf("\tixor\n");
    }

    private void writeDot(SimpleNode node){
        SimpleNode left = (SimpleNode) node.jjtGetChild(0);
        SimpleNode right = (SimpleNode) node.jjtGetChild(1);

        if(right.getIdentity().equals("length")) {
            writeExpression(left);
            this.stackCalculator.addInstruction("arraylength");
            this.printWriter.printf("\tarraylength\n");
            return;
        }

        String className = left.getIdentity();
        String methodName = right.getIdentity();
        ImportMethod importMethod = this.symbolTable.getImportMethod(className, methodName);

        if(importMethod != null && importMethod.isStatic()) {
            writeExpression((SimpleNode) right.jjtGetChild(0));
            writeImportMethod(className, right);

            return;
        }

        writeExpression(left);
        writeExpression((SimpleNode) right.jjtGetChild(0));

        boolean isImport = this.symbolTable.isMethodImport(methodName, genArgsArray((SimpleNode) right.jjtGetChild(0)));

        String ret = "";

        if(isImport)
            ret = writeImportMethod(this.classNode.getExtend(), right);
        else
            ret = writeLocalMethod(right);

        // Glorious part that fixes the problem of the stack height 0 != 1

        SimpleNode parent = (SimpleNode) node.jjtGetParent();

        if(parent instanceof ASTAssignement) {
            SimpleNode pp = (SimpleNode) parent.jjtGetChild(0);

            ret = convertType(pp.getReturnType());
        } else if(parent instanceof ASTArrayInit || parent instanceof ASTAdditionSubtraction ||
                parent instanceof ASTMultiplicationDivision || parent instanceof ASTLessThan)
            ret = "I";
        else if(parent instanceof ASTAnd)
            ret = "Z";

        if((parent instanceof ASTWhile || parent instanceof ASTIfElse || parent instanceof ASTIfBody||
                parent instanceof ASTMethod || parent instanceof ASTMain) && !ret.equals("V")) {
            this.stackCalculator.addInstruction("pop");
            this.printWriter.printf("\tpop\n");
        }
    }

    private ArrayList<String> genArgsArray(SimpleNode node) {
        ArrayList<String> args = new ArrayList<>();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            args.add(child.getReturnType());
        }

        return args;
    }

    private String writeLocalMethod(SimpleNode node) {
        Method method = this.symbolTable.getMethod(node.getIdentity());
        String args = genArgsMethodInvoke((SimpleNode) node.jjtGetChild(0));

        this.stackCalculator.addInstruction("invokevirtual", method.getNumParameters());
        this.printWriter.printf("\tinvokevirtual %s/%s(%s)%s\n", this.classNode.getIdentity(), node.getIdentity(), args, convertType(method.getReturnType()));

        return convertType(method.getReturnType());
    }

    private String writeImportMethod(String className, SimpleNode node) {
        ImportMethod importMethod = this.symbolTable.getImportMethod(className, node.getIdentity());

        if(importMethod.isStatic()) {
            this.stackCalculator.addInstruction("invokestatic");
            this.printWriter.printf("\tinvokestatic ");
        } else {
            this.stackCalculator.addInstruction("invokevirtual", importMethod.getNumParameters());
            this.printWriter.printf("\tinvokevirtual ");
        }

        String args = genArgs(importMethod);
        this.printWriter.printf("%s/%s(%s)%s\n", importMethod.getClassName(), node.getIdentity(), args, convertType(importMethod.getReturnType()));

        return convertType(importMethod.getReturnType());
    }

    private String genArgsMethodInvoke(SimpleNode node) {
        if (!(node instanceof ASTFunctionCallArguments))
            return "";

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode aux = (SimpleNode) node.jjtGetChild(i);
            ret.append(convertType(aux.getReturnType()));
        }

        return ret.toString();
    }

    private String genArgs(ImportMethod method) {
        StringBuilder ret = new StringBuilder();

        ArrayList<String> vars = method.getParameters();

        if(vars.size() == 0)
            return ret.toString();

        for (String var : vars) {
            ret.append(convertType(var));
        }

        return ret.toString();
    }

    private void writeFunctionCallArguments(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            writeExpression((SimpleNode) node.jjtGetChild(i));
        }
    }

    private void writeInteger(SimpleNode node) {
        int val = Integer.parseInt(node.getIdentity());

        if(val < 6) {
            this.stackCalculator.addInstruction("iconst");
            this.printWriter.printf("\ticonst_%d\n", val);
        } else if(val < 128) {
            this.stackCalculator.addInstruction("bipush");
            this.printWriter.printf("\tbipush %d\n", val);      // bipush -> push byte
        } else if(val < 32768) {
            this.stackCalculator.addInstruction("sipush");
            this.printWriter.printf("\tsipush %d\n", val);      // sipush -> push short
        } else {
            this.stackCalculator.addInstruction("ldc");
            this.printWriter.printf("\tldc %d\n", val);
        }
    }

    private void writeID(SimpleNode node){
        String varName = node.getIdentity();

        if(this.currMethod.checkVariable(varName)) {
            Symbol var = this.currMethod.getVariable(varName);

            switch (var.getType()) {
                case "int":
                case "boolean":
                    this.stackCalculator.addInstruction("iload");
                    this.printWriter.printf("\tiload%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
                case "int[]":
                default:
                    this.stackCalculator.addInstruction("aload");
                    this.printWriter.printf("\taload%s%d\n", var.getIndex() <= 3 ? "_" : " ", var.getIndex());
                    break;
            }

        } else if (this.symbolTable.globalVariableExists(varName)) {
            this.stackCalculator.addInstruction("aload_0");
            this.printWriter.printf("\taload_0\n");

            this.stackCalculator.addInstruction("getfield");
            this.printWriter.printf("\tgetfield %s/%s %s\n", this.classNode.getIdentity(), getVarName(varName), convertType(this.symbolTable.getGlobalVarType(varName)));
        }
    }

    private void writeArrayAccess(SimpleNode node) {
        writeID((SimpleNode) node.jjtGetChild(0));              // Var name
        writeExpression((SimpleNode) node.jjtGetChild(1));      // Index

        this.stackCalculator.addInstruction("iaload");
        this.printWriter.printf("\tiaload\n");
    }

    private void writeNew(SimpleNode node) {
        if(node.jjtGetNumChildren() > 0){
            writeExpression((SimpleNode) node.jjtGetChild(0));

            this.stackCalculator.addInstruction("newarray");
            this.printWriter.printf("\tnewarray int\n");
        } else {
            this.stackCalculator.addInstruction("new");
            this.printWriter.printf("\tnew %s\n", node.getReturnType());

            this.stackCalculator.addInstruction("dup");
            this.printWriter.printf("\tdup\n");

            this.stackCalculator.addInstruction("invokespecial");
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

    // -- Dealing with Jasmin forbidden words --
    // Forbidden words:
    //      - field
    private String getVarName(String varName) {
        if(!varName.equals("field"))
            return varName;

        lutSwappedWords.putIfAbsent("field", genRandomVarName());

        return lutSwappedWords.get("field");
    }

    private String genRandomVarName() {
        int length = 8;
        Random rng = new Random();
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
