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
package org.pentaho.designstudio.editors.reportwizard.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.pentaho.designstudio.editors.reportwizard.ReportWizardPlugin;
import org.pentaho.designstudio.editors.reportwizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ReportWizard;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class ReportWizardPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  static final String[][] RDW_PREVIEW_STYLES = new String[][]{{Messages.getString("ActionSequencePreferencePage.PDF_FORMAT"), ReportWizard.PREVIEW_TYPE_PDF}, {Messages.getString("ActionSequencePreferencePage.HTML_FORMAT"), ReportWizard.PREVIEW_TYPE_HTML}, {Messages.getString("ActionSequencePreferencePage.EXCEL_FORMAT"), ReportWizard.PREVIEW_TYPE_XLS}}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public ReportWizardPreferencePage() {
		super(GRID);
		setPreferenceStore(ReportWizardPlugin.getDefault().getPreferenceStore());
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
    addField(new RadioGroupFieldEditor(PreferenceConstants.DESIGN_WIZ_PREVIEW_FORMAT, Messages.getString("ActionSequencePreferencePage.PREVIEW_FORMAT"), RDW_PREVIEW_STYLES.length, RDW_PREVIEW_STYLES, getFieldEditorParent())); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}