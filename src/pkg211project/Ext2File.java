/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg211project;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mustafa
 */
public class Ext2File {

    private long size;
    private long position;
    private long start;
    private String filename;
    RandomAccessFile f;
    Volume vol;
    Inode inode;
    ByteBuffer buffer;
    private List<String> files;
    FileInfo currentFile;
    List<ByteBuffer> buffs;

    Ext2File(Volume vol, String filepath) throws FileNotFoundException, IOException {
        this.vol = vol;
        filepath = filepath.substring(1, filepath.length());
        files = Arrays.asList(filepath.split("/")); //splites filepath based on the '/' and stores it each part separately..
        FileInfo[] dirInfo = vol.getRootDirectoryInfo();
        
        traverse(dirInfo);
           
}

/**
 * Reads at most length bytes starting at byte offset startByte from start of
 * file. Byte 0 is the first byte in the file startByte must be such that, 0 ≤
 * startByte and less than file.size or an exception should be raised. If there
 * are fewer than length bytes remaining these will be read and a smaller number
 * of bytes than requested will be returned.
 */
public byte[] read(long startByte, long length) throws IOException {
    int parts; 
    if(length>1024){
        parts = (int)Math.ceil((double)length/1024.0);
    } 
    else {
        parts = 1;
    }
  
    int newLength = (int) length/parts; 
    byte[] bytes = new byte[newLength];
    
    buffer.position((int) startByte);
    buffer.get(bytes);
            return bytes;
    }
    /**
     * Reads at most length bytes starting at current position in the file.
     * If the current position is set beyond the end of the file, and exception should be raised.
     * If there are fewer than length bytes remaining these will be read and a smaller number of bytes than requested will be returned.
     */
     public byte[] read(long length) throws IOException{
            byte[] bytes = new byte[(int)length];
            buffer.get(bytes);
            return bytes;
    }
     /**
     * Move to byte position in file
     * Setting position to 0L will move to the start of the file. 
     * Note, it is legal to seek beyond the end of the file; if writing were supported, this is how holes are created.
     */
    public void seek(long position) throws IOException{
        f.seek((int) position);
    }
    /**
     * Returns current position in file, i.e. the byte offset from the start of the file. 
     * The file position will be zero when the file is first opened 
     * and will advance by the number of bytes read with every call to one of the read( ) routines.
     */
    public long position(){
        return position;
        
    }
    /**
     * Returns size of file as specified in filesystem.
     */
    public long size(){
        return currentFile.getFileSize();
    }
    

    
     /**
     * The following method traverses the file system based on the the filepath past into the constructor. 
     */ 
    public void traverse(FileInfo[] dirInfo) throws IOException{
        
     for(int j = 0; j<files.size();j++){ 
         
        for(int i = 0; i<dirInfo.length;i++){
            
                 if(files.get(0).equalsIgnoreCase(dirInfo[i].getFileName())){
                    currentFile = dirInfo[i];
                    int inodeTablePointer;
                    int block = 0;
                    if(dirInfo[i].getInodeNumber() > vol.getInodesPerGroup()){
                        block = dirInfo[i].getInodeNumber()/vol.getInodesPerGroup();
                        inodeTablePointer = vol.getInodeTablePointer(block);
                    }
                    else{
                        inodeTablePointer = vol.getInodeTablePointer();
                    }
                    
                    Inode inodeInfo = new Inode(inodeTablePointer, block, vol.getF(), dirInfo[i].getInodeNumber());
                    inode = inodeInfo;
                    buffs = new ArrayList<ByteBuffer>();
                    for(int x = 0; x<inodeInfo.getPointers().length;x++){
                        if(inodeInfo.getPointer(x) != 0){
                            inode = inodeInfo;
                            buffs.add(vol.goTo(inodeInfo.getPointer(x)));
                        }
                    }
                    buffer = buffs.get(0);
                    buffs = buffs.subList(1, files.size());
                    break;
                }
                 
    }             
            if(currentFile.getFileType() == 1){
                break;
            }
            else {
                Directory dir = new Directory(vol.getF(),vol.getInodeTablePointer());
                dirInfo = dir.getFileInfo(buffer);
                files = files.subList(1, files.size()); //takes first directory off the list.
                traverse(dirInfo); //method calls itself
            }
        }
    }
    
}
