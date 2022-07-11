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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.loria.ecoo.so6.xml.node.ElementNode;
import fr.loria.ecoo.so6.xml.node.TreeNode;


public class NodesManager {

    private static final String INVALID_NODE_ID = "invalid node ID =";

    public static int MIN_CANDIDATEPARENT_LEVEL = 3;
    private static int NULL_ID = 0;
    public int sourceNumberOfNodes = 0;
    public int resultNumberOfNodes = 0;
    private List<TreeNode> v0nodeByDID; // contains Node
    private List<TreeNode> v1nodeByDID; // contains Node
    private List<AtomicInfo> v0nodes; // contains AtomicInfo
    private List<AtomicInfo> v1nodes; // contains AtomicInfo
    private List<IndexToCandidates> xxlistOfCandidatesByParentLevelByHash; // contains
    private TreeNode v0doc;
    private TreeNode v1doc;
    private int statsRecursiveAssignFailed = 0;
    private int statsNodeAlreadyAssigned = 0;
    private int statsCantMatchDifferentOwnHash = 0;
    private int sourceAssigned = 0;
    private int resultAssigned = 0;

    public NodesManager() {
        // to do something else...
        this.v0nodes = new ArrayList<>();
        this.v0nodes.add(null);
        this.v1nodes = new ArrayList<>();
        this.v1nodes.add(null);
        this.v0nodeByDID = new ArrayList<>();
        this.v0nodeByDID.add(null);
        this.v1nodeByDID = new ArrayList<>();
        this.v1nodeByDID.add(null);
        this.xxlistOfCandidatesByParentLevelByHash = new ArrayList<>();
        this.xxlistOfCandidatesByParentLevelByHash.add(new IndexToCandidates());

        // until here...
    }

    public void dumpAssignedNodes() {
        System.out.println("v0nodeByDID");

        for (int i = 0; i < v0nodeByDID.size(); i++) {
            System.out.println("\t" + i + "->" + v0nodeByDID.get(i));
        }

        System.out.println("v1nodeByDID");

        for (int i = 0; i < v1nodeByDID.size(); i++) {
            System.out.println("\t" + i + "->" + v1nodeByDID.get(i));
        }

        System.out.println("v0node");

        for (int i = 0; i < v0nodes.size(); i++) {
            if (v0nodes.get(i) != null) {
                AtomicInfo info = v0nodes.get(i);
                System.out.println("\t" + i + "->" + info.myMatchID);
                System.out.println("\tOwnHash = " + info.myOwnHash.toHexString());
                System.out.println("\tmySubtreeHash = " + info.mySubtreeHash.toHexString());
            } else {
                System.out.println("\t" + i + "-> null");
            }
        }

        System.out.println("v1node");

        for (int i = 0; i < v1nodes.size(); i++) {
            if (v1nodes.get(i) != null) {
                AtomicInfo info = v1nodes.get(i);
                System.out.println("\t" + i + "->" + info.myMatchID);
                System.out.println("\tOwnHash = " + info.myOwnHash.toHexString());
                System.out.println("\tmySubtreeHash = " + info.mySubtreeHash.toHexString());
            } else {
                System.out.println("\t" + i + "-> null");
            }
        }
    }

    public void addV0NodeInfo(AtomicInfo myAtomicInfo) {
        this.v0nodes.add(myAtomicInfo);
    }

    public void addV1NodeInfo(AtomicInfo myAtomicInfo) {
        this.v1nodes.add(myAtomicInfo);
    }

    public void addV0NodeByID(TreeNode node) {
        this.v0nodeByDID.add(node);
    }

    public void addV1NodeByID(TreeNode node) {
        this.v1nodeByDID.add(node);
    }

    // accessor
    public AtomicInfo getV0NodeInfo(int v0nodeID) throws Exception {
        if ((v0nodeID < 1) || (v0nodeID > this.v0nodes.size())) {
            throw new Exception(INVALID_NODE_ID + v0nodeID);
        }

        return this.v0nodes.get(v0nodeID);
    }

