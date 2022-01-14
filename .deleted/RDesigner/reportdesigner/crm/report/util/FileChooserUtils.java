/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.commands.ReportFileView;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 22.02.2006
 * Time: 15:48:29
 */
public class FileChooserUtils
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(FileChooserUtils.class.getName());


    private FileChooserUtils()
    {
    }


    @Nullable
    public static File requestSaveFile(@NotNull JComponent parent, @NotNull WorkspaceSettings workspaceSettings, @NotNull String lastAccessedFileKey, @NotNull final String fileEnding, @NotNull final String fileTypeDescription)
    {
        return requestSaveFile(parent, workspaceSettings, lastAccessedFileKey, fileEnding, fileTypeDescription, true);
    }


    @Nullable
    public static File requestSaveFile(@NotNull JComponent parent, @NotNull WorkspaceSettings workspaceSettings, @NotNull String lastAccessedFileKey, @NotNull final String fileEnding, @NotNull final String fileTypeDescription, boolean warnExistingFile)
    {
        File f = getGuessedPath(workspaceSettings, lastAccessedFileKey);
        JFileChooser fileChooser;

        if (f == null || !f.canWrite())
        {
            if (f != null)
            {
                fileChooser = new JFileChooser(f);
                fileChooser.setSelectedFile(f);
            }
            else
            {
                fileChooser = new JFileChooser();
            }
        }
        else
        {
            fileChooser = new JFileChooser(f);
            fileChooser.setSelectedFile(f);
        }

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter()
        {
            public boolean accept(@NotNull File f)
            {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(fileEnding);
            }


            @NotNull
            public String getDescription()
            {
                return fileTypeDescription;
            }
        });

        fileChooser.setFileView(new ReportFileView());

        int option = fileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.getName().toLowerCase().endsWith(fileEnding))
            {
                selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + fileEnding);
            }

            if (warnExistingFile && selectedFile.exists())
            {
                int warnOption = JOptionPane.showConfirmDialog(parent,
                                                               TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.Message"),
                                                               TranslationManager.getInstance().getTranslation("R", "FileChooser.Overwrite.Title"),
                                                               JOptionPane.OK_CANCEL_OPTION,
                                                               JOptionPane.WARNING_MESSAGE);
                if (warnOption != JOptionPane.OK_OPTION)
                {
                    return null;
                }
            }

            try
            {
                workspaceSettings.put(lastAccessedFileKey, selectedFile.getCanonicalPath());
            }
            catch (IOException e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FileChooserUtils.requestSaveFile ", e);
            }
            return selectedFile;
        }
        else
        {
            return null;
        }
    }


    @Nullable
    private static File getGuessedPath(@NotNull WorkspaceSettings workspaceSettings, @NotNull String lastAccessedFileKey)
    {
        String fileName = workspaceSettings.getString(lastAccessedFileKey);
        if (fileName != null)
        {
            return new File(fileName);
        }
        return null;
    }
}
