package com.tint.specular.states;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tint.specular.Setting;
import com.tint.specular.Specular;

public class SettingsMenuState extends State {
	//FIELDS
	private boolean useParticles;
	private boolean usePowerUps;
	
	private float sensitivity;
	private int controls;

//	private Texture background;
	private HashMap<String, Setting> settings = new HashMap<String, Setting>();
	
	//LibGDX scene stuff
	private Stage stage;
	private Skin skin;
	
	public SettingsMenuState(Specular game) {
		super(game);
		this.game = game;
		
		/*settings.put("Resolution", new Setting(Gdx.graphics.getDisplayModes()[0].width + "x" + Gdx.graphics.getDisplayModes()[0].height));
		settings.get("Resolution").addValue("600x480");
		settings.get("Resolution").setButtonDimensions(40, 40, 40, 40);
		settings.get("Resolution").setButtonPositions(Gdx.graphics.getWidth() / 2 - 140, 200, Gdx.graphics.getWidth() / 2 + 100, 200);
		
		settings.put("Particles", new Setting("On", "Off"));
		settings.get("Particles").setButtonDimensions(40, 40, 40, 40);
		settings.get("Particles").setButtonPositions(Gdx.graphics.getWidth() / 2 - 140, 300, Gdx.graphics.getWidth() / 2 + 100, 300);
		
		settings.put("Controls", new Setting("Accelerometer and stick", "Two stick"));
		settings.get("Controls").setButtonDimensions(40, 40, 40, 40);
		settings.get("Controls").setButtonPositions(Gdx.graphics.getWidth() / 2 - 140, 400, Gdx.graphics.getWidth() / 2 + 100, 400);
		*/
		/*------------------------------------------------------------------------------------------------------*/
		Texture btnTextures = new Texture(Gdx.files.internal("graphics/mainmenu/Buttons.png"));
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin();
		
		//Adding a default font
		skin.add("default", new BitmapFont());
		
		//Button style based on the skins
		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.up = (Drawable) new TextureRegion(btnTextures, 0, 0, 32, 32);
		btnStyle.down = (Drawable) new TextureRegion(btnTextures, 33, 0, 32, 32);
		btnStyle.over = (Drawable) new TextureRegion(btnTextures, 0, 33, 32, 32);
		btnStyle.checked =  (Drawable) new TextureRegion(btnTextures, 33, 33, 32, 32);
		btnStyle.font = skin.getFont("default");
		skin.add("default", btnStyle);
		
		//Scrollpane style
		ScrollPaneStyle paneStyle = new ScrollPaneStyle();
		paneStyle.background = null;
		paneStyle.hScroll = null;
		paneStyle.hScrollKnob = null;
		
		//List style
		ListStyle listStyle = new ListStyle();
		listStyle.font = skin.getFont("default");
		listStyle.fontColorSelected = Color.LIGHT_GRAY;
		listStyle.fontColorUnselected = Color.DARK_GRAY;
		listStyle.selection = null;
		
		//Dropdown list style
		SelectBoxStyle boxStyle = new SelectBoxStyle();
		boxStyle.background = null;
		boxStyle.scrollStyle = paneStyle;
		boxStyle.listStyle = listStyle;
		boxStyle.font = skin.getFont("default");
		boxStyle.fontColor = Color.WHITE;
		boxStyle.disabledFontColor = Color.GRAY;
		
		//A container holding scene2d objects
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		//Widgets
		/*________________________________________________________________*/
		
		//back button
		TextButton back = new TextButton("Click me!", skin);
		
		
		//Adding a input listener to the button
		back.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("down");
				return true;
			}
		});
		
		//Adding the button to the table container
		table.add(back);
		
		//Dropdown list
		SelectBox list = new SelectBox(null, boxStyle);
		table.add(list);
	}

	@Override
	public void render(float delta) {
		//Clearing screen
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Positioning camera
		/*game.camera.position.set(0, 0, 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);*/
		
		stage.act(10);
		stage.draw();
		//Drawing
		/*game.batch.begin();
//		game.batch.draw(background, 0, 0);
		
		//Drawing headers
		Util.writeCentered(game.batch, font, "Controls", Gdx.graphics.getWidth() / 2,
				settings.get("Controls").getNextButton().getY() + settings.get("Controls").getNextButton().getHeight()
				+ font.getLineHeight());
		
		Util.writeCentered(game.batch, font, "Particles", Gdx.graphics.getWidth() / 2,
				settings.get("Particles").getNextButton().getY() + settings.get("Particles").getNextButton().getHeight()
				+ font.getLineHeight());
		
		Util.writeCentered(game.batch, font, "Resolution", Gdx.graphics.getWidth() / 2,
				settings.get("Resolution").getNextButton().getY() + settings.get("Resolution").getNextButton().getHeight()
				+ font.getLineHeight());
		
		for(String str : settings.keySet()) {
			settings.get(str).getNextButton().renderShape(game.shape);
			settings.get(str).getPreviousButton().renderShape(game.shape);
			
			Util.writeCentered(game.batch, font, settings.get(str).getSelectedValue().toString(),
					Gdx.graphics.getWidth() / 2, settings.get(str).getPreviousButton().getY() +
					settings.get(str).getPreviousButton().getHeight() / 2);
		}
		
		game.batch.end();
		*/
	}
	
	public void update() {
		
	}
	
	public void updateButtons(float x, float y) {
		for(String str : settings.keySet()) {
			/*if(back.isOver(x, y, true)) {
				game.enterState(States.MAINMENUSTATE);
			} else */if(settings.get(str).getNextButton().isOver(x, y, true)) {
				settings.get(str).setIndex(settings.get(str).getIndex() + 1);
			} else if(settings.get(str).getPreviousButton().isOver(x, y, true)) {
				settings.get(str).setIndex(settings.get(str).getIndex() - 1);
			}
		}
	}
	
	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	public void useParticles(boolean use) {
		useParticles = use;
	}
	
	public void usePowerUps(boolean use) {
		usePowerUps = use;
	}
	
	public void setControls(int controls) {
		this.controls = controls;
	}
	
	public boolean isUsingParticles() {
		return useParticles;
	}
	
	public boolean isUsingPowerUps() {
		return usePowerUps;
	}
	
	public int getScreenWidth() {
		return Integer.parseInt(settings.get("Resolution").getSelectedValue().toString().split("x")[0]);
	}
	
	public int getScreenHeight() {
		return Integer.parseInt(settings.get("Resolution").getSelectedValue().toString().split("x")[1]);
	}
	
	public int getControls() {
		return controls;
	}
	
	public float getSensitivity() {
		return sensitivity;
	}
	
	public HashMap<String, Setting> getSettings() {
		return settings;
	}

	@Override
	public void show() {
		super.show();
//		Gdx.input.setInputProcessor(new MenuInputProcessor(this));
	}
}