    public AtomicInfo getV1NodeInfo(int v1nodeID) throws Exception {
        if ((v1nodeID < 1) || (v1nodeID > this.v1nodes.size())) {
            throw new Exception(INVALID_NODE_ID + v1nodeID);
        }

        return this.v1nodes.get(v1nodeID);
    }

    public TreeNode getV0NodeByID(int v0nodeID) throws Exception {
        if ((v0nodeID < 1) || (v0nodeID > this.v0nodeByDID.size())) {
            throw new Exception(INVALID_NODE_ID + v0nodeID);
        }

        return this.v0nodeByDID.get(v0nodeID);
    }

    public TreeNode getV1NodeByID(int v1nodeID) throws Exception {
        if ((v1nodeID < 1) || (v1nodeID > this.v1nodeByDID.size())) {
            throw new Exception(INVALID_NODE_ID + v1nodeID);
        }

        return this.v1nodeByDID.get(v1nodeID);
    }

    private IndexToCandidates getCandidatesByParentLevelByHash(int level) {
        return this.xxlistOfCandidatesByParentLevelByHash.get(level);
    }

    public TreeNode getV0Document() {
        return this.v0doc;
    }

    public TreeNode getV1Document() {
        return this.v1doc;
    }

    /*
     * Registration of v0 and v1 nodes
     *
     * What we want is: - a direct DiffID <-> Node mapping - a path from v1 Node to its hash value - a path from an hash value to v0 nodes with the same hash -
     * a notion of 'weight' for a subtree in v1
     *
     */
    public void registerSourceDocument(TreeNode sourceDoc)
        throws Exception {
        if (this.sourceNumberOfNodes != 0) {
            throw new Exception("source document has already been registered");
        }

        TreeNode v0root = sourceDoc;
        registerSubtree(v0root, true);
        this.v0doc = sourceDoc;

        for (int i = 1; i <= MIN_CANDIDATEPARENT_LEVEL; i++) {
            this.xxlistOfCandidatesByParentLevelByHash.add(new IndexToCandidates());
        }

        computeCandidateIndexTables(this.sourceNumberOfNodes);
    }

    public void registerResultDocument(TreeNode resultDoc)
        throws Exception {
        if (this.sourceNumberOfNodes == 0) {
            throw new Exception("source document must be registered before target document");
        }

        TreeNode v1root = resultDoc;
        registerSubtree(v1root, false);
        this.v1doc = resultDoc;
    }

    // hash set function from lookup2.c are required...
    private int registerSubtree(TreeNode node, boolean isSource)
        throws Exception {
        if (node == null) {
            throw new Exception("node is null");
        }

        AtomicInfo myAtomicInfo = new AtomicInfo();
        myAtomicInfo.myWeight = node.getWeight(); // might be optimized (caching)...

        List<AtomicInfo> childrenAtomicInfoList = new ArrayList<>();

        // Node management
        myAtomicInfo.myOwnHash = node.getHash32();
        myAtomicInfo.mySubtreeHash = (Hash32) myAtomicInfo.myOwnHash.clone();

        StringBuilder hash = new StringBuilder().append(myAtomicInfo.mySubtreeHash.toHexString());

        if (node.allowChildren() && node.hasChildren()) {
            for (TreeNode child : node.getChildren()) {
                int childID = registerSubtree(child, isSource);
                AtomicInfo childAtomicInfo = (isSource) ? getV0NodeInfo(childID) : getV1NodeInfo(childID);
                childrenAtomicInfoList.add(childAtomicInfo);
                hash.append(childAtomicInfo.mySubtreeHash.toHexString());
            }

            myAtomicInfo.mySubtreeHash = new Hash32(hash.toString());
        }

        if (node.allowAttributes()) {
            // Attribute Management
            Map<String, String> attributes = node.getAttributes();

            for (String name : attributes.keySet()) {
                Hash32 keyIdH = new Hash32(node.getHash32());
                Hash32 attrNameH = new Hash32(name);
                myAtomicInfo.myOwnHash.value = (keyIdH.value << 1) + (attrNameH.value << 2);
            }
        }

        if (isSource) {
            myAtomicInfo.myID = ++this.sourceNumberOfNodes;
        } else {
            myAtomicInfo.myID = ++this.resultNumberOfNodes;
        }

        // Update AtomicInfo of children
        int nbChildren = childrenAtomicInfoList.size();

        AtomicInfo pred = null;
        AtomicInfo current = null;

        for (int i = 0; i < nbChildren; i++) {
            current = childrenAtomicInfoList.get(i);
            current.myParent = myAtomicInfo.myID;
            current.myPosition = (i + 1);

            if (pred != null) {
                pred.nextSibling = current.myID;
            }

            pred = current;
        }

        if (nbChildren != 0) {
            myAtomicInfo.firstChild = childrenAtomicInfoList.get(0).myID;
        }

        // Store node in array
        if (isSource) {
            addV0NodeInfo(myAtomicInfo);
            addV0NodeByID(node);
        } else {
            addV1NodeInfo(myAtomicInfo);
            addV1NodeByID(node);
        }

        return myAtomicInfo.myID;
    }

