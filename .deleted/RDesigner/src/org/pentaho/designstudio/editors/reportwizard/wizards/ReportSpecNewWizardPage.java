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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.pentaho.designstudio.editors.reportwizard.messages.Messages;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (xaction).
 */

public class ReportSpecNewWizardPage extends WizardPage {
	public static final String BASE_FILENAME = Messages.getString("ReportSpecNewWizardPage.UI_UNTITLED_REPORT_SPEC_NAME"); //$NON-NLS-1$
  public static final String JFREE_REPORT_WIZ_FILE_EXTENSION = "xreportspec"; //$NON-NLS-1$
	
	private Text containerText;
	private Text fileText;
	private ISelection selection;	

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ReportSpecNewWizardPage(ISelection selection) {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.getString("ReportSpecNewWizardPage.UI_WIZ_TITLE")); //$NON-NLS-1$
		setDescription(Messages.getString("ReportSpecNewWizardPage.UI_WIZ_DESCRIPTION")); //$NON-NLS-1$
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("ReportSpecNewWizardPage.UI_WIZ_CONTAINER_LABEL")); //$NON-NLS-1$

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText(Messages.getString("ReportSpecNewWizardPage.UI_WIZ_BROWSE_LABEL")); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("ReportSpecNewWizardPage.UI_WIZ_FILE_NAME_LABEL")); //$NON-NLS-1$

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
						
		initialize();
		dialogChanged();
		setControl(container);
	}
	
	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;

			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());

				String modelFilename = BASE_FILENAME + "." + JFREE_REPORT_WIZ_FILE_EXTENSION; //$NON-NLS-1$
				for (int i = 1; container.findMember(modelFilename) != null; ++i) {
					modelFilename = BASE_FILENAME + i + "." + JFREE_REPORT_WIZ_FILE_EXTENSION; //$NON-NLS-1$
				}
				fileText.setText(modelFilename);
			}			
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				Messages.getString("ActionSequenceNewWizardPage.UI_WIZ_NEW_CONTAINER_DLG_MSG")); //$NON-NLS-1$
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus(Messages.getString("ActionSequenceNewWizardPage.UI_WIZ_NO_CONTAINER_ERROR_MSG")); //$NON-NLS-1$
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus(Messages.getString("ActionSequenceNewWizardPage.UI_WIZ_CONTAINER_NOT_EXIST_MSG")); //$NON-NLS-1$
			return;
		}
		if (!container.isAccessible()) {
			updateStatus(Messages.getString("ActionSequenceNewWizardPage.UI_WIZ_PROJ_NOT_WRITABLE_MSG")); //$NON-NLS-1$
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(Messages.getString("ActionSequenceNewWizardPage.UI_WIZ_NO_FILENAME_ERROR_MSG")); //$NON-NLS-1$
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus(Messages.getString("ActionSequenceNewWizardPage.UI_WIZ_INVALID_FILENAME_MSG")); //$NON-NLS-1$
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase(JFREE_REPORT_WIZ_FILE_EXTENSION) == false) { 
				updateStatus(Messages.getString("ReportSpecNewWizardPage.UI_WIZ_INVALID_FILE_EXT_MSG")); //$NON-NLS-1$
				return;
			}
		}
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}
}