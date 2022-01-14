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
package org.pentaho.jfreereport.legacy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.Expression;
import org.jfree.report.function.FunctionUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class XYDataSetCollectorFunction extends AbstractFunction
{
    @NotNull
    private static final Logger LOG = Logger.getLogger(XYDataSetCollectorFunction.class.getName());


    @NotNull
    private ArrayList<String> seriesNames;
    @Nullable
    private String domainColumn;
    @Nullable
    private XYSeriesCollection xyDataset;
    @Nullable
    private String resetGroup;
    @NotNull
    private HashMap<String, XYSeries> series;


    public XYDataSetCollectorFunction()
    {
        this.seriesNames = new ArrayList<String>();
        series = new HashMap<String, XYSeries>();

        xyDataset = new XYSeriesCollection();
    }


    public void setSeriesName(final int index, @NotNull final String field)
    {
        if (seriesNames.size() == index)
        {
            seriesNames.add(field);
        }
        else
        {
            seriesNames.set(index, field);
        }
    }


    @NotNull
    public String getSeriesName(final int index)
    {
        return seriesNames.get(index);
    }


    public int getSeriesNameCount()
    {
        return seriesNames.size();
    }


    @NotNull
    public String[] getSeriesName()
    {
        return seriesNames.toArray(new String[seriesNames.size()]);
    }


    public void setSeriesName(@NotNull final String[] fields)
    {
        this.seriesNames.clear();
        this.seriesNames.addAll(Arrays.asList(fields));
    }


    @Nullable
    public String getDomainColumn()
    {
        return domainColumn;
    }


    public void setDomainColumn(@Nullable String domainColumn)
    {
        this.domainColumn = domainColumn;
    }


    @Nullable
    public String getResetGroup()
    {
        return resetGroup;
    }


    public void setResetGroup(@Nullable String resetGroup)
    {
        this.resetGroup = resetGroup;
    }


    @Nullable
    public Object getValue()
    {
        return xyDataset;
    }


    public void reportInitialized(@NotNull ReportEvent event)
    {
    }


    public void groupStarted(@NotNull ReportEvent event)
    {
        if (FunctionUtilities.isDefinedGroup(getResetGroup(), event))
        {
            // reset ...
            if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "XYDataSetCollectorFunction.groupStarted ");//NON-NLS
                XYSeriesCollection xyDataset = new XYSeriesCollection();
                for (int i = 0; i < getSeriesName().length; i++)
                {
                    String s = getSeriesName()[i];
                    XYSeries series = new XYSeries(s, true, true);
                    xyDataset.addSeries(series);
                    this.series.put(s, series);

                }
                this.xyDataset = xyDataset;
                //results.add(categoryDataset);
            }
            else
            {
                if (FunctionUtilities.isLayoutLevel(event))
                {
                    // Activate the current group, which was filled in the prepare run.
                    //currentIndex += 1;
                    //categoryDataset = results.get(currentIndex);
                }
            }
        }
        else
        {
            // reset ...
            if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "XYDataSetCollectorFunction.groupStarted ");//NON-NLS
                XYSeriesCollection xyDataset = new XYSeriesCollection();
                for (int i = 0; i < getSeriesName().length; i++)
                {
                    String s = getSeriesName()[i];
                    XYSeries series = new XYSeries(s, true, true);
                    xyDataset.addSeries(series);
                    this.series.put(s, series);
                }
                this.xyDataset = xyDataset;
                //results.add(categoryDataset);
            }
        }
    }


    public void itemsAdvanced(@NotNull final ReportEvent event)
    {
        if (FunctionUtilities.isDefinedPrepareRunLevel(this, event))
        {
            final Object domainValue = getDataRow().get(getDomainColumn());
            if (domainValue == null)
            {
                return;
            }
            if (!(domainValue instanceof Number))
            {
                return;
            }

            final Number x = (Number) domainValue;

            for (String sn : seriesNames)
            {
                final Object o = getDataRow().get(sn);
                if (o instanceof Number)
                {
                    Number y = (Number) o;
                    (series.get(sn)).add(x, y);
                }
            }
        }

    }


    @NotNull
    public Expression getInstance()
    {
        final XYDataSetCollectorFunction fn = (XYDataSetCollectorFunction) super.getInstance();
        fn.xyDataset = null;
        fn.series = new HashMap<String, XYSeries>();
        return fn;
    }

}
