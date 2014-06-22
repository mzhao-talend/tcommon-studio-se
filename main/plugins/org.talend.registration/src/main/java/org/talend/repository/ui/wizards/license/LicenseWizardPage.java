// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.ui.wizards.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.i18n.Messages;

/**
 * Page for new project details. <br/>
 * 
 * $Id: LicenseWizardPage.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class LicenseWizardPage extends WizardPage {

    /** CLUF field. */
    private Text clufText;

    /**
     * Constructs a new LicenseWizardPage.
     * 
     * @param server
     * @param password
     * @param author
     */
    public LicenseWizardPage() {
        super("WizardPage"); //$NON-NLS-1$

        setTitle(Messages.getString("LicenseWizard.title")); //$NON-NLS-1$
        setDescription(""); //$NON-NLS-1$
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        ((GridData) parent.getLayoutData()).widthHint = 520;
        ((GridData) parent.getLayoutData()).heightHint = 280;
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);

        Label subTitleLabel = new Label(container, SWT.NONE);
        subTitleLabel.setText(Messages.getString("LicenseWizard.subtitle")); //$NON-NLS-1$

        clufText = new Text(container, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT | SWT.BORDER);
        clufText.setBackground(new Color(null, 255, 255, 255));
        clufText.setEditable(false);
        String license = getLicense();
        clufText.setText(license);

        GridData data = new GridData(GridData.FILL_BOTH);
        clufText.setLayoutData(data);

        // Label footerLabel = new Label(container, SWT.NONE);
        // footerLabel.setText(Messages.getString("LicenseWizard.footer")); //$NON-NLS-1$

        setControl(container);
        setPageComplete(true);
    }

    /**
     * DOC mhirt Comment method "getLicense".
     * 
     * @return
     * @throws PersistenceException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String getLicense() {
        String license = ""; //$NON-NLS-1$
        try {
            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                    IBrandingService.class);
            final URL url = brandingService.getLicenseFile();

            FileReader fileReader = new FileReader(new File(url.getPath()));
            BufferedReader in = new BufferedReader(fileReader);

            String licenseLine = ""; //$NON-NLS-1$
            while ((licenseLine = in.readLine()) != null) {
                license += licenseLine + "\n"; //$NON-NLS-1$
            }

        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        } catch (IOException e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
        return license;
    }
}