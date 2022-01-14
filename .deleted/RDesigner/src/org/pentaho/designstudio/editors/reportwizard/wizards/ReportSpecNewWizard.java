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
package org.pentaho.designstudio.editors.reportwizard.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.pentaho.designstudio.editors.reportwizard.ReportWizardPlugin;
import org.pentaho.designstudio.editors.reportwizard.messages.Messages;
import org.pentaho.jfreereport.castormodel.reportspec.ReportSpec;
import org.pentaho.jfreereport.wizard.utility.CastorUtility;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "action_xml". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class ReportSpecNewWizard extends Wizard implements INewWizard {
	private ReportSpecNewWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for ActionSequenceNewWizard.
	 */
	public ReportSpecNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new ReportSpecNewWizardPage(selection);
		addPage(page);
		
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String containerName = page.getContainerName();
    String tmpFileName = page.getFileName();
    if (tmpFileName.indexOf(".") == -1) { //$NON-NLS-1$
      tmpFileName = tmpFileName + "." + ReportSpecNewWizardPage.JFREE_REPORT_WIZ_FILE_EXTENSION; //$NON-NLS-1$
    }
    final String fileName = tmpFileName;
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
      System.out.println("Parent Exception");
      e.printStackTrace();
			Throwable realException = e.getTargetException();
      System.out.println("Child Exception");
      realException.printStackTrace();
			MessageDialog.openError(getShell(), Messages.getString("ActionSequenceNewWizard.UI_WIZ_ERROR_DLG_TITLE"), realException.getMessage()); //$NON-NLS-1$
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		String containerName,
		String fileName,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask(Messages.getString("ActionSequenceNewWizard.UI_WIZ_CREATING_MSG", fileName), 2); //$NON-NLS-1$
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(Messages.getString("ActionSequenceNewWizard.UI_WIZ_CREATING_ERROR_MSG", containerName)); //$NON-NLS-1$
		}
		IContainer container = (IContainer) resource;
    ReportSpec reportSpec = new ReportSpec();
    final IFile file = container.getFile(new Path(fileName));
    CastorUtility.getInstance().writeCastorObject(reportSpec, file.getRawLocation().toString());
//  get resource
//    IResource myresource =
//    ResourcesPlugin.getWorkspace().getRoot().findMember(myFilenameString);
//
//     then just refresh it

    resource.refreshLocal(IResource.DEPTH_INFINITE, null /*or progress
    monitor*/);

		monitor.worked(1);
		monitor.setTaskName(Messages.getString("ActionSequenceNewWizard.UI_WIZ_OPENING_MSG")); //$NON-NLS-1$
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.pentaho.designstudio.editors.actionsequence", IStatus.OK, message, null); //$NON-NLS-1$
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle( "Pentaho" ); //$NON-NLS-1$
		ImageDescriptor image = ReportWizardPlugin.getImageDescriptor( Messages.getString("ActionSequenceNewWizard.ICON_NEW_ACTION_SEQUENCE")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(image);
	}
}