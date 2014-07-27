package com.tint.specular.input; 
  
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GfxSettings;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.utils.Util;
  
public class SettingsInputProcessor extends InputAdapter { 
  
    private Specular game; 
    private boolean soundsMuted = true, musicMuted = true, backBtnPressed, controlsPressed, daeronPressed, warriyoPressed;
    private int graphics;
    private Sound btnSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/ButtonPress.ogg"));
      
    public SettingsInputProcessor(Specular game) {
        this.game = game; 
        soundsMuted = Specular.prefs.getBoolean("SoundsMuted");
        musicMuted = Specular.prefs.getBoolean("MusicMuted");
        graphics = Specular.prefs.getInteger("Graphics");
    }
    
    @Override
	public boolean keyUp(int keycode) {
    	if(keycode == Keys.BACK)
			game.enterState(States.MAINMENUSTATE);
    		GfxSettings.init();
    	
    	return false;
	}

	@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
        backBtnPressed = Util.isTouching(touchpointx, touchpointy, 80, Specular.camera.viewportHeight - 160, 435, 105, false); 
        controlsPressed = Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 1030, Specular.camera.viewportHeight - 155, 905, 110, false);
        
        daeronPressed = Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 650, Specular.camera.viewportHeight - 570, 500, 110, false);
        warriyoPressed = Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 650, Specular.camera.viewportHeight - 450, 500, 110, false);
        
        if(!soundsMuted && (backBtnPressed || controlsPressed)) {
        	btnSound.play();
        }
        return false; 
    } 
  
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	/*
    	 * Explanation over system below
    	 * 
    	 * 1. Particles, Music, Sound y-coordinate: coordinates taken(always same) from bottom left corner and adjusted because input(0, 0) is upper left corner,
    	 * Options-texture's height is 1080p
    	 * 
    	 * 2. Controls, Back y-coordinate: It is always same beacause its calculated by subtracting a predefined number from the screens height
    	 * 
    	 * 3. Controls x-coordinate: It is measured from the right side of the screen
    	 */
    	
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
          
        // Graphics
        if(Util.isTouching(touchpointx, touchpointy, 160, 1080 - 725 + Specular.camera.viewportHeight - 1080, 680, 220, false)) { 
        	graphics = graphics == 2 ? 0 : ++graphics;
        }
        
        // Music 
        else if(Util.isTouching(touchpointx, touchpointy, 160, 1080 - 490 + Specular.camera.viewportHeight - 1080, 550, 128, false)) {
            musicMuted = !musicMuted;
            if(musicMuted)
            	((MainmenuState) game.getState(States.MAINMENUSTATE)).stopMusic();
            else
            	((MainmenuState) game.getState(States.MAINMENUSTATE)).startMusic();
        } 
        // Sound 
        else if(Util.isTouching(touchpointx, touchpointy, 160, 1080 - 350 + Specular.camera.viewportHeight - 1080, 410, 128, false))
            soundsMuted = !soundsMuted; 

        // Controls
        else if(Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 1030, Specular.camera.viewportHeight - 155, 905, 110, false))
        	game.enterState(States.CONTROLSETUPSTATE);
        

//         Back 
        else if(Util.isTouching(touchpointx, touchpointy, 80, Specular.camera.viewportHeight - 160, 435, 105, false))
            game.enterState(States.MAINMENUSTATE);
        
        if(Util.isTouching(touchpointx, touchpointy, 80, Specular.camera.viewportHeight - 160, 435, 105, false))
           GfxSettings.init();

        else if(Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 650, Specular.camera.viewportHeight - 570, 500, 110, false))
        	Gdx.net.openURI("https://soundcloud.com/daerontrance");
        
        else if(Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 650, Specular.camera.viewportHeight - 450, 500, 110, false))
        	Gdx.net.openURI("https://soundcloud.com/warriyo");
        
        backBtnPressed = false;
        controlsPressed = false;
        daeronPressed = false;
        warriyoPressed = false;
        
        return false;
    }
  
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
          
        backBtnPressed = Util.isTouching(touchpointx, touchpointy, 80, Specular.camera.viewportHeight - 160, 435, 105, false); 
        controlsPressed = Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 1030, Specular.camera.viewportHeight - 155, 905, 110, false);
        
        daeronPressed = Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 650, Specular.camera.viewportHeight - 570, 500, 110, false);
        
        warriyoPressed = Util.isTouching(touchpointx, touchpointy, Specular.camera.viewportWidth - 650, Specular.camera.viewportHeight - 450, 500, 110, false);
        return false; 
    }
    
    public int getGraphicsSettings() {
    	return graphics;
    }
      
    public boolean musicMuted() { 
        return musicMuted; 
    }
      
    public boolean soundsMuted() { 
        return soundsMuted; 
    }
      
    public boolean backPressed() { 
        return backBtnPressed; 
    }
      
    public boolean controlsPressed() { 
        return controlsPressed; 
    }

	public boolean isDaeronPressed() {
		return daeronPressed;
	}

	public boolean isWarriyoPressed() {
		return warriyoPressed;
	}
}