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

import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.pentaho.jfreereport.wizard.ReportWizard;

public class SWTImageMap extends Composite implements PaintListener, MouseListener, MouseMoveListener {
  HotSpot[] hotspots;
  Image[] images;
  String[] tooltips;
  String[] text;
  int numHotspots = 0;
  int index = 0;
  Font font;
  int x = 15;
  int y = 10;
  int shadowX = 16;
  int shadowY = 11;

  /**
   * 
   */
  public SWTImageMap(Composite parent, int style, String settingsFile) {
    super(parent, style);
    FontData fd = new FontData();
    addPaintListener(this);
    addMouseListener(this);
    addMouseMoveListener(this);
    try {
      Properties p = new Properties();
      InputStream is = getClass().getClassLoader().getResourceAsStream(settingsFile);
      p.load(is);
      fd.setName(p.getProperty("font-name")); //$NON-NLS-1$
      fd.setHeight(Integer.parseInt(p.getProperty("font-size"))); //$NON-NLS-1$
      fd.setStyle(Integer.parseInt(p.getProperty("font-style"))); //$NON-NLS-1$
      font = new Font(getDisplay(), fd);
      x = Integer.parseInt(p.getProperty("label-x")); //$NON-NLS-1$
      y = Integer.parseInt(p.getProperty("label-y")); //$NON-NLS-1$
      shadowX = Integer.parseInt(p.getProperty("label-shadow-x")); //$NON-NLS-1$
      shadowY = Integer.parseInt(p.getProperty("label-shadow-y")); //$NON-NLS-1$
      numHotspots = Integer.parseInt(p.getProperty("numberOfHotspots")); //$NON-NLS-1$
      hotspots = new HotSpot[numHotspots];
      images = new Image[numHotspots];
      tooltips = new String[numHotspots];
      text = new String[numHotspots];
      InputStream imageStream = null;
      for (int i = 0; i < numHotspots; i++) {
        String hotspotPointString = p.getProperty("hotspot" + (i + 1)); //$NON-NLS-1$
        String hotspotImage = p.getProperty("hotspotImage" + (i + 1)); //$NON-NLS-1$
        String hotspotTooltip = p.getProperty("hotspotTooltip" + (i + 1)); //$NON-NLS-1$
        String hotspotText = p.getProperty("hotspotText" + (i + 1)); //$NON-NLS-1$
        StringTokenizer st = new StringTokenizer(hotspotPointString, ","); //$NON-NLS-1$
        int x1 = Integer.parseInt(st.nextToken());
        int y1 = Integer.parseInt(st.nextToken());
        int x2 = Integer.parseInt(st.nextToken());
        int y2 = Integer.parseInt(st.nextToken());
        HotSpot hs = new HotSpot(x1, y1, x2, y2);
        hotspots[i] = hs;
        try {
          imageStream = getClass().getResourceAsStream(hotspotImage);
          Image image = new Image(getDisplay(), imageStream);
          images[i] = image;
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            imageStream.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        tooltips[i] = hotspotTooltip;
        text[i] = hotspotText;
      }
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
   */
  public void paintControl(PaintEvent e) {
    GC gc = e.gc;
    gc.setBackground(ReportWizard.background);
    gc.fillRectangle(0, 0, e.width, e.height);
    gc.setFont(font);
    gc.drawImage(images[index], 0, 0);
    gc.setForeground(new Color(getDisplay(), 160, 160, 160));
    gc.drawText(text[index], shadowX, shadowY, true);
    gc.setForeground(new Color(getDisplay(), 255, 255, 255));
    gc.drawText(text[index], x, y, true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDown(MouseEvent arg0) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(MouseEvent e) {
  }

  public int getHotspotIndex(int x, int y) {
    for (int i = 0; i < hotspots.length; i++) {
      HotSpot hs = hotspots[i];
      if (hs.isContained(new Point(x, y))) {
        return i;
      }
    }
    return -1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseMove(MouseEvent e) {
    int hotspotIndex = getHotspotIndex(e.x, e.y);
    if (hotspotIndex != -1) {
      Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
      ((Control) e.widget).setCursor(cursor);
      setToolTipText(tooltips[hotspotIndex]);
    } else {
      Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_ARROW);
      ((Control) e.widget).setCursor(cursor);
      setToolTipText(""); //$NON-NLS-1$
    }
  }

  /**
   * @return Returns the stepIndex.
   */
  public int getIndex() {
    return index;
  }

  /**
   * @param stepIndex
   *          The stepIndex to set.
   */
  public void setIndex(int index) {
    this.index = index;
    redraw();
  }
}
