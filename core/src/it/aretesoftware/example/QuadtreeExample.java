package it.aretesoftware.example;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import it.aretesoftware.quadtree.QuadTreeItem;
import it.aretesoftware.quadtree.QuadTreeRoot;

public class QuadtreeExample extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapes;
	Texture texture;

	Rectangle cameraBounds, rootBounds, spritesBounds;
	Viewport viewport;
	QuadTreeRoot<SpriteEntity> root;
	Array<SpriteEntity> sprites;

	CameraAndSpritesInput cameraAndSpritesInput;
	UserInterface userInterface;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shapes = new ShapeRenderer();
		texture = new Texture("badlogic.jpg");

		viewport = new ScreenViewport();
		OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();
		camera.zoom = 7.5f;
		camera.position.set(30000, 30000, 0);
		camera.update();
		cameraBounds = new Rectangle();

		rootBounds = new Rectangle(-20000, -20000, 100000, 100000);
		root = new QuadTreeRoot<>(rootBounds, 6, 4, 32);

		spritesBounds = new Rectangle(10000, 10000, 40000, 40000);
		CreateEntities(1000);

		cameraAndSpritesInput = new CameraAndSpritesInput(this);
		userInterface = new UserInterface(this);
	}

	void CreateEntities(int size) {
		sprites = new Array<>(size);
		for (int i = 0; i < size; i++) {
			sprites.add(new SpriteEntity(texture));
		}
		RandomizeEntities();
	}

	void RandomizeEntities() {
		Random random = new Random();
		Array<SpriteEntity> entities  = sprites;
		Rectangle area = spritesBounds;
		for (SpriteEntity entity : entities) {
			Texture texture = entity.GetTexture();
			float textureWidth = texture.getWidth();
			float textureHeight = texture.getHeight();
			float x = random.nextInt((int)area.getWidth()) + area.getX();
			if (x + textureWidth > area.getX() + area.getWidth()) {
				x -= textureWidth;
			}
			float y = random.nextInt((int)area.getHeight()) + area.getY();
			if (y + textureHeight > area.getY() + area.getHeight()) {
				y -= textureHeight;
			}
			Rectangle entityBounds = entity.GetBounds();
			entityBounds.setPosition(x, y);
		}
	}

	//

	@Override
	public void render () {
		cameraAndSpritesInput.HandleInput();
		Draw();
	}

	private void Draw() {
		ScreenUtils.clear(1, 1, 1, 1);

		//FIXME: Unsupported by GWT
		//long start = System.nanoTime();
		DrawEntities();
		//long end = System.nanoTime();
		//float timeToRender = (end - start) / 100000f;

		DrawDebugLines();
		userInterface.Draw(Gdx.graphics.getDeltaTime());
	}

	private void DrawEntities() {
		Camera camera = viewport.getCamera();
		viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (userInterface.IsQuadTreeDisabled()) {
			DrawWithoutQuadTree();
		}
		else {
			DrawWithQuadTree();
		}
		batch.end();
	}

	private void DrawDebugLines() {
		Camera camera = viewport.getCamera();
		viewport.apply();
		shapes.setProjectionMatrix(camera.combined);
		shapes.begin(ShapeRenderer.ShapeType.Line);

		if (!userInterface.IsQuadTreeDisabled()) {
			root.Render(shapes);
			shapes.flush();
		}

		shapes.setColor(Color.BLUE);
		Gdx.gl.glLineWidth(5f);
		shapes.rect(cameraBounds.x, cameraBounds.y, cameraBounds.width, cameraBounds.height);
		shapes.flush();

		shapes.setColor(Color.WHITE);
		Gdx.gl.glLineWidth(1f);
		shapes.end();
	}

	private void DrawWithQuadTree() {
		root.Clear();
		for (SpriteEntity entity : sprites) {
			QuadTreeItem<SpriteEntity> item = root.ObtainItem();
			item.init(entity, entity.GetBounds());
			root.Insert(item);
		}

		Array<QuadTreeItem<SpriteEntity>> list = root.Retrieve(cameraBounds);
		for (QuadTreeItem<SpriteEntity> item : list) {
			item.GetObject().Draw(batch);
		}
	}

	private void DrawWithoutQuadTree() {
		for (SpriteEntity entity : sprites) {
			if (entity.IsVisible(cameraBounds)) {
				entity.Draw(batch);
			}
		}
	}

	//
	
	@Override
	public void dispose () {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		userInterface.Resize(width, height);
	}
}
