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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mustafa
 */
public class Inode {
    private RandomAccessFile f;
    
    private short fileMode;
    private short ownerUserID;
    private int filesizeLower;
    private int lastAccessTime;
    private int creationTime;
    private int lastModifiedTime;
    private int deletedTime;
    private int groupID;
    private int numberOfHardLinks;
    private int[] pointers;
    private int indirectPointer;
    private int doubleIndirectPointer;
    private int tripleIndirectPointer;
    private int filesizeUpper;
    
    private final int IFSCK = 0xC000;      // Socket
    private final int IFLNK = 0xA000;      // Symbolic Link
    private final int IFREG = 0x8000;      // Regular File
    private final int IFBLK = 0x6000;      // Block Device
    private final int IFDIR = 0x4000;      // Directory
    private final int IFCHR = 0x2000;      // Character Device
    private final int IFIFO = 0x1000;      // FIFO

    private final int ISUID = 0x0800;      // Set process User ID
    private final int ISGID = 0x0400;      // Set process Group ID
    private final int ISVTX = 0x0200;      // Sticky bit


    private final int IRUSR = 0x0100;      // User read
    private final int IWUSR = 0x0080;      // User write
    private final int IXUSR = 0x0040;      // User execute

    private final int IRGRP = 0x0020;      // Group read
    private final int IWGRP = 0x0010;      // Group write
    private final int IXGRP = 0x0008;      // Group execute

    private final int IROTH = 0x0004;      // Others read
    private final int IWOTH = 0x0002;      // Others wite
    private final int IXOTH = 0x0001;      // Others execute
    
        public Inode(int inodeTablePointer, int block, RandomAccessFile f, int inodeNumber) throws IOException{
            this.f = f;
            
            
                inodeNumber = inodeNumber - 1712*block;
            
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            f.seek(1024*inodeTablePointer + 128*(inodeNumber-1));
            byte[] inodeBlock = new byte[1024];
            f.read(inodeBlock);
            buffer.put(inodeBlock);
            
            
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            
            fileMode = buffer.getShort(0);
            getPermissions();
            ownerUserID =  buffer.getShort(2);
            filesizeLower = buffer.getInt(4);
            
            lastAccessTime = buffer.getInt(8);
            creationTime = buffer.getInt(12);
            lastModifiedTime = buffer.getInt(16);
            deletedTime = buffer.getInt(20);
            groupID = buffer.getInt(24);
            numberOfHardLinks = buffer.getInt(26);
            pointers = new int[12];
            for(int i = 0; i<12;i++){
               pointers[i]=buffer.getInt(40+(i*4));
            }
            indirectPointer = buffer.getInt(88);
            doubleIndirectPointer = buffer.getInt(92);
            tripleIndirectPointer = buffer.getInt(96);
            filesizeUpper = buffer.getInt(108);
    }
        
    
        
    
    public int getPointer(int index){
        return pointers[index];
    }
    public int[] getPointers(){
        return pointers;
    }
    
    public String getPermissions(){
        int[] othersPermissions = new int[3];
        othersPermissions[0] = IROTH;
        othersPermissions[1] = IWOTH;
        othersPermissions[2] = IXOTH;

        int[] groupPermissions = new int[3];
        groupPermissions[0] = IRGRP;
        groupPermissions[1] = IWGRP;
        groupPermissions[2] = IXGRP;
        
        int[] userPermissions = new int[3];
        userPermissions[0] = IRUSR;
        userPermissions[1] = IWUSR;
        userPermissions[2] = IXUSR;
        
        char[] permissions = new char[10];
        
        int dir = IFDIR;
        int file = IFREG;
        
        if((fileMode & dir) == fileMode){
            permissions[0] = 'd';
        }
        else{
            permissions[0] = '-';
        }
        
                if((fileMode & userPermissions[0]) == fileMode){
                    permissions[1] = 'r';
                }
                else{
                    permissions[1] = '-';
                }
                if((fileMode & userPermissions[1]) == fileMode){
                    permissions[2] = 'w';
                }
                else{
                    permissions[2] = '-';
                }
                if((fileMode & userPermissions[2]) == fileMode){
                    permissions[3] = 'x';
                }
                else{
                    permissions[3] = '-';
                }
                if((fileMode & groupPermissions[0]) == fileMode){
                    permissions[4] = 'r';
                }
                else{
                    permissions[4] = '-';
                }
                if((fileMode & groupPermissions[1]) == fileMode){
                    permissions[5] = 'w';
                }
                else{
                    permissions[5] = '-';
                }
                if((fileMode & groupPermissions[2]) == fileMode){
                    permissions[6] = 'x';
                }
                else{
                    permissions[6] = '-';
                }
                if((fileMode & othersPermissions[0]) == fileMode){
                    permissions[7] = 'r';
                }
                else{
                    permissions[7] = '-';
                }
                if((fileMode & othersPermissions[1]) == fileMode){
                    permissions[8] = 'w';
                }
                else{
                    permissions[8] = '-';
                }
                if((fileMode & othersPermissions[0]) == fileMode){
                    permissions[9] = 'x';
                }
                else{
                    permissions[9] = '-';
                }
                
                
            System.out.println(new String(permissions));
            return new String(permissions);
    }
     /**
     * The following method obtains the last Modified date. 
     */
    public int getLastModifiedDate(){
       return lastModifiedTime;
    }
     /**
     * The following method obtains the number of hardlinks. 
     */
    public int getHardlinks(){
       return numberOfHardLinks;
    }
    /**
     * The following method obtains the UserID. 
     */
    public int getUserID(){
       return ownerUserID;
    }
    /**
     * The following method obtains the groupID. 
     */
    public int getGroupID(){
       return groupID;
    }
    /**
     * The following method obtains the file size. 
     * returns the size of the associated file.
     */
    public String getFileSize(){
        String upper = Integer.toBinaryString(filesizeUpper);
        String lower = Integer.toBinaryString(filesizeLower);
        return lower.concat(upper);
    }
    
    
}
