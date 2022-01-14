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
package org.pentaho.reportdesigner.crm.report.lineal;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 26.01.2006
 * Time: 10:47:17
 */
public interface LinealModelListener
{
    void guidLineAdded(@NotNull GuideLine guideLine);


    void guidLineRemoved(@NotNull GuideLine guideLine);


    void activationChanged(@NotNull GuideLine guideLine);


    void positionChanged(@NotNull GuideLine guideLine, double oldPosition);
}
