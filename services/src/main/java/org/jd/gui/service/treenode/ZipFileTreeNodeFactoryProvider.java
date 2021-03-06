/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.gui.service.treenode;

import org.jd.gui.api.API;
import org.jd.gui.api.feature.ContainerEntryGettable;
import org.jd.gui.api.feature.UriGettable;
import org.jd.gui.api.model.Container;
import org.jd.gui.spi.TreeNodeFactory;
import org.jd.gui.view.data.TreeNodeBean;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ZipFileTreeNodeFactoryProvider extends DirectoryTreeNodeFactoryProvider {
	protected static final ImageIcon ICON = new ImageIcon(ZipFileTreeNodeFactoryProvider.class.getClassLoader().getResource("org/jd/gui/images/zip_obj.png"));

    @Override public String[] getSelectors() { return appendSelectors("*:file:*.zip"); }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DefaultMutableTreeNode & ContainerEntryGettable & UriGettable> T make(API api, Container.Entry entry) {
        int lastSlashIndex = entry.getPath().lastIndexOf("/");
        String name = entry.getPath().substring(lastSlashIndex+1);
        T node = (T)new TreeNode(entry, "generic", new TreeNodeBean(name, ICON));
        // Add dummy node
        node.add(new DefaultMutableTreeNode());
        return node;
	}

    protected static class TreeNode extends DirectoryTreeNodeFactoryProvider.TreeNode {
        protected String ct;

        public TreeNode(Container.Entry entry, String containerType, Object userObject) {
            super(entry, userObject);
            ct = containerType;
        }

        // --- TreeNodeExpandable --- //
        public void populateTreeNode(API api) {
            if (!initialized) {
                removeAllChildren();

                for (Container.Entry e : getChildren()) {
                    TreeNodeFactory factory = api.getTreeNodeFactory(e);
                    if (factory != null) {
                        add(factory.make(api, e));
                    }
                }

                initialized = true;
            }
        }
    }
}
