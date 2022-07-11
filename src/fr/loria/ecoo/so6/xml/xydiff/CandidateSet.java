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

import java.util.ArrayList;
import java.util.List;

//	   Note that push_back must be used to add candidates (in postfix order)
//	   Thus the ordering of children will be somehow taken into consideration during
// lookup for the best candidate
public class CandidateSet {
    private List<Integer> v0node = new ArrayList<>(); // contient des int

    public int size() {
        return this.v0node.size();
    }

    public int get(int i) {
        return this.v0node.get(i);
    }

    public void add(int i) {
        this.v0node.add(i);
    }
}