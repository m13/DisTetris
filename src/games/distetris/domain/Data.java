package games.distetris.domain;

import java.io.Serializable;
import java.util.Random;

import android.graphics.Color;


/**
 * Content the basic data of a player (team, score and color)
 * @author Sergio
 */
public class Data implements Serializable  {
	private static final long serialVersionUID = 1346787542L;
	
	private Integer team;
	private Integer score;
	private Integer color;
	
	/**
	 * Full constructor
	 * @param team Integer value team
	 * @param score Integer value score
	 * @param color Integer value color
	 */
	public Data (Integer team, Integer score, Integer color) {
		this.team = team;
		this.score = score;
		this.color = color;
	}
	
	/**
	 * Fast constructor
	 * @param team Integer value team
	 */
	public Data (Integer team) {
		this.team = team;
		this.score = 0;
		Random color = new Random();
		this.color = Color.rgb(color.nextInt(256), color.nextInt(256), color.nextInt(256));
	}
	
	/**
	 * Increase the current value of the player score with the new value
	 * @param value New score value
	 */
	public void incrScore (Integer value) {
		this.score += value;
	}
	
	// getters
	public Integer getTeam() { return this.team; }
	public Integer getScore() { return this.score; }
	public Integer getColor() { return this.color; }
	
	// setters
	public void setTeam(Integer v) { this.team = v; }
	public void setScore(Integer v) { this.score = v; }
	public void setColor(Integer v) { this.color = v; }
}
