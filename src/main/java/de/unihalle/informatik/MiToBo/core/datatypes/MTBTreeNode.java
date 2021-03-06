/*
 * This file is part of MiToBo, the Microscope Image Analysis Toolbox.
 *
 * Copyright (C) 2010 - @YEAR@
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Fore more information on MiToBo, visit
 *
 *    http://www.informatik.uni-halle.de/mitobo/
 *
 */

/* 
 * Most recent change(s):
 * 
 * $Rev$
 * $Date$
 * $Author$
 * 
 */

package de.unihalle.informatik.MiToBo.core.datatypes;

import java.util.*;

import de.unihalle.informatik.Alida.admin.annotations.ALDMetaInfo;
import de.unihalle.informatik.Alida.admin.annotations.ALDMetaInfo.ExportPolicy;

/**
 * This class implements the nodes of class 'Tree'. The recursive structure of a
 * tree is implicitly given by the list of children of each node that again are
 * TreeNodes on their own.
 * 
 * @see MTBTree
 * @see MTBTreeNodeData
 * 
 * @author Birgit Möller
 */
@ALDMetaInfo(export=ExportPolicy.ALLOWED)
public class MTBTreeNode {

	/**
	 * Data object associated with the given node.
	 */
	protected MTBTreeNodeData dataObject;

	/**
	 * List of nodes that are childs of the given node.
	 */
	protected Vector<MTBTreeNode> childs;

	/**
	 * Parent node
	 */
	protected MTBTreeNode parent;

	/**
	 * Constructor for a TreeNode.
	 * 
	 * @param object	Data object associated with the node.
	 */
	public MTBTreeNode(MTBTreeNodeData object) {
		this.childs = new Vector<MTBTreeNode>();
		this.dataObject = object;
		this.parent = null;
		// add reference to node to data object
		this.dataObject.setNode(this);
	}

	/**
	 * Returns a reference to the data associated with the node.
	 * 
	 * @return Reference to the node's data object.
	 */
	public MTBTreeNodeData getData() {
		return this.dataObject;
	}

	/**
	 * Add a new child to the node.
	 * <p>
	 * Sets the parent reference of the child to this node.
	 * 
	 * @param t	Node to be added as child to this node.
	 */
	public void addChild(MTBTreeNode t) {
		this.childs.addElement(t);
		t.parent = this;
	}

	/**
	 * Remove child node.
	 * 
	 * @param t	Node to be removed.
	 */
	public void removeChild(MTBTreeNode t) {
		if (this.childs.remove(t)) {
			t.parent = null;
		}
	}

	/**
	 * Returns Vector with child nodes.
	 * 
	 * @return Vector of child nodes.
	 */
	public Vector<MTBTreeNode> getChilds() {
		return this.childs;
	}

	/**
	 * Set the parent of this node.
	 * <p>
	 * Also adds this node to the children of the parent.
	 * 
	 * @param t		Parent node.
	 */
	public void setParent(MTBTreeNode t) {
		this.parent = t;
		t.childs.add(this);
	}

	/**
	 * Get the parent of this node.
	 * 
	 * @return Parent node.
	 */
	public MTBTreeNode getParent() {
		return this.parent;
	}

	/**
	 * Recursively prints the data contained in the nodes of the tree.
	 * <p>
	 * The method requires all data objects to be derived from 
	 * {@link TreeNodeData} and implement the function printData().
	 */
	public void printData() {

		// print data for the root node
		this.dataObject.printData();

		// if there exist childs, continue recursively
		if (this.childs.size() > 0)
			System.out.println("--> Children = ");
		for (int i = 0; i < this.childs.size(); ++i)
			this.childs.get(i).printData();
	}
}
