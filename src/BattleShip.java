import javax.swing.*;
import java.applet.*;
import java.net.*;

//colors and junk
import java.awt.*;
//event listeners
import java.awt.event.*;

public class BattleShip extends JFrame implements ActionListener{
	//Variable declarations
   private JPanel Ocean, ShipAdd, Ocean2;
   private JButton start, finalImage;
   private JButton[][] grid;
   private JButton[][] grid2;
   private JLabel message, playerBoard, pcBoard;
   private JRadioButton down2up, up2down, left2right, right2left;
   private ButtonGroup OrientationButtons = new ButtonGroup();
   //Setting up the boards and arrays for the ships for the players
   private Board p1 = new Board(8,8);
   private Board p2 = new Board(8,8);   
   private Ship[] p1Ships = new Ship[3];
   private Ship[] p2Ships = new Ship[3];
   private int shipAmnt = 0, winCheckC=0, winCheckP=0, hitLastI=-1, hitLastJ=-1;
   private String direction="";
   private String[] directions={"up","down","left","right"};		   
   private boolean choosing=true;
   private AudioClip hit, miss;
  
   //Constructor that begins the game
   public BattleShip(){
	   	 //Set all GUI elements
    	 message = new JLabel("");
    	 message.setFont(new Font("Serif",Font.BOLD, 15));
    	 playerBoard = new JLabel("Your Board");
    	 playerBoard.setFont(new Font("Aharoni",Font.PLAIN, 25));
		 pcBoard = new JLabel("Computer Board");
		 pcBoard.setFont(new Font("Aharoni",Font.PLAIN, 25));
    	 Ocean = new JPanel(new FlowLayout());
    	 Ocean.setPreferredSize(new Dimension(450, 450));
    	 Ocean2 = new JPanel(new FlowLayout());
    	 Ocean2.setPreferredSize(new Dimension(450, 450));
    	 ShipAdd = new JPanel(new FlowLayout());
    	 ShipAdd.setPreferredSize(new Dimension(920, 70));
    	 grid = new JButton[8][8];
    	 grid2=new JButton[8][8];
    	 down2up = new JRadioButton("Bottom to Top");
		 OrientationButtons.add(down2up);
		 down2up.setSelected(true);
		 up2down = new JRadioButton("Top to Bottom");
		 OrientationButtons.add(up2down);
		 left2right = new JRadioButton("Left to Right");
		 OrientationButtons.add(left2right);
		 right2left = new JRadioButton("Right to Left");
		 OrientationButtons.add(right2left);
       //adding sound elements
       try{
         hit = Applet.newAudioClip(new URL("file:explode.wav")); 
         miss = Applet.newAudioClip(new URL("file:splash.wav"));
       }
       catch(MalformedURLException murle){
          System.out.println("done");
       }
       
		 //For loop to add the buttons as a grid for selection in the game
		 for(int i = 0; i<8; i++){
			 for(int j=0;j<8;j++){
	 		  	 grid[i][j]=new JButton(new ImageIcon("Water.png"));
	 		  	 grid[i][j].setPreferredSize(new Dimension(50, 50));
	 		  	 grid[i][j].addActionListener(this);
	 		  	 Ocean.add(grid[i][j]);    
			 }
 	  	 
		 }
		 Ocean.add(playerBoard);
		 for(int i = 0; i<8; i++){
			 //adding the other board
			 for(int j=0;j<8;j++){
	 		  	 grid2[i][j]=new JButton(new ImageIcon("Water.png"));
	 		  	 grid2[i][j].setPreferredSize(new Dimension(50, 50));
	 		  	 grid2[i][j].addActionListener(this);
	 		  	 Ocean2.add(grid2[i][j]);
	 	  	 }
			 
		 }
		 Ocean2.add(pcBoard);
		 //Disable buttons at beginning of game
		 disableButtons(true);
		 start=new JButton("Start Game");
		 start.addActionListener(this);
		 ShipAdd.add(start);
		 //Setting up the GUI
		 ShipAdd.add(down2up, BorderLayout.CENTER);
    	 ShipAdd.add(up2down, BorderLayout.CENTER);
    	 ShipAdd.add(left2right, BorderLayout.CENTER);
    	 ShipAdd.add(right2left, BorderLayout.CENTER);
    	 down2up.setVisible(false);
    	 up2down.setVisible(false);
    	 left2right.setVisible(false);
    	 right2left.setVisible(false);
    	 
		 add(Ocean, BorderLayout.WEST);
		 add(Ocean2, BorderLayout.EAST);
		 add(ShipAdd, BorderLayout.SOUTH);

		 //Set size and title
		 setTitle("BattleShip");
		 setSize(905,575);
		 setLocationRelativeTo(null);
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setVisible(true);
		 setResizable(false);
		 
	 }   
      	
   	
    //Action Listener
   	public void actionPerformed(ActionEvent e){
   		try{
   			//On start button click
   			if(e.getSource()==start){
   				//Hide the start button and begin ship choosing process
   				start.setVisible(false);
   				choosing=true;
   				ShipAdd.add(message);//Add message to GUI
   				//Using HTML in message to get it to look the way we want
   				message.setText("<html><body>Select the orientation of your Patrol Boat: Length 2. <br>Then choose grid location to place it in</body></html>");
   				down2up.setVisible(true);
			   	up2down.setVisible(true);
			   	left2right.setVisible(true);
			   	right2left.setVisible(true);
			   	disableButtons(false);
			   	//Disable buttons and show radioButtons
   			}

	        for(int i=0;i<8;i++){
	        	 for(int j=0;j<8;j++){
	        		if(e.getSource()==grid[i][j]){
	        			//Update direction so the current direction is always updated when placing a boat
	        			updateD();
	        			//PLACING INITIAL BOATS
	        			if(shipAmnt<3 && choosing==true){ //If user hasn't placed all ships and it is their turn to choose
	        				if(shipAmnt==0){ //On first boat, display first boat message
	        					if(!checkBorder(i,j,2,direction) || !checkBoat(i,j,direction,2,p1)){//If not out of bounds or hitting another ship
	        						JOptionPane.showMessageDialog(null, "Please place ship in a spot it will fit!");
	        					}else{
	        						//Player 1's first ship=new ship with clicked coordinates and selected direction and size of 2 for first ship
	        						p1Ships[0]=new Ship(direction,i,j,2);
	        						PlacePlayerShip(i,j,direction,2); //Adds ship to GUI
	        						//Show next message
	        						message.setText("<html><body>Select the orientation of your Submarine: Length 3. <br>Then choose grid location to place it in</body></html>"); 
	        						JOptionPane.showMessageDialog(null, "Patrol Boat is placed!");//Notify user
	        						shipAmnt++;
	        					}
	        						
	        				}else if(shipAmnt==1){//Same as above with new size and message
	        					if(!checkBorder(i,j,3,direction) || !checkBoat(i,j,direction,3,p1)){
	        						JOptionPane.showMessageDialog(null, "Please place ship in a spot it will fit!");
	        					}else{
	        						p1Ships[1]=new Ship(direction,i,j,3);
	        						PlacePlayerShip(i,j,direction,3);
	        						message.setText("<html><body>Select the orientation of your Battleship: Length 4. <br>Then choose grid location to place it in</body></html>");
	        						JOptionPane.showMessageDialog(null, "Submarine is placed!");
	        						shipAmnt++;
	        					}
	        				}else if(shipAmnt==2){
	        					if(!checkBorder(i,j,4,direction) || !checkBoat(i,j,direction,4,p1)){
	        						JOptionPane.showMessageDialog(null, "Please place ship in a spot it will fit!");
	        					}else{
	        						p1Ships[2]=new Ship(direction,i,j,4);
	        						PlacePlayerShip(i,j,direction,4);
	        						JOptionPane.showMessageDialog(null, "BattleShip is placed!");
	        						shipAmnt++;
	        						choosing=false;
	        						//Have computer choose spots for its ships
	        						computerChoose();
	        						//Hide radioButtons and display new message
	        						message.setText("You get to go first. Pick a spot to attack");
	        						down2up.setVisible(false);
	        		   			   	up2down.setVisible(false);
	        		   			   	left2right.setVisible(false);
	        		   			   	right2left.setVisible(false);
	        		   
	        					}
	        				}
	        			}
	        		 } 
	        	 }
	        }
	        
	        for(int i=0;i<8;i++){
	        	for(int j=0;j<8;j++){
	        		if(e.getSource()==grid2[i][j]){//When clicking second grid...
	        			if(!choosing){//If user is not picking their boats
		        			message.setText("Attacking...");
	        				if(p2.getSpotData(i,j)!=1 && p2.getSpotData(i, j)!=2){//If spot is not a water space or boat space (hit, miss, sunk space)
	        					JOptionPane.showMessageDialog(null, "Plase choose a blank spot to attack!");
	        				}else{

	        					if(p2.getSpotData(i, j)==2){//If user has clicked a boat spot
                           hit.play(); //plays a sound if they hit
	        						grid2[i][j].setIcon(new ImageIcon("HitB.png"));//Set new icon
	        						p2.setSpot(i,j, 3);//Set that spot as a Hit on the board
	        						checkSunk(p2,p2Ships,false);//Check if this users ship has sunk
	        					}else{
                           miss.play(); //plays the appropriate sound
	        						//Otherwise, user has missed
	        						grid2[i][j].setIcon(new ImageIcon("Miss.png"));//Change icon for that spot
	        						p2.setSpot(i, j, 4);//Set that spot as a Miss on the board
	        					}
	        					//Make the computer choose
                        if(winCheckP!=3){
   	        					computerPlay();
                        }
	        				}
	        			}
	        		}
	        	}
	        }
	        
	        //This is the win/lose screen that resets all variables and restarts the game 
	        if(e.getSource()==finalImage){
	        	for(int i=0;i<8;i++){
	        		for(int j=0;j<8;j++){
	        			p1.setSpot(i,j,1);
	        			p2.setSpot(i, j, 1);
	        			grid[i][j].setIcon(new ImageIcon("Water.png"));
	        			grid2[i][j].setIcon(new ImageIcon("Water.png"));
	        			//Reset icons/spots on boards
	        		}
	        	}
	        	choosing=true;
	        	shipAmnt=0;
	        	Ocean.setVisible(true);
				Ocean2.setVisible(true);
				finalImage.setVisible(false);
				ShipAdd.setPreferredSize(new Dimension(920, 70));
				disableButtons(true);
    			start.setVisible(true);
    			winCheckC=0;
    			winCheckP=0;
    			hitLastI=-1;
    			hitLastJ=-1;
    			message.setText("");
	        }
      
   		}
      
	      catch(Exception x) {
	         x.printStackTrace();
	      }
	}  

