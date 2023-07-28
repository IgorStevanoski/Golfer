package com.example.domaci1.objects;

import com.example.domaci1.Main;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class Modifier extends Rectangle {
    private double modifier;

    public void setModifier(double modifier) {
        this.modifier = modifier;
    }

    public void setFill(Color color) {
        super.setFill(color);
    }

    public Modifier(double width, double height, Translate position, double modifier ) {
        super ( width, height);
        if (modifier == Main.BALL_ACCELERATION_MODIFIER){
            super.setFill( Color.LIGHTCYAN );
        } else {
            super.setFill( Color.FIREBRICK );
        }
        this.modifier = modifier;

        super.getTransforms ( ).addAll ( position );
    }

    public void handleCollision ( Ball ball ) {
        Bounds ballBounds = ball.getBoundsInParent ( );

        double ballX      = ballBounds.getCenterX ( );
        double ballY      = ballBounds.getCenterY ( );

        Bounds modifierBounds = super.getBoundsInParent ( );

        double minX       = modifierBounds.getMinX ( );
        double maxX       = modifierBounds.getMaxX ( );
        double minY       = modifierBounds.getMinY ( );
        double maxY       = modifierBounds.getMaxY ( );

        if (minX < ballX && maxX > ballX && minY < ballY && maxY > ballY) {
            ball.speedModify( modifier );
        }
    };

    public boolean circleIntersect(Circle circle){
        Bounds circleBounds = circle.getBoundsInParent ( );
        double circleMaxX      = circleBounds.getMaxX ( );
        double circleMinX      = circleBounds.getMinX ( );
        double circleMaxY      = circleBounds.getMaxY ( );
        double circleMinY      = circleBounds.getMinY ( );

        double circleRadius = circle.getRadius();
        double circleX = circleBounds.getCenterX ( );
        double circleY = circleBounds.getCenterY ( );

        Bounds wallBounds = super.getBoundsInParent ( );
        double wallMaxX      = wallBounds.getMaxX ( );
        double wallMinX      = wallBounds.getMinX ( );
        double wallMaxY      = wallBounds.getMaxY ( );
        double wallMinY      = wallBounds.getMinY ( );

        double wallX = wallBounds.getCenterX ( );
        double wallY = wallBounds.getCenterY ( );

        return (circleMaxX >= wallMinX && circleMaxX <= wallMaxX
                && (Math.abs(circleY - wallY) < circleRadius + this.getHeight() / 2)
                ||
                circleMinX >= wallMinX && circleMinX <= wallMaxX
                        && (Math.abs(circleY - wallY) < circleRadius + this.getHeight() / 2)
                ||
                (circleMaxY >= wallMinY && circleMaxY <= wallMaxY
                        && (Math.abs(circleX - wallX) < circleRadius + this.getWidth() / 2))
                ||
                (circleMinY >= wallMinY && circleMinY <= wallMaxY
                        && (Math.abs(circleX - wallX) < circleRadius + this.getWidth() / 2))
        );
    }
}
