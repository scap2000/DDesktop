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
 * ExpressionCollection.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Collects all expressions used in the report. There exist 2 states of the ExpressionCollection. In the first,
 * modifiable state, expressions can be added to the collection. During the adding the expressions get initialized. An
 * ExpressionCollection in this state is not able to connect to an DataRow.
 * <p/>
 * The second state is an immutable state of this collection, no expressions can be added or removed.  This
 * ReadOnlyExpressionCollection can be created by calling getCopy() on the first-state expression collection. The
 * ReadOnlyExpressionCollection is able to connect to a DataRow.
 *
 * @author Thomas Morgner
 */
public class ExpressionCollection implements Cloneable, Serializable
{
  /**
   * Ordered storage for the Expressions.
   */
  private ArrayList expressionList;

  /**
   * Creates a new expression collection (initially empty).
   */
  public ExpressionCollection()
  {
    expressionList = new ArrayList();
  }

  /**
   * Creates a new expression collection, populated with the supplied expressions.
   *
   * @param expressions a collection of expressions.
   * @throws ClassCastException if the collection does not contain Expressions
   */
  public ExpressionCollection(final Collection expressions)
  {
    this();
    addAll(expressions);
  }

  /**
   * Adds all expressions contained in the given collection to this expression collection. The expressions get
   * initialized during the adding process.
   *
   * @param expressions the expressions to be added.
   * @throws ClassCastException if the collection does not contain expressions
   */
  public void addAll(final Collection expressions)
  {
    if (expressions != null)
    {
      final Iterator iterator = expressions.iterator();
      while (iterator.hasNext())
      {
        final Expression f = (Expression) iterator.next();
        add(f);
      }
    }
  }

  /**
   * Returns the {@link Expression} with the specified name (or <code>null</code>).
   *
   * @param name the expression name (<code>null</code> not permitted).
   * @return The expression.
   * @throws NullPointerException if the name given is <code>null</code>.
   */
  public Expression get(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }

    final int position = findExpressionByName(name);
    if (position == -1)
    {
      return null;
    }
    return getExpression(position);
  }

  /**
   * Searches the list of expressions for an expression with the given name.
   *
   * @param name the name, never null.
   * @return the position of the expression with that name or -1 if no expression contains that name.
   */
  private int findExpressionByName(final String name)
  {
    for (int i = 0; i < expressionList.size(); i++)
    {
      final Expression expression = (Expression) expressionList.get(i);
      if (name.equals(expression.getName()))
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Adds an expression to the collection.  The expression is initialized before it is added to this collection.
   *
   * @param e the expression.
   */
  public void add(final Expression e)
  {
    if (e == null)
    {
      throw new NullPointerException("Expression is null");
    }

    final int position = findExpressionByName(e.getName());
    if (position == -1)
    {
      privateAdd(e);
      return;
    }

    removeExpression(e);
    privateAdd(e);
  }

  /**
   * Adds an expression to the collection.
   *
   * @param e the expression.
   * @throws NullPointerException if the given Expression is null.
   */
  protected void privateAdd(final Expression e)
  {
    expressionList.add(e);
  }

  /**
   * Removes an expression from the collection.
   *
   * @param e the expression.
   * @throws NullPointerException if the given Expression is null.
   */
  public void removeExpression(final Expression e)
  {
    final int position = findExpressionByName(e.getName());
    if (position == -1)
    {
      return;
    }
    expressionList.remove(position);
  }

  /**
   * Returns the number of active expressions in this collection.
   *
   * @return the number of expressions in this collection
   */
  public int size()
  {
    return expressionList.size();
  }

  /**
   * Returns the expression on the given position in the list.
   *
   * @param pos the position in the list.
   * @return the expression.
   * @throws IndexOutOfBoundsException if the given position is invalid
   */
  public Expression getExpression(final int pos)
  {
    return (Expression) expressionList.get(pos);
  }

  /**
   * Clones this expression collection and all expressions contained in the collection.
   *
   * @return The clone.
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    final ExpressionCollection col = (ExpressionCollection) super.clone();
    col.expressionList = (ArrayList) expressionList.clone();
    col.expressionList.clear();

    try
    {
      final Iterator it = expressionList.iterator();
      while (it.hasNext())
      {
        final Expression ex = (Expression) it.next();
        col.privateAdd(ex.getInstance());
      }
    }
    catch(Exception e)
    {
      throw new CloneNotSupportedException("Unable to clone an expression");
    }
    return col;
  }

  /**
   * Returns the expressions contained in this collection as unmodifiable list.
   *
   * @return the expressions as list.
   * @deprecated This functionality will be removed. Use the array-version instead.
   */
  public List getExpressionList()
  {
    return Collections.unmodifiableList(expressionList);
  }

  /**
   * Return all expressions contained in this collection as array.
   *
   * @return the expressions as array.
   */
  public Expression[] getExpressions()
  {
    return (Expression[]) expressionList.toArray(new Expression[expressionList.size()]);
  }
}
