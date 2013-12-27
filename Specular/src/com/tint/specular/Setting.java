package com.tint.specular;

import com.badlogic.gdx.utils.Array;
import com.tint.specular.ui.Button;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Setting {
	
	//FIELDS
	private Array<Object> values = new Array<Object>();
	private Button next, previous;
	private int index = 0;
	
	public Setting() {
		next = new Button();
		previous = new Button();
	}
	
	public Setting(Object value) {
		values.add(value);
		next = new Button();
		previous = new Button();
	}
	
	public Setting(Object value1, Object value2) {
		values.add(value1);
		values.add(value2);
		next = new Button();
		previous = new Button();
	}
	
	public void addValue(Object value) {
		values.add(value);
	}
	
	public void removeValue(Object value) {
		values.removeValue(value, true);
	}
	
	public Object getValue(int index) {
		return values.get(index);
	}
	
	public Object getSelectedValue() {
		return values.get(index);
	}
	
	public Array<Object> getValues() {
		return values;
	}
	
	public void setIndex(int index) {
		this.index = index;
		if(this.index < 0) {
			this.index = values.size + index % values.size;
		} else if(this.index >= values.size) {
			this.index = index % values.size;
		}
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setButtonPositions(float x1, float y1, float x2, float y2) {
		previous.setPosition(x1, y1);
		next.setPosition(x2, y2);
	}
	
	public void setButtonDimensions(float width1, float height1, float width2, float height2) {
		previous.setSize(width1, height1);
		next.setSize(width2, height2);
	}
	
	public Button getNextButton() {
		return next;
	}
	
	public Button getPreviousButton() {
		return previous;
	}
}
