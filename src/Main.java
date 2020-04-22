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
            Javamm javamm = new Javamm(new java.io.FileInputStream(args[0]));
            SimpleNode root = javamm.Start();

            if (javamm.getNumErrors() > 0) {
                System.out.println("Errors ocurred (" + javamm.getNumErrors() + ")");
                throw new RuntimeException();
            }

            root.dump("");

        } catch(FileNotFoundException e){
            System.out.println("File not found");
            System.exit(-1);
        }catch(ParseException e) {
            throw new RuntimeException();
        }
    }

    // From Javamm.jjt
    // public static void main(String args[]) throws RuntimeException {
	// 	try{
	// 	    Javamm javamm = new Javamm(new java.io.FileInputStream(args[0]));
	// 	    SimpleNode root = javamm.Start();

	// 	    if (Javamm.numErrors > 0) {
    //             System.out.println("Errors ocurred");
    //             throw new RuntimeException();
    //         }

    //         root.dump("");
	// 	    //build symbol table
	// 	    //check semantics
	// 	    //display symbol table

	// 	} catch(FileNotFoundException e){
	// 	    System.out.println("File not found");
	// 	    System.exit(-1);
	// 	}catch(ParseException e) { throw new RuntimeException(); }
	// }

    public static int eval(SimpleNode node) {
        return 0;
    }
}
