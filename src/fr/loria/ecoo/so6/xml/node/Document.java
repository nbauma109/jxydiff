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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class Document extends AbstractTreeNode {

    private static final long serialVersionUID = 1L;

    private static final String VERSION = "version";
    private static final String ENCODING = "encoding";

    public Document() {
        this("1.0", null);
    }

    public Document(String version, String encoding) {
        super(true, true);

        if (encoding != null) {
            this.setAttribute(ENCODING, encoding);
        }

        if (version != null) {
            this.setAttribute(VERSION, version);
        }
    }

    public String getVersion() {
        return attributes.get(VERSION);
    }

    public String getEncoding() {
        return attributes.get(ENCODING);
    }

    public void setEncoding(String encoding) {
        attributes.put(ENCODING, encoding);
    }

    public String getStandalone() {
        return attributes.get("standalone");
    }

    public void setStandalone(String standalone) {
        attributes.put("standalone", standalone);
    }

    public void setVersion(String version) {
        attributes.put(VERSION, version);
    }

    public void save(String fileName) throws IOException {
        this.save(fileName, false);
    }

    public void save(String fileName, boolean split) throws IOException {
        String encoding = getEncoding();
        Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter writer = new OutputStreamWriter(fos, charset)) {
            exportXML(writer, split);
            writer.flush();
        }
    }

    @Override
    public void exportXML(Writer writer, boolean split)
        throws IOException {
        // write header
        writer.write("<?xml version=\"");
        writer.write(getVersion());
        writer.write("\"");

        if (getEncoding() != null) {
            writer.write(" encoding=\"");
            writer.write(getEncoding());
            writer.write("\"");
        }

        if (getStandalone() != null) {
            writer.write(" standalone=\"");
            writer.write(getStandalone());
            writer.write("\"");
        }

        writer.write("?>");

        // write children
        for (TreeNode node : children) {
            node.exportXML(writer, split);
        }

        writer.flush();
    }

    @Override
    public Hash32 getHash32() {
        String s = getVersion() + "|" + getEncoding() + "|" + getStandalone();

        return new Hash32(s);
    }

    @Override
    public String getElementName() {
        return "";
    }
}
