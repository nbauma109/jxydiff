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
package fr.loria.ecoo.so6.antlr;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import antlr.Token;


public class Main {
    public static void main(String[] args) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8)) {
            XMLLexer lexer = new XMLLexer(reader);

            Token t = null;
            while ((t = lexer.nextToken()).getText() != null) {
                System.out.println(t);
            }
            //XMLParser parser = new XMLParser(lexer);
            //Document doc = parser.document();
            //doc.exportXML(new PrintWriter(System.out), false);

            //doc.save(args[1], true);
            //
            //TreeNode node1 = doc.getNode("0:0:7:281");
            //TreeNode node2 = doc.getNode("0:0:7:282");
            //System.out.println(node1.toString());
            //System.out.println(node2.toString());
            //System.out.println("equalsContent="+node1.equalsContent(node2));
        } catch (Exception e) {
           System.err.println(e);
        }
    }
}
