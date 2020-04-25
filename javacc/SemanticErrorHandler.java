package javacc;

import javax.script.ScriptEngineManager;

public class SemanticErrorHandler {
    private static SemanticErrorHandler handler = new SemanticErrorHandler();

    private int number_of_errors = 0;
    private final int MAX_NUM_ERRORS = 10;

    private SemanticErrorHandler(){}

    public static SemanticErrorHandler getInstance(){
        return handler;
    }

    public void printError(String scope, String message){
        printError(scope, message, null);
    }

    public void printError(String scope, String message, String code_fragment){
        number_of_errors++;
        System.out.println("ERROR " + number_of_errors + ": in scope " + scope);
        System.out.println(message);
        System.out.println((code_fragment==null ? "" : ("Code fragment: " + code_fragment)) + '\n');
    }

    public void printNumberOfErrors(){
        System.out.println("------------------------------------");
        System.out.println("Number of semantic errors: " + number_of_errors);
        System.out.println("------------------------------------");
        System.out.println("Compilation status: " + (number_of_errors == 0 ? "SUCCESS" : "FAIL"));
        number_of_errors = 0;
    }
}
