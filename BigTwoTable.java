import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * The BigTwoTable implements the CardGameTable interface.
 * It is used to build a GUI for the BigTwo card game and handle all user actions.
 * 
 * @author Hong Ming, Wong
 *
 */
public class BigTwoTable implements CardGameTable{
	
	//commonly used constants that will be used later on
	private static final int frameHeight = 1000;
	private static final int frameWidth = 1500;
	private static final int buttonHeight = 30;
	private static final int buttonWidth = 120;
	private static final int shiftDown = 170;
	private static final int scrollPaneSize = 500;
	private static final int rightShift = 50;
	private static final int selectedShift = 20;
	private static final int stringX = 22;
	private static final int avatarX = 20;
	private static final int cardX = 140;
	private static final int cardInitialY = 50;
	private static final int avatarInitialY = 50;
	private static final int stringInitialY = 25;
	private static final int lineInitialY = 160;
	private static final int cardwidth = 73;
	private static final int cardheight = 96;
	
	
	
	//private variables
	private CardGame game; //the card game associated with the table
	private boolean[] selected; // helps indicate which are selected
	private int activePlayer; //the LOCAL player
	private JFrame frame;
	private BigTwoPanel bigTwoPanel;
	private JPanel playPassPanel; //a panel for the pass and play button
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea; 
	private JTextField textField;
	
	//stores the images
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	
	//used to retrieve the card images
	private final char[] suit = {'d', 'c', 'h', 's'};
	private final char[] rank = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
	
