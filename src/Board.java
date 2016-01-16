public class Board{
   
   private int[][] board;
   private int height, width;
   
   public Board(int w, int h){
      height = h;
      width = w;
      board = new int[w][h];
      for(int i=0; i<w; i++){
         for(int z=0; z<h; z++){
            board[i][z] = 1;
         }
      }      
   }   
   
   public int[][] getBoard(){
	   return board;
   }
   
   public int getWidth(){
      return width;
   }
   
   public int getHeight(){
      return height;
   }
   
   public int getSpotData(int x, int y){
      int s = board[x][y];
      return s;
   }
   
   public void setSpot(int x, int y, int s){
      board[x][y] = s;
   }   
   
   public String attack(int x, int y){
      String result = "";
      if(board[x][y] == 2){		//If ship in place
         result = "Hit!";
         board[x][y] = 3;		//Changes to Hit icon
      }
      else if(board[x][y] == 1){//If no ship in spot
         result = "Miss!";
         board[x][y] = 4;		//Changes to miss icon
      }
      return result;
   }       
   
   public String toString(){
      String printing = "";
      for(int i=0; i<width; i++){
         for(int z=0; z<height; z++){
            printing = (printing + board[i][z]+ " ");
         }
         printing += "\n";
      }
      return printing;
   }           
   
}   