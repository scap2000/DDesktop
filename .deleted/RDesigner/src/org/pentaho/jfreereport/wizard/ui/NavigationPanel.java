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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.jfreereport.wizard.ReportWizard;
import org.pentaho.jfreereport.wizard.ui.swt.SWTImageMap;

public class NavigationPanel extends SWTImageMap {
  ReportWizard reportWizard;

  /**
   * 
   */
  public NavigationPanel(Composite parent, int style, ReportWizard wizard) {
    super(parent, style, "NavigationPanel.properties"); //$NON-NLS-1$
    reportWizard = wizard;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(MouseEvent e) {
    switch (getHotspotIndex(e.x, e.y)) {
    case 0: {
      reportWizard.handleTabSelection(ReportWizard.TAB_TEMPLATE);
      break;
    }
    case 1: {
      reportWizard.handleTabSelection(ReportWizard.TAB_QUERY);
      break;
    }
    case 2: {
      reportWizard.handleTabSelection(ReportWizard.TAB_MAPPING);
      break;
    }
    case 3: {
      reportWizard.handleTabSelection(ReportWizard.TAB_LAYOUT);
      break;
    }
    case 4: {
      reportWizard.handleTabSelection(ReportWizard.TAB_FORMAT);
      break;
    }
    case 5: {
      reportWizard.handleTabSelection(ReportWizard.TAB_PAGE);
      break;
    }
    case 6: {
      reportWizard.handleTabSelection(ReportWizard.TAB_REPORT);
      break;
    }
    }
  }
}