	// a private method to help load the images and store them
	private void loadImages() {
		String location = "image/";
		cardBackImage = new ImageIcon(location + "b.gif").getImage();
		
		cardImages = new Image[13][4];
		//load the images for each card
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				String card = location + rank[i] + suit[j] + ".gif";
				cardImages[i][j] = new ImageIcon(card).getImage();
			}
		}
		
		avatars = new Image[4];
		// load the images for the avatars
		avatars[0] = new ImageIcon(location + "1.png").getImage();
		avatars[1] = new ImageIcon(location + "2.png").getImage();
		avatars[2] = new ImageIcon(location + "3.png").getImage();
		avatars[3] = new ImageIcon(location + "4.png").getImage();
	

	}
	
	//initialize every component of the GUI
	private void buildAll() {
		//create a JFrame
		frame = new JFrame();
		String name = ((NetworkGame) game).getPlayerName();
		
		frame.setTitle("Big Two: " + name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Setting up the table Panel
		bigTwoPanel = new BigTwoPanel();

		
		//Setting up buttons
		playButton = new JButton("Play");
		playButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		passButton = new JButton("Pass");
		passButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		playPassPanel = new JPanel();

		
		playPassPanel.setPreferredSize(new Dimension(frameWidth, 50));
		playPassPanel.add(playButton);
		
		playPassPanel.add(passButton);
		
		JLabel label = new JLabel("                        "
				+ "MESSAGE: ");
		label.setFont(new Font("Futura", Font.BOLD, 20));
		playPassPanel.add(label);
		
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(650, buttonHeight));
		textField.addKeyListener(new EnterKeyListener());
		playPassPanel.add(textField);
	
		
		//Setting up the menu bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Menu");
		menu.setFont(new Font("Futura", Font.BOLD, 20));
		menuBar.add(menu);
		
		JMenuItem connect = new JMenuItem("Connect");
		connect.setFont(new Font("Futura", Font.BOLD, 20));
		menu.add(connect);
		JMenuItem quit = new JMenuItem("Quit");
		quit.setFont(new Font("Futura", Font.BOLD, 20));
		menu.add(quit);
		
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		
		//Setting up text area
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane (msgArea);
		msgArea.setFont(new Font("Futura", Font.BOLD, 23));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(scrollPaneSize, scrollPaneSize - 72));
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		
		//setting up chat area
		chatArea = new JTextArea("This is the chat area. Be nice!\n");
		chatArea.setEditable(false);
		JScrollPane scrollPane2 = new JScrollPane (chatArea);
		chatArea.setFont(new Font("Futura", Font.BOLD, 23));
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setPreferredSize(new Dimension(scrollPaneSize, scrollPaneSize - 72));
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setForeground(Color.WHITE);
		chatArea.setBackground(Color.BLACK);
		
		
		JPanel textPanel = new JPanel();
		textPanel.setPreferredSize(new Dimension(500, frameHeight));
		//final frame configurations
		textPanel.add(scrollPane);
		textPanel.add(scrollPane2);
		
		frame.add(BorderLayout.EAST, textPanel);
		frame.add(BorderLayout.CENTER, bigTwoPanel);
		frame.add(BorderLayout.SOUTH, playPassPanel);
		
		
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
	}
	
	/**
	 * A constructor for creating a BigTwoTable GUI.
	 * @param game A reference to a card game associated with the table.
	 */
	public BigTwoTable(CardGame game){
		this.game = game;
		selected = new boolean[13];
		loadImages();
		buildAll();
		disable();
		
		
	}
	
	/**
	 * A method for setting the index of the active player
	 * @param activePlayer the index of the active player
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
		
	}

	/**
	 * A method for getting an array of indices of the cards selected
	 * @return an array of indices of the selected card
	 */
	@Override
	public int[] getSelected() {
		
		int size = 0;
		for (int i = 0; i < selected.length; i++) {
			if (selected[i] == true) {
				size++;
			}
		}
		
		if (size == 0) return null;
		
		int[] selectedcards = new int[size];
		size = 0; //reset
		for (int i = 0; i < selected.length; i++) {
			if (selected[i] == true) {
				selectedcards[size] = i;
				size++;
			}
		}
		
		
		return selectedcards;
	}

	/**
	 * A method to reset the entire selected array to false;
	 * 
	 */
	@Override
	public void resetSelected() {
		if (selected == null) return;
		
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
			
		}
		
	}
	
	/**
	 * A method to repaint the entire GUI.
	 * It calls resetSelected() and then repaints the frame.
	 */
	@Override
	public void repaint() {
		resetSelected();
		frame.repaint();
		
	}
	
	/**
	 * A method to print a string to the Message Area of the GUI.
	 * @param msg The message
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
		msgArea.append("\n");
		msgArea.setCaretPosition(msgArea.getText().length());
	}

	/**
	 * A method to append a string the Chat Area of the GUI.
	 * 
	 * @param msg The message
	 */
	public void printChatArea(String msg) {
		chatArea.append(msg);
		chatArea.append("\n");
		chatArea.setCaretPosition(chatArea.getText().length());
	}
	

	/**
	 * A method to clear the message are of the GUI
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * A method for resetting the GUI.
	 * 
	 */
	@Override
	public void reset() {
		disable();
		resetSelected();
		clearMsgArea();
		repaint();
		enable();
		
	}

	/**
	 * A method for enabling use interaction with the GUI.
	 */
	@Override
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
		
	}
	
	/**
	 * A method for disabling user interaction with GUI except for the menu bar.
	 */
	@Override
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	//deals with events when player tries to pass.
	private class PassButtonListener implements ActionListener{
		/**
		 * When the pass button is clicked, it will call the makeMove method 
		 * of the CardGame interface.
		 * It will then repaint the entire GUI.
		 * @param a ActionEvent object
		 */
		@Override
		public void actionPerformed(ActionEvent a) {
			game.makeMove(activePlayer, null);
			repaint();
		}

	}
	
	
	// An inner class that implements the ActionListener interface.
	//It handles events when the play button is pressed.
	
	private class PlayButtonListener implements  ActionListener{
		/**
		 * If no cards have been selected, this method will warn players.
		 * If cards have been selected, then it will call the MakeMove method
		 * of the CardGame interface.
		 * Regardless of cards selected or not, it will call the repaint method at the end.
		 * @param e ActionEvent object
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (getSelected() == null) {
				printMsg("You need to select your cards!");
			}
			
			else {
				game.makeMove(activePlayer, getSelected());
			}
			repaint();
		}
		
	}
	
	
	private class ConnectMenuItemListener implements ActionListener{
		/**
		 * When the Connect Menu Item is clicked, it prompts the user for connection details, and attempts to make a new connection.
		 * @param arg0 ActionEvent Object
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			NetworkGame x = (NetworkGame) game;
			x.makeConnection();
			
		}
		
	}
	
	//event when player want to quit
	private class QuitMenuItemListener implements ActionListener{

		/**
		 * When the 'Quit' menu is clicked, it closes the entire frame.
		 * @param e ActionEvent object
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			printMsg("Thank you for playing!");
			((BigTwoClient) game).closeStream();
			System.exit(0);
			
		}
		
	}
	

	// An inner subclass of JPanel that implements the MouseListener interface.
	// It overrides the paintComponent() method and the mouseClicked() method.
	private class BigTwoPanel extends JPanel implements MouseListener{
		/**
		 * The default public constructor. It adds itself to as the MouseListener.
		 * 
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		
		/**
		 * Allows the Active Player to select their cards.
		 * When the cards are selected, it will pop up.
		 * When the cards are de-selected, it will return to its original position.
		 * @param mouse MouseEvent object
		 * 
		 */
		@Override
		public void mouseClicked(MouseEvent mouse) {
			if (this.isEnabled() == false) return;
			int normalcardX = cardX;
			int cardY = cardInitialY;
			
			int x = mouse.getX();
			int y = mouse.getY();
			int numberOfCards = game.getPlayerList().get(activePlayer).getNumOfCards();	
			
			for (int i = 0; i < numberOfCards; i++) {
				
				if (selected[i] == false) { //unselected cards in their initial position
					int lowerbound = (activePlayer * shiftDown) + cardY; // the top side of the card
					int ymiddlebound = lowerbound + cardheight - selectedShift; //if the next card is selected, below this have more area 
					int upperbound = lowerbound + cardheight; //the low side of the card
					int leftbound =  normalcardX + (rightShift * i); //the left side of the card
					int rightbound = leftbound + rightShift; // the right side of the card
					int xextendedbound = leftbound + cardwidth; //if the next card is selected, extended get the edge of card
					
					if (i != (numberOfCards - 1) && selected[i + 1] == true) { //if not the last card  and the next card is selected
						if (y > lowerbound && y < ymiddlebound) {
							if (x > leftbound && x < rightbound) {
								selected[i] = true;
								break;
							}
						}
						
						else if (y > ymiddlebound && y < upperbound) {
							if (x > leftbound && x < xextendedbound) {
								selected[i] = true;
								break;
							}
						}
					}
					
					else {
						if (i == (numberOfCards - 1)) {
							if (y > lowerbound && y < upperbound) {
								if (x > leftbound && x < xextendedbound) {
									selected[i] = true;
									break;
								}
							}
						}
						
						
						else { 
							if (y > lowerbound && y < upperbound) {
								
								if (x > leftbound && x < rightbound) {
									selected[i] = true;
									break;
								}
							}
						}
					}
				}
				
				
				else if (selected[i] == true) {
					
					int lowerbound = cardY + (activePlayer * shiftDown) - selectedShift; //different from above
					int ymiddlebound = lowerbound + selectedShift;
					int upperbound = lowerbound + cardheight;
					int leftbound =  normalcardX + (rightShift * i);
					int rightbound = leftbound + rightShift;
					int xextendedbound = leftbound + cardwidth;
					
					if (i != numberOfCards - 1 && selected[i + 1] == true) {
						if (y > lowerbound && y < upperbound) { //the above part
							if (x > leftbound && x < rightbound) {
								selected[i] = false;
								break;
							}
						}
						
					}
					else {
						if (i == numberOfCards - 1) {
							if (y > lowerbound && y < upperbound) {
								if (x > leftbound && x < xextendedbound) {
									selected[i] = false;
									break;
								}
							}
						}
						else {
						
							if (y > lowerbound && y < ymiddlebound) {
								if (x > leftbound && x < xextendedbound) {
									selected[i] = false;
									break;
								}
							}
						
							else if (y > ymiddlebound && y < upperbound) {
								//the lower part of the card
								if (x > leftbound && x < rightbound) {
									selected[i] = false;
									break;
								}
							}
						}
					}
				}
			}
			
			this.repaint();
		}
		
		/**
		 * Draws out the entire look of the BigTwoTable GUI.
		 * It prints out all the player names, the line separation between their table,
		 * along with cards. The active player's cards will be painted with the cards facing up,
		 * while other players will have their cards painted down.
		 * The cards' position will also be painted based on if they are selected or not.
		 * @param g Graphics object
		 * 
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g); //called the superclass method so that background continues to be drawn

			
			this.setBackground(Color.red.brighter().brighter());
			
			//these are variables that change
			int avatarY = avatarInitialY;
			int stringY = stringInitialY;
			int lineY = lineInitialY;
			int normalcardX = cardX;
			int cardY = cardInitialY;
			
			
			
			g.setFont(new Font("Futura", Font.BOLD, 19));
			
			
			//draws all the avatar images and player names
			for (int i = 0; i < 4; i++) {
				if (game.getPlayerList().get(i).getName() == null) {
					g.drawString("<Empty>", stringX, stringY);
				}
				else {
					String player = "";
					if (i == game.getCurrentIdx()) player = "Active => ";
					player += game.getPlayerList().get(i).getName();
					g.drawString(player, stringX, stringY);
					g.drawImage(avatars[i], avatarX, avatarY, this);
				}
				avatarY += shiftDown;
				stringY += shiftDown;
			}
			
			
			
			for (int i = 0; i < 4; i++) {
				g.drawLine(0, lineY, 1500, lineY);
				lineY += shiftDown;
			}
			
			
			
			for (int i = 0; i < 4; i++) {
				int cardinHand = game.getPlayerList().get(i).getNumOfCards();
				
				if (i == activePlayer) {
					CardList hand = game.getPlayerList().get(i).getCardsInHand();
					for (int j = 0; j < cardinHand; j++) {
						int rank = hand.getCard(j).getRank();
						int suit = hand.getCard(j).getSuit();
						
						if (selected[j] == true) {
							 g.drawImage(cardImages[rank][suit], normalcardX + j * rightShift, cardY - selectedShift, this);
						}
						
						else g.drawImage(cardImages[rank][suit], normalcardX + j * rightShift, cardY, this);
					}
				}
					
					
					
				else if (i != activePlayer) {
					for (int j = 0; j < cardinHand; j++) {
						g.drawImage(cardBackImage, normalcardX +  j * rightShift, cardY, this);
					}
				}
				cardY += shiftDown;
			}
			
			
			if (game.getHandsOnTable().size() == 0) {
				g.drawString("No hands have been played yet. ", stringX, stringY);
				return;
			}
			
			
			Hand previousHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
			g.drawString("Previous Hand Played: " + previousHand.getType() + " by " + previousHand.getPlayer().getName(), stringX, stringY);
			for (int i = 0; i < previousHand.size(); i++) {
				int rank = previousHand.getCard(i).getRank();
				int suit = previousHand.getCard(i).getSuit();
				g.drawImage(cardImages[rank][suit], normalcardX + i * rightShift, cardY, this);
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	//Key listener
	private class EnterKeyListener implements KeyListener{

		/**
		 * When the enter key is pressed, it sends the message to chat.
		 * @param e none
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER) {
				String text = textField.getText();
				textField.setText("");
				textField.requestFocus();
				Object txt = text;
				NetworkGame x = (NetworkGame) game;
				CardGameMessage message = new CardGameMessage(CardGameMessage.MSG, x.getPlayerID(), txt);
				x.sendMessage(message);
				
				
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
}
