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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 18.11.2004
 * Time: 18:03:33
 */
public class WeakList<T> implements Iterable<T>
{
    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(WeakList.class.getName());

    private static final boolean DEBUG = false;

    @NotNull
    private ArrayList<Ref<T>> arrayList;


    public WeakList()
    {
        arrayList = new ArrayList<Ref<T>>();
    }


    public boolean add(@NotNull T elem)
    {
        Ref<T> ref = new Ref<T>(elem);

        if (!arrayList.contains(ref))
        {
            return arrayList.add(ref);
        }
        else
        {
            return false;
        }
    }


    public boolean remove(@NotNull T elem)
    {
        return arrayList.remove(new Ref<T>(elem));
    }


    public boolean contains(@NotNull T elem)
    {
        Ref<T> ref = new Ref<T>(elem);
        return arrayList.contains(ref);
    }


    @NotNull
    public Iterator<T> iterator()
    {
        ArrayList<T> tempList = new ArrayList<T>();
        for (Iterator<Ref<T>> iterator = arrayList.iterator(); iterator.hasNext();)
        {
            Ref<T> ref = iterator.next();
            T t = ref.get();
            if (t == null)
            {
                //noinspection ConstantConditions
                if (DEBUG && LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "WeakList.getIterator ref.getInfoText() = " + ref.getInfoText());
                iterator.remove();
            }
            else
            {
                tempList.add(t);
            }
        }

        return tempList.iterator();
    }


    @SuppressWarnings({"EqualsAndHashcode"})
    private static class Ref<T> extends WeakReference<T>
    {
        @NotNull
        private String infoText;


        private Ref(@NotNull T o)
        {
            super(o);

            if (DEBUG)
            {
                infoText = o.toString();
            }
        }


        @NotNull
        public String getInfoText()
        {
            return infoText;
        }


        public boolean equals(@NotNull Object obj)
        {
            if (!(obj instanceof Ref))
            {
                return false;
            }

            Ref r = (Ref) obj;
            if (get() != null)
            {
                return get().equals(r.get());
            }
            else
            {
                return r.get() == null;
            }
        }

    }
}
