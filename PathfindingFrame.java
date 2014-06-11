package com.DanielNorman.Pathfinding;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.DanielNorman.Pathfinding.Node.Type;

@SuppressWarnings("serial")
public class PathfindingFrame extends JFrame {
	private Graph graph;
	private int mouseButton = MouseEvent.NOBUTTON;

	private JPanel contentPane;
	private GraphicsPanel graphicsPanel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PathfindingFrame frame = new PathfindingFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PathfindingFrame() {
		setTitle("Pathfinding with Dijkstra");
		graph = new Graph();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 679, 456);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		graphicsPanel = new GraphicsPanel(graph);
		graphicsPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				graphicsPanel.handleMouseButton(mouseButton, e.getX(), e.getY(), e.getModifiers());
			}
		});
		graphicsPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseButton = e.getButton();
				graphicsPanel.handleMouseButton(mouseButton, e.getX(), e.getY(), e.getModifiers());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mouseButton = MouseEvent.NOBUTTON;
			}
		});
		graphicsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		graphicsPanel.setBounds(10, 10, 400, 400);
		contentPane.add(graphicsPanel);
		
		JTextArea txtrLeftclickDrag = new JTextArea();
		txtrLeftclickDrag.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtrLeftclickDrag.setWrapStyleWord(true);
		txtrLeftclickDrag.setEditable(false);
		txtrLeftclickDrag.setBackground(SystemColor.control);
		txtrLeftclickDrag.setText("Add wall:\tLeft click+drag\r\nDelete wall:\tRight click+drag\r\nAdd start:\tCtrl+left click\r\nAdd end:\tCtrl+right click");
		txtrLeftclickDrag.setBounds(420, 42, 215, 93);
		contentPane.add(txtrLeftclickDrag);
		
		final JCheckBox cutCornersCheckBox = new JCheckBox("Cut corners");
		cutCornersCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.graph.canCutCorners = cutCornersCheckBox.isSelected();
				graphicsPanel.updateGraph();
			}
		});
		cutCornersCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cutCornersCheckBox.setBounds(439, 142, 196, 27);
		contentPane.add(cutCornersCheckBox);
		
		final JCheckBox diagonalMovementCheckBox = new JCheckBox("Diagonal movement");
		diagonalMovementCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.graph.canGoDiagonal = diagonalMovementCheckBox.isSelected();
				graphicsPanel.updateGraph();
			}
		});
		diagonalMovementCheckBox.setSelected(true);
		diagonalMovementCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		diagonalMovementCheckBox.setBounds(439, 172, 196, 27);
		contentPane.add(diagonalMovementCheckBox);
		
		final JSlider heuristicSlider = new JSlider();
		heuristicSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				graphicsPanel.graph.heuristicWeight = heuristicSlider.getValue() * 0.25;
				graphicsPanel.updateGraph();
			}
		});
		heuristicSlider.setSnapToTicks(true);
		heuristicSlider.setValue(1);
		heuristicSlider.setToolTipText("");
		heuristicSlider.setPaintTicks(true);
		heuristicSlider.setMaximum(2);
		heuristicSlider.setPaintLabels(true);
		heuristicSlider.setMajorTickSpacing(1);
		heuristicSlider.setBounds(548, 225, 105, 45);
		contentPane.add(heuristicSlider);
		
		JLabel lblNewLabel = new JLabel("Heuristic weight:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(420, 225, 118, 46);
		contentPane.add(lblNewLabel);
		
		final JRadioButton manhattanRadioButton = new JRadioButton("Manhattan heuristic");
		manhattanRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.graph.isUsingManhattanHeuristic = manhattanRadioButton.isSelected();
				graphicsPanel.updateGraph();
			}
		});
		manhattanRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		manhattanRadioButton.setBackground(SystemColor.menu);
		manhattanRadioButton.setBounds(439, 321, 196, 27);
		contentPane.add(manhattanRadioButton);
		
		final JRadioButton geometricRadioButton = new JRadioButton("Geometric heuristic");
		geometricRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphicsPanel.graph.isUsingManhattanHeuristic = manhattanRadioButton.isSelected();
				graphicsPanel.updateGraph();
			}
		});
		geometricRadioButton.setSelected(true);
		geometricRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		geometricRadioButton.setBackground(new Color(240, 240, 240));
		geometricRadioButton.setBounds(439, 291, 196, 27);
		contentPane.add(geometricRadioButton);
		

		
		ButtonGroup group = new ButtonGroup();
	    group.add(geometricRadioButton);
	    group.add(manhattanRadioButton);
	    
	    JButton btnNewButton = new JButton("Reset walls");
	    btnNewButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		for (int i = 0; i < graphicsPanel.graph.MAP_SIZE; ++i)
	    		{
	    			for (int j = 0; j < graphicsPanel.graph.MAP_SIZE; ++j)
	    			{
	    				if (graphicsPanel.graph.map[i][j].type == Type.Wall) graphicsPanel.graph.map[i][j].type = Type.Normal;
	    			}
	    		}
	    		graphicsPanel.updateGraph();
	    	}
	    });
	    btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    btnNewButton.setBounds(476, 372, 118, 23);
	    contentPane.add(btnNewButton);
	}
}
