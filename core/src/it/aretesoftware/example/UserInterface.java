package it.aretesoftware.example;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UserInterface {

    private final QuadtreeExample example;
    private final Stage stage;

    private Label memoryLabel, fpsLabel, timeToRenderLabel;
    private TextField spritesCountTextField, maxLevelTextField, maxItemsPerNodeTextField;
    private CheckBox disableQuadTreeCheckBox;

    UserInterface(QuadtreeExample example) {
        this.example = example;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), example.batch);
        Gdx.input.setInputProcessor(stage);

        PopulateStage();
    }

    //

    private void PopulateStage() {
        Skin skin = new Skin(Gdx.files.internal("plain-james/plain-james-ui.json"));
        CreateInfoTable(skin);
        CreateSpritesAndQuadTreeControlsTable(skin);
        CreateBindingsTable(skin);
    }

    private void CreateInfoTable(Skin skin) {
        memoryLabel = new Label("", skin);
        fpsLabel = new Label("", skin);
        timeToRenderLabel = new Label("", skin);
        //
        Table informationTable = new Table(skin);
        informationTable.setFillParent(true);
        //FIXME: Unsupported by GWT
        //informationTable.add(timeToRenderLabel).top().left();
        //informationTable.row();
        informationTable.add(memoryLabel).top().left();
        informationTable.row();
        informationTable.add(fpsLabel).expand().top().left();
        stage.addActor(informationTable);
    }

    private void CreateSpritesAndQuadTreeControlsTable(Skin skin) {
        TextButton randomizeSpritesTextButton = new TextButton("Randomize Sprites", skin);
        randomizeSpritesTextButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                example.RandomizeEntities();
            }
        });
        spritesCountTextField = new TextField(String.valueOf(example.sprites.size), skin);
        spritesCountTextField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        spritesCountTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    if (c != 10) return;
                }
                else if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
                    if (c != 13) return;
                }
                String text = textField.getText();
                int newSpritesCount = Integer.parseInt(text);
                example.CreateEntities(newSpritesCount);
            }
        });
        spritesCountTextField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) {
                    int spritesCount = example.sprites.size;
                    spritesCountTextField.setText(String.valueOf(spritesCount));
                }
            }
        });
        maxLevelTextField = new TextField(String.valueOf(example.root.GetMaxLevel()), skin);
        maxLevelTextField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        maxLevelTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    if (c != 10) return;
                }
                else if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
                    if (c != 13) return;
                }
                String text = textField.getText();
                int newMaxLevel = Integer.parseInt(text);
                example.root.SetMaxLevel(newMaxLevel);
            }
        });
        maxLevelTextField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) {
                    int maxLevel = example.root.GetMaxLevel();
                    maxLevelTextField.setText(String.valueOf(maxLevel));
                }
            }
        });
        //
        maxItemsPerNodeTextField = new TextField(String.valueOf(example.root.GetMaxItemsPerNode()), skin);
        maxItemsPerNodeTextField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        maxItemsPerNodeTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    if (c != 10) return;
                }
                else if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
                    if (c != 13) return;
                }
                String text = textField.getText();
                int newMaxItemsPerNode = Integer.parseInt(text);
                example.root.SetMaxItemsPerNode(newMaxItemsPerNode);
            }
        });
        maxItemsPerNodeTextField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) {
                    int maxItemsPerNode = example.root.GetMaxItemsPerNode();
                    maxItemsPerNodeTextField.setText(String.valueOf(maxItemsPerNode));
                }
            }
        });
        //
        disableQuadTreeCheckBox = new CheckBox(" Disable QuadTree", skin);
        //
        HorizontalGroup spritesCount = new HorizontalGroup();
        spritesCount.addActor(new Label("Sprites: ", skin));
        spritesCount.addActor(spritesCountTextField);

        HorizontalGroup maxLevel = new HorizontalGroup();
        maxLevel.addActor(new Label("Max Level: ", skin));
        maxLevel.addActor(maxLevelTextField);

        HorizontalGroup maxItemsPerNode = new HorizontalGroup();
        maxItemsPerNode.addActor(new Label("Max Items Per Node: ", skin));
        maxItemsPerNode.addActor(maxItemsPerNodeTextField);

        Table widgetsTable = new Table(skin);
        widgetsTable.setFillParent(true);
        widgetsTable.add(randomizeSpritesTextButton).top().right();
        widgetsTable.row();
        widgetsTable.add(spritesCount).top().right();
        widgetsTable.row();
        widgetsTable.add(maxLevel).top().right();
        widgetsTable.row();
        widgetsTable.add(maxItemsPerNode).top().right();
        widgetsTable.row();
        widgetsTable.add(disableQuadTreeCheckBox).expand().top().right();
        stage.addActor(widgetsTable);
    }

    private void CreateBindingsTable(Skin skin) {
        Table table = new Table(skin);
        table.setFillParent(true);
        table.add("Camera Zoom In/Out: E/Q").expand().bottom().left();
        table.row();
        table.add("Camera Movement: WASD").bottom().left();
        table.row();
        table.add("Sprites Movement: Arrow Keys").bottom().left();
        stage.addActor(table);
    }

    //

    void Draw(float timeToRender) {
        memoryLabel.setText("Heap memory (MB): " + (Gdx.app.getJavaHeap() / 1000000));
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        timeToRenderLabel.setText("Time to render (ms): " + timeToRender);
        stage.act();

        stage.getViewport().apply();
        stage.draw();
    }

    boolean IsQuadTreeDisabled() {
        return disableQuadTreeCheckBox.isChecked();
    }

    void Resize(int width, int height) {
        FitViewport viewport = (FitViewport) stage.getViewport();
        viewport.setWorldSize(width, height);
        viewport.update(width, height, true);
    }

}
