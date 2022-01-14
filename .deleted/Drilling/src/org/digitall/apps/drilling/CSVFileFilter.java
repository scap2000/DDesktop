package org.digitall.apps.drilling;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CSVFileFilter extends FileFilter {

    public CSVFileFilter() {
    }

    public boolean accept(File f) {
	return f.getName().toLowerCase().endsWith("csv") || f.isDirectory();
    }

    public String getDescription() {
	return "CSV file";
    }

}
