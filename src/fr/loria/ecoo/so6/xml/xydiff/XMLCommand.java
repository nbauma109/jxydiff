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

import java.util.Iterator;

import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.Path;
import fr.loria.ecoo.so6.xml.node.PathElement;


public abstract class XMLCommand implements Comparable<XMLCommand> {
    public static final int INSERT_ATTRIBUTE = 3;
    public static final int DELETE_ATTRIBUTE = 4;
    public static final int UPDATE_ATTRIBUTE = 5;
    public static final int DELETE_NODE = 1;
    public static final int INSERT_NODE = 2;
    protected Path nodePath;
    protected int pos = -1;
    protected int type = -1;

    protected XMLCommand(Path path) {
        this.nodePath = path;
    }

    public void setNodePath(Path nodePath) {
        this.nodePath = nodePath;
    }

    public Path getNodePath() {
        return this.nodePath;
    }

    @Override
    public String toString() {
        String type;

        switch (this.type) {
        case INSERT_ATTRIBUTE:
            type = "AddAttribute";

            break;

        case DELETE_ATTRIBUTE:
            type = "DeleteAttribute";

            break;

        case DELETE_NODE:
            type = "DeleteNode";

            break;

        case INSERT_NODE:
            type = "InsertNode";

            break;

        case UPDATE_ATTRIBUTE:
            type = "UpdateAttribute";

            break;

        default:
            type = "Unknown";
        }

        return type + "(" + getNodePath() + ")";
    }

    public ElementNode toXML() {
        return null;
    }

    public void setTye(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return this.pos;
    }

    public static int sign(int val) {
        if (val > 0) {
            return 1;
        }
        if (val < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(XMLCommand cmd) {

        if (cmd.type != this.type) {
            //System.out.println("different type so " + cmd.type + "-" + this.type);
            return sign(this.type - cmd.type); // + ou - ???
        }

        int result = 0;

        Iterator<PathElement> me = this.nodePath.getPathElements().iterator();
        Iterator<PathElement> obj = cmd.nodePath.getPathElements().iterator();

        while (me.hasNext() && obj.hasNext()) {
            int meVal = me.next().getPosition();
            int objVal = obj.next().getPosition();

            if (meVal != objVal) {
                //System.out.println("found predecessor path so " + meVal + "-" + objVal);
                result = sign(meVal - objVal);

                break;
            }
        }

        if (result == 0) {
            if (me.hasNext()) {
                //System.out.println("me has more token");
                result = 1;
            } else if (obj.hasNext()) {
                //System.out.println("obj has more token");
                result = -1;
            } else {
                //System.out.println("same paths so use positions : " + cmd.pos + "-" + this.pos);
                result = sign(cmd.pos - pos);
            }
        }

        // sort order is reversed for DeletedNode
        if (cmd.type == XMLCommand.DELETE_NODE) {
            result = -result;
        }

        return result;
    }
}
