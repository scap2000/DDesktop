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
package org.pentaho.reportdesigner.crm.report.model.functions;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * User: Martin
 * Date: 26.07.2006
 * Time: 17:43:35
 */
@SuppressWarnings({"HardCodedStringLiteral", "UseOfSystemOutOrSystemErr"})
public class FunctionGenerator
{
    public static void main(@NotNull String[] args) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException
    {
        Property property1 = new Property(Integer.class, "testField", PropertyKeys.GROUP_UNKNOWN, -1);
        Property property2 = new Property(int.class, "intField", PropertyKeys.GROUP_UNKNOWN, -1);

        FunctionGenerator functionGenerator = new FunctionGenerator("ch.gridvision.bla.TestClass");
        functionGenerator.addProperty(property1);
        functionGenerator.addProperty(property2);
        byte[] bytes = functionGenerator.generateClass();
        //noinspection IOResourceOpenedButNotSafelyClosed
        FileOutputStream fos = new FileOutputStream("TestClass.class");
        fos.write(bytes);
        fos.close();

        byte[] bytesBeanInfo = functionGenerator.generateBeanInfo();
        //noinspection IOResourceOpenedButNotSafelyClosed
        FileOutputStream fosBeanInfo = new FileOutputStream("TestClassBeanInfo.class");
        fosBeanInfo.write(bytesBeanInfo);
        fosBeanInfo.close();

        Class aClass = FunctionGenerator.defineClass(functionGenerator);
        ReportFunctionElement rfe = (ReportFunctionElement) aClass.newInstance();
        Undo undo = new Undo();
        rfe.setUndo(undo);

        Class<?> beanInfoClass = Class.forName("ch.gridvision.bla.TestClassBeanInfo");
        Constructor<?> constructor = beanInfoClass.getConstructor();
        System.out.println("constructor = " + constructor);

        Object beanInfo = beanInfoClass.newInstance();
        System.out.println("beanInfo = " + beanInfo);

        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods)
        {
            System.out.println("method = " + method);
            if (method.getParameterTypes().length == 1)//setter
            {
                Object o = method.invoke(rfe, Integer.valueOf(123));
                System.out.println("o = " + o);
            }
        }

        for (Method method : methods)
        {
            System.out.println("method = " + method);
            if (method.getParameterTypes().length == 0)//getter
            {
                Object o = method.invoke(rfe);
                System.out.println("o = " + o);
            }
        }

