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
package fr.loria.ecoo.so6.xml.node;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import fr.loria.ecoo.so6.xml.xydiff.Hash32;


public interface TreeNode {
    // Tree manipulation
    TreeNode getParent();

    void setParent(TreeNode parent);

    List<TreeNode> getChildren();

    TreeNode getChild(int childPos);

    void insertChild(int pos, TreeNode child);

    boolean removeChild(TreeNode child);

    TreeNode removeChild(int pos);

    void appendChild(TreeNode child);

    int getChildPosition(TreeNode child);

    void computePath();

    Path getPath();

    Path getLastComputedPath();

    TreeNode getNextSibling();

    TreeNode getPreviousSibling();

    TreeNode getFirstChild();

    TreeNode getLastChild();

    Map<String, String> getAttributes();

    String getAttribute(String name);

    void setAttribute(String name, String vlaue);

    void removeAttribute(String name);

    boolean hasAttributes();

    boolean hasChildren();

    // XyDiff requirement
    double getWeight();

    Hash32 getHash32();

    String getId();

    // Xml export
    void exportXML(Writer writer, boolean split)
        throws IOException;

    // XML serialization
    void toBase64(Writer osw) throws IOException;

    // Node content comparison
    boolean equalsContent(Object node);

    // Node content
    boolean allowAttributes();

    boolean allowChildren();

    String getElementName();
}
