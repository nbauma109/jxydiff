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
package fr.loria.ecoo.so6.xml.xydiff;

import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.Path;


public class DeleteAttribute extends XMLCommand {
    private String name;

    public DeleteAttribute(Path path, String name) {
        super(path);
        this.name = name;
        this.type = XMLCommand.DELETE_ATTRIBUTE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ("DeleteAttribute: " + this.name + " path " + this.nodePath);
    }

    @Override
    public ElementNode toXML() {
        try {
            ElementNode ad = new ElementNode("AttributeDeleted");
            ad.setAttribute("pos", this.nodePath.getNumericPath());
            ad.setAttribute("path", this.nodePath.getPseudoXPath());
            ad.setAttribute("name", name);

            return ad;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
