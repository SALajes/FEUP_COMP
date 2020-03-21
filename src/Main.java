//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.FileNotFoundException;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        try{
            Javamm javamm = new Javamm(new java.io.FileInputStream(args[0]));
            javamm.Start();
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
