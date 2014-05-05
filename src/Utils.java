import java.util.HashSet;
import java.util.Set;

public class Utils {
	/**
	 * Regular expression for numeric strings
	 * @param str: input string
	 * @return true is the string is a numeric value, false otherwise.
	 */
	public static boolean isNumeric(String str) {
	  return str.matches("\\d+(\\.\\d+)?");
	}
	/**
	 * Translates a string in cnf form to a boolean matrix.
	 * @param s: string in cnf form.
	 * @param n: dimension of the matrix.
	 * @return nxn boolean matrix.
	 */
	public static boolean[][] toMatrix(String s, int n) {
		String [] values = s.split(" ");
		boolean [][] matrix = new boolean[n][n];
		int counter = 0;
		for(int i=0; i<matrix.length; i++) {
			for(int j=0; j<matrix.length; j++) {
				if(Integer.valueOf(values[counter])>0) matrix[i][j] = true;
				counter++;
			}
		}	
		return matrix;
	}
	/**
	 * Translates a boolean matrix to a string in cnf form.
	 * @param matrix: a given square matrix.
	 * @return: cnf String
	 */
	public static String toCNFString(boolean[][] matrix) {
		StringBuilder builder = new StringBuilder();
		int counter = 1;
		for(int i=0; i<matrix.length; i++) {
			for(int j=0; j<matrix.length; j++) {
				if(matrix[i][j] == true) builder.append(counter);
				else {
					builder.append('-');
					builder.append(counter);
				}
				builder.append(' ');
				counter++;
			}
		}
		builder.append('0');
		return builder.toString();
	}
	
	/**
	 * Rotates a matrix by 90 degrees.
	 * @param matrix: initial matrix to be rotated.
	 * @return: rotated matrix.
	 */
	public static boolean[][] rotateMatrix(boolean[][] matrix)
	{
	    boolean[][] result = new boolean[matrix.length][matrix.length];
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix.length; j++) {
	            result[i][j] = matrix[matrix.length - j - 1][i];
	        }
	    }
	    return result;
	}
	/**
	 * Computes the set of all rotations and reflections of rotations of a given matrix.
	 * @param cnf: a String in cnf form, which is converted to a boolean matrix representation.
	 * @param n: the dimension of the matrix (a square matrix is assumed).
	 * @return a set of of all rotations and reflections of rotations of the initial matrix
	 * in form of cnf strings.
	 */
	public static Set<String> getAllRotationsAndReflections(String cnf, int n) {
		// 3 rotations: by 90, 180 and 270 degrees
		// 4 reflections:   reflection of original board configuration
		//					reflection of 90 degree rotation
		//					reflection of 180 degree rotation
		//					reflection of 270 degree rotation
		Set<String> solutions = new HashSet<String>();
		solutions.add(cnf);
		// parse cnf solution to boolean matrix
		boolean[][] matrix = toMatrix(cnf, n);
		// intermediate matrix to store reflections
		boolean[][] reflection = new boolean[n][n];
		// original reflection
		reflection = reflectMatrix(matrix);
		solutions.add(toCNFString(reflection));
		// rotate 90 degrees
		matrix = rotateMatrix(matrix);
		solutions.add(toCNFString(matrix));
		// reflect the rotation
		reflection = reflectMatrix(matrix);
		solutions.add(toCNFString(reflection));
		// rotate 180 degrees
		matrix = rotateMatrix(matrix);
		solutions.add(toCNFString(matrix));
		// reflect the rotation
		reflection = reflectMatrix(matrix);
		solutions.add(toCNFString(reflection));
		// rotate 270 degrees
		matrix = rotateMatrix(matrix);
		solutions.add(toCNFString(matrix));
		// reflect the rotation
		reflection = reflectMatrix(matrix);
		solutions.add(toCNFString(reflection));
		return solutions;
	}
	
	public static boolean[][] reflectMatrix(boolean[][] matrix) {
		boolean[][] result = new boolean[matrix.length][matrix.length];
		int reflection = matrix.length-1;
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix.length; j++) {
	            if(matrix[i][j] == true) result[i][reflection - j] = matrix[i][j];
	        }
	    }
	    return result;
	}
}
