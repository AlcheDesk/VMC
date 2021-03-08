package com.meowlomo.vmc.file;

import java.nio.file.Path;
import java.util.List;

public interface FileService {
    // create and write file
    boolean create(Path path, byte[] fileContent, boolean overwrite);

    // append a file content to a existing file
    boolean appendToFile(Path path, byte[] fileContent);

    // read a file
    byte[] readFile(Path path);

    // create a directory
    boolean createDirectory(Path path);

    // check permission
    boolean isReadable(Path path);

    boolean isWritable(Path path);

    // if the path exist
    boolean exist(Path path);

    // get file server free space
    long getFreeSpace();

    // rename a file or directory
    boolean rename(Path originalPath, String newname, boolean overwrite);

    // delete a file or a directory
    boolean delete(Path path, boolean recursive);

    // list a directory
    List<Path> listFile(Path path, String searchPattern);

    // check is file
    boolean isFile(Path path);

    // check is directory
    boolean folderExists(Path path);

    // file exists
    boolean fileExists(Path path);

    boolean accessiable();
}
