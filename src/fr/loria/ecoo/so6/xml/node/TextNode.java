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

import fr.loria.ecoo.so6.xml.xydiff.Hash32;

import java.io.IOException;
import java.io.Writer;


public class TextNode extends AbstractTreeNode {

    private static final long serialVersionUID = 1L;

    protected StringBuilder content;

    public TextNode() {
        this("");
    }

    public TextNode(String content) {
        super(false, false);
        this.content = new StringBuilder(content);
    }

    public void appendContent(String content) {
        this.content.append(content);
    }

    public void setContent(String newContent) {
        this.content = new StringBuilder(newContent);
    }

    public String getContent() {
        return content.toString();
    }

    @Override
    public void exportXML(Writer writer, boolean split)
        throws IOException {
        writer.write(content.toString());

        if (split) {
            writer.write("<!--SPLIT-->");
        }

        writer.flush();
    }

    @Override
    public boolean equalsContent(Object obj) {
        if (obj instanceof TextNode) {
            return ((TextNode) obj).content.toString().equals(content.toString());
        }
        return false;
    }

    @Override
    public double getWeight() {
        if (getContent().length() == 0) {
            // prevent to detect operations on an empty node
            return 0.00001;
        }

        return Math.log(getContent().length() + 1);
    }

    @Override
    public Hash32 getHash32() {
        return new Hash32(getContent());
    }

    @Override
    public String getElementName() {
        return "";
    }
}
