import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI
for the Big Two card game and handle all user actions. Below is a detailed description for the
BigTwoTable class.
 * @author Marco Brian
 *
 */
public class BigTwoTable implements CardGameTable {
	private BigTwoClient game;
	private int activePlayer;
	private int lastPlayerHandOnTable;
	private JMenuItem ConnectGame; //replaces the restart menu item from previous assignment
	private JPanel bigTwoPanel ;
	private Image[][] cardImages ;
	private Image[] avatars; 
	private Image cardBackImage;
	private JFrame frame;
	private JTextArea msgArea; 
	private JButton playButton;
	private JButton passButton;
	//Chat message section
	private JTextArea chatArea;
	private JTextField inputChatField;
	private JPanel textPanel; 
	
	
	/**
	 * This is used for disabling and enabling the bigTwoPanel
	 */
	
	private boolean selected[];
	
	/**
	 * records whether it is the first round
	 */
	boolean first_round = true;
	
	/**
	 * a method for resetting the list of selected cards.
	 */
	public void resetSelected() {
		for(int i = 0 ; i < selected.length; i++) {
			selected[i] = false;
		}
	}
	
	/**
	 * a method for getting an array of indices of the cards selected.
	 */
	public int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		if(count==0) {

			cardIdx = new int[1];
			cardIdx[0] = -1;
		}
		return cardIdx;
	}
	
	/**
	 * The constructor for BigTwoTable that takes a class that implements the CardGame interface as an argument.
	 * In the constructor we instantiate most of the instance variables and giving it values
	 * @param game BigTwo class
	 */
	public BigTwoTable(BigTwoClient game) {
		this.game = game;
		activePlayer = game.getCurrentIdx();
		frame = new JFrame();
		
		selected = new boolean[13];
		
		//setting values of selected to false
		resetSelected();
		
		//create a new 2D array 
		cardImages = new Image[4][13];
		
		//load card back image
		cardBackImage = new ImageIcon("res/"+"back.gif").getImage();
		
		//load card images 
		for(int j =0 ; j < 13 ; j++) {
			cardImages[0][j] = new ImageIcon("res/"+String.valueOf(j) + "d.gif").getImage();
			cardImages[1][j] = new ImageIcon("res/"+String.valueOf(j) + "c.gif").getImage();
			cardImages[2][j] = new ImageIcon("res/"+String.valueOf(j) + "h.gif").getImage();
			cardImages[3][j] = new ImageIcon("res/"+String.valueOf(j) + "s.gif").getImage();
		}
		//load avatar images
		avatars = new Image[4];
		avatars[0] = new ImageIcon("res/"+"JS.png").getImage();
		avatars[1] = new ImageIcon("res/"+"DT.png").getImage();
		avatars[2] = new ImageIcon("res/"+"JB.png").getImage();
		avatars[3] = new ImageIcon("res/"+"TL.png").getImage();
		
		
		//creating the bigTwoPanel
		bigTwoPanel = new BigTwoPanel();
		//Using BorderLayout for the panel
		bigTwoPanel.setLayout(new BorderLayout());
		
		
		playButton = new JButton("Play"); 
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener()); // add an action listener to play button
		passButton.addActionListener(new PassButtonListener()); // add an action listener to pass button
		
		
		JPanel buttonPanel = new JPanel(); // make a buttonPanel to store play button and pass button
		buttonPanel.add(playButton); // add play button to the buttonPanel
		buttonPanel.add(passButton); // add pass button to the buttonPanel
		bigTwoPanel.add( buttonPanel,BorderLayout.SOUTH);
		
		//Makes the message area 
		msgArea = new JTextArea(0,30);
		msgArea.setLineWrap(true); //Allows characters to wrap around
		msgArea.setWrapStyleWord(true); //allows words to wrap around the text area like a word document
		msgArea.setEditable(false); //Make the text area not editable
		msgArea.setFont(new Font("Courier New",1,20)); //Setting up the font 
		msgArea.setVisible(true); //Make the text area visible

		//Makes the message area into a scrollable pane 
		JScrollPane scrollPane = new JScrollPane(msgArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(30,500));
		
		
		//Chat message components		
		
		//Creates the Chat Area
		chatArea = new JTextArea(0,30);
		chatArea.setText("Chatroom: \n");
		chatArea.setLineWrap(true); //Allows characters to wrap around
		chatArea.setWrapStyleWord(true); //allows words to wrap around the text area like a word document
		chatArea.setEditable(false); //Make the text area not editable
		chatArea.setFont(new Font("Courier New",1,20)); //Setting up the font 
		chatArea.setVisible(true);
		//Make the chat area into a scrollable pane
		JScrollPane chatscrollPane = new JScrollPane(chatArea);
		chatscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatscrollPane.setPreferredSize(new Dimension(30,400));
		
		//Creates an input text field for entering and submitting messages
		inputChatField = new HintTextField(30,"Enter message:");
		inputChatField.setEditable(true);
		inputChatField.setVisible(true);
		inputChatField.setPreferredSize(new Dimension(30,5));
		//Adds a listener so that when "enter" button is pressed, the message will be sent
		inputChatField.addKeyListener(new inputTextAreaListener());
		
		
		//creates a panel that holds the game message area + chat area + input text field
		textPanel = new JPanel();
		//using a BoxLayout that spans vertically
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.add(scrollPane);
		textPanel.add(chatscrollPane);
		textPanel.add(inputChatField);
	
		//Creates menu bar and adding Restart and Quit menu items adding along its Listeners
		JMenuBar mb=new JMenuBar();    
		JMenu GameMenu = new JMenu("Game");    
		ConnectGame =new JMenuItem("Connect");    
		JMenuItem QuitGame =new JMenuItem("Quit");   
		QuitGame.addActionListener(new QuitMenuItemListener());
		ConnectGame.addActionListener(new ConnectMenuItemListener());
		GameMenu.add(ConnectGame);
		GameMenu.add(QuitGame);
		mb.add(GameMenu);
		frame.add(mb);
		frame.setJMenuBar(mb);  

		//Setting the orientation of components in the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(bigTwoPanel,BorderLayout.CENTER);
		frame.add(textPanel,BorderLayout.EAST);
		frame.setSize(1050,1000);
		frame.setVisible(true);
		frame.setResizable(false);
		
		
	}
	
