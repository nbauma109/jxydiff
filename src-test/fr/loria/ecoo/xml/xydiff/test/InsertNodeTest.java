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
package fr.loria.ecoo.xml.xydiff.test;

import fr.loria.ecoo.so6.xml.node.CDataNode;
import fr.loria.ecoo.so6.xml.node.CommentNode;
import fr.loria.ecoo.so6.xml.node.DocTypeNode;
import fr.loria.ecoo.so6.xml.node.Document;
import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.ProcessingInstructionNode;
import fr.loria.ecoo.so6.xml.node.TextNode;
import fr.loria.ecoo.so6.xml.util.SmallFileUtils;
import fr.loria.ecoo.so6.xml.xydiff.XyDiff;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;


public class InsertNodeTest extends TestCase {

    private static final String ROOT_PATH = "/root";
    private static final String ROOT_A = "/root/a";
    private static final String XML_VERSION_1_0 = "<?xml version=\"1.0\"?>";
    private static final String ROOT = "<root/>";
    private static final String XML_COMMAND = "XML Command";
    private static final String ROOT_A_ROOT = "<root><a/></root>";
    private static final String PATH_0_0_0 = "0:0:0";
    private static final String INSERTED = "Inserted";
    private static final String DELTA = "delta";

    private String tempDirPath;
    private String temp1;
    private String temp2;

    public InsertNodeTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        File temp = SmallFileUtils.createTmpDir();
        tempDirPath = temp.getAbsolutePath();
        temp1 = tempDirPath + File.separator + "test1.xml";
        temp2 = tempDirPath + File.separator + "test2.xml";
    }

    public void testInsertNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + ROOT_A_ROOT;
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_A);

        ElementNode toAppend = new ElementNode("a");
        i.appendChild(toAppend);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertNode2() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a test=\"val\"/></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a test=\"val\"/><a/></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", "0:0:1");
        i.setAttribute("path", ROOT_A);

        ElementNode a = new ElementNode("a");
        i.appendChild(a);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        System.out.println(s1);
        System.out.println(s2);

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testDeleteNodeInsertNode() throws Exception {
        try (//Test the node path
        FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT_A_ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><b/></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode d = new ElementNode("Deleted");
        d.setAttribute("pos", PATH_0_0_0);
        d.setAttribute("path", ROOT_A);

        ElementNode a = new ElementNode("a");
        d.appendChild(a);
        root.appendChild(d);

        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", "/root/b");
        
        ElementNode b = new ElementNode("b");
        i.appendChild(b);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertSubtree() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a><b/><c/><d><e/></d></a></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_A);

        ElementNode a = new ElementNode("a");

        // Construct subtree
        ElementNode b = new ElementNode("b");
        ElementNode c = new ElementNode("c");
        ElementNode d = new ElementNode("d");
        ElementNode e = new ElementNode("e");
        d.appendChild(e);
        a.appendChild(b);
        a.appendChild(c);
        a.appendChild(d);
        i.appendChild(a);

        //
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertProcessingInstruction() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><?test1 test2?></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_PATH);
        
        ProcessingInstructionNode toAppend = new ProcessingInstructionNode("test1 test2");
        i.appendChild(toAppend);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        System.out.println(s1);
        System.out.println(s2);

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertDocumentType() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String dt = "<!DOCTYPE doc SYSTEM \"test.dtd\">";
            String c2 = XML_VERSION_1_0 + dt + ROOT;
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", "0:0");
        i.setAttribute("path", "");
        
        DocTypeNode dtn = new DocTypeNode(" doc SYSTEM \"test.dtd\"");
        i.appendChild(dtn);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        System.out.println(s1);
        System.out.println(s2);

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertCommentNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><!-- comment --></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_PATH);
        
        CommentNode c = new CommentNode(" comment ");
        i.appendChild(c);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertTextNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root>&test;</root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_PATH);
        
        TextNode text = new TextNode("&test;");
        i.appendChild(text);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertCDATANode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><![CDATA[test insert cdata section in the tree]]></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_PATH);
        
        CDataNode cdata = new CDataNode("test insert cdata section in the tree");
        i.appendChild(cdata);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testUpdateNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root>abcdef</root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><!--abcdef--></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode d = new ElementNode("Deleted");
        TextNode t = new TextNode("abcdef");
        d.appendChild(t);
        d.setAttribute("pos", PATH_0_0_0);
        d.setAttribute("path", ROOT_PATH);
        d.setAttribute("update", "yes");

        ElementNode i = new ElementNode(INSERTED);
        CommentNode comment = new CommentNode("abcdef");
        i.appendChild(comment);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_PATH);
        i.setAttribute("update", "yes");
        root.appendChild(d);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = ref.toString();
        String s2 = delta.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertEntityNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + ROOT_A_ROOT;
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a/>&e;</root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", "0:0:1");
        i.setAttribute("path", ROOT_PATH);

        TextNode ent = new TextNode("&e;");
        i.appendChild(ent);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    @Override
    protected void tearDown() throws Exception {
        System.out.println(tempDirPath);
    }
}
