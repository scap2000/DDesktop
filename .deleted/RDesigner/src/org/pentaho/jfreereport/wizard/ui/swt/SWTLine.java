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

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

public class SWTLine extends Composite {
  Color color1 = new Color(getDisplay(), 0, 0, 0);
  Color color2 = new Color(getDisplay(), 0, 0, 0);
  boolean horizontal = true;

  /**
   * @param parent
   * @param style
   */
  public SWTLine(Composite parent, int style) {
    super(parent, style);
    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        gc.setForeground(color1);
        // gc.drawLine(0, (int)(e.height-2), e.width, (int)(e.height-2));
        // gc.drawLine(0, (int)(e.height/2), e.width, (int)(e.height/2));
        int middleX = getBounds().width / 2;
        int middleY = getBounds().height / 2;
        if (horizontal) {
          gc.drawLine(0, middleY, getBounds().width, middleY);
        } else {
          gc.drawLine(middleX, 0, middleX, getBounds().height);
        }
        gc.setForeground(color2);
        // gc.drawLine(0, (int)(e.height-1), e.width, (int)(e.height-1));
        // gc.drawLine(0, (int)(e.height/2)+1, e.width, (int)(e.height/2)+1);
        if (horizontal) {
          gc.drawLine(0, middleY+1, getBounds().width, middleY+1);
        } else {
          gc.drawLine(middleX+1, 0, middleX+1, getBounds().height);
        }
      }
    });
  }

  public void setHorizontal(boolean horizontal) {
    this.horizontal = horizontal;
  }

  public void setEtchedColors(Color primary, Color secondary) {
    color1 = primary;
    color2 = secondary;
  }
}
