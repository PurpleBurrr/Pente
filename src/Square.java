import java.awt.Color;
import java.awt.Graphics;


public class Square {
	
	private int xLoc, yLoc;
	private int sWidth;
	private Color boardSquareColor = new Color(153,50,204);
	private Color crossHairColor = Color.black; //wood color
	private int squareState = PenteMain.EMPTY;
	
	//added for capture
	private int myRow, myCol;
	
	public Square(int x, int y, int w, int r, int c) {
		xLoc=x;
		yLoc=y;
		sWidth=w;
		myRow=r;
		myCol=c;
	}
	
	public void drawMe(Graphics g){
		g.setColor(boardSquareColor);
		g.fillRect(xLoc,  yLoc,  sWidth,  sWidth);
		g.setColor(crossHairColor);
		
		//cross hair
		g.setColor(crossHairColor);
		g.drawLine(xLoc+(int)(sWidth/2), yLoc, xLoc+(int)(sWidth/2), yLoc+sWidth);
		g.drawLine(xLoc, yLoc+(int)(sWidth/2), xLoc+sWidth, yLoc+(int)(sWidth/2));
		
		/*g.setColor(new Color(153,50,204));
		g.drawRect(xLoc, yLoc, sWidth, sWidth);*/
		
		if(squareState==PenteMain.BLACK){
			g.setColor(Color.BLACK);
			g.fillOval(xLoc+3, yLoc+3, sWidth-6, sWidth-6);
		}
		if(squareState==PenteMain.WHITE){
			g.setColor(Color.WHITE);
			g.fillOval(xLoc+3, yLoc+3, sWidth-6, sWidth-6);
		}
	}
	
	public void setState(int newState){
		squareState=newState;
	}
	public int getState(){
		return squareState;
	}
	
	public int getRow(){
		return myRow;
	}
	public int getCol(){
		return myCol;
	}
	
	//checks a mouseclick to see if it's inside a square
	public boolean youClickedMe(int mouseX, int mouseY){
		boolean squareClicked = false;
		
		if (mouseX>xLoc&&mouseX<xLoc+sWidth && mouseY>yLoc&&mouseY<yLoc+sWidth){
			squareClicked=true;
		}
		return squareClicked;
	}

}
