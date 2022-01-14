package org.jfree.report.modules.parser.ext.factory.objects;

import org.jfree.report.modules.parser.ext.factory.base.AbstractObjectDescription;
import org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException;
import org.jfree.report.style.VerticalTextAlign;

/**
 * An object-description for an {@link org.jfree.report.ElementAlignment} object.
 *
 * @author Thomas Morgner
 */
public class VerticalAlignmentObjectDescription extends AbstractObjectDescription
{
  /**
   * Creates a new object description.
   */
  public VerticalAlignmentObjectDescription ()
  {
    super(VerticalTextAlign.class);
    setParameterDefinition("value", String.class);
  }

  /**
   * Creates an {@link org.jfree.report.ElementAlignment} object based on this description.
   *
   * @return The object.
   */
  public Object createObject ()
  {
    final String o = (String) getParameter("value");
    if (o == null)
    {
      return null;
    }
    if ("use-script".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.USE_SCRIPT;
    }
    if ("text-bottom".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.TEXT_BOTTOM;
    }
    if ("bottom".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.BOTTOM;
    }
    if ("text-top".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.TEXT_TOP;
    }
    if ("top".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.TOP;
    }
    if ("central".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.CENTRAL;
    }
    if ("middle".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.MIDDLE;
    }

    if ("sub".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.SUB;
    }
    if ("super".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.SUPER;
    }
    if ("baseline".equalsIgnoreCase(o))
    {
      return VerticalTextAlign.BASELINE;
    }
    return null;
  }

  /**
   * Sets the parameters in the object description to match the specified object.
   *
   * @param o the object (an {@link org.jfree.report.ElementAlignment} instance).
   * @throws org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException if the object is not recognised.
   */
  public void setParameterFromObject (final Object o)
          throws ObjectFactoryException
  {
    if (o.equals(VerticalTextAlign.BOTTOM))
    {
      setParameter("value", "bottom");
    }
    else if (o.equals(VerticalTextAlign.TEXT_BOTTOM))
    {
      setParameter("value", "text-bottom");
    }
    else if (o.equals(VerticalTextAlign.MIDDLE))
    {
      setParameter("value", "middle");
    }
    else if (o.equals(VerticalTextAlign.TEXT_TOP))
    {
      setParameter("value", "text-top");
    }
    else if (o.equals(VerticalTextAlign.TOP))
    {
      setParameter("value", "top");
    }
    else if (o.equals(VerticalTextAlign.CENTRAL))
    {
      setParameter("value", "central");
    }
    else if (o.equals(VerticalTextAlign.SUB))
    {
      setParameter("value", "sub");
    }
    else if (o.equals(VerticalTextAlign.SUPER))
    {
      setParameter("value", "super");
    }
    else if (o.equals(VerticalTextAlign.BASELINE))
    {
      setParameter("value", "baseline");
    }
    else if (o.equals(VerticalTextAlign.USE_SCRIPT))
    {
      setParameter("value", "use-script");
    }
    else
    {
      throw new ObjectFactoryException("Invalid value specified for ElementAlignment");
    }
  }

}
