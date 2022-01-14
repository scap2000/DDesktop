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
 * EncodingSelector.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.gui.plaintext;

import java.awt.BorderLayout;

import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jfree.fonts.encoding.EncodingRegistry;
import org.jfree.report.modules.gui.commonswing.EncodingComboBoxModel;
import org.jfree.report.modules.output.pageable.plaintext.helper.PrinterEncoding;
import org.jfree.report.modules.output.pageable.plaintext.helper.PrinterSpecification;
import org.jfree.report.util.i18n.Messages;

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
 * EncodingSelector.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class EncodingSelector extends JPanel
{
  /**
   * Provides access to externalized strings
   */
  public static final Messages MESSAGES = new Messages(PlainTextExportGUIModule.BUNDLE_NAME);
  
  public static class GenericPrinterSpecification
          implements PrinterSpecification
  {
    private static final byte[] EMPTY_ARRAY = new byte[0];

    public GenericPrinterSpecification ()
    {
    }

    public String getDisplayName ()
    {
      return getName();
    }

    /**
     * Returns the encoding definition for the given java encoding.
     *
     * @param encoding the java encoding that should be mapped into a printer specific
     *                 encoding.
     * @return the printer specific encoding.
     *
     * @throws IllegalArgumentException if the given encoding is not supported.
     */
    public PrinterEncoding getEncoding (final String encoding)
    {
      if (isEncodingSupported(encoding) == false)
      {
        throw new IllegalArgumentException(MESSAGES.getErrorString("EncodingSelector.ERROR_0001_ENCODING_NOT_SUPPORTED")); //$NON-NLS-1$
      }

      return new PrinterEncoding(encoding, encoding, encoding, EMPTY_ARRAY);
    }

    /**
     * Returns the name of the encoding mapping. This is usually the same as the printer
     * model name.
     *
     * @return the printer model.
     */
    public String getName ()
    {
      return MESSAGES.getString("EncodingSelector.USER_GENERIC_PRINTER"); //$NON-NLS-1$
    }

    /**
     * Checks, whether the given Java-encoding is supported.
     *
     * @param encoding the java encoding that should be mapped into a printer specific
     *                 encoding.
     * @return true, if there is a mapping, false otherwise.
     */
    public boolean isEncodingSupported (final String encoding)
    {
      if (EncodingRegistry.getInstance().isSupportedEncoding(encoding))
      {
        // if already checked there, then use it ...
        return true;
      }
      return false;
    }

    /**
     * Returns true, if a given operation is supported, false otherwise.
     *
     * @param operationName the operation, that should be performed
     * @return true, if the printer will be able to perform that operation, false
     *         otherwise.
     */
    public boolean isFeatureAvailable (final String operationName)
    {
      // we accept the first default that is offered, we do not even check the operation
      return true;
    }
  }

  private EncodingComboBoxModel encodingComboBoxModel;
  private JComboBox encodingComboBox;

  /**
   * Create a new JPanel with a double buffer and a flow layout
   */
  public EncodingSelector ()
  {
    setLayout(new BorderLayout());
    encodingComboBox = new JComboBox();
    add(encodingComboBox, BorderLayout.CENTER);
    setEncodings(new GenericPrinterSpecification(), Locale.getDefault());
  }

  public String getSelectedEncoding ()
  {
    return encodingComboBoxModel.getSelectedEncoding();
  }

  public void setSelectedEncoding (final String encoding)
  {
    this.encodingComboBoxModel.setSelectedEncoding(encoding);
  }

  public void setEncodings (final PrinterSpecification printerSpecification,
                            final Locale locale)
  {
    if (printerSpecification == null)
    {
      throw new NullPointerException(MESSAGES.getErrorString("EncodingSelector.ERROR_0002_NULL_SPECIFICATION")); //$NON-NLS-1$
    }
    final EncodingComboBoxModel defaultEncodingModel = EncodingComboBoxModel.createDefaultModel(locale);

    final EncodingComboBoxModel retval = new EncodingComboBoxModel(locale);
    for (int i = 0; i < defaultEncodingModel.getSize(); i++)
    {
      final String encoding = defaultEncodingModel.getEncoding(i);
      if (printerSpecification.isEncodingSupported(encoding))
      {
        final String description = defaultEncodingModel.getDescription(i);
        retval.addEncoding(encoding, description);
      }
    }
    retval.sort();
    final Object oldSelectedValue = encodingComboBox.getSelectedItem();
    encodingComboBox.setModel(retval);
    encodingComboBoxModel = retval;
    encodingComboBoxModel.setSelectedItem(oldSelectedValue);
  }
}
