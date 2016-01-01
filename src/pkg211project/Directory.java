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
public class Directory {

    private int inodeTablePointer;
    RandomAccessFile f;
    FileInfo[] file;

    public Directory(RandomAccessFile file, int tablePointer) {
        inodeTablePointer = tablePointer;
        f = file;
    }

    /**
     * Returns contents of a directory in a form suited to being output in Unix
     * like format
     *
     * @author mustafa
     * @param buffer
     * @return FileInfo[]
     * @throws java.io.IOException
     */
    public FileInfo[] getFileInfo(ByteBuffer buffer) throws IOException {

        List<FileInfo> info = new ArrayList<FileInfo>();
        long capacity = 0;
        FileInfo previous = new FileInfo(0, inodeTablePointer, f, buffer);

        for (int i = 0; capacity < 1024; i++) {
            if (i == 0) {
                info.add(previous);
            } else {
                info.add(new FileInfo((int) capacity, inodeTablePointer, f, buffer));
            }

            previous = info.get(i);
            capacity += info.get(i).getLength();
        }

        for (FileInfo x : info) {
            //System.out.format("%10s%8d%2d%8d%10d%12d%12s\n", x.getPermissions(), x.getHardlinks(), x.getUserID(), x.getGroupID(), x.getFileSize(), x.getModifiedDate(), x.getFileName());
        }
        return info.toArray(new FileInfo[info.size()]);
    }

}
