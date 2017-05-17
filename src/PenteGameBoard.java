import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class PenteGameBoard extends JPanel implements MouseListener{
	
	private int bWidthPixels;
	private int bWidthSquares;
	private int bSquareWidth;
	private int currentTurn=PenteMain.BLACK;
	
	private Square [] [] theBoard;
	
	//counting captures
	private int whiteCaptures=0;
	private int blackCaptures=0;
	
	Shan computerMoveGenerator=null;
	boolean playingAgainstShan = false;
	int shansStoneColor;
	
	public PenteGameBoard(int bWPixel, int bWSquares){
		bWidthPixels=bWPixel;
		bWidthSquares=bWSquares;
		//compute the width of the bSquares
		bSquareWidth=(int)(Math.ceil(bWidthPixels/bWidthSquares))+1;
		
		this.setSize(bWidthPixels,bWidthSquares);
		this.setBackground(new Color(153,50,204));
		
		theBoard = new Square[bWidthSquares][bWidthSquares];
		
		//fill squares
		for (int row=0; row<bWSquares; ++row){
			for (int col=0; col<bWSquares; ++col){
				theBoard [row][col]=new Square((col*bSquareWidth), 
						            (row*bSquareWidth), 
						            bSquareWidth, row, col);
			}
		}
		
		
		//set the first stone
		theBoard[(int)(bWidthSquares/2)][(int)(bWidthSquares/2)].setState(PenteMain.BLACK);
		String computerAnswer=JOptionPane.showInputDialog("Play against Shan? Y/N");
		if(computerAnswer.equals("Y")||computerAnswer.equals("y")||computerAnswer.equals("yes")
				||computerAnswer.equals("Yes")){
			computerMoveGenerator = new Shan(this, currentTurn);
			playingAgainstShan=true;
			shansStoneColor=currentTurn;
		}
		
		//change the turn
		this.changeTurn();
		//activate mouselistener
		this.addMouseListener(this);
	}
	
	//overrides PaintComponent in JPanel
	public void paintComponent(Graphics g){
		g.setColor(new Color(153,50,204));
		g.fillRect(0, 0, bWidthPixels, bWidthPixels);
		
		for (int row=0; row<bWidthSquares; ++row){
			for (int col=0; col<bWidthSquares; ++col){
				theBoard [row][col].drawMe(g);;
			}
		}
		
	}
	
	public void changeTurn(){
		if (currentTurn==PenteMain.BLACK){
			currentTurn=PenteMain.WHITE;
		}else if (currentTurn==PenteMain.WHITE){
				currentTurn=PenteMain.BLACK;
			}
		}

	@Override
	public void mouseClicked(MouseEvent e) {
		playGame(e);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void playGame(MouseEvent e){
		Square s=findSquare(e.getX(), e.getY());
		if(s!=null){
		  if(s.getState()==PenteMain.EMPTY){
			  this.doPlay(s);
			  if(playingAgainstShan=true && currentTurn==shansStoneColor){
				  Square cs = computerMoveGenerator.doComputerMove(s.getRow(), s.getCol());
				  this.doPlay(cs);
				  this.requestFocus();
			  }
			  
		  }else{
			  JOptionPane.showMessageDialog(null, "You can't click here.");
		  }
		}else{
			JOptionPane.showMessageDialog(null, "You didn't click on a square.");
		}
	}
	
	public void doPlay(Square s){
		s.setState(currentTurn);
		this.repaint();
		this.checkForCaptures(s);
		this.checkForWinOnCaptures();
		this.checkForWin2(s);
		this.changeTurn();
	}
	public Square findSquare(int mouseX, int mouseY){
		Square clickSquare=null;
		//run thtough all of the squares and call boolean
		for (int row=0; row<bWidthSquares; ++row){
			for (int col=0; col<bWidthSquares; ++col){
				if (theBoard[row][col].youClickedMe(mouseX, mouseY)==true){
					clickSquare = theBoard[row][col];
				}
		    }
	    }
		return clickSquare;
    }
	
	public void checkForCaptures(Square s){
		
		int sRow=s.getRow();
		int sCol=s.getCol();
		int theOpposite = this.getTheOppositeState(s);
		
		for (int dy=-1; dy<=1; ++dy){
			//safe in vertical direction
			if ((dy>0 && sRow<bWidthSquares-3)||(dy<0 && sRow>=3)||dy==0){
				for (int dx=-1; dx<=1; ++dx){
					//safe in horizontal direction
					if((dx>0 && sCol<bWidthSquares-3)||(dx<0&&sCol>=3)||dx==0){
						if(theBoard[sRow+(1*dy)][sCol+(1*dx)].getState()==theOpposite){
							if(theBoard[sRow+(2*dy)][sCol+(2*dx)].getState()==theOpposite){
								if(theBoard[sRow+(3*dy)][sCol+(3*dx)].getState()==currentTurn){
									System.out.println("We had a capture!");
									this.takeStones(sRow+(1*dy), sCol+(1*dx), sRow+(2*dy), sCol+(2*dx), currentTurn);
									repaint();
								}
							}
						 }
			         }
				 }
			 }		
		}
	}

	
	public int getTheOppositeState(Square s){
		if (s.getState()==PenteMain.BLACK){
			return PenteMain.WHITE;
		}else{
			return PenteMain.BLACK;
		}
	}
	
	public void takeStones(int r1, int c1, int r2, int c2, int taker){
		//to clear stones
		theBoard[r1][c1].setState(PenteMain.EMPTY);
		theBoard[r2][c2].setState(PenteMain.EMPTY);
		
		if (taker==PenteMain.BLACK){
			++blackCaptures;
		}else{
			++whiteCaptures;
		}
	}
	
	public void checkForWinOnCaptures(){
		if(blackCaptures>=5){
			JOptionPane.showMessageDialog(null, "Black wins with "+blackCaptures+ " captures!");
		}
		if(whiteCaptures>=5){
			JOptionPane.showMessageDialog(null, "White wins with "+whiteCaptures+ " captures!");
		}
	}
	
	public void checkForWin(Square s){
		int sRow=s.getRow();
		int sCol=s.getCol();
		this.repaint();
		
		
		for (int dy=-1; dy<=1; ++dy){
			//vertical direction
			if ((dy>0 && sRow<bWidthSquares-4)||(dy<0 && sRow>=4)||dy==0){
				
				for (int dx=-1; dx<=1; ++dx){
					if(!(dx==0&&dy==0)){
						//horizontal direction
						if((dx>0 && sCol<bWidthSquares-4)||(dx<0&&sCol>=4)||dx==0){
							//four more things like you
							if(theBoard[sRow+(1*dy)][sCol+(1*dx)].getState()==currentTurn)
							if(theBoard[sRow+(2*dy)][sCol+(2*dx)].getState()==currentTurn)
							if(theBoard[sRow+(3*dy)][sCol+(3*dx)].getState()==currentTurn)
							if(theBoard[sRow+(4*dy)][sCol+(4*dx)].getState()==currentTurn)
							this.weHaveAWinner();
							repaint();
						}
				     }
				}
			}	
		}
	}
	
	public void checkForWin2(Square s){
		boolean done = false;
		int[] myDys = {-1, 0, 1};
		int whichDy = 0;
		
		while (!done&& whichDy<3){
			if (checkForWinAllInOne(s, myDys[whichDy], 1 )== true){
				weHaveAWinner();
				done=true;
			}
			whichDy++;
		}
		if (!done){
			if (checkForWinAllInOne(s, 1, 0)==true){
				weHaveAWinner();
			}
		}
	}
	
	public boolean checkForWinAllInOne(Square s, int dy, int dx){
		boolean isThereAWin = false;
		int sRow = s.getRow();
		int sCol = s.getCol();
		//for a right check and left...
		int howManyRight=0;
		int howManyLeft=0;
		   //loop to check right
		int step=1;
		while((sCol+step<bWidthSquares)&&(sRow+(step*dy)<bWidthSquares) &&
				(sCol+(step*dx)>=0 )&& (sRow+(step*dy)>=0 )&&
				(theBoard[sRow+(step*dy)][sCol+(step*dx)].getState()==currentTurn)){
			howManyRight++; 
			step++;//keep going
		}
		   //loop to check left
		step=1;
		while((sCol-(step*dx)>=0) && (sRow-(step*dy)>=0)&&
				(sCol-step<bWidthSquares)&&(sRow-(step*dy)<bWidthSquares) &&
				(theBoard[sRow-(step*dy)][sCol-(step*dx)].getState()==currentTurn)){
			howManyLeft++;
			step++;
		}
		if((howManyRight+howManyLeft+1)>=5){
			isThereAWin=true;
		}
		return isThereAWin;
	}
	
	public void weHaveAWinner(){
		String theWinner=null;
		if(currentTurn==PenteMain.WHITE){
			theWinner="Whitestone player!!!";
		}else{
			theWinner="Blackstone player!!!";
		}
		JOptionPane.showMessageDialog(null, "congratulations "+theWinner);
	}
	
	public int getBoardWidthInSquares(){
		return bWidthSquares;
	}
	public Square[][] getActualGameBoard(){
		return theBoard;
	}
			

}
	

