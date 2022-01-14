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
 * ClassKeyEditor.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.gui.config.editor;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * The class key editor is used to edit report configuration keys which take the name of
 * an class as value.
 *
 * @author Thomas Morgner
 */
public class ClassKeyEditor extends TextKeyEditor
{
  /**
   * The base class, to which all value classes must be assignable.
   */
  private Class baseClass;

  /**
   * Creates a new class key editor for the given entry and configuration. The given
   * display name will be used as label text.
   *
   * @param config      the report configuration.
   * @param entry       the configuration description entry that describes the key
   * @param displayName the text for the label
   */
  public ClassKeyEditor (final HierarchicalConfiguration config,
                         final ClassConfigDescriptionEntry entry,
                         final String displayName)
  {
    super(config, entry, displayName);
    baseClass = entry.getBaseClass();
    if (baseClass == null)
    {
      Log.warn("Base class undefined, defaulting to java.lang.Object"); //$NON-NLS-1$
      baseClass = Object.class;
    }
    validateContent();
  }

  /**
   * Checks, whether the given value is a valid classname and is assignable from the base
   * class.
   *
   * @see org.jfree.report.modules.gui.config.editor.TextKeyEditor#validateContent()
   */
  public void validateContent ()
  {
    if (baseClass == null)
    {
      // validate is called before the baseclass is set ... ugly!
      return;
    }
    try
    {
      final Class c = ObjectUtilities.getClassLoader(getClass()).loadClass(getContent());
      setValidInput(baseClass.isAssignableFrom(c));
    }
    catch (Exception e)
    {
      // ignored ..
      setValidInput(false);
    }
    // Log.debug ("Validate ClassContent:" + getContent() + " is Valid: " + isValidInput());
  }
}
