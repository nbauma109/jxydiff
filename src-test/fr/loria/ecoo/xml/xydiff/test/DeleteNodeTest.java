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

import fr.loria.ecoo.so6.xml.node.CommentNode;
import fr.loria.ecoo.so6.xml.node.Document;
import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.TextNode;
import fr.loria.ecoo.so6.xml.util.SmallFileUtils;
import fr.loria.ecoo.so6.xml.xydiff.XyDiff;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;


public class DeleteNodeTest extends TestCase {

    private static final String DELETED = "Deleted";
    private static final String XML_VERSION_1_0 = "<?xml version=\"1.0\"?>";
    private static final String ROOT = "<root/>";
    private static final String XML_COMMAND = "XML Command";
    private static final String DELTA = "delta";

    private String tempDirPath;
    private String temp1;
    private String temp2;

    public DeleteNodeTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        File temp = SmallFileUtils.createTmpDir();
        tempDirPath = temp.getAbsolutePath();
        temp1 = tempDirPath + File.separator + "test1.xml";
        temp2 = tempDirPath + File.separator + "test2.xml";
    }

    public void testDeleteNode() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a/></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + ROOT;
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode d = new ElementNode(DELETED);
        d.setAttribute("pos", "0:0:0");
        d.setAttribute("path", "/root/a");

        ElementNode toAppend = new ElementNode("a");
        d.appendChild(toAppend);
        root.appendChild(d);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        System.out.println("delta " + s1);
        System.out.println("ref   " + s2);

        assertEquals(XML_COMMAND, s1, s2);
    }

    public void testDeleteSubtree() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "<root><a><b/><c/><d><e/></d></a></root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + ROOT;
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(DELETED);
        i.setAttribute("pos", "0:0:0");
        i.setAttribute("path", "/root/a");

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

    public void testDeleteTextNodeChildOfDocumentRoot()
        throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = XML_VERSION_1_0 + "\n<root/>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = XML_VERSION_1_0 + ROOT;
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode(DELTA);
        ElementNode i = new ElementNode(DELETED);
        i.setAttribute("pos", "0:0");
        i.setAttribute("path", "");

        TextNode t = new TextNode("\n");
        i.appendChild(t);
        root.appendChild(i);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals(XML_COMMAND, s1, s2);
    }
    
    public void testTextNodesShouldBeTogether() throws Exception {
		Document doc = new Document();
		TextNode t1 = new TextNode("t1");
		TextNode t2 = new TextNode("t2");
		CommentNode c1 = new CommentNode("comment");
		doc.appendChild(t1);
		doc.appendChild(c1);
		doc.appendChild(t2);
		
		assertEquals("<?xml version=\"1.0\"?>t1<!--comment-->t2", doc.toString());
		
		doc.removeChild(c1);
		
		assertEquals(1, doc.getChildren().size());
	}

    @Override
    protected void tearDown() throws Exception {
        System.out.println(tempDirPath);
    }
}
