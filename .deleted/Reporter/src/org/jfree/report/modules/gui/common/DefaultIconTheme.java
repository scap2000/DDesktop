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
 * $Id: DefaultIconTheme.java 3710 2007-11-06 21:03:01Z tmorgner $
 * ------------
 * (C) Copyright 2000-2005, by Object Refinery Limited.
 * (C) Copyright 2005-2007, by Pentaho Corporation.
 */

package org.jfree.report.modules.gui.common;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.util.i18n.Messages;
import org.jfree.util.Configuration;
import org.jfree.util.ResourceBundleSupport;

/**
 * Creation-Date: 13.11.2006, 19:27:51
 *
 * @author Thomas Morgner
 */
public class DefaultIconTheme implements IconTheme
{
  private String bundleName;

  private Messages messages;

  public DefaultIconTheme()
  {
    initialize(JFreeReportBoot.getInstance().getGlobalConfig());
  }

  public void initialize(final Configuration configuration)
  {
    this.bundleName = configuration.getConfigProperty("org.jfree.report.modules.gui.common.IconThemeBundle"); //$NON-NLS-1$
  }

  public Icon getSmallIcon(final Locale locale, final String id)
  {
    return getResourceBundleSupport(locale).getIcon(id, false);
  }

  public Icon getLargeIcon(final Locale locale, final String id)
  {
    return getResourceBundleSupport(locale).getIcon(id, true);
  }

  private ResourceBundleSupport getResourceBundleSupport(final Locale locale)
  {
    if (bundleName == null)
    {
      if (messages == null)
      {
        this.messages = new Messages(GuiCommonModule.BUNDLE_NAME);
      }
      throw new IllegalStateException(messages.getErrorString("DefaultIconTheme.ERROR_0001_NO_RESOURCE_BUNDLE")); //$NON-NLS-1$
    }
    return new ResourceBundleSupport(locale, ResourceBundle.getBundle(bundleName, locale));
  }
}
