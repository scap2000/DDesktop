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
 * ComponentDrawableFilter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.filter;

import java.awt.Component;

import javax.swing.JFrame;

import org.jfree.report.JFreeReportBoot;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.util.ComponentDrawable;
import org.jfree.util.Configuration;

/**
 * A filter that wraps AWT- and Swing-components into a Drawable implementation.
 *
 * @author Thomas Morgner
 */
public class ComponentDrawableFilter implements DataFilter
{
  /** The datasource from where to read the urls. */
  private DataSource source;
  /** The Window-Peer used for the draw operation. */
  private JFrame frame;

  /**
   * Default constructor.
   */
  public ComponentDrawableFilter()
  {
  }

  /**
   * Returns the ComponentDrawable for the AWT-Component or null.
   *
   * @param runtime the expression runtime that is used to evaluate formulas and expressions when computing the value of
   *                this filter.
   * @return the value.
   */
  public Object getValue(final ExpressionRuntime runtime)
  {
    if (isHeadless())
    {
      return null;
    }

    if (runtime == null)
    {
      return null;
    }

    final DataSource ds = getDataSource();
    if (ds == null)
    {
      return null;
    }
    final Object o = ds.getValue(runtime);
    if (o == null)
    {
      return null;
    }

    if (o instanceof Component == false)
    {
      return null;
    }


    final Configuration config = runtime.getConfiguration();
    final ComponentDrawable cd;
    final String drawMode = config.getConfigProperty("org.jfree.report.ComponentDrawableMode", "shared");
    if ("private".equals(drawMode))
    {
      cd = new ComponentDrawable();
    }
    else if ("synchronized".equals(drawMode))
    {
      cd = new ComponentDrawable();
      cd.setPaintSynchronized(true);
    }
    else
    {
      if (frame == null)
      {
        frame = new JFrame();
      }
      cd = new ComponentDrawable(frame);
      cd.setPaintSynchronized(true);
    }

    final String allowOwnPeer = config.getConfigProperty("org.jfree.report.AllowOwnPeerForComponentDrawable");
    cd.setAllowOwnPeer ("true".equals(allowOwnPeer));
    cd.setComponent((Component) o);
    return cd;
  }

  /**
   * A helper method that queries the configuration (and therefore also the system properties) for whether the
   * system is in headless mode.
   *
   * @return true, if the system is headless, false otherwise.
   */
  protected static boolean isHeadless()
  {
    final Configuration config = JFreeReportBoot.getInstance().getGlobalConfig();
    return "true".equals(config.getConfigProperty("java.awt.headless", "false"));
  }

  /**
   * Creates a clone of this filter.
   *
   * @return the clone.
   * @throws CloneNotSupportedException if an error occured.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final ComponentDrawableFilter il = (ComponentDrawableFilter) super.clone();
    if (source != null)
    {
      il.source = (DataSource) source.clone();
    }
    return il;
  }

  /**
   * Returns the assigned DataSource for this Target.
   *
   * @return The datasource.
   */
  public DataSource getDataSource()
  {
    return source;
  }

  /**
   * Assigns a DataSource for this Target.
   *
   * @param ds The data source.
   */
  public void setDataSource(final DataSource ds)
  {
    this.source = ds;
  }

}
