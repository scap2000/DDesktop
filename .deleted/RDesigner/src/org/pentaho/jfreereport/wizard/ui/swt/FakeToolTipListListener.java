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

package org.pentaho.jfreereport.wizard.ui.swt;

import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.jfreereport.wizard.utility.SWTUtility;

public class FakeToolTipListListener implements Listener {
  Shell tip = null;
  Label label = null;
  List list = null;
  HashMap nameMap = null;
  
  public FakeToolTipListListener(List list, HashMap nameMap) {
    setList(list);
    this.nameMap = nameMap;
  }
  
  public void setList(List list) {
    this.list = list;
  }

  public void handleEvent(Event event) {
    switch (event.type) {
    case SWT.Dispose:
    case SWT.KeyDown:
    case SWT.MouseMove: {
      if (tip == null)
        break;
      tip.dispose();
      tip = null;
      label = null;
      break;
    }
    case SWT.MouseHover: {
      Point p = new Point(event.x, event.y);
      String item = SWTUtility.getListItemForPoint(list, p);
      if (item != null) {
        if (tip != null && !tip.isDisposed())
          tip.dispose();
        tip = new Shell(list.getShell(), SWT.ON_TOP | SWT.TOOL);
        tip.setLayout(new FillLayout());
        label = new Label(tip, SWT.NONE);
        label.setForeground(list.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
        label.setBackground(list.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        String labelText = (String)nameMap.get(item);
        if (labelText == null) {
          labelText = item;
        }
        label.setText(labelText);
        Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Rectangle rect = event.getBounds();
        Point pt = list.toDisplay(rect.x, rect.y);
        tip.setBounds(pt.x + 20, pt.y + 20, size.x, size.y);
        tip.setVisible(true);
      }
    }
    }
  }
}
