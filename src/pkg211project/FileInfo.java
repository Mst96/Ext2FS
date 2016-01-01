/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg211project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author mustafa
 */
public class FileInfo {

    private int inodeNumber;
    private short length;
    private byte nameLength;
    private byte fileType;
    private String fileName;
    private Inode inode;
    private String permissions;

    public FileInfo(int position, int inodeTablePointer, RandomAccessFile f, ByteBuffer buffer) throws IOException {

        inodeNumber = buffer.getInt(position);
        length = buffer.getShort(position + 4);
        nameLength = buffer.get(position + 6);
        fileType = buffer.get(position + 7);

        byte[] stringHolder = new byte[(int) nameLength];
        buffer.position(position + 8);
        buffer.get(stringHolder);
        fileName = new String(stringHolder);
        
        int block = inodeNumber/1712;
        inode = new Inode(inodeTablePointer, block, f, inodeNumber);
        permissions = inode.getPermissions();

    }

    public short getLength() {
        return length;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public String getPermissions(){
        return permissions;
    }
    
    public int getModifiedDate(){
        long milliseconds = inode.getLastModifiedDate() * 1000;
        Date modDate = new Date(milliseconds);
        return inode.getLastModifiedDate();
    }
    public int getFileSize(){
        int decimal = Integer.parseInt(inode.getFileSize(), 2);
        return decimal;
    }
    public int getFileType(){
        return fileType;
    }
    public int getUserID(){
        return inode.getUserID();
    }
    public int getGroupID(){
        return inode.getGroupID();
    }
    public int getHardlinks(){
       return inode.getHardlinks();
    }
    public Inode getInode(){
        return inode;
    }
    public int getInodeNumber(){
        return inodeNumber;
    }

}
