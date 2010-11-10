/**
 * 
 */
package com.cowinvest.lib.suffixTree;

/**
 * @author Jagjit 'Jay' Choudhary
 *
 */
class SuffixTreeEdge {
	// constructor to build a leaf edge (which will eventually point to a leafnode)
	SuffixTreeEdge (int startIndex) {
		this.startIndex = startIndex;
		this.endIndex   = -1;
		this.childNode  = null;
	}
	
	// constructor to build a branch edge (which points to a branch node)
	SuffixTreeEdge (int startIndex, int endIndex, SuffixTreeNode childNode) {
		this.startIndex = startIndex;
		this.endIndex   = endIndex;
		this.childNode  = childNode;
	}
	
	boolean isLeaf () {
		return (endIndex < 0);
	}
	
	int getStartIndex() {
		return startIndex;
	}
	
	void setStartIndex( int startIndex) {
		this.startIndex = startIndex;
	}

	int getEndIndex() {
		return endIndex;
	}
	
	void setEndIndex( int endIndex) {
		this.endIndex = endIndex;
	}

	SuffixTreeNode getChild () {
		return childNode;
	}
	// private
	private int startIndex;
	private int endIndex;
	private SuffixTreeNode childNode;

} // class SuffixTreeEdge
