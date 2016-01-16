public class Ship{
  
   private int size, startX, startY;
   private String direction;  
    
   //accounting for orientation and takes starting point
	public Ship(String d,int x, int y, int s){
      startX = x;
      startY = y;    
      direction=d;
      size=s;     
	}
   //Sink Test
	public int getX(){
		return startX;
	}
	
	public int getY(){
		return startY;
	}
	
	public String getDirection(){
		return direction;
	}
	
	public int getSize(){
		return size;
	}
	
	public boolean checkSunk(Board b){
		int size2=size-1;
		int check=size;
		
		if(direction.equals("up")){
			
			for(int i=startX;i>=startX-size2;i--){
				
				if(b.getSpotData(i, startY)==3 && b.getSpotData(i, startY)!=5){
					check--;
				}
					
			}
		}else if(direction.equals("down")){
			for(int i=startX;i<=startX+size2;i++){
				if(b.getSpotData(i, startY)==3 && b.getSpotData(i, startY)!=5){
					check--;
				}
					
			}
		}else if(direction.equals("left")){
			for(int i=startY;i>=startY-size2;i--){
				if(b.getSpotData(startX, i)==3 && b.getSpotData(startX, i)!=5){
					check--;
				}
					
			}
		}else if(direction.equals("right")){
			for(int i=startY;i<=startY+size2;i++){
				if(b.getSpotData(startX, i)==3 && b.getSpotData(startX, i)!=5){
					check--;
				}
					
			}
		}
		if(check==0){
			if(direction.equals("up")){
				
				for(int i=startX;i>startX-size2;i--){
					b.setSpot(i, startY,5);	
				}
			}
         else if(direction.equals("down")){
				for(int i=startX;i<startX+size2;i++){
					b.setSpot(i, startY,5);
						
				}
			}
         else if(direction.equals("left")){
				for(int i=startY;i>startY-size2;i--){
					b.setSpot(startX, i,5);
						
				}
			}
         else if(direction.equals("right")){
				for(int i=startY;i<startY+size2;i++){
					b.setSpot(startX, i,5);	
				}
			}
			return true;
		}
		else
			return false;
	}	
}