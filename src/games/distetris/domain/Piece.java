package games.distetris.domain;

import java.io.Serializable;

import android.graphics.Color;

public class Piece implements Serializable {
	private static final long serialVersionUID = 2L;
	/*Constants of pieces type and rotation*/
	
	
	public int x = 0; 			//Horitzontal pos measured in blocks
	public int y = 0; 			//Vertical pos measured in blocks
	private int type = 0; 		//Piece type, indexed in PieceConstants
	private int rotation = 0; 	//Piece Rotation, indexed in PieceConstants
	public int color = Color.parseColor("#ffff0000");		//Piece Color, Android Color
	
	
	
	public Piece(int type, int rotation) {
		super();
		this.type = type;
		this.rotation = rotation;
	}
	
	/**
	 * Returns the matrix representing the Piece
	 * @return
	 */
	public byte[][] getPieceMatrix(){
		return PieceConstants.cPieces[type][rotation];
	}
	
	/**
	 * Return the type of a specific block
	 *  0 - This is no Block
	 *  1 - Normal Block
	 *  2 - Pivot Block
	 * @param x
	 * @param y
	 * @return
	 */
	public byte getBlockType(int x, int y){
		return PieceConstants.cPieces[type][rotation][x][y];
	}
	
	//GETTERS & SETTERS
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRotation() {
		return rotation;
	}
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	

}