    private void computeCandidateIndexTables(int v0nodeID)
        throws Exception {
        AtomicInfo myAtomicInfo = getV0NodeInfo(v0nodeID);

        //		((CandidateSet) getCandidatesByParentLevelByHash(0).get(new
        // Integer(myAtomicInfo.mySubtreeHash.valnue))).v0node.add(new
        // Integer(myAtomicInfo.myID));
        IndexToCandidates itc = getCandidatesByParentLevelByHash(0);
        CandidateSet cs = itc.get(myAtomicInfo.mySubtreeHash.value);
        cs.add(myAtomicInfo.myID);

        int relativeLevel = 1;
        int relativeId = myAtomicInfo.myParent;

        while ((relativeLevel <= NodesManager.MIN_CANDIDATEPARENT_LEVEL) && (relativeId > 0)) {
            long[] buf = new long[2];
            buf[1] = relativeId;
            buf[0] = myAtomicInfo.mySubtreeHash.value;

            //hash32 tablekey((unsigned char*)buf, 8);
            //			Object tablekey = buf; // quick hack...
            Hash32 tablekey = new Hash32(buf);

            IndexToCandidates theIndex = getCandidatesByParentLevelByHash(relativeLevel);
            theIndex.get(tablekey.value).add(myAtomicInfo.myID);

            relativeLevel++;
            relativeId = getV0NodeInfo(relativeId).myParent;
        }

        int childID = myAtomicInfo.firstChild;

        while (childID != 0) {
            computeCandidateIndexTables(childID);
            childID = getV0NodeInfo(childID).nextSibling;
        }
    }

    private void nodeAssign(int v0nodeID, int v1nodeID)
        throws Exception {
        if (tryNodeAssign(v0nodeID, v1nodeID)) {
            getV0NodeInfo(v0nodeID).myMatchID = v1nodeID;
            this.sourceAssigned++;
            getV1NodeInfo(v1nodeID).myMatchID = v0nodeID;
            this.resultAssigned++;
        }
    }

    private boolean tryNodeAssign(int v0nodeID, int v1nodeID)
        throws Exception {
        TreeNode n0 = getV0NodeByID(v0nodeID);
        TreeNode n1 = getV1NodeByID(v1nodeID);

        if (n0.getClass() == n1.getClass()) {
            if (!(n0 instanceof ElementNode) || !(n1 instanceof ElementNode)) {
                return true;
            }
            if (((ElementNode) n0).getElementName().equals(((ElementNode) n1).getElementName())) {
                return true;
            }
        }

        return false;
    }

    private void forceParentsAssign(int v0nodeID, int v1nodeID, int level)
        throws Exception {
        if ((v0nodeID == 0) || (v1nodeID == 0)) {
            throw new Exception("forceParentsAssign: bad arguments (" + v0nodeID + ", " + v1nodeID + ")");
        }

        int v0ascendant = v0nodeID;
        int v1ascendant = v1nodeID;

        for (int i = 0; i < (level - 1); i++) {
            v0ascendant = getV0NodeInfo(v0ascendant).myParent;
            v1ascendant = getV1NodeInfo(v1ascendant).myParent;

            if ((v0ascendant == 0) || (v1ascendant == 0) || v0Assigned(v0ascendant) || v1Assigned(v1ascendant)) {
                return;
            }

            nodeAssign(v0ascendant, v1ascendant);

            return;
        }
    }

