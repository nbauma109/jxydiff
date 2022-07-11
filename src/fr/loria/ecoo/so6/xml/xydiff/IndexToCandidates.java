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

import java.util.Hashtable;
import java.util.Map;

public class IndexToCandidates {
    private Map<Integer, CandidateSet> candidateSets = new Hashtable<>(); // (key, CandidateSet)

    public CandidateSet get(int key) {
        //if (key == 0) {
        //	System.out.println("ERROR IndexToCandidates.get: key is 0");
        //} else {
        //System.out.println("requesting CandidateSet with key=" +
        // Integer.toHexString(key));
        //}
        CandidateSet cs = this.candidateSets.get(Integer.valueOf(key));

        if (cs == null) {
            // If cs is null allocate a new Candidate Set
            cs = new CandidateSet();

            //this.candidateSets.put(Integer.valueOf(key), cs);
            put(key, cs);
        }

        return cs;
    }

    public void put(int key, CandidateSet cs) {
        this.candidateSets.put(key, cs);
    }

    public boolean containsKey(int key) throws Exception {
        return this.candidateSets.containsKey(Integer.valueOf(key));
    }
}