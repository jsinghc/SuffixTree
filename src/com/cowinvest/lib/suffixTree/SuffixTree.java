/**
 * 
 */
package com.cowinvest.lib.suffixTree;
import java.util.*;
/**
 * @author Jagjit 'Jay' Choudhary
 *
 */

public class SuffixTree {
	public SuffixTree (String text) {
		root = new SuffixTreeNode();
		this.text = text;

		SuffixTreeBuilder treeBuilder = new SuffixTreeBuilder();
		treeBuilder.build();
	}

	// method to search for a string in the text 
	// the text is represented by the suffix tree
	public boolean search (String searchText) {
		SuffixTreeNode searchNode = root;
		int edgeIndex = 1;
		int length = searchText.length();
		int searchIndex = 1;
		SuffixTreeEdge edge = null;
		boolean match = true;
		
		while (searchIndex <= length) {
			// search node is null if reached the end of suffix tree 
			// but reached the end of search text 
			if (searchNode == null) {
				match = false;
				break;
			}

			// if edge is null check if the node has an edge starting with char at searchIndex
			if (edge == null) {
				edge = searchNode.getChild(searchText.charAt(searchIndex-1));
				if (edge == null) {	
					match = false;
					break;
				}
				edgeIndex = edge.getStartIndex();
			}
			else {
				// check if the next character matches
				if (searchText.charAt(searchIndex - 1) != text.charAt(edgeIndex - 1)) {
					match = false;
					break;					
				}
			}
			// if the edge index has reached the end advance to the next branch node
			if (edgeIndex == edge.getEndIndex()) {
				searchNode = edge.getChild();
				edge = null;
			}

			// control comes here if the char has matched
			searchIndex++;
			edgeIndex++;
		}
		
		return match;
		
	}
	
	// private
	private SuffixTreeNode root;
	private String         text;

	private class SuffixTreeBuilder {

	    public void build () {
			SKTuple sk = new SKTuple(root, 1);
			int length = text.length();
			
			for (int i = 1; i <= length; i++) {
				// update the tree with the character at i-th pos
				// also update the s state and k index for the active point
				update(sk, i);
				// update s,k with the canonical reference point of s,k,i
				canonize(sk, i);
			}
			
			// set the end index for all the lead edges
			 ListIterator<SuffixTreeEdge> 	leafIterator = leafEdges.listIterator(0); 
			 while (leafIterator.hasNext()) {
				 SuffixTreeEdge leafEdge = leafIterator.next();
				 leafEdge.setEndIndex(length);
			 }
		}
	    
	    public SuffixTreeBuilder () {
	    	leafEdges = new LinkedList<SuffixTreeEdge>();
	    }
		
	    private LinkedList <SuffixTreeEdge> leafEdges;
	    
		private class SKTuple {
			int kIndex;
			SuffixTreeNode sState;
			
			SKTuple (SuffixTreeNode sState, int kIndex) {
				this.kIndex = kIndex;
				this.sState = sState;
			}
		}
		
		private class TestAndSplitReturn {
			boolean         endPointReached;
			SuffixTreeNode  rState;
			
			TestAndSplitReturn (boolean         endPointReached, 
								SuffixTreeNode  rState) {
				this.endPointReached = endPointReached;
				this.rState          = rState;
			}
		}
		
		
		// the method implementing the update procedure of Ukkonen's SuffixTree algorithm
		private void update (SKTuple sk, int i) {
			SuffixTreeNode oldr = root;
			TestAndSplitReturn testAndSplitReturn;
			
			testAndSplitReturn = testAndSplit(sk, i-1, text.charAt(i-1));
			
			while (!testAndSplitReturn.endPointReached) {
				// create new transition from the state r
				SuffixTreeEdge newEdge = new SuffixTreeEdge(i);
				leafEdges.add(newEdge);
				testAndSplitReturn.rState.addChild(text.charAt(i-1), newEdge);
				
				// oldr is not root add a suffixlink to the state r
				if (oldr != root) {
					oldr.setSuffixLink (testAndSplitReturn.rState);
				}
				
				oldr = testAndSplitReturn.rState;
				
				// in this implementation, we do not have a separate node for state _|_
				// the check for root state tells when to stop following the 
				// suffix links. 
				if (sk.sState == root) {
					// if k <= i-1, then canonize of the suffixlink of root,k,i-1 results
					// in s <-- root and k <-- k+1
					if (sk.kIndex <= i-1) {
						sk.kIndex++;
					}
					// if k > i-1, then canonize leads to _|_ and susequent canonize of _|_,k,i leads to 
					// s <-- root, k <-- k+1 for next i <-- i+1
					// so we increment k and break 
					else {
						sk.kIndex++;
						break;
					}
				}
				else {
					sk.sState = sk.sState.getSuffixLink();
					canonize(sk, i-1);
				}
				testAndSplitReturn = testAndSplit(sk, i-1, text.charAt(i-1));				
			}
			
			// oldr is not root add a suffixlink to the state s
			if (oldr != root) {
				oldr.setSuffixLink (sk.sState);
			}
			
			return;
		}
		
		
		// the method implementing the testAndSplit procedure of Ukkonen's SuffixTree algorithm
		// the method tests if the state represented by s,k,p is an end point for tVal 
		// If it is not an endpoint, then it makes the state s,k,p explicit
		private TestAndSplitReturn testAndSplit (SKTuple sk, int pIndex, char tVal) {
			SuffixTreeEdge tEdge = sk.sState.getChild(text.charAt(sk.kIndex-1));
			
			if (sk.kIndex <= pIndex) {
				// check if the next character on the edge matches the next character in update tVal
				if (tVal == text.charAt(tEdge.getStartIndex() + pIndex - sk.kIndex)) {
					// if the character matches, we have reached the end point
					return new TestAndSplitReturn(true, sk.sState);
				}
				else {
					// if the character does not match, create the state r
					SuffixTreeNode rState = new SuffixTreeNode();
					
					// add the transition from s,k,p to r
					sk.sState.addChild(text.charAt(sk.kIndex-1), new SuffixTreeEdge(tEdge.getStartIndex(), tEdge.getStartIndex() + pIndex - sk.kIndex, rState));

					// add the transition from r to the rest of the tree ahead
					tEdge.setStartIndex(tEdge.getStartIndex() + pIndex - sk.kIndex + 1);
					rState.addChild(text.charAt(tEdge.getStartIndex() - 1), tEdge);
					
					return new TestAndSplitReturn(false, rState);
				}
			} 
			else {
				// For k > p, we return s state. 
				// if tEdge is present we have reached end point
				return new TestAndSplitReturn((tEdge != null) ? true : false, sk.sState);
			}
			
		}
		
		// the method implementing the canonize procedure of Ukkonen's SuffixTree algorithm
		// find the closest explicit state node for the state represented by s,k,p
		private void canonize (SKTuple sk, int pIndex) {
			if (pIndex < sk.kIndex) {
				return;
			}
			else {
				SuffixTreeEdge edge = sk.sState.getChild(text.charAt(sk.kIndex-1));
				while (!edge.isLeaf() && 
					   ((edge.getEndIndex() - edge.getStartIndex()) <= (pIndex - sk.kIndex))) {
					sk.kIndex = sk.kIndex + (edge.getEndIndex() - edge.getStartIndex()) + 1;
					sk.sState = edge.getChild();
					
					if (pIndex < sk.kIndex) {
						break;
					}
					edge = sk.sState.getChild(text.charAt(sk.kIndex-1));
				}				
				return;				
			}			
		}
		
	} // class SuffixTreeBuilder
	
} // class SuffixTree

