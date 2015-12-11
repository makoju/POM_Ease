package com.ability.ease.auto.utilities;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Convenience class for reading Unicode text files.
 * This class reads any encoding but requires a BOM at the beginning of the file.
 * If there is no BOM the default OS encoding is assumed.
 */
public class UnicodeFileReader extends UnicodeReader {

    public UnicodeFileReader(String fileName) throws FileNotFoundException {
	super(new FileInputStream(fileName));
    }


    public UnicodeFileReader(File file) throws FileNotFoundException {
	super(new FileInputStream(file));
    }


    public UnicodeFileReader(FileDescriptor fd) {
	super(new FileInputStream(fd));
    }

}
