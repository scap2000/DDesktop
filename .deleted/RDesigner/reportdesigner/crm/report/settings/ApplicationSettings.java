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

import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserException;
import org.gjt.xpp.XmlPullParserFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.Unit;
import org.pentaho.reportdesigner.crm.report.util.TranslationUtil;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 07.02.2006
 * Time: 20:12:30
 */
public class ApplicationSettings implements XMLExternalizable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ApplicationSettings.class.getName());

    @NotNull
    private static final ApplicationSettings instance = new ApplicationSettings();


    @NotNull
    public static ApplicationSettings getInstance()
    {
        return instance;
    }


    @NotNull
    private PropertyChangeSupport propertyChangeSupport;

    private boolean loaded;

    @NotNull
    private Locale language;

    @NotNull
    private Unit unit;

    private boolean useDefaultBrowser;
    @NotNull
    private String customBrowserExecutable;
    @NotNull
    private String customBrowserParameters;

    @NotNull
    private ProxySettings proxySettings;

    @NotNull
    private ExternalToolSettings externalToolSettings;

    @NotNull
    private final List<Locale> availableLocales;


    private ApplicationSettings()
    {
        propertyChangeSupport = new PropertyChangeSupport(this);
        loaded = false;

        proxySettings = new ProxySettings();

        unit = Unit.POINTS;

        useDefaultBrowser = true;
        customBrowserExecutable = "";
        customBrowserParameters = "{0}";

        language = Locale.getDefault();

        availableLocales = TranslationUtil.findLocales();

        if (!availableLocales.contains(language))
        {
            language = availableLocales.get(0);
        }

        Authenticator.setDefault(new Authenticator()
        {
            @NotNull
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(getProxySettings().getProxyUser(), getProxySettings().getProxyPassword().toCharArray());
            }
        });

        externalToolSettings = new ExternalToolSettings();
    }


    @NotNull
    public List<Locale> getAvailableLocales()
    {
        return availableLocales;
    }


    @NotNull
    public ExternalToolSettings getExternalToolSettings()
    {
        return externalToolSettings;
    }


    @NotNull
    public ProxySettings getProxySettings()
    {
        return proxySettings;
    }


    @NotNull
    public Locale getLanguage()
    {
        return language;
    }


    public void setLanguage(@NotNull Locale language)
    {
        //noinspection ConstantConditions
        if (language == null)
        {
            throw new IllegalArgumentException("language must not be null");
        }

        Locale oldLanguage = this.language;
        this.language = language;

        propertyChangeSupport.firePropertyChange(PropertyKeys.LANGUAGE, oldLanguage, language);
    }


    @NotNull
    public Unit getUnit()
    {
        return unit;
    }


    public void setUnit(@NotNull Unit unit)
    {
        //noinspection ConstantConditions
        if (unit == null)
        {
            throw new IllegalArgumentException("unit must not be null");
        }

        Unit oldUnit = this.unit;
        this.unit = unit;

        propertyChangeSupport.firePropertyChange(PropertyKeys.UNIT, oldUnit, unit);
    }


    public boolean isUseDefaultBrowser()
    {
        return useDefaultBrowser;
    }


    public void setUseDefaultBrowser(boolean useDefaultBrowser)
    {
        boolean oldUseDefaultBrowser = this.useDefaultBrowser;
        this.useDefaultBrowser = useDefaultBrowser;

        propertyChangeSupport.firePropertyChange(PropertyKeys.USE_DEFAULT_BROWSER, oldUseDefaultBrowser, useDefaultBrowser);
    }


    @NotNull
    public String getCustomBrowserExecutable()
    {
        return customBrowserExecutable;
    }


    public void setCustomBrowserExecutable(@NotNull String customBrowserExecutable)
    {
        //noinspection ConstantConditions
        if (customBrowserExecutable == null)
        {
            throw new IllegalArgumentException("customBrowserExecutable must not be null");
        }

        String oldCustomBrowserString = this.customBrowserExecutable;
        this.customBrowserExecutable = customBrowserExecutable;

        propertyChangeSupport.firePropertyChange(PropertyKeys.CUSTOM_BROWSER_EXECUTABLE, oldCustomBrowserString, customBrowserExecutable);
    }


    @NotNull
    public String getCustomBrowserParameters()
    {
        return customBrowserParameters;
    }


    public void setCustomBrowserParameters(@NotNull String customBrowserParameters)
    {
        //noinspection ConstantConditions
        if (customBrowserParameters == null)
        {
            throw new IllegalArgumentException("customBrowserParameters must not be null");
        }

        String oldCustomBrowserParameters = this.customBrowserParameters;
        this.customBrowserParameters = customBrowserParameters;

        propertyChangeSupport.firePropertyChange(PropertyKeys.CUSTOM_BROWSER_PARAMETERS, oldCustomBrowserParameters, customBrowserParameters);
    }


    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    public void addPropertyChangeListener(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }


    public void removePropertyChangeListener(@NotNull String propertyName, @NotNull PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }


    public boolean isLoaded()
    {
        return loaded;
    }


    public void loadFromFile(@NotNull File file)
    {
        BufferedReader bufferedReader = null;
        try
        {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            //noinspection IOResourceOpenedButNotSafelyClosed
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), XMLConstants.ENCODING));
            XMLContext xmlContext = new XMLContext();
            xmlPullParser.setInput(bufferedReader);
            xmlPullParser.next(); // get first start tag
            XmlPullNode node = xmlPullParserFactory.newPullNode(xmlPullParser);

            if (XMLConstants.WORKSPACE_SETTINGS.equals(node.getRawName()))//silly me, remove in a future version
            {
                readObject(node, xmlContext);
            }
            else if (XMLConstants.APPLICATION_SETTINGS.equals(node.getRawName()))
            {
                readObject(node, xmlContext);
            }

            node.resetPullNode();

            loaded = true;
        }
        catch (XmlPullParserException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        catch (FileNotFoundException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ReportDialog.loadWorkspaceSettings ", e);
        }
        catch (IOException e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
        finally
        {
            IOUtil.closeStream(bufferedReader);
        }
    }


    public void writeSettings(@NotNull File file) throws IOException
    {
        FileOutputStream outputStream = null;
        try
        {
            //noinspection IOResourceOpenedButNotSafelyClosed
            outputStream = new FileOutputStream(file);
            XMLContext xmlContext = new XMLContext();
            XMLWriter xmlWriter0 = new XMLWriter(outputStream, true);
            xmlWriter0.writeDefaultProlog();
            xmlWriter0.startElement(XMLConstants.APPLICATION_SETTINGS);
            externalizeObject(xmlWriter0, xmlContext);
            xmlWriter0.closeElement(XMLConstants.APPLICATION_SETTINGS);
            xmlWriter0.close();
        }
        finally
        {
            IOUtil.closeStream(outputStream);
        }
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute("v", "1");
        xmlWriter.writeProperty(PropertyKeys.LANGUAGE, language.toString());
        xmlWriter.writeProperty("unit", unit.toString());
        xmlWriter.startElement(PropertyKeys.BROWSER_SETTINGS).writeAttribute(PropertyKeys.USE_DEFAULT_BROWSER, String.valueOf(useDefaultBrowser)).writeAttribute(PropertyKeys.CUSTOM_BROWSER_EXECUTABLE, customBrowserExecutable).writeAttribute(PropertyKeys.CUSTOM_BROWSER_PARAMETERS, customBrowserParameters).closeElement(PropertyKeys.BROWSER_SETTINGS);

        xmlWriter.startElement(PropertyKeys.PROXY_SETTINGS);
        proxySettings.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.PROXY_SETTINGS);

        xmlWriter.startElement(PropertyKeys.EXTERNAL_TOOL_SETTINGS);
        externalToolSettings.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.EXTERNAL_TOOL_SETTINGS);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws IOException, XmlPullParserException
    {
        while (!node.isFinished())
        {
            Object obj = node.readNextChild();
            if (obj instanceof XmlPullNode)
            {
                XmlPullNode child = (XmlPullNode) obj;
                if (XMLConstants.PROPERTY.equals(child.getRawName()))
                {
                    String key = child.getAttributeValueFromRawName(XMLConstants.NAME);
                    if (PropertyKeys.LANGUAGE.equals(key))
                    {
                        String lang = readContent(child);
                        if (lang != null)
                        {
                            language = TranslationUtil.createLocaleFromString(lang);

                            if (!availableLocales.contains(language))
                            {
                                language = availableLocales.get(0);
                            }
                        }
                    }
                    else
                    {
                        String unitString = readContent(child);
                        if (unitString != null)
                        {
                            try
                            {
                                unit = Unit.valueOf(unitString);
                            }
                            catch (Exception e)
                            {
                                if (LOG.isLoggable(Level.SEVERE)) LOG.log(Level.SEVERE, "ApplicationSettings.readObject ", e);
                            }
                        }
                    }
                }
                else if (PropertyKeys.BROWSER_SETTINGS.equals(child.getRawName()))
                {
                    String useDefaultBrowserTemp = child.getAttributeValueFromRawName(PropertyKeys.USE_DEFAULT_BROWSER);
                    if (useDefaultBrowserTemp != null)
                    {
                        useDefaultBrowser = Boolean.parseBoolean(useDefaultBrowserTemp);
                    }

                    String customBrowserExecutableTemp = child.getAttributeValueFromRawName(PropertyKeys.CUSTOM_BROWSER_EXECUTABLE);
                    if (customBrowserExecutableTemp != null)
                    {
                        customBrowserExecutable = customBrowserExecutableTemp;
                    }

                    String customBrowserParametersTemp = child.getAttributeValueFromRawName(PropertyKeys.CUSTOM_BROWSER_PARAMETERS);
                    if (customBrowserParametersTemp != null)
                    {
                        customBrowserParameters = customBrowserParametersTemp;
                    }
                }
                else if (PropertyKeys.PROXY_SETTINGS.equals(child.getRawName()))
                {
                    proxySettings = new ProxySettings();
                    proxySettings.readObject(child, xmlContext);
                }
                else if (PropertyKeys.EXTERNAL_TOOL_SETTINGS.equals(child.getRawName()))
                {
                    externalToolSettings = new ExternalToolSettings();
                    externalToolSettings.readObject(child, xmlContext);
                }


                child.resetPullNode();
            }
        }
    }


    @Nullable
    private String readContent(@NotNull XmlPullNode propertyNode) throws IOException, XmlPullParserException
    {
        while (!propertyNode.isFinished())
        {
            Object value = propertyNode.readNextChild();
            if (value instanceof String)
            {
                String s = (String) value;
                return s;
            }
        }
        return null;
    }
}