    protected boolean v0Assigned(int id) throws Exception {
        if ((id < 1) || (id > this.sourceNumberOfNodes)) {
            throw new Exception("OutOfBound node ID");
        }

        return (getV0NodeInfo(id).myMatchID != NodesManager.NULL_ID);
    }

    protected boolean v1Assigned(int id) throws Exception {
        if ((id < 1) || (id > this.resultNumberOfNodes)) {
            throw new Exception("OutOfBound node ID");
        }

        return (getV1NodeInfo(id).myMatchID != NodesManager.NULL_ID);
    }

    /*
     * Cette phase ne sert ? rien tant qu'il n'y a pas d'appel en amont ? matchID
     */
    public int fullBottomUp(int v1nodeID) throws Exception {
        //System.out.println("fullBottomUp call on v1 node " + v1nodeID);
        AtomicInfo myV1AtomicInfo = getV1NodeInfo(v1nodeID);

        Map<Integer, Float> weightByCandidate = new HashMap<>(); // (nodeid: int,

        // weight:float)
        int childID = myV1AtomicInfo.firstChild;

        while (childID != 0) {
            AtomicInfo childInfo = getV1NodeInfo(childID);
            int childMatch = fullBottomUp(childID);

            if (childMatch != 0) {
                AtomicInfo childMatchInfo = getV0NodeInfo(childMatch);
                int v0childParent = childMatchInfo.myParent;

                if (v0childParent != 0) {
                    if (!weightByCandidate.containsKey(Integer.valueOf(v0childParent))) {
                        weightByCandidate.put(v0childParent, (float) childInfo.myWeight);
                    } else {
                        float weight = weightByCandidate.get(Integer.valueOf(v0childParent));
                        weight += childInfo.myWeight;
                        weightByCandidate.put(v0childParent, weight);
                    }
                }
            }

            childID = childInfo.nextSibling;
        }

        // Do self
        if (myV1AtomicInfo.myMatchID != 0) {
            // Node in v1 with the ID v1nodeID already has a match
            return myV1AtomicInfo.myMatchID;
        }

        if (weightByCandidate.isEmpty()) {
            // Node in v1 with the ID v1nodeID has non matched
            return 0; //myV1AtomicInfo.myMatchID;
        }

        // Find parent corresponding to largest part of children
        float max = (float) -1.0;
        int bestMatch = 0;

        for (Integer key : weightByCandidate.keySet()) {
            int first = key;
            float second = weightByCandidate.get(key);

            if (second > max) {
                bestMatch = first;
                max = second;
            }
        }

        //if(getV0NodeByID(bestMatch).equalsContent(getV1NodeByID(v1nodeID))){
        nodeAssign(bestMatch, v1nodeID);

        //}
        return myV1AtomicInfo.myMatchID;
    }

    /*
     * Top-Down matching algorithm
     *
     * The algorithm relies on two features: 1) Order the nodes to match in top-down or weight-related order 2) Find the best node among a certain number of
     * nodes with the same content
     *
     */
    public void topDownMatch(int v0rootID, int v1rootID)
        throws Exception {
        List<Integer> toMatch = new ArrayList<>();

        toMatch.add(v1rootID);

        while (!toMatch.isEmpty()) {
            // get node to investigate
            int nodeID = toMatch.remove(0); // equivalent
            Hash32 v1hash = getV1NodeInfo(nodeID).mySubtreeHash;

            int matcher = 0;

            if (!v1Assigned(nodeID)) {
                if (nodeID == v1rootID) {
                    nodeAssign(v0rootID, v1rootID);
                } else {
                    matcher = getBestCandidate(nodeID, v1hash.value);
                }
            }

            if (matcher != 0) {
                // GetBestCandidate is not null, so we match children too
                recursiveAssign(matcher, nodeID);
            } else {
                // if not found, children will have to be investigated
                int childID = getV1NodeInfo(nodeID).firstChild;

                while (childID != 0) {
                    toMatch.add(childID);
                    childID = getV1NodeInfo(childID).nextSibling;
                }
            }

            // next node to investigate
        }
    }

