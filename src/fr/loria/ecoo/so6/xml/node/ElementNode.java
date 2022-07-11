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
import java.util.Map.Entry;

import fr.loria.ecoo.so6.xml.xydiff.Hash32;


public class ElementNode extends AbstractTreeNode {

    private static final long serialVersionUID = 1L;

    private String elementName;

    public ElementNode(String elementName) {
        super(true, true);
        this.elementName = elementName;
    }

    public void setElementName(String name) {
        this.elementName = name;
    }

    @Override
    public String getElementName() {
        return this.elementName;
    }

    @Override
    public void exportXML(Writer writer, boolean split)
        throws IOException {
        // write elements
        writer.write("<");
        writer.write(elementName);

        // write attributes
        for (Entry<String, String> e : attributes.entrySet()) {
            String key = e.getKey();
            writer.write(" ");
            writer.write(key);

            String attrValue = e.getValue();

            if (attrValue.indexOf("\"") == -1) {
                writer.write("=\"");
                writer.write(attrValue);
                writer.write("\"");
            } else {
                writer.write("='");
                writer.write(attrValue);
                writer.write("'");
            }
        }

        if (children.isEmpty()) {
            // close element
            writer.write("/>");
        } else {
            writer.write(">");

            // write children
            for (TreeNode node : children) {
                node.exportXML(writer, split);
            }

            // close elements
            writer.write("</");
            writer.write(elementName);
            writer.write(">");
        }

        writer.flush();
    }

    @Override
    public boolean equalsContent(Object obj) {
        if (obj instanceof ElementNode) {
            ElementNode elementObj = (ElementNode) obj;

            // check element name
            // check attributes
            if (!elementObj.elementName.equals(elementName) || (elementObj.attributes.size() != attributes.size())) {
                return false;
            }

            for (Entry<String, String> e : attributes.entrySet()) {
                String key = e.getKey();

                if (!elementObj.attributes.get(key).equals(e.getValue())) {
                    return false;
                }
            }

            // check children
            return super.equalsContent(obj);
        }
        return false;
    }

    @Override
    public Hash32 getHash32() {
        String s = getElementName();

        return new Hash32(s);
    }
}
