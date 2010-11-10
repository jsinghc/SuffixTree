/**
 * 
 */
package com.cowinvest.lib.suffixTree;

import java.util.HashMap;

/**
 * @author Jagjit 'Jay' Choudhary
 *
 */
class SuffixTreeNode {
	SuffixTreeNode() {
		childNodes = new HashMap<Character, SuffixTreeEdge>();
		suffixLink = null;
	}
		
	// add a new edge (the edge has startIndex, endIndex and next node)
	// adding an initialized edge is equivalent to adding the transition g'(s, (k,x)) = r
	void addChild (char childChar, SuffixTreeEdge edge) {
		childNodes.put(childChar, edge);
	}
	
	SuffixTreeEdge getChild (char childChar) {
		return childNodes.get(childChar);
	}
	
	// Set the suffix link ()
	void setSuffixLink (SuffixTreeNode suffixLink) {
		this.suffixLink = suffixLink;
	}
	
	SuffixTreeNode getSuffixLink () {
		return suffixLink;
	}
	
	// private
	private HashMap<Character, SuffixTreeEdge> childNodes;
	
	private SuffixTreeNode suffixLink;
	
	

} // class SuffixTreeNode
