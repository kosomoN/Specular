package com.tint.specular.input; 
  
import com.badlogic.gdx.Gdx; 
import com.badlogic.gdx.InputAdapter; 
import com.badlogic.gdx.Input.Keys;
import com.tint.specular.Specular; 
import com.tint.specular.Specular.States; 
import com.tint.specular.states.MainmenuState;
import com.tint.specular.utils.Util; 
  
public class SettingsInputProcessor extends InputAdapter { 
  
    private Specular game; 
    private boolean soundsMuted = true, musicMuted = true, particlesEnabled = true, backBtnPressed, controlsPressed;
      
    public SettingsInputProcessor(Specular game) {
        this.game = game; 
        soundsMuted = Specular.prefs.getBoolean("SoundsMuted");
        musicMuted = Specular.prefs.getBoolean("MusicMuted");
        particlesEnabled = Specular.prefs.getBoolean("Particles");
    }
    
    @Override
	public boolean keyUp(int keycode) {
    	if(keycode == Keys.BACK)
			game.enterState(States.MAINMENUSTATE);
    	
    	return false;
	}

	@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
        backBtnPressed = Util.isTouching(touchpointx, touchpointy, 80, 920, 435, 105, false); 
        controlsPressed = Util.isTouching(touchpointx, touchpointy, 890, 925, 905, 110, false);
          
        return false; 
    } 
  
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
          
        // Particles 
        if(Util.isTouching(touchpointx, touchpointy, 190, 365, 880, 128, false)) { 
            particlesEnabled = !particlesEnabled; 
        } 
        // Music 
        else if(Util.isTouching(touchpointx, touchpointy, 190, 525, 550, 128, false)) { 
            musicMuted = !musicMuted;
            if(musicMuted)
            	((MainmenuState) game.getState(States.MAINMENUSTATE)).stopMusic();
            else
            	((MainmenuState) game.getState(States.MAINMENUSTATE)).startMusic();
        } 
        // Sound 
        else if(Util.isTouching(touchpointx, touchpointy, 190, 685, 410, 128, false))
            soundsMuted = !soundsMuted; 

        // Controls
        else if(Util.isTouching(touchpointx, touchpointy, 890, 925, 905, 110, false))
        	game.enterState(States.CONTROLSETUPSTATE);

        // Back 
        else if(Util.isTouching(touchpointx, touchpointy, 80, 920, 435, 105, false))
            game.enterState(States.MAINMENUSTATE); 
        
        backBtnPressed = false;
        controlsPressed = false;
        
        return false;
    }
  
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
          
        backBtnPressed = Util.isTouching(touchpointx, touchpointy, 80, 920, 435, 105, false); 
        controlsPressed = Util.isTouching(touchpointx, touchpointy, 890, 925, 905, 110, false);
        
        return false; 
    } 
      
    public boolean musicMuted() { 
        return musicMuted; 
    } 
      
    public boolean soundsMuted() { 
        return soundsMuted; 
    } 
      
    public boolean particlesEnabled() { 
        return particlesEnabled; 
    } 
      
    public boolean backPressed() { 
        return backBtnPressed; 
    } 
      
    public boolean controlsPressed() { 
        return controlsPressed; 
    } 
}