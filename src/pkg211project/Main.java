/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg211project;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author mustafa
 */
public class Main {

    /**
     * This API allows for the user to read a file in the ext2 filesystem given a filepath.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Volume  vol = new Volume("ext2fs"); 
        Ext2File  f = new Ext2File (vol, "/two-cities"); 
        byte buf[ ] = f.read(0L, f.size());
         System.out.format ("%s\n", new String(buf));
    }
    
}
