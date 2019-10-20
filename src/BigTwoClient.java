import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BigTwoClient implements CardGame, NetworkGame {
	private int lastwinnerIdx;  //holds the id of the last player who placed a hand on the table
	private int numOfPlayers; //the number of players in the game
	private Deck deck ; //the deck used for the game
	private ArrayList<CardGamePlayer> playerList ;//a list of players.
	private ArrayList<Hand> handsOnTable; //a list of hands played on the table.
	private int playerID; //an integer specifying the playerID (i.e., index) of the local player.
	private String playerName; // a string specifying the name of the local player.
	private String serverIP ; // a string specifying the IP address of the game server.
	private int serverPort; // an integer specifying the TCP port of the game server.
	private Socket sock ; //a socket connection to the game server.
	private ObjectOutputStream oos; //an ObjectOutputStream for sending messages to the server.
	private int currentIdx; // an integer specifying the index of the player for the current turn.
	private BigTwoTable table; // a Big Two table which builds the GUI for the game and handles all user actions.
	private BigTwoCard ThreeOfDiamonds = new BigTwoCard(0,2); //Three of diamonds card
	
	/**
	 * a constructor for creating a Big Two client. You should (i) create 4
players and add them to the list of players; (ii) create a Big Two table which builds the
GUI for the game and handles user actions; and (iii) make a connection to the game
server by calling the makeConnection() method from the NetworkGame interface.
	 */
	public BigTwoClient() {
		String input = JOptionPane.showInputDialog("Input your name");
		while(input.isEmpty()) {
			input = JOptionPane.showInputDialog("Name is empty");
		}
		
		this.playerName = input;
		this.playerList = new ArrayList<CardGamePlayer>();
		this.handsOnTable = new ArrayList<Hand>();
		this.numOfPlayers = 4;
		for(int i = 0 ; i < this.numOfPlayers;i++) {
			this.playerList.add(new CardGamePlayer(""));
		}
		
		table = new BigTwoTable(this);
		table.disable();
		makeConnection();
		
		
	}


	/**
	 * method for creating an instance of BigTwoClient
	 * @param args not used
	 */
	public static void main(String[] args) {
		new BigTwoClient();
	}
	/**
	 * a method for
returning a valid hand from the specified list of cards of the player. Returns null if no
valid hand can be composed from the specified list of cards.
	 * @param player player that made the hand
	 * @param cards list of cards selected
	 * @return Hand a hand of cards
	 */
	public Hand composeHand(CardGamePlayer player,CardList cards) {
		if(cards==null) {
			return null;
		}
		
		int hand_size = cards.size();
		Hand hand_returned;
		if(hand_size==1) {
			hand_returned = new Single(player,cards);
			return hand_returned;
		}
		else if(hand_size==2) {
			hand_returned = new Pair(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}
		}
		else if(hand_size==3) {
			hand_returned = new Triple(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}
		}
		else if(hand_size==5) {

			hand_returned = new StraightFlush(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}

			hand_returned = new Straight(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}
			
			hand_returned = new Flush(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}
			
			hand_returned = new FullHouse(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}
			
			hand_returned = new Quad(player,cards);
			if(hand_returned.isValid()) {
				return hand_returned;
			}
			
			
		}
		return null;
	}
	
	/**
	 * class ServerHandler – an inner class that implements the Runnable interface. You
should implement the run() method from the Runnable interface and create a thread
with an instance of this class as its job in the makeConnection() method from the
NetworkGame interface for receiving messages from the game server. Upon receiving a
message, the parseMessage() method from the NetworkGame interface should be
called to parse the messages accordingly.
	 */

	public class ServerHandler implements Runnable{

		@Override
		public void run() {
	
			try {
				// reads incoming messages from the server
				CardGameMessage servermessage; 
				ObjectInputStream reader = new ObjectInputStream(sock.getInputStream()); //gets servers input stream
				while ((servermessage = (CardGameMessage) reader.readObject()) != null) { //gets message from server
					parseMessage(servermessage);  //parses the message 
				} 
				
			} catch (Exception ex) {
				ex.printStackTrace(); //prints error messages
			}
		}
		
	}
	//NetworkGame interface 
	
	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 */
	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return this.playerID;
	}

	/**
	 * a method for setting the playerID (i.e., index) of
the local player. This method should be called from the parseMessage() method when a
message of the type PLAYER_LIST is received from the game server.
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * a method for getting the name of the local player.
	 */
	@Override
	public String getPlayerName() {
		return this.playerName;
	}
	/**
	 * a method for setting the name of the local
player.
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * method for getting the IP address of the game server
	 */

	@Override
	public String getServerIP() {
		return this.serverIP;
	}

	/**
	 * a method for setting the IP address of the game
server.
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;		
	}
	
	/**
	 * a method for getting the TCP port of the game server.
	 */

	@Override
	public int getServerPort() {
		return this.serverPort;
	}
	/**
	 * a method for setting the TCP port of the game
server.
	 */

	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;		
	}