        undo.debugPrint();
    }


    private static final boolean DEBUG_WRITE_CLASS_FILES = false;
    private static final boolean DEBUG_WRITE_BEAN_INFO_CLASS_FILES = false;

    @NotNull
    public static final String REPORT_FUNCTION_ELEMENT_CLASS_PATH = "org/pentaho/reportdesigner/crm/report/model/ReportFunctionElement";
    @NotNull
    public static final String UNDO_CLASS_PATH = "org/pentaho/reportdesigner/lib/client/undo/Undo";
    @NotNull
    public static final String UNDO_ENTRY_CLASS_PATH = "org/pentaho/reportdesigner/lib/client/undo/UndoEntry";
    @NotNull
    public static final String REFLECTION_UNDO_ENTRY_CLASS_PATH = "org/pentaho/reportdesigner/lib/client/undo/ReflectionUndoEntry";

    @NotNull
    public static final String GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH = "org/pentaho/reportdesigner/crm/report/model/GroupingPropertyDescriptor";

    @NotNull
    public static final String PACKAGE_PREFIX = "generated.";


    @NotNull
    public static Class<ReportFunctionElement> defineClass(@NotNull FunctionGenerator functionGenerator)
    {
        byte[] bytes = functionGenerator.generateClass();
        byte[] bytesBeanInfo = functionGenerator.generateBeanInfo();

        if (DEBUG_WRITE_CLASS_FILES)
        {
            debugWriteClassFile(functionGenerator.getClassname(), bytes);
        }
        if (DEBUG_WRITE_BEAN_INFO_CLASS_FILES)
        {
            debugWriteClassFile(functionGenerator.getClassnameBeanInfo(), bytesBeanInfo);
        }

        ClassLoader classLoader = FunctionGenerator.class.getClassLoader();
        if (classLoader == null)
        {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        if (classLoader == null)
        {
            throw new IllegalStateException("no classloader found for function injection");
        }

        try
        {
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);//NON-NLS
            method.setAccessible(true);
            Object clazz = method.invoke(classLoader, functionGenerator.getClassname(), bytes, Integer.valueOf(0), Integer.valueOf(bytes.length));
            /*Object clazzBeanInfo = */
            method.invoke(classLoader, functionGenerator.getClassnameBeanInfo(), bytesBeanInfo, Integer.valueOf(0), Integer.valueOf(bytesBeanInfo.length));
            //noinspection unchecked
            return (Class<ReportFunctionElement>) clazz;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("class generation failed miserably", e);
        }
    }


    private static void debugWriteClassFile(@NotNull String classname, @NotNull byte[] bytes)
    {
        FileOutputStream fos = null;
        try
        {
            int i = classname.lastIndexOf('.');
            if (i != -1)
            {
                classname = classname.substring(i + 1);
            }
            //noinspection IOResourceOpenedButNotSafelyClosed
            fos = new FileOutputStream(classname + ".class");
            fos.write(bytes);
            fos.close();
        }
        catch (IOException e)
        {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        finally
        {
            IOUtil.closeStream(fos);
        }
    }


    @NotNull
    private LinkedHashMap<String, Property> properties;
    @NotNull
    private String classname;

    @NotNull
    private PropertyMethodEmitter propertyMethodEmitterPrimitive;
    @NotNull
    private PropertyMethodEmitter propertyMethodEmitterReference;


    public FunctionGenerator(@NotNull String classname)
    {
        this.classname = classname;
        this.properties = new LinkedHashMap<String, Property>();

        propertyMethodEmitterPrimitive = new PropertyMethodEmitterPrimitive();
        propertyMethodEmitterReference = new PropertyMethodEmitterReference();
    }


    @NotNull
    public String getClassname()
    {
        return classname;
    }


    @NotNull
    public String getClassnameBeanInfo()
    {
        return classname + "BeanInfo";
    }


    public void addProperty(@NotNull Property property)
    {
        properties.put(property.getName(), property);
    }


    @NotNull
    private byte[] generateClass()
    {
        ClassWriter cw = new ClassWriter(false);
        MethodVisitor mv;

        String classPathName = classname.replace('.', '/');
        int index = classname.lastIndexOf('.');
        String simpleClassName;
        if (index == -1)
        {
            simpleClassName = classname;
        }
        else
        {
            simpleClassName = classname.substring(index + 1);
        }

        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, classPathName, null, REPORT_FUNCTION_ELEMENT_CLASS_PATH, null);

        cw.visitSource(simpleClassName + ".java", null);//NON-NLS

        for (Property property : properties.values())
        {
            //name is already a field on the superclass, so we don't generate field/getter/setter
            if (!"name".equals(property.getName()))
            {
                /*fv = */
                cw.visitField(Opcodes.ACC_PRIVATE, property.getName(), property.getTypeString(), null, null);
            }
        }

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);//NON-NLS
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, REPORT_FUNCTION_ELEMENT_CLASS_PATH, "<init>", "()V");//NON-NLS
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        for (Property property : properties.values())
        {
            //name is already a field on the superclass, so we don't generate field/getter/setter
            if (!"name".equals(property.getName()))
            {
                if (property.isPrimitiveType())
                {
                    propertyMethodEmitterPrimitive.emit(cw, classPathName, property);
                }
                else
                {
                    propertyMethodEmitterReference.emit(cw, classPathName, property);
                }
            }
        }

        cw.visitEnd();

        return cw.toByteArray();
    }


    @NotNull
    private byte[] generateBeanInfo()
    {
        ClassWriter cw = new ClassWriter(false);
        FieldVisitor fv;
        MethodVisitor mv;

        String classname = this.classname + "BeanInfo";

        String classPathNameBeanInfo = classname.replace('.', '/');
        String classPathName = this.classname.replace('.', '/');

        int index = classname.lastIndexOf('.');
        String simpleClassName;
        if (index == -1)
        {
            simpleClassName = classname;
        }
        else
        {
            simpleClassName = classname.substring(index + 1);
        }

        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, classPathNameBeanInfo, null, "java/beans/SimpleBeanInfo", null);

        cw.visitSource(simpleClassName + ".java", null);

        fv = cw.visitField(Opcodes.ACC_PRIVATE, "groupingPropertyDescriptor", "[L" + GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH + ";", null, null);
        fv.visitEnd();

        //invoke super constructor
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, new String[]{"java/beans/IntrospectionException"});
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/beans/SimpleBeanInfo", "<init>", "()V");

        //ArrayList<GroupingPropertyDescriptor> groupingPropertyDescriptorsList = new ArrayList<GroupingPropertyDescriptor>();
        mv.visitTypeInsn(Opcodes.NEW, "java/util/ArrayList");
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
        mv.visitVarInsn(Opcodes.ASTORE, 1);

        for (Property property : properties.values())
        {
            addPropertyDescriptor(classPathName, property, mv);
        }

        //groupingPropertyDescriptor = groupingPropertyDescriptorsList.toArray(new GroupingPropertyDescriptor[groupingPropertyDescriptorsList.size()]);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I");
        mv.visitTypeInsn(Opcodes.ANEWARRAY, GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;");
        mv.visitTypeInsn(Opcodes.CHECKCAST, "[L" + GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH + ";");
        mv.visitFieldInsn(Opcodes.PUTFIELD, classPathNameBeanInfo, "groupingPropertyDescriptor", "[L" + GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH + ";");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(8, 2);
        mv.visitEnd();

        //public BeanDescriptor getBeanDescriptor()
        //{
        //    return new BeanDescriptor(AllTypesFunction.class);
        //}
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getBeanDescriptor", "()Ljava/beans/BeanDescriptor;", null, null);
        mv.visitCode();
        mv.visitTypeInsn(Opcodes.NEW, "java/beans/BeanDescriptor");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(Type.getType("L" + classPathName + ";"));
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/beans/BeanDescriptor", "<init>", "(Ljava/lang/Class;)V");
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(3, 1);
        mv.visitEnd();

        //public PropertyDescriptor[] getPropertyDescriptors()
        //{
        //    return groupingPropertyDescriptor;
        //}
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getPropertyDescriptors", "()[Ljava/beans/PropertyDescriptor;", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, classPathNameBeanInfo, "groupingPropertyDescriptor", "[L" + GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH + ";");
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }


    private void addPropertyDescriptor(@NotNull String classPathName, @NotNull Property property, @NotNull MethodVisitor mv)
    {
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitTypeInsn(Opcodes.NEW, GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH);
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(property.getName());
        mv.visitLdcInsn(property.getGroup());
        mv.visitInsn(Opcodes.ICONST_0);
        if (property.getSortingID() == -1)
        {
            mv.visitInsn(Opcodes.ICONST_M1);
        }
        else
        {
            mv.visitIntInsn(Opcodes.BIPUSH, property.getSortingID());
        }
        mv.visitLdcInsn(Type.getType("L" + classPathName + ";"));
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, GROUPING_PROPERTY_DESCRIPTOR_CLASS_PATH, "<init>", "(Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Class;)V");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z");
        mv.visitInsn(Opcodes.POP);
    }
}
