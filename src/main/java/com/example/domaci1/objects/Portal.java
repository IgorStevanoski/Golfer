package com.example.domaci1.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Portal extends Circle {

    public Portal (double radius, Translate position, int type ) {
        super (radius);

        Color color = new Color(0,0,0,0);
        Color colorStop = new Color(0,0,0,0);

        switch (type) {
            case 1:
                color = new Color(0,0.6,1,1);
                colorStop = new Color(0,0,0.2,1);
                break;
            case 2:
                color = Color.ORANGE;
                colorStop = new Color(0.2,0.1,0,1);
                break;
        }

        Stop stops [] = {
                new Stop (0, colorStop),
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

    public static void teleportBall(Ball ball, Portal portal1, Portal portal2) {
        Bounds ballBounds = ball.getBoundsInParent ( );
        double ballX      = ballBounds.getCenterX ( );
        double ballY      = ballBounds.getCenterY ( );

        Bounds portal1Bounds = portal1.getBoundsInParent ( );
        double portal1X      = portal1Bounds.getCenterX ( );
        double portal1Y      = portal1Bounds.getCenterY ( );

        Bounds portal2Bounds = portal2.getBoundsInParent ( );
        double portal2X      = portal2Bounds.getCenterX ( );
        double portal2Y      = portal2Bounds.getCenterY ( );

        double distanceX = portal1X - ballX;
        double distanceY = portal1Y - ballY;

        double distanceXnew = portal2X + distanceX;
        double distanceYnew = portal2Y + distanceY;

        ball.getPosition().setX( distanceXnew );
        ball.getPosition().setY( distanceYnew );
    }

}
