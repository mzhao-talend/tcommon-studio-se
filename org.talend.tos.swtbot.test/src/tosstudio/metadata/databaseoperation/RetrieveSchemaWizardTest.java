// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package tosstudio.metadata.databaseoperation;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.matchers.WidgetOfType;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.talend.swtbot.TalendSwtBotForTos;
import org.talend.swtbot.Utilities;

/**
 * DOC fzhong class global comment. Detailled comment
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class RetrieveSchemaWizardTest extends TalendSwtBotForTos {

    private SWTBotView view;

    private SWTBotTree tree;

    private SWTBotTreeItem treeNode;

    private static final String DBNAME = "mysql";

    private static final String TABLENAME = "autotest";

    @Before
    public void createDBConnection() {
        view = Utilities.getRepositoryView(gefBot);
        tree = new SWTBotTree((Tree) gefBot.widget(WidgetOfType.widgetOfType(Tree.class), view.getWidget()));
        treeNode = Utilities.getTalendItemNode(tree, Utilities.TalendItemType.DB_CONNECTIONS);
        Utilities.createDbConnection(gefBot, treeNode, Utilities.DbConnectionType.MYSQL, DBNAME);
        String sql = "create table " + TABLENAME + "(id int, name varchar(20))";
        Utilities.executeSQL(gefBot, treeNode.getNode(DBNAME + " 0.1"), sql);
    }

    @Test
    public void retrieveSchema() {
        int rowCount = 0;
        Utilities.retrieveDbSchema(gefBot, treeNode, DBNAME, TABLENAME);

        rowCount = treeNode.expandNode(DBNAME + " 0.1").getNode("Table schemas").rowCount();
        Assert.assertEquals("schemas be retrieved even cancel the wizard of retrieving schema", 0, rowCount);
    }

    @After
    public void removePreviouslyCreateItems() {
        String sql = "drop table " + TABLENAME;
        Utilities.executeSQL(gefBot, treeNode.expandNode(DBNAME + " 0.1"), sql);
        Utilities.cleanUpRepository(treeNode);
        Utilities.emptyRecycleBin(gefBot, tree);
    }
}