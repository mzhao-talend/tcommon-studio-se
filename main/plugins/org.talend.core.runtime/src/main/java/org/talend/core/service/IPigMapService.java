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
package org.talend.core.service;

import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.talend.commons.ui.expressionbuilder.IExpressionDataBean;
import org.talend.core.IService;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public interface IPigMapService extends IService {

    public IContentProposalProvider createExpressionProposalProvider(IExpressionDataBean dataBean);

    public void setPigMapData(AbstractExternalData externalData);
}
