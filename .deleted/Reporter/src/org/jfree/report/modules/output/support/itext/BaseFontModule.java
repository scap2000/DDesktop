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
 * BaseFontModule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.support.itext;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.fonts.itext.ITextFontRegistry;

/**
 * The module definition for the itext font processing module.
 *
 * @author Thomas Morgner
 */
public class BaseFontModule extends AbstractModule
{
  private static ITextFontRegistry fontRegistry;

  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public BaseFontModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public static synchronized ITextFontRegistry getFontRegistry()
  {
    if (fontRegistry == null)
    {
      fontRegistry = new ITextFontRegistry();
      fontRegistry.initialize();
    }
    return fontRegistry;
  }

  /**
   * Initialialize the font factory when this class is loaded and the system property of
   * <code>"org.jfree.report.modules.output.pageable.itext.PDFOutputTarget.AutoInit"</code>
   * is set to <code>true</code>.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
    if (isClassLoadable("com.lowagie.text.Document") == false)
    {
      throw new ModuleInitializeException("Unable to load iText classes. " +
              "Check your classpath configuration.");
    }

    if ("onInit".equals(subSystem.getGlobalConfig().getConfigProperty
        ("org.jfree.report.modules.output.support.itext.AutoInit")))
    {
      getFontRegistry();
    }
  }
}
