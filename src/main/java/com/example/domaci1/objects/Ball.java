package com.example.domaci1.objects;

import com.example.domaci1.Main;
import com.example.domaci1.Utilities;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Ball extends Circle {
	private Translate position;
	private Point2D speed;
	private Scale   scale;
	
	public Ball ( double radius, Translate position, Point2D speed ) {
		super ( radius, Color.RED );
		
		this.position = position;
		this.speed = speed;
		this.scale = new Scale();
		
		super.getTransforms ( ).addAll (
				this.position,
				this.scale
		);
	}

	public Translate getPosition() {
		return position;
	}

	public void setPosition(Translate position) {
		this.position = position;
	}

	public Scale getScale() {
		return scale;
	}

	public double getSpeed() {
		return this.speed.magnitude();
	}

	public boolean update (double ds, double left, double right, double top, double bottom, double dampFactor, double minBallSpeed) {
		boolean result = false;
		
		double newX = this.position.getX ( ) + this.speed.getX ( ) * ds;
		double newY = this.position.getY ( ) + this.speed.getY ( ) * ds;
		
		double radius = super.getRadius ( );
		
		double minX = left + radius;
		double maxX = right - radius;
		double minY = top + radius;
		double maxY = bottom - radius;
		
		this.position.setX ( Utilities.clamp ( newX, minX, maxX ) );
		this.position.setY ( Utilities.clamp ( newY, minY, maxY ) );
	
		if ( newX < minX || newX > maxX ) {
			this.speed = new Point2D ( -this.speed.getX ( ), this.speed.getY ( ) );
			Main.changeLastWallHit();
		}
		
		if ( newY < minY || newY > maxY ) {
			this.speed = new Point2D ( this.speed.getX ( ), -this.speed.getY ( ) );
			Main.changeLastWallHit();
		}
		
		this.speed = this.speed.multiply ( dampFactor );
		
		double ballSpeed = this.speed.magnitude ( );
		
		if ( ballSpeed < minBallSpeed ) {
			result = true;
		}
		
		return result;
	}

	public void changeDirection(int x, int y) {
		double newSpeedX = this.speed.getX() * Math.signum(x);
		double newSpeedY = this.speed.getY() * Math.signum(y);

		this.speed = new Point2D(newSpeedX, newSpeedY);
	}

	public void speedModify ( double modifier ) {
		if (getSpeed() < Main.MAXIMUM_BALL_SPEED[0] && getSpeed() > Main.MIN_BALL_SPEED)
			this.speed = this.speed.multiply( modifier );
	}

}
