

// Skeletal program for the "Image Histogram" assignment
// Written by:  Minglun Gong

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

// Main class
public class ImageHistogram extends Frame implements ActionListener {
	BufferedImage input;
	int width, height;
	TextField texRad, texThres;
	ImageCanvas source, target;
	PlotCanvas plot;
	// Constructor
	public ImageHistogram(String name) {
		super("Image Histogram");
		// load image
		try {
			input = ImageIO.read(new File(name));
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
		width = input.getWidth();
		height = input.getHeight();
		// prepare the panel for image canvas.
		Panel main = new Panel();
		source = new ImageCanvas(input);
		plot = new PlotCanvas();
		target = new ImageCanvas(input);
		main.setLayout(new GridLayout(1, 3, 10, 10));
		main.add(source);
		main.add(plot);
		main.add(target);
		// prepare the panel for buttons.
		Panel controls = new Panel();
		Button button = new Button("Display Histogram");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Stretch");
		button.addActionListener(this);
		controls.add(button);
		controls.add(new Label("Cutoff fraction:"));
		texThres = new TextField("10", 2);
		controls.add(texThres);
		button = new Button("Aggressive Stretch");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Histogram Equalization");
		button.addActionListener(this);
		controls.add(button);
		// add two panels
		add("Center", main);
		add("South", controls);
		addWindowListener(new ExitListener());
		setSize(width*2+400, height+100);
		setVisible(true);
	}
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	// Action listener for button click events
	public void actionPerformed(ActionEvent e) {
            if ( ((Button)e.getSource()).getLabel().equals("Aggressive Stretch") ) {
                // get image histogram
  
                int red=0, green=0, blue=0; 
                int[] rH = new int[256];
                int[] gH = new int[256];
                int[] bH = new int[256];
                for ( int y=0, i=0 ; y<height ; y++ ) {
                    for ( int x=0 ; x<width ; x++, i++ ) {
                        Color clr = new Color(source.image.getRGB(x, y));
                        red = clr.getRed();
                        green = clr.getGreen();
                        blue = clr.getBlue();
                        rH[red]++;
                        gH[green]++;
                        bH[blue]++;
                    }
                }
                // get cut off value
                float cutoff = Float.valueOf(texThres.getText());              
                float cP = cutoff / 100 * 256;
                int cutoffPercent = (int) cP;
                
                
                // find min/max for red, green and blue channels
                int rMin = cutoffPercent;
                int rMax = 255 - cutoffPercent;
                while (rH[rMin] == 0) {
                    rMin++;
                }
                while (rH[rMax] == 0) {
                    rMax--;
                }
                int gMin = cutoffPercent;
                int gMax = 255 - cutoffPercent;
                while (gH[gMin] == 0){
                    gMin++;
                }
                while (gH[gMax] == 0) {
                    gMax--;
                }    
                int bMin = cutoffPercent;
                int bMax = 255 - cutoffPercent;
                while (bH[bMin] == 0){
                    bMin++;
                }
                while (bH[bMax] == 0) {
                    bMax--;
                }
                
                System.out.println(rMin + " " + rMax + " " + gMin + " " + gMax + " " + bMin + " " + bMax);
                
                // apply aggressive stretch
                for ( int y=0, i=0 ; y<height ; y++ ) {
                    for ( int x=0 ; x<width ; x++, i++ ) {
                        Color clr = new Color (source.image.getRGB(x, y));
                        red = (clr.getRed() - rMin) * 256 / (rMax - rMin);
                        green = (clr.getGreen() - gMin) * 256 / (gMax - gMin);
                        blue = (clr.getBlue() - bMin) * 256 / (bMax - bMin);
                        target.image.setRGB(x,y, red<<16 | green<<8 | blue);
                    }
                }                 
                target.repaint();
            }
            
            if ( ((Button)e.getSource()).getLabel().equals("Histogram Stretch") ) {
                // get histogram
                int red=0, green=0, blue=0; 
                int[] rH = new int[256];
                int[] gH = new int[256];
                int[] bH = new int[256];
                for ( int y=0, i=0 ; y<height ; y++ ) {
                    for ( int x=0 ; x<width ; x++, i++ ) {
                        Color clr = new Color(source.image.getRGB(x, y));
                        red = clr.getRed();
                        green = clr.getGreen();
                        blue = clr.getBlue();
                        rH[red]++;
                        gH[green]++;
                        bH[blue]++;
                    }
                }
                // find min/max values for r,g and b channels
                int rMin = 0;
                int rMax = 255;
                while (rH[rMin] == 0){
                    rMin++;
                }
                while (rH[rMax] == 0) {
                    rMax--;
                }
                int gMin = 0;
                int gMax = 255;
                while (gH[gMin] == 0){
                    gMin++;
                }
                while (gH[gMax] == 0) {
                    gMax--;
                }
                int bMin = 0;
                int bMax = 255;
                while (bH[bMin] == 0){
                    bMin++;
                }
                while (bH[bMax] == 0) {
                    bMax--;
                }
                // apply histogram stretch
                for ( int y=0, i=0 ; y<height ; y++ ) {
                    for ( int x=0 ; x<width ; x++, i++ ) {
                      Color clr = new Color (source.image.getRGB(x, y));
                      red = (clr.getRed() - rMin) * 256 / (rMax - rMin);
                      green = (clr.getGreen() - gMin) * 256 / (gMax - gMin);
                      blue = (clr.getBlue() - bMin) * 256 / (bMax - bMin);
                     target.image.setRGB(x,y, red<<16 | green<<8 | blue);
                    }
                }
                target.repaint();

            }
            
            if ( ((Button)e.getSource()).getLabel().equals("Histogram Equalization") ) {
                int red = 0, green = 0, blue = 0;
                int[] rH = new int[256];
                int[] gH = new int[256];
                int[] bH = new int[256];
                for (int y = 0, i = 0; y < height; y++) {
                    for (int x = 0; x < width; x++, i++) {
                       Color clr = new Color(source.image.getRGB(x, y));
                       red = clr.getRed();
                       green = clr.getGreen();
                       blue = clr.getBlue();
                       rH[red]++;
                       gH[green]++;
                       bH[blue]++;
                    }
                }
                float[] rHn = new float[256];
                float[] gHn = new float[256];
                float[] bHn = new float[256];
                for (int i = 0; i <= 255; i++) {
                    rHn[i] = (float)rH[i] / (width * height);
                    gHn[i] = (float)gH[i] / (width * height);
                    bHn[i] = (float)bH[i] / (width * height);
                }
                
            }
                
            if ( ((Button)e.getSource()).getLabel().equals("Display Histogram") ) {
		int red = 0, green = 0, blue = 0;
                int[] rH = new int[256];
                int[] gH = new int[256];
                int[] bH = new int[256];
                for (int y = 0, i = 0; y < height; y++) {
                    for (int x = 0; x < width; x++, i++) {
                       Color clr = new Color(source.image.getRGB(x, y));
                       red = clr.getRed();
                       green = clr.getGreen();
                       blue = clr.getBlue();
                       rH[red]++;
                       gH[green]++;
                       bH[blue]++;
                    }
                }
                float[] rHn = new float[256];
                float[] gHn = new float[256];
                float[] bHn = new float[256];
                for (int i = 0; i <= 255; i++) {
                    rHn[i] = (float)rH[i] / (width * height);
                    gHn[i] = (float)gH[i] / (width * height);
                    bHn[i] = (float)bH[i] / (width * height);
                }

                for (int i = 1; i <= 255; i++) {
                    plot.drawLineSegment(Color.RED, i - 1, rH[i - 1], i, rH[i]);
                    plot.drawLineSegment(Color.GREEN, i - 1, gH[i - 1], i, gH[i]);
                    plot.drawLineSegment(Color.BLUE, i-1, bH[i-1], i, bH[i]);
                }                
            }
	}
	public static void main(String[] args) {
		new ImageHistogram(args.length==1 ? args[0] : "signal_hill.png");
	}
}

// Canvas for plotting histogram
class PlotCanvas extends Canvas {
	// lines for plotting axes and mean color locations
	LineSegment x_axis, y_axis;
	LineSegment red, green, blue;
        ArrayList<LineSegment> redList = new ArrayList();
        ArrayList<LineSegment> greenList = new ArrayList();
        ArrayList<LineSegment> blueList = new ArrayList();
	boolean showMean = false;
        boolean showHistogram = false;

