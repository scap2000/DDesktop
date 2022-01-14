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
 * ElementStyleSheet.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.style;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jfree.serializer.SerializerHelper;
import org.jfree.ui.FloatDimension;
import org.jfree.util.ObjectUtilities;

/**
 * An element style-sheet contains zero, one or many attributes that affect the appearance of report elements.  For each
 * attribute, there is a predefined key that can be used to access that attribute in the style sheet.
 * <p/>
 * Every report element has an associated style-sheet.
 * <p/>
 * A style-sheet maintains a list of parent style-sheets.  If an attribute is not defined in a style-sheet, the code
 * refers to the parent style-sheets to see if the attribute is defined there.
 * <p/>
 * All StyleSheet entries are checked against the StyleKeyDefinition for validity.
 * <p/>
 * As usual, this implementation is not synchronized, we need the performance during the reporting.
 *
 * @author Thomas Morgner
 */
public abstract class ElementStyleSheet extends AbstractStyleSheet
    implements Serializable, StyleChangeListener, Cloneable
{
  /**
   * A key for the 'minimum size' of an element. This style property is not inherited from the parent band.
   *
   * @deprecated use the minimum-width and minimum-height style-keys instead.
   */
  public static final StyleKey MINIMUMSIZE = ElementStyleKeys.MINIMUMSIZE;
  /**
   * A key for the 'maximum size' of an element. This style property is not inherited from the parent band.
   *
   * @deprecated use the minimum-width and minimum-height style-keys instead.
   */
  public static final StyleKey MAXIMUMSIZE = ElementStyleKeys.MAXIMUMSIZE;

  /**
   * A key for the 'preferred size' of an element. This style property is not inherited from the parent band.
   *
   * @deprecated use the minimum-width and minimum-height style-keys instead.
   */
  public static final StyleKey PREFERREDSIZE = ElementStyleKeys.PREFERREDSIZE;
  /**
   * A key for the 'bounds' of an element. This style property is not inherited from the parent band. This style
   * property is an internal state property and is therefore not written to the XML.
   *
   * @deprecated no longer used.
   */
  public static final StyleKey BOUNDS = ElementStyleKeys.BOUNDS;

  /**
   * A key for an element's 'visible' flag.
   */
  public static final StyleKey VISIBLE = ElementStyleKeys.VISIBLE;

  /**
   * A key for the 'paint' used to color an element. For historical reasons, this key requires a color value.
   */
  public static final StyleKey PAINT = ElementStyleKeys.PAINT;

  /**
   * A key for the 'ext-paint' used to fill or draw an element. If the specified paint is not supported by the output
   * target, the color given with the 'paint' key is used instead.
   */
  public static final StyleKey EXTPAINT = ElementStyleKeys.EXTPAINT;

  /**
   * A key for the 'stroke' used to draw an element. (This now only applies to shape and drawable-elements.)
   */
  public static final StyleKey STROKE = ElementStyleKeys.STROKE;

  /**
   * A key for the horizontal alignment of an element.
   */
  public static final StyleKey ALIGNMENT = ElementStyleKeys.ALIGNMENT;

  /**
   * A key for the vertical alignment of an element.
   */
  public static final StyleKey VALIGNMENT = ElementStyleKeys.VALIGNMENT;

  /**
   * A key for an element's 'scale' flag.
   */
  public static final StyleKey SCALE = ElementStyleKeys.SCALE;

  /**
   * A key for an element's 'keep aspect ratio' flag.
   */
  public static final StyleKey KEEP_ASPECT_RATIO = ElementStyleKeys.KEEP_ASPECT_RATIO;

  /**
   * A key for the dynamic height flag for an element.
   */
  public static final StyleKey DYNAMIC_HEIGHT = ElementStyleKeys.DYNAMIC_HEIGHT;

  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element is not cachable. This key
   * defaults to true.
   *
   * @deprecated This property is no longer used.
   */
  public static final StyleKey ELEMENT_LAYOUT_CACHEABLE = ElementStyleKeys.ELEMENT_LAYOUT_CACHEABLE;

  public static final StyleKey HREF_TARGET = ElementStyleKeys.HREF_TARGET;

  /**
   * Specifies the anchor tag's target window for opening the link.
   */
  public static final StyleKey HREF_WINDOW = ElementStyleKeys.HREF_WINDOW;

  /**
   * An internal flag style indicating whether the current HRef is inherited from a child.
   * <p/>
   * This style property is an internal state property and is therefore not written to the XML.
   */
  public static final StyleKey HREF_INHERITED = ElementStyleKeys.HREF_INHERITED;

  /**
   * The StyleKey for the user defined cell data format.
   */
  public static final StyleKey EXCEL_WRAP_TEXT = ElementStyleKeys.EXCEL_WRAP_TEXT;

  /**
   * The StyleKey for the user defined cell data format.
   */
  public static final StyleKey EXCEL_DATA_FORMAT_STRING = ElementStyleKeys.EXCEL_DATA_FORMAT_STRING;

  /**
   * A key for the 'font family' used to draw element text.
   */
  public static final StyleKey FONT = TextStyleKeys.FONT;

  /**
   * A key for the 'font size' used to draw element text.
   */
  public static final StyleKey FONTSIZE = TextStyleKeys.FONTSIZE;

  /**
   * A key for the 'font size' used to draw element text.
   */
  public static final StyleKey LINEHEIGHT = TextStyleKeys.LINEHEIGHT;

  /**
   * A key for an element's 'bold' flag.
   */
  public static final StyleKey BOLD = TextStyleKeys.BOLD;

  /**
   * A key for an element's 'italic' flag.
   */
  public static final StyleKey ITALIC = TextStyleKeys.ITALIC;

  /**
   * A key for an element's 'underlined' flag.
   */
  public static final StyleKey UNDERLINED = TextStyleKeys.UNDERLINED;

  /**
   * A key for an element's 'strikethrough' flag.
   */
  public static final StyleKey STRIKETHROUGH = TextStyleKeys.STRIKETHROUGH;

  /**
   * A key for an element's 'embedd' flag.
   */
  public static final StyleKey EMBEDDED_FONT = TextStyleKeys.EMBEDDED_FONT;

  /**
   * A key for an element's 'embedd' flag.
   */
  public static final StyleKey FONTENCODING = TextStyleKeys.FONTENCODING;

  /**
   * The string that is used to end a text if not all text fits into the element.
   */
  public static final StyleKey RESERVED_LITERAL = TextStyleKeys.RESERVED_LITERAL;

  /**
   * The Layout Cacheable stylekey. Set this stylekey to false, to define that the element is not cachable. This key
   * defaults to true.
   */
  public static final StyleKey TRIM_TEXT_CONTENT = TextStyleKeys.TRIM_TEXT_CONTENT;

  /**
   * A singleton marker for the cache.
   */
  private static final Object UNDEFINED_VALUE = new Object();

  /**
   * The style-sheet name.
   */
  private String name;

  /**
   * Storage for the parent style sheets (if any).
   */
  private ArrayList parents;

  /**
   * Storage for readonly style sheets.
   */
  private ElementDefaultStyleSheet globalDefaultStyleSheet;
  private ElementStyleSheet cascadeStyleSheet;

  /**
   * Parent style sheet cache.
   */
  private transient StyleSheetCarrier[] parentsCached;

  private transient StyleKey[] propertyKeys;
  private transient Object[] properties;
  private transient Object[] cachedProperties;
  private transient Object[] cachedData;

  /**
   * Style change support.
   */
  private transient StyleChangeSupport styleChangeSupport;

  /**
   * A flag that controls whether or not caching is allowed.
   */
  private boolean allowCaching;
  /**
   * The cached font definition instance from this stylessheet.
   */
  private transient FontDefinition fontDefinition;

  private long changeTracker;
  private static final StyleKey[] EMPTY_KEYS = new StyleKey[0];

  /**
   * Creates a new element style-sheet with the given name.  The style-sheet initially contains no attributes, and has
   * no parent style-sheets.
   *
   * @param name the name (<code>null</code> not permitted).
   */
  protected ElementStyleSheet(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("ElementStyleSheet constructor: name is null.");
    }
    this.name = name;
    this.parents = new ArrayList(5);
    this.styleChangeSupport = new StyleChangeSupport(this);
  }

  /**
   * Creates a new element style-sheet with the given name.  The style-sheet initially contains no attributes, and has
   * no parent style-sheets.
   *
   * @param name the name (<code>null</code> not permitted).
   */
  protected ElementStyleSheet(final String name,
                              final ElementStyleSheet deriveParent)
  {
    if (name == null)
    {
      throw new NullPointerException("ElementStyleSheet constructor: name is null.");
    }
    if (deriveParent == null)
    {
      throw new NullPointerException("ElementStyleSheet constructor: parent cannot be null.");
    }
    this.name = name;
    this.parents = new ArrayList(5);
    this.styleChangeSupport = new StyleChangeSupport(this);
    // These arrays are immutable ..
    this.propertyKeys = deriveParent.propertyKeys;
    this.properties = new Object[propertyKeys.length];
    // Cached properties are not always needed ..
    this.changeTracker = deriveParent.changeTracker;
  }

  /**
   * Returns <code>true</code> if caching is allowed, and <code>false</code> otherwise.
   *
   * @return A boolean.
   */
  public final boolean isAllowCaching()
  {
    return allowCaching;
  }

  public long getChangeTracker()
  {
    return changeTracker;
  }

  /**
   * Returns true, if the given key is locally defined, false otherwise.
   *
   * @param key the key to test
   * @return true, if the key is local, false otherwise.
   */
  public boolean isLocalKey(final StyleKey key)
  {
    if (properties == null)
    {
      return false;
    }
    final int identifier = key.getIdentifier();
    if (properties.length <= identifier)
    {
      return false;
    }
    return properties[identifier] != null;
  }

  /**
   * Sets the flag that controls whether or not caching is allowed.
   *
   * @param allowCaching the flag value.
   */
  public void setAllowCaching(final boolean allowCaching)
  {
    this.allowCaching = allowCaching;
    if (this.allowCaching == false)
    {
      this.cachedProperties = null;
    }
  }

  /**
   * Returns the name of the style-sheet.
   *
   * @return the name (never <code>null</code>).
   */
  public String getName()
  {
    return name;
  }

  /**
   * Adds a parent style-sheet. This method adds the parent to the beginning of the list, and guarantees, that this
   * parent is queried first.
   *
   * @param parent the parent (<code>null</code> not permitted).
   */
  public void addParent(final ElementStyleSheet parent)
  {
    addParent(0, parent);
  }

  /**
   * Adds a parent style-sheet. Parents on a lower position are queried before any parent with an higher position in the
   * list.
   *
   * @param position the position where to insert the parent style sheet
   * @param parent   the parent (<code>null</code> not permitted).
   * @throws IndexOutOfBoundsException if the position is invalid (pos &lt; 0 or pos &gt;= numberOfParents)
   */
  public void addParent(final int position, final ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.addParent(...): parent is null.");
    }
    if (parent.isSubStyleSheet(this) == false)
    {
      final StyleSheetCarrier carrier = createCarrier(parent);
      if (carrier == null)
      {
        throw new IllegalArgumentException
            ("The given StyleSheet cannot be added to this stylesheet.");
      }
      parents.add(position, carrier);
      parentsCached = null;
    }
    else
    {
      throw new IllegalArgumentException("Cannot add parent as child.");
    }
  }

  protected abstract StyleSheetCarrier createCarrier(ElementStyleSheet styleSheet);


  /**
   * Checks, whether the given element stylesheet is already added as child into the stylesheet tree.
   *
   * @param parent the element that should be tested.
   * @return true, if the element is a child of this element style sheet, false otherwise.
   */
  protected boolean isSubStyleSheet(final ElementStyleSheet parent)
  {
    for (int i = 0; i < parents.size(); i++)
    {
      final StyleSheetCarrier ca = (StyleSheetCarrier) parents.get(i);
      final ElementStyleSheet es = ca.getStyleSheet();
      if (es == parent)
      {
        return true;
      }
      if (es.isSubStyleSheet(parent) == true)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Removes a parent style-sheet.
   *
   * @param parent the style-sheet to remove (<code>null</code> not permitted).
   */
  public void removeParent(final ElementStyleSheet parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("ElementStyleSheet.removeParent(...): parent is null.");
    }
    final Iterator it = parents.iterator();
    while (it.hasNext())
    {
      final StyleSheetCarrier carrier = (StyleSheetCarrier) it.next();
      if (carrier.isSame(parent))
      {
        it.remove();
        carrier.invalidate();
      }
    }
    parentsCached = null;
  }

  /**
   * Returns a list of the parent style-sheets.
   * <p/>
   * The list is unmodifiable.
   *
   * @return the list.
   */
  public ElementStyleSheet[] getParents()
  {
    if (parentsCached == null)
    {
      this.parentsToCache();
    }
    final ElementStyleSheet[] styleSheets =
        new ElementStyleSheet[parentsCached.length];
    for (int i = 0; i < styleSheets.length; i++)
    {
      styleSheets[i] = parentsCached[i].getStyleSheet();
    }
    return styleSheets;
  }

  /**
   * Returns the global default (if defined).
   *
   * @return the list.
   */
  public ElementDefaultStyleSheet getGlobalDefaultStyleSheet()
  {
    return globalDefaultStyleSheet;
  }

  public void setGlobalDefaultStyleSheet(final ElementDefaultStyleSheet defaultStyleSheet)
  {
    this.globalDefaultStyleSheet = defaultStyleSheet;
  }

  public ElementStyleSheet getCascadeStyleSheet()
  {
    return cascadeStyleSheet;
  }

  public void setCascadeStyleSheet(final ElementStyleSheet cascadeStyleSheet)
  {
    if (this.cascadeStyleSheet != null)
    {
      this.cascadeStyleSheet.removeListener(this);
    }
    this.cascadeStyleSheet = cascadeStyleSheet;
    if (this.cascadeStyleSheet != null)
    {
      this.cascadeStyleSheet.addListener(this);
    }
  }

  public final Object[] toArray(final StyleKey[] keys)
  {
    if (cachedData != null)
    {
      return (Object[]) cachedData.clone();
    }

    final Object[] data = new Object[keys.length];

    // Step 1: Copy all the properties which are set directly.
    if (properties != null)
    {
      final int maxIdx = Math.min(keys.length, properties.length);
      System.arraycopy(properties, 0, data, 0, maxIdx);
    }

    // Step 2: Copy all cached properties.
    if (cachedProperties != null)
    {
      for (int i = 0; i < keys.length; i++)
      {
        final StyleKey key = keys[i];
        final int identifier = key.getIdentifier();

        if (identifier >= cachedProperties.length)
        {
          continue;
        }
        if (data[identifier] != null)
        {
          continue;
        }

        final Object value = cachedProperties[identifier];
        if (value != null)
        {
          if (value == UNDEFINED_VALUE)
          {
            data[identifier] = null;
          }
          else
          {
            data[identifier] = value;
          }
        }
      }
    }

    // Step 3: Copy all remaining properties
    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      if (key == null)
      {
        continue;
      }

      final int identifier = key.getIdentifier();

      if (data[identifier] != null)
      {
        continue;
      }
      data[identifier] = getStyleProperty(key, null);
    }

    if (allowCaching)
    {
      cachedData = data;
      return (Object[]) data.clone();
    }
    return data;
  }

  /**
   * Returns the value of a style.  If the style is not found in this style-sheet, the code looks in the parent
   * style-sheets.  If the style is not found in any of the parent style-sheets, then the default value (possibly
   * <code>null</code>) is returned.
   *
   * @param key          the style key.
   * @param defaultValue the default value (<code>null</code> permitted).
   * @return the value.
   */
  public Object getStyleProperty(final StyleKey key, final Object defaultValue)
  {
    final Object legacyVal = handleGetLegacyKeys(key);
    if (legacyVal == UNDEFINED_VALUE)
    {
      return defaultValue;
    }
    else if (legacyVal != null)
    {
      return legacyVal;
    }

    final int identifier = key.getIdentifier();
    if (properties != null)
    {
      if (properties.length > identifier)
      {
        final Object value = properties[identifier];
        if (value != null)
        {
          return value;
        }
      }
    }

    if (cachedProperties != null)
    {
      if (cachedProperties.length > identifier)
      {
        final Object value = cachedProperties[identifier];
        if (value != null)
        {
          if (value == UNDEFINED_VALUE)
          {
            return defaultValue;
          }
          return value;
        }
      }
    }

    // parents must always be queried ...
    parentsToCache();
    for (int i = 0; i < parentsCached.length; i++)
    {
      final ElementStyleSheet st = parentsCached[i].getStyleSheet();
      final Object value = st.getStyleProperty(key, null);
      if (value == null)
      {
        continue;
      }
      putInCache(key, value);
      return value;
    }

    if (key.isInheritable())
    {
      if (cascadeStyleSheet != null)
      {
        final Object value = cascadeStyleSheet.getStyleProperty(key, null);
        if (value != null)
        {
          putInCache(key, value);
          return value;
        }
      }
    }

    if (globalDefaultStyleSheet != null)
    {
      final Object value = globalDefaultStyleSheet.getStyleProperty(key, null);
      if (value != null)
      {
        putInCache(key, value);
        return value;
      }
    }

    putInCache(key, UNDEFINED_VALUE);
    return defaultValue;
  }

  /**
   * Puts an object into the cache (if caching is enabled).
   *
   * @param key   the stylekey for that object
   * @param value the object.
   */
  private void putInCache(final StyleKey key, final Object value)
  {
    if (allowCaching)
    {
      final int identifier = key.getIdentifier();
      if (cachedProperties != null)
      {
        if (cachedProperties.length <= identifier)
        {
          final Object[] newCache = new Object[StyleKey.getDefinedStyleKeyCount()];
          System.arraycopy(cachedProperties, 0, newCache, 0, cachedProperties.length);
          cachedProperties = newCache;
        }
      }
      else
      {
        cachedProperties = new Object[StyleKey.getDefinedStyleKeyCount()];
      }
      cachedProperties[identifier] = value;
      cachedData = null;
    }
  }

  /**
   * Sets a boolean style property.
   *
   * @param key   the style key (<code>null</code> not permitted).
   * @param value the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException   if the value cannot be assigned with the given key.
   */
  public void setBooleanStyleProperty(final StyleKey key, final boolean value)
  {
    if (value)
    {
      setStyleProperty(key, Boolean.TRUE);
    }
    else
    {
      setStyleProperty(key, Boolean.FALSE);
    }
  }

  /**
   * Sets a style property (or removes the style if the value is <code>null</code>).
   *
   * @param key   the style key (<code>null</code> not permitted).
   * @param value the value.
   * @throws NullPointerException if the given key is null.
   * @throws ClassCastException   if the value cannot be assigned with the given key.
   */
  public void setStyleProperty(final StyleKey key, final Object value)
  {
    if (key == null)
    {
      throw new NullPointerException("ElementStyleSheet.setStyleProperty: key is null.");
    }
    if (isFontDefinitionProperty(key))
    {
      fontDefinition = null;
    }
    if (handleSetLegacyKeys(key, value))
    {
      return;
    }

    final int identifier = key.getIdentifier();
    if (value == null)
    {
      if (properties != null)
      {
        if (properties.length > identifier)
        {
          if (properties[identifier] == null)
          {
            return;
          }

          // invalidate the cache ..
          if (allowCaching && cachedData != null)
          {
            putInCache(key, null);
          }

          changeTracker += 1;
          properties[identifier] = null;
        }
      }

      if (propertyKeys != null)
      {
        if (propertyKeys.length > identifier)
        {
          propertyKeys[identifier] = null;
        }
      }
      styleChangeSupport.fireStyleRemoved(key);
      return;
    }

    if (key.getValueType().isAssignableFrom(value.getClass()) == false)
    {
      throw new ClassCastException("Value for key " + key.getName()
          + " is not assignable: " + value.getClass()
          + " is not assignable from " + key.getValueType());
    }
    if (properties != null)
    {
      if (properties.length <= identifier)
      {
        final Object[] newProps = new Object[StyleKey.getDefinedStyleKeyCount()];
        System.arraycopy(properties, 0, newProps, 0, properties.length);
        properties = newProps;
      }
    }
    else
    {
      properties = new Object[StyleKey.getDefinedStyleKeyCount()];
    }

    if (propertyKeys != null)
    {
      if (propertyKeys.length <= identifier)
      {
        final StyleKey[] newProps = new StyleKey[StyleKey.getDefinedStyleKeyCount()];
        System.arraycopy(propertyKeys, 0, newProps, 0, propertyKeys.length);
        propertyKeys = newProps;
      }
    }
    else
    {
      propertyKeys = new StyleKey[StyleKey.getDefinedStyleKeyCount()];
    }
    if (ObjectUtilities.equal(properties[identifier], value))
    {
      // no need th change anything ..
      return;
    }

    // invalidate the cache ..
    if (allowCaching && cachedData != null)
    {
      putInCache(key, value);
    }

    changeTracker += 1;
    properties[identifier] = value;
    propertyKeys[identifier] = key;

    styleChangeSupport.fireStyleChanged(key, value);
  }

  private boolean handleSetLegacyKeys(final StyleKey key, final Object value)
  {
    if (value == null)
    {
      return false;
    }
    if (key == ElementStyleKeys.ABSOLUTE_POS)
    {
      final Point2D point = (Point2D) value;
      setStyleProperty(ElementStyleKeys.POS_X, new Float(point.getX()));
      setStyleProperty(ElementStyleKeys.POS_Y, new Float(point.getY()));
      return true;
    }
    if (key == ElementStyleKeys.MINIMUMSIZE)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.MIN_WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.MIN_HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    if (key == ElementStyleKeys.MAXIMUMSIZE)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.MAX_WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.MAX_HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    if (key == ElementStyleKeys.PREFERREDSIZE)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    if (key == ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    if (key == ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    if (key == ElementStyleKeys.BORDER_TOP_LEFT_RADIUS)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    if (key == ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS)
    {
      final Dimension2D dim = (Dimension2D) value;
      setStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_WIDTH, new Float(dim.getWidth()));
      setStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_HEIGHT, new Float(dim.getHeight()));
      return true;
    }
    return false;
  }

  private Object handleGetLegacyKeys(final StyleKey key)
  {
    if (key == ElementStyleKeys.ABSOLUTE_POS)
    {
      final Float x = (Float) getStyleProperty(ElementStyleKeys.POS_X);
      final Float y = (Float) getStyleProperty(ElementStyleKeys.POS_Y);
      if (x == null || y == null)
      {
        return UNDEFINED_VALUE;
      }
      return new Point2D.Double(x.doubleValue(), y.doubleValue());
    }
    if (key == ElementStyleKeys.MINIMUMSIZE)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.MIN_WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.MIN_HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    if (key == ElementStyleKeys.MAXIMUMSIZE)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.MAX_WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.MAX_HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    if (key == ElementStyleKeys.PREFERREDSIZE)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    if (key == ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.BORDER_BOTTOM_LEFT_RADIUS_HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    if (key == ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS_HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    if (key == ElementStyleKeys.BORDER_TOP_LEFT_RADIUS)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.BORDER_TOP_LEFT_RADIUS_HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    if (key == ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS)
    {
      final Float w = (Float) getStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_WIDTH);
      final Float h = (Float) getStyleProperty(ElementStyleKeys.BORDER_TOP_RIGHT_RADIUS_HEIGHT);
      if (w == null || h == null)
      {
        return UNDEFINED_VALUE;
      }
      return new FloatDimension(w.floatValue(), h.floatValue());
    }
    return null;
  }

  /**
   * Creates and returns a copy of this object. After the cloning, the new StyleSheet is no longer registered with its
   * parents.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    try
    {
      final ElementStyleSheet sc = (ElementStyleSheet) super.clone();
      if (properties != null)
      {
        sc.properties = (Object[]) properties.clone();
      }
      if (propertyKeys != null)
      {
        sc.propertyKeys = (StyleKey[]) propertyKeys.clone();
      }
      //noinspection CloneCallsConstructors
      sc.styleChangeSupport = new StyleChangeSupport(sc);
      if (cachedProperties != null)
      {
        sc.cachedProperties = (Object[]) cachedProperties.clone();
      }
      parentsToCache();
      sc.parents.clone();
      sc.parents.clear();
      sc.parentsCached = new StyleSheetCarrier[parentsCached.length];
      for (int i = 0; i < parentsCached.length; i++)
      {
        final StyleSheetCarrier carrier = (StyleSheetCarrier) parentsCached[i].clone();
        sc.parentsCached[i] = carrier;
        sc.parents.add(carrier);
      }
      sc.cascadeStyleSheet = null;
      sc.globalDefaultStyleSheet = globalDefaultStyleSheet;
      return sc;
    }
    catch (CloneNotSupportedException cne)
    {
      throw new IllegalStateException("Clone failed.");
    }
  }

  protected StyleSheetCarrier[] getParentReferences()
  {
    parentsToCache();
    return parentsCached;
  }

  /**
   * Clones the style-sheet. The assigned parent style sheets are not cloned. The stylesheets are not assigned to the
   * contained stylesheet collection, you have to reassign them manually ...
   *
   * @return the clone.
   */
  public ElementStyleSheet getCopy()
      throws CloneNotSupportedException
  {
    return (ElementStyleSheet) clone();
  }

  /**
   * Creates the cached object array for the parent element style sheets.
   */
  private void parentsToCache()
  {
    if (parentsCached == null)
    {
      parentsCached = (StyleSheetCarrier[])
          parents.toArray(new StyleSheetCarrier[parents.size()]);
    }
  }

  /**
   * Checks, whether the given key is one of the keys used to define the font definition from this stylesheet.
   *
   * @param key the key that should be checked.
   * @return true, if the key is a font definition key, false otherwise.
   */
  private boolean isFontDefinitionProperty(final StyleKey key)
  {
    if (key == TextStyleKeys.FONT)
    {
      return true;
    }
    if (key == TextStyleKeys.FONTSIZE)
    {
      return true;
    }
    if (key == TextStyleKeys.BOLD)
    {
      return true;
    }
    if (key == TextStyleKeys.ITALIC)
    {
      return true;
    }
    if (key == TextStyleKeys.UNDERLINED)
    {
      return true;
    }
    if (key == TextStyleKeys.STRIKETHROUGH)
    {
      return true;
    }
    if (key == TextStyleKeys.EMBEDDED_FONT)
    {
      return true;
    }
    if (key == TextStyleKeys.FONTENCODING)
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the font for this style-sheet.
   *
   * @return the font.
   */
  public FontDefinition getFontDefinitionProperty()
  {
    if (fontDefinition == null)
    {
      final String name = (String) getStyleProperty(TextStyleKeys.FONT);
      final int size = getIntStyleProperty(TextStyleKeys.FONTSIZE, -1);
      final boolean bold = getBooleanStyleProperty(TextStyleKeys.BOLD);
      final boolean italic = getBooleanStyleProperty(TextStyleKeys.ITALIC);
      final boolean underlined = getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
      final boolean strike = getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
      final boolean embed = getBooleanStyleProperty(TextStyleKeys.EMBEDDED_FONT);
      final String encoding = (String) getStyleProperty(TextStyleKeys.FONTENCODING);

      final FontDefinition retval = new FontDefinition(name, size, bold, italic, underlined, strike,
          encoding, embed);
      if (allowCaching)
      {
        fontDefinition = retval;
      }
      else
      {
        return retval;
      }
    }
    return fontDefinition;
  }

  /**
   * Sets the font for this style-sheet.
   *
   * @param font the font (<code>null</code> not permitted).
   */
  public void setFontDefinitionProperty(final FontDefinition font)
  {
    if (font == null)
    {
      throw new NullPointerException("ElementStyleSheet.setFontStyleProperty: font is null.");
    }
    setStyleProperty(TextStyleKeys.FONT, font.getFontName());
    setStyleProperty(TextStyleKeys.FONTSIZE, new Integer(font.getFontSize()));
    setBooleanStyleProperty(TextStyleKeys.BOLD, font.isBold());
    setBooleanStyleProperty(TextStyleKeys.ITALIC, font.isItalic());
    setBooleanStyleProperty(TextStyleKeys.UNDERLINED, font.isUnderline());
    setBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH, font.isStrikeThrough());
    setBooleanStyleProperty(TextStyleKeys.EMBEDDED_FONT, font.isEmbeddedFont());
    setStyleProperty(TextStyleKeys.FONTENCODING, font.getFontEncoding(null));
  }

  /**
   * Returns an enumeration of all local property keys.
   *
   * @return an enumeration of all localy defined style property keys.
   */
  public Iterator getDefinedPropertyNames()
  {
    final ArrayList al = new ArrayList();
    if (propertyKeys != null)
    {
      for (int i = 0; i < propertyKeys.length; i++)
      {
        if (propertyKeys[i] != null)
        {
          al.add(propertyKeys[i]);
        }
      }
    }
    return Collections.unmodifiableList(al).iterator();
  }

  public StyleKey[] getDefinedPropertyNamesArray()
  {
    if (propertyKeys == null)
    {
      return EMPTY_KEYS;
    }
    return (StyleKey[]) propertyKeys.clone();
  }

  /**
   * Adds a {@link StyleChangeListener}.
   *
   * @param l the listener.
   */
  protected void addListener(final StyleChangeListener l)
  {
    styleChangeSupport.addListener(l);
  }

  /**
   * Removes a {@link StyleChangeListener}.
   *
   * @param l the listener.
   */
  protected void removeListener(final StyleChangeListener l)
  {
    styleChangeSupport.removeListener(l);
  }

  /**
   * Forwards a change event notification to all registered {@link StyleChangeListener} objects.
   *
   * @param source the source of the change.
   * @param key    the style key.
   * @param value  the new value.
   */
  public void styleChanged(final ElementStyleSheet source, final StyleKey key,
                           final Object value)
  {
    if (source == this)
    {
      return;
    }

    cachedData = null;
    changeTracker += 1;

    if (cachedProperties != null)
    {
      final int identifier = key.getIdentifier();
      if (cachedProperties.length > identifier)
      {
        cachedProperties[identifier] = value;
      }
      if (isFontDefinitionProperty(key))
      {
        fontDefinition = null;
      }
    }

    styleChangeSupport.fireStyleChanged(key, value);
  }

  /**
   * Forwards a change event notification to all registered {@link StyleChangeListener} objects.
   *
   * @param source the source of the change.
   * @param key    the style key.
   */
  public void styleRemoved(final ElementStyleSheet source, final StyleKey key)
  {
    if (source == this)
    {
      return;
    }
    changeTracker += 1;

    if (cachedProperties != null)
    {
      final int identifier = key.getIdentifier();
      if (cachedProperties.length > identifier)
      {
        cachedProperties[identifier] = null;
      }
      if (isFontDefinitionProperty(key))
      {
        fontDefinition = null;
      }
    }
    cachedData = null;
    styleChangeSupport.fireStyleRemoved(key);
  }

  /**
   * Helper method for serialization.
   *
   * @param out the output stream where to write the object.
   * @throws IOException if errors occur while writing the stream.
   */
  private void writeObject(final ObjectOutputStream out)
      throws IOException
  {
    out.defaultWriteObject();

    out.writeObject(propertyKeys);
    if (properties == null)
    {
      out.writeInt(0);
    }
    else
    {
      final int size = properties.length;
      out.writeInt(size);
      for (int i = 0; i < size; i++)
      {
        final Object value = properties[i];
        SerializerHelper.getInstance().writeObject(value, out);
      }
    }
  }

  /**
   * Helper method for serialization.
   *
   * @param in the input stream from where to read the serialized object.
   * @throws IOException            when reading the stream fails.
   * @throws ClassNotFoundException if a class definition for a serialized object could not be found.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    styleChangeSupport = new StyleChangeSupport(this);

    in.defaultReadObject();
    final StyleKey[] keys = (StyleKey[]) in.readObject();
    final int size = in.readInt();
    final Object[] values = new Object[size];
    final SerializerHelper serHelper = SerializerHelper.getInstance();
    for (int i = 0; i < size; i++)
    {
      values[i] = serHelper.readObject(in);
    }
    if (keys == null)
    {
      return;
    }
    final int keyCount = StyleKey.getDefinedStyleKeyCount();
    properties = new Object[keyCount];
    propertyKeys = new StyleKey[keyCount];
    final int maxLen = Math.min
        (Math.min(properties.length, keys.length),
            Math.min(propertyKeys.length, values.length));
    for (int i = 0; i < maxLen; i++)
    {
      final StyleKey key = keys[i];
      if (key != null)
      {
        final int identifier = key.getIdentifier();
        properties[identifier] = values[i];
        propertyKeys[identifier] = key;
      }
    }
  }

  /**
   * Returns true, if this stylesheet is one of the global default stylesheets. Global default stylesheets are
   * unmodifiable and shared among all element stylesheets.
   *
   * @return true, if this is one of the unmodifiable global default stylesheets, false otherwise.
   */
  public boolean isGlobalDefault()
  {
    return false;
  }
}
