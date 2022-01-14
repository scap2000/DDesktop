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
 * BSHExpression.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.misc.beanshell;

import bsh.EvalError;
import bsh.Interpreter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;

import org.jfree.report.function.AbstractExpression;
import org.jfree.report.function.Expression;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * An expression that uses the BeanShell scripting framework to perform a scripted
 * calculation. The expression itself is contained in a function called
 * <p/>
 * <code>Object getValue()</code>
 * <p/>
 * and this function is defined in the <code>expression</code> property. You have to
 * overwrite the function <code>getValue()</code> to begin and to end your expression, but
 * you are free to add your own functions to the script.
 * <p/>
 * By default, base Java core and extension packages are imported for you. They are: <ul>
 * <li><code>java.lang<code> <li><code>java.io</code> <li><code>java.util</code>
 * <li><code>java.net</code> <li><code>java.awt</code> <li><code>java.awt.event</code>
 * <li><code>javax.swing</code> <li><code>javax.swing.event</code> </ul>
 * <p/>
 * An example in the XML format: (from report1.xml)
 * <p/>
 * <pre><expression name="expression" class="org.jfree.report.modules.misc.beanshell.BSHExpression">
 * <properties>
 * <property name="expression">
 * // you may import packages and classes or use the fully qualified name of the class
 * import org.jfree.report.*;
 * <p/>
 * String userdefinedFunction (String parameter, Date date)
 * {
 * return parameter + " - the current date is " + date);
 * }
 * <p/>
 * // use simple java code to perform the expression. You may use all classes
 * // available in your classpath as if you write "real" java code in your favourite
 * // IDE.
 * // See the www.beanshell.org site for more information ...
 * //
 * // A return value of type "Object" is alway implied ...
 * getValue ()
 * {
 * return userdefinedFunction ("Hello World :) ", new Date());
 * }
 * </property>
 * </properties>
 * </expression></pre>
 *
 * @author Thomas Morgner
 */
public class BSHExpression extends AbstractExpression
{
  /**
   * The headerfile with the default initialisations.
   */
  public static final String BSHHEADERFILE =
      "org/jfree/report/modules/misc/beanshell/BSHExpressionHeader.txt"; //$NON-NLS-1$

  /**
   * The beanshell-interpreter used to evaluate the expression.
   */
  private transient Interpreter interpreter;
  private transient boolean invalid;

  private String expression;

  /**
   * default constructor, create a new BeanShellExpression.
   */
  public BSHExpression()
  {
  }

  /**
   * This method tries to create a new and fully initialized BeanShell interpreter.
   *
   * @return the interpreter or null, if there was no way to create the interpreter.
   */
  protected Interpreter createInterpreter()
  {
    try
    {
      final Interpreter interpreter = new Interpreter();
      initializeInterpreter(interpreter);
      return interpreter;
    }
    catch (Exception e)
    {
      Log.error("Unable to initialize the expression", e); //$NON-NLS-1$
      return null;
    }
  }

  /**
   * Initializes the bean-shell interpreter by executing the code in
   * the BSHExpressionHeader.txt file.
   *
   * @param interpreter the interpreter that should be initialized.
   * @throws EvalError if an BeanShell-Error occured.
   * @throws IOException if the beanshell file could not be read.
   */
  protected void initializeInterpreter(final Interpreter interpreter)
      throws EvalError, IOException
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
        ("BSHExpressionHeader.txt", BSHExpression.class); //$NON-NLS-1$
    // read the header, creates a skeleton
    final Reader r = new InputStreamReader(new BufferedInputStream(in));
    try
    {
      interpreter.eval(r);
    }
    finally
    {
      r.close();
    }

    // now add the userdefined expression
    // the expression is given in form of a function with the signature of:
    //
    // Object getValue ()
    //
    if (getExpression() != null)
    {
      interpreter.eval(expression);
    }
  }

  /**
   * Evaluates the defined expression. If an exception or an evaluation error occures, the
   * evaluation returns null and the error is logged. The current datarow and a copy of
   * the expressions properties are set to script-internal variables. Changes to the
   * properties will not alter the expressions original properties and will be lost when
   * the evaluation is finished.
   * <p/>
   * Expressions do not maintain a state and no assumptions about the order of evaluation
   * can be made.
   *
   * @return the evaluated value or null.
   */
  public Object getValue()
  {
    if (invalid)
    {
      return null;
    }
    if (interpreter == null)
    {
      interpreter = createInterpreter();
      if (interpreter == null)
      {
        invalid = true;
        return null;
      }
    }
    try
    {
      interpreter.set("dataRow", getDataRow()); //$NON-NLS-1$
      return interpreter.eval("getValue ();"); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      Log.warn(new Log.SimpleMessage("Evaluation error: ", //$NON-NLS-1$
          e.getClass(), " - ", e.getMessage()), e); //$NON-NLS-1$
      return null;
    }
  }

  /**
   * Return a new instance of this expression. The copy is initialized and uses the same
   * parameters as the original, but does not share any objects.
   *
   * @return a copy of this function.
   */
  public Expression getInstance()
  {
    final BSHExpression ex = (BSHExpression) super.getInstance();
    ex.interpreter = null;
    return ex;
  }

  /**
   * Serialisation support. The transient child elements were not saved.
   *
   * @param in the input stream.
   * @throws IOException            if there is an I/O error.
   * @throws ClassNotFoundException if a serialized class is not defined on this system.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
  }

  /**
   * Sets the beanshell script as string.
   *
   * @return the script.
   */
  public String getExpression()
  {
    return expression;
  }

  /**
   * Sets the beanshell script that should be executed. The script should
   * define a getValue() method which returns a single object.
   *
   * @param expression the script.
   */
  public void setExpression(final String expression)
  {
    this.expression = expression;
    this.invalid = false;
    this.interpreter = null;
  }
}