    private void recursiveAssign(int v0nodeID, int v1nodeID)
        throws Exception {
        if ((v0nodeID == 0) || (v1nodeID == 0)) {
            throw new Exception("The IDs value is '0'");
        }

        nodeAssign(v0nodeID, v1nodeID);

        int v0child = getV0NodeInfo(v0nodeID).firstChild;
        int v1child = getV1NodeInfo(v1nodeID).firstChild;

        while (v0child != 0) {
            if (v1child == 0) {
                this.statsRecursiveAssignFailed++;
                System.err.println("recursiveAssign: expected child in v1 not found");

                return;
            }

            recursiveAssign(v0child, v1child);
            v0child = getV0NodeInfo(v0child).nextSibling;
            v1child = getV1NodeInfo(v1child).nextSibling;
        }

        if (v1child != 0) {
            this.statsRecursiveAssignFailed++;
            System.err.println("recursiveAssign: expect child in v0 not found");
        }
    }

    // TODO : getBestCandidate...
    // From a number of old nodes that have the exact same signature, one has to
    // choose which one
    // will be considered 'matching' the new node
    // Basically, the best is the old node somehow related to new node: parents
    // are matching for example
    // If none has this property, and if hash_matching is *meaningful* (text
    // length > ???) we may consider
    // returning any matching node
    // Maybe on a second level parents ?
    private int getBestCandidate(int v1nodeID, int selfkey)
        throws Exception {
        if (!getCandidatesByParentLevelByHash(0).containsKey(selfkey)) {
            //System.out.println("Out of getBestCandidate because the key is
            // not present");
            return 0;
        }
        CandidateSet secondz = getCandidatesByParentLevelByHash(0).get(selfkey);

        if (secondz.size() == 0) {
            //System.out.println("Out of getBestCandidate because there is
            // no element for this key");
            return 0;
        }

        // first pass: finds a node which parent matches v1node parent (useful
        // because documents roots always match or parent
        // may be matched thanks to its unique label)
        int candidateRelativeLevel = 1;
        int v1nodeRelative = v1nodeID;

        // the relative weight correspond to the ratio of the weight of the
        // subtree over the weight of the entire document
        int v1rootID = this.resultNumberOfNodes;
        double relativeWeight = getV1NodeInfo(v1nodeID).myWeight / getV1NodeInfo(v1rootID).myWeight;
        int maxLevelPath = NodesManager.MIN_CANDIDATEPARENT_LEVEL + (int) (5.0 * Math.log(this.resultNumberOfNodes) * relativeWeight);

        // Try to attach subtree to existing match among ancesters
        // up to maximum level of ancester, depending on subtree weight
        while (candidateRelativeLevel <= maxLevelPath) {
            // get info for ancester at corresponding level
            v1nodeRelative = getV1NodeInfo(v1nodeRelative).myParent;

            if (v1nodeRelative == 0) {
                return 0;
            }

            AtomicInfo v1nodeRelativeInfo = getV1NodeInfo(v1nodeRelative);

            if (v1nodeRelativeInfo.myMatchID > 0) {
                // for the lower levels, use precomputed index tables to acces
                // candidates given the parent
                if (candidateRelativeLevel <= NodesManager.MIN_CANDIDATEPARENT_LEVEL) {
                    //System.out.println("using pre-computed index for relative
                    // level " + candidateRelativeLevel);
                    long[] buf = new long[2];
                    buf[1] = v1nodeRelativeInfo.myMatchID;
                    buf[0] = selfkey;

                    //hash32 tablekey((unsigned char*)buf, 8);
                    //Object tablekey = buf; // quick hack...
                    Hash32 tablekey = new Hash32(buf);

                    IndexToCandidates theIndex = getCandidatesByParentLevelByHash(candidateRelativeLevel);

                    if (theIndex.containsKey(tablekey.value)) {
                        CandidateSet second = theIndex.get(tablekey.value);

                        for (int i = 0; i < second.size(); i++) {
                            int c = second.get(i);

                            if (!v0Assigned(c)) {
                                if (candidateRelativeLevel > 1) {
                                    forceParentsAssign(c, v1nodeID, candidateRelativeLevel);
                                }

                                return c;
                            }
                        }
                    }
                } else {
                    // for higher levels, try every candidate and this if its
                    // ancestor is a match for us
                    if (!getCandidatesByParentLevelByHash(0).containsKey(selfkey)) {
                        return 0;
                    }

                    CandidateSet second = getCandidatesByParentLevelByHash(0).get(selfkey);

                    for (int i = 0; i < second.size(); i++) {
                        int candidate = second.get(i);

                        if (!v0Assigned(candidate)) { // node still not assigned

                            //System.out.print("(" + candidate + ")");
                            // get its relative
                            int candidateRelative = candidate;
                            int I = 0;

                            for (int j = 0; j < candidateRelativeLevel; j++) {
                                candidateRelative = getV0NodeInfo(candidateRelative).myParent;

                                if (candidateRelative == 0) {
                                    I = candidateRelativeLevel + 1;
                                }
                            }

                            // if relative is ok at required level, test
                            // matching
                            if ((I == candidateRelativeLevel) && (getV0NodeInfo(candidateRelative).myMatchID == v1nodeRelative)) {
                                if (candidateRelativeLevel > 1) {
                                    forceParentsAssign(candidate, v1nodeID, candidateRelativeLevel);
                                }

                                return candidate;
                            }
                        } // else : candidate was already assigned
                    } // try next candidate
                } // end MIN(Precomputed) < relativeLevel < MAX
            } // end ancestor is matched

            candidateRelativeLevel++;
        } // next level

        return 0;
    }