	public void computerPlay() {
		
		message.setText("Opponent is attacking you...");

			//If the last attack from the computer was a miss
			if(hitLastI==-1 || hitLastJ==-1){
				int i=(int) (Math.random()*8);
				int j=(int) (Math.random()*8);
				//Choose random numbers until the spot chosen is a water spot or boat spot
				while(p1.getSpotData(i,j)!=1 && p1.getSpotData(i, j)!=2){
					i=(int) (Math.random()*8);
					j=(int) (Math.random()*8);
				}
				
				//If miss
				if(p1.getSpotData(i,j)==1){
					message.setText("OPPONENT MISSED!");
					grid[i][j].setIcon(new ImageIcon("Miss.png"));
					p1.setSpot(i, j, 4);//Set spot to Miss
					hitLastI=-1;
					hitLastJ=-1;
				}else{
					//If hit
					message.setText("OPPONENT HIT YOUR SHIP");
					grid[i][j].setIcon(new ImageIcon("HitB.png"));
					p1.setSpot(i, j, 3);//Set spot to Hit
					//Set hit variables to the current spot where computer just hit a boat
					hitLastI=i;
					hitLastJ=j;
					checkSunk(p1,p1Ships, true); //Check if that ship has sunk
				}
			}else{
				//if last attack was a hit, next attack will be on a spot directly next to the hit
				int dir=(int) (Math.random()*4);//Choose random direction
				String d="";
				d=directions[dir];
				//Adjust coordinates appropriately based on what direction the computer will attack next
				if(d.equals("up"))
					hitLastI--;
				else if(d.equals("down"))
					hitLastI++;
				else if(d.equals("left"))
					hitLastJ--;
				else hitLastJ++;
				
				//If the spot chosen is not suitable, choose a random spot on the board
				if(hitLastI>7 || hitLastI<0 || hitLastJ>7 || hitLastJ<0 || p1.getSpotData(hitLastI,hitLastJ)==3 || p1.getSpotData(hitLastI,hitLastJ)==4 || p1.getSpotData(hitLastI,  hitLastJ)==5){
					int i=(int) (Math.random()*8);
					int j=(int) (Math.random()*8);
					while(p1.getSpotData(i,j)!=1 && p1.getSpotData(i, j)!=2){
						i=(int) (Math.random()*8);
						j=(int) (Math.random()*8);
					}
					
					if(p1.getSpotData(i,j)==1){//If water
						message.setText("OPPONENT MISSED!");
						grid[i][j].setIcon(new ImageIcon("Miss.png"));
						p1.setSpot(i, j, 4);//Change to miss
						hitLastI=-1;
						hitLastJ=-1;
					}else{//if ship
						message.setText("OPPONENT HIT YOUR SHIP");
						grid[i][j].setIcon(new ImageIcon("HitB.png"));
						p1.setSpot(i, j, 3);//Change to hit
						hitLastI=i;
						hitLastJ=j;
						checkSunk(p1,p1Ships, true);//Check if ship has sunk
					}
				}else{
					if(p1.getSpotData(hitLastI,hitLastJ)==1){
						message.setText("OPPONENT MISSED!");
						grid[hitLastI][hitLastJ].setIcon(new ImageIcon("Miss.png"));
						p1.setSpot(hitLastI, hitLastJ, 4);
						hitLastI=-1;
						hitLastJ=-1;
					}else{
						message.setText("OPPONENT HIT YOUR SHIP");
						grid[hitLastI][hitLastJ].setIcon(new ImageIcon("HitB.png"));
						p1.setSpot(hitLastI, hitLastJ, 3);
						checkSunk(p1,p1Ships, true);
					}
				}
				
				
			}
			
			
		
	}

