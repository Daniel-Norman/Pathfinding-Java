package com.DanielNorman.Pathfinding;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.DanielNorman.Pathfinding.Node.Type;

@SuppressWarnings("serial")
public class GraphicsPanel extends JPanel {
	public static final int SQUARE_SIZE = 10;
	public Graph graph;
	
	public GraphicsPanel(Graph g)
	{
		graph = g;
		
		graph.setStart(1, 10);
		graph.setDestination(35, 29);


		graph.setEdges();
		
		graph.visitNodes();
		
	}
	
	public void handleMouseButton(int button, int x, int y, int modifier)
	{
		x /= SQUARE_SIZE;
		y /= SQUARE_SIZE;
		if (x < 0 || y < 0 || x >= graph.MAP_SIZE || y >= graph.MAP_SIZE) return;

		if (button == MouseEvent.BUTTON1 && (modifier & 3) == MouseEvent.CTRL_MASK) //Set start tile
		{
			graph.start.type = Type.Normal;
			graph.setStart(x, y);
		}
		else if (button == MouseEvent.BUTTON3 && (modifier & 3) == MouseEvent.CTRL_MASK) //Set destination tile
		{
			graph.destination.type = Type.Normal;
			graph.setDestination(x, y);
		}
		else if (button == MouseEvent.BUTTON1 && graph.map[x][y].type != Type.Wall) //Set wall tile
		{
			graph.setWall(x, y, true);
		}
		else if (button == MouseEvent.BUTTON3 && graph.map[x][y].type == Type.Wall) //Set normal tile
		{
			graph.setWall(x, y, false);
		}
		else return; //No need to reset and redraw everything 
		
		updateGraph();
	}
	
	public void updateGraph()
	{
		graph.resetGraph();
		graph.setEdges();
		graph.visitNodes();
		repaint();
	}
	
	@Override
    protected void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	
    	for (int x = 0; x < graph.MAP_SIZE; ++x)
    	{
    		for (int y = 0; y < graph.MAP_SIZE; ++y)
    		{
    			switch (graph.map[x][y].type)
    			{
    			case Wall:
    				g.setColor(Color.BLACK);
    				break;
    			case Start:
    				g.setColor(Color.GREEN);
    				break;
    			case Destination:
    				g.setColor(Color.RED);
    				break;
    			case Path:
    				g.setColor(Color.CYAN);
    				break;
    			}
    			if (graph.map[x][y].type != Type.Normal)
    			{
    				g.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    			}
    		}
    	}

    	
    	
    	
    	g.setColor(Color.BLACK);
    	for (int x = 0; x < getBounds().width; x += SQUARE_SIZE) g.drawLine(x, 0, x, getBounds().height);
    	for (int y = 0; y < getBounds().height; y += SQUARE_SIZE) g.drawLine(0, y, getBounds().width, y);
    }
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}
}
