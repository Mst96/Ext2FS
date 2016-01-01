/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg211project;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author mustafa
 */
public class Helper {
    private Object BitConverter;
    /**
 *Outputs an array of bytes as returned by read( ) in a readable hexadecimal format,
 * perhaps with printable ASCII codes by the side.
 * Need to be able to neatly handle having too few bytes
 */
    public String dumpHexBytes(byte[] bytes){
        String hexDump = DatatypeConverter.printHexBinary(bytes);
        System.out.format("%s\n", hexDump);
        return hexDump;
        
        
            
        }
}
