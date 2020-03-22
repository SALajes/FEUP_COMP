//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.FileNotFoundException;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws RuntimeException {
        try{
            Javamm.numErrors = 0;
            Javamm javamm = new Javamm(new java.io.FileInputStream(args[0]));
            javamm.Start().dump("");

            if (javamm.numErrors > 0) {
                System.out.println("Errors ocurred");
                throw new RuntimeException();
            }
        } catch(FileNotFoundException e){
            System.out.println("File not found");
            System.exit(-1);
        }catch(ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static int eval(SimpleNode node) {
        return 0;
    }
}
