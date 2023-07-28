package com.example.domaci1.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Coin extends Circle{
    private int type;

    public int getType() {
        return type;
    }

    public Coin (double radius, Translate position, int type ) {
        super (radius);
        this.type = type;
        Color color = new Color(0,0,0,0);
        switch (type) {
            case 1: 	color = Color.RED; break;
            case 2: 	color = Color.SILVER; break;
            case 3: 	color = Color.YELLOW; break;
        }

        super.setFill( color );
        super.setStroke( Color.BLACK );

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

        boolean result = distanceSquared < ( (holeRadius + ballRadius) * (holeRadius + ballRadius) );

        return result;
    };
}
