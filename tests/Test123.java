package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test123 {
    public static void main(String[] args) {
        try(PrintWriter p = new PrintWriter(new FileWriter("MESSAGE_DATABASE" + "/Alice-@@Bob.txt"));){
            p.printf("hey hey hey");
        } catch (IOException e){
            System.out.println("Oh no!!");
        }
    }
}
