/*
 *      Copyright 2016 Jean-Pierre Hotz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.jeanpierrehotz.falseide.ui;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 */
public class FileTreeNode implements MutableTreeNode {

    private String fileName;

    private MutableTreeNode parent;
    private ArrayList<MutableTreeNode> children;

    public FileTreeNode(){
        this.children = new ArrayList<>();
    }

    public FileTreeNode(String name){
        this.children = new ArrayList<>();

        this.fileName = name;
    }

    @Override
    public TreeNode getChildAt(int i) {
        return children.get(i);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode treeNode) {
        if(treeNode instanceof MutableTreeNode) {
            return children.indexOf(treeNode);
        }else{
            return -1;
        }
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    @Override
    public Enumeration children() {
        return null;
    }

    @Override
    public void insert(MutableTreeNode mutableTreeNode, int i) {
        if(mutableTreeNode instanceof FileTreeNode){
            children.add(i, mutableTreeNode);
            mutableTreeNode.setParent(this);
        }
    }

    @Override
    public void remove(int i) {
        children.remove(i);
    }

    @Override
    public void remove(MutableTreeNode mutableTreeNode) {
        children.remove(mutableTreeNode);
    }

    /**
     * @deprecated
     * @param o the object to set the User object of this object to
     */
    @Override
    public void setUserObject(Object o) {
        // this.userObject = o;
    }

    public void setFileName(String name){
        this.fileName = name;
    }

    public String getFileName(){
        return fileName;
    }

    @Override
    public void removeFromParent() {
        parent.remove(this);
    }

    @Override
    public void setParent(MutableTreeNode mutableTreeNode) {
        parent = mutableTreeNode;
    }

    @Override
    public String toString() {
        if(fileName == null) {
            return super.toString();
        }else{
            return fileName;
        }
    }


}
