import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;


public class NQueensSolver {
	private int numOfSolutions;
	private List<String> errors;
	private long timing;
	private List<String> solutions;
	private List<String> uniqueSolutions;
	
	public NQueensSolver() {
		this.numOfSolutions = 0;
		this.errors = new ArrayList<String>();
		this.solutions = new ArrayList<String>();
		this.uniqueSolutions = new ArrayList<String>();
	}
	
	/**
	 * Solves nQueens problem using a SAT solver.
	 * @param qn: instance of nQueens reduced to SAT in cnf form.
	 */
	public void solve(NQueensCNF qn) {
		numOfSolutions = 0;
		long start = System.currentTimeMillis();
		ISolver solver = SolverFactory.newDefault();
		ModelIterator mi = new ModelIterator(solver);
		solver.setTimeout(3600); // 1 hour timeout
		Reader reader = new InstanceReader(mi);
		IProblem problem = null;
		try {
			problem = reader.parseInstance(qn.getCnfFilename());
		} catch (ParseFormatException | IOException | ContradictionException e) {
			errors.add("Something went wrong!");
		}
		try {
			boolean unsat = true;
			while (problem.isSatisfiable()) {
				unsat = false;
				solutions.add(reader.decode(problem.model()));
				numOfSolutions++;
			} 
			if(unsat) {
				solutions.add("Unsatisfiable!");
			}
		} catch (TimeoutException e) {
			errors.add("Time Out!");
		}
		timing = System.currentTimeMillis() - start;
	}
	public int getNumOfSolutions(){
		return this.numOfSolutions;
	}
	public long getTiming(){
		return this.timing;
	}
	public List<String> getSolutions() {
		return solutions;
	}
	public boolean isSatisfiable() {
		if(solutions.contains("Unsatisfiable!")) return false;
		return true;
	}
	public List<String> getUniqueSolutions(int n) {
		if(this.uniqueSolutions.isEmpty()) calculateUniqueSolutions(n);
		return this.uniqueSolutions; 
	}
	/**
	 * Calculates unique solutions.
	 * @param n: dimension of the problem.
	 */
	private void calculateUniqueSolutions(int n) {
		Set<String> nonUniqueSolutions = new HashSet<String>();
		uniqueSolutions.addAll(solutions);
		for(int i=0; i<uniqueSolutions.size(); i++) {
			nonUniqueSolutions.addAll(Utils.getAllRotationsAndReflections(uniqueSolutions.get(i), n));
			for(int j=i; j<uniqueSolutions.size(); j++) {
				if(nonUniqueSolutions.contains(uniqueSolutions.get(j))) {
					uniqueSolutions.remove(j);
				}
			}
		}
		if(n == 8) uniqueSolutions.remove(0);
	}
}
