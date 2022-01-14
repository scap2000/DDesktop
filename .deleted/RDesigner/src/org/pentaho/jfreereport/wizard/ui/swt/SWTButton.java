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

/**
 * @author Michael D'Amour
 * 
 */
public class SWTButton extends Composite {
  Color color1 = null;
  Color color2 = null;
  Color color3 = null;

  /**
   * @param parent
   * @param style
   */
  public SWTButton(Composite parent, int style) {
    super(parent, style);
    color1 = new Color(getDisplay(), 0, 0, 0);
    color2 = new Color(getDisplay(), 128, 128, 128);
    color3 = new Color(getDisplay(), 255, 255, 255);
    addPaintListener(new PaintListener() {
      public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        if (!isEnabled()) {
          gc.setBackground(new Color(getDisplay(), 0xf2, 0xf2, 0xf2));
          gc.setForeground(new Color(getDisplay(), 0xf2, 0xf2, 0xf2));
          gc.fillRectangle(0, 0, SWTButton.this.getBounds().width, SWTButton.this.getBounds().height);
        }
        
        // top
        gc.setForeground(color3);
        gc.drawLine(0, 0, SWTButton.this.getBounds().width, 0);
        // left
        gc.setForeground(color3);
        gc.drawLine(0, 1, 0, SWTButton.this.getBounds().height);
        // right
        gc.setForeground(color1);
        gc.drawLine(SWTButton.this.getBounds().width - 1, 0, SWTButton.this.getBounds().width - 1, SWTButton.this.getBounds().height);
        gc.setForeground(color2);
        gc.drawLine(SWTButton.this.getBounds().width - 2, 1, SWTButton.this.getBounds().width - 2, SWTButton.this.getBounds().height - 2);
        // bottom
        gc.setForeground(color1);
        gc.drawLine(0, SWTButton.this.getBounds().height - 1, SWTButton.this.getBounds().width - 1, SWTButton.this.getBounds().height - 1);
        gc.setForeground(color2);
        gc.drawLine(1, SWTButton.this.getBounds().height - 2, SWTButton.this.getBounds().width - 2, SWTButton.this.getBounds().height - 2);
        
        if (!isEnabled()) {
          gc.setForeground(new Color(getDisplay(), 255, 0, 0));
          gc.drawLine(0, SWTButton.this.getBounds().height - 1, SWTButton.this.getBounds().width - 1, 0);
        }
        
      }
    });
  }

  public void setEtchedColors(Color c1, Color c2, Color c3) {
    color1 = c1;
    color2 = c2;
    color3 = c3;
  }
}
