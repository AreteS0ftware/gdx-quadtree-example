package it.aretesoftware.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class CameraAndSpritesInput {

    private final QuadtreeExample example;

    CameraAndSpritesInput(QuadtreeExample example) {
        this.example = example;
    }

    void HandleInput() {
        HandleEntities();
        HandleCamera();
    }

    //

    private void HandleEntities() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            MoveEntities(5f, 0);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            MoveEntities(-5f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            MoveEntities(0, +5f);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            MoveEntities(0, -5f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            example.RandomizeEntities();
        }
    }

    private void MoveEntities(float x, float y) {
        Array<SpriteEntity> entities  = example.sprites;
        for (SpriteEntity entity : entities) {
            Rectangle entityBounds = entity.GetBounds();
            entityBounds.setPosition(entityBounds.x + x, entityBounds.y + y);
        }
    }

    //

    private void HandleCamera() {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            MoveCamera(+15f, 0);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            MoveCamera(-15f, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            MoveCamera(0, +15f);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            MoveCamera(0, -15f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            ZoomCamera(+0.1f);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            ZoomCamera(-0.1f);
        }

        OrthographicCamera camera = (OrthographicCamera) example.viewport.getCamera();
        camera.update();

        Rectangle cameraBounds = example.cameraBounds;
        Frustum frustum = camera.frustum;
        Vector3 min = frustum.planePoints[0];
        Vector3 max = frustum.planePoints[2];
        float diffX = 300 * camera.zoom;
        float diffY = 200 * camera.zoom;
        cameraBounds.set(min.x + diffX, min.y + diffY, max.x - min.x - (diffX * 2f), max.y - min.y - (diffY * 2f));
    }

    private void MoveCamera(float xOffset, float yOffset) {
        OrthographicCamera camera = (OrthographicCamera) example.viewport.getCamera();
        camera.position.x += xOffset;
        camera.position.y += yOffset;
    }

    private void ZoomCamera(float zoomOffset) {
        OrthographicCamera camera = (OrthographicCamera) example.viewport.getCamera();
        camera.zoom += zoomOffset;
    }

}
