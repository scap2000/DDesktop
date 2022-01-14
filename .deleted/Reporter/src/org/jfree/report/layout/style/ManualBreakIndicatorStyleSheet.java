package org.jfree.report.layout.style;

import java.awt.geom.Point2D;

import org.jfree.report.style.AbstractStyleSheet;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.ui.FloatDimension;

/**
 * A replaced content element that is contained in a 'canvas' box (which is the default for all non-inline replaced
 * content elements) must have a minimum width and height of 100% so that it fills the whole box.
 *
 * @author Thomas Morgner
 */
public class ManualBreakIndicatorStyleSheet extends AbstractStyleSheet
{
  private static final Float WIDTH = new Float(-100);
  private static final Float ZERO = new Float(0);
  private StyleSheet parent;

  public ManualBreakIndicatorStyleSheet(final StyleSheet parent)
  {
    this.parent = parent;
  }

  public StyleSheet getParent()
  {
    return parent;
  }

  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    if (ElementStyleKeys.MINIMUMSIZE.equals(key))
    {
      return new FloatDimension(-100, 0);
    }
    if (ElementStyleKeys.MIN_WIDTH.equals(key))
    {
      return ManualBreakIndicatorStyleSheet.WIDTH;
    }
    if (ElementStyleKeys.MIN_HEIGHT.equals(key))
    {
      return ManualBreakIndicatorStyleSheet.ZERO;
    }
    if (ElementStyleKeys.POS_X.equals(key))
    {
      return ManualBreakIndicatorStyleSheet.ZERO;
    }
    if (ElementStyleKeys.POS_Y.equals(key))
    {
      return ManualBreakIndicatorStyleSheet.ZERO;
    }
    if (ElementStyleKeys.ABSOLUTE_POS.equals(key))
    {
      return new Point2D.Float (0,0);
    }
    if (BandStyleKeys.PAGEBREAK_BEFORE.equals(key))
    {
      return Boolean.TRUE;
    }
    return parent.getStyleProperty(key, defaultValue);
  }

  public Object[] toArray(final StyleKey[] keys)
  {
    final Object[] objects = (Object[]) parent.toArray(keys).clone();
    objects[ElementStyleKeys.MIN_WIDTH.getIdentifier()] = ManualBreakIndicatorStyleSheet.WIDTH;
    objects[ElementStyleKeys.MIN_HEIGHT.getIdentifier()] = ManualBreakIndicatorStyleSheet.WIDTH;
    objects[ElementStyleKeys.MINIMUMSIZE.getIdentifier()] = null;
    objects[ElementStyleKeys.POS_X.getIdentifier()] = ManualBreakIndicatorStyleSheet.ZERO;
    objects[ElementStyleKeys.POS_Y.getIdentifier()] = ManualBreakIndicatorStyleSheet.ZERO;
    objects[ElementStyleKeys.ABSOLUTE_POS.getIdentifier()] = null;
    objects[BandStyleKeys.PAGEBREAK_BEFORE.getIdentifier()] = Boolean.TRUE;
    return objects;
  }

  public InstanceID getId()
  {
    return parent.getId();
  }

  public long getChangeTracker()
  {
    return parent.getChangeTracker();
  }
}