    /*
     * Optimization Phase * * The goal of this phase is to use matchings, and if they give us * obvious results for children *
     */
    public void optimize(int v0nodeID) throws Exception {
        // not completely done in Diff_NodesManager.cc
        AtomicInfo myAtomicInfo = this.getV0NodeInfo(v0nodeID);

        // If node is matched, we can try to do some work
        // Get Free nodes in v0
        Map<String, Integer> v0freeChildren = new HashMap<>(); // Map(String, int)

        if (v0Assigned(v0nodeID)) {
            int childID = myAtomicInfo.firstChild;

            while (childID != 0) {
                if (!v0Assigned(childID)) {
                    TreeNode child = this.getV0NodeByID(childID);

                    if (child.allowChildren()) {
                        // Children is free !
                        if (v0freeChildren.containsKey(child.getId())) {
                            v0freeChildren.put(child.getId(), (-1));

                            // But many have the same name...
                        } else {
                            v0freeChildren.put(child.getId(), (childID));
                        }
                    }
                }

                childID = (this.getV0NodeInfo(childID)).nextSibling;
            }

            // Look for similar nodes in v1
            int v1nodeID = myAtomicInfo.myMatchID;
            AtomicInfo v1AtomicInfo = this.getV1NodeInfo(v1nodeID);

            Map<String, Integer> v1freeChildren = new HashMap<>();

            if (v1Assigned(v1nodeID)) {
                childID = v1AtomicInfo.firstChild;

                while (childID != 0) {
                    if (!v1Assigned(childID)) {
                        TreeNode child = this.getV1NodeByID(childID);

                        if (child.allowChildren()) {
                            //String tag = child.getNodeName();
                            // v1 children is free
                            if (v1freeChildren.containsKey(child.getId())) {
                                v1freeChildren.put(child.getId(), (-1));

                                // But many have the same name...
                            } else {
                                v1freeChildren.put(child.getId(), (childID));
                            }
                        }
                    }

                    childID = (this.getV1NodeInfo(childID)).nextSibling;
                }
            }

            // Now match unique children
            for (String key : v0freeChildren.keySet()) {
                String first = key;
                int second = v0freeChildren.get(key);

                if ((second > 0) && (v1freeChildren.containsKey(first))) {
                    int v1ID = (v1freeChildren.get(first));

                    if (v1ID > 0) {
                        this.nodeAssign(second, v1ID);
                    }
                }
            }

            // End-if - Assigned(v0nodeID)
        }

        // Apply recursively on children
        int childID = myAtomicInfo.firstChild;

        while (childID != 0) {
            this.optimize(childID);
            childID = (this.getV0NodeInfo(childID)).nextSibling;
        }
    }

