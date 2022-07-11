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

import fr.loria.ecoo.so6.xml.node.Document;
import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.TextNode;
import fr.loria.ecoo.so6.xml.util.SmallFileUtils;
import fr.loria.ecoo.so6.xml.xydiff.XyDiff;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;


public class MoveNodeTest extends TestCase {

    private static final String ROOT_A_B = "/root/a/b";
    private static final String ROOT_B = "/root/b";
    private static final String ROOT_D = "/root/d";
    private static final String ROOT_D_B = "/root/d/b";
    private static final String PATH_0_0_0 = "0:0:0";
    private static final String PATH_0_0_0_0 = "0:0:0:0";
    private static final String PATH_0_0_1 = "0:0:1";
    private static final String PATH_0_0_1_0 = "0:0:1:0";
    private static final String PATH_0_0_1_1 = "0:0:1:1";
    private static final String XML_VERSION_1_0 = "<?xml version=\"1.0\"?>";
    private static final String XML_COMMAND = "XML Command";
    private static final String DELTA = "delta";
    private static final String INSERTED = "Inserted";
    private static final String DELETED = "Deleted";

    // test move node and subtree
    private String tempDirPath;
    private String temp1;
    private String temp2;

    public MoveNodeTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        File temp = SmallFileUtils.createTmpDir();
        tempDirPath = temp.getAbsolutePath();
        temp1 = tempDirPath + File.separator + "test1.xml";
        temp2 = tempDirPath + File.separator + "test2.xml";
    }

    public void testInsertNodeAndMoveNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><b/><c/></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a><c/></a><d><b/></d></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_1);
        i.setAttribute("path", ROOT_D);

        ElementNode d = new ElementNode("d");
        i.appendChild(d);

        ElementNode b = new ElementNode("b");
        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_0_0);
        md.setAttribute("path", ROOT_A_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_1_0);
        mi.setAttribute("path", ROOT_D_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(i);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertSubtreeAndMoveNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><b/><c/></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a><c/></a><d><e/><b/></d></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_1);
        i.setAttribute("path", ROOT_D);

        ElementNode d = new ElementNode("d");
        ElementNode e = new ElementNode("e");
        d.appendChild(e);
        i.appendChild(d);

        ElementNode b = new ElementNode("b");

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_0_0);
        md.setAttribute("path", ROOT_A_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_1_1);
        mi.setAttribute("path", ROOT_D_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(i);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertNodeAndMoveSubtree() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><b>text</b><c/></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a><c/></a><d><b>text</b></d></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_1);
        i.setAttribute("path", ROOT_D);

        ElementNode d = new ElementNode("d");
        i.appendChild(d);

        ElementNode b = new ElementNode("b");
        TextNode t = new TextNode("text");
        b.appendChild(t);

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_0_0);
        md.setAttribute("path", ROOT_A_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_1_0);
        mi.setAttribute("path", ROOT_D_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(i);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertSubtreeAndMoveSubtree() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><b>text<f/></b><c/></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a><c/></a><d><e/><b>text<f/></b></d></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_1);
        i.setAttribute("path", ROOT_D);

        ElementNode d = new ElementNode("d");
        ElementNode e = new ElementNode("e");
        d.appendChild(e);
        i.appendChild(d);

        ElementNode b = new ElementNode("b");
        ElementNode f = new ElementNode("f");
        TextNode t = new TextNode("text");
        b.appendChild(t);
        b.appendChild(f);

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_0_0);
        md.setAttribute("path", ROOT_A_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_1_1);
        mi.setAttribute("path", ROOT_D_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(i);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s2, s1);
    }

    public void testWeakMoveNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><c/></a><b/></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><b/><a><c/></a></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);

        ElementNode b = new ElementNode("b");
        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_1);
        md.setAttribute("path", ROOT_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_0);
        mi.setAttribute("path", ROOT_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testWeakMoveNodeWithTheSameTagNameAndDifferentAttribute()
        throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><b attr=\"val\"><d/></b><b attr=\"val\"/></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><a><b attr=\"val\"/><b attr=\"val\"><d/></b></a></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);

        ElementNode b = new ElementNode("b");
        b.setAttribute("attr", "val");

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", "0:0:0:1");
        md.setAttribute("path", ROOT_A_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_0_0);
        mi.setAttribute("path", ROOT_A_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(mi);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        System.out.println(s1);
        System.out.println(s2);

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testWeakMoveSubtreeWithTheSameWeight()
        throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><c/></a><b><d/></b></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><b><d/></b><a><c/></a></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);

        ElementNode a = new ElementNode("a");
        ElementNode c = new ElementNode("c");
        a.appendChild(c);

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_0);
        md.setAttribute("path", "/root/a");
        md.appendChild(a);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_1);
        mi.setAttribute("path", "/root/a");
        mi.appendChild(a);

        root.appendChild(md);
        root.appendChild(mi);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testMoveSubtreeWithDifferentWeight() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><c><d/></c></a><b><e/></b></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><b><e/></b><a><c><d/></c></a></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);

        ElementNode b = new ElementNode("b");
        ElementNode e = new ElementNode("e");
        b.appendChild(e);

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", PATH_0_0_1);
        md.setAttribute("path", ROOT_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_0);
        mi.setAttribute("path", ROOT_B);
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testInsertNodeAndMoveNodeWithTheSameTagName()
        throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><c/><b/></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<root><b><b/></b><a><c/></a></root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(INSERTED);
        i.setAttribute("pos", PATH_0_0_0);
        i.setAttribute("path", ROOT_B);

        ElementNode b = new ElementNode("b");
        i.appendChild(b);

        ElementNode md = new ElementNode(DELETED);
        md.setAttribute("move", "yes");
        md.setAttribute("pos", "0:0:0:1");
        md.setAttribute("path", ROOT_A_B);
        md.appendChild(b);

        ElementNode mi = new ElementNode(INSERTED);
        mi.setAttribute("move", "yes");
        mi.setAttribute("pos", PATH_0_0_0_0);
        mi.setAttribute("path", "/root/b/b");
        mi.appendChild(b);

        root.appendChild(md);
        root.appendChild(i);
        root.appendChild(mi);

        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        System.out.println(s1);

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testDoubleWeakMove() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<a><b/><c><d/><e/><f/></c></a>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<a><b/><c><f/><e/><d/></c></a>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);

        ElementNode md1 = new ElementNode(DELETED);
        md1.setAttribute("move", "yes");
        md1.setAttribute("pos", PATH_0_0_1_1);
        md1.setAttribute("path", "/a/c/e");

        ElementNode e = new ElementNode("e");
        md1.appendChild(e);

        ElementNode md2 = new ElementNode(DELETED);
        md2.setAttribute("move", "yes");
        md2.setAttribute("pos", PATH_0_0_1_0);
        md2.setAttribute("path", "/a/c/d");

        ElementNode d = new ElementNode("d");
        md2.appendChild(d);

        ElementNode mi1 = new ElementNode(INSERTED);
        mi1.setAttribute("move", "yes");
        mi1.setAttribute("pos", PATH_0_0_1_1);
        mi1.setAttribute("path", "/a/c/e");
        mi1.appendChild(e);

        ElementNode mi2 = new ElementNode(INSERTED);
        mi2.setAttribute("move", "yes");
        mi2.setAttribute("pos", "0:0:1:2");
        mi2.setAttribute("path", "/a/c/d");
        mi2.appendChild(d);

        root.appendChild(md1);
        root.appendChild(md2);
        root.appendChild(mi1);
        root.appendChild(mi2);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testMoveNodeAndUpdateAttribute() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<a><b/><c><d attr=\"val\"/></c></a>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<a><b/><c></c><e><d attr=\"val1\"/></e></a>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        String s1 = delta.toString();
        String test = "<?xml version=\"1.0\"?><delta><Deleted move=\"yes\" path=\"/a/c/d\" pos=\"0:0:1:0\"><d attr=\"val\"/></Deleted><Inserted path=\"/a/e\" pos=\"0:0:2\"><e/></Inserted><Inserted move=\"yes\" path=\"/a/e/d\" pos=\"0:0:2:0\"><d attr=\"val1\"/></Inserted><AttributeUpdated nv=\"val1\" name=\"attr\" ov=\"val\" path=\"/a/e/d/@attr\" pos=\"0:0:2:0\"/></delta>";

        assertEquals(XML_COMMAND, test, s1);
    }

    public void testMoveNodeAndInsertAttribute() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<a><b/><c><d/></c></a>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<a><b/><c></c><e><d attr=\"val1\"/></e></a>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        String s1 = delta.toString();
        String test = "<?xml version=\"1.0\"?><delta><Deleted move=\"yes\" path=\"/a/c/d\" pos=\"0:0:1:0\"><d/></Deleted><Inserted path=\"/a/e\" pos=\"0:0:2\"><e/></Inserted><Inserted move=\"yes\" path=\"/a/e/d\" pos=\"0:0:2:0\"><d attr=\"val1\"/></Inserted><AttributeInserted name=\"attr\" value=\"val1\" path=\"/a/e/d/@attr\" pos=\"0:0:2:0\"/></delta>";

        assertEquals(XML_COMMAND, test, s1);
    }

    public void testMoveNodeAndDeleteAttribute() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<a><b/><c><d attr=\"val\"/></c></a>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + "<a><b/><c></c><e><d/></e></a>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        String s1 = delta.toString();
        String test = "<?xml version=\"1.0\"?><delta><Deleted move=\"yes\" path=\"/a/c/d\" pos=\"0:0:1:0\"><d attr=\"val\"/></Deleted><Inserted path=\"/a/e\" pos=\"0:0:2\"><e/></Inserted><Inserted move=\"yes\" path=\"/a/e/d\" pos=\"0:0:2:0\"><d/></Inserted><AttributeDeleted name=\"attr\" path=\"/a/e/d/@attr\" pos=\"0:0:2:0\"/></delta>";

        assertEquals(XML_COMMAND, test, s1);
    }

    @Override
    protected void tearDown() throws Exception {
        System.out.println(tempDirPath);
    }
}
