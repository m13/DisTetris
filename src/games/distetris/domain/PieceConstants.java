package games.distetris.domain;

public class PieceConstants {
	public static int FREEBLOCK = 0;
	public static int PIVOTBLOCK = 2;
	public static int PIECEBLOCK = 1;
	public static byte[/*Type*/][/*Rotation*/][/*Horitz Blocks*/][/*Vert Blocks*/] cPieces =
	{
	// Square
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}
	    }
	   },

	// I
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 1, 2, 1, 1},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0}, 
	    {0, 0, 2, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 1, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {1, 1, 2, 1, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 1, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    }
	   }
	  ,
	// L
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 0, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 1, 2, 1, 0},
	    {0, 1, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 1, 1, 0, 0},
	    {0, 0, 2, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 1, 0},
	    {0, 1, 2, 1, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    }
	   },
	// L mirrored
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 0, 0},
	    {0, 1, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 1, 0, 0, 0},
	    {0, 1, 2, 1, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 2, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 1, 2, 1, 0},
	    {0, 0, 0, 1, 0},
	    {0, 0, 0, 0, 0}
	    }
	   },
	// Z
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 1, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 1, 2, 0, 0},
	    {0, 0, 1, 1, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 1, 2, 0, 0},
	    {0, 1, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 1, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    }
	   },
	// Z mirrored
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 0, 1, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 1, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 1, 0, 0, 0},
	    {0, 1, 2, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 1, 0},
	    {0, 1, 2, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    }
	   },
	// T
	  {
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0},
	    {0, 1, 2, 1, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 1, 2, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 0, 0, 0, 0}
	    },
	   {
	    {0, 0, 0, 0, 0},
	    {0, 0, 1, 0, 0},
	    {0, 1, 2, 1, 0},
	    {0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0}
	    }
	   }
	};
}
