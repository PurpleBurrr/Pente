import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Shan {
	private PenteGameBoard myBoard;
	private int myStoneColor, opponentStoneColor;
	private int boardWidthSquares;
	private Square [][] theGameBoard;
	
	private boolean timeToMakeAMove = false;
	private boolean moveToMake = false;
	private int moveToDealWithRow;
	private int moveToDealWithCol;
	
	private ArrayList<OpponentGroup> groups1 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> groups2 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> groups3 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> groups4 = new ArrayList<OpponentGroup>();
	
	ShanHelper eel;
	
	public Shan(PenteGameBoard b, int stoneColor){
		myBoard=b;
		myStoneColor=stoneColor;
		this.setOpponentStoneColor();
		boardWidthSquares = b.getBoardWidthInSquares();
		theGameBoard = b.getActualGameBoard();
		JOptionPane.showMessageDialog(null, "Hey, Shan here. Ready to play.");
		eel = new ShanHelper(myBoard, opponentStoneColor);
		
	}
	public void setOpponentStoneColor(){
		if(myStoneColor==PenteMain.BLACK){
			opponentStoneColor=PenteMain.WHITE;
		}else{
			opponentStoneColor=PenteMain.BLACK;
		}
	}
	
	public Square doComputerMove(int lastMoveRow, int lastMoveCol){
		this.assessBoard(lastMoveRow, lastMoveCol);
		eel.assessBoard(lastMoveRow, lastMoveCol);
		Square nextMove=null;
		nextMove = eel.blockEveryone(eel.getEelGroup4(),4);
		if (nextMove==null){
			nextMove = this.blockEveryone(groups4, 4);
			if (nextMove == null){
				nextMove=this.blockEveryone(groups3, 3);
				if (nextMove == null){
					nextMove=this.captureATwo();
					if (nextMove == null){
						nextMove = this.blockEveryone(groups2, 2);
						if (nextMove == null){
							nextMove=this.makeRandomMove();
						}
					}
				}
			}
		}
		return nextMove;
	}
	
	
	private Square captureATwo(){
		Square nextMove=null;
		if (groups2.size()>0){
			boolean done = false;
			int groupIndex = 0;
			while (!done && groupIndex<groups2.size()){
				OpponentGroup currentGroup = groups2.get(groupIndex);
				Square e1 = groups2.get(groupIndex).getEnd1Square();
				Square e2 = groups2.get(groupIndex).getEnd2Square();
				
				if (e1.getState()==PenteMain.EMPTY && e2.getState()==myStoneColor){
					nextMove=e1;
				}
				if (e1.getState()==myStoneColor && e2.getState()==PenteMain.EMPTY){
					nextMove=e2;
				}
				groupIndex++;
				
			}
		}
		return nextMove;
	}
	public Square blockEveryone(ArrayList<OpponentGroup> whatGroup, int whatGroupSize){
		Square nextMove = null;
		if (whatGroup.size()>0){
			
			boolean done = false;
			int groupIndex=0;
			
			while (!done && groupIndex<whatGroup.size()){
				
				OpponentGroup curGroup = whatGroup.get(groupIndex);
				
				Square e1 =whatGroup.get(groupIndex).getEnd1Square();
				Square e2 =whatGroup.get(groupIndex).getEnd2Square();
				
				groupIndex++;
				
				if (curGroup.getInMiddleGroupStatus()==true){
					nextMove=curGroup.getInMiddleGroupSquare();
				}
				else{
				if (e1!= null && e1.getState() == PenteMain.EMPTY && e2!= null && e2.getState() == PenteMain.EMPTY) {
					int r = (int)(Math.random()*100);
					if (r>50){
						nextMove=e1;
					}else{
						nextMove=e2;
					}
					done=true;
				}
				else {

					if (whatGroupSize==4){
						if (e1!=null && e1.getState()==PenteMain.EMPTY){
							nextMove=e1;
							done=true;
							
						}if (e2!=null && e2.getState()==PenteMain.EMPTY){
							nextMove=e2;
							done=true; 
						}
					}
					
				}
				}
			}
		}
		return nextMove;
	}
	
	
	public Square makeRandomMove(){
		int newMoveRow, newMoveCol;
		do{
			newMoveRow=(int)(Math.random()*boardWidthSquares);
			newMoveCol=(int)(Math.random()*boardWidthSquares);
			
		}
		while (theGameBoard[newMoveRow][newMoveCol].getState()!=PenteMain.EMPTY);
		
			System.out.println("Shan wants to move to [" + newMoveRow+"]["+newMoveCol+"]");
			return theGameBoard[newMoveRow][newMoveCol];
		
	}
	
	public void assessBoard(int lastMoveRow, int lastMoveCol) {
		groups4.clear();
		groups3.clear();
		groups2.clear();
		groups1.clear();
		
		this.lookForGroupsHorizontal(lastMoveRow, lastMoveCol);
		this.lookForGroupsVertical(lastMoveRow, lastMoveCol);
		this.lookForGroupsDiagonalRight(lastMoveRow, lastMoveCol);
		this.lookForGroupsDiagonalLeft(lastMoveRow, lastMoveCol);
		
		this.doInMiddleCheck(3);
		this.doInMiddleCheck(4);
		
	}
	
	public void lookForGroupsHorizontal(int lastMoveRow, int lastMoveCol) {
		int curCol;
		//System.out.println("in LOOKFORGROUP");
		
		for (int row=0; row<boardWidthSquares; ++row){
			curCol=0;
			
			//skip over stones until you find one
			while (curCol<boardWidthSquares){
				Square newStart=findOpponentStartHorizontal(row, curCol);
				
				//when an opponent start is found
				if (newStart!=null){
					//make a newGroup
					OpponentGroup newGroup=new OpponentGroup(OpponentGroup.HORIZONTAL_GROUP);
					//add stone to array
					newGroup.addSquareToGroup(newStart);
					//System.out.println("ADDING a square to group.");
					
					//check edge
					int startRow=newStart.getRow();
					int startCol=newStart.getCol();
					if(startCol <= 0){
						newGroup.setEnd1Square(null);
					}else{
						newGroup.setEnd1Square(theGameBoard[startRow][startCol-1]);
					}
					
					//check to see if the current player move is this stone
					if (startRow == lastMoveRow && startCol==lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup();
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
					}
					
					//start getting neighbors
					startCol++;
					
					//you don't know where to end so here should be a while loop
					while(startCol<boardWidthSquares &&
							theGameBoard[startRow][startCol].getState()==this.opponentStoneColor){
						newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
						//now check if the second edge is currentMove
						if (startRow == lastMoveRow && startCol==lastMoveCol){
							newGroup.setCurrentMoveIsInThisGroup();
							newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
							
						}
						startCol++;
					}
					//set the second edge
					if(startCol >= boardWidthSquares){
						newGroup.setEnd2Square(null);
					}else{
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					}
					curCol = startCol;
					
					//finally add newGroupToGroupList
					this.addNewGroupToGroupLists(newGroup);
					
				}else{
					//this forces out of the loop
					curCol = boardWidthSquares;
					//System.out.println("SQUARE START IS NULL... I DIDNT FIND A SQUARE");
				}
			}
		}
		
	}
	
	public Square findOpponentStartHorizontal(int whatRow, int whatCol){
		Square opponentStart=null;
		boolean done = false;
		int currentCol=whatCol;
		
		while (!done && currentCol<boardWidthSquares){
			if (theGameBoard[whatRow][currentCol].getState()==this.opponentStoneColor){
				opponentStart = theGameBoard[whatRow][currentCol];
				done = true;
			}
			currentCol++;
		}
		return opponentStart;
	}
	
	
	public void lookForGroupsVertical(int lastMoveRow, int lastMoveCol) {
		int curRow;
		for (int col=0; col<boardWidthSquares; ++col){
			curRow=0;
			
			//skip over stones until you find one
			while (curRow<boardWidthSquares){
				Square newStart=findOpponentStartVertical(curRow, col);
				
				//when an opponent start is found
				if (newStart!=null){
					//make a newGroup
					OpponentGroup newGroup=new OpponentGroup(OpponentGroup.VERTICAL_GROUP);
					//add stone to array
					newGroup.addSquareToGroup(newStart);
					//System.out.println("ADDING a square to group.");
					
					//check edge
					int startRow=newStart.getRow();
					int startCol=newStart.getCol();
					if(startRow <= 0){
						newGroup.setEnd1Square(null);
					}else{
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol]);
					}
					
					//check to see if the current player move is this stone
					if (startRow == lastMoveRow && startCol==lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup();
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
					}
					
					//start getting neighbors
					startRow++;
					
					//you don't know where to end so here should be a while loop
					while(startRow<boardWidthSquares &&
							theGameBoard[startRow][startCol].getState()==this.opponentStoneColor){
						newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
						//now check if the second edge is currentMove
						if (startRow == lastMoveRow && startCol==lastMoveCol){
							newGroup.setCurrentMoveIsInThisGroup();
							newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
							
						}
						startRow++;
					}
					//set the second edge
					if(startRow >= boardWidthSquares){
						newGroup.setEnd2Square(null);
					}else{
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					}
					curRow = startRow;
					
					//finally add newGroupToGroupList
					this.addNewGroupToGroupLists(newGroup);
					
				}else{
					//this forces out of the loop
					curRow = boardWidthSquares;
					//System.out.println("SQUARE START IS NULL... I DIDNT FIND A SQUARE");
				}
			}
		}
		
	}
	
	
	public Square findOpponentStartVertical(int whatRow, int whatCol){
		Square opponentStart=null;
		boolean done = false;
		int currentRow=whatRow;
		
		while (!done && currentRow<boardWidthSquares){
			if (theGameBoard[currentRow][whatCol].getState()==this.opponentStoneColor){
				opponentStart = theGameBoard[currentRow][whatCol];
				done = true;
			}
			currentRow++;
		}
		return opponentStart;
	}
	
	
	
	public void lookForGroupsDiagonalRight(int lastMoveRow, int lastMoveCol){
		
		//down
		for(int row=0; row<boardWidthSquares; ++row){
			int curCol=0;
			int curRow=row;
			while(curCol<boardWidthSquares-row && curRow<boardWidthSquares){
				Square groupStart = findOpponentDiagonalRight(curRow, curCol, 0);
				if (groupStart!=null){
					OpponentGroup newGroup=new OpponentGroup(OpponentGroup.DIAG_R_GROUP);
					newGroup.addSquareToGroup(groupStart);
					//check edge
					int startRow=groupStart.getRow();
					int startCol=groupStart.getCol();
					if(startRow-1 >= 0 && startCol-1>=0){
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol-1]);
					}else{
						newGroup.setEnd1Square(null);
					}
					//check to see if the current player move is this stone
					if (startRow == lastMoveRow && startCol==lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup();
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
					//look for additional group members
					startCol++;
					startRow++;
					boolean done=false;
					
					//collects the length of opponent stones
					while(startCol<boardWidthSquares-row && startRow<boardWidthSquares && !done){
						if (theGameBoard[startRow][startCol].getState() == this.opponentStoneColor){
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
							if (startRow == lastMoveRow && startCol==lastMoveCol){
								newGroup.setCurrentMoveIsInThisGroup();
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
							}
							startCol++;
							startRow++;
						}else{
							done=true;
						}
					}
					//check other edges
					if (startRow<boardWidthSquares &&startCol<boardWidthSquares){
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					}else{
						newGroup.setEnd2Square(null);
					}
					//important to stop infinite loop
					curCol=startCol;
					curRow=startRow;
					//add group to list
					this.addNewGroupToGroupLists(newGroup);
				}else{
					curRow=boardWidthSquares;
				}
			}
		}
		
		//right
		for (int col=1; col<boardWidthSquares; ++col){
			int curCol=col;
			int curRow = 0;
			while(curRow<boardWidthSquares-col && curCol<boardWidthSquares){
				Square groupStart = findOpponentDiagonalRight(curRow, curCol, 0);
				if (groupStart!=null){
					OpponentGroup newGroup=new OpponentGroup(OpponentGroup.DIAG_R_GROUP);
					newGroup.addSquareToGroup(groupStart);
					int startRow=groupStart.getRow();
					int startCol=groupStart.getCol();
					if(startRow-1 >= 0 && startCol-1>=0){
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol-1]);
					}else{
						newGroup.setEnd1Square(null);
					}
					//check to see if the current player move is this stone
					if (startRow == lastMoveRow && startCol==lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup();
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
					//look for additional group members
					startCol++;
					startRow++;
					boolean done=false;
					while(startCol<boardWidthSquares && startRow<boardWidthSquares-col && !done){
						if (theGameBoard[startRow][startCol].getState() == this.opponentStoneColor){
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
							if (startRow == lastMoveRow && startCol==lastMoveCol){
								newGroup.setCurrentMoveIsInThisGroup();
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
							}
							startCol++;
							startRow++;
						}else{
							done=true;
						}
					}
					
					//check other edge
					if (startRow<boardWidthSquares && startCol<boardWidthSquares){
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					}else{
						newGroup.setEnd2Square(null);
					}
					curCol=startCol;
					curRow=startRow;
					this.addNewGroupToGroupLists(newGroup);
				}else{
					curCol=boardWidthSquares;
				}
			
			}
		}
	}
	
	public Square findOpponentDiagonalRight(int whatRow, int whatCol, int r){
		Square opponentStart= null;
		boolean done=false;
		int curCol=whatCol;
		int curRow=whatRow;
		
		while(!done && curCol<boardWidthSquares-r && curRow< boardWidthSquares){
			if (theGameBoard[curRow][curCol].getState()==opponentStoneColor){
				opponentStart = theGameBoard[curRow][curCol];
				done = true;
			}
			curRow++;
			curCol++;
		}
		return opponentStart;
	}
	

	
	public void lookForGroupsDiagonalLeft( int lastMoveRow, int lastMoveCol ){
		//Do Part 1 of the Diagonal... 
		for(int row = 0 ; row < boardWidthSquares; ++row ){
			int curCol = boardWidthSquares-1; 
			int curRow = row;
			while(curCol >= row  && curRow < boardWidthSquares) {
				Square groupStart = findOpponentDiagonalLeft(curRow,curCol);
				if( groupStart != null ) {
		//You have a start so set up a new group! 
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.DIAG_L_GROUP);
					newGroup.addSquareToGroup(groupStart); 
					int startRow = groupStart.getRow(); 
					int startCol = groupStart.getCol();
		// Check first edge 
					if(startRow - 1 >= 0 && startCol + 1 < boardWidthSquares) {
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol+1]);
					} else {
						newGroup.setEnd1Square(null);
					}
					if( startRow == lastMoveRow && startCol == lastMoveCol ){
						newGroup.setCurrentMoveIsInThisGroup(); 
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
					startCol--; 
					startRow++; 
					boolean done = false;
					while( startCol >= row && startRow < boardWidthSquares && !done){
						if(theGameBoard[startRow][startCol].getState() == this.opponentStoneColor ) {
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
							if( startRow == lastMoveRow && startCol == lastMoveCol ){
								newGroup.setCurrentMoveIsInThisGroup(); 
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
							}
							startRow++; startCol--;
						} else {
							done = true;
						}
					} 
					if(startRow< boardWidthSquares && startCol >=0) {
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					} else {
						newGroup.setEnd2Square(null);
					}
					curCol = startCol; 
					curRow = startRow; 
					this.addNewGroupToGroupLists(newGroup);
				} else {
					curRow = boardWidthSquares; curCol = row-1;
				}
			}
		}
		//Do Part 2 of the Diagonal 
		for(int col = boardWidthSquares-2 ; col >= 0; --col ){
			int curCol = col; int curRow = 0;
			while(curRow <= col  && curCol >= 0) {
			Square groupStart = findOpponentDiagonalLeft( curRow,curCol);
			if(groupStart != null){
				OpponentGroup newGroup = new OpponentGroup(OpponentGroup.DIAG_L_GROUP);
				newGroup.addSquareToGroup(groupStart); 
				int startRow = groupStart.getRow(); 
				int startCol = groupStart.getCol();
				if(startRow - 1 >= 0 && startCol + 1 < boardWidthSquares) {
					newGroup.setEnd1Square(theGameBoard[startRow-1][startCol+1]);
				} else {
					newGroup.setEnd1Square(null);
				}
				if( startRow == lastMoveRow && startCol == lastMoveCol ){
					newGroup.setCurrentMoveIsInThisGroup(); 
					newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
				}
				startCol--;
				startRow++; 
				boolean done = false;
				while( !done && startCol >=0  && startRow < boardWidthSquares ){
					if(theGameBoard[startRow][startCol].getState() == this.opponentStoneColor ) {
						newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
						if( startRow == lastMoveRow && startCol == lastMoveCol ){
							newGroup.setCurrentMoveIsInThisGroup(); 
							newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
						}
						startRow++; 
						startCol--;
					} else {
						done = true;
					}
				}
				if(startRow< boardWidthSquares && startCol>= 0) {
					newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
				} else {
					newGroup.setEnd2Square(null);
				}
				curCol = startCol; 
				curRow = startRow;
				this.addNewGroupToGroupLists(newGroup);
			} else {
				curCol = -1;
			}
		}
	}
}
	
	public Square findOpponentDiagonalLeft(int whatRow, int whatCol){
		Square opponentStart= null;
		boolean done=false;
		int curCol=whatCol;
		int curRow=whatRow;
		
		while(!done && curCol>=0 && curRow< boardWidthSquares){
			if (theGameBoard[curRow][curCol].getState()==opponentStoneColor){
				opponentStart = theGameBoard[curRow][curCol];
				done = true;
			}
			curRow++;
			curCol--;
		}
		return opponentStart;
	}
	
	
	public void addNewGroupToGroupLists(OpponentGroup ng){
		
		//System.out.println("HI in add group to list for " + ng.getGroupTypeText() + " group with " + ng.getGroupLength() +" stones");
		switch(ng.getGroupLength()){
		case 1:
			groups1.add(ng);
			break;
		case 2:
			System.out.println("I have an " + ng.getGroupTypeText() + " Group with 2 opponent stones.");
			groups2.add(ng);
			break;
		case 3:
			System.out.println("I have an " + ng.getGroupTypeText() + " Group with 3 opponent stones.");
			groups3.add(ng);
			break;
		case 4:
			System.out.println("I have an " + ng.getGroupTypeText() + " Group with 4 opponent stones.");
			groups4.add(ng);
			break;
		default:
			System.out.println("Something went wrong.");
			break;
		}
	}

	public void doInMiddleCheck( int groupSize ){
		for(int row = 0; row < boardWidthSquares; ++row){
			for(int col = 0; col < boardWidthSquares; ++col){ 
				if(theGameBoard[row][col].getState() == PenteMain.EMPTY){
					checkForBlockInMiddle(row, col, groupSize);
				}
			}
		}
	}
	
	public void checkForBlockInMiddle(int row, int col, int groupSize){
		boolean done = false; 
		int[] myDys = {-1, 0, 1};
		int whichDy = 0;  
		while(!done && whichDy < 3){
			checkForBlockInMiddleAllAround(row, col, groupSize, myDys[whichDy], 1 );
			whichDy++;
		} 
		checkForBlockInMiddleAllAround(row, col, groupSize, 1, 0 );
	}
	
	public void checkForBlockInMiddleAllAround(int row, int col, int groupSize, int dy, int dx){
		int sRow = row;
		int sCol = col;
		
		//for a right-check and left...
		int howManyRight = 0;
		int howManyLeft = 0;
		
		//loop to check right side of where stone s is
		int step = 1;
		while((sCol + (step * dx) < boardWidthSquares) && 
				(sRow + (step * dy) < boardWidthSquares) &&
				(sCol + (step * dx) >= 0) && 
				(sRow + (step * dy) >= 0) &&
				(theGameBoard[sRow + (step * dy)][sCol + (step * dx)].getState() == this.opponentStoneColor)){
			howManyRight++;
			step++;
		}
		//Moving Left....
		step = 1;
		while((sCol - (step * dx) >= 0) &&  
				(sRow - (step * dy) >= 0) &&
				(sCol - (step * dx) < boardWidthSquares) && 
				(sRow - (step * dy) < boardWidthSquares) &&
				(theGameBoard[sRow - (step * dy)][sCol - (step * dx)].getState() == this.opponentStoneColor)){
			howManyLeft++;
			step++;
		}
		
		
		if((howManyRight + howManyLeft) >= groupSize){
			//If you have this then you want to set Up an Opponent group for this
			System.out.println("For square at " + row + ", " + col + " we have group of size of " + (howManyRight + howManyLeft));
			OpponentGroup newGroup;
			if( groupSize == 4 ) {
				String middleGroupText = getMiddleGroupText(dx, dy, 4);
				newGroup = new OpponentGroup(OpponentGroup.MIDDLE_4_GROUP);
				newGroup.setGroupRanking(4);
				newGroup.setGroupLength(4); //in OG
				newGroup.setGroupTypeText(middleGroupText); //in OG
			} else {
				String middleGroupText = getMiddleGroupText(dx, dy, 3);
				newGroup = new OpponentGroup(OpponentGroup.MIDDLE_3_GROUP);
				newGroup.setGroupRanking(3);
				newGroup.setGroupLength(3); //add in OG
				newGroup.setGroupTypeText(middleGroupText); //add in OG
			}
			
			newGroup.setInMiddleGroup(true);
			newGroup.setInMiddleGroupSquare(theGameBoard[row][col]);
			this.addNewGroupToGroupLists(newGroup);		
		}	
	}
	
	
	
	public String getMiddleGroupText(int dx, int dy, int groupSize){
		String gs = ""; 
		if(groupSize == 4){
			gs="4"; 
		}else{ 
			gs="3"; 
		} 
		String theType = ""; 
		if(dx == 1){
			if(dy == 1) theType = "Diag Right"; 
			if(dy == 0) theType = "Horizontal"; 
			if(dy == -1) theType = "Diag Left";
		} else {
		theType = "Vertical";
		} 
		return "Middle " + gs + ": " + theType;
	}
	
	
	
}
