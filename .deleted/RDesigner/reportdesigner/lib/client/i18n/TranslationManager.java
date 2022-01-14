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
package org.pentaho.reportdesigner.lib.client.i18n;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.Collator;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 05.03.2005
 * Time: 06:29:18
 */
public class TranslationManager
{
    @NotNull
    @NonNls
    public static final String COMMON_BUNDLE_PREFIX = "COMMON_BUNDLE_PREFIX";

    @NotNull
    @NonNls
    private static final Logger LOG = Logger.getLogger(TranslationManager.class.getName());

    @NotNull
    private static final TranslationManager instance = new TranslationManager();


    @NotNull
    public static TranslationManager getInstance()
    {
        return instance;
    }


    @NotNull
    private TreeSet<String> missingKeys;
    @NotNull
    private HashMap<String, ResourceBundle> prefixBundleMap;
    @NotNull
    private HashSet<Locale> supportedLocales;


    private TranslationManager()
    {
        Collator collator = Collator.getInstance();
        collator.setStrength(Collator.PRIMARY);
        missingKeys = new TreeSet<String>(collator);

        prefixBundleMap = new HashMap<String, ResourceBundle>();
        supportedLocales = new HashSet<Locale>();
    }


    public void addBundle(@NotNull @NonNls String prefix, @NotNull ResourceBundle resourceBundle) throws BundleAlreadyExistsException
    {
        if (prefixBundleMap.containsKey(prefix))
        {
            throw new BundleAlreadyExistsException();
        }

        prefixBundleMap.put(prefix, resourceBundle);
    }


    @NotNull
    public Locale[] getSupportedLocales()
    {
        Locale[] localeList = new Locale[supportedLocales.size()];
        localeList = supportedLocales.toArray(localeList);

        return localeList;
    }


    public void addSupportedLocale(@NotNull Locale locale)
    {
        supportedLocales.add(locale);
    }


    @NotNull
    private String getRawTranslation(@NotNull @NonNls String prefix, @Nullable @NonNls String key, @NotNull @NonNls Object... parameters)
    {
        if (key == null)
        {
            if (LOG.isLoggable(Level.FINE)) //noinspection ThrowableInstanceNeverThrown
                LOG.log(Level.FINE, "TranslationManager.getTranslation " + prefix, new RuntimeException("key = null, locale = " + Locale.getDefault()));
            return "no key defined";//NON-NLS
        }

        String fixdeKey = key;

        if (key.contains(" "))
        {
            fixdeKey = key.replace(' ', '_');
        }

        try
        {
            ResourceBundle bundle = prefixBundleMap.get(prefix);
            if (bundle != null)
            {
                String text = bundle.getString(key);

                if (text != null)
                {
                    if (parameters.length > 0)
                    {
                        for (int i = 0; i < parameters.length; i++)
                        {
                            Object parameter = parameters[i];
                            if (parameter instanceof String)
                            {
                                String p = (String) parameter;
                                if (p.indexOf('_') != -1)
                                {
                                    parameters[i] = p.replace("_", "__");
                                }
                            }
                        }

                        text = MessageFormat.format(text, parameters);
                    }
                    return text;
                }
            }
        }
        catch (Throwable e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TranslationManager.getTranslation ", e);
            //TODO do the right thing, perhaps it is contained in the common bundle, but catching throwable seems to be a bit broad
            //TODO we can remove the whole fallback strategy as it is only used in case of a user using the wrong prefix (?)
            //TODO would be better to tell the user that he is using the wrong prefix
        }
        //
        //if (!COMMON_BUNDLE_PREFIX.equals(prefix))
        //{
        //    try
        //    {
        //        ResourceBundle bundle = prefixBundleMap.get(COMMON_BUNDLE_PREFIX);
        //        if (bundle != null)
        //        {
        //            String text = bundle.getString(key);
        //
        //            if (text != null)
        //            {
        //                if (parameters.length > 0)
        //                {
        //                    text = MessageFormat.format(text, parameters);
        //                }
        //                return text;
        //            }
        //        }
        //    }
        //    catch (Throwable e)
        //    {
        //        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "TranslationManager.getTranslation e = " + e);
        //    }
        //}

        if (parameters.length > 0)
        {
            StringBuilder stringBuilder = new StringBuilder(prefix).append(":").append(fixdeKey);
            stringBuilder.append(" (");
            for (Object o : parameters)
            {
                stringBuilder.append(o.getClass().getName());
                stringBuilder.append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append(")");
            missingKeys.add(stringBuilder.toString());
        }
        else
        {
            missingKeys.add(fixdeKey);
        }

        return key;
    }


    @NotNull
    public String getTranslation(@NotNull @NonNls String prefix, @NotNull @NonNls String key, @NotNull @NonNls Object... parameters)
    {
        String text = getRawTranslation(prefix, key, parameters);
        //remove all underscores not escaped by underscore
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length() - 1; i++)
        {
            char c1 = text.charAt(i);
            char c2 = text.charAt(i + 1);

            if (c1 == '_' && c2 != '_')
            {
                //ignore
            }
            else if (c1 == '_'/* && c2 == '_'*/)
            {
                //was escaped underscore, append one underscore and skip the second
                sb.append('_');
                i++;
            }
            else
            {
                sb.append(c1);
            }
        }
        if (text.length() > 0 && text.charAt(text.length() - 1) != '_')
        {
            sb.append(text.charAt(text.length() - 1));//append last char whatever it might be
        }

        text = sb.toString();
        return text;
    }


    public int getMnemonic(@NotNull @NonNls String prefix, @NotNull @NonNls String key, @NotNull @NonNls Object... parameters)
    {
        String translation = getRawTranslation(prefix, key, parameters);
        if (translation.equals(key))
        {
            return 0;
        }
        else if (translation.length() == 0)
        {
            return 0;
        }
        else if (translation.length() == 1)
        {
            return translation.charAt(0);
        }
        else
        {
            //try to find a mnemonic (character prefixed with an underscore, and not escaped with an additional underscore)
            for (int i = 0; i < translation.length() - 1; i++)
            {
                char c1 = translation.charAt(i);
                char c2 = translation.charAt(i + 1);

                if (c1 == '_' && c2 != '_')
                {
                    return Character.toUpperCase(c2);
                }
                else if (c1 == '_'/* && c2 == '_'*/)
                {
                    i++;
                }
            }
            return 0;
        }
    }


    public int getDisplayedMnemonicIndex(@NotNull @NonNls String prefix, @NotNull @NonNls String key, @NotNull @NonNls Object... parameters)
    {
        String translation = getRawTranslation(prefix, key, parameters);
        //try to find a mnemonic (character prefixed with an underscore, and not escaped with an additional underscore)
        for (int i = 0; i < translation.length() - 1; i++)
        {
            char c1 = translation.charAt(i);
            char c2 = translation.charAt(i + 1);

            if (c1 == '_' && c2 != '_')
            {
                return i;
            }
            else if (c1 == '_'/* && c2 == '_'*/)
            {
                i++;
            }
        }
        return -1;
    }


    @NotNull
    public Collection<String> getMissingKeysList()
    {
        return Collections.unmodifiableCollection(missingKeys);
    }

}

