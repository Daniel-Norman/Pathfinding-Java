package com.DanielNorman.Pathfinding;

import java.util.ArrayList;

public class Node
{
	enum Type{ Normal, Wall, Start, Destination, Path };
	
	ArrayList<Edge> neighbors = new ArrayList<Edge>();
	Node previous = null;
	double distance = Integer.MAX_VALUE, heuristic = 0;
	Type type = Type.Normal;
	boolean visited = false;
	int x, y;
	
	public Node(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Node)
		{
			Node otherNode = (Node) other;
			return this.x == otherNode.x && this.y == otherNode.y;
		}
		return false;
	}
}
