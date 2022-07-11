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
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.loria.ecoo.so6.xml.exception.InvalidNodePath;

public abstract class AbstractTreeNode implements TreeNode, Serializable {

    private static final long serialVersionUID = 1L;

    protected TreeNode parent;
    protected boolean allowAttributes;
    protected boolean allowChildren;
    protected Path lastComputedPath;
    protected List<TreeNode> children;
    protected Map<String, String> attributes;

    protected AbstractTreeNode(boolean allowChildren, boolean allowAttributes) {
        children = new ArrayList<>();
        this.allowChildren = allowChildren;
        this.allowAttributes = allowAttributes;
        this.attributes = new Hashtable<>();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public TreeNode getChild(int childPos) {
        if (children.isEmpty()) {
            return null;
        }
        return children.get(childPos);
    }

    @Override
    public void insertChild(int pos, TreeNode child) {
        if (!allowChildren) {
            throw new RuntimeException("Can't insert child to this node");
        }

        child.setParent(this);
        children.add(pos, child);
    }

    @Override
    public boolean removeChild(TreeNode child) {
    	TreeNode prev = child.getPreviousSibling();
    	TreeNode next = child.getNextSibling();
    	
        child.setParent(null);

        boolean result = children.remove(child);
        
        if (prev != null && next != null
        		&& isReallyTextNode(prev)
        		&& isReallyTextNode(next)) {
        	String joinedContent = ((TextNode)prev).getContent() + ((TextNode)next).getContent();
        	int firstTextNodePos = this.getChildPosition(prev);
        	
        	children.remove(prev);
        	children.remove(next);
        	
        	TextNode joinedNode = new TextNode(joinedContent);
        	this.insertChild(firstTextNodePos, joinedNode);
        }
        
        return result;
    }
    
    private static boolean isReallyTextNode(TreeNode node) {
    	return node instanceof TextNode &&
    		(!(node instanceof CommentNode) && !(node instanceof CDataNode) && !(node instanceof ProcessingInstructionNode) && !(node instanceof DocTypeNode));
    }

    @Override
    public TreeNode removeChild(int pos) {
    	TreeNode child = this.getChild(pos);
    	
    	this.removeChild(child);
    	
    	return child;
    }

    @Override
    public void appendChild(TreeNode child) {
        if (!allowChildren) {
            throw new RuntimeException("Can't insert child to this node");
        }

        child.setParent(this);
        children.add(child);
    }

    @Override
    public int getChildPosition(TreeNode child) {
        return children.indexOf(child);
    }

    @Override
    public double getWeight() {
        double weight = 1.0;

        for (Iterator<TreeNode> i = getChildren().iterator(); i.hasNext();) {
            weight += i.next().getWeight();
        }

        //System.out.println("weight " + weight + " toString " +
        // this.toString());
        return weight;
    }

    @Override
    public String getId() {
        return getHash32().toHexString();
    }

    @Override
    public Path getPath() {        
        List<PathElement> pathElements = new ArrayList<>();
        if (getParent() == null) {
            pathElements.add(new PathElement(0, getElementName()));
        } else {
            pathElements.addAll(getParent().getPath().getPathElements());
            pathElements.add(new PathElement(getParent().getChildPosition(this), getElementName()));
        }
        return new Path(pathElements);
    }
    
    @Override
    public void computePath() {
        lastComputedPath = getPath();

        for (Iterator<TreeNode> i = children.iterator(); i.hasNext();) {
            i.next().computePath();
        }
    }

    @Override
    public Path getLastComputedPath() {
        return lastComputedPath;
    }

    @Override
    public void toBase64(Writer osw) throws IOException {
        StringWriter writer = new StringWriter();
        exportXML(writer, false);
        writer.close();
        osw.write(Base64.getEncoder().encodeToString(writer.toString().getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public boolean equalsContent(Object obj) {
        if (obj instanceof TreeNode) {
            TreeNode objCompare = (TreeNode) obj;
            int size = getChildren().size();

            if (objCompare.getChildren().size() != size) {
                return false;
            }

            for (int i = 0; i < size; i++) {
                if (!objCompare.getChild(i).equalsContent(getChild(i))) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public TreeNode getNode(String nodePath) throws InvalidNodePath {
        TreeNode root = this;
        int[] path = convertPath(nodePath);

        while (root.getParent() != null) {
            root = root.getParent();
        }

        TreeNode node = root;

        try {
            for (int i = 1; i < path.length; i++) {
                node = node.getChild(path[i]);
            }
        } catch (RuntimeException e) {
            throw new InvalidNodePath(e.getMessage());
        }

        return node;
    }

    @Override
    public TreeNode getNextSibling() {
        int childPos = this.getParent().getChildPosition(this);

        return this.getParent().getChild(childPos + 1);
    }

    @Override
    public TreeNode getPreviousSibling() {
        return this.getParent().getChild(this.getParent().getChildPosition(this) - 1);
    }

    // Node path manipulation
    public static int[] convertPath(String path) {
        String[] split = path.split(":");
        int[] result = new int[split.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(split[i]);
        }

        return result;
    }

    @Override
    public boolean hasChildren() {
        if (this.getChildren().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public TreeNode getFirstChild() {
        return this.getChild(0);
    }

    @Override
    public TreeNode getLastChild() {
        List<TreeNode> list = this.getChildren();
        int nbChildren = list.size();

        return this.getChild(nbChildren - 1);
    }

    @Override
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override
    public boolean hasAttributes() {
        if (this.attributes.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean allowAttributes() {
        return allowAttributes;
    }

    @Override
    public boolean allowChildren() {
        return allowChildren;
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();

        try {
            exportXML(writer, false);
            writer.close();

            return writer.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "error";
    }
}
