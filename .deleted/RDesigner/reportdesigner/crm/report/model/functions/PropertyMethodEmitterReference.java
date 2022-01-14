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
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * User: Martin
 * Date: 26.07.2006
 * Time: 21:02:40
 */
@SuppressWarnings({"HardCodedStringLiteral"})
public class PropertyMethodEmitterReference extends PropertyMethodEmitter
{
    public void emit(@NotNull ClassWriter cw, @NotNull String classPathName, @NotNull Property property)
    {
        String typeName = property.getTypeString();
        String getterName = property.getGetterName();
        String setterName = property.getSetterName();
        String fieldName = property.getName();

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, getterName, "()" + typeName, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, classPathName, fieldName, typeName);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, setterName, "(" + typeName + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, classPathName, fieldName, typeName);
        mv.visitVarInsn(Opcodes.ASTORE, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, classPathName, fieldName, typeName);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classPathName, "getUndo", "()L" + FunctionGenerator.UNDO_CLASS_PATH + ";");
        Label l0 = new Label();
        mv.visitJumpInsn(Opcodes.IFNULL, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classPathName, "getUndo", "()L" + FunctionGenerator.UNDO_CLASS_PATH + ";");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, FunctionGenerator.UNDO_CLASS_PATH, "isInProgress", "()Z");
        mv.visitJumpInsn(Opcodes.IFNE, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classPathName, "getUndo", "()L" + FunctionGenerator.UNDO_CLASS_PATH + ";");
        mv.visitLdcInsn(fieldName);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, FunctionGenerator.UNDO_CLASS_PATH, "startTransaction", "(Ljava/lang/String;)V");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classPathName, "getUndo", "()L" + FunctionGenerator.UNDO_CLASS_PATH + ";");
        mv.visitTypeInsn(Opcodes.NEW, FunctionGenerator.REFLECTION_UNDO_ENTRY_CLASS_PATH);
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitLdcInsn(setterName);
        mv.visitLdcInsn(Type.getType(typeName));
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, FunctionGenerator.REFLECTION_UNDO_ENTRY_CLASS_PATH, "<init>", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)V");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, FunctionGenerator.UNDO_CLASS_PATH, "add", "(L" + FunctionGenerator.UNDO_ENTRY_CLASS_PATH + ";)V");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classPathName, "getUndo", "()L" + FunctionGenerator.UNDO_CLASS_PATH + ";");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, FunctionGenerator.UNDO_CLASS_PATH, "endTransaction", "()V");
        mv.visitLabel(l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitLdcInsn(fieldName);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, classPathName, "firePropertyChange", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(8, 3);
        mv.visitEnd();
    }
}
