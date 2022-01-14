/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * LocaleSelectionReportControler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.internationalisation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.jfree.report.DefaultResourceBundleFactory;
import org.jfree.report.JFreeReport;
import org.jfree.report.modules.gui.base.DefaultReportController;
import org.jfree.report.modules.gui.base.PreviewPane;
import org.jfree.ui.KeyedComboBoxModel;
import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
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
 * LocaleSelectionReportControler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class LocaleSelectionReportControler extends DefaultReportController
{
  private class UpdateAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    protected UpdateAction ()
    {
      putValue(Action.NAME, "Update");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final PreviewPane base = getPreviewPane();
      if (base == null)
      {
        return;
      }
      final JFreeReport report = base.getReportJob();
      final DefaultResourceBundleFactory rfact =
              new DefaultResourceBundleFactory(getSelectedLocale());
      report.setResourceBundleFactory(rfact);
      try
      {
        base.setReportJob(report);
      }
      catch (Exception ex)
      {
        Log.error("Unable to refresh the report.", ex);
      }
    }
  }

  private KeyedComboBoxModel localesModel;
  private PreviewPane previewPane;

  public LocaleSelectionReportControler ()
  {
    final UpdateAction updateAction = new UpdateAction();
    localesModel = createLocalesModel();

    final JComboBox cbx = new JComboBox(localesModel);
    setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    add(new JLabel("Select locale:"), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    add(cbx, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    add(new ActionButton(updateAction));

  }

  private KeyedComboBoxModel createLocalesModel ()
  {
    final KeyedComboBoxModel cn = new KeyedComboBoxModel();
    final Locale[] locales = Locale.getAvailableLocales();
    for (int i = 0; i < locales.length; i++)
    {
      final Locale locale = locales[i];
      cn.add(locale, locale.getDisplayName());
    }
    cn.setSelectedKey(Locale.getDefault());
    return cn;
  }

  public Locale getSelectedLocale ()
  {
    final Locale l = (Locale) localesModel.getSelectedKey();
    if (l == null)
    {
      return Locale.getDefault();
    }
    return l;
  }

  public void setSelectedLocale (final Locale locale)
  {
    if (locale == null)
    {
      throw new NullPointerException();
    }
    localesModel.setSelectedKey(locale);
  }

  public void initialize(final PreviewPane pane)
  {
    super.initialize(pane);
    this.previewPane = pane;
  }

  public PreviewPane getPreviewPane()
  {
    return previewPane;
  }
}
