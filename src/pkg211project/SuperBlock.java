package pkg211project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mustafa
 */
public class SuperBlock {
    private short magicNumber;
    private int totalInodes;
    private int totalBlocks;
    private int blocksPerGroup;
    private int inodesPerGroup;
    private int sizeOfInode;
    private String volumeLabel;
    /**
     * The following method opens the superblock.
     * This methods opens the superblock and thus allows for key information about the file to be located.
     */
    public void open(RandomAccessFile f) throws FileNotFoundException, IOException{
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            byte[] bytes = new byte[1024];
            f.seek(1024);
            f.read(bytes);
            buffer.put(bytes);
            
            magicNumber = buffer.getShort(56);
            totalInodes = buffer.getInt(0);
            totalBlocks = buffer.getInt(4);
            blocksPerGroup = buffer.getInt(32);
            inodesPerGroup = buffer.getInt(40);
            sizeOfInode = buffer.getInt(88);
            
            byte[] stringHolder = new byte[16];
            buffer.position(120);
            buffer.get(stringHolder);
            volumeLabel = new String(stringHolder);
            
    }
    /**
     * The following method obtains the Inode Table Pointer. 
     * returns the number of Inodes Per Group.
     */
    public int getNumberOfInodesPerGroup(){
        return inodesPerGroup;
    }
    
    
    
    
}