/**
 * an inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle button-click events for the “Play” button. When the “Play” button is clicked,
you should call the makeMove() method of your CardGame object to make a move
 * @author Marco Brian
 *
 */
	
	public class PlayButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e){
			if(game.getPlayerID()== activePlayer) {
			game.makeMove(activePlayer, getSelected());
			resetSelected();
			}
		}
		
		
	}
	
/**
 * an inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle button-click events for the “Pass” button. When the “Pass” button is clicked,
you should call the makeMove() method of your CardGame object to make a move.
 * @author Marco Brian
 *
 */
	public class PassButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e){
			if(game.getPlayerID()== activePlayer) {
				if(activePlayer!=lastPlayerHandOnTable && first_round!=true) {
					game.makeMove(activePlayer, null);
					resetSelected();		
				}
				else {
					printMsg(game.getPlayerName()+" must play a Hand");
				}
			}
		}
		
	}
	/**
	 * 
	 * an inner class that extends the JPanel class and implements the
MouseListener interface. Overrides the paintComponent() method inherited from the
JPanel class to draw the card game table. Implements the mouseClicked() method from
the MouseListener interface to handle mouse click events.
	 * 
	 * @author Marco Brian
	 *
	 */
	public class BigTwoPanel extends JPanel implements MouseListener {
		
		/**
		 * Defines the gap between avatars in pixels
		 */
		int gap = 50;
		
		/**
		 * Defines the gap between cards in pixels
		 *
		 */
		
		int between_cards = 15;
		/**
		 * The x-coordinate offset from (0,0)
		 */
		
		int x_offset = 50 ;
		/**
		 * The x-coordinate offset from (0,0)
		 */
		int y_offset = 100 ;
		
		/**
		 * height of avatar in pixels
		 */
		int avatar_height = 100;
		/**
		 * width of avatar in pixels
		 */
		int avatar_width = 129;
		/**
		 *  the amount of pixels for raising a card
		 */
		int raised = 10; 
		/**
		 * width of card in pixels
		 */
		int card_width = 73;
		/**
		 * height of card in pixels
		 */
		int card_height = 97;
		
		/**
		 * Constructor used to add a mouselistener and setting the size of the BigTwoPanel
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
			this.setSize(700,700);
		}
		
		/**
		 * This is the paintComponent method of the JPanel class ,
		 *  used to create the GUI for the bigTwoPanel such as printing avatars and cards to the screen.
		 */
		public void paintComponent(Graphics g) {	
			//Background image
			Image background = new ImageIcon("res/background2.png").getImage();
			g.drawImage(background, 0 , 0, 700,1000, this);
			Font myFont = new Font ("Courier New", 1, 17);
			g.setFont(myFont);

			//Prints the Avatars and the cards beside it
			for(int i =0 ; i < 4 ; i++ ) {
				//show card only for active player
				String player_name = game.getPlayerList().get(i).getName();
				if(player_name!="") {

					if( i == activePlayer) // we set the color for the current player to blue
					{
						g.setColor(Color.BLUE);
					}
					else if (i==game.getPlayerID()) {
						g.setColor(Color.ORANGE);
					}
					else{
						g.setColor(Color.BLACK);
					}

					if(i==game.getPlayerID()) {
						g.drawString(game.getPlayerName() + "(You)", x_offset, y_offset + (avatar_height +gap)*i - 10);
						g.drawImage(avatars[i], x_offset , y_offset + (avatar_height +gap)*i ,avatar_width ,avatar_height, this);
						for(int j =0 ; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
							Card sampleCard  = game.getPlayerList().get(i).getCardsInHand().getCard(j);
							if(selected[j]) {
								g.drawImage(cardImages[sampleCard.suit][sampleCard.rank], 200 + between_cards*j,  y_offset + (avatar_height +gap)*i - raised, this);
							}
							else {
								g.drawImage(cardImages[sampleCard.suit][sampleCard.rank], 200 + between_cards*j,  y_offset + (avatar_height +gap)*i, this);
							}
						}	
					}

					//We dont show cards for non active players
					else {
						g.drawString(game.getPlayerList().get(i).getName(), x_offset, y_offset + (avatar_height +gap)*i - 10);
						g.drawImage(avatars[i], 50 , 100 + (100+gap)*i , 129 ,100, this);
						for(int j =0 ; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
							g.drawImage(cardBackImage, 200 + between_cards*j,  y_offset + (avatar_height +gap)*i, this);
						}	
					}



				}
			}
			
			//Prints the HandsOnTable GUI
			if(game.getHandsOnTable().size()>0)
			{
				g.setColor(Color.BLACK); 
				String playedBy = "Played by " + game.getPlayerList().get(lastPlayerHandOnTable).getName() ; 
				g.drawString(playedBy, 50, 680);
				for(int i = 0 ; i < game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size() ; i++) 
				{
					Card cardOnTable =  game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i); 
					Image usedCard = cardImages[cardOnTable.suit][cardOnTable.rank]; 
					g.drawImage(usedCard, 50 + card_width *i , 700 , this); 
				}
			}
		}
		
		/**
		 * This is the override of mouseClicked method. This is used to locate the cursor pointing at the selected cards and then applying
		 * the raised position of the cards. It scans the cards from right to left. Comparing a current card with the previous card.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			//if bigTwoPanel is enabled we allow mouseclick event
			
			
			int mouseX = e.getX(); // get x coordinate of the clicked cursor
			int mouseY = e.getY(); // get y coordinate of the clicked cursor
			
			
			//checks for selected cards
			int localplayer = game.getPlayerID();
			for(int a = game.getPlayerList().get(localplayer).getNumOfCards() -1 ; a>0; a--) 
			{
				int xCurrent = 200 + a*between_cards; //x coordinate of starting location of current card 
				int xPrevious = 200 + (a-1)*between_cards; //x coordinate of starting location of previous card 
				int yCurrent ; //y coordinate of starting location of current card
				int yPrevious; //y coordinate of starting location of previous card
				
				boolean currentUp = false; // to record whether the current card is selected or not
				boolean previousUp = false;  // to record whether the previous card is selected or not
				
				//This is to make a card respond corresponding to its current state
				if(selected[a]==true) 
				{
					yCurrent = y_offset + (avatar_height +gap)*localplayer - raised; 
					currentUp = true;
				}
				else
				{
					yCurrent = y_offset + (avatar_height +gap)*localplayer;
				}
				
				if(selected[a-1]==true) // if the previous card already selected
				{
					yPrevious = y_offset + (avatar_height +gap)*localplayer - raised;
					previousUp = true;
				}
				else
				{
					yPrevious = y_offset + (avatar_height +gap)*localplayer;
				}
				
				
				if(mouseX>=xCurrent && mouseX <= xCurrent +card_width && mouseY>= yCurrent && mouseY<= yCurrent + card_height) // if the point inside the current card
				{
					
					//sets to selected to true if not yet selected
					if(currentUp == false) 
					{
						selected[a]=true;
					}
					else 
					{
						selected[a]=false;
					}
					break;
				}
				
				
				if(mouseX>=xPrevious && mouseX<= xPrevious + card_width && mouseY>=yPrevious && mouseY<= yPrevious + card_height) // if the point inside the previous card
				{
					
					if(mouseX>=xCurrent && mouseX <= xCurrent +card_width && mouseY>= yCurrent && mouseY<= yCurrent + card_height) // if the point is inside another card we end the loop
					{
						break;
					}
					// if the point is contained inside the previous card
					else 
					{
						if(previousUp == false)
						{
							selected[a-1]=true;
						}
						else
						{
							selected[a-1]=false;
						}
						break; //break out of loop because we dont need to check anymore
					}
				}
			}
			
			//We have to also make the case for only 1 card left, basically the same thing
			if(game.getPlayerList().get(localplayer).getNumOfCards()== 1) 
			{
				int xCurrent = 200;
				int yCurrent ;
				boolean currentUp = false;
				if(selected[0]==true)
				{
					yCurrent = y_offset + (avatar_height +gap)*localplayer - raised;
					currentUp = true;
				}
				else
				{
					yCurrent = y_offset + (avatar_height +gap)*localplayer;
				}
				
				if(mouseX>=xCurrent && mouseX <= xCurrent +card_width && mouseY>= yCurrent && mouseY<= yCurrent +card_height)
				{
					if(currentUp == false)
					{
						selected[0]=true;
					}
					else
					{
						selected[0]=false;
					}
				}
			}
			
			
			
			this.repaint();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * 
	 * an inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle menu-item-click events for the “Restart” menu item. When the “Restart”
menu item is selected, you should (i) create a new BigTwoDeck object and call its
shuffle() method; and (ii) call the start() method of your CardGame object with the
BigTwoDeck object as an argument. Here i also implemented a dialog to ask the user again for confirmation.
	 * 
	 * @author Marco Brian
	 *
	 */

	public class ConnectMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to reset connection?", "Reset Connection",
			        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			    if (response == JOptionPane.YES_OPTION) {
					game.makeConnection();
			    }
		}
	}
	
	/**
	 * an inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle menu-item-click events for the “Quit” menu item. When the “Quit” menu
item is selected, you should terminate your application. Here i also implemented a dialog to ask the user again for confirmation.
	 * 
	 * @author Marco Brian
	 *
	 */
	public class QuitMenuItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to Quit? All progress will be lost.", "Quit Game",
			        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			    if (response == JOptionPane.YES_OPTION) {
			    	System.exit(0);
			    }
			
		}
		
	}
	/**
	 * a method for setting the index of the
last player whose hand is on table.
	 * 
	 * @param lastPlayerHandOnTable last player on table
	 */
	public void setLastPlayerHandOnTable(int lastPlayerHandOnTable) {
		if (lastPlayerHandOnTable < 0 || lastPlayerHandOnTable >= 4) {
			this.lastPlayerHandOnTable = -1;
		} else {
			this.lastPlayerHandOnTable = lastPlayerHandOnTable;
		}
		
	}

	/**
	 * a method for setting the index of the
active player (i.e., the current player).
	 */

	@Override
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= 4) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
		
	}
	/**
	 * a method for repainting the GUI.
	 * 
	 */
	@Override
	public void repaint() {
		frame.repaint();
		
	}
	/**
	 * a method for printing the specified string to the message
area of the GUI
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
		msgArea.append("\n");
		msgArea.setCaretPosition(msgArea.getDocument().getLength()); //set text always scroll to bottom
	}
	/**
	 * a method for clearing the message area of the GUI.
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}
	/**
	 * a method for resetting the GUI. You should (i) reset the list of selected
cards using resetSelected() method from the CardGameTable interface; (ii) clear the
message area using the clearMsgArea() method from the CardGameTable interface;
and (iii) enable user interactions using the enable() method from the CardGameTable
interface.
	 */
	@Override
	public void reset() {
		resetSelected();
		clearMsgArea();
		clearChatMsgArea();
		enable();
		repaint();
	}
	
	/**
	 * a method for enabling user interactions with the GUI. You should (i)
enable the “Play” button and “Pass” button (i.e., making them clickable); and (ii)
enable the BigTwoPanel for selection of cards through mouse clicks.
	 * 
	 */
	@Override
	public void enable() {
		playButton.setEnabled(true); 
		passButton.setEnabled(true);
		}
	
	/**
	 * a method for disabling user interactions with the GUI. You should (i)
disable the “Play” button and “Pass” button (i.e., making them not clickable); and (ii)
disable the BigTwoPanel for selection of cards through mouse clicks.
	 */
	@Override
	public void disable() {
		playButton.setEnabled(false); 
		passButton.setEnabled(false);		
	}


	public void enableConnect() 
	{
		ConnectGame.setEnabled(true);
	}
	
	public void disableConnect() 
	{
		ConnectGame.setEnabled(false);
	}

	public JFrame getFrame() {
		return this.frame;
	}
	
	

	/**
	 * A class that implements a KeyListener. This listener is added to the inputTextField
	 * variable so that it will be able to send a CardGameMessage of type MSG to the server
	 * whenever an enter button is clicked after typing the message.
	 * @author Marco Brian
	 *
	 */
	public class inputTextAreaListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER){
				String message_to_sent = inputChatField.getText();
				if(!message_to_sent.isEmpty()) {
				CardGameMessage message = new CardGameMessage(CardGameMessage.MSG, -1, message_to_sent);
				game.sendMessage(message);
				inputChatField.setText("");
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	/**
	 * Method for printing a message to the chat area
	 * @param msg a string to be printed on the chat text area
	 */
	public void printChatMsg(String msg) {
		chatArea.append(msg);
		chatArea.append("\n");
		chatArea.setCaretPosition(chatArea.getDocument().getLength()); //set text always scroll to bottom
	}
	/**
	 * a method for clearing the chat message area of the GUI.
	 */
	public void clearChatMsgArea() {
		chatArea.setText("");
	}
	

	/**
	 * A public method to enable the chat message features
	 */
	public void enableChat() 
	{
		chatArea.setEnabled(true);
		inputChatField.setEnabled(true);
	}
	
	/**
	 * A public method to disable the chat message features
	 */
	public void disableChat() 
	{
		chatArea.setEnabled(false);
		inputChatField.setEnabled(false);
	}
	
	/**
	 * This is a class for creating a text field that can create a hint,
	 * used for the input text field. It will show a message when it is not clicked.
	 * @author Marco Brian
	 *
	 */
	public class HintTextField extends JTextField {  
		   
		   Font gainFont = new Font("Courier New",1,20);  
		   Font lostFont = new Font("Courier New",Font.ITALIC,20); 
		   
		   public HintTextField(int column,final String hint) {  
			 super(column);
		     setText(hint);  
		     setFont(lostFont);  
		     setForeground(Color.GRAY);  
		   
		     this.addFocusListener(new FocusAdapter() {  
		   
		       @Override  
		       public void focusGained(FocusEvent e) {  
		         if (getText().equals(hint)) {  
		           setText("");  
		           setFont(gainFont);  
				   setForeground(Color.BLACK);
		         } else {  
		           setText(getText());  
		           setFont(gainFont);  
		         }  
		       }  
		   
		       @Override  
		       public void focusLost(FocusEvent e) {  
		         if (getText().equals(hint)|| getText().length()==0) {  
		           setText(hint);  
		           setFont(lostFont);  
		           setForeground(Color.GRAY);  
		         } else {  
		           setText(getText());  
		           setFont(gainFont);  
		           setForeground(Color.BLACK);  
		         }  
		       }  
		     });  
		   
		   }  
		 }  
	
}
