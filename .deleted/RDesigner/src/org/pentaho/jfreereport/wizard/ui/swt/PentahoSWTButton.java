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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class PentahoSWTButton extends Composite implements PaintListener, MouseTrackListener, MouseListener, MouseMoveListener {
  private static int AVERAGE_FONT_WIDTH = 7;
  String text = null;
  GridData gridData = null;
  boolean compositeWidthSet = false;
  static Font font = null;
  static Image leftActiveImage = null;
  static Image middleActiveImage = null;
  static Image rightActiveImage = null;
  static Image leftPressedImage = null;
  static Image middlePressedImage = null;
  static Image rightPressedImage = null;
  static Image leftRolloverImage = null;
  static Image middleRolloverImage = null;
  static Image rightRolloverImage = null;
  static Image leftDisabledImage = null;
  static Image middleDisabledImage = null;
  static Image rightDisabledImage = null;
  static Image rightForwardActiveImage = null;
  static Image rightForwardPressedImage = null;
  static Image rightForwardRolloverImage = null;
  static Image rightForwardDisabledImage = null;
  static Image leftBackActiveImage = null;
  static Image leftBackPressedImage = null;
  static Image leftBackRolloverImage = null;
  static Image leftBackDisabledImage = null;
  static Image upActiveImage = null;
  static Image upPressedImage = null;
  static Image upRolloverImage = null;
  static Image upDisabledImage = null;
  static Image downActiveImage = null;
  static Image downPressedImage = null;
  static Image downRolloverImage = null;
  static Image downDisabledImage = null;
  List selectionListenerList = new ArrayList();
  public static int NORMAL = 0;
  public static int FORWARD = 1;
  public static int BACK = 2;
  public static int UP = 3;
  public static int DOWN = 4;
  int mode = NORMAL;
  boolean rollover = false;
  boolean pressed = false;

  public static Image getImage(Display display, String path) {
    try {
      InputStream is = PentahoSWTButton.class.getResourceAsStream(path);
      return new Image(display, is);
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return null;
  }

  /**
   * @param parent
   * @param style
   */
  public PentahoSWTButton(Composite parent, int style, GridData gridData, int mode, String text) {
    super(parent, style);
    addPaintListener(this);
    addMouseTrackListener(this);
    addMouseListener(this);
    this.gridData = gridData;
    this.mode = mode;
    this.text = text;
    // normal active
    if (leftActiveImage == null) {
      leftActiveImage = getImage(getDisplay(), "/images/btn_left_active.png"); //$NON-NLS-1$
    }
    if (middleActiveImage == null) {
      middleActiveImage = getImage(getDisplay(), "/images/btn_middle_active.png"); //$NON-NLS-1$
    }
    if (rightActiveImage == null) {
      rightActiveImage = getImage(getDisplay(), "/images/btn_right_active.png"); //$NON-NLS-1$
    }
    // normal pressed
    if (leftPressedImage == null) {
      leftPressedImage = getImage(getDisplay(), "/images/btn_left_pressed.png"); //$NON-NLS-1$
    }
    if (middlePressedImage == null) {
      middlePressedImage = getImage(getDisplay(), "/images/btn_middle_pressed.png"); //$NON-NLS-1$
    }
    if (rightPressedImage == null) {
      rightPressedImage = getImage(getDisplay(), "/images/btn_right_pressed.png"); //$NON-NLS-1$
    }
    // normal rollover
    if (leftRolloverImage == null) {
      leftRolloverImage = getImage(getDisplay(), "/images/btn_left_rollover.png"); //$NON-NLS-1$
    }
    if (middleRolloverImage == null) {
      middleRolloverImage = getImage(getDisplay(), "/images/btn_middle_rollover.png"); //$NON-NLS-1$
    }
    if (rightRolloverImage == null) {
      rightRolloverImage = getImage(getDisplay(), "/images/btn_right_rollover.png"); //$NON-NLS-1$
    }
    // normal disabled
    if (leftDisabledImage == null) {
      leftDisabledImage = getImage(getDisplay(), "/images/btn_left_disabled.png"); //$NON-NLS-1$
    }
    if (middleDisabledImage == null) {
      middleDisabledImage = getImage(getDisplay(), "/images/btn_middle_disabled.png"); //$NON-NLS-1$
    }
    if (rightDisabledImage == null) {
      rightDisabledImage = getImage(getDisplay(), "/images/btn_right_disabled.png"); //$NON-NLS-1$
    }
    // forward active
    if (rightForwardActiveImage == null) {
      rightForwardActiveImage = getImage(getDisplay(), "/images/btn_next_right_active.png"); //$NON-NLS-1$
    }
    // forward pressed
    if (rightForwardPressedImage == null) {
      rightForwardPressedImage = getImage(getDisplay(), "/images/btn_next_right_pressed.png"); //$NON-NLS-1$
    }
    // forward rollover
    if (rightForwardRolloverImage == null) {
      rightForwardRolloverImage = getImage(getDisplay(), "/images/btn_next_right_rollover.png"); //$NON-NLS-1$
    }
    // forward disabled
    if (rightForwardDisabledImage == null) {
      rightForwardDisabledImage = getImage(getDisplay(), "/images/btn_next_right_disabled.png"); //$NON-NLS-1$
    }
    // back active
    if (leftBackActiveImage == null) {
      leftBackActiveImage = getImage(getDisplay(), "/images/btn_back_left_active.png"); //$NON-NLS-1$
    }
    // back pressed
    if (leftBackPressedImage == null) {
      leftBackPressedImage = getImage(getDisplay(), "/images/btn_back_left_pressed.png"); //$NON-NLS-1$
    }
    // back rollover
    if (leftBackRolloverImage == null) {
      leftBackRolloverImage = getImage(getDisplay(), "/images/btn_back_left_rollover.png"); //$NON-NLS-1$
    }
    // back disabled
    if (leftBackDisabledImage == null) {
      leftBackDisabledImage = getImage(getDisplay(), "/images/btn_back_left_disabled.png"); //$NON-NLS-1$
    }
    // up active
    if (upActiveImage == null) {
      upActiveImage = getImage(getDisplay(), "/images/btn_up_active.png"); //$NON-NLS-1$
    }
    // up pressed
    if (upPressedImage == null) {
      upPressedImage = getImage(getDisplay(), "/images/btn_up_pressed.png"); //$NON-NLS-1$
    }
    // up rollover
    if (upRolloverImage == null) {
      upRolloverImage = getImage(getDisplay(), "/images/btn_up_rollover.png"); //$NON-NLS-1$
    }
    // up disabled
    if (upDisabledImage == null) {
      upDisabledImage = getImage(getDisplay(), "/images/btn_up_disabled.png"); //$NON-NLS-1$
    }
    // down active
    if (downActiveImage == null) {
      downActiveImage = getImage(getDisplay(), "/images/btn_down_active.png"); //$NON-NLS-1$
    }
    // down pressed
    if (downPressedImage == null) {
      downPressedImage = getImage(getDisplay(), "/images/btn_down_pressed.png"); //$NON-NLS-1$
    }
    // down rollover
    if (downRolloverImage == null) {
      downRolloverImage = getImage(getDisplay(), "/images/btn_down_rollover.png"); //$NON-NLS-1$
    }
    // down disabled
    if (downDisabledImage == null) {
      downDisabledImage = getImage(getDisplay(), "/images/btn_down_disabled.png"); //$NON-NLS-1$
    }
    if (font == null) {
      FontData fontData = getFont().getFontData()[0];
      fontData.setHeight(10);
      fontData.setStyle(SWT.NORMAL);
      fontData.setName("LucidaSans"); //$NON-NLS-1$
      font = new Font(getDisplay(), fontData);
    }
    setFont(font);
    if (mode == UP) {
      gridData.heightHint = upActiveImage.getImageData().height;
      gridData.widthHint = upActiveImage.getImageData().width;
    } else if (mode == DOWN) {
      gridData.heightHint = downActiveImage.getImageData().height;
      gridData.widthHint = downActiveImage.getImageData().width;
    } else {
      gridData.heightHint = leftActiveImage.getImageData().height;
      gridData.widthHint = Math.max(leftActiveImage.getImageData().width + middleActiveImage.getImageData().width + rightActiveImage.getImageData().width, getTextWidth() + 60);
    }
  }

  public void setEnabled(boolean enabled) {
    if (isEnabled() != enabled) {
      redraw();
    }
    super.setEnabled(enabled);
  }

  public void addSelectionListener(SelectionListener listener) {
    selectionListenerList.add(listener);
  }

  public void removeSelectionListener(SelectionListener listener) {
    selectionListenerList.remove(listener);
  }

  public void paintControl(PaintEvent e) {
    GC gc = e.gc;
    gc.setFont(font);
    // gc.setBackground(ReportWizard.background);
    // gc.setBackground(new Color(getDisplay(), 0, 0, 0));
    // gc.fillRectangle(0, 0, e.width, e.height);
    if (isEnabled()) {
      if (mode == UP) {
        if (pressed) {
          gc.drawImage(upPressedImage, 0, 0);
        } else if (rollover) {
          gc.drawImage(upRolloverImage, 0, 0);
        } else {
          gc.drawImage(upActiveImage, 0, 0);
        }
        return;
      } else if (mode == DOWN) {
        if (pressed) {
          gc.drawImage(downPressedImage, 0, 0);
        } else if (rollover) {
          gc.drawImage(downRolloverImage, 0, 0);
        } else {
          gc.drawImage(downActiveImage, 0, 0);
        }
        return;
      }
    } else if (mode == UP) {
      gc.drawImage(upDisabledImage, 0, 0);
      return;
    } else if (mode == DOWN) {
      gc.drawImage(downDisabledImage, 0, 0);
      return;
    }
    Image leftImage = null;
    Image middleImage = null;
    Image rightImage = null;
    if (isEnabled()) {
      if (mode == BACK) {
        if (pressed) {
          leftImage = leftBackPressedImage;
        } else if (rollover) {
          leftImage = leftBackRolloverImage;
        } else {
          leftImage = leftBackActiveImage;
        }
      } else {
        if (pressed) {
          leftImage = leftPressedImage;
        } else if (rollover) {
          leftImage = leftRolloverImage;
        } else {
          leftImage = leftActiveImage;
        }
      }
      if (pressed) {
        middleImage = middlePressedImage;
      } else if (rollover) {
        middleImage = middleRolloverImage;
      } else {
        middleImage = middleActiveImage;
      }
      if (mode == FORWARD) {
        if (pressed) {
          rightImage = rightForwardPressedImage;
        } else if (rollover) {
          rightImage = rightForwardRolloverImage;
        } else {
          rightImage = rightForwardActiveImage;
        }
      } else {
        if (pressed) {
          rightImage = rightPressedImage;
        } else if (rollover) {
          rightImage = rightRolloverImage;
        } else {
          rightImage = rightActiveImage;
        }
      }
    } else {
      if (mode == BACK) {
        leftImage = leftBackDisabledImage;
      } else {
        leftImage = leftDisabledImage;
      }
      middleImage = middleDisabledImage;
      if (mode == FORWARD) {
        rightImage = rightForwardDisabledImage;
      } else {
        rightImage = rightDisabledImage;
      }
    }
    // draw left image
    gc.drawImage(leftImage, 0, 0);
    // draw middle image (stretched)
    int rightWidth = rightImage.getImageData().width;
    int leftWidth = leftImage.getImageData().width;
    int middleWidth = Math.max(gridData.widthHint - rightWidth - leftWidth, middleImage.getImageData().width);
    int middleHeight = gridData.heightHint;
    gc.drawImage(middleImage, 0, 0, middleImage.getImageData().width, middleImage.getImageData().height, leftWidth, 0, middleWidth, middleHeight);
    // draw right image
    gc.drawImage(rightImage, gridData.widthHint - rightImage.getImageData().width, 0);
    // draw string
    // need to center
    int x = (gridData.widthHint - gc.textExtent(text).x) / 2;
    int y = ((gridData.heightHint - (int)getFont().getFontData()[0].getHeight()) / 2) - 3;
    // draw shadow first
    if (isEnabled()) {
      // gc.setForeground(new Color(getDisplay(), 135, 169, 87));
      gc.setForeground(new Color(getDisplay(), 75, 86, 96));
      gc.drawText(text, x, y, true);
    } else {
      gc.setForeground(new Color(getDisplay(), 200, 200, 200));
      gc.drawText(text, x, y, true);
    }
    // gc.drawString(text, x + 1, y + 1, true);
    // gc.setForeground(new Color(getDisplay(), 255, 255, 255));
  }

  /**
   * @return Returns the text.
   */
  public String getText() {
    return text;
  }

  /**
   * @param text
   *          The text to set.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * @return Returns the leftActiveImage.
   */
  public Image getLeftActiveImage() {
    return leftActiveImage;
  }

  /**
   * @param leftActiveImage
   *          The leftActiveImage to set.
   */
  public void setLeftActiveImage(Image leftActiveImage) {
    PentahoSWTButton.leftActiveImage = leftActiveImage;
  }

  /**
   * @return Returns the middleActiveImage.
   */
  public Image getMiddleActiveImage() {
    return middleActiveImage;
  }

  /**
   * @param middleActiveImage
   *          The middleActiveImage to set.
   */
  public void setMiddleActiveImage(Image middleActiveImage) {
    PentahoSWTButton.middleActiveImage = middleActiveImage;
  }

  /**
   * @return Returns the rightActiveImage.
   */
  public Image getRightActiveImage() {
    return rightActiveImage;
  }

  /**
   * @param rightActiveImage
   *          The rightActiveImage to set.
   */
  public void setRightActiveImage(Image rightActiveImage) {
    PentahoSWTButton.rightActiveImage = rightActiveImage;
  }

  public int getTextWidth() {
    return AVERAGE_FONT_WIDTH * getText().length();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseEnter(MouseEvent e) {
    rollover = true;
    Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
    ((Control) e.widget).setCursor(cursor);
    redraw();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseExit(MouseEvent e) {
    rollover = false;
    Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_ARROW);
    ((Control) e.widget).setCursor(cursor);
    redraw();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseHover(MouseEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDoubleClick(MouseEvent e) {
    // TODO Auto-generated method stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseDown(MouseEvent e) {
    // TODO Auto-generated method stub
    pressed = true;
    redraw();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
   */
  public void mouseUp(MouseEvent e) {
    if (!getDisplay().isDisposed()) {
      Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_WAIT);
      ((Control) e.widget).setCursor(cursor);
    }
    try {
      if (e.x > this.getBounds().width || e.y > this.getBounds().height) {
        rollover = false;
      } else if (e.x < 0 || e.y < 0) {
        rollover = false;
      }
      if (isEnabled() && rollover) {
        fireSelection();
      }
      if (!getDisplay().isDisposed()) {
        Cursor cursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
        ((Control) e.widget).setCursor(cursor);
      }
      pressed = false;
      redraw();
    } catch (Exception ex) {
    }
  }

  public void fireSelection() {
    for (int i = 0; i < selectionListenerList.size(); i++) {
      Event e = new Event();
      e.widget = this;
      SelectionEvent se = new SelectionEvent(e);
      se.widget = this;
      ((SelectionListener) selectionListenerList.get(i)).widgetSelected(se);
    }
  }

  public void mouseMove(MouseEvent e) {
    // determine if mouse moved outside
    if (e.x > this.getBounds().width || e.y > this.getBounds().height) {
      rollover = false;
    } else if (e.x < 0 || e.y < 0) {
      rollover = false;
    }
  }

}
