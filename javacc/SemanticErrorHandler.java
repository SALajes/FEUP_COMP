import javax.script.ScriptEngineManager;

public class SemanticErrorHandler {
    private static SemanticErrorHandler handler = new SemanticErrorHandler();

    private int number_of_errors = 0;
    private int number_of_warnings = 0;
    private final int MAX_NUM_ERRORS = 10;

    private SemanticErrorHandler(){}

    public static SemanticErrorHandler getInstance(){
        return handler;
    }

    public void resetRegistry(){
        number_of_errors = 0;
        number_of_warnings = 0;
    }

    public void printError(String scope, String message){
        printError(scope, message, null);
    }

    public void printError(String scope, String message, String code_fragment){
        number_of_errors++;
        print("\nERROR ", number_of_errors, scope, message, code_fragment);
    }

    public void printWarning(String scope, String message){
        printWarning(scope, message, null);
    }

    public void printWarning(String scope, String message, String code_fragment){
        number_of_warnings++;
        print("\nWARNING ", number_of_warnings, scope, message, code_fragment);
    }

    private void print(String type, int number, String scope, String message, String code_fragment){
        System.out.println(type + number + ": in scope " + scope);
        System.out.println(message);
        System.out.println(code_fragment==null ? "" : ("Code fragment: " + code_fragment));
    }

    public void determineSemanticAnalysis() throws ParseException {
        System.out.println("\n------------ ANALYSIS ------------");
        System.out.println(number_of_errors + " ERRORS");
        System.out.println(number_of_warnings + " WARNINGS");
        System.out.println("----------------------------------");
        System.out.println("> Compilation status: " + (number_of_errors == 0 ? "SUCCESS" : "FAIL"));

        if(number_of_errors > 0){
            throw new ParseException();
        }
    }
}