	public void PlacePlayerShip(int i, int j, String d, int s) {
		String filename="";
		//s-=1;
		if(d.equals("up")){//find the front, middle and back of boat and change icons appropriately when placing player ship in grid
			for(int k=i;k>(i-s);k--){//The algorithm we worked out to do this task
				p1.setSpot(k, j, 2);
				if(k==i){
		           	filename = "BoatBack0.png";
		        	}  
		        else if(k==(i-s+1)){
		           	filename = "BoatFront0.png";
		        	}
		        else{
		           	filename = "MiddleV.png";
		        }  
				grid[k][j].setIcon(new ImageIcon(filename));
			}
		}else if(d.equals("down")){
			for(int k=i;k<(i+s);k++){
				p1.setSpot(k, j, 2);
				if(k==i){
		           	filename = "BoatBack180.png";
		        	}  
		        else if(k==(i+s-1)){
		           	filename = "BoatFront180.png";
		        	}
		        else{
		           	filename = "MiddleV.png";
		        	}   
		   		grid[k][j].setIcon(new ImageIcon(filename));

			}
		}else if(d.equals("right")){
			for(int k=j;k<(j+s);k++){
				p1.setSpot(i, k, 2);
				if(k==j){
		           	filename = "BoatBack90.png";
		        	}  
		        else if(k==(j+s-1)){
		           	filename = "BoatFront90.png";
		        	}
		        else{
		           	filename = "MiddleH.png";
		        	}
				grid[i][k].setIcon(new ImageIcon(filename));

			}
		}else{
			for(int k=j;k>(j-s);k--){
				p1.setSpot(i, k, 2);
				if(k==j){
		           	filename = "BoatBack270.png";
		        	}  
		        else if(k==(j-s+1)){
		           	filename = "BoatFront270.png";
		        	}
		        else{
		           	filename = "MiddleH.png";
		        	}
		   		grid[i][k].setIcon(new ImageIcon(filename));

			}
		}
	}
	
