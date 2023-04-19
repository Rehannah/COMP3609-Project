import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for certain AWT classes
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.image.BufferStrategy;	// need this to implement page flipping


public class GameWindow extends JFrame implements
				Runnable,
				KeyListener,
				MouseListener,
				MouseMotionListener
{
  	private static final int NUM_BUFFERS = 2;	// used for page flipping

	private int pWidth, pHeight;     		// width and height of screen

	private Thread gameThread = null;            	// the thread that controls the game
	private volatile boolean isRunning = false;    	// used to stop the game thread

	private BirdAnimation animation = null;		// animation sprite

	private BufferedImage image;			// drawing area for each frame

	private Image quit1Image;			// first image for quit button
	private Image quit2Image;			// second image for quit button
	private Image lifeImage;
	private Image noLifeImage;

	private boolean finishedOff = false;		// used when the game terminates

	private volatile boolean isOverQuitButton = false;
	private Rectangle quitButtonArea;		// used by the quit button

	private volatile boolean isOverPauseButton = false;
	private Rectangle pauseButtonArea;		// used by the pause 'button'
	private volatile boolean isPaused = false;

	private volatile boolean isOverStopButton = false;
	private Rectangle stopButtonArea;		// used by the stop 'button'
	private volatile boolean isStopped = false;

	private volatile boolean isOverShowAnimButton = false;
	private Rectangle showAnimButtonArea;		// used by the show animation 'button'
	private volatile boolean isAnimShown = false;

	private volatile boolean isOverPauseAnimButton = false;
	private Rectangle pauseAnimButtonArea;		// used by the pause animation 'button'
	private volatile boolean isAnimPaused = false;
   
	private GraphicsDevice device;			// used for full-screen exclusive mode 
	private Graphics gScr;
	private BufferStrategy bufferStrategy;

	private SoundManager soundManager;
	private TileMapManager tileManager;
	private TileMap	tileMap;
	private Score score;

	private GamePanel panel;
	private Image backgroundImage;

	private int level=1;



	public GameWindow() {
 
		super("Treasure RecovARRy");

		initFullScreen();

		quit1Image = ImageManager.loadImage("images/Quit1.png");
		quit2Image = ImageManager.loadImage("images/Quit2.png");
		lifeImage = ImageManager.loadImage("images/myimages/lives/life.png");
		noLifeImage = ImageManager.loadImage("images/myimages/lives/nolife.png");

		setButtonAreas();

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		animation = new BirdAnimation();
		soundManager = SoundManager.getInstance();
		image = new BufferedImage (pWidth, pHeight, BufferedImage.TYPE_INT_RGB);
		
		backgroundImage = ImageManager.loadImage ("images/myimages/background/pirateship.gif");

		panel = new GamePanel(this);
        panel.setPreferredSize(new Dimension(getWidth(), getHeight()));

		startGame();
	}


	// implementation of Runnable interface

	public void run () {
		try {
			isRunning = true;
			while (isRunning) {
				if (isPaused == false) {
					if (level==1) {
						gameUpdate();
					}
					else{
						panel.gameUpdate();
					}
				}
				screenUpdate();
				Thread.sleep (50);
			}
		}
		catch(InterruptedException e) {}

		finishOff();
	}


	/* This method performs some tasks before closing the game.
	   The call to System.exit() should not be necessary; however,
	   it prevents hanging when the game terminates.
	*/

	private void finishOff() { 
    		if (!finishedOff) {
			finishedOff = true;
			restoreScreen();
			System.exit(0);
		}
	}


	/* This method switches off full screen mode. The display
	   mode is also reset if it has been changed.
	*/

	private void restoreScreen() { 
		Window w = device.getFullScreenWindow();
		
		if (w != null)
			w.dispose();
		
		device.setFullScreenWindow(null);
	}


	public void gameUpdate () {
		if (level==1) {
			tileMap.update();
		}
		if (!isPaused && isAnimShown && !isAnimPaused)
			animation.update();
		panel.gameUpdate();
	}


	private void screenUpdate() { 

		try {
			gScr = bufferStrategy.getDrawGraphics();
			gameRender(gScr);
			gScr.dispose();
			if (!bufferStrategy.contentsLost())
				bufferStrategy.show();
			else
				System.out.println("Contents of buffer lost.");
      
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)

			Toolkit.getDefaultToolkit().sync();
		}
		catch (Exception e) { 
			e.printStackTrace();  
			isRunning = false; 
		} 
	}

	public Score getScore(){
		return score;
	}

	public void gameRender (Graphics gScr) {		// draw the game objects

		Graphics2D imageContext = (Graphics2D) image.getGraphics();

		if (level==1) {
			tileMap.draw(imageContext);
		}
		else{
			// render the background image first
			imageContext.drawImage(backgroundImage, 0, 0, pWidth, pHeight, null);

			if (panel.getPlayer() != null) {
				panel.getPlayer().draw(imageContext);
			}
			else{
				panel.createGameEntities();
			}

			if (panel.swordPirate.isActive()) {
				panel.swordPirate.draw(imageContext);
			}
			if (panel.knifePirate.isActive()) {
				panel.knifePirate.draw(imageContext);
			}
			// if (panel.bird.isActive()) {
			// 	panel.bird.draw(imageContext);
			// }
			if (panel.captain.isActive()) {
				panel.captain.draw(imageContext);
			}
		}
	
		if (isAnimShown)
			animation.draw(imageContext);		// draw the animation

		//Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for window
		drawButtons(imageContext);			// draw the buttons
		drawScore(imageContext);
		Graphics2D g2 = (Graphics2D) gScr;
		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		imageContext.dispose();
		g2.dispose();
	}


	private void drawScore(Graphics2D g) {

		Font oldFont, newFont;
		int leftOffset = pWidth - 425;

		oldFont = g.getFont();		// save current font to restore when finished
	
		newFont = new Font ("Cambria", Font.BOLD, 20);
		g.setFont(newFont);		// set this as font for text on buttons

    	g.setColor(Color.black);	// set outline colour of button
		g.drawString("Points Scored", leftOffset, 60);
		g.drawString("Lives Remaining", leftOffset-15, 110);
		newFont = new Font ("Cambria", Font.BOLD, 25);
		g.setFont(newFont);
		g.drawString(Integer.toString(score.getPoints()), leftOffset+210, 63);
		g.setFont(oldFont);

		leftOffset += 150;
		int i;
		int width = 50;
		int height = 43;
		// draw full hearts for the lives the player has
		for(i=0; i<score.getLives(); i++){
			g.drawImage(lifeImage, leftOffset, 85, width, height, null);	
			leftOffset += width;
		}

		// draw empty hearts for the lives the player doesn't have
		while(i<3){
			g.drawImage(noLifeImage, leftOffset, 85, width, height, null);	// draw the background image
			leftOffset += width;
			i++;
		}

		// pointsL.setText("Points Scored");
		// pointsL.setHorizontalAlignment(JLabel.CENTER);		
		// pointsL.setForeground(textColour);
		// pointsL.setBackground(colour);
		// pointsL.setOpaque(true);

		// livesL.setText("Lives");		
		// livesL.setHorizontalAlignment(JLabel.CENTER);
		// livesL.setFont(new Font("Cambria", Font.BOLD, 20));
		// livesL.setForeground(textColour);
		// livesL.setBackground(colour);
		// livesL.setOpaque(true);
		
		// pointsTF.setHorizontalAlignment(JLabel.CENTER);
		// pointsTF.setFont(new Font("Cambria", Font.BOLD, 20));
		// pointsTF.setEditable(false);
		// pointsTF.setBackground(colour);
		// pointsTF.setForeground(textColour);
		// pointsTF.setBorder(BorderFactory.createEmptyBorder());	 


	}


	private void initFullScreen() {				// standard procedure to get into FSEM

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();

		setUndecorated(true);	// no menu bar, borders, etc.
		setIgnoreRepaint(true);	// turn off all paint events since doing active rendering
		setResizable(false);	// screen cannot be resized
		
		if (!device.isFullScreenSupported()) {
			System.out.println("Full-screen exclusive mode not supported");
			System.exit(0);
		}

		device.setFullScreenWindow(this); // switch on full-screen exclusive mode

		// we can now adjust the display modes, if we wish

		showCurrentMode();

		pWidth = getBounds().width;
		pHeight = getBounds().height;
		
		System.out.println("Width of window is " + pWidth);
		System.out.println("Height of window is " + pHeight);

		try {
			createBufferStrategy(NUM_BUFFERS);
		}
		catch (Exception e) {
			System.out.println("Error while creating buffer strategy " + e); 
			System.exit(0);
		}

		bufferStrategy = getBufferStrategy();
	}


	// This method provides details about the current display mode.

	private void showCurrentMode() {
/*
		DisplayMode dm[] = device.getDisplayModes();

		for (int i=0; i<dm.length; i++) {
			System.out.println("Current Display Mode: (" + 
                           dm[i].getWidth() + "," + dm[i].getHeight() + "," +
                           dm[i].getBitDepth() + "," + dm[i].getRefreshRate() + ")  " );			
		}

		//DisplayMode d = new DisplayMode (800, 600, 32, 60);
		//device.setDisplayMode(d);
*/

		DisplayMode dm = device.getDisplayMode();

		System.out.println("Current Display Mode: (" + 
                           dm.getWidth() + "," + dm.getHeight() + "," +
                           dm.getBitDepth() + "," + dm.getRefreshRate() + ")  " );
  	}


	// Specify screen areas for the buttons and create bounding rectangles

	private void setButtonAreas() {
		
		//  leftOffset is the distance of a button from the left side of the window.
		//  Buttons are placed at the top of the window.

		int leftOffset = (pWidth - (5 * 150) - (4 * 20)) / 2;
		pauseButtonArea = new Rectangle(leftOffset, 60, 150, 40);

		// leftOffset = leftOffset + 170;
		// stopButtonArea = new Rectangle(leftOffset, 60, 150, 40);

		// leftOffset = leftOffset + 170;
		// showAnimButtonArea = new Rectangle(leftOffset, 60, 150, 40);

		// leftOffset = leftOffset + 170;
		// pauseAnimButtonArea = new Rectangle(leftOffset, 60, 150, 40);

		leftOffset = leftOffset + 170;
		// int quitLength = quit1Image.getWidth(null);
		// int quitHeight = quit1Image.getHeight(null);
		// quitButtonArea = new Rectangle(leftOffset, 55, 180, 50);
		
		quitButtonArea = new Rectangle(leftOffset, 60, 150, 40);
	}


	private void drawButtons (Graphics g) {
		Font oldFont, newFont;

		oldFont = g.getFont();		// save current font to restore when finished
	
		newFont = new Font ("TimesRoman", Font.ITALIC + Font.BOLD, 18);
		g.setFont(newFont);		// set this as font for text on buttons

    		g.setColor(Color.black);	// set outline colour of button

		// draw the pause 'button'

		g.setColor(Color.BLACK);
		g.drawOval(pauseButtonArea.x, pauseButtonArea.y, 
			   pauseButtonArea.width, pauseButtonArea.height);

		if (isOverPauseButton && !isStopped)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);	

		if (isPaused && !isStopped)
			g.drawString("Resume", pauseButtonArea.x+45, pauseButtonArea.y+25);
		else
			g.drawString("Pause", pauseButtonArea.x+50, pauseButtonArea.y+25);

		// draw the stop 'button'

		g.setColor(Color.BLACK);
		g.drawOval(quitButtonArea.x, quitButtonArea.y, 
			   quitButtonArea.width, quitButtonArea.height);

		if (isOverQuitButton)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);

		g.drawString("Quit", quitButtonArea.x+60, quitButtonArea.y+25);

		// draw the show animation 'button'

		// g.setColor(Color.BLACK);
		// g.drawOval(showAnimButtonArea.x, showAnimButtonArea.y, 
		// 	   showAnimButtonArea.width, showAnimButtonArea.height);

		// if (isOverShowAnimButton && !isPaused && !isStopped)
		// 	g.setColor(Color.WHITE);
		// else
		// 	g.setColor(Color.RED);
      	// 	g.drawString("Start Anim", showAnimButtonArea.x+35, showAnimButtonArea.y+25);

		// draw the pause anim 'button'

		// g.setColor(Color.BLACK);
		// g.drawOval(pauseAnimButtonArea.x, pauseAnimButtonArea.y, 
		// 	   pauseAnimButtonArea.width, pauseAnimButtonArea.height);

		// if (isOverPauseAnimButton && isAnimShown && !isPaused && !isStopped)
		// 	g.setColor(Color.WHITE);
		// else
		// 	g.setColor(Color.RED);

		// if (isAnimShown && isAnimPaused && !isStopped)
		// 	g.drawString("Anim Paused", pauseAnimButtonArea.x+30, pauseAnimButtonArea.y+25);
		// else
		// 	g.drawString("Pause Anim", pauseAnimButtonArea.x+35, pauseAnimButtonArea.y+25);

		// draw the quit button (an actual image that changes when the mouse moves over it)

		// if (isOverQuitButton)
		//    g.drawImage(quit1Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);
		//     	       //quitButtonArea.width, quitButtonArea.height, null);
				
		// else
		//    g.drawImage(quit2Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);
		//     	       //quitButtonArea.width, quitButtonArea.height, null);

		// g.setColor(Color.BLACK);
		// g.drawOval(quitButtonArea.x, quitButtonArea.y, 
		// 	   quitButtonArea.width, quitButtonArea.height);
		// if (isOverQuitButton)
		// 	g.setColor(Color.WHITE);
		// else
		// 	g.setColor(Color.RED);

		// g.drawString("Quit", quitButtonArea.x+60, quitButtonArea.y+25);

		g.setFont(oldFont);		// reset font

	}


	private void startGame() { 
		System.out.println("stat");
		if (gameThread == null) {
			//soundManager.playSound ("background", true);
			score =  new Score(this);
			tileManager = new TileMapManager (this, score);
			if (level==1) {
				try {
					tileMap = tileManager.loadMap("maps/map.txt");
					int w, h;
					w = tileMap.getWidth();
					h = tileMap.getHeight();
					System.out.println ("Width of tilemap " + w);
					System.out.println ("Height of tilemap " + h);
				}
				catch (Exception e) {
					System.out.println(e);
					System.exit(0);
				}

			}

			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	// displays a message to the screen when the user stops the game

	private void gameOverMessage(Graphics g) {
		
		Font font = new Font("SansSerif", Font.BOLD, 24);
		FontMetrics metrics = this.getFontMetrics(font);

		String msg = "Game Over. Thanks for playing!";

		int x = (pWidth - metrics.stringWidth(msg)) / 2; 
		int y = (pHeight - metrics.getHeight()) / 2;

		g.setColor(Color.BLUE);
		g.setFont(font);
		g.drawString(msg, x, y);

	}


	// implementation of methods in KeyListener interface

	public void keyPressed (KeyEvent e) {

		if (isPaused)
			return;

		int keyCode = e.getKeyCode();

		if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
             	   (keyCode == KeyEvent.VK_END)) {
           		isRunning = false;		// user can quit anytime by pressing
			return;				//  one of these keys (ESC, Q, END)			
         	}
		else
		if (keyCode == KeyEvent.VK_LEFT) {
			if (level==2) {
				panel.updatePlayer(1);
			}
		}
		else
		if (keyCode == KeyEvent.VK_RIGHT) {
			if (level==2) {
				panel.updatePlayer(2);
			}
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			if (level==1) {
				tileMap.jump();
			}
		}
		else
		if (keyCode == KeyEvent.VK_UP) {
			//bat.moveUp();
		}
		else
		if (keyCode == KeyEvent.VK_DOWN) {
			//bat.moveDown();
		}

	}


	public void keyReleased (KeyEvent e) {

	}


	public void keyTyped (KeyEvent e) {

	}


	// implement methods of MouseListener interface

	public void mouseClicked(MouseEvent e) {

	}


	public void mouseEntered(MouseEvent e) {

	}


	public void mouseExited(MouseEvent e) {

	}


	public void mousePressed(MouseEvent e) {
		testMousePress(e.getX(), e.getY());
	}


	public void mouseReleased(MouseEvent e) {

	}


	// implement methods of MouseMotionListener interface

	public void mouseDragged(MouseEvent e) {

	}	


	public void mouseMoved(MouseEvent e) {
		testMouseMove(e.getX(), e.getY()); 
	}


	/* This method handles mouse clicks on one of the buttons
	   (Pause, Stop, Start Anim, Pause Anim, and Quit).
	*/

	private void testMousePress(int x, int y) {

		if (isStopped && !isOverQuitButton) 	// don't do anything if game stopped
			return;

		// if (isOverStopButton) {			// mouse click on Stop button
		// 	isStopped = true;
		// 	isPaused = false;
		// }
		else
		if (isOverPauseButton) {		// mouse click on Pause button
			isPaused = !isPaused;     	// toggle pausing
		}
		// else 
		// if (isOverShowAnimButton && !isPaused) {// mouse click on Start Anim button
		// 	isAnimShown = true;
		//  	isAnimPaused = false;
		// 	animation.start();
		// }
		// else
		// if (isOverPauseAnimButton) {		// mouse click on Pause Anim button
		// 	if (isAnimPaused) {
		// 		isAnimPaused = false;
		// 		animation.playSound();
		// 	}
		// 	else {
		// 		isAnimPaused = true;	// toggle pausing
		// 		animation.stopSound();
		// 	}
		// }
		else if (isOverQuitButton) {		// mouse click on Quit button
			isRunning = false;		// set running to false to terminate
		}
  	}


	/* This method checks to see if the mouse is currently moving over one of
	   the buttons (Pause, Stop, Show Anim, Pause Anim, and Quit). It sets a
	   boolean value which will cause the button to be displayed accordingly.
	*/

	private void testMouseMove(int x, int y) { 
		if (isRunning) {
			isOverPauseButton = pauseButtonArea.contains(x,y) ? true : false;
			// isOverStopButton = stopButtonArea.contains(x,y) ? true : false;
			// isOverShowAnimButton = showAnimButtonArea.contains(x,y) ? true : false;
			// isOverPauseAnimButton = pauseAnimButtonArea.contains(x,y) ? true : false;
			isOverQuitButton = quitButtonArea.contains(x,y) ? true : false;
		}
	}


	// public void setPoints(int points) {
	// 	score.setPoints(points);
	// }


    // public void setLives(int lives) {
	// 	score.setLives(lives);
    // }

}