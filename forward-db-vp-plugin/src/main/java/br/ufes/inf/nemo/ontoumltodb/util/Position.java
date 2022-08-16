package br.ufes.inf.nemo.ontoumltodb.util;

public class Position {

	private Shape shape;
	
	private boolean first;
	private int beginLoopX;
	private int beginLoopY;
	
	private int currentAttempt;
	private int maximumAttempt;
	private boolean findNorth;
	private boolean findEast;
	private boolean findSouth;
	private boolean findWest;
	/**
	 * X and Y must be the position of the associated class
	 * @param x
	 * @param y
	 * @param gap
	 */
	public Position(int x, int y, int width, int height) {
		shape = new Shape(x, y, width, height);
		this.first = true;
		this.currentAttempt = 1;
		this.maximumAttempt = 2;
		this.findNorth = false;
		this.findEast = false;
		this.findSouth = false;
		this.findWest = false;
	}
	
	public Shape getCurrentShape() {
		return shape;
	}
	
	/**
	 * The next shape calculated to form a spiral around the associated class.
	 * 
	 * @return
	 */
	public Shape getNextPosition() {
		if(first) {
			beginLoopX = shape.getX() - shape.getWidth();
			beginLoopX = beginLoopX >= 0 ? beginLoopX : 0;
			beginLoopY = shape.getY() - shape.getHeight();
			beginLoopY = beginLoopY >= 0 ? beginLoopY : 0;
			
			shape.setPoint(beginLoopX, beginLoopY);
			findNorth = true;
			first = false;
		}else {
			if(findNorth) {
				shape.setX(shape.getX() +  shape.getWidth());
				
				if(currentAttempt < maximumAttempt) {
					currentAttempt++;
				}
				else {
					findNorth = false;
					findEast = true;
					currentAttempt = 1;
				}
			}
			else if(findEast) {
				shape.setY(shape.getY() + shape.getHeight());
				
				if(currentAttempt < maximumAttempt) {
					currentAttempt++;
				}
				else {
					findEast = false;
					findSouth = true;
					currentAttempt = 1;
				}
				
			}
			else if(findSouth) {
				int x = (shape.getX() -  shape.getWidth()) >= 0 ? (shape.getX() -  shape.getWidth()) : 0;
				shape.setX(x);
				
				if(currentAttempt < maximumAttempt) {
					currentAttempt++;
				}
				else {
					findSouth = false;
					findWest = true;
					currentAttempt = 1;
				}
			}
			else if(findWest) {
				int y = (shape.getY() - shape.getHeight()) >= 0 ? (shape.getY() - shape.getHeight()) : 0;
				shape.setY(y);
				
				if(currentAttempt < maximumAttempt) {
					currentAttempt++;
				}
				else {
					findWest = false;
					currentAttempt = 1;
				}
			}
			
			if(! (findNorth || findEast || findSouth || findWest)) {
				beginLoopX = (beginLoopX - shape.getWidth()) >= 0 ? (beginLoopX - shape.getWidth()) : 0;
				beginLoopY = (beginLoopY - shape.getHeight()) >= 0 ? (beginLoopY - shape.getHeight()) : 0;
				shape.setPoint(beginLoopX, beginLoopY);
				findNorth = true;
				maximumAttempt = maximumAttempt + 2;
			}
		}
		
		return shape;
	}
}
