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

	public int pWidth, pHeight;     		// width and height of screen

	private Thread gameThread = null;            	// the thread that controls the game
	private volatile boolean isRunning = false;    	// used to stop the game thread

	private BufferedImage image;			// drawing area for each frame
	
	private Image lifeImage;
	private Image noLifeImage;

	private boolean finishedOff = false;		// used when the game terminates

	private volatile boolean isOverQuitButton = false;
	private Rectangle quitButtonArea;		// used by the quit button

	private volatile boolean isOverPauseButton = false;
	private Rectangle pauseButtonArea;		// used by the pause 'button'
	private volatile boolean isPaused = false;
   
	private GraphicsDevice device;			// used for full-screen exclusive mode 
	private Graphics gScr;
	private BufferStrategy bufferStrategy;

	private SoundManager soundManager;
	private TileMapManager tileManager;
	private TileMap	tileMap;
	private Score score;

	private GamePanel panel;

	private int level=1;

	private long musicStartTime;

	private boolean motion;
	private boolean changingLevel;
	private BufferedImage levelUpImage;
	private int timeElapsed;



	public GameWindow() {
 
		super("Treasure RecovARRy");

		initFullScreen();

		lifeImage = ImageManager.loadImage("images/lives/life.png");
		noLifeImage = ImageManager.loadImage("images/lives/nolife.png");

		setButtonAreas();

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		soundManager = SoundManager.getInstance();
		image = new BufferedImage (pWidth, pHeight, BufferedImage.TYPE_INT_RGB);
		
		
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
		if (level==2) {
			panel.gameUpdate();
		}
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

		if (level == 1) {
			tileMap.draw(imageContext);
		}
		else{
			if(changingLevel)
				drawLevelGraphic(imageContext);
			if(timeElapsed > 1)
				panel.gameRender(imageContext);			
		}
	
		if(motion)
			drawButtons(imageContext);			// draw the buttons
		
		drawScore(imageContext);
		Graphics2D g2 = (Graphics2D) gScr;

		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		imageContext.dispose();
		g2.dispose();
	}

	private void drawLevelGraphic(Graphics2D g2) {		
		if(levelUpImage == null)
			getLevelUpImage();

		int x = (getWidth() - levelUpImage.getWidth(null)) / 2; 
		int y = (getHeight() - levelUpImage.getHeight(null)) / 2;

		g2.drawImage(levelUpImage, x,y, 300,200,null);

		timeElapsed++;
		if(timeElapsed>100)
			changingLevel = false;
	}


	private void getLevelUpImage() {
		levelUpImage = ImageManager.loadBufferedImage("images/levelUP.png");

		int imWidth = levelUpImage.getWidth();
        int imHeight = levelUpImage.getHeight();
        int [] pixels = new int[imWidth * imHeight];
        int a, colours, newValue;
        levelUpImage.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
         
        for (int i=0; i<pixels.length; i++) {
            a = (pixels[i] >>> 24);
			colours = pixels[i] & 16777215;
            if (a > 100) {
				a = 100;				
               	newValue = colours | (a << 24);
               	pixels[i] = newValue;
            }
        }
        levelUpImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	
      
	}


	public void endGame(){
		
		soundManager.stopSound("l1background");
		soundManager.stopSound("l2background");
		soundManager.stopSound("bird");
		screenUpdate();				
		gameOverMessage(this.getGraphics());
		soundManager.playSound("lose", false);
		try {
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) {}
		isRunning = false;
		
	}


	private void drawScore(Graphics2D g) {

		Font oldFont, newFont;
		int leftOffset = pWidth - 400;

		oldFont = g.getFont();		// save current font to restore when finished
	
		newFont = new Font ("Cambria", Font.BOLD, 20);
		g.setFont(newFont);		// set this as font for text on buttons

    	g.setColor(Color.black);	// set outline colour of button
		g.drawString("Points Scored", leftOffset, 50);
		g.drawString("Player Lives", leftOffset+5, 100);
		newFont = new Font ("Cambria", Font.BOLD, 25);
		g.setFont(newFont);
		g.drawString(Integer.toString(score.getPoints()), leftOffset+210, 53);
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

		// showCurrentMode();

		pWidth = getBounds().width;
		pHeight = getBounds().height;
		
		// System.out.println("Width of window is " + pWidth);
		// System.out.println("Height of window is " + pHeight);

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
		DisplayMode dm = device.getDisplayMode();

		System.out.println("Current Display Mode: (" + 
                           dm.getWidth() + "," + dm.getHeight() + "," +
                           dm.getBitDepth() + "," + dm.getRefreshRate() + ")  " );
  	}


	// Specify screen areas for the buttons and create bounding rectangles

	private void setButtonAreas() {
		
		//  leftOffset is the distance of a button from the left side of the window.
		//  Buttons are placed at the top of the window.

		int leftOffset = (pWidth - (2 * 150) - 20) / 2;
		pauseButtonArea = new Rectangle(leftOffset, 50, 150, 40);

		leftOffset = leftOffset + 170;
		
		quitButtonArea = new Rectangle(leftOffset, 50, 150, 40);
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

		if (isOverPauseButton)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);	

		if (isPaused)
			g.drawString("Resume", pauseButtonArea.x+45, pauseButtonArea.y+25);
		else
			g.drawString("Pause", pauseButtonArea.x+50, pauseButtonArea.y+25);


		// draw the quit 'button'
		g.setColor(Color.BLACK);
		g.drawOval(quitButtonArea.x, quitButtonArea.y, 
			   quitButtonArea.width, quitButtonArea.height);

		if (isOverQuitButton)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.RED);

		g.drawString("Quit", quitButtonArea.x+60, quitButtonArea.y+25);

		g.setFont(oldFont);		// reset font

	}
	
	private void startGame() { 
		if (gameThread == null) {
			motion = true;
			
			score =  new Score();
			
			panel = new GamePanel(this, score);
			panel.setPreferredSize(new Dimension(getWidth(), getHeight()));
		
			if (level==1) {
				try {	
					soundManager.playSound ("l1background", true);				
					tileManager = new TileMapManager (this, score);
					tileMap = tileManager.loadMap("maps/map1.txt");
					/*  int w, h;
					w = tileMap.getWidth();
					h = tileMap.getHeight();
					System.out.println ("Width of tilemap " + w);
					System.out.println ("Height of tilemap " + h);
					*/
				}
				catch (Exception e) {
					System.out.println(e);
					System.exit(0);
				}

			}
			else{
				soundManager.playSound ("l2background", true);
			}

			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	// displays a message to the screen when the user loses the game

	private void gameOverMessage(Graphics g) {
		
		Image gameOver = ImageManager.loadImage("images/lose.png");
		int x = (pWidth - gameOver.getWidth(null)) / 2; 
		int y = (pHeight - gameOver.getHeight(null)) / 2;
		g.drawImage(gameOver, x,y, 300,200,null);

	}


	// implementation of methods in KeyListener interface

	public void keyPressed (KeyEvent e) {		
		
		int keyCode = e.getKeyCode();

		if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END)) {
           	isRunning = false;		// user can quit anytime by pressing
			return;				//  one of these keys (ESC, Q, END)			
        }
		
		if(!motion)
			return;
		
		if(isPaused)
			return; 
		
		if (keyCode == KeyEvent.VK_SPACE) {
			if (level==1) {
				tileMap.jump();
			}
			else{
				panel.updatePlayer(5);
			}
			return;
		}

		if (keyCode == KeyEvent.VK_LEFT) {
			if (level==2) {
				panel.updatePlayer(1);
			}
			return;
		}
		
		if (keyCode == KeyEvent.VK_RIGHT) {
			if (level==2) {
				panel.updatePlayer(2);
			}
			return;
		}

		if (keyCode == KeyEvent.VK_ENTER) {
			if (level==2) {
				panel.throwCoconut();
			}
			return;
		}
	}

	public void keyReleased (KeyEvent e) {		

		if(!motion)
			return;

		int keyCode = e.getKeyCode();
		
		if (keyCode == KeyEvent.VK_LEFT) {
			if (level==2) {
				panel.updatePlayer(3);
			}
			return;
		}
		
		if (keyCode == KeyEvent.VK_RIGHT) {
			if (level==2) {
				panel.updatePlayer(4);
			}
			return;
		}
	}

	public void keyTyped (KeyEvent e) {}


	// implement methods of MouseListener interface

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		testMousePress(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {}

	// implement methods of MouseMotionListener interface

	public void mouseDragged(MouseEvent e) {}	

	public void mouseMoved(MouseEvent e) {
		testMouseMove(e.getX(), e.getY()); 
	}


	/* This method handles mouse clicks on one of the buttons
	   (Pause and Quit).
	*/

	private void testMousePress(int x, int y) {

		if(!motion)
			return;

		if (isOverPauseButton) {		// mouse click on Pause button
			isPaused = !isPaused;     	// toggle pausing
			if(isPaused){
				if(level == 1){
					musicStartTime = soundManager.getClipPosition("l1background");		//save time of background music for restart
					soundManager.stopSound("l1background");	
					return;
				}
				musicStartTime = soundManager.getClipPosition("l2background");		//save time of background music for restart
				soundManager.stopSound("l2background");	
				soundManager.stopSound("bird");
				return;
			}
			if(level == 1)
				soundManager.playSound("l1background", true, musicStartTime);
			else
				soundManager.playSound("l2background", true, musicStartTime);
		}
		else if (isOverQuitButton) {		// mouse click on Quit button
			isRunning = false;		// set running to false to terminate
		}
  	}


	/* This method checks to see if the mouse is currently moving over one of
	   the buttons (Pause and Quit). It sets a boolean value which will cause 
	   the button to be displayed accordingly.
	*/

	private void testMouseMove(int x, int y) { 
		if(!motion)
			return;
		
			if (isRunning) {
			isOverPauseButton = pauseButtonArea.contains(x,y) ? true : false;
			isOverQuitButton = quitButtonArea.contains(x,y) ? true : false;
		}
	}

	public void increaseLevel() {
		level=2;
		changingLevel = true;
		timeElapsed = 0;
		soundManager.stopSound("l1background");
		soundManager.playSound("l2background", true);
	}


	public void setMotion(boolean m) {
		motion = m;
	}


	public void winGame() {
		winMessage(this.getGraphics());
		try {
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) {}
		isRunning = false;


	}


	private void winMessage(Graphics g) {
		
		Image gameOver = ImageManager.loadImage("images/win.png");
		int x = (getWidth() - gameOver.getWidth(null)) / 2; 
		int y = (getHeight() - gameOver.getHeight(null)) / 2;
		g.drawImage(gameOver, x,y, 300,200,null);
	}


	public int getScreenWidth() {
		return device.getDisplayMode().getWidth();
	}
}