import java.util.ArrayList;


public class OpponentGroup {
	
	public static final int HORIZONTAL_GROUP =1;
	public static final int VERTICAL_GROUP =2;
	public static final int DIAG_R_GROUP =3;
	public static final int DIAG_L_GROUP =4;
	
	public static final int MIDDLE_4_GROUP = -4;
	public static final int MIDDLE_3_GROUP = -3;
	
	private ArrayList<Square> groupList;
	private int groupLength = 0;
	private int groupRanking = 0;
	private Square end1Square = null;
	private Square end2Square = null;
	private boolean inMiddleGroup=false;
	private Square inMiddleSquare = null;
	
	//handles issue of whether the current move is a part of the group
	private int groupType;
	private String groupTypeText;
	
	private boolean currentMoveIsInGroup=false;
	private int currentMoveArrayListLocation=-1;
	
	
	
	
	public OpponentGroup(int gt){
		groupList = new ArrayList<Square>();
		groupType = gt;
		this.setGroupTypeToString();
		
	}
	
	
	
	
	public void addSquareToGroup(Square whichSquare){
		groupList.add(whichSquare);
		groupLength++;
		groupRanking++;
	}
	
	public void setEnd1Square(Square whatSquare){
		end1Square = whatSquare;
	}
	
	public void setEnd2Square(Square whatSquare){
		end2Square = whatSquare;
	}
	
	public ArrayList<Square> getGroupList(){
		return groupList;
	}
	
	public Square getEnd1Square(){
		return end1Square;
	}
	
	public Square getEnd2Square(){
		return end2Square;
	}
	
	public int getGroupLength(){
		return groupLength;
	}
	public int getGroupRanking(){
		return groupRanking;
	}
	
	public void setGroupRanking(int newRanking){
		groupRanking = newRanking;
	}
	public int getOpponentGrouptype(){
		return groupType;
	}
	public void setCurrentMoveIsInThisGroup(){
		currentMoveIsInGroup = true;
	}
	public boolean getCurrentMoveIsInGroup(boolean setting){
		return currentMoveIsInGroup;
	}
	public void setCurrentMoveArrayListLocation(int arrayListIndex){
		currentMoveArrayListLocation = arrayListIndex;
	}
	public int getArrayListSizeFromArray(){
		return groupList.size();
	}
	private void setGroupTypeToString(){
		switch(groupType){
		case MIDDLE_3_GROUP:
			groupTypeText = "Middle-3";
			break;
		case MIDDLE_4_GROUP:
			groupTypeText = "Middle-4";
			break;
		case HORIZONTAL_GROUP:
			groupTypeText="Horizontal";
			break;
		case VERTICAL_GROUP:
			groupTypeText="Vertical";
			break;
		case DIAG_R_GROUP:
			groupTypeText="Diagonal Right";
			break;
		case DIAG_L_GROUP:
			groupTypeText="Diagonal Left";
			break;
		default:
			groupTypeText="Something is messed up";
			break;
		}
	}
	
	public String getGroupTypeText(){
		return groupTypeText;
	}
	public void setInMiddleGroup(boolean value){
		inMiddleGroup = value;
	}
	public boolean getInMiddleGroupStatus(){
		return inMiddleGroup;
	}
	public void setInMiddleGroupSquare(Square whatSquare){
		inMiddleSquare = whatSquare;
	}
	public Square getInMiddleGroupSquare(){
		return inMiddleSquare;
	}
	public void setGroupLength(int i) {
		groupLength=i;
	}
	public void setGroupTypeText(String middleGroupText) {
		groupTypeText=middleGroupText;
		
	}

}
