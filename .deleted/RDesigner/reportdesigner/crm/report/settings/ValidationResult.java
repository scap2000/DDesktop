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
package org.pentaho.reportdesigner.crm.report.settings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: Martin
 * Date: 01.03.2006
 * Time: 18:13:42
 */
public class ValidationResult
{
    @NotNull
    private ArrayList<ValidationMessage> validationMessages;


    public ValidationResult()
    {
        validationMessages = new ArrayList<ValidationMessage>();
    }


    public ValidationResult(@NotNull Collection<? extends ValidationMessage> validationMessages)
    {
        this.validationMessages = new ArrayList<ValidationMessage>(validationMessages);
    }


    public void addValidationMessage(@NotNull ValidationMessage validationMessage)
    {
        validationMessages.add(validationMessage);
    }


    public void addValidationMessages(@NotNull ValidationMessage[] vms)
    {
        validationMessages.addAll(Arrays.asList(vms));
    }


    public void addValidationMessages(@NotNull Collection<? extends ValidationMessage> vms)
    {
        validationMessages.addAll(vms);
    }


    @NotNull
    public ArrayList<ValidationMessage> getValidationMessages(@NotNull ValidationMessage.Severity[] severities)
    {
        ArrayList<ValidationMessage> filteredValidationMessages = new ArrayList<ValidationMessage>();
        for (ValidationMessage validationMessage : validationMessages)
        {
            for (ValidationMessage.Severity severity : severities)
            {
                if (validationMessage.getSeverity().equals(severity))
                {
                    filteredValidationMessages.add(validationMessage);
                }
            }
        }

        ArrayList<ValidationMessage> orderedValidationMessages = new ArrayList<ValidationMessage>(filteredValidationMessages);
        Collections.sort(orderedValidationMessages, new Comparator<ValidationMessage>()
        {
            public int compare(@NotNull ValidationMessage vm1, @NotNull ValidationMessage vm2)
            {
                return vm1.getSeverity().compareTo(vm2.getSeverity());
            }
        });

        return orderedValidationMessages;
    }


    public boolean containsValidationMessages(@NotNull ValidationMessage.Severity[] severity)
    {
        for (ValidationMessage validationMessage : validationMessages)
        {
            for (ValidationMessage.Severity s : severity)
            {
                if (validationMessage.getSeverity().equals(s))
                {
                    return true;
                }
            }
        }

        return false;
    }


}
