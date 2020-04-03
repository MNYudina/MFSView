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
public class JMy4Panel extends JPanel {
	ArrayList<Pair<Integer>> pairsList;

	public JMy4Panel(ArrayList<Pair<Integer>> pairsList) {
		super();
		this.pairsList = pairsList;
		//System.out.println(pairsList.size());
	}

	protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int size=40;
	        int delta=6;
	        for (Pair<Integer> pair : pairsList) {
	        	
	        	int i=pair.getFirst();
	        	int x1=(i%2)*size+5;
				int y1=(i<1.5)?5:(size+5);
	        	
				int j=pair.getSecond();
				int x2=j%2*size+5;
				int y2=(j<1.5)?5:(size+5);
		       
				g.drawLine(x1,y1, x2, y2);

				
				//if(pairsList.size()>11)System.out.println("i="+i+" j="+j+""+x1+","+y1+";;"+x2+","+y2);
				
				if((y1==y2)&&(x2>x1)) 
		        {
		        	g.drawLine(x2,y2, x2-delta, y2-delta/2);
		        	g.drawLine(x2,y2, x2-delta, y2+delta/2);
		        }
				else if((y1==y2)&&(x1>x2)) {
			        	g.drawLine(x2,y2, x2+delta, y2-delta/2);
			        	g.drawLine(x2,y2, x2+delta, y2+delta/2);

					}
				else if((y1>y2)&&(x1==x2)) {
		        	g.drawLine(x2,y2, x2+delta/2, y2+delta);
		        	g.drawLine(x2,y2, x2-delta/2, y2+delta);
		        	

				}
				else if((y1<y2)&&(x1==x2)) {
		        	g.drawLine(x2,y2, x2+delta/2, y2-delta);
		        	g.drawLine(x2,y2, x2-delta/2, y2-delta);

				}
				else if((x1>x2)&&(y1>y2)) {
		        	g.drawLine(x2,y2, x2+delta, y2+delta/2);
		        	g.drawLine(x2,y2, x2+delta/2, y2+delta);
				}
				else if((x1<x2)&&(y2>y1)) {
		        	g.drawLine(x2,y2, x2-delta, y2-delta/2);
		        	g.drawLine(x2,y2, x2-delta/2, y2-delta);

				}
				else if((x2>x1)&&(y2<y1)) {
		        	g.drawLine(x2,y2, x2-delta, y2+delta/2);
		        	g.drawLine(x2,y2, x2-delta/2, y2+delta);
				
				}
				else if((x1>x2)&&(y1<y2)) {
		        	g.drawLine(x2,y2, x2+delta/2, y2-delta);
		        	g.drawLine(x2,y2, x2+delta, y2-delta/2);

				}
			}

	    };
}
