/**
 * JXyDiff: An XML Diff Written in Java
 *
 * Contact: pascal.molli@loria.fr
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of QPL/CeCill
 *
 * See licences details in QPL.txt and CeCill.txt
 *
 * Initial developer: Raphael Tani
 * Initial Developer: Gregory Cobena
 * Initial Developer: Gerald Oster
 * Initial Developer: Pascal Molli
 * Initial Developer: Serge Abiteboul
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package fr.loria.ecoo.so6.xml.xydiff;

import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.Path;
import fr.loria.ecoo.so6.xml.node.TreeNode;


public class DeleteNode extends XMLCommand {
    private TreeNode node;
    private boolean isMoved;
    private boolean isUpdated;

    public DeleteNode(Path path, TreeNode node) {
        super(path);
        this.node = node;
        this.type = XMLCommand.DELETE_NODE;
        this.isMoved = false;
        this.isUpdated = false;
    }

    public void setIsMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    public boolean getIsMoved() {
        return this.isMoved;
    }

    public void setIsUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean getIsUpdated() {
        return this.isUpdated;
    }

    public TreeNode getNode() {
        return node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("DeleteNode: ").append(node.toString()).append(" path ").append(this.nodePath);

        if (isMoved) {
            s.append(" (move)");
        }

        if (this.isUpdated) {
            s.append(" (update)");
        }

        return s.toString();
    }

    @Override
    public ElementNode toXML() {
        try {
            ElementNode d = new ElementNode("Deleted");
            d.setAttribute("pos", this.nodePath.getNumericPath());
            d.setAttribute("path", this.nodePath.getPseudoXPath());

            if (this.isMoved) {
                d.setAttribute("move", "yes");
            }

            if (this.isUpdated) {
                d.setAttribute("update", "yes");
            }

            d.appendChild(node);

            return d;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
