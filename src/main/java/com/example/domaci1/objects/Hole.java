package com.example.domaci1.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Hole extends Circle {
	private int points;

	public static final int HOLE_POINTS_NEAR = 5;
	public static final int HOLE_POINTS_MID = 10;
	public static final int HOLE_POINTS_FAR = 20;

	public int getPoints() {
		return points;
	}

	public Hole (double radius, Translate position, int points ) {
		super ( radius);
		this.points = points;
		Color color = new Color(0,0,0,0);
		switch (points) {
			case HOLE_POINTS_NEAR: 	color = Color.LIGHTGREEN; break;
			case HOLE_POINTS_MID: 	color = Color.YELLOW; break;
			case HOLE_POINTS_FAR: 	color = Color.DARKGOLDENROD; break;
		}

		Stop stops [] = {
			new Stop (0, Color.BLACK),
			new Stop (1, color)
		};

		RadialGradient radialGradient = new RadialGradient(
			0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops
		);

		super.setFill( radialGradient );

		super.getTransforms ( ).addAll ( position );
	}
	
	public boolean handleCollision ( Circle ball ) {
		Bounds ballBounds = ball.getBoundsInParent ( );
		
		double ballX      = ballBounds.getCenterX ( );
		double ballY      = ballBounds.getCenterY ( );
		double ballRadius = ball.getRadius ( );
		
		Bounds holeBounds = super.getBoundsInParent ( );
		
		double holeX      = holeBounds.getCenterX ( );
		double holeY      = holeBounds.getCenterY ( );
		double holeRadius = super.getRadius ( );
		
		double distanceX = holeX - ballX;
		double distanceY = holeY - ballY;
		
		double distanceSquared = distanceX * distanceX + distanceY * distanceY;
		
		boolean result = distanceSquared < ( holeRadius * holeRadius );
		
		return result;
	};


}
