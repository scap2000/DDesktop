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
 *
 * @created Feb 01, 2006
 * @author Michael D'Amour
 */
package org.pentaho.jfreereport.wizard.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.WizardManager;
import org.pentaho.jfreereport.wizard.messages.Messages;
import org.pentaho.jfreereport.wizard.ui.swt.PentahoSWTButton;
import org.pentaho.jfreereport.wizard.ui.swt.SWTLine;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

/**
 * 
 * This class is the base class for all wizard panels. WizardPanels are added to a WizardManager's "step" list. A WizardPanel may override several methods to control wizard behavior, for example, isContinueAllowed() is used to determine if
 * enough information has been filled out by the user to continue to the next step (or finish).
 * 
 */
public class WizardPanel extends Composite implements SelectionListener, IWizardListener, KeyListener {
	/**
	 * The 'Back' button on the wizard
	 */
	protected PentahoSWTButton backButton;

	/**
	 * The 'Next' button on the wizard
	 */
	protected PentahoSWTButton nextButton;

	/**
	 * The 'Finish' button on the wizard
	 */
	protected PentahoSWTButton finishButton;

	protected PentahoSWTButton publishButton;
	
	/**
	 * The 'Ok' button on the wizard
	 */
	protected PentahoSWTButton okButton;

	/**
	 * The 'Cancel' button on the wizard
	 */
	protected PentahoSWTButton cancelButton;

	/**
	 * The 'Preview' button on the wizard
	 */
	protected PentahoSWTButton previewButton;

	/**
	 * A reference to the WizardManager
	 */
	protected WizardManager wizardManager = null;

	/**
	 * An SWT Composite which is the main content area for a wizard step.
	 */
	protected Composite mainPanel = null;

	/**
	 * There are two flags that mark a wizard step, dirty and stateChanged. Dirty is used to indicate that we are too dirty to continue, it should only be used for special cases.
	 */
	public boolean dirty = false;

	/**
	 * stateChanged is the primary flag to indicate that something has changed. The purpose of this flag is to allow us to process or skip changes. When next/finish is fired we can check stateChanged.
	 */
	public boolean stateChanged = false;

	/**
	 * This list contains IDirtyListeners who care about being notified when dirtiness happens.
	 */
	List eventListenerList = new LinkedList();

	protected Font labelFont;

	protected Font textFont;

	Properties properties;

