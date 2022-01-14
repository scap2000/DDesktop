/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package org.pentaho.designstudio.editors.reportwizard;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.pentaho.designstudio.editors.reportwizard.preferences.PreferenceConstants;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.ui.IDirtyListener;

public class ReportDesignWizardEditor extends EditorPart {

  boolean isDirty;
  ReportWizard reportWizard;
  
  public ReportDesignWizardEditor() {
    super();
    // TODO Auto-generated constructor stub
  }

  public void doSave(IProgressMonitor monitor) {
    reportWizard.doSave();
    isDirty = false;
    firePropertyChange(PROP_DIRTY);
  }

  public void doSaveAs() {
    Shell shell= getSite().getShell();
    IEditorInput input= getEditorInput();

    SaveAsDialog dialog= new SaveAsDialog(shell);

    IFile original= (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
    if (original != null)
      dialog.setOriginalFile(original);

    dialog.create();


    if (dialog.open() == Window.CANCEL) {
      return;
    }

    IPath filePath= dialog.getResult();
    if (filePath == null) {
      return;
    }

    IWorkspace workspace= ResourcesPlugin.getWorkspace();
    IFile file= workspace.getRoot().getFile(filePath);

    reportWizard.doSaveAs(file.getRawLocation().toString());
    try {
      file.refreshLocal(IFile.DEPTH_ZERO, null);
    } catch (CoreException e) {
      e.printStackTrace();
    }
    firePropertyChange(PROP_DIRTY);
    isDirty = false;
    setInput(new FileEditorInput(file));
    setPartName(file.getName());
    //Need to fire this because of a bug in windows
    firePropertyChange(IEditorPart.PROP_INPUT);
  }

  public void init(IEditorSite site, IEditorInput input) throws PartInitException {
    if (!(input instanceof IFileEditorInput))
      throw new PartInitException("Invalid Input: Must be IFileEditorInput"); //$NON-NLS-1$
    setSite(site);
    setInput(input);
    setPartName(input.getName());
  }

  public boolean isDirty() {
    return isDirty;
  }

  public boolean isSaveAsAllowed() {
    return true;
  }

  
  public void createPartControl(Composite parent) {
    parent.setLayout(new FillLayout());
//    URL reportWizDir = ReportWizardPlugin.getDefault().getBundle().getEntry( "/rdw"); //$NON-NLS-1$
    URL reportWizDir = ReportWizardPlugin.getDefault().getBundle().getEntry( "/"); //$NON-NLS-1$
    try {
      reportWizDir = Platform.resolve(reportWizDir);
      File reportWizardWorkingDir = new File(Platform.resolve(reportWizDir).getPath());
      
      IFileEditorInput editorInput = (IFileEditorInput)getEditorInput();
      try {
        reportWizard = new ReportWizard(editorInput.getFile().getRawLocation().toString(), null, reportWizardWorkingDir.getAbsolutePath(), ReportWizardPlugin.getDefault().getStateLocation().toString(), null, parent, ReportWizard.MODE_EMBEDDED);
      } catch (Throwable e) {
        e.printStackTrace();
      }
      String previewFormat = ReportWizardPlugin.getDefault().getPluginPreferences().getString(PreferenceConstants.DESIGN_WIZ_PREVIEW_FORMAT);
      if ((previewFormat != null) && (previewFormat.trim().length() > 0)) {
        reportWizard.setPreviewType(previewFormat);
      }
      reportWizard.addDirtyListener(new IDirtyListener() {

        public void dirtyFired(boolean dirty) {
          isDirty = dirty;
          firePropertyChange(PROP_DIRTY);
        }
        
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setFocus() {
    // TODO Auto-generated method stub

  }

}