/**
 * a method for making a socket connection with the game
server. Upon successful connection, you should (i) create an ObjectOutputStream for
sending messages to the game server; (ii) create a thread for receiving messages from
the game server; (iii) send a message of the type JOIN to the game server, with playerID
being -1 and data being a reference to a string representing the name of the local
player; (iv) send a message of the type READY to the game server, with playerID and
data being -1 and null, respectively.
 */
	@Override
	public void makeConnection() {
		try {
			sock = new Socket("127.0.0.1",2396);
			oos = new ObjectOutputStream(sock.getOutputStream());
			//receive message from server
			Thread serverHandling = new Thread(new ServerHandler());
			serverHandling.start();

			setServerIP((sock.getRemoteSocketAddress().toString()));
			setServerPort(2396);
			sendMessage(new CardGameMessage(CardGameMessage.JOIN,-1,this.playerName));
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
			System.out.println("network established");
		}
		catch(IOException e) {
			e.printStackTrace();
			table.enableConnect();
		}

		

	}
/**
 * a method for parsing the messages
received from the game server. This method should be called from the thread
responsible for receiving messages from the game server. Based on the message type,
different actions will be carried out (please refer to the general behavior of the client
described in the previous section).
 */
	@Override
	public void parseMessage(GameMessage message) {
		switch (message.getType()) {
		case CardGameMessage.PLAYER_LIST:
			//update player ID
			setPlayerID(message.getPlayerID());
			String[] name_array = (String[]) message.getData();
			for(int i=0 ; i < name_array.length;i++) {
				if(name_array[i] != null) {
					playerList.get(i).setName(name_array[i]);
				}
			}
			table.disableConnect();
			table.enableChat();
			break;
		case CardGameMessage.JOIN:
			//update player name
			getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			table.repaint();
			break;
		case CardGameMessage.FULL:
			//For players that tries to connect when table is full
			table.printMsg("Sorry table is full.");
			table.printMsg("You will not be able to join the game.");	
			table.disable();
			table.enableConnect();
			table.disableChat();
			break;
		case CardGameMessage.QUIT:
			//Stops the game and informs players of the person who quit the game
			table.printMsg(getPlayerList().get(message.getPlayerID()).getName() + " has quit the game");
			getPlayerList().get(message.getPlayerID()).setName("");
			table.disable();
			table.repaint();
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			break;
		case CardGameMessage.READY:
			// marks the specified player as ready for a new game
			table.repaint();
			table.printMsg(getPlayerList().get(message.getPlayerID()).getName() + " is ready!");
			break;
		case CardGameMessage.START:
			//starts the game after 4 players have joined
			table.enable();
			start((BigTwoDeck) message.getData()); //gets the shuffled big two deck
			break;
		case CardGameMessage.MOVE:
			//Checks other players' moves
			checkMove(message.getPlayerID(), (int[]) message.getData());
			break;
		case CardGameMessage.MSG:
			//Prints the message from other players to the table
			table.printChatMsg((String) message.getData() + "\n");
			break;
		default:
			//Wrong message
			table.printMsg("Wrong message type: " + message.getType());
			break;
		}
	}
