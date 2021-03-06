// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.engine;

import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.updates.runtime.engine.factory.AbstractExtraUpdatesFactory;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.StatusException;

/**
 * created by ggu on Jul 17, 2014 Detailled comment
 *
 */
public class ExtraFeaturesUpdatesFactory {

    private final static ExtraFeaturesUpdatesReader updatesFactoryReader = new ExtraFeaturesUpdatesReader();

    /**
     * 
     * DOC ggu Comment method "retrieveUninstalledExtraFeatures".
     * 
     * 
     * Retrieve all uninstalled extra features.
     * 
     * @param monitor
     * @return
     */
    public void retrieveUninstalledExtraFeatures(IProgressMonitor monitor, Set<ExtraFeature> uninstalledExtraFeatures) {
        if (uninstalledExtraFeatures == null) {
            Assert.isNotNull(uninstalledExtraFeatures);
        }
        AbstractExtraUpdatesFactory[] updatesFactories = updatesFactoryReader.getUpdatesFactories();
        try {
            if (updatesFactories != null) {
                for (AbstractExtraUpdatesFactory factory : updatesFactories) {
                    factory.retrieveUninstalledExtraFeatures(monitor, uninstalledExtraFeatures);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public void beforeInstall() throws StatusException {
        AbstractExtraUpdatesFactory[] updatesFactories = updatesFactoryReader.getUpdatesFactories();
        if (updatesFactories != null) {
            for (AbstractExtraUpdatesFactory factory : updatesFactories) {
                factory.beforeInstall();
            }
        }
    }

    public void afterInstall() throws StatusException {
        AbstractExtraUpdatesFactory[] updatesFactories = updatesFactoryReader.getUpdatesFactories();
        if (updatesFactories != null) {
            for (AbstractExtraUpdatesFactory factory : updatesFactories) {
                factory.afterInstall();
            }
        }
    }

    public void afterUpdate() throws Exception {
        AbstractExtraUpdatesFactory[] updatesFactories = updatesFactoryReader.getUpdatesFactories();
        if (updatesFactories != null) {
            for (AbstractExtraUpdatesFactory factory : updatesFactories) {
                factory.afterUpdate();
            }
        }
    }
}
