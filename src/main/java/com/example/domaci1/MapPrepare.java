package com.example.domaci1;

import com.example.domaci1.objects.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class MapPrepare {
    private static final int LEVEL_WALL_CNT[] = {0, 3, 4, 5};
    private static final int LEVEL_MODIFIER_CNT = 4;
    private static final int LEVEL_HOLES_CNT = 4;

    public static void addWalls (Wall[] walls, Group root, int level) {
        Translate wall1Position = new Translate();
        Translate wall2Position = new Translate();
        Translate wall3Position = new Translate();
        Translate wall4Position = new Translate();
        Translate wall5Position = new Translate();

        Wall wall1 = new Wall(1,1, Color.GRAY, wall1Position);
        Wall wall2 = new Wall(1,1, Color.GRAY, wall2Position);
        Wall wall3 = new Wall(1,1, Color.GRAY, wall3Position);
        Wall wall4 = new Wall(1,1, Color.GRAY, wall4Position);
        Wall wall5 = new Wall(1,1, Color.GRAY, wall5Position);

        switch (level) {
            case 1:
                wall1Position = new Translate(
                        Main.WINDOW_WIDTH / 2 - Main.WALL_WIDTH / 2,
                        Main.WINDOW_HEIGHT * 0.25 - Main.WALL_HEIGHT / 2
                );
                wall1 = new Wall(Main.WALL_WIDTH, Main.WALL_HEIGHT, Color.GRAY, wall1Position);
                wall2Position = new Translate(
                        Main.WINDOW_WIDTH / 2 - Main.WALL_HEIGHT * 3 / 2,
                        Main.WINDOW_HEIGHT * 0.5
                );
                wall2 = new Wall(Main.WALL_HEIGHT, Main.WALL_WIDTH, Color.GRAY, wall2Position);
                wall3Position = new Translate (
                        Main.WINDOW_WIDTH / 2 + Main.WALL_HEIGHT / 2,
                        Main.WINDOW_HEIGHT * 0.5
                );
                wall3 = new Wall(Main.WALL_HEIGHT, Main.WALL_WIDTH, Color.GRAY, wall3Position );
                break;
            case 2:
                wall1Position = new Translate(
                        Main.PLAYER_WIDTH,
                        Main.WINDOW_HEIGHT / 2
                );
                wall1 = new Wall(Main.WALL_HEIGHT, Main.WALL_WIDTH * 3, Color.GRAY, wall1Position);
                wall2Position = new Translate(
                        Main.WINDOW_WIDTH - Main.PLAYER_WIDTH - Main.WALL_HEIGHT,
                        Main.WINDOW_HEIGHT / 2
                );
                wall2 = new Wall(Main.WALL_HEIGHT, Main.WALL_WIDTH * 3, Color.GRAY, wall2Position);
                wall3Position = new Translate (
                        Main.WINDOW_WIDTH / 2 - Main.WALL_WIDTH * 3 / 2,
                        Main.WINDOW_HEIGHT / 2
                );
                wall3 = new Wall(Main.WALL_WIDTH * 3, Main.WALL_WIDTH * 3, Color.GRAY, wall3Position );
                wall4Position = new Translate (
                        Main.WINDOW_WIDTH / 2 - Main.WALL_WIDTH * 4 ,
                        Main.WINDOW_HEIGHT * 0.25 - Main.WALL_WIDTH * 4
                );
                wall4 = new Wall(Main.WALL_WIDTH * 8, Main.WALL_WIDTH * 8, Color.GRAY, wall4Position );
                break;
            case 3:
                wall1Position = new Translate(
                        Main.WINDOW_WIDTH   - Main.WALL_WIDTH * 2,
                        Main.WINDOW_HEIGHT / 2 - Main.PLAYER_WIDTH
                );
                wall1 = new Wall(Main.WALL_WIDTH * 4, Main.WINDOW_HEIGHT / 2, Color.GRAY, wall1Position);
                wall2Position = new Translate(
                        Main.WINDOW_WIDTH * 3 / 2 - Main.WALL_WIDTH * 4 ,
                        Main.WINDOW_HEIGHT * 0.25 - Main.WALL_WIDTH * 4
                );
                wall2 = new Wall(Main.WALL_WIDTH * 8, Main.WALL_WIDTH * 8, Color.GRAY, wall2Position);
                wall3Position = new Translate (
                        Main.WINDOW_WIDTH * 3 / 2 - Main.WALL_WIDTH * 4 ,
                        Main.WINDOW_HEIGHT - (Main.WINDOW_HEIGHT * 0.25 + Main.WALL_WIDTH * 4)
                );
                wall3 = new Wall(Main.WALL_WIDTH * 8, Main.WALL_WIDTH * 8, Color.GRAY, wall3Position );
                wall4Position = new Translate (
                        Main.WINDOW_WIDTH / 2 - Main.WALL_WIDTH * 4 ,
                        Main.WINDOW_HEIGHT * 0.25 - Main.WALL_WIDTH * 4
                );
                wall4 = new Wall(Main.WALL_WIDTH * 8, Main.WALL_WIDTH * 8, Color.GRAY, wall4Position );
                wall5Position = new Translate (
                        Main.WINDOW_WIDTH - Main.WALL_HEIGHT ,
                        Main.WINDOW_HEIGHT * 0.25 - Main.WALL_WIDTH / 2
                );
                wall5 = new Wall(Main.WALL_HEIGHT * 2, Main.WALL_WIDTH , Color.GRAY, wall5Position );
                break;
        }

        walls[0] = wall1;
        walls[1] = wall2;
        walls[2] = wall3;
        walls[3] = wall4;
        walls[4] = wall5;

        for (int i = 0; i < LEVEL_WALL_CNT[level]; i++) {
            root.getChildren().add(walls[i]);
        }
    }

    public static void addFence (Wall[] fences, Group root, double width, double height, int levelSize) {
        Image fenceImage = new Image (Main.class.getClassLoader().getResourceAsStream("fence.jpg"));
        ImagePattern fencePattern = new ImagePattern( fenceImage );

        Translate fence1Position = new Translate (
                0,
                0
        );
        Wall fence1 = new Wall(width * levelSize , Main.PLAYER_WIDTH, Color.GRAY, fence1Position );
        fence1.setFill( fencePattern );
        root.getChildren().add(fence1);

        Translate fence2Position = new Translate (
                0,
                height - Main.PLAYER_WIDTH
        );
        Wall fence2 = new Wall(width * levelSize , Main.PLAYER_WIDTH, Color.GRAY, fence2Position );
        fence2.setFill( fencePattern );
        root.getChildren().add(fence2);

        Translate fence3Position = new Translate (
                0,
                0
        );
        Wall fence3 = new Wall(Main.PLAYER_WIDTH , height, Color.GRAY, fence3Position );
        fence3.setFill( fencePattern );
        root.getChildren().add(fence3);

        Translate fence4Position = new Translate (
                width * levelSize - Main.PLAYER_WIDTH,
                0
        );
        Wall fence4 = new Wall(Main.PLAYER_WIDTH , height, Color.GRAY, fence4Position );
        fence4.setFill( fencePattern );
        root.getChildren().add(fence4);

        fences[0] = fence1;
        fences[1] = fence2;
        fences[2] = fence3;
        fences[3] = fence4;
    }

    public static void addSpeedModifiers ( Modifier[] modifiers, Group root, int level) {
        Translate modifier1position = new Translate(
                Main.WINDOW_WIDTH / 6,
                Main.WINDOW_HEIGHT / 7
        );
        Translate modifier2position = new Translate(
                Main.WINDOW_WIDTH * 5 / 6 - Main.MODIFIER_WIDTH / 2,
                Main.WINDOW_HEIGHT / 7
        );
        Translate modifier3position = new Translate(
                Main.WINDOW_WIDTH / 6,
                Main.WINDOW_HEIGHT * 5 / 7
        );
        Translate modifier4position = new Translate(
                Main.WINDOW_WIDTH * 5 / 6 - Main.MODIFIER_WIDTH / 2,
                Main.WINDOW_HEIGHT * 5 / 7
        );

        Modifier modifier1 = new Modifier( Main.MODIFIER_WIDTH, Main.MODIFIER_WIDTH,
                modifier1position, Main.BALL_SLOW_MODIFIER);
        Modifier modifier2 = new Modifier( Main.MODIFIER_WIDTH, Main.MODIFIER_WIDTH,
                modifier2position, Main.BALL_ACCELERATION_MODIFIER);
        Modifier modifier3 = new Modifier( Main.MODIFIER_WIDTH, Main.MODIFIER_WIDTH,
                modifier3position, Main.BALL_ACCELERATION_MODIFIER);
        Modifier modifier4 = new Modifier( Main.MODIFIER_WIDTH, Main.MODIFIER_WIDTH,
                modifier4position, Main.BALL_SLOW_MODIFIER);

        switch (level) {
            case 1:
                break;
            case 2:
                modifier1position = new Translate(
                        Main.WINDOW_WIDTH / 2 - Main.WALL_WIDTH * 3,
                        Main.WINDOW_HEIGHT / 2 - Main.WALL_WIDTH * 3 / 2
                );
                modifier2position = new Translate(
                        Main.WINDOW_WIDTH / 2 - Main.WALL_WIDTH * 5 ,
                        Main.WINDOW_HEIGHT * 0.25 - Main.WALL_WIDTH * 5
                );
                modifier3position = new Translate(
                        Main.PLAYER_WIDTH,
                        Main.PLAYER_WIDTH
                );
                modifier4position = new Translate(
                        Main.WINDOW_WIDTH - Main.PLAYER_WIDTH - Main.MODIFIER_WIDTH / 2,
                        Main.PLAYER_WIDTH
                );

                modifier1 = new Modifier( Main.WALL_WIDTH * 6, Main.WALL_WIDTH * 6,
                        modifier1position, Main.BALL_ACCELERATION_MODIFIER);
                modifier2 = new Modifier( Main.WALL_WIDTH * 10, Main.WALL_WIDTH * 10,
                        modifier2position, Main.BALL_ACCELERATION_MODIFIER);
                modifier3 = new Modifier( Main.MODIFIER_WIDTH / 2, Main.WINDOW_HEIGHT / 2,
                        modifier3position, Main.BALL_SLOW_MODIFIER);
                modifier4 = new Modifier( Main.MODIFIER_WIDTH / 2, Main.WINDOW_HEIGHT / 2,
                        modifier4position, Main.BALL_SLOW_MODIFIER);
                break;
            case 3:
                modifier1position = new Translate(
                        Main.PLAYER_WIDTH,
                        Main.PLAYER_WIDTH
                );
                modifier2position = new Translate(
                        Main.WINDOW_WIDTH * 2 - Main.PLAYER_WIDTH - Main.MODIFIER_WIDTH * 2,
                        Main.PLAYER_WIDTH
                );
                modifier3position = new Translate(
                        Main.PLAYER_WIDTH,
                        Main.WINDOW_HEIGHT - Main.PLAYER_WIDTH - Main.MODIFIER_WIDTH * 2
                );
                modifier4position = new Translate(
                        Main.WINDOW_WIDTH * 2 - Main.PLAYER_WIDTH - Main.MODIFIER_WIDTH * 3,
                        Main.WINDOW_HEIGHT - Main.PLAYER_WIDTH - Main.MODIFIER_WIDTH * 3
                );

                modifier1 = new Modifier( Main.MODIFIER_WIDTH * 3, Main.MODIFIER_WIDTH * 3,
                        modifier1position, Main.BALL_ACCELERATION_MODIFIER);
                modifier2 = new Modifier( Main.MODIFIER_WIDTH * 2, Main.MODIFIER_WIDTH * 2,
                        modifier2position, Main.BALL_SLOW_MODIFIER);
                modifier3 = new Modifier( Main.MODIFIER_WIDTH * 2, Main.MODIFIER_WIDTH * 2,
                        modifier3position, Main.BALL_SLOW_MODIFIER);
                modifier4 = new Modifier( Main.MODIFIER_WIDTH * 3, Main.MODIFIER_WIDTH * 3,
                        modifier4position, Main.BALL_ACCELERATION_MODIFIER);
                break;
        }

        modifiers[0] = modifier1;
        modifiers[1] = modifier2;
        modifiers[2] = modifier3;
        modifiers[3] = modifier4;

        for (int i = 0; i < LEVEL_MODIFIER_CNT; i++) {
            root.getChildren().add( modifiers[i] );
        }

    }

    public static void addFourHoles ( Hole[] holes, Group root, int level ) {
        Translate hole1Position = new Translate (
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.4
        );
        Translate hole2Position = new Translate (
                Main.WINDOW_WIDTH / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Translate hole3Position = new Translate (
                Main.WINDOW_WIDTH * 2 / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Translate hole4Position = new Translate (
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.1
        );

        switch (level) {
            case 1:
                break;
            case 2:
                hole1Position = new Translate(
                        Main.WINDOW_WIDTH / 2,
                        Main.WINDOW_HEIGHT * 0.4
                );
                hole2Position = new Translate(
                        Main.PLAYER_WIDTH * 4,
                        Main.WINDOW_HEIGHT * 0.45
                );
                hole3Position = new Translate(
                        Main.WINDOW_WIDTH - Main.PLAYER_WIDTH * 4,
                        Main.WINDOW_HEIGHT * 0.45
                );
                hole4Position = new Translate(
                        Main.WINDOW_WIDTH / 2,
                        Main.WINDOW_HEIGHT * 0.1
                );
                break;
            case 3:
                hole1Position = new Translate(
                        Main.WINDOW_WIDTH,
                        Main.WINDOW_HEIGHT * 0.3
                );
                hole2Position = new Translate(
                        Main.WINDOW_WIDTH,
                        Main.WINDOW_HEIGHT * 0.2
                );
                hole3Position = new Translate(
                        Main.WINDOW_WIDTH * 3 / 2 ,
                        Main.WINDOW_HEIGHT / 2
                );
                hole4Position = new Translate(
                        Main.WINDOW_WIDTH * 3 / 2,
                        Main.WINDOW_HEIGHT * 0.9
                );
                break;
        }

        Hole hole1 = new Hole ( Main.HOLE_RADIUS, hole1Position, Hole.HOLE_POINTS_NEAR );
        Hole hole2 = new Hole ( Main.HOLE_RADIUS, hole2Position, Hole.HOLE_POINTS_MID );
        Hole hole3 = new Hole ( Main.HOLE_RADIUS, hole3Position, Hole.HOLE_POINTS_MID );
        Hole hole4 = new Hole ( Main.HOLE_RADIUS, hole4Position, Hole.HOLE_POINTS_FAR );

        holes[0] = hole1;
        holes[1] = hole2;
        holes[2] = hole3;
        holes[3] = hole4;

        for (int i = 0; i < LEVEL_HOLES_CNT; i++) {
            root.getChildren().add( holes[i] );
        }
    }
    public static void addPortals (Portal[] portals, Group root) {
        Translate portal1Position = new Translate (
                Main.WINDOW_WIDTH * 1 / 8,
                Main.WINDOW_HEIGHT * 0.4
        );
        Translate portal2Position = new Translate (
                Main.WINDOW_WIDTH * 15 / 8,
                Main.WINDOW_HEIGHT * 0.4
        );

        Portal portal1 = new Portal ( Main.HOLE_RADIUS * 1.5, portal1Position, 1 );
        Portal portal2 = new Portal ( Main.HOLE_RADIUS * 1.5 , portal2Position, 2 );

        portals[0] = portal1;
        portals[1] = portal2;

        root.getChildren().addAll( portal1, portal2 );
    }

    public static void addIndicator(Scale scale, Group root) {
        Rectangle speedIndicator = new Rectangle(Main.PLAYER_WIDTH / 2, Main.WINDOW_HEIGHT, Color.RED);
        root.getChildren().add(speedIndicator);

        speedIndicator.getTransforms().addAll(
                new Translate(Main.PLAYER_WIDTH / 2, Main.WINDOW_HEIGHT),
                new Rotate(180),
                scale
        );
    }

    public static SpaceShip addSpaceShip(Translate translate, double windowWidth, Group root) {
        double shipRadius = Main.HOLE_RADIUS * 2;
        double windowHeight = Main.WINDOW_HEIGHT;
        double startX = translate.getX() + shipRadius / 2;
        double startY = translate.getY() + shipRadius / 2;

        double endX = startX < windowWidth / 2 ? Math.random() * windowWidth / 2 + windowWidth / 2 :
            Math.random() * windowWidth;
        double endY = startY <  windowHeight / 2 ? Math.random() * windowHeight / 2 + windowHeight / 2 :
                Math.random() * windowHeight;

        Point2D speed = new Point2D ( endX - startX, endY - startY ).normalize().multiply(200);

        SpaceShip spaceShip = new SpaceShip(shipRadius, translate, speed);
        root.getChildren().add(spaceShip);

        return spaceShip;
    }
        /*Translate ballPosition = this.player.getBallPosition ( );
        Point2D ballSpeed    = this.player.getSpeed ( ).multiply ( ballSpeedFactor );

        this.ball = new Ball ( Main.BALL_RADIUS, ballPosition, ballSpeed );
        this.root.getChildren ( ).addAll ( this.ball );*/

    /*public Point2D getSpeed ( ) {
        double startX = this.position.getX ( ) + this.width / 2;
        double startY = this.position.getY ( ) + this.height - baseRadius;

        double endX = startX + Math.sin ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;
        double endY = startY - Math.cos ( Math.toRadians ( this.rotate.getAngle ( ) ) ) * this.height;

        Point2D result = new Point2D ( endX - startX, endY - startY );

        return result.normalize ( );
    }*/

    public static boolean playerCircleIntersect(Circle circle){
        Bounds circleBounds = circle.getBoundsInParent ( );

        double circleX      = circleBounds.getCenterX ( );
        double circleY      = circleBounds.getCenterY ( );
        double circleRadius = circle.getRadius ( );

        double playerX      = Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2;
        double playerY      = Main.WINDOW_HEIGHT - Main.PLAYER_HEIGHT;

        double playerRadius = Main.PLAYER_HEIGHT + Main.PLAYER_WIDTH;

        double distanceX = playerX - circleX;
        double distanceY = playerY - circleY;

        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        boolean result = distanceSquared < ( (playerRadius + circleRadius) * (playerRadius + circleRadius) );

        return result;
    }

    public static Translate generateSpaceShipPosition(double windowWidth, double windowHeight) {
        double x;
        double y;
        int side1 = (int) Math.floor(Math.random() * 2);    // levo/desno ili gore/dole
        int side2 = (int) Math.floor(Math.random() * 2);    // levo ili desno / gore ili dole
        x = side1 == 0 ? Math.random() * (windowWidth + 2 * Main.HOLE_RADIUS * 2) - Main.HOLE_RADIUS * 2:
                side2 == 0 ? - Main.HOLE_RADIUS * 2 : windowWidth - Main.HOLE_RADIUS * 2;
        y = side1 == 1 ?Math.random() * (windowHeight + 2 * Main.HOLE_RADIUS * 2) - Main.HOLE_RADIUS * 2:
                side2 == 0 ? - Main.HOLE_RADIUS * 2 : windowHeight - Main.HOLE_RADIUS * 2;

        return new Translate(x, y);
    }
}