    /*
     * create the Script that constructs v1 from v0. This script will then be used to create the XML-delta document
     *
     */

    // ---- Make script for DELETE operations ----
    public void markOldTree(int v0nodeID) throws Exception {
        AtomicInfo myAtomicInfo = getV0NodeInfo(v0nodeID);

        // apply to children
        int childID = myAtomicInfo.firstChild;

        while (childID != 0) {
            markOldTree(childID);
            childID = getV0NodeInfo(childID).nextSibling;
        }

        // test if node is DELETED
        if (!v0Assigned(v0nodeID)) {
            myAtomicInfo.myEvent = AtomicInfo.NODEEVENT_DELETED;
        } else // test if node is a STRONG MOVE
        if ((myAtomicInfo.myParent != 0) && ((!v0Assigned(myAtomicInfo.myParent)) || (getV0NodeInfo(myAtomicInfo.myParent).myMatchID != getV1NodeInfo(myAtomicInfo.myMatchID).myParent))) {
            myAtomicInfo.myEvent = AtomicInfo.NODEEVENT_STRONGMOVE;
        }
    }

    public void markNewTree(int v1nodeID) throws Exception {
        AtomicInfo myAtomicInfo = getV1NodeInfo(v1nodeID);

        int childID = myAtomicInfo.firstChild;

        while (childID != 0) {
            markNewTree(childID);
            childID = getV1NodeInfo(childID).nextSibling;
        }

        // test if node is assigned
        if (!v1Assigned(v1nodeID)) {
            myAtomicInfo.myEvent = AtomicInfo.NODEEVENT_INSERTED;
        } else if ((myAtomicInfo.myParent != 0) && ((!v1Assigned(myAtomicInfo.myParent)) || (getV1NodeInfo(myAtomicInfo.myParent).myMatchID != getV0NodeInfo(myAtomicInfo.myMatchID).myParent))) {
            myAtomicInfo.myEvent = AtomicInfo.NODEEVENT_STRONGMOVE;
        }
    }

    // ++++ Compute Weak Move ++++
    public void computeWeakMove(int v0nodeID) throws Exception {
        AtomicInfo myAtomicInfo = getV0NodeInfo(v0nodeID);

        // Apply to children
        int v0childID = myAtomicInfo.firstChild;

        while (v0childID != 0) {
            computeWeakMove(v0childID);
            v0childID = getV0NodeInfo(v0childID).nextSibling;
        }

        if ((myAtomicInfo.firstChild == 0) || !v0Assigned(v0nodeID)) {
            return;
        }

        // apply to Self
        int v1nodeID = myAtomicInfo.myMatchID;

        // Set Index to children of v0 node that are remaining on this node
        // Set 0 for others
        List<WSequence> oldChildValue = new ArrayList<>(); // contains WSequence
        oldChildValue.add(new WSequence(-1, 999000.0));

        int index = 1;
        v0childID = myAtomicInfo.firstChild;

        while (v0childID != 0) {
            AtomicInfo childInfo = getV0NodeInfo(v0childID);

            if (childInfo.myEvent == AtomicInfo.NODEEVENT_NOP) {
                oldChildValue.add(new WSequence(index++, childInfo.myWeight));
            } else {
                oldChildValue.add(new WSequence(0, 998000.0));
            }

            v0childID = childInfo.nextSibling;
        }

        List<WSequence> originalSequence = new ArrayList<>(); // contains WSequence
        originalSequence.add(new WSequence(-1, 997000.0));

        for (int i = 1; i < oldChildValue.size(); i++) {
            if (oldChildValue.get(i).data != 0) {
                originalSequence.add(oldChildValue.get(i));
            }
        }

        // construct sequence with new orders of children, given their 'stable
        // index'
        List<WSequence> finalSequence = new ArrayList<>(); // contains WSequence
        finalSequence.add(new WSequence(-1, 996000.0));

        int v1childID = getV1NodeInfo(v1nodeID).firstChild;

        while (v1childID != 0) {
            AtomicInfo childInfo = getV1NodeInfo(v1childID);

            if (childInfo.myEvent == AtomicInfo.NODEEVENT_NOP) {
                int originPos = getV0NodeInfo(childInfo.myMatchID).myPosition;
                finalSequence.add(new WSequence(oldChildValue.get(originPos).data, childInfo.myWeight));
            }

            v1childID = childInfo.nextSibling;
        }

        // Resolution : may be replaced by Longest Common Subsequence algorithms
        // Find move operations converting originalSequence into finalSequence
        if (originalSequence.size() < 100) {
            CommonSubSequenceAlgorithms.lcss(originalSequence, finalSequence);
        } else {
            CommonSubSequenceAlgorithms.easy_css(originalSequence, finalSequence);
        }

        // children that have been marked with 0 will be 'moved'
        index = 1;
        v0childID = myAtomicInfo.firstChild;

        while (v0childID != 0) {
            if (getV0NodeInfo(v0childID).myEvent == AtomicInfo.NODEEVENT_NOP) {
                if (originalSequence.get(index).data == 0) {
                    getV0NodeInfo(v0childID).myEvent = AtomicInfo.NODEEVENT_WEAKMOVE;
                    getV1NodeInfo(getV0NodeInfo(v0childID).myMatchID).myEvent = AtomicInfo.NODEEVENT_WEAKMOVE;
                }

                index++;
            }

            v0childID = getV0NodeInfo(v0childID).nextSibling;
        }
    }

