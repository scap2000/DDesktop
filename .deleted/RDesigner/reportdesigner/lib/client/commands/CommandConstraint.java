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
package org.pentaho.reportdesigner.lib.client.commands;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * User: Martin
 * Date: 19.05.2005
 * Time: 12:49:43
 */
public class CommandConstraint
{

    public enum ConstraintType
    {
        @NotNull FIRST,
        @NotNull LAST,
        @NotNull BEFORE,
        @NotNull AFTER
    }

    @NotNull
    public static final CommandConstraint FIRST = new CommandConstraint(ConstraintType.FIRST, "");
    @NotNull
    public static final CommandConstraint LAST = new CommandConstraint(ConstraintType.LAST, "");

    @NotNull
    private ConstraintType constraintType;
    @NotNull
    private String reference;


    public CommandConstraint(@NotNull ConstraintType constraintType, @NotNull @NonNls String reference)
    {
        //noinspection ConstantConditions
        if (constraintType == null)
        {
            throw new IllegalArgumentException("constrainedType must not be null");
        }
        //noinspection ConstantConditions
        if (reference == null)
        {
            throw new IllegalArgumentException("reference must not be null");
        }

        this.constraintType = constraintType;
        this.reference = reference;
    }


    @NotNull
    public ConstraintType getConstraintType()
    {
        return constraintType;
    }


    @NotNull
    public String getReference()
    {
        return reference;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (!(o instanceof CommandConstraint)) return false;

        final CommandConstraint commandConstraint = (CommandConstraint) o;

        if (!constraintType.equals(commandConstraint.constraintType)) return false;
        return reference.equals(commandConstraint.reference);
    }


    public int hashCode()
    {
        int result;
        result = constraintType.hashCode();
        result = 29 * result + reference.hashCode();
        return result;
    }
}
