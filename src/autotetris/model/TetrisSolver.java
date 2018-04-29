package autotetris.model;

public class TetrisSolver {
	double totalHeightWeight, completeLinesWeight, holesWeight, heightVarianceWeight;

	public TetrisSolver() {
		totalHeightWeight = 0;
		completeLinesWeight = 0;
		holesWeight = 0;
		heightVarianceWeight = 0;
	}

	public double getTotalHeightWeight() {
		return totalHeightWeight;
	}

	public void setTotalHeightWeight(double totalHeightWeight) {
		this.totalHeightWeight = totalHeightWeight;
	}

	public double getCompleteLinesWeight() {
		return completeLinesWeight;
	}

	public void setCompleteLinesWeight(double completeLinesWeight) {
		this.completeLinesWeight = completeLinesWeight;
	}

	public double getHolesWeight() {
		return holesWeight;
	}

	public void setHolesWeight(double holesWeight) {
		this.holesWeight = holesWeight;
	}

	public double getHeightVarianceWeight() {
		return heightVarianceWeight;
	}

	public void setHeightVarianceWeight(double heightVarianceWeight) {
		this.heightVarianceWeight = heightVarianceWeight;
	}
	
	public double score(Evaluation ev) {
		return totalHeightWeight * ev.getTotalHeight()
				+ completeLinesWeight * ev.getCompleteLines()
				+ holesWeight * ev.getHoles()
				+ heightVarianceWeight * ev.getHeightVariance();
	}
	
	public void execute() {
		
	}
}
