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
 * ConfigReferenceTableModel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.referencedoc;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

import javax.xml.parsers.ParserConfigurationException;

import org.jfree.report.modules.gui.config.ConfigEditor;
import org.jfree.report.modules.gui.config.model.ClassConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.ConfigDescriptionModel;
import org.jfree.report.modules.gui.config.model.EnumConfigDescriptionEntry;
import org.jfree.report.modules.gui.config.model.TextConfigDescriptionEntry;
import org.jfree.util.ObjectUtilities;

import org.xml.sax.SAXException;

/**
 * Creation-Date: 02.10.2007, 20:59:09
 *
 * @author Thomas Morgner
 */
public class ConfigReferenceTableModel extends AbstractTableModel
{
  private static class ConfigEntry implements Comparable
  {
    private Boolean global;
    private String keyName;
    private String description;
    private String type;
    private String value;

    public ConfigEntry(final Boolean global,
                       final String keyName,
                       final String description,
                       final String type,
                       final String value)
    {
      if (global == null)
      {
        throw new NullPointerException();
      }
      if (keyName == null)
      {
        throw new NullPointerException();
      }
      this.global = global;
      this.keyName = keyName;
      this.description = description;
      this.type = type;
      this.value = value;
    }

    public int compareTo(final Object o)
    {
      final ConfigEntry co = (ConfigEntry) o;
      if (Boolean.FALSE.equals(global) && Boolean.TRUE.equals(co.global))
      {
        return 1;
      }
      if (Boolean.TRUE.equals(global) && Boolean.FALSE.equals(co.global))
      {
        return -1;
      }
      return keyName.compareTo(co.keyName);
    }

    public Boolean getGlobal()
    {
      return global;
    }

    public String getKeyName()
    {
      return keyName;
    }

    public String getDescription()
    {
      return description;
    }

    public String getType()
    {
      return type;
    }

    public String getValue()
    {
      return value;
    }
  }

  private ArrayList entries;

  public ConfigReferenceTableModel() throws IOException, ParserConfigurationException, SAXException
  {
    final ConfigDescriptionModel model = new ConfigDescriptionModel();
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
        (ConfigEditor.CONFIG_DESCRIPTION_FILENAME, ConfigEditor.class);
    if (in == null)
    {
      throw new IllegalStateException("Fatal: No model found");
    }
    try
    {
      model.load(in);
    }
    finally
    {
      in.close();
    }

    entries = new ArrayList();
//    entries.add(new ConfigEntry(Boolean.TRUE, "org.jfree.Key", "aasdoa asdasd kawkea aqw asihasldk aiwer aslkdhawoie alsdh aiwhaldhasl kdhaoweh alsdknasl khiowehalksd", "class", "org.free.Cass.asd.asd.asd.asd.asd.asd"));
    final int size = model.getSize();
    for (int i = 0; i < size; i++)
    {
      final ConfigDescriptionEntry o = model.get(i);
      if (o.isHidden())
      {
        continue;
      }
      final String keyName = o.getKeyName();
      final String description = o.getDescription();
      final Boolean global;
      if (o.isGlobal())
      {
        global = Boolean.TRUE;
      }
      else
      {
        global = Boolean.FALSE;
      }
      if (o instanceof TextConfigDescriptionEntry)
      {
        entries.add(new ConfigEntry(global, keyName, description, "text", null));
      }
      else if (o instanceof ClassConfigDescriptionEntry)
      {
        final ClassConfigDescriptionEntry co = (ClassConfigDescriptionEntry) o;
        entries.add(new ConfigEntry(global, keyName, description, "class", co.getBaseClass().getName()));
      }
      else if (o instanceof EnumConfigDescriptionEntry)
      {
        final EnumConfigDescriptionEntry co = (EnumConfigDescriptionEntry) o;
        final String[] strings = co.getOptions();
        final StringBuffer b = new StringBuffer();
        for (int j = 0; j < strings.length; j++)
        {
          if (j != 0)
          {
            b.append(", ");
          }
          b.append(strings[j]);
        }
        entries.add(new ConfigEntry(global, keyName, description, "enum", b.toString()));
      }
    }

    Collections.sort(entries);
  }


  public String getColumnName(final int column)
  {
    switch (column)
    {
      case 0:
        return "key-name";
      case 1:
        return "description";
      case 2:
        return "global";
      case 3:
        return "type";
      case 4:
        return "value";
      default:
        throw new IndexOutOfBoundsException();
    }
  }


  public Class getColumnClass(final int columnIndex)
  {
    switch (columnIndex)
    {
      case 0:
        return String.class;
      case 1:
        return String.class;
      case 2:
        return Boolean.class;
      case 3:
        return String.class;
      case 4:
        return String.class;
      default:
        throw new IndexOutOfBoundsException();
    }
  }


  public int getRowCount()
  {
    return entries.size();
  }

  public int getColumnCount()
  {
    return 5;
  }

  public Object getValueAt(final int rowIndex, final int columnIndex)
  {
    final ConfigEntry o = (ConfigEntry) entries.get(rowIndex);

    switch (columnIndex)
    {
      case 0:
        return o.getKeyName();
      case 1:
        return o.getDescription();
      case 2:
        return o.getGlobal();
      case 3:
        return o.getType();
      case 4:
        return o.getValue();
      default:
        throw new IndexOutOfBoundsException();
    }
  }
}
