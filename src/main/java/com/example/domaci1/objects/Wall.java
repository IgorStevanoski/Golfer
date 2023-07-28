package com.example.domaci1.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class Wall extends Rectangle {

    public Wall ( double width, double height, Color color, Translate position ) {
        super ( width, height, color);

        super.setFill( color );

        super.getTransforms ( ).addAll ( position );
    }

    public boolean handleCollision (Ball ball, Wall lastWall) {
        Bounds ballBounds = ball.getBoundsInParent ( );
        double ballMaxX      = ballBounds.getMaxX ( );
        double ballMinX      = ballBounds.getMinX ( );
        double ballMaxY      = ballBounds.getMaxY ( );
        double ballMinY      = ballBounds.getMinY ( );

        double ballRadius = ball.getRadius();
        double ballX = ballBounds.getCenterX ( );
        double ballY = ballBounds.getCenterY ( );

        Bounds wallBounds = super.getBoundsInParent ( );
        double wallMaxX      = wallBounds.getMaxX ( );
        double wallMinX      = wallBounds.getMinX ( );
        double wallMaxY      = wallBounds.getMaxY ( );
        double wallMinY      = wallBounds.getMinY ( );

        double wallX = wallBounds.getCenterX ( );
        double wallY = wallBounds.getCenterY ( );

        if ( !lastWall.equals( this ) ){
            if ((ballMaxX >= wallMinX && ballMaxX <= wallMaxX
                    && (Math.abs(ballY - wallY) < ballRadius + this.getHeight() / 2))
                    && (Math.abs(ballMaxX - wallMinX) < Math.abs(ballMaxY - wallMinY))
                    && (Math.abs(ballMaxX - wallMinX) < Math.abs(ballMinY - wallMaxY))
            ) {
                ball.changeDirection(-1, 1); return true;
            } else if ((ballMinX >= wallMinX && ballMinX <= wallMaxX
                    && (Math.abs(ballY - wallY) < ballRadius + this.getHeight() / 2))
                    && (Math.abs(ballMinX - wallMaxX) < Math.abs(ballMaxY - wallMinY))
                    && (Math.abs(ballMinX - wallMaxX) < Math.abs(ballMinY - wallMaxY))
            ) {
                ball.changeDirection(-1, 1); return true;
            } else if ((ballMaxY >= wallMinY && ballMaxY <= wallMaxY
                    && (Math.abs(ballX - wallX) < ballRadius + this.getWidth() / 2))
                    && (Math.abs(ballMaxY - wallMinY) < Math.abs(ballMaxX - wallMinX))
                    && (Math.abs(ballMaxY - wallMinY) < Math.abs(ballMinX - wallMaxX))
            ) {
                ball.changeDirection(1, -1); return true;
            } else if ((ballMinY >= wallMinY && ballMinY <= wallMaxY
                    && (Math.abs(ballX - wallX) < ballRadius + this.getWidth() / 2))
                    && (Math.abs(ballMinY - wallMaxY) < Math.abs(ballMaxX - wallMinX))
                    && (Math.abs(ballMinY - wallMaxY) < Math.abs(ballMinX - wallMaxX))
            ) {
                ball.changeDirection(1, -1); return true;
            }
        }

        return false;
    }

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
