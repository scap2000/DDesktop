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
package org.pentaho.jfreereport.wizard.utility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.pentaho.jfreereport.wizard.ui.swt.SWTButton;

public class SWTUtility {
  public static final String COPYRIGHTMESSAGE = "Copyright \u00A9 2005-2007 Pentaho Corporation.  All rights reserved. \nThis software was developed by Pentaho Corporation and is provided under the terms\nof the Mozilla Public License, Version 1.1, or any later version. You may not use\nthis file except in compliance with the license. If you need a copy of the license,\nplease go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho BI Platform.\nThe Initial Developer is Pentaho Corporation.\n\nSoftware distributed under the Mozilla Public License is distributed on an \"AS IS\"\nbasis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to\nthe license for the specific language governing your rights and limitations."; //$NON-NLS-1$
  private static Image icon;
  private static FilenameFilter xmlFilter;
  public static final String[] REPORT_SPEC_FILTERS = new String[] { "*.xreportspec", "*.xml", "*.xreportarc" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  public static final String[] REPORT_ARCHIVE_FILTERS = new String[] { ".xreportarc", ".zip" }; //$NON-NLS-1$ //$NON-NLS-2$

  public static final String[] XML_FILTER_STRINGS = new String[] { "*.xml", "*.XML" }; //$NON-NLS-1$ //$NON-NLS-2$

  public static final String[] IMAGE_FILTER_STRINGS = new String[] { "*.jpg", "*.gif", "*.png", "*.bmp" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

  public static final String[] KETTLE_TRANSFORMATION_FILE = new String[] { "*.ktr" }; // $NON_NLS-1$

  public static void setIcon(Image image) {
    icon = image;
  }

  public static Image getIcon() {
    return icon;
  }

  public static void centerShellOnDisplay(Shell shell, Display display, int desiredWidth, int desiredHeight) {
    int screenWidth = display.getPrimaryMonitor().getBounds().width;
    int screenHeight = display.getPrimaryMonitor().getBounds().height;
    int applicationX = ((Math.abs(screenWidth - desiredWidth)) / 2);
    int applicationY = ((Math.abs(screenHeight - desiredHeight)) / 2);
    shell.setSize(desiredWidth, desiredHeight);
    shell.setLocation(applicationX, applicationY);
    if (icon != null) {
      shell.setImage(icon);
    }
  }

  public static Shell createModalDialogShell(int desiredWidth, int desiredHeight, String title) {
    Shell shell = new Shell(Display.getCurrent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    shell.setText(title);
    centerShellOnDisplay(shell, Display.getCurrent(), desiredWidth, desiredHeight);
    return shell;
  }

  public static Shell createShell(int desiredWidth, int desiredHeight, Layout layout, String title, int style) {
    Shell shell = new Shell(Display.getCurrent(), style);
    if (layout != null) {
      shell.setLayout(layout);
    }
    shell.setText(title);
    centerShellOnDisplay(shell, Display.getCurrent(), desiredWidth, desiredHeight);
    return shell;
  }

  public static void removeChildren(Composite c) {
    if (c != null) {
      Control children[] = c.getChildren();
      for (int i = 0; i < children.length; i++) {
        children[i].dispose();
      }
    }
  }

  public static void setEnabled(Composite c, boolean enabled) {
    // if (c == null) {
    // return;
    // }
    // c.setEnabled(enabled);
    // // System.out.println(c + ":" + enabled);
    // Control children[] = c.getChildren();
    // for (int i = 0; i < children.length; i++) {
    // if (children[i] instanceof Composite) {
    // setEnabled((Composite) children[i], enabled);
    // } else {
    // children[i].setEnabled(enabled);
    // // System.out.println(children[i] + ":" + enabled);
    // }
    // }
  }

  public static void setBackground(Composite c, Color background) {
    if (!(c instanceof SWTButton)) {
      c.setBackground(background);
    }
    // System.out.println(c + ":" + enabled);
    Control children[] = c.getChildren();
    for (int i = 0; i < children.length; i++) {
      if (children[i] instanceof Text) {
        // ignore if not READ_ONLY
        if ((children[i].getStyle() & SWT.READ_ONLY) == SWT.READ_ONLY) {
          children[i].setBackground(background);
        }
      } else if (children[i] instanceof Table) {
        // ignore
      } else if (children[i] instanceof Combo) {
        // ignore
      } else if (children[i] instanceof List) {
        // ignore
      } else if (children[i] instanceof Spinner) {
        // ignore
      } else if (children[i] instanceof Composite) {
        setBackground((Composite) children[i], background);
      } else if (children[i] instanceof TabFolder) {
        System.out.println("TabFolder..");
        TabFolder folder = (TabFolder) children[i];
        folder.setBackground(background);
        for (int j = 0; j < folder.getItemCount(); j++) {
          setBackground((Composite) folder.getItem(j).getControl(), background);
        }
      } else if (!(children[i] instanceof SWTButton)) {
        children[i].setBackground(background);
        // System.out.println(children[i] + ":" + enabled);
      }
    }
  }

  public static void moveSelectedItems(List sourceList, int index) {
    int selectedIndices[] = sourceList.getSelectionIndices();
    Arrays.sort(selectedIndices);
    boolean moveUp = (selectedIndices[0] - index) > 0;
    int numMove = Math.abs(selectedIndices[0] - index);
    for (int i = 0; i < numMove; i++) {
      moveSelectedItems(sourceList, moveUp);
    }
  }

  public static void moveSelectedItems(List sourceList, List destinationList) {
    int selectedIndices[] = sourceList.getSelectionIndices();
    Arrays.sort(selectedIndices);
    int newSelectedIndices[] = new int[selectedIndices.length];
    for (int i = 0; i < selectedIndices.length; i++) {
      destinationList.add(sourceList.getItem(selectedIndices[i]));
      newSelectedIndices[i] = destinationList.getItemCount() - 1;
    }
    destinationList.setSelection(newSelectedIndices);
    sourceList.remove(selectedIndices);
  }

  public static void moveSelectedItems(List list, boolean moveUp) {
    int selectedIndices[] = list.getSelectionIndices();
    Arrays.sort(selectedIndices);
    if (moveUp) {
      // invert array
      for (int left = 0, right = selectedIndices.length - 1; left < right; left++, right--) {
        // exchange the first and last
        int temp = selectedIndices[left];
        selectedIndices[left] = selectedIndices[right];
        selectedIndices[right] = temp;
      }
    }
    int newSelectedIndices[] = new int[selectedIndices.length];
    for (int i = selectedIndices.length - 1; i >= 0; i--) {
      int index = selectedIndices[i];
      if ((moveUp && index > 0) || (!moveUp && index < list.getItemCount() - 1)) {
        index = moveUp ? index - 1 : index + 1;
        String items[] = list.getItems();
        for (int j = 0; j < items.length; j++) {
          if (j == selectedIndices[i]) {
            // swap
            String item = items[j];
            items[j] = items[index];
            items[index] = item;
            j++;
          }
        }
        list.removeAll();
        list.setItems(items);
      }
      newSelectedIndices[i] = index;
    }
    list.setSelection(newSelectedIndices);
  }

  public static int getListIndexForPoint(List list, Point p) {
    int height = list.getItemHeight();
    int index = p.y / height;
    return index;
  }

  public static String getListItemForPoint(List list, Point p) {
    int height = list.getItemHeight();
    int index = p.y / height;
    try {
      return list.getItem(index);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void getChildrenOfType(java.util.List resultList, Control parent, Class classClass) {
    if (parent instanceof Composite) {
      Control children[] = ((Composite) parent).getChildren();
      for (int i = 0; i < children.length; i++) {
        if (children[i] instanceof Composite) {
          if (children[i].getClass().getName().equals(classClass.getName())) {
            resultList.add(children[i]);
          }
          getChildrenOfType(resultList, children[i], classClass);
        } else {
          if (children[i].getClass().getName().equals(classClass.getName())) {
            resultList.add(children[i]);
          }
        }
      }
    }
  }

  public static FilenameFilter getXMLFilenameFilter() {
    if (xmlFilter == null) {
      xmlFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
          if (name.toUpperCase().indexOf(".XML") >= 0) { //$NON-NLS-1$
            return true;
          }
          return false;
        }
      };
    }
    return xmlFilter;
  }

  public static String getJFreeColorString(RGB color) {
    if (color == null) {
      return "#FF000"; //$NON-NLS-1$
    }
    String r = Integer.toHexString(color.red);
    if (r.length() == 1) {
      r = "0" + r; //$NON-NLS-1$
    }
    String g = Integer.toHexString(color.green);
    if (g.length() == 1) {
      g = "0" + g; //$NON-NLS-1$
    }
    String b = Integer.toHexString(color.blue);
    if (b.length() == 1) {
      b = "0" + b; //$NON-NLS-1$
    }
    return "#" + r + g + b; //$NON-NLS-1$
  }

  public static RGB getRGB(String colorSpec) {
    int red = getRed(colorSpec);
    int green = getGreen(colorSpec);
    int blue = getBlue(colorSpec);
    return new RGB(red, green, blue);
  }

  public static int getRed(String colorSpec) {
    return Integer.parseInt(colorSpec.substring(1, 3), 16);
  }

  public static int getGreen(String colorSpec) {
    return Integer.parseInt(colorSpec.substring(3, 5), 16);
  }

  public static int getBlue(String colorSpec) {
    return Integer.parseInt(colorSpec.substring(5, 7), 16);
  }
}