    // ++++ Detect Update Operations ++++
    // This is another specific case:
    // If children is a single text node, and is not assigned, then consider its
    // value has been 'updated'
    // Node that even if a text node is updated, a new XID will be allocated
    // for the entry corresponding to the 'insert' part of 'UPDATE'
    // The reason for this is that the minimal requirement for an XID
    // is that the value of the root of the subtree identified by the XID
    // is always the same
    public void detectUpdate(int v0nodeID) throws Exception {
        AtomicInfo myAtomicInfo = this.getV0NodeInfo(v0nodeID);

        if (v0Assigned(v0nodeID)) {
            int v1nodeID = myAtomicInfo.myMatchID;
            AtomicInfo v1AtomicInfo = this.getV1NodeInfo(v1nodeID);

            int child0 = myAtomicInfo.firstChild;
            AtomicInfo myChild0Info = null;

            if (child0 != 0) {
                myChild0Info = this.getV0NodeInfo(child0);
            }

            if ((child0 != 0) && (!v0Assigned(child0)) && (myChild0Info.nextSibling == 0)) {

                int child1 = v1AtomicInfo.firstChild;

                // Separate the test to childID because, child1 may be '0'
                if (child1 != 0) {
                    AtomicInfo myChild1Info = this.getV1NodeInfo(child1);

                    if ((!v1Assigned(child1)) && (myChild1Info.nextSibling == 0)) {
                        TreeNode domChild0 = this.getV0NodeByID(child0);
                        TreeNode domChild1 = this.getV1NodeByID(child1);

                        if ((!domChild0.allowChildren()) && (!domChild1.allowChildren())) {
                            if ((myChild0Info.myEvent != AtomicInfo.NODEEVENT_DELETED) || (myChild1Info.myEvent != AtomicInfo.NODEEVENT_INSERTED)) {
                                throw new Exception("NodesManager: Update handler state is inconsistant");
                            }
                            // ---
                            myChild0Info.myEvent = AtomicInfo.NODEEVENT_UPDATE_OLD;
                            myChild1Info.myEvent = AtomicInfo.NODEEVENT_UPDATE_NEW;
                        }
                    }
                }
            }
        }

        // Apply recursively on children
        int childID = myAtomicInfo.firstChild;

        while (childID != 0) {
            this.detectUpdate(childID);
            childID = (this.getV0NodeInfo(childID)).nextSibling;
        }
    }
}
