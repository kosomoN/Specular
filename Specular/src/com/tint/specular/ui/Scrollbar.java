package com.tint.specular.ui;

import com.badlogic.gdx.graphics.Texture;

public class Scrollbar {
	private Texture texture;
	private float x, y;
	private float width, height;
	@SuppressWarnings("rawtypes")
	private Enum orientation;
	private Slider slider;
	
	public enum Orientation {
		HORIZONTAL, VERTICAL
	}
	
	public Scrollbar(Texture texture, float x, float y, float width, float height, Orientation orientation) {
		this.texture = texture;
		this.x =x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.orientation = orientation;
	}
	
	public void scroll(float newCoordinate) {
		if(orientation.equals(Orientation.HORIZONTAL)) {
			x = newCoordinate;
		} else {
			y = newCoordinate;
		}
	}

	public Slider getSlider() {
		return slider;
	}
	
	public void setSlider(Texture texture, float x, float y, float width, float height) {
		slider = new Slider(texture, x, y, width, height);
	}
	
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	protected class Slider {
		private Texture texture;
		private float x, y;
		private float width, height;
		
		public Slider(Texture texture, float x, float y, float width, float height) {
			this.texture = texture;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public Texture getTexture() {
			return texture;
		}

		public void setTexture(Texture texture) {
			this.texture = texture;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public float getWidth() {
			return width;
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public float getHeight() {
			return height;
		}

		public void setHeight(float height) {
			this.height = height;
		}
	}
}
