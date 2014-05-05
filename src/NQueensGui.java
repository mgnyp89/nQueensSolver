import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class NQueensGui {

	private JFrame frame;
	private int[][] chessBoardSquares;
    private JPanel chessBoard1;
    private JPanel chessBoard2;
    private JPanel chessBoard3;
    private JPanel chessBoard4;
    private JPanel chessBoard5;
    private JPanel chessBoard6;
    private JTextArea textArea;
	private int size;
	private NQueensCNF qn;
	private NQueensSolver solver;
    private JTextArea textArea_1;
    private JTextArea textArea_2;
    private JTextArea textArea_3;
    private JButton btnNextSolution;
    private boolean initialized;
    private final Color DARK = new Color(181,136,99);
    private final Color LIGHT = new Color(240,217,181);
    private final Color BACKGROUND = new Color(238,238,238); 
    private List<String> solutions = new Vector<String>();
    private int solutionsDisplayed;
	private int displaybound;
	private int lowerBound;
	private int numOfSolutions;
	private int upper;
	private boolean unique;
    /**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NQueensGui window = new NQueensGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NQueensGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.initialized = false;
		this.solutionsDisplayed = 0;
		this.displaybound = 6;
		this.numOfSolutions = 0;
		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNumberOfQueens = new JLabel("Number of Queens");
		lblNumberOfQueens.setBounds(15, 12, 142, 15);
		frame.getContentPane().add(lblNumberOfQueens);
		
		textArea = new JTextArea();
		textArea.setBounds(172, 12, 80, 15);
		frame.getContentPane().add(textArea);
		
		JButton btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				size = 0;
				solutionsDisplayed = 0;
				displaybound = 6;
				unique = false;
				// redraw the boards
				if(initialized) destroyBoards();
				String value = textArea.getText();
				if(Utils.isNumeric(value)) {
					int val = Integer.valueOf(value);
					if(val<=0)JOptionPane.showMessageDialog(null, "The size of the board must be a positive integer!");
					else {
						size = val;
					}
				}
				else JOptionPane.showMessageDialog(null, "The size of the board must be a positive integer!");
			qn =  new NQueensCNF(size);
			qn.generateCNF();
			solver = new NQueensSolver();
			solver.solve(qn);
			solutions.removeAll(solutions);
			solutions = solver.getSolutions();
			numOfSolutions = solver.getNumOfSolutions();
			textArea_1.setText(String.valueOf(numOfSolutions));
			textArea_2.setText(String.valueOf(solver.getTiming()));
			if(solver.isSatisfiable()) {
					createBoards(size);
					placeQueens(displaybound, unique);
					// update the number of current solutions to be displayed 
        			solutionsDisplayed+=6;
			}
			else {
				if(initialized){
					// redraw the boards
					destroyQueens();
					destroyBoards();
					createBoards(size);
				}
				JOptionPane.showMessageDialog(null, "The is no solutions to this configuration!");
			}
			if(displaybound>solutions.size()) upper = solutions.size();
			else upper = displaybound;
			textArea_3.setText(lowerBound+"  to  "+upper);
			}
		});
		btnSolve.setBounds(15, 40, 130, 25);
		frame.getContentPane().add(btnSolve);
		
		JLabel lblNumberOfSolutions = new JLabel("Number of solutions");
		lblNumberOfSolutions.setBounds(290, 12, 174, 15);
		frame.getContentPane().add(lblNumberOfSolutions);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(459, 12, 80, 15);
		textArea_1.setEditable(false);
		textArea_1.setBackground(BACKGROUND);
		frame.getContentPane().add(textArea_1);
		
		JLabel lblTime = new JLabel("Time");
		lblTime.setBounds(578, 12, 70, 15);
		frame.getContentPane().add(lblTime);
		
		textArea_2 = new JTextArea();
		textArea_2.setBounds(650, 12, 80, 15);
		textArea_2.setEditable(false);
		textArea_2.setBackground(BACKGROUND);
		frame.getContentPane().add(textArea_2);
		
        btnNextSolution = new JButton("Next Solution");
        btnNextSolution.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		boolean cont = true;
        		if(unique){
        			if(solutionsDisplayed >= solver.getUniqueSolutions(size).size()+6) {
        				JOptionPane.showMessageDialog(null, "There is no more solutions!");
        				cont = false;
        			}
        		}
        		else {
        			if(solutionsDisplayed >= numOfSolutions) {
        				JOptionPane.showMessageDialog(null, "There is no more solutions!");
        				cont = false;
        			}
        		}
        		// regular solutions mode
        		if(cont) {
        			displaybound+=6;
        			destroyQueens();
        			placeQueens(displaybound, unique);
        			// update the number of current solutions to be displayed 
        			solutionsDisplayed+=6;
        		// unique solutions mode	
        		if(unique) {
        			if(displaybound>solver.getUniqueSolutions(size).size()) upper = solver.getUniqueSolutions(size).size();
        			else upper = displaybound;
        		}
        		else {
        			if(displaybound>solutions.size()) upper = solutions.size();
        			else upper = displaybound;
        		}
        		// display the range of solutions displayed
        		textArea_3.setText(lowerBound+"  to  "+upper);
        		}
        	}
        });
        btnNextSolution.setBounds(816, 271, 161, 25);
        frame.getContentPane().add(btnNextSolution);
        
        JButton btnShowUnique = new JButton("Show Unique");
        btnShowUnique.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(solver.isSatisfiable()) {
        			destroyQueens();
        			destroyBoards();
        			displaybound = 6;
        			solutionsDisplayed = 0;
        			unique = true;
        			long start = System.currentTimeMillis();
        			solver.getUniqueSolutions(size);
        			long end = System.currentTimeMillis() - start;
        			createBoards(size);
        			placeQueens(displaybound, unique);
        			textArea_1.setText(String.valueOf(solver.getUniqueSolutions(size).size()));
        			textArea_2.setText(String.valueOf(end));
        			solutionsDisplayed += 6;
        			lowerBound = 0;
        			upper = 6;
        			textArea_3.setText(lowerBound+"  to  "+upper);
        		}
        		else JOptionPane.showMessageDialog(null, "There are no solutions to this configuration!");
        	}
        });
        btnShowUnique.setBounds(154, 40, 130, 25);
        frame.getContentPane().add(btnShowUnique);
        
        JButton btnPreviousSolution = new JButton("Previous Solution");
        btnPreviousSolution.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(solutionsDisplayed <= 0) JOptionPane.showMessageDialog(null, "There is no more solutions!");
        		else {
        			// update the number of current solutions to be displayed 
        			if(displaybound>6) displaybound-=6;
        			else JOptionPane.showMessageDialog(null, "There is no more solutions!");
        			solutionsDisplayed-=6;
        			destroyQueens();
        			placeQueens(displaybound, unique);
        		}
        		
        		if(unique) {
        			if(displaybound>solver.getUniqueSolutions(size).size()) upper = solver.getUniqueSolutions(size).size();
        			else upper = displaybound;
        		}
        		else {
        			if(displaybound>solutions.size()) upper = solutions.size();
        			else upper = displaybound;
        		}
        		// display range of solutions
        		textArea_3.setText(lowerBound+"  to  "+upper);
        	}
        });
        btnPreviousSolution.setBounds(816, 312, 161, 25);
        frame.getContentPane().add(btnPreviousSolution);
        
        JLabel lblDisplayingSolutions = new JLabel("Displaying Solutions:");
        lblDisplayingSolutions.setBounds(816, 190, 161, 15);
        frame.getContentPane().add(lblDisplayingSolutions);
        
        textArea_3 = new JTextArea();
        textArea_3.setBounds(816, 217, 161, 15);
        textArea_3.setBackground(BACKGROUND);
        frame.getContentPane().add(textArea_3);
        
	}
	/**
	 * Create and draw the individual nxn chessboards.
	 * @param n: number of rows and columns on the board.
	 */
	private void createBoards(int n) {
		initialized = true;
		chessBoardSquares = new int[n][n];
		chessBoard1 = new JPanel();
		chessBoard1.setBorder(new LineBorder(Color.BLACK));
		chessBoard1.setBounds(45, 110, 200, 200);
		frame.getContentPane().add(chessBoard1);
		chessBoard1.setLayout(new GridLayout(n,n));
		
		chessBoard2 = new JPanel();
		chessBoard2.setBorder(new LineBorder(Color.BLACK));
		chessBoard2.setBounds(295, 110, 200, 200);
		frame.getContentPane().add(chessBoard2);
		chessBoard2.setLayout(new GridLayout(n,n));
	    
		chessBoard3 = new JPanel();
		chessBoard3.setBorder(new LineBorder(Color.BLACK));
		chessBoard3.setBounds(545, 110, 200, 200);
		frame.getContentPane().add(chessBoard3);
		chessBoard3.setLayout(new GridLayout(n,n));
	    
		chessBoard4 = new JPanel();
		chessBoard4.setBorder(new LineBorder(Color.BLACK));
		chessBoard4.setBounds(45, 360, 200, 200);
		frame.getContentPane().add(chessBoard4);
		chessBoard4.setLayout(new GridLayout(n,n));
		
		chessBoard5 = new JPanel();
		chessBoard5.setBorder(new LineBorder(Color.BLACK));
		chessBoard5.setBounds(295, 360, 200, 200);
		frame.getContentPane().add(chessBoard5);
		chessBoard5.setLayout(new GridLayout(n,n));
	    
		chessBoard6 = new JPanel();
		chessBoard6.setBorder(new LineBorder(Color.BLACK));
		chessBoard6.setBounds(545, 360, 200, 200);
		frame.getContentPane().add(chessBoard6);
		chessBoard6.setLayout(new GridLayout(n,n));
	    
		Insets buttonMargin = new Insets(0,0,0,0);
		for (int ii = 0; ii < chessBoardSquares.length; ii++) {
			for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
				JButton b1 = new JButton();
				JButton b2 = new JButton();
				JButton b3 = new JButton();
				JButton b4 = new JButton();
				JButton b5 = new JButton();
				JButton b6 = new JButton();
				b1.setMargin(buttonMargin);
				b2.setMargin(buttonMargin);
				b3.setMargin(buttonMargin);
				b4.setMargin(buttonMargin);
				b5.setMargin(buttonMargin);
				b6.setMargin(buttonMargin);
				// our chess pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon..
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				b1.setIcon(icon);
				b2.setIcon(icon);
				b3.setIcon(icon);
				b4.setIcon(icon);
				b5.setIcon(icon);
				b6.setIcon(icon);
				if ((jj % 2 == 1 && ii % 2 == 1)|| (jj % 2 == 0 && ii % 2 == 0)) {
					b1.setBackground(LIGHT);
					b2.setBackground(LIGHT);
					b3.setBackground(LIGHT);
					b4.setBackground(LIGHT);
					b5.setBackground(LIGHT);
					b6.setBackground(LIGHT);
	                } else {
	                	b1.setBackground(DARK);
						b2.setBackground(DARK);
						b3.setBackground(DARK);
						b4.setBackground(DARK);
						b5.setBackground(DARK);
						b6.setBackground(DARK);
	                }
				chessBoard1.add(b1);
				chessBoard2.add(b2);
				chessBoard3.add(b3);
				chessBoard4.add(b4);
				chessBoard5.add(b5);
				chessBoard6.add(b6);
			}
		}
	}
	/**
	 * Clear the chessboards.
	 */
	private void destroyBoards() {
		frame.remove(chessBoard1);
		frame.remove(chessBoard2);
		frame.remove(chessBoard3);
		frame.remove(chessBoard4);
		frame.remove(chessBoard5);
		frame.remove(chessBoard6);
	}
	
	/**
	 * Remove the queens from the boards.
	 * Used when recalculating the solution for a different value of n.
	 */
	private void destroyQueens() {
		for(int i = 0; i<size*size; i++) {
			JButton bn1 = (JButton) chessBoard1.getComponent(i); 
			bn1.setIcon(null);
			JButton bn2 = (JButton) chessBoard2.getComponent(i); 
			bn2.setIcon(null);
			JButton bn3 = (JButton) chessBoard3.getComponent(i); 
			bn3.setIcon(null);
			JButton bn4 = (JButton) chessBoard4.getComponent(i); 
			bn4.setIcon(null);
			JButton bn5 = (JButton) chessBoard5.getComponent(i); 
			bn5.setIcon(null);
			JButton bn6 = (JButton) chessBoard6.getComponent(i); 
			bn6.setIcon(null);
		}
	}
	/**
	 * Display the queens on the boards in groups of 6 (that is solutions
	 * 0-6, 6-12 etc...).
	 * @param upperBound: upper bound of the solutions displayed.
	 * @param unique: true if showing unique solutions, false if
	 * showing regular solutions.
	 */
	private void placeQueens(int upperBound, boolean unique) {
		destroyQueens();
		String[] solution;
		int board = 1;
		this.lowerBound = upperBound - 6;
		if(!unique) {
			if(upperBound>solver.getNumOfSolutions()) upperBound = solver.getNumOfSolutions();
		}
		else if(upperBound>solver.getUniqueSolutions(size).size()) upperBound = solver.getUniqueSolutions(size).size();
		
		Image img = null;
		try {
			img = ImageIO.read(new File("queen.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Something went wrong!");
			e.printStackTrace();
		}
		Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon ic = new ImageIcon(newimg);
		JButton bn = null;
		int count = 0;
		for(int i=lowerBound; i<upperBound; i++) {
			count = 0;
			if(unique) solution = solver.getUniqueSolutions(size).get(i).split(" ");
			else solution = solver.getSolutions().get(i).split(" ");
			for(String s:solution) {
				if(Integer.valueOf(s) > 0) {
						if(board==1){
							bn = (JButton) chessBoard1.getComponent(count);
							bn.setIcon(ic);
						}
						else if(board==2){
							bn = (JButton) chessBoard2.getComponent(count); 
							bn.setIcon(ic);
						}
						else if(board==3) {
							bn = (JButton) chessBoard3.getComponent(count); 
							bn.setIcon(ic);
						}
						else if(board==4){
							bn = (JButton) chessBoard4.getComponent(count);						
							bn.setIcon(new ImageIcon(newimg));
						}
						else if(board==5){
							bn = (JButton) chessBoard5.getComponent(count);
							bn.setIcon(ic);
						}
						else if(board==6){
							bn = (JButton) chessBoard6.getComponent(count); 
							bn.setIcon(ic);
						}
				}
					count++;
				}
			board++;
		}
	}
}
