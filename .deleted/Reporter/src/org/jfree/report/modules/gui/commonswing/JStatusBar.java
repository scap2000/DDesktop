/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: JStatusBar.java 3185 2007-08-15 16:43:22Z dkincade $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */
package org.jfree.report.modules.gui.commonswing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;

import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jfree.report.modules.gui.common.DefaultIconTheme;
import org.jfree.report.modules.gui.common.IconTheme;

/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2000-2007, by Object Refinery Limited, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: JStatusBar.java 3185 2007-08-15 16:43:22Z dkincade $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */
public class JStatusBar extends JComponent
{
  private JComponent otherComponents;
  private JLabel statusHolder;
  private IconTheme iconTheme;
  private StatusType statusType;

  public JStatusBar()
  {
    this(new DefaultIconTheme());
  }

  public JStatusBar (final IconTheme theme)
  {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createMatteBorder
            (1, 0, 0, 0, UIManager.getDefaults().getColor("controlShadow"))); //$NON-NLS-1$
    statusHolder = new JLabel(" "); //$NON-NLS-1$
    statusHolder.setMinimumSize(new Dimension(0, 20));
    add(statusHolder, BorderLayout.CENTER);

    otherComponents = new JPanel();
    add(otherComponents, BorderLayout.EAST);
    this.iconTheme = theme;
    this.statusType = StatusType.NONE;
  }

  protected IconTheme getIconTheme()
  {
    return iconTheme;
  }

  public void setIconTheme(final IconTheme iconTheme)
  {
    final IconTheme oldTheme = this.iconTheme;
    this.iconTheme = iconTheme;
    firePropertyChange("iconTheme", oldTheme, iconTheme); //$NON-NLS-1$

    if (iconTheme == null)
    {
      statusHolder.setIcon(null);
    }
    else
    {
      updateTypeIcon(getStatusType());
    }
  }

  public JComponent getExtensionArea ()
  {
    return otherComponents;
  }

  public StatusType getStatusType()
  {
    return statusType;
  }

  public String getStatusText()
  {
    return statusHolder.getText();
  }

  public void setStatusText (final String text)
  {
    final String oldText = statusHolder.getText();
    this.statusHolder.setText(text);
    firePropertyChange("statusText", oldText, text); //$NON-NLS-1$
  }

  public void setStatusType (final StatusType type)
  {
    if (statusType == null)
    {
      throw new NullPointerException();
    }
    final StatusType oldType = statusType;
    this.statusType = type;
    firePropertyChange("statusType", oldType, type); //$NON-NLS-1$
    updateTypeIcon(type);
  }

  public void setStatus (final StatusType type, final String text)
  {
    this.statusType = type;
    updateTypeIcon(type);
    statusHolder.setText(text);
  }

  private void updateTypeIcon(final StatusType type)
  {
    if (iconTheme != null)
    {
      if (type == StatusType.ERROR)
      {
        final Icon res = getIconTheme().getSmallIcon(getLocale(), "statusbar.errorIcon"); //$NON-NLS-1$
        statusHolder.setIcon(res);
      }
      else if (type == StatusType.WARNING)
      {
        final Icon res = getIconTheme().getSmallIcon(getLocale(), "statusbar.warningIcon"); //$NON-NLS-1$
        statusHolder.setIcon(res);
      }
      else if (type == StatusType.INFORMATION)
      {
        final Icon res = getIconTheme().getSmallIcon(getLocale(), "statusbar.informationIcon"); //$NON-NLS-1$
        statusHolder.setIcon(res);
      }
      else
      {
        final Icon res = getIconTheme().getSmallIcon(getLocale(), "statusbar.otherIcon"); //$NON-NLS-1$
        statusHolder.setIcon(res);
      }
    }
  }

  public void clear ()
  {
    setStatus(StatusType.NONE, " "); //$NON-NLS-1$
  }

  /**
   * Gets the locale of this component.
   *
   * @return this component's locale; if this component does not have a locale,
   *         the locale of its parent is returned
   * @throws java.awt.IllegalComponentStateException
   *          if the <code>Component</code> does not have its own locale and has
   *          not yet been added to a containment hierarchy such that the locale
   *          can be determined from the containing parent
   * @see #setLocale
   * @since JDK1.1
   */
  public Locale getLocale()
  {
    try
    {
      return super.getLocale();
    }
    catch(IllegalComponentStateException ice)
    {
      return Locale.getDefault();
    }
  }
}
