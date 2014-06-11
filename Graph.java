package com.DanielNorman.Pathfinding;

import java.util.ArrayList;

import com.DanielNorman.Pathfinding.Node.Type;


public class Graph
{
	public final int MAP_SIZE = 40;
	
	Node[][] map = new Node[MAP_SIZE][MAP_SIZE];
	ArrayList<Node> unvisitedNodes = new ArrayList<Node>();
	Node start, destination, current;
	boolean canCutCorners = false;
	boolean canGoDiagonal = true;
	boolean isUsingManhattanHeuristic = false;
	double heuristicWeight = 0.25;
	
	public Graph()
	{
		for (int i = 0; i < MAP_SIZE; ++i)
		{
			for (int j = 0; j < MAP_SIZE; ++j)
			{
				map[i][j] = new Node(i, j);
				unvisitedNodes.add(map[i][j]);
			}
		}
	}
	
	public void resetGraph()
	{
		start.distance = 0;
		current = start;
		unvisitedNodes = new ArrayList<Node>();
		for (int i = 0; i < MAP_SIZE; ++i)
		{
			for (int j = 0; j < MAP_SIZE; ++j)
			{
				map[i][j].distance = Integer.MAX_VALUE;
				map[i][j].neighbors = new ArrayList<Edge>();
				map[i][j].previous = null;
				map[i][j].heuristic = 0;
				map[i][j].visited = false;
				
				unvisitedNodes.add(map[i][j]);
				if (start.equals(map[i][j])) setStart(i, j);
				if (destination.equals(map[i][j])) setDestination(i, j);
				if (map[i][j].type == Type.Path) map[i][j].type = Type.Normal;
			}
		}
	}
	
	public void setStart(int x, int y)
	{
		start = map[x][y];
		start.type = Type.Start;
		start.distance = 0;
		current = start;
	}
	
	public void setDestination(int x, int y)
	{
		destination = map[x][y];
		destination.type = Type.Destination;
	}
	
	public void setWall(int x, int y, boolean isWall)
	{
		if (x >= MAP_SIZE || y >= MAP_SIZE || x < 0 || y < 0) return;
		map[x][y].type = isWall ? Type.Wall : Type.Normal;
	}
	
	
	public void setEdges()
	{
		int destX = destination.x;
		int destY = destination.y;

		for (int i = 0; i < MAP_SIZE; ++i)
		{
			for (int j = 0; j < MAP_SIZE; ++j)
			{
				if (isUsingManhattanHeuristic) map[i][j].heuristic = Math.abs(destX - map[i][j].x) + Math.abs(destY - map[i][j].y);
				else map[i][j].heuristic = Math.sqrt(Math.pow((destX - map[i][j].x), 2) + Math.pow((destY - map[i][j].y), 2));

				Type wall = Type.Wall;
				if (j + 1 < MAP_SIZE && map[i][j + 1].type != wall)			//right node
					map[i][j].neighbors.add(new Edge(map[i][j + 1], 1));
				if (j > 0 && map[i][j - 1].type != wall)					//left node
					map[i][j].neighbors.add(new Edge(map[i][j - 1], 1));
				if (i + 1 < MAP_SIZE && map[i + 1][j].type != wall)			//below node
					map[i][j].neighbors.add(new Edge(map[i + 1][j], 1));
				if (i > 0 && map[i - 1][j].type != wall)					//above node
					map[i][j].neighbors.add(new Edge(map[i - 1][j], 1));

				if (!canGoDiagonal) continue;
				
				if (j + 1 < MAP_SIZE && i > 0 && map[i - 1][j + 1].type != wall &&
					(canCutCorners || (map[i- 1][j].type != wall && map[i][j + 1].type != wall)))		//right above node
					map[i][j].neighbors.add(new Edge(map[i - 1][j + 1], 1.414));
				if (j + 1 < MAP_SIZE && i + 1 < MAP_SIZE && map[i + 1][j + 1].type != wall &&
					(canCutCorners || (map[i + 1][j].type != wall && map[i][j + 1].type != wall)))	//right below node
					map[i][j].neighbors.add(new Edge(map[i + 1][j + 1], 1.414));
				if (j > 0 && i > 0 && map[i - 1][j - 1].type != wall &&
					(canCutCorners || (map[i - 1][j].type != wall && map[i][j - 1].type != wall)))	//left above node
					map[i][j].neighbors.add(new Edge(map[i - 1][j - 1], 1.414));
				if (j > 0 && i + 1 < MAP_SIZE && map[i + 1][j - 1].type != wall &&
					(canCutCorners || (map[i + 1][j].type != wall && map[i][j - 1].type != wall)))	//left below node
					map[i][j].neighbors.add(new Edge(map[i + 1][j - 1], 1.414));
			}
		}
	}
	
	public boolean visitNodes()
	{
		if (current == null) return false; //Return false if we can't reach our destination (no more nodes in the unvisited list)

		
		for (Edge edge : current.neighbors)
		{
			double distance = current.distance + edge.length + (edge.node.heuristic) * heuristicWeight;
			if (!edge.node.visited && distance < edge.node.distance) //If a new tentative distance is smaller than that neighbor's current saved distance, replace it
			{
				edge.node.distance = distance;
				edge.node.previous = current;
			}
		}

		current.visited = true;
		
		unvisitedNodes.remove(current); //Remove the current node from the unvisited list
		
		if (destination.visited)
		{
			for (Node path = destination.previous; path != start; path = path.previous)
				path.type = Type.Path; //Mark all nodes along the path from destination to start
			return true; //Return true because we've reached our destination
		}
		else
		{
			Node smallestNode = null;
			double smallestDistance = Integer.MAX_VALUE;
			for (Node node : unvisitedNodes)
			{
				if (node.distance < smallestDistance)
				{
					smallestNode = node;
					smallestDistance = smallestNode.distance;
				}
			}
			
			current = smallestNode; //Set our new current node as that smallest distance node
			return visitNodes(); //Visit nodes again
		}
	}
}
