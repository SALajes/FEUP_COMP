//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.FileNotFoundException;

public class Main {
    private static final String DEBUG_FLAG = "-d";

    public Main() {
    }

    public static void main(String[] args) throws RuntimeException {
        try{
            Javamm javamm = new Javamm(new java.io.FileInputStream(args[0]));
            SimpleNode root = javamm.Start();
            SemanticErrorHandler.getInstance().resetNumberOfErrors();

            if (javamm.getNumErrors() > 0) {
                System.out.println("Errors ocurred (" + javamm.getNumErrors() + ")");
                throw new RuntimeException();
            }

            root.dump("");

            if(args.length > 1 && DEBUG_FLAG.equals("-d"))
                javamm.symbol_table.dump();

            //check semantics
            SymbolTable symbol_table = javamm.getSymbolTable();
            root.checkSemantics(symbol_table);

            SemanticErrorHandler.getInstance().determineCompilation();

            CodeGenerator codeGenerator = new CodeGenerator(root, symbol_table);
            codeGenerator.generateCode();

        } catch(FileNotFoundException e){
            System.out.println("File not found");
            System.exit(-1);
        }catch(ParseException e) {
            throw new RuntimeException();
        }
    }

    public static int eval(SimpleNode node) {
        return 0;
    }
}
