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
 * GroupReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.Group;
import org.jfree.report.GroupList;
import org.jfree.report.ReportDefinition;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.modules.parser.base.common.AbstractPropertyXmlReadHandler;
import org.jfree.report.modules.parser.base.common.GroupFieldsReadHandler;
import org.jfree.xmlns.parser.XmlReadHandler;

import org.xml.sax.SAXException;

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
 * GroupReadHandler.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
public class GroupReadHandler extends AbstractPropertyXmlReadHandler
{
  /**
   * Literal text for an XML report element.
   */
  public static final String GROUP_HEADER_TAG = "groupheader";

  /**
   * Literal text for an XML report element.
   */
  public static final String GROUP_FOOTER_TAG = "groupfooter";

  /**
   * Literal text for an XML report element.
   */
  public static final String FIELDS_TAG = "fields";

  /**
   * Literal text for an XML report element.
   */
  public static final String FIELD_TAG = "field";


  private static final String NAME_ATT = "name";

  private GroupList groupList;
  private Group group;

  public GroupReadHandler (final GroupList groupList)
  {
    this.groupList = groupList;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException
  {
    final String groupName = attrs.getValue(getUri(), NAME_ATT);
    if (groupName != null)
    {
      final ReportDefinition report = (ReportDefinition)
              getRootHandler().getHelperObject(ReportParserUtil.HELPER_OBJ_REPORT_NAME);
      group = report.getGroups().getGroupByName(groupName);
      if (group == null)
      {
        group = new Group();
        group.setName(groupName);
      }
    }
    else
    {
      group = new Group();
    }
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected XmlReadHandler getHandlerForChild (final String uri,
                                               final String tagName,
                                               final PropertyAttributes atts)
          throws  SAXException
  {
    if (getUri().equals(uri) == false)
    {
      return null;
    }

    if (tagName.equals(GROUP_HEADER_TAG))
    {
      return new GroupHeaderReadHandler(group.getHeader());
    }
    else if (tagName.equals(GROUP_FOOTER_TAG))
    {
      return new GroupFooterReadHandler(group.getFooter());
    }
    else if (tagName.equals(FIELDS_TAG))
    {
      return new GroupFieldsReadHandler(group);
    }
    return null;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void doneParsing ()
          throws SAXException
  {
    groupList.add(group);
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return group;
  }
}