	/**
	 * Creates a new WizardPanel object.
	 * 
	 * @param parent
	 * @param style
	 * @param manager
	 */
	public WizardPanel(Composite parent, int style, WizardManager manager) {
		super(parent, style);
		initialize(manager);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * 
	 * The initialize method creates the GUI
	 * 
	 * @param manager
	 *            WizardManager that we will add ourselves to listen on and hold reference to
	 */
	public void initialize(WizardManager manager) {
		manager.addWizardListener(this);
		wizardManager = manager;
		FontData fd = new FontData();
		fd.setName(getFont().getFontData()[0].getName());
		fd.setHeight(10);
		fd.setLocale(getFont().getFontData()[0].getLocale());
		fd.setStyle(SWT.BOLD);
		labelFont = new Font(getDisplay(), fd);
		FontData fd2 = new FontData();
		fd2.setName(getFont().getFontData()[0].getName());
		fd2.setHeight(9);
		fd2.setLocale(getFont().getFontData()[0].getLocale());
		fd2.setStyle(SWT.NORMAL);
		textFont = new Font(getDisplay(), fd2);
		GridLayout gridLayout = null;
		if (ReportWizard.applicationMode == ReportWizard.MODE_DIALOG) {
			gridLayout = new GridLayout(9, false);
			setLayout(gridLayout);
			mainPanel = createContentPanel(9);
			mainPanel.setBackground(ReportWizard.background);
			SWTLine line = new SWTLine(this, SWT.NONE);
			line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
			GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
			gridData.heightHint = 5;
			gridData.horizontalSpan = 9;
			line.setLayoutData(gridData);
			line.setHorizontal(true);
			line.setBackground(ReportWizard.background);
		} else if (ReportWizard.applicationMode == ReportWizard.MODE_APPLICATION) {
			gridLayout = new GridLayout(8, false);
			setLayout(gridLayout);
			mainPanel = createContentPanel(8);
			mainPanel.setBackground(ReportWizard.background);
			SWTLine line = new SWTLine(this, SWT.NONE);
			line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
			GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
			gridData.heightHint = 5;
			gridData.horizontalSpan = 8;
			line.setLayoutData(gridData);
			line.setHorizontal(true);
			line.setBackground(ReportWizard.background);
		} else if (ReportWizard.applicationMode == ReportWizard.MODE_EMBEDDED) {
			gridLayout = new GridLayout(7, false);
			setLayout(gridLayout);
			mainPanel = createContentPanel(7);
			mainPanel.setBackground(ReportWizard.background);
			SWTLine line = new SWTLine(this, SWT.NONE);
			line.setEtchedColors(new Color(getDisplay(), new RGB(128, 128, 128)), new Color(getDisplay(), new RGB(255, 255, 255)));
			line.setHorizontal(true);
			line.setBackground(ReportWizard.background);
			GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
			gridData.heightHint = 5;
			gridData.horizontalSpan = 7;
			line.setLayoutData(gridData);
		}
		GridData gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
		gridData.widthHint = 40;
		Label l = new Label(this, SWT.NONE);
		l.setText(""); //$NON-NLS-1$
		l.setLayoutData(gridData);
		l.setBackground(ReportWizard.background);
		gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
		previewButton = new PentahoSWTButton(this, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("WizardPanel.0")); //$NON-NLS-1$
		previewButton.setLayoutData(gridData);
		previewButton.addSelectionListener(this);
		Label space = new Label(this, SWT.NONE);
		space.setText(""); //$NON-NLS-1$
		gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
		gridData.widthHint = 40;
		space.setLayoutData(gridData);
		space.setBackground(ReportWizard.background);
		gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
		backButton = new PentahoSWTButton(this, SWT.NONE, gridData, PentahoSWTButton.BACK, Messages.getString("WizardPanel.1")); //$NON-NLS-1$
		backButton.setLayoutData(gridData);
		backButton.addSelectionListener(this);
		gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
		nextButton = new PentahoSWTButton(this, SWT.NONE, gridData, PentahoSWTButton.FORWARD, Messages.getString("WizardPanel.2")); //$NON-NLS-1$
		nextButton.setLayoutData(gridData);
		nextButton.addSelectionListener(this);
		if (ReportWizard.applicationMode == ReportWizard.MODE_APPLICATION) {
			Label spacer = new Label(this, SWT.NONE);
			spacer.setText(""); //$NON-NLS-1$
			gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
			gridData.widthHint = 40;
			spacer.setLayoutData(gridData);
			spacer.setBackground(ReportWizard.background);
			gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
			publishButton = new PentahoSWTButton(this, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("WizardPanel.3")); //$NON-NLS-1$
			publishButton.setLayoutData(gridData);
			publishButton.addSelectionListener(this);
		}
		if (ReportWizard.applicationMode == ReportWizard.MODE_DIALOG) {
			Label spacer = new Label(this, SWT.NONE);
			spacer.setText(""); //$NON-NLS-1$
			gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
			gridData.widthHint = 40;
			spacer.setLayoutData(gridData);
			spacer.setBackground(ReportWizard.background);
			gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
			okButton = new PentahoSWTButton(this, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("WizardPanel.4")); //$NON-NLS-1$
			okButton.setLayoutData(gridData);
			okButton.addSelectionListener(this);
			gridData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false);
			cancelButton = new PentahoSWTButton(this, SWT.NONE, gridData, PentahoSWTButton.NORMAL, Messages.getString("WizardPanel.5")); //$NON-NLS-1$
			cancelButton.setLayoutData(gridData);
			cancelButton.addSelectionListener(this);
		}
		Label r = new Label(this, SWT.NONE);
		r.setText(""); //$NON-NLS-1$
		r.setBackground(ReportWizard.background);
		gridData = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
		r.setLayoutData(gridData);
	}

