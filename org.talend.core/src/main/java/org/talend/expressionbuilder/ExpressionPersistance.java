// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.expressionbuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.expression.EMFExpression;
import org.talend.core.model.expression.EMFVariable;
import org.talend.core.model.expression.ExpressionFactory;
import org.talend.core.model.expression.ExpressionPackage;
import org.talend.core.model.general.Project;
import org.talend.expressionbuilder.test.shadow.Expression;
import org.talend.expressionbuilder.test.shadow.Variable;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionPersistance.java 下午01:26:21 2007-9-24 +0000 (2007-9-24) yzhang $
 * 
 */
public class ExpressionPersistance {

    public static final String EXPRESSION_FOLDER_NAME = "ExpressionBuilder";

    public static final String CONFIGURATION_FOLDER_NAME = "configuration";

    private String ownerId;

    private static ExpressionPersistance expressionPersistance;

    private String path;

    private ResourceSet resourceSet;

    private EList<EObject> contents;

    private String rootFolderPath;

    /**
     * yzhang ExpressionPersistance constructor comment.
     */
    private ExpressionPersistance() {
    }

    /**
     * yzhang Comment method "getInstance".
     * 
     * @return
     */
    public static ExpressionPersistance getInstance() {
        if (expressionPersistance == null) {
            expressionPersistance = new ExpressionPersistance();
        }
        return expressionPersistance;
    }

    /**
     * Getter for ownerId.
     * 
     * @return the ownerId
     */
    public String getOwnerId() {
        return this.ownerId;
    }

    /**
     * Sets the ownerId.
     * 
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path.
     * 
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * yzhang Comment method "convert".
     * 
     * @param expression
     * @return
     */
    private EMFExpression convert(Expression expression) {

        EMFExpression emfExpression = ExpressionFactory.eINSTANCE.createEMFExpression();
        emfExpression.setId(this.ownerId);
        emfExpression.setExpression(expression.getExpression());
        for (Variable variable : expression.getVariables()) {
            EMFVariable emfVariable = ExpressionFactory.eINSTANCE.createEMFVariable();
            emfVariable.setName(variable.getName());
            emfVariable.setValue(variable.getValue());
            emfVariable.setTalendType(variable.getTalendType());
            emfVariable.setNullable(variable.isNullable());
            emfExpression.getVariables().add(emfVariable);
        }

        return emfExpression;
    }

    /**
     * yzhang Comment method "convert".
     * 
     * @param emfExpression
     * @return
     */
    private Expression convert(EMFExpression emfExpression) {

        List<Variable> vars = new ArrayList<Variable>();

        for (EMFVariable emfVar : emfExpression.getVariables()) {
            vars.add(new Variable(emfVar.getName(), emfVar.getValue(), emfVar.getTalendType(), emfVar.isNullable()));
        }

        return new Expression(emfExpression.getExpression(), vars);
    }

    /**
     * yzhang Comment method "saveExpression".
     * 
     * @param expression
     */
    public void saveExpression(Expression expression) {

        boolean fileExist = initContents();

        EMFExpression emfExpression = convert(expression);
        Resource resource = resourceSet.createResource(URI.createURI("http:///My.expression"));

        File file = new File(path);

        if (this.contents != null && fileExist) {
            for (EObject eObject : this.contents) {
                if (eObject instanceof EMFExpression) {
                    if (((EMFExpression) eObject).getId().equals(this.ownerId)) {
                        this.contents.remove(eObject);
                        break;
                    }
                }
            }
            this.contents.add(emfExpression);
            resource.getContents().addAll(this.contents);
        } else {
            resource.getContents().add(emfExpression);
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            resource.save(new FileOutputStream(file), null);
        } catch (IOException e) {
            RuntimeExceptionHandler.process(e);
        }

    }

    /**
     * yzhang Comment method "loadExpression".
     * 
     * @return
     */
    public Expression loadExpression() {

        if (!initContents()) {
            return new Expression();
        }

        for (EObject eObject : contents) {
            if (eObject instanceof EMFExpression && ((EMFExpression) eObject).getId().equals(this.ownerId)) {
                return convert((EMFExpression) eObject);
            }
        }
        return new Expression();
    }

    /**
     * yzhang Comment method "initContents".
     */
    private boolean initContents() {

        resourceSet = new ResourceSetImpl();

        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

        resourceSet.getPackageRegistry().put(ExpressionPackage.eNS_URI, ExpressionPackage.eINSTANCE);

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        URI uri = URI.createFileURI(file.getAbsolutePath());
        Resource resource = resourceSet.getResource(uri, true);
        this.contents = resource.getContents();

        return true;
    }

    /**
     * yzhang Comment method "jobNameChanged".
     * 
     * @param oldJobName
     * @param newJobName
     */
    public void jobNameChanged(String oldJobName, String newJobName) {
        IPath oldFilePath = new Path(getExpressionStoreFolderPath()).append(oldJobName + ".xml");
        IPath newFilePath = new Path(getExpressionStoreFolderPath()).append(newJobName + ".xml");
        File oldFile = new File(oldFilePath.toOSString());
        if (!oldFile.exists()) {
            return;
        }
        File newFile = new File(newFilePath.toOSString());
        oldFile.renameTo(newFile);
    }

    /**
     * yzhang Comment method "jobDeleted".
     * 
     * @param jobName
     */
    public void jobDeleted(String jobName) {
        IPath filePath = new Path(getExpressionStoreFolderPath()).append(jobName + ".xml");
        File file = new File(filePath.toOSString());
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * yzhang Comment method "getExpressionStoreFolder".
     * 
     * @return
     */
    private String getExpressionStoreFolderPath() {

        if (rootFolderPath == null) {

            if (path == null) {
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                RepositoryContext repositoryContext = (RepositoryContext) CorePlugin.getContext().getProperty(
                        Context.REPOSITORY_CONTEXT_KEY);
                Project project = repositoryContext.getProject();
                IProject p = root.getProject(project.getTechnicalLabel());

                IFolder configurationFolder = p.getFolder(CONFIGURATION_FOLDER_NAME);
                if (!configurationFolder.exists()) {
                    try {
                        configurationFolder.create(true, true, null);
                    } catch (CoreException e) {
                        RuntimeExceptionHandler.process(e);
                    }
                }

                IFolder expressionFolder = configurationFolder.getFolder(EXPRESSION_FOLDER_NAME);
                if (!expressionFolder.exists()) {
                    try {
                        expressionFolder.create(true, true, null);
                    } catch (CoreException e) {
                        RuntimeExceptionHandler.process(e);
                    }
                }
                path = expressionFolder.getLocation().toOSString();
            }

            int index = path.indexOf(EXPRESSION_FOLDER_NAME) + EXPRESSION_FOLDER_NAME.length();

            rootFolderPath = path.substring(0, index);
        }

        return rootFolderPath;

    }

}
