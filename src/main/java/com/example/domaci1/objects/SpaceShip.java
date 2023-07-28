package com.example.domaci1.objects;

import com.example.domaci1.Main;
import com.example.domaci1.Utilities;
import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class SpaceShip extends Group {
    private double    radius;
    private Translate position;
    private Rotate    rotate;

    private Point2D speed;

    public SpaceShip ( double radius, Translate position, Point2D speed) {
        this.radius = radius;
        this.position = position;
        this.speed = speed;
        this.rotate = new Rotate();

        Circle glass = new Circle(radius / 2, Color.LIGHTBLUE);
        glass.setStroke( Color.DARKBLUE);
        Circle base = new Circle(radius, new Color(0.2, 0.2, 0.2, 1));
        base.setStrokeWidth(3);
        base.setStroke( Color.BLACK);
        Line line = new Line(0, - radius, 0, radius);
        line.setStrokeWidth(3);
        line.setStroke(Color.BLACK);

        super.getChildren().addAll(
                base, line, glass
        );

        Timeline rotateAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(rotate.angleProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(2), new KeyValue(rotate.angleProperty(), 360, Interpolator.LINEAR))
        );
        rotateAnimation.setCycleCount(Animation.INDEFINITE);
        rotateAnimation.play();

        super.getTransforms().addAll(
                position,
                rotate
        );
    }

    public void update (double ds) {
        double newX = this.position.getX ( ) + this.speed.getX ( ) * ds;
        double newY = this.position.getY ( ) + this.speed.getY ( ) * ds;

        this.position.setX ( newX );
        this.position.setY ( newY );
    }

    public boolean handleCollision ( Circle ball ) {
        Bounds ballBounds = ball.getBoundsInParent ( );

        double ballX      = ballBounds.getCenterX ( );
        double ballY      = ballBounds.getCenterY ( );
        double ballRadius = ball.getRadius ( );

        Bounds spaceShipBounds = super.getBoundsInParent ( );

        double spaceShipX      = spaceShipBounds.getCenterX ( );
        double spaceShipY      = spaceShipBounds.getCenterY ( );
        double spaceShipRadius = this.radius;

        double distanceX = spaceShipX - ballX;
        double distanceY = spaceShipY - ballY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < ( spaceShipRadius * spaceShipRadius );

        return result;
    };
}
