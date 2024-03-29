/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private final List<Sprite> spriteBombs = new ArrayList<>();
    private final List<Sprite> spriteMonsters = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();
        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight);
        // Create decor sprites
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));

        spritePlayer = SpriteFactory.createPlayer(layer, player);
        game.getWorld().getListMonster().forEach(monster -> spriteMonsters.add(SpriteFactory.createMonster(layer, monster)));
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput();

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput() {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isEnter()) {
            player.requestDoorOpen();
        }
        if (input.isSpace()) {
            player.BombRequest();
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput();
            }
        }.start();
    }


    private void update(long now) {
        player.update(now);
        game.getListBombs().forEach(L -> {
            L.forEach(bomb -> bomb.stateCalculator(now));
            L.removeIf(Bomb::isRemovable);
        });
        game.getListMonsters().forEach(L -> {
            L.forEach(monster -> monster.update(now));
            L.removeIf(monster -> monster.IsAlive());
        });
        if (game.hasLevelChanged()) {
            stage.close();
            initialize(stage, game);
            game.levelChangedRequestDone();
        }
        if (game.getWorld().hasChanged()) {
            sprites.forEach(Sprite::remove);
            sprites.clear();
            game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
            // Gestion des bombes
            spriteBombs.forEach(Sprite::remove);
            spriteBombs.clear();
            game.getWorld().getListBomb().forEach(bomb -> spriteBombs.add(SpriteFactory.createBomb(layer, bomb)));
            // Gestion des monstres
            spriteMonsters.forEach(Sprite::remove);
            spriteMonsters.clear();
            game.getWorld().getListMonster().forEach(monster -> spriteMonsters.add(SpriteFactory.createMonster(layer, monster)));

            game.getWorld().ChangeRequestDone();
        }
        if (player.IsAlive()) {
            gameLoop.stop();
            showMessage("Perdu !", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné !", Color.BLUE);
        }
    }


    private void render() {
        spriteBombs.forEach(Sprite::render);
        sprites.forEach(Sprite::render);
        spriteMonsters.forEach(Sprite::render);
        //last rendering to have player in the foreground
        spritePlayer.render();

    }

    public void start() {
        gameLoop.start();
    }
}
