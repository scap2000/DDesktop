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
 * DemoTextInputPanel.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.layouts;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The DemoTextInputPanel provides two input fields for the report properties
 * used in the StackedLayoutDemos.
 *
 * @author Thomas Morgner
 */
public class DemoTextInputPanel extends JPanel
{
  private JTextArea messageOneField;
  private JTextArea messageTwoField;

  public DemoTextInputPanel()
  {
    setLayout(new GridBagLayout());

    final JLabel messageOneLabel = new JLabel ("One:");
    final JLabel messageTwoLabel = new JLabel ("Two:");
    messageOneField = new JTextArea();
    messageOneField.setWrapStyleWord(true);
    messageOneField.setRows(10);
    messageTwoField = new JTextArea();
    messageTwoField.setRows(10);
    messageTwoField.setWrapStyleWord(true);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    add (messageOneLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    add (messageTwoLabel, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    add (new JScrollPane(messageOneField), gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;
    add (new JScrollPane(messageTwoField), gbc);
  }

  public String getMessageOne ()
  {
    return messageOneField.getText();
  }

  public String getMessageTwo ()
  {
    return messageTwoField.getText();
  }
}