	public void PlaceCompShip(int i, int j, String d, int s){
		if(d.equals("up")){//Same as method above except it does not change the icon to a boat
			for(int k=i;k>(i-s);k--){
				p2.setSpot(k, j, 2);
			}
		}else if(d.equals("down")){
			for(int k=i;k<(i+s);k++){
				p2.setSpot(k, j, 2);
			}
		}else if(d.equals("right")){
			for(int k=j;k<(j+s);k++){
				p2.setSpot(i, k, 2);
			}
		}else{
			for(int k=j;k>(j-s);k--){
				p2.setSpot(i, k, 2);
			}
		}
	}

	public void computerChoose() {
		for(int k=0;k<3;k++){
			//Choose three boats by randomly picking coordinates and a direction
			int i=(int) (Math.random()*8);
			int j=(int) (Math.random()*8);
			int d=(int) (Math.random()*4);
			if(d==0)
				direction="up";
			else if(d==1)
				direction="down";
			else if(d==2)
				direction="left";
			else direction="right";
			
			if(k==0){
				//On first boat, check that it is not out of bounds or on another boat, place it appropriately, make Ship object. Repeat for other ships
				if(!checkBorder(i,j,2,direction) || !checkBoat(i,j,direction,2,p2)){
					k--;
				}else{
					p2Ships[0]=new Ship(direction,i,j,2);
					PlaceCompShip(i,j,direction,2);
				}
					
			}else if(k==1){
				if(!checkBorder(i,j,3,direction) || !checkBoat(i,j,direction,3,p2)){
					k--;
				}else{
					p2Ships[1]=new Ship(direction,i,j,3);
					PlaceCompShip(i,j,direction,3);
				}
			}else if(k==2){
				
				if(!checkBorder(i,j,4,direction) || !checkBoat(i,j,direction,4,p2)){
					k--;
				}else{
					p2Ships[2]=new Ship(direction,i,j,4);
					PlaceCompShip(i,j,direction,4);
				}
			}
		}
		
	}

