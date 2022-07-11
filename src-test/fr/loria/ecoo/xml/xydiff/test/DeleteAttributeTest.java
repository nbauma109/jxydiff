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
import fr.loria.ecoo.so6.xml.util.SmallFileUtils;
import fr.loria.ecoo.so6.xml.xydiff.XyDiff;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileWriter;


public class DeleteAttributeTest extends TestCase {
    private String tempDirPath;
    private String temp1;
    private String temp2;

    public DeleteAttributeTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        File temp = SmallFileUtils.createTmpDir();
        tempDirPath = temp.getAbsolutePath();
        temp1 = tempDirPath + File.separator + "test1.xml";
        temp2 = tempDirPath + File.separator + "test2.xml";
    }

    public void testDeleteAttribute() throws Exception {
        try (FileWriter writer1 = new FileWriter(temp1)) {
            String c1 = "<?xml version=\"1.0\"?>" + "<root><a test=\"val\"/>text</root>";
            char[] buffer1 = c1.toCharArray();
            writer1.write(buffer1);
        }
        try (FileWriter writer2 = new FileWriter(temp2)) {
            String c2 = "<?xml version=\"1.0\"?>" + "<root><a/>text</root>";
            char[] buffer2 = c2.toCharArray();
            writer2.write(buffer2);
        }
        XyDiff xydiff = new XyDiff(temp1, temp2);
        Document delta = xydiff.diff().getDeltaDocument();

        Document ref = new Document();
        ElementNode root = new ElementNode("delta");
        ElementNode ia = new ElementNode("AttributeDeleted");
        ia.setAttribute("pos", "0:0:0");
        ia.setAttribute("path", "/root/a/@test");
        ia.setAttribute("name", "test");
        root.appendChild(ia);
        ref.appendChild(root);

        String s1 = delta.toString();
        String s2 = ref.toString();

        assertEquals("XML Command", s1, s2);
    }

    @Override
    protected void tearDown() throws Exception {
        System.out.println(tempDirPath);
    }
}
