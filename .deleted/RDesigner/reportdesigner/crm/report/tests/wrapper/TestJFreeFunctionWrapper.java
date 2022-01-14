/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.tests.wrapper;

import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.lib.client.undo.ReflectionUndoEntry;

@SuppressWarnings({"ALL"})
public class TestJFreeFunctionWrapper extends ReportFunctionElement
{
    //private String field;

    private double number;
    private int intNumber;

    private boolean bool;
    private long millis;

    private float hugo;

    private short shorty;

    private byte bla;

    private char character;


    public TestJFreeFunctionWrapper()
    {
    }

    //public String getField()
    //{
    //    return field;
    //}
    //
    //
    //public void setField(final String field)
    //{
    //    final String oldField = this.field;
    //    this.field = field;
    //
    //    if (getUndo() != null && !getUndo().isInProgress())
    //    {
    //        getUndo().startTransaction(PropertyKeys.FIELD);
    //        getUndo().add(new UndoEntry()
    //        {
    //            public void undo()
    //            {
    //                setField(oldField);
    //            }
    //
    //
    //            public void redo()
    //            {
    //                setField(field);
    //            }
    //        });
    //        getUndo().endTransaction();
    //    }
    //
    //    firePropertyChange(PropertyKeys.FIELD, oldField, field);
    //}


    public double getNumber()
    {
        return number;
    }


    public void setNumber(double number)
    {
        double d = this.number;
        this.number = number;
        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("number");
            getUndo().add(new ReflectionUndoEntry(this, "setNumber", Double.TYPE, Double.valueOf(d), Double.valueOf(number)));
            getUndo().endTransaction();
        }
        firePropertyChange("number", Double.valueOf(d), Double.valueOf(number));
    }


    public int getIntNumber()
    {
        return intNumber;
    }


    public void setIntNumber(int intNumber)
    {
        int d = this.intNumber;
        this.intNumber = intNumber;
        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("intNumber");
            getUndo().add(new ReflectionUndoEntry(this, "setIntNumber", Integer.TYPE, Integer.valueOf(d), Integer.valueOf(intNumber)));
            getUndo().endTransaction();
        }
        firePropertyChange("intNumber", Integer.valueOf(d), Integer.valueOf(intNumber));
    }


    public boolean isBool()
    {
        return bool;
    }


    public void setBool(boolean bool)
    {
        boolean b = this.bool;
        this.bool = bool;

        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("bool");
            getUndo().add(new ReflectionUndoEntry(this, "setBool", Boolean.TYPE, Boolean.valueOf(b), Boolean.valueOf(bool)));
            getUndo().endTransaction();
        }
        firePropertyChange("bool", Boolean.valueOf(b), Boolean.valueOf(bool));
    }


    public long getMillis()
    {
        return millis;
    }


    public void setMillis(long millis)
    {
        long m = this.millis;
        this.millis = millis;

        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("millis");
            getUndo().add(new ReflectionUndoEntry(this, "setMillis", Long.TYPE, Long.valueOf(m), Long.valueOf(millis)));
            getUndo().endTransaction();
        }
        firePropertyChange("millis", Long.valueOf(m), Long.valueOf(millis));
    }


    public float getHugo()
    {
        return hugo;
    }


    public void setHugo(float hugo)
    {
        float h = this.hugo;
        this.hugo = hugo;

        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("hugo");
            getUndo().add(new ReflectionUndoEntry(this, "setHugo", Float.TYPE, Float.valueOf(h), Float.valueOf(hugo)));
            getUndo().endTransaction();
        }
        firePropertyChange("hugo", Float.valueOf(h), Float.valueOf(hugo));
    }


    public short getShorty()
    {
        return shorty;
    }


    public void setShorty(short shorty)
    {
        short s = this.shorty;
        this.shorty = shorty;

        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("shorty");
            getUndo().add(new ReflectionUndoEntry(this, "setShorty", Short.TYPE, Short.valueOf(s), Short.valueOf(shorty)));
            getUndo().endTransaction();
        }
        firePropertyChange("shorty", Short.valueOf(s), Short.valueOf(shorty));
    }


    public byte getBla()
    {
        return bla;
    }


    public void setBla(byte bla)
    {
        byte b = this.bla;
        this.bla = bla;

        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("bla");
            getUndo().add(new ReflectionUndoEntry(this, "setBla", Byte.TYPE, Byte.valueOf(b), Byte.valueOf(bla)));
            getUndo().endTransaction();
        }
        firePropertyChange("bla", Byte.valueOf(b), Byte.valueOf(bla));
    }


    public char getCharacter()
    {
        return character;
    }


    public void setCharacter(char character)
    {
        char c = this.character;
        this.character = character;

        if (getUndo() != null && !getUndo().isInProgress())
        {
            getUndo().startTransaction("character");
            getUndo().add(new ReflectionUndoEntry(this, "setCharacter", Character.TYPE, Character.valueOf(c), Character.valueOf(character)));
            getUndo().endTransaction();
        }
        firePropertyChange("character", Character.valueOf(c), Character.valueOf(character));
    }
}
