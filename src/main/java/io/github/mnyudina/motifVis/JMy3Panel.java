package io.github.mnyudina.motifVis;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.uci.ics.jung.graph.util.Pair;
/**
 * 
 * @author Gleepa
 *
 */
public class JMy3Panel extends JPanel {
	ArrayList<Pair<Integer>> pairsList;

	public JMy3Panel(ArrayList<Pair<Integer>> pairsList) {
		super();
		this.pairsList = pairsList;
		// System.out.println(pairsList.size());
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int size = 40;
		int delta = 6;
		for (Pair<Integer> pair : pairsList) {

			int i = pair.getFirst();

			int j = pair.getSecond();

			if (i == 0 && j == 1) {				//0,1
				int x2 = (size + 10) / 2;		int y2 = 5;
				g.drawLine(5, size + 5, x2, y2);
				g.drawLine(x2, y2, x2 - delta, y2 + delta );
				g.drawLine(x2, y2, x2 , y2 +2+ delta);

			} 
			else if (i == 1 && j == 0) {		//1,0
				int x2 =  5;		int y2 = size + 5;
				g.drawLine((size + 10) / 2, 5, x2, y2);
	        	g.drawLine(x2,y2, x2, y2-2-delta);
	        	g.drawLine(x2,y2, x2+delta, y2-delta);

			}
			
			else if (i == 1 && j == 2) {		//1,2
				int x2 = size + 5;		int y2 = size + 5;
				g.drawLine((size + 10) / 2, 5, x2, y2);
				g.drawLine(x2, y2, x2 - delta, y2 - delta );
				g.drawLine(x2, y2, x2 , y2 -2 -delta);

			} else if (i == 2 && j == 1) {		//2,1
				int x2 = (size + 10) / 2;	int y2 = 5;
				g.drawLine((size + 5), (size + 5), x2, y2);
				g.drawLine(x2, y2, x2 + delta, y2 + delta);
				g.drawLine(x2, y2, x2 , y2 + 2+delta);
			}
			
			else if (i == 0 && j == 2) {		//0,2
				int x2 = size + 5;	int y2 = size + 5;
				g.drawLine(5, size + 5, x2, y2);
				g.drawLine(x2, y2, x2 - delta, y2 - delta / 2);
				g.drawLine(x2, y2, x2 - delta, y2 + delta / 2);
			
			} else if (i == 2 && j == 0) {		 //2,0
				int x2 = 5;
				int y2 = size + 5;
				g.drawLine(size + 5, size + 5, x2, y2);
				g.drawLine(x2, y2, x2 + delta, y2 - delta / 2);
				g.drawLine(x2, y2, x2 + delta, y2 + delta / 2);

			} 
		}
		// if(pairsList.size()>11) System.out.println("=============");

	};

}
