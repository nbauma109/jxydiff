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
package fr.loria.ecoo.so6.xml.util;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import fr.loria.ecoo.so6.antlr.XMLLexer;
import fr.loria.ecoo.so6.antlr.XMLParser;
import fr.loria.ecoo.so6.xml.exception.AttributeAlreadyExist;
import fr.loria.ecoo.so6.xml.exception.AttributeNotAllowed;
import fr.loria.ecoo.so6.xml.exception.InvalidNodePath;
import fr.loria.ecoo.so6.xml.exception.ParseException;
import fr.loria.ecoo.so6.xml.node.Document;
import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.ProcessingInstructionNode;
import fr.loria.ecoo.so6.xml.node.TextNode;
import fr.loria.ecoo.so6.xml.node.TreeNode;


public class XmlUtil {
    // Path manipulation
    public static String getParentPath(String nodePath) {
        // add for report
        if (nodePath.lastIndexOf(":") == -1) {
            return nodePath;
        }

        //
        return nodePath.substring(0, nodePath.lastIndexOf(":"));
    }

    public static int getChildPosition(String nodePath) {
        return Integer.parseInt(nodePath.substring(nodePath.lastIndexOf(":") + 1));
    }

    public static String getEncoding(String fileName) throws IOException {
        FileReader fr = new FileReader(fileName);
        XMLLexer lexer = new XMLLexer(fr);
        XMLParser parser = new XMLParser(lexer);
        Document doc = new Document();

        // Change set parseProlog()
        try {
            parser.prolog(doc);
        } catch (Exception e) {
            // Default encoding UTF-8
            return "UTF-8";

            //throw new ParseException(e.getMessage());
        } finally {
            fr.close();
        }

        return doc.getEncoding();
    }

    // Load Document (Call the parser)
    public static Document load(String fileName) throws IOException, ParseException {
        String encoding = getEncoding(fileName);
        Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
        try (FileInputStream fis = new FileInputStream(fileName);
             InputStreamReader inr = new InputStreamReader(fis, charset)) {

            XMLLexer lexer = new XMLLexer(inr);
            XMLParser parser = new XMLParser(lexer);
    
            try {
                return parser.document();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ParseException(e.getMessage());
            }
        }
    }

    // Import / Export : Nodes
    public static TreeNode importNode(String base64Object)
        throws ParseException {
        String s = new String(Base64.getDecoder().decode(base64Object), StandardCharsets.UTF_8);

        //System.out.println("---\n" + s + "\n---");
        StringReader reader = new StringReader(s);
        XMLLexer lexer = new XMLLexer(reader);
        XMLParser parser = new XMLParser(lexer);

        try {
            return parser.node();
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
    }

    public static TreeNode clone(TreeNode node) throws Exception {
        return clone(node, true);
    }

    public static TreeNode clone(TreeNode node, boolean withChildren)
        throws Exception {
        StringWriter writer = new StringWriter();
        node.toBase64(writer);
        writer.close();

        TreeNode n = importNode(writer.toString());

        if (!withChildren) {
            n.getChildren().clear();
        }

        return n;
    }

    // Operations
    public static void insertNode(String xmlFileName, String nodePath, TreeNode nodeToInsert)
        throws IOException, InvalidNodePath, ParseException {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(getParentPath(nodePath));
        node.insertChild(getChildPosition(nodePath), nodeToInsert);
        doc.save(xmlFileName, true);
    }

    public static void deleteNode(String xmlFileName, String nodePath)
        throws IOException, InvalidNodePath, ParseException {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(getParentPath(nodePath));
        node.removeChild(getChildPosition(nodePath));
        doc.save(xmlFileName, true);
    }

    public static void setAttribute(String xmlFileName, String nodePath, String attributeName, String attributeValue)
        throws IOException, InvalidNodePath, ParseException, AttributeNotAllowed {
        try {
            setAttribute(xmlFileName, nodePath, attributeName, attributeValue, false);
        } catch (AttributeAlreadyExist e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertAttribute(String xmlFileName, String nodePath, String attributeName, String attributeValue)
        throws IOException, AttributeAlreadyExist, InvalidNodePath, ParseException, AttributeNotAllowed {
        setAttribute(xmlFileName, nodePath, attributeName, attributeValue, true);
    }

    public static void updateAttribute(String xmlFileName, String nodePath, String attributeName, String attributeValue)
        throws IOException, AttributeAlreadyExist, InvalidNodePath, ParseException, AttributeNotAllowed {
        setAttribute(xmlFileName, nodePath, attributeName, attributeValue, false);
    }

    public static void setAttribute(String xmlFileName, String nodePath, String attributeName, String attributeValue, boolean throwExceptionIfExist)
        throws IOException, AttributeAlreadyExist, InvalidNodePath, ParseException, AttributeNotAllowed {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(nodePath);

        if (!node.allowAttributes()) {
            throw new AttributeNotAllowed(node.toString());
        }
        if (throwExceptionIfExist && ((node.getAttribute(attributeName)) != null)) {
            throw new AttributeAlreadyExist(attributeName);
        }

        node.setAttribute(attributeName, attributeValue);

        doc.save(xmlFileName, true);
    }

    public static void deleteAttribute(String xmlFileName, String nodePath, String attributeName)
        throws IOException, InvalidNodePath, ParseException, AttributeNotAllowed {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(nodePath);

        if (!node.allowAttributes()) {
            throw new AttributeNotAllowed(node.toString());
        }
        node.removeAttribute(attributeName);

        doc.save(xmlFileName, true);
    }

    public static void updateElementName(String xmlFileName, String nodePath, String newElementName)
        throws IOException, InvalidNodePath, ParseException {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(nodePath);
        ((ElementNode) node).setElementName(newElementName);
        doc.save(xmlFileName, true);
    }

    public static void updateTextNode(String xmlFileName, String nodePath, String newContent)
        throws IOException, InvalidNodePath, ParseException {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(nodePath);
        ((TextNode) node).setContent(newContent);
        doc.save(xmlFileName, true);
    }

    public static void updateProcessingInstructionNode(String xmlFileName, String nodePath, String target, String newContent)
        throws IOException, ParseException, InvalidNodePath {
        Document doc = load(xmlFileName);
        TreeNode node = doc.getNode(nodePath);
        ((ProcessingInstructionNode) node).setTarget(target);
        ((ProcessingInstructionNode) node).setContent(newContent);
        doc.save(xmlFileName, true);
    }

    public static void updateCommentNode(String xmlFileName, String nodePath, String newContent)
        throws IOException, InvalidNodePath, ParseException {
        updateTextNode(xmlFileName, nodePath, newContent);
    }

    public static void updateCDateNode(String xmlFileName, String nodePath, String newContent)
        throws IOException, InvalidNodePath, ParseException {
        updateTextNode(xmlFileName, nodePath, newContent);
    }

    public static void updateDocTypeNode(String xmlFileName, String nodePath, String newContent)
        throws IOException, InvalidNodePath, ParseException {
        updateTextNode(xmlFileName, nodePath, newContent);
    }
}
