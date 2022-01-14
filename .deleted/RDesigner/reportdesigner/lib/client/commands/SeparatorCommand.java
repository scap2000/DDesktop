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

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 25.11.2004
 * Time: 18:49:34
 */
public class SeparatorCommand extends AbstractCommand
{
    public SeparatorCommand()
    {
        super("SeparatorCommand");
    }


    public SeparatorCommand(@NotNull String name)
    {
        super(name);
    }


    public void update(@NotNull CommandEvent event)
    {
    }


    public void execute(@NotNull CommandEvent event)
    {
    }

}
