import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * The BigTwoClient implements the CardGame interface and NetworkGame interface.
 * It is used to model a online multi-player GUI BigTwo card game.
 * 
 * @author Hong Ming, Wong
 *
 */

public class BigTwoClient implements CardGame, NetworkGame{
	
	//private variables and methods
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID; //the player ID of the local player
	private String playerName; //name of the local player
	private final String defaultIP = "127.0.0.1"; 
	private String serverIP;
	private final int defaultPort = 2396;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx = -1;
	private BigTwoTable table;
	private static final String[] names = {"Bob", "Shrek"};
	private void connectionSetup(){
		serverIP = defaultIP;
		serverPort = defaultPort;
		String IP = JOptionPane.showInputDialog("IP Address (LOCAL by default):");
		if (IP != null && !IP.isEmpty()) {
			serverIP = IP;
		}
		
		String port = JOptionPane.showInputDialog("Server Port (2396 by default):");
		if (port != null && !port.isEmpty()) {
			try {
				int inum = Integer.parseInt(port);
				serverPort = inum;
			}catch (Exception ex) {
				ex.printStackTrace();
				serverPort = defaultPort;
			}	
		}
	}
	
	/**
	 * This public method closes the stream and the socket.
	 * 
	 */
	public void closeStream() {
		try {
			oos.close();
			sock.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * A constructor for creating a BigTwo client.
	 * It first asks for the player's name, the desired IP Address and TCP port.
	 * It then creates four players and add them to the list of players.
	 * After that, it creates a BigTwo table.
	 * Finally, it attempts to make a connection with the server.
	 * 
	 */
	public BigTwoClient() {
		playerName = JOptionPane.showInputDialog("Enter your name:");
		if (playerName == null || playerName.isEmpty()) {
			Integer code = (int) (Math.random() * 1000);
			int index = (int) (Math.random() * 2);
			if (index == 2) index = 1;
			playerName =  names[index] + code.toString(); 
		}
		
		handsOnTable = new ArrayList<Hand>();
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++) {
			CardGamePlayer player = new CardGamePlayer();
			player.setName(null);
			playerList.add(player);
		}
		table = new BigTwoTable(this);

		makeConnection();
		table.repaint();
	}


	@Override
	/**
	 * A getter method of the number of players.
	 * @return The number of players
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	/**
	 * A getter method of the deck.
	 * @return the deck of cards used to play the game.
	 */
	@Override
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Returns the list of players in the game.
	 * @return the list of players.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	/**
	 * Getter method of the hands played on the table.
	 * @return the hands played on the table
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}

	/**
	 * A method for getting the index of the player in the current turn.
	 * @return the index of the current player.
	 */
	@Override
	public int getCurrentIdx() {
		return currentIdx;
	}

	/**
	 * A method for starting/restarting the game.
	 * It removes all the cards from the players and the table.
	 * It then distributes cards to the players with the new deck.
	 * It identifies the player with the 3 of diamonds.
	 * It also sets the active player of the BigTwoTable to the player ID of the local player.
	 * 
	 * @param deck A shuffled deck of cards
	 */
	@Override
	public void start(Deck deck) {
		//1. remove all the cards from the players
		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).removeAllCards();	
		}
		//2. remove all the cards on the table
		handsOnTable.clear();
		//3. distribute the specified deck, because deck is shuffled in main
		int distribute = 0;
		for (int i = 0; i < 52; i++) {
			Card check = deck.getCard(i);
			playerList.get(distribute).addCard(check);
			if (check.getRank() == 2 && check.getSuit() == 0) {
				currentIdx = distribute; //if player has the 3 of diamond, the player will start first
			}
			distribute = (distribute + 1) % 4;
		}
		playerList.get(0).getCardsInHand().sort();
		playerList.get(1).getCardsInHand().sort();
		playerList.get(2).getCardsInHand().sort();
		playerList.get(3).getCardsInHand().sort();
		table.setActivePlayer(playerID);//sets the active player to THE LOCAL PLAYER
		table.repaint();
		table.enable();
		
	}

	/**
	 * A method of making a move by a player with the specified playerID
	 * using the cards specified by the list of indices.
	 * It checks whether the move made is valid or not.
	 * If valid, it creates a CardGameMessage object of type MOVE and sends it the game server.
	 * If not, it prompts the player to reselect cards.
	 * @param playerID the playerID
	 * @param cardIdx the list of indices
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		Object data = cardIdx;
		if(currentIdx != playerID) {
			table.printMsg("Please wait for your turn.");
			return;
		}
		if (cardIdx == null && handsOnTable.size() == 0) {
			table.printMsg("You need to play a move.");
			return;
		}
		if(cardIdx == null && handsOnTable.get(handsOnTable.size() - 1).getPlayer().getName() == playerList.get(playerID).getName()) {
			table.printMsg("You have to play something!");
			return;
		}
		
		if (verifyMove(playerID, cardIdx) == false) {
			table.printMsg("Not a legal move!");
			table.printMsg("Please reselect your cards.");
			table.printMsg("");
			return;
		}
		
		
		CardGameMessage move = new CardGameMessage(CardGameMessage.MOVE, -1, data);
		sendMessage(move);
	
	}

	
	//a private move to verify if a hand is valid or not.
	private boolean verifyMove(int playerID, int[] cardIdx) {
		if (cardIdx == null) return true;
		//assuming something has been played
		boolean valid = false;
		CardGamePlayer player = playerList.get(playerID);
		CardList candidate = player.play(cardIdx);
		Hand result = composeHand(player, candidate);
		
		//if no valid hand can be returned then false
		if (result == null) return false;
		
		//if a valid hand is return but it's the first move
		if (handsOnTable.size() == 0) {
			int i = 0;
			while (valid == false && i < result.size()){
				if (result.getCard(i).getRank() == 2 && result.getCard(i).getSuit() == 0) {
					valid = true;
					break;
				}
				else i++;
				}
		}				
		
		//if a valid hand is returned but it's not the first move
		else if (handsOnTable.size() != 0) {
			valid = result.beats(handsOnTable.get(handsOnTable.size() - 1));
		}
		return valid;
	}
	
	
	
	/**
	 * A method for checking a move made by a player.
	 * It assumes that the move made is always valid.
	 * When a move is made, it updates the message area, 
	 * as well as the current index.
	 * It also checks whether the game has ended or not.
	 * If the game has ended, then it prompts a dialog that displays the result.
	 * It then sends a CardGameMessage of type READY to signify that the player is ready.
	 * @param playerID the ID of the player who made the move
	 * @param cardIdx the list of indices
	 */
	
	public void checkMove(int playerID, int[] cardIdx) {
		CardGamePlayer player = playerList.get(playerID);
		
		if (cardIdx == null) {
			table.printMsg(player.getName() + " plays: ");
			table.printMsg("{Pass}");
			table.printMsg("");
			currentIdx = (currentIdx + 1) % 4;
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn to play.");
			return;
		}
		else{
			CardList candidate = player.play(cardIdx);
			Hand result = composeHand(player, candidate);
			handsOnTable.add(result);
			player.removeCards(candidate);
			table.printMsg(player.getName() + " plays: ");
			table.printMsg("{" + result.getType() +"}" + result.toString());
			table.printMsg("");
		}
			
		
	///check if  the end of the game has reached
		if (endOfGame()) {
			String endGame = "Game ends.\n";
			
			
			for (int i = 0; i < 4; i++) {
				String name = playerList.get(i).getName();
				if (i == currentIdx) {
					endGame += (name + " wins the game.\n");
				}
				else {
					int cardsleft = getPlayerList().get(i).getNumOfCards();
					if (cardsleft == 1) {
						endGame += (name +  " has 1 card in hand.\n");
					}
					else {
						endGame += (name + " has " + cardsleft + " cards in hand.\n");
					}
				}
			}
			endGame += ("Thank you for playing!");
			table.disable();
			
			JOptionPane.showMessageDialog(null, endGame); 
			sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));		
		}
		
		//not the end of the game
		else {
			currentIdx = (currentIdx + 1) % 4;
			table.printMsg(playerList.get(currentIdx).getName() + "'s turn to play.");
			table.repaint();
		}
	}


	/**
	 * Checks whether the game has ended or not.
	 * @return if ended, yes. else, no.
	 */
	@Override
	public boolean endOfGame() {
		for (int i = 0; i < playerList.size(); i++) {
			if (playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * A method for getting the playerID of the local player.
	 * @return the index of the local player
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * A method for setting the index of the local player.
	 * @param playerID the playerID
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * A method for getting the name of the local player.
	 * @return the name of the local player
	 */
	@Override
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * A method for setting the name of the local player
	 * @param playerName the name of the local player
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		
	}

	/**
	 * A method for getting the Server IP address
	 * @return the Server IP
	 */
	@Override
	public String getServerIP() {
		return serverIP;
	}

	
	/**
	 * A method for setting the ServerIP
	 * @param serverIP the ServerIP 
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
		
	}

	/**
	 * A method for getting the TCP port of the game server.
	 *@return the TCP port of the game server
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * A method for setting the TCP port of the game server
	 * @param serverPort the TCP port of the game server
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * A method for making a socket connection.
	 * Upon a successful connection, an ObjectOutputStream is created
	 * for sending messages.
	 * It also creates a separate thread for receiving messages from the game server.
	 * 
	 */
	@Override
	public void makeConnection() {
		
		
		//if there is already a connection
	
		if (sock != null) {
			table.printMsg("You're connected to a game!");
			return;
		}
		
		connectionSetup();
		
		try {
			sock = new Socket(serverIP, serverPort);
			oos = new ObjectOutputStream(sock.getOutputStream());
			Thread receive = new Thread(new ServerHandler());
			receive.start();
			table.printMsg("Successful Connection! Say Hi!");
			table.repaint();
			
		}catch(Exception ex) {
			table.printMsg("Failed to connect. Please try again.");
			ex.printStackTrace();
		}
		
	}

	/**
	 * A method of parsing the messages received from the game server.
	 * @param message the GameMessage received from the server.
	 */
	@Override
	public synchronized void parseMessage(GameMessage message) {
	
		switch (message.getType()) {
		case CardGameMessage.JOIN:{
			int newPlayer = message.getPlayerID();
			String name = (String) message.getData();
			playerList.get(newPlayer).setName(name);
			if (playerID == newPlayer) {
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
			break;
		}
		case CardGameMessage.FULL:{
			//ignore player ID and data
			table.printMsg("Sorry, the server is full.");
			table.printMsg("Please try again later.");
			try {
				oos.close();
				sock.close();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				sock = null;
				oos = null;
			}
			return;
		}
		case CardGameMessage.QUIT:{
			int quitter = message.getPlayerID();
			table.printMsg(playerList.get(quitter).getName() + " has left the game.");
			table.printMsg("");
			
			playerList.get(quitter).setName(null);
			
			if (endOfGame() == false) {
				table.disable();
				for (int i = 0; i < 4; i++) {
					getPlayerList().get(i).removeAllCards();
					
				}
				handsOnTable.clear();
				currentIdx = -1;
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				
			}
			break;
		}
		case CardGameMessage.READY:{
			int ready = message.getPlayerID();
			String name = playerList.get(ready).getName();
			table.printMsg(name +" is ready to play.");
			break;
		}
		case CardGameMessage.START:{
			deck = (BigTwoDeck) message.getData();
			start(deck);
			table.enable();
			table.printMsg("New Game has started!");
			table.printMsg(playerList.get(getCurrentIdx()).getName() + " will start first!");
			break;
		}
		case CardGameMessage.MOVE:{
			int id = message.getPlayerID();
			int[] cardIdx = (int[]) message.getData();
			checkMove(id, cardIdx);
			break;
		}
		case CardGameMessage.MSG:{
			String MSG = (String) message.getData();
			table.printChatArea(MSG);
			return;
		}
		case CardGameMessage.PLAYER_LIST:{
			setPlayerID(message.getPlayerID());
			playerList.get(playerID).setName(playerName);
			String[] list = (String[]) message.getData();
			for (int counter = 0; counter < 4; counter++) {
				if (counter == playerID);
				if (list[counter] == null);
				else {
					playerList.get(counter).setName(list[counter]);
				}
			}
			
			CardGameMessage joinMessage = new CardGameMessage(CardGameMessage.JOIN, -1, playerName);
			sendMessage(joinMessage);
			break;
		}
		default:
			break;
		}
		table.repaint();
	}

	/**
	 * A method for sending the specified message to the game server.
	 * @param message a GameMessage object
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//an inner class that implements the Runnable interface.
	//creates a ObjectInputStream that receives data from the 
	//server and parses it.
	private class ServerHandler implements Runnable{
		@Override
		public void run() {
			try {
				ObjectInputStream oinput = new ObjectInputStream(sock.getInputStream());
				Object input;
				while((input = oinput.readObject()) != null) {
					parseMessage((CardGameMessage) input);
				}
				
				oinput.close();
			}catch (Exception ex) {
				ex.printStackTrace();
			}
			
			table.repaint();
			
		}
		
	}
	
	/**
	 * A method for composing a valid hand.
	 * If no valid hand can be created, then it returns null.
	 * @param player the specified player
	 * @param cards the list of cards intended to be played.
	 * @return a valid hand or null 
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {		
		if (cards.size() == 1) {
			Single single = new Single(player, cards);
			if (single.isValid()) {
				return single;
			}
		}
		else if (cards.size() == 2) {
			Pair pair = new Pair(player, cards);
			if (pair.isValid()) {
				return pair;
			}
		}
		else if (cards.size()== 3) {
			Triple triple = new Triple(player, cards);
			if (triple.isValid()) {
				return triple;
			}
		}
		
		else if (cards.size() == 5){
				
				//in descending order, from highest valued 5 cards combination to lowest
			StraightFlush straightflush = new StraightFlush(player, cards);;
				if (straightflush.isValid()) {
					return straightflush;
				}
				
			Quad quad = new Quad(player, cards);
				if (quad.isValid()) {
					return quad;
				} 
				
			FullHouse fullhouse = new FullHouse(player, cards);
				if (fullhouse.isValid()) {
					return fullhouse;
				} 
				
			Flush flush = new Flush(player, cards);
				if (flush.isValid()) {
					return flush;
				}
				
			Straight straight = new Straight(player, cards);
				if (straight.isValid()) {
					return straight;
				}
				
		}
		return null;
			
	}

	/**
	 * The driver method. It creates an instance of BigTwoClient.
	 * @param args None.
	 */
	public static void main(String[] args) {
		new BigTwoClient();
	}
}