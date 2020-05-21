import java.util.HashMap;

public class StackCalculator {
    private int maxStack;
    private int stack;

    private static HashMap<String, Integer> instructionCost = new HashMap<>();

    public StackCalculator() {
        this.maxStack = 0;
        this.stack = 0;

        fillHash();
    }

    int getMaxStack() {
        return maxStack;
    }

    void reset() {
        this.maxStack = 0;
        this.stack = 0;
    }

    void addInstruction(String instruction) {
        if(instructionCost.get(instruction) == null) {
            System.out.println(instruction);
            return;
        }

        this.stack += instructionCost.get(instruction);

        if(this.stack < 0)
            this.stack = 0;

        if(this.stack > this.maxStack)
            this.maxStack = this.stack;

        System.out.println("STACK: " + this.stack + "\tMAX: "+ this.maxStack+"\tINS: "+instruction);
    }

    void addInstruction(String instruction, int args) {
        if(instructionCost.get(instruction) == null) {
            System.out.println(instruction);
            return;
        }

        this.stack += instructionCost.get(instruction);
        this.stack -= args;

        if(this.stack < 0)
            this.stack = 0;

        if(this.stack > this.maxStack)
            this.maxStack = this.stack;

        System.out.println("STACK: " + this.stack + "\tMAX: "+ this.maxStack+"\tINS: "+instruction);
    }

    // https://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
    private void fillHash() {
        instructionCost.put("aload", 1);
        instructionCost.put("aload_0", 1);
        instructionCost.put("areturn", -1);
        instructionCost.put("arraylength", 0);
        instructionCost.put("astore", -1);
        instructionCost.put("bipush", 1);
        instructionCost.put("dup", 1);
        instructionCost.put("getfield", 0);
        instructionCost.put("goto", 0);
        instructionCost.put("iadd", -1);
        instructionCost.put("iaload", -1);
        instructionCost.put("iastore", -3);
        instructionCost.put("iconst", 1);
        instructionCost.put("iconst_0", 1);
        instructionCost.put("iconst_1", 1);
        instructionCost.put("iconst_2", 1);
        instructionCost.put("iconst_3", 1);
        instructionCost.put("iconst_4", 1);
        instructionCost.put("iconst_5", 1);
        instructionCost.put("idiv", -1);
        instructionCost.put("ifeq", -1);
        instructionCost.put("ifge", -1);
        instructionCost.put("ifne", -1);
        instructionCost.put("iload", 1);
        instructionCost.put("imul", -1);
        instructionCost.put("invokespecial", 0);    // - num args
        instructionCost.put("invokestatic", 0);     // ??
        instructionCost.put("invokevirtual", 0);    // - num args
        instructionCost.put("ireturn", -1);
        instructionCost.put("istore", -1);
        instructionCost.put("isub", -1);
        instructionCost.put("ldc", 1);
        instructionCost.put("new", 1);
        instructionCost.put("newarray", 0);
        instructionCost.put("pop", 0);
        instructionCost.put("putfield", -2);
        instructionCost.put("return", 0);
        instructionCost.put("sipush", 1);
        instructionCost.put("swap", 0);
    }
}