	public PlotCanvas() {
		x_axis = new LineSegment(Color.BLACK, -10, 0, 256+10, 0);
		y_axis = new LineSegment(Color.BLACK, 0, -10, 0, 200+10);
	}
	// set mean image color for plot
	public void setMeanColor(Color clr) {
		red = new LineSegment(Color.RED, clr.getRed(), 0, clr.getRed(), 100);
		green = new LineSegment(Color.GREEN, clr.getGreen(), 0, clr.getGreen(), 100);
		blue = new LineSegment(Color.BLUE, clr.getBlue(), 0, clr.getBlue(), 100);
		showMean = true;
		repaint();
	}
        public void drawLineSegment(Color clr, int x0, int y0, int x1, int y1) {
            if (clr.equals(Color.RED)) {
            redList.add(new LineSegment(clr, x0, y0, x1, y1));
        }
        if (clr.equals(Color.BLUE)) {
            blueList.add(new LineSegment(clr, x0, y0, x1, y1));
        }
        if (clr.equals(Color.GREEN)) {
            greenList.add(new LineSegment(clr, x0, y0, x1, y1));
        }

        showHistogram = true;
        repaint();
        }
	// redraw the canvas
	public void paint(Graphics g) {
		// draw axis
		int xoffset = (getWidth() - 256) / 2;
		int yoffset = (getHeight() - 200) / 2;
		x_axis.draw(g, xoffset, yoffset, getHeight());
		y_axis.draw(g, xoffset, yoffset, getHeight());
		if ( showHistogram ) {
	   // for (int i = 0; i < redList.size(); i++) {
           //     LineSegment line = redList.get(i);
           //     line.draw(g, xoffset, yoffset, getHeight());
           // }
            for (int i = 0; i < greenList.size(); i++) {
                LineSegment line = greenList.get(i);
                line.draw(g, xoffset, yoffset, getHeight());
            }

            //for (int i = 0; i < blueList.size(); i++) {
            //    LineSegment line = blueList.get(i);
            //    line.draw(g, xoffset, yoffset, getHeight());
           // }
		}
	}
}

// LineSegment class defines line segments to be plotted
class LineSegment {
	// location and color of the line segment
	int x0, y0, x1, y1;
	Color color;
	// Constructor
	public LineSegment(Color clr, int x0, int y0, int x1, int y1) {
		color = clr;
		this.x0 = x0; this.x1 = x1;
		this.y0 = y0; this.y1 = y1;
	}
	public void draw(Graphics g, int xoffset, int yoffset, int height) {
		g.setColor(color);
		g.drawLine(x0+xoffset, height-y0-yoffset, x1+xoffset, height-y1-yoffset);
	}
}