/**
 * a method for sending the specified
message to the game server. This method should be called whenever the client wants to
communicate with the game server or other clients.
 */
	@Override
	public void sendMessage(GameMessage message) {

		try {
			//send the message to the server
			oos.writeObject(message);
		} catch(Exception E) {
			//tell Player the message cannot be sent
			table.printMsg("Error sending message! Please try again!" + "\n");
			E.printStackTrace();
		}
	}
	
	/**
	 * method for getting the number of players.
	 */
	
   // CardGame interface methods
	@Override
	public int getNumOfPlayers() {
		// TODO Auto-generated method stub
		return this.numOfPlayers;
	}
	/**
	 * a method for getting the deck of cards being used.
	 */
	@Override
	public Deck getDeck() {
		// TODO Auto-generated method stub
		return this.deck;
	}
	/**
	 * a method for getting the list of players.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		// TODO Auto-generated method stub
		return this.playerList;
	}
	/**
	 * a method for getting the list of hands played on
the table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		// TODO Auto-generated method stub
		return this.handsOnTable;
	}
	/**
	 * a method for getting the index of the player for the current turn.
	 */
	@Override
	public int getCurrentIdx() {
		
		return this.currentIdx;
	}
	/**
	 * a method for starting/restarting the game with a given
shuffled deck of cards. You should (i) remove all the cards from the players as well as
from the table; (ii) distribute the cards to the players; (iii) identify the player who holds
the 3 of Diamonds; (iv) set the currentIdx of the BigTwoClient instance to the
playerID (i.e., index) of the player who holds the 3 of Diamonds; and (v) set the
activePlayer of the BigTwoTable instance to the playerID (i.e., index) of the local
player (i.e., only shows the cards of the local player and the local player can only select
cards from his/her own hand).
	 * 
	 */
	@Override
	public void start(Deck deck) {
		this.deck =deck;
		handsOnTable = new ArrayList<Hand>();
		lastwinnerIdx = -1;

		for(CardGamePlayer player: playerList) {
			player.removeAllCards();
		}
		
		int count=0;
		for(CardGamePlayer player : playerList) {
			for(int j= 0 ; j < 13 ; j++) {
				player.addCard(deck.getCard(count));
				count++;
			}
		}
		
		for(CardGamePlayer player : playerList) {
			player.sortCardsInHand();
		}
		
		for(CardGamePlayer player:playerList) {
			if(player.getCardsInHand().contains(ThreeOfDiamonds)) {
				currentIdx=playerList.indexOf(player);
				this.table.setActivePlayer(currentIdx);
			}
		}
		
		//Small game description 
		table.reset();
		table.printMsg("WINTER IS HERE! White walkers are approaching closer to the land of the living and soon will take over Westeros!");
		table.printMsg("");
		table.printMsg("All political issues will now be solved through the BigTwo game, he who wins the game shall sit on the Iron Throne!!");
		table.printMsg("");
		table.printMsg("The first player to start is the one who holds the three of diamonds [3" + "\u2666" + "]");
		
		//Disables non active player and enables the active player
		if(this.getPlayerID()!=currentIdx) {
			table.disable();
		}
		else {
			table.enable();
		}
		
		
	}
	/**
	 * a method for making a move by a
player with the specified playerID using the cards specified by the list of indices. This
method should be called from the BigTwoTable when the local player presses either the
“Play” or “Pass” button. You should create a CardGameMessage object of the type
MOVE, with the playerID and data in this message being -1 and cardIdx, respectively,
and send it to the game server using the sendMessage() method from the NetworkGame
interface.
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx));
	}
	/**
	 * a method for checking a move
made by a player. This method should be called from the parseMessage() method from
the NetworkGame interface when a message of the type MOVE is received from the game
server. The playerID and data in this message give the playerID of the player who
makes the move and a reference to a regular array of integers specifying the indices of
the selected cards, respectively. These are used as the arguments in calling the
checkMove() method.
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {

		
		//If the selected cards is a null array then the user must have passed this round
		if(cardIdx==null) {
			table.printMsg(getPlayerList().get(currentIdx).getName()+ " has passed this turn");
			currentIdx=(currentIdx+1)%4;
			table.setActivePlayer(currentIdx);
			table.resetSelected();
			if(this.getPlayerID()!=currentIdx) {
				table.disable();
			}
			else {
				table.enable();
			}
			table.repaint();
			return;
		}
		
		//Gets the hands from the current_player
		CardGamePlayer currentPlayer = playerList.get(playerID);
		CardList played_cards = currentPlayer.play(cardIdx);
		Hand hand_played = composeHand(currentPlayer,played_cards);
		
		//if hand_played is null, the cards chosen is not a valid hand
		if(hand_played==null) {
			table.printMsg(getPlayerList().get(currentIdx).getName()+ " makes an invalid move");
			table.resetSelected();
			return;
		}
		
		
		else if(handsOnTable.size()==0) {
			table.first_round=false;
			if(!hand_played.contains(ThreeOfDiamonds)) {
				table.printMsg("First hand requires the Three Of Diamonds");
			}
			else{
				handsOnTable.add(hand_played);
				currentPlayer.removeCards(hand_played);
				lastwinnerIdx = currentIdx;
				String hand_played_string = hand_played.toString();
				table.printMsg(getPlayerList().get(currentIdx).getName());
				table.printMsg("{"+hand_played.getType()+"}");
				table.printMsg(hand_played_string);
				currentIdx = (currentIdx +1) % 4;
				table.setActivePlayer(currentIdx);
				table.setLastPlayerHandOnTable(lastwinnerIdx);
				if(endOfGame()) {
					table.disable();
					String result = "Game Over! Here are the results: \n" ; 
					for(CardGamePlayer player : playerList) {
						if(player==playerList.get(lastwinnerIdx)) {
							result = result + player.getName() + " wins the game\n";
						}
						else {
							result = result + player.getName() + " has " + player.getCardsInHand().size() + " cards in hand.\n";
						}
					}
					result = result + "Do you want to join again? Click Yes to join and No to quit the game";
					int response = JOptionPane.showConfirmDialog(table.getFrame(), result, "Game end",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					    if (response == JOptionPane.YES_OPTION) {
							sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
							table.printMsg("Waiting for other players");
					    }
					    else {
					    	System.exit(0);
					    }
					
				}
				

			}
		}
		else {
			if(hand_played.beats(handsOnTable.get(handsOnTable.size()-1)) || lastwinnerIdx==currentIdx) {
				handsOnTable.add(hand_played);
				currentPlayer.removeCards(hand_played);
				lastwinnerIdx = currentIdx;
				String hand_played_string = hand_played.toString();
				table.printMsg(getPlayerList().get(currentIdx).getName());
				table.printMsg("{"+hand_played.getType()+"}");
				table.printMsg(hand_played_string);
				currentIdx = (currentIdx +1) % 4;
				table.setActivePlayer(currentIdx);
				table.setLastPlayerHandOnTable(lastwinnerIdx);
				if(endOfGame()) {
					table.disable();
					String result = "Game Over! Here are the results: \n" ; 
					for(CardGamePlayer player : playerList) {
						if(player==playerList.get(lastwinnerIdx)) {
							result = result + player.getName() + " wins the game\n";
						}
						else {
							result = result + player.getName() + " has " + player.getCardsInHand().size() + " cards in hand.\n";
						}
					}
					result = result + "Do you want to join again? Click Yes to join and No to quit the game\n";
					int response = JOptionPane.showConfirmDialog(table.getFrame(), result, "Game end",
					        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					    if (response == JOptionPane.YES_OPTION) {
							sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
					    }
					    else {
					    	System.exit(0);
					    }
				}
			}
			else {
				table.printMsg(getPlayerList().get(currentIdx).getName()+ " makes an invalid move");
				String hand_played_string = hand_played.toString();
				table.printMsg("{"+hand_played.getType()+"}");
				table.printMsg(hand_played_string + "<== Invalid");
				table.resetSelected();
			}
		}

		if(this.getPlayerID()!=currentIdx) {
			table.disable();
		}
		else {
			table.enable();
		}

		table.repaint();
		
	}
	/**
	 * a method for checking if the game ends.
	 */
	@Override
	public boolean endOfGame() {
		boolean gameover = false;
		if(playerList.get(lastwinnerIdx).getCardsInHand().isEmpty()) {
			gameover=true;
		}
		return gameover;
		}
	}


