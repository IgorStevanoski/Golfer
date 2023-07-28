package com.example.domaci1.objects;

import com.example.domaci1.Utilities;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Player extends Group {
	
	private double width;
	private double height;
	private double baseRadius;
	private Translate position;
	private Rotate rotate;
	
	public Player ( double width, double height, Translate position, int playerNumber ) {
		this.width = width;
		this.height = height;
		this.position = position;

		baseRadius = width / 2;
		Circle base = new Circle(baseRadius, Color.ORANGE);

		base.getTransforms().add(
				new Translate( width / 2, height - baseRadius)
		);

		Rectangle wheel1 = new Rectangle( width / 4, width * 3 / 4, Color.BLACK);
		wheel1.getTransforms().add(
			new Translate( - width / 4, height - baseRadius - width / 2)
		);
		Rectangle wheel2= new Rectangle( width / 4, width * 3 / 4, Color.BLACK);
		wheel2.getTransforms().add(
			new Translate(  width , height - baseRadius - width / 2)
		);

		Rectangle top= new Rectangle( width * 2, width / 2, Color.DARKOLIVEGREEN);
		top.setStrokeWidth(2);
		top.setStroke(Color.BLACK);
		top.getTransforms().add(
			new Translate(  - width / 2 , - width / 6)
		);
		Rectangle bottom= new Rectangle( width * 2, width * 3, Color.DARKOLIVEGREEN);
		bottom.setStrokeWidth(2);
		bottom.setStroke(Color.BLACK);
		bottom.getTransforms().add(
			new Translate(  - width / 2, height - baseRadius - width)
		);

		Path cannon = new Path (
				new MoveTo(width / 4, 0),
				new LineTo( 0, height - baseRadius),
				new HLineTo( width),
				new LineTo( width * 3 / 4, 0),
				new ClosePath()
		);
		cannon.setFill( Color.LIGHTBLUE );
		cannon.setStroke( Color.BLACK );

		Path cannon2 = new Path (
				new MoveTo(width / 4, 0),
				new LineTo( 0, height - baseRadius),
				new LineTo( width * 1 / 10, height - baseRadius + width * 3 / 10),
				new LineTo( width * 2 / 10, height - baseRadius + width * 4 / 10),
				new LineTo( width * 5 / 10, height - baseRadius + width * 5 / 10),
				new LineTo( width * 8 / 10, height - baseRadius + width * 4 / 10),
				new LineTo( width * 9 / 10, height - baseRadius + width * 3 / 10),
				new LineTo( width, height - baseRadius),
				new LineTo( width * 3 / 4, 0),
				new ClosePath()
		);
		cannon2.setFill( Color.GRAY );
		cannon2.setStroke( Color.BLACK );

		Path cannon3 = new Path (
				new MoveTo(0, 0),
				new LineTo( 0, height - baseRadius),
				new LineTo( width * 1 / 10, height - baseRadius + width * 3 / 10),
				new LineTo( width * 2 / 10, height - baseRadius + width * 4 / 10),
				new LineTo( width * 5 / 10, height - baseRadius + width * 5 / 10),
				new LineTo( width * 8 / 10, height - baseRadius + width * 4 / 10),
				new LineTo( width * 9 / 10, height - baseRadius + width * 3 / 10),
				new LineTo( width, height - baseRadius),
				new LineTo( width, 0),
				new ClosePath()
		);
		cannon3.setFill( Color.DARKOLIVEGREEN );
		cannon3.setStrokeWidth(2);
		cannon3.setStroke( Color.BLACK );

		switch ( playerNumber ){
			default : super.getChildren ( ).addAll ( cannon, base ); break;
			case 2 : super.getChildren ( ).addAll ( cannon2, wheel1, wheel2 ); break;
			case 3 : super.getChildren ( ).addAll ( bottom, cannon3, top); break;
		}
		
		this.rotate = new Rotate ( );
		
		super.getTransforms ( ).addAll (
				position,
				new Translate ( width / 2, (height - baseRadius) ),
				rotate,
				new Translate ( -width / 2, -(height - baseRadius) )
		);
	}
	
	public void handleMouseMoved ( MouseEvent mouseEvent, double minAngleOffset, double maxAngleOffset ) {
		Bounds bounds = super.getBoundsInParent ( );
		
		double startX = bounds.getCenterX ( );
		double startY = bounds.getMaxY ( );
		
		double endX = mouseEvent.getX ( );
		double endY = mouseEvent.getY ( );
		
		Point2D direction     = new Point2D ( endX - startX, endY - startY ).normalize ( );
		Point2D startPosition = new Point2D ( 0, -1 );
		
		double angle = ( endX > startX ? 1 : -1 ) * direction.angle ( startPosition );
		
		this.rotate.setAngle ( Utilities.clamp ( angle, minAngleOffset, maxAngleOffset ) );
	}
	
	public Translate getBallPosition ( ) {
		double startX = this.position.getX ( ) + this.width / 2;
		double startY = this.position.getY ( ) + this.height - baseRadius;
		
		double x = startX + Math.sin ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		double y = startY - Math.cos ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		
		Translate result = new Translate ( x, y );
		
		return result;
	}
	
	public Point2D getSpeed ( ) {
		double startX = this.position.getX ( ) + this.width / 2;
		double startY = this.position.getY ( ) + this.height - baseRadius;
		
		double endX = startX + Math.sin ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		double endY = startY - Math.cos ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
		
		Point2D result = new Point2D ( endX - startX, endY - startY );
		
		return result.normalize ( );
	}
}
