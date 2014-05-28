package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.input.UpgradeInputProcessor;
import com.tint.specular.upgrades.BeamUpgrade;
import com.tint.specular.upgrades.BoardshockUpgrade;
import com.tint.specular.upgrades.BurstUpgrade;
import com.tint.specular.upgrades.FirerateUpgrade;
import com.tint.specular.upgrades.LifeUpgrade;
import com.tint.specular.upgrades.MultiplierUpgrade;
import com.tint.specular.upgrades.PDSUpgrade;
import com.tint.specular.upgrades.RepulsorUpgrade;
import com.tint.specular.upgrades.SlowdownUpgrade;
import com.tint.specular.upgrades.SwarmUpgrade;
import com.tint.specular.upgrades.Upgrade;

public class UpgradeState extends State {
	
	private UpgradeInputProcessor upgradeProcessor;
	private Upgrade[] upgrades = new Upgrade[10];
	private Texture progressBar, barPiece;
	private Preferences savedUpgrades;
	private int upgradePoints;
	
	public UpgradeState(Specular game) {
		super(game);
		progressBar = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Progress Bar.png"));
		barPiece = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Prrogress Piece"));
	}

	@Override
	public void render(float delta) {
		for(int i = 0; i < upgrades.length; i++) {
			// Icon
			game.batch.draw(upgrades[i].getTexture(), 0, 0);
			
			// Progress bar
			game.batch.draw(progressBar, 0, 0);
			
			for(int j = 0; j < upgrades[i].getGrade(); i++) {
				game.batch.draw(barPiece, 0, 0);
			}
			
			// Buttons
			upgradeProcessor.getConfirmButton().render();
			upgradeProcessor.getResetButton().render();
		}
	}
	
	@Override
	public void show() {
		super.show();
		savedUpgrades = Specular.prefs;
		resetUpgrades();
		
		upgradeProcessor = new UpgradeInputProcessor(game, this);
		Gdx.input.setInputProcessor(upgradeProcessor);
	}

	public void saveUpgrades() {
		Specular.prefs.putInteger("Life Upgrade Grade", upgrades[0].getGrade());
		Specular.prefs.putInteger("Life Upgrade Cost", upgrades[0].getCost());
		Specular.prefs.putInteger("Firerate Upgrade Grade", upgrades[1].getGrade());
		Specular.prefs.putInteger("Firerate Upgrade Cost", upgrades[1].getCost());
		Specular.prefs.putInteger("Burst Upgrade Grade", upgrades[2].getGrade());
		Specular.prefs.putInteger("Burst Upgrade Cost", upgrades[2].getCost());
		Specular.prefs.putInteger("Beam Upgrade Grade", upgrades[3].getGrade());
		Specular.prefs.putInteger("Beam Upgrade Cost", upgrades[3].getCost());
		Specular.prefs.putInteger("Multiplier Upgrade Grade", upgrades[4].getGrade());
		Specular.prefs.putInteger("Multiplier Upgrade Cost", upgrades[4].getCost());
		Specular.prefs.putInteger("PDS Upgrade Grade", upgrades[5].getGrade());
		Specular.prefs.putInteger("PDS Upgrade Cost", upgrades[5].getCost());
		Specular.prefs.putInteger("Swarm Upgrade Grade", upgrades[6].getGrade());
		Specular.prefs.putInteger("Swarm Upgrade Cost", upgrades[6].getCost());
		Specular.prefs.putInteger("Repulsor Upgrade Grade", upgrades[7].getGrade());
		Specular.prefs.putInteger("Repulsor Upgrade Cost", upgrades[7].getCost());
		Specular.prefs.putInteger("Slowdown Upgrade Grade", upgrades[8].getGrade());
		Specular.prefs.putInteger("Slowdown Upgrade Cost", upgrades[8].getCost());
		Specular.prefs.putInteger("Boardshock Upgrade Grade", upgrades[9].getGrade());
		Specular.prefs.putInteger("Boardshock Upgrade Cost", upgrades[9].getCost());
		
		Specular.prefs.flush();
		savedUpgrades = Specular.prefs;
	}
	
	public void resetUpgrades() {
		upgrades[0] = new LifeUpgrade(savedUpgrades.getInteger("Life Upgrade Grade"), savedUpgrades.getInteger("Life Upgrade Cost"));
		upgrades[1] = new FirerateUpgrade(savedUpgrades.getInteger("Firerate Upgrade Grade"), savedUpgrades.getInteger("Firerate Upgrade Cost"));
		upgrades[2] = new BurstUpgrade(savedUpgrades.getInteger("Burst Upgrade Grade"), savedUpgrades.getInteger("Burst Upgrade Cost"));
		upgrades[3] = new BeamUpgrade(savedUpgrades.getInteger("Beam Upgrade Grade"), savedUpgrades.getInteger("Beam Upgrade Cost"));
		upgrades[4] = new MultiplierUpgrade(savedUpgrades.getInteger("Multiplier Upgrade Grade"), savedUpgrades.getInteger("Multiplier Upgrade Cost"));
		upgrades[5] = new PDSUpgrade(savedUpgrades.getInteger("PDS Upgrade Grade"), savedUpgrades.getInteger("PDS Upgrade Cost"));
		upgrades[6] = new SwarmUpgrade(savedUpgrades.getInteger("Swarm Upgrade Grade"), savedUpgrades.getInteger("Swarm Upgrade Cost"));
		upgrades[7] = new RepulsorUpgrade(savedUpgrades.getInteger("Repulsor Upgrade Grade"), savedUpgrades.getInteger("Repulsor Upgrade Cost"));
		upgrades[8] = new SlowdownUpgrade(savedUpgrades.getInteger("Slowdown Upgrade Grade"), savedUpgrades.getInteger("Slowdown Upgrade Cost"));
		upgrades[9] = new BoardshockUpgrade(savedUpgrades.getInteger("Boardshock Upgrade Grade"), savedUpgrades.getInteger("Boardshock Upgrade Cost"));
	}
	
	public Upgrade[] getUpgrades() {
		return upgrades;
	}

	public int getUpgradePoints() {
		return upgradePoints;
	}
}
