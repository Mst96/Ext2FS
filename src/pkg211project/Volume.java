/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg211project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author mustafa
 */
public class Volume {

    private ByteBuffer buffer;
    private SuperBlock superBlock;
    private int inodeTablePointer;
    private long blockSize = 1024;
    private Directory rootDirectory;
    private FileInfo[] rootDirectoryInfo;
    private RandomAccessFile f;
    private int inodesPerGroup;
    
    /**
     * Opens the Volume represented by the host Windows/ Linux file filename
     * (the file you downloaded)
     */
    public Volume(String filename) {
        try {
            f = new RandomAccessFile("ext2fs", "r"); //passes the file extfs as a random access file and the read.
            SuperBlock superBlock = new SuperBlock(); 
            superBlock.open(f);     //opens superblock
            inodesPerGroup = superBlock.getNumberOfInodesPerGroup();

            buffer = ByteBuffer.allocate(1024); //allocates buffer of 1024 bytes of memory.

            
            
            
            f.seek(2048); //seeks to second block 
            byte[] pointerBytes = new byte[1024]; 
            f.read(pointerBytes); //read block into pointerBytes
            buffer.put(pointerBytes); //put into buffer
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            inodeTablePointer = buffer.getInt(8); //Get Inode Table Pointer which is 8 bytes along.

            Inode inodeInfo = new Inode(inodeTablePointer, 0, f, 2); //root directory's inode information

            //Opening that pointer in inode2 points to.
            
            buffer.clear();
            buffer = this.goTo(inodeInfo.getPointer(0));//Following inode 2's pointer.

            rootDirectory = new Directory(f, inodeTablePointer);
            rootDirectoryInfo = rootDirectory.getFileInfo(buffer);

            //f.close();
            
        } catch (IOException exc) {
            System.out.println(exc);
            System.exit(1);
        }

    }
    /**
     * Follows pointer and then reads fileblock into a buffer and returns this buffer.
     * @param pointer
     * @return ByteBuffer
     * @throws java.io.IOException
     */
    public ByteBuffer goTo(int pointer) throws IOException{
            //This bit goes to where the inode points to. File or directory
            buffer = ByteBuffer.allocate(1024);
            f.seek(1024*pointer);        
            byte[] fileBlock = new byte[1024];
            f.read(fileBlock);
            buffer.put(fileBlock);
            //Helper h = new Helper();
            //h.dumpHexBytes(fileBlock); //dump hex bytes for debugging
            
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            
            return buffer;
    }    
    
    /**
     * The following method obtains the Root Directory info. 
     * returns the root directory info .
     */
    public FileInfo[] getRootDirectoryInfo() {
        return rootDirectoryInfo;
    }
    /**
     * The following method obtains RandomAccessFile. 
     */
    public RandomAccessFile getF(){
        return f;
    }
    /**
     * The following method finds and reads the Inode Table Pointer. 
     * returns the Inode Table Pointer.
     */
    public int getInodeTablePointer(int block) throws IOException{
            f.seek(2048);
            buffer.position(0); //sets position to the first byte.
            byte[] pointerBytes = new byte[1024];
            f.read(pointerBytes);
            buffer.put(pointerBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            inodeTablePointer = buffer.getInt(32*block + 8); //moves buffer along depending on the block of the inode.
            System.out.println("Inode table pointer: " + inodeTablePointer);
        return inodeTablePointer;
    }
    /**
     * The following method obtains the Inode Table Pointer. 
     * returns the Inode Table Pointer.
     */
    public int getInodeTablePointer(){
        return inodeTablePointer;
    }
    /**
     * The following method obtains the Inode Table Pointer. 
     * returns the number of Inodes Per Group.
     */
    public int getInodesPerGroup(){
        return inodesPerGroup;
    }
}