	/**
	 * 
	 * This method creates the main panel composite used by Wizard Panels.
	 * 
	 * @return Composite the main panel
	 */
	public Composite createContentPanel(int hSpan) {
		mainPanel = new Composite(this, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = hSpan;
		mainPanel.setLayoutData(gridData);
		return mainPanel;
	}

	/**
	 * 
	 * This method adds an IDirtyListener to our listener list
	 * 
	 * @param l
	 *            the IDirtyListener to add
	 */
	public void addDirtyListener(IDirtyListener l) {
		eventListenerList.add(l);
	}

	/**
	 * 
	 * This method removes an IDirtyListener from our listener list
	 * 
	 * @param l
	 *            the IDirtyListener to remove
	 */
	public void removeDirtyListener(IDirtyListener l) {
		eventListenerList.remove(l);
	}

	/**
	 * This method removes all IDirtyListeners from the event listener list
	 */
	public void removeAllDirtyListeners() {
		eventListenerList.clear();
	}

	/**
	 * Fires 'dirtyFired' on all listeners
	 */
	public void fireDirtyEvent() {
		for (int i = 0; i < eventListenerList.size(); i++) {
			IDirtyListener l = (IDirtyListener) eventListenerList.get(i);
			l.dirtyFired(dirty);
		}
	}

	/**
	 * 
	 * When any widget is selected, such as a button, a combobox, a checkbox, etc, we mark dirty/stateChanged unless the event originates from next/back/finish/cancel buttons.
	 * 
	 * @param e
	 */
	public void widgetSelected(SelectionEvent e) {
		Object source = e.getSource();
		if (source == nextButton) {
			wizardManager.next();
		} else if (source == backButton) {
			wizardManager.back();
		} else if (source == publishButton) {
			wizardManager.publish();
		} else if (source == finishButton) {
			wizardManager.finish();
		} else if (source == cancelButton) {
			wizardManager.cancel();
		} else if (source == previewButton) {
			// does it make sense to have a preview()
			wizardManager.preview();
		} else if (source == okButton) {
			wizardManager.finish();
		} else {
			stateChanged = true;
			dirty = true;
			fireDirtyEvent();
		}
		updateState();
	}

	/**
	 * This method updates the button enabled state as well as the overall panel enabled state. The buttons have their state updated based on checking if their action is allowed. If the previous wizard step does not allow continue then this
	 * step should appear to be disabled (if a GUI, like tabs, allows you to select them).
	 */
	public void updateState() {
		int myIndex = wizardManager.getSteps().indexOf(this);
		boolean enable = true;
		for (int i = 0; enable && (i < myIndex); i++) {
			WizardPanel previousStep = (WizardPanel) wizardManager.getSteps().get(i);
			enable = previousStep.isContinueAllowed();
		}
		if (!isEnabled() || !enable) {
			SWTUtility.setEnabled(this, enable);
		}
		if (backButton != null) {
			backButton.setEnabled(wizardManager.isBackAllowed());
		}
		if (nextButton != null) {
			nextButton.setEnabled(wizardManager.isNextAllowed());
		}
		if (finishButton != null) {
			finishButton.setEnabled(wizardManager.isFinishAllowed());
		}
		if (previewButton != null) {
			previewButton.setEnabled(wizardManager.isFinishAllowed());
		}
		initWizardPanel();
	}

	public void initWizardPanel() {
	}

	/**
	 * 
	 * This method is necessary to implement when implementing SelectionListener and we do not use it.
	 * 
	 * @param e
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * 
	 * This method should be overridden by subclasses to return if we are allowed to move to the next step or finish.
	 * 
	 * @return boolean
	 */
	public boolean isContinueAllowed() {
		return true;
	}

	/**
	 * 
	 * This method should be overridden by subclasses to return if we are allowed to move to the next step.
	 * 
	 * @return boolean
	 */
	public boolean isNextAllowed() {
		return true;
	}

	/**
	 * 
	 * This method should be overridden by subclasses to return if we are allowed to move back (the default behavior here is to return true, which is likely acceptable most of the time).
	 * 
	 * @return boolean
	 */
	public boolean isBackAllowed() {
		return true;
	}

	/**
	 * 
	 * This method is called when the WizardManager is told to move to the next step. This method is called on all listeners, so it may be important and useful for implements of this method in subclasses to filter out everything except
	 * itself.
	 * 
	 * @param source
	 *            The WizardPanel that the next event originated from
	 */
	public boolean nextFired(WizardPanel source) {
		updateState();
		return true;
	}

	/**
	 * 
	 * This method is called when the WizardManager is told to move to the previous step. This method is called on all listeners, so it may be important and useful for implements of this method in subclasses to filter out everything except
	 * itself.
	 * 
	 * @param source
	 *            The WizardPanel that the back event originated from
	 */
	public void backFired(WizardPanel source) {
		updateState();
	}

	/**
	 * 
	 * This method is called when the WizardManager is told to finish. This method is called on all listeners, so it may be important and useful for implements of this method in subclasses to filter out everything except itself.
	 * 
	 * @param source
	 *            The WizardPanel that the finish event originated from
	 */
	public void finishFired(WizardPanel source) {
		updateState();
	}

	/**
	 * 
	 * This method is called when the WizardManager is informed that the wizard is cancelled. This method is called on all listeners, so it may be important and useful for implements of this method in subclasses to filter out everything
	 * except itself.
	 * 
	 * @param source
	 *            The WizardPanel that the finish event originated from
	 */
	public boolean cancelFired(WizardPanel source) {
    return true;
	}

	/**
	 * 
	 * This method is called when the WizardManager is informed that the wizard is previewing. This method is called on all listeners, so it may be important and useful for implements of this method in subclasses to filter out everything
	 * except itself.
	 * 
	 * @param source
	 *            The WizardPanel that the finish event originated from
	 */
	public boolean previewFired(WizardPanel source) {
		updateState();
		return true;
	}

	public void publishFired(WizardPanel source) {
		updateState();
	}

	/**
	 * This method gets the WizardManager for this WizardPanel
	 * 
	 * @return Returns the wizardManager.
	 */
	public WizardManager getWizardManager() {
		return wizardManager;
	}

	/**
	 * This method sets the WizardManager for this WizardPanel
	 * 
	 * @param wizardManager
	 *            The wizardManager to set.
	 */
	public void setWizardManager(WizardManager wizardManager) {
		this.wizardManager = wizardManager;
	}

	/**
	 * This method gets the main panel (Composite).
	 * 
	 * @return Returns the mainPanel.
	 */
	public Composite getMainPanel() {
		return mainPanel;
	}

	/**
	 * This method sets the main panel (Composite).
	 * 
	 * @param mainPanel
	 *            The mainPanel to set.
	 */
	public void setMainPanel(Composite mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * 
	 * This method is fired when a key is pressed on components we have added the WizardPanel as a KeyListener on.
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * 
	 * This method is fired when a key is released on components we have added the WizardPanel as a KeyListener on.
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		stateChanged = true;
		dirty = true;
		fireDirtyEvent();
		wizardManager.update();
	}

}
