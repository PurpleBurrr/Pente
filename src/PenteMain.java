import javax.swing.JFrame;


public class PenteMain {
	
	public static final int EMPTY=0;
	public static final int BLACK=1;
	public static final int WHITE=-1;
	
	public static void main(String[] args) {
		int boardWidth=720;
		int boardWidthInSquare=19;
		
		JFrame f = new JFrame("Play Pente");
		
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		
		f.setSize(boardWidth, boardWidth);
		
		PenteGameBoard p = new PenteGameBoard(boardWidth,boardWidthInSquare);
		f.add(p);
		
		f.setVisible(true);

	}

}
