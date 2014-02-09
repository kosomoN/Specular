package com.tint.specular.input; 
  
import com.badlogic.gdx.Gdx; 
import com.badlogic.gdx.InputAdapter; 
import com.tint.specular.Specular; 
import com.tint.specular.Specular.States; 
import com.tint.specular.utils.Util; 
  
public class SettingsInputProcessor extends InputAdapter { 
  
    private Specular game; 
    private boolean soundsMuted = true, musicMuted = true, particlesEnabled = true, backPressed, controlsPressed; 
      
    public SettingsInputProcessor(Specular game) { 
        this.game = game; 
        soundsMuted = Specular.prefs.getBoolean("SoundsMuted"); 
        musicMuted = Specular.prefs.getBoolean("MusicMuted"); 
        particlesEnabled = Specular.prefs.getBoolean("Particles"); 
    } 
      
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
        backPressed = Util.isTouching(touchpointx, touchpointy, 47, 888, 559, 192, false); 
        controlsPressed = Util.isTouching(touchpointx, touchpointy, 1160, 910, 655, 90, false);
          
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
        } 
        // Sound 
        else if(Util.isTouching(touchpointx, touchpointy, 190, 685, 410, 128, false)) { 
            soundsMuted = !soundsMuted; 
        }
        // Controls
        else if(Util.isTouching(touchpointx, touchpointy, 1160, 910, 655, 90, false)) {
        	Specular.prefs.putBoolean("SoundsMuted", soundsMuted); 
            Specular.prefs.putBoolean("MusicMuted", musicMuted); 
            Specular.prefs.putBoolean("Particles", particlesEnabled); 
            Specular.prefs.flush(); 
        	game.enterState(States.CONTROLSETUPSTATE);
        }
        // Back 
        else if(Util.isTouching(touchpointx, touchpointy, 47, 888, 559, 192, false)) { 
            Specular.prefs.putBoolean("SoundsMuted", soundsMuted); 
            Specular.prefs.putBoolean("MusicMuted", musicMuted); 
            Specular.prefs.putBoolean("Particles", particlesEnabled); 
            Specular.prefs.flush(); 
            
            
            game.enterState(States.MAINMENUSTATE); 
        }
        
        backPressed = false;
        controlsPressed = false;
        
        return false;
    }
  
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) { 
        // Translate screen coordinates to viewport coordinates 
        float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth; 
        float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight; 
          
        backPressed = Util.isTouching(touchpointx, touchpointy, 47, 888, 559, 192, false); 
        controlsPressed = Util.isTouching(touchpointx, touchpointy, 1160, 910, 655, 90, false);
        
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
        return backPressed; 
    } 
      
    public boolean controlsPressed() { 
        return controlsPressed; 
    } 
}