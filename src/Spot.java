public class Spot
{
	private int type;
   
	public Spot(){
	}
	public int getType(){  
		return type;
	}
	public void setType(int t){
		type = t;
	}
	public String toString(){
      return ("" + type);
	}
}