	public boolean checkBorder(int i, int j, int size, String d) {
		size-=1;
		//Checks if ship is out of bounds using simple adding of coordinates and sizes based on direction
		if(d.equals("up")){
			if(i-size<0){
				return false;
			}else
				return true;
		}else if(d.equals("down")){
			if(i+size>7){
				return false;
			}else
				return true;
		}else if(d.equals("right")){
			if(j+size>7){
				return false;
			}else
				return true;
		}else{
			if(j-size<0){
				return false;
			}else
				return true;
		}
		
	}
	
	public boolean checkBoat(int i, int j, String d, int s, Board b){
		//Checks if ship is hitting another ship, returns false if yes.
		if(d.equals("up")){
			for(int k=i;k>(i-s);k--){//Go through each piece of the boat based on size and location
				if(b.getSpotData(k, j)!=1)//If one of the spots isn't water, return false. Repeat for other directions
					return false;
			}
		}else if(d.equals("down")){
			for(int k=i;k<(i+s);k++){
				if(b.getSpotData(k, j)!=1)
					return false;
			}
		}else if(d.equals("right")){
			for(int k=j;k<(j+s);k++){
				if(b.getSpotData(i, k)!=1)
					return false;
			}
		}else{
			for(int k=j;k>(j-s);k--){
				if(b.getSpotData(i, k)!=1)
					return false;
			}
		}
		return true;
	}

	public static void main(String[] args){
		//Create the window
      BattleShip myWindow = new BattleShip();
	}
	
	public void updateD(){
		//Called before each player ship placement selection to update the direction that is selected in the radiobuttons
		if(down2up.isSelected()){
	   		direction="up";                 
	   	} 
	   	else if(up2down.isSelected()){
	   		direction="down";                
	   	}
	   	else if(left2right.isSelected()){
	   		direction="right";                  
	   	}
	   	else if(right2left.isSelected()){
	   		direction="left";                
	   	}
	}
	
	public void checkSunk(Board b, Ship[] ships, boolean comp){
		
		for(int i=0;i<ships.length;i++){
			//If the ship in question has sunk (method in the Ship class)
			if(ships[i].checkSunk(b)){
				if(comp){//If it is the computer that sunk it, show appropriate message and change the boats icons and spot integers to "sunk"
					JOptionPane.showMessageDialog(null,"OPPONENT SUNK YOUR SHIP!!");
					changeSunk(ships[i].getX(),ships[i].getY(),ships[i].getSize(),ships[i].getDirection(),grid);
					winCheckC++;
					//If all three ships have sunk, call the win method, false inputted here to indicate that the player did not win
					if(winCheckC==3)
						win(false);
				}
            else{
					JOptionPane.showMessageDialog(null,"SHIP SUNK!!");
					changeSunk(ships[i].getX(),ships[i].getY(),ships[i].getSize(),ships[i].getDirection(),grid2);
					winCheckP++;
					if(winCheckP==3)
						win(true);
				}
 			}
		}
	}

	public void changeSunk(int i, int j, int s, String d, JButton[][] g){
		String filename="HitB.png";
		//s-=1;
		//very similar to the PlacePlayerSHip method except it is done with our "sunken" icons
		if(d.equals("up")){
			for(int k=i;k>(i-s);k--){ 
				if(k==i){
		           	filename = "BackSunk.png";
		        	}  
		        else if(k==(i-s+1)){
		           	filename = "FrontSunk.png";
		        	}
		        else{
		           	filename = "MiddleSunk.png";
		        }  
				g[k][j].setIcon(new ImageIcon(filename));
			}
		}else if(d.equals("down")){
			for(int k=i;k<(i+s);k++){
				if(k==i){
		           	filename = "BackSunk.png";
		        	}  
		        else if(k==(i+s-1)){
		           	filename = "FrontSunk.png";
		        	}
		        else{
		           	filename = "MiddleSunk.png";
		        } 
		   		g[k][j].setIcon(new ImageIcon(filename));

			}
		}else if(d.equals("right")){
			for(int k=j;k<(j+s);k++){
				if(k==j){
		           	filename = "BackSunk.png";
		        	}  
		        else if(k==(j+s-1)){
		           	filename = "FrontSunk.png";
		        	}
		        else{
		           	filename = "MiddleSunk.png";
		        	}
				g[i][k].setIcon(new ImageIcon(filename));

			}
		}else{
			for(int k=j;k>(j-s);k--){
				if(k==j){
		           	filename = "BackSunk.png";
		        	}  
		        else if(k==(j-s+1)){
		           	filename = "FrontSunk.png";
		        	}
		        else{
		           	filename = "MiddleSunk.png";
		        	}
		   		g[i][k].setIcon(new ImageIcon(filename));

			}
		}
	}
	
	public void win(boolean player){
		//If player won, show congrats picture, hide everything else
		if(player){
			Ocean.setVisible(false);
			Ocean2.setVisible(false);
			finalImage=new JButton(new ImageIcon("CongratsImproved.png"));
			finalImage.addActionListener(this);
			ShipAdd.setPreferredSize(new Dimension(905, 575));
			ShipAdd.add(finalImage);
			finalImage.setVisible(true);
		}else{	
			//Otherwise, show "Sorry" picture, hide everything else
			Ocean.setVisible(false);
			Ocean2.setVisible(false);
			finalImage=new JButton(new ImageIcon("SorryImproved.png"));
			finalImage.addActionListener(this);
			ShipAdd.setPreferredSize(new Dimension(905, 575));
			ShipAdd.add(finalImage);
			finalImage.setVisible(true);
		}
		
	}
	
	public void disableButtons(boolean d){
		//If d is true, hide all buttons. Else, show all buttons on the grids.
		if(d){
			for(int i = 0; i<8; i++){
	    	  	for(int j=0;j<8;j++){
	    		  	grid[i][j].setEnabled(false);
	    	  	}
	      	}
	      	for(int i = 0; i<8; i++){
	    	  	for(int j=0;j<8;j++){
	    		  	grid2[i][j].setEnabled(false);
	    		  	
	    	  	}
	      	}
		}else{
			for(int i = 0; i<8; i++){
	    	  	for(int j=0;j<8;j++){
	    		  	grid[i][j].setEnabled(true);
	    	  	}
	      	}
	      	for(int i = 0; i<8; i++){
	    	  	for(int j=0;j<8;j++){
	    		  	grid2[i][j].setEnabled(true);
	    		  	
	    	  	}
	      	}
		}
	}
	
	
}  