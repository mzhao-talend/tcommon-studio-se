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
package org.talend.librariesmanager.ui.views;

import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.core.model.general.ModuleNeeded;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: StatusImageProvider.java 1754 2007-02-01 09:46:26Z plegall $
 * 
 */
public class StatusImageProvider implements IColumnImageProvider {

    public Image getImage(Object bean) {
        ModuleNeeded componentImportNeeds = (ModuleNeeded) bean;
        switch (componentImportNeeds.getStatus()) {
        case INSTALLED:
            return ImageProvider.getImage(ECoreImage.MODULE_INSTALLED_ICON);
        case NOT_INSTALLED:
            if (componentImportNeeds.isRequired()) {
                return ImageProvider.getImage(ECoreImage.MODULE_ERROR_ICON);
            } else {
                return ImageProvider.getImage(ECoreImage.MODULE_WARNING_ICON);
            }
        case UNUSED:
            return ImageProvider.getImage(ECoreImage.MODULE_UNKNOWN_ICON);
        default:
            return ImageProvider.getImage(ECoreImage.MODULE_UNKNOWN_ICON);
        }
    }

}
