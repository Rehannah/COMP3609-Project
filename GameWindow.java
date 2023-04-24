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

	private BufferedImage image;			// drawing area for each frame

	private Image quit1Image;			// first image for quit button
	private Image quit2Image;			// second image for quit button
	private Image lifeImage;
	private Image noLifeImage;
	private BufferedImage pause1Image;
	private BufferedImage pause2Image;

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
		pause1Image = ImageManager.loadBufferedImage("images/myimages/pause.png");

		setButtonAreas();

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		soundManager = SoundManager.getInstance();
		image = new BufferedImage (pWidth, pHeight, BufferedImage.TYPE_INT_RGB);
		
		backgroundImage = ImageManager.loadImage ("images/myimages/background/pirateship.gif");

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
		// if (!isPaused && isAnimShown && !isAnimPaused)
			// animation.update();
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

		if (level==1) {
			tileMap.draw(imageContext);
		}
		else{
			// render the background image first
			imageContext.drawImage(backgroundImage, 0, 0, pWidth+350, pHeight+80, null);

			if (panel.getPlayer() != null) {
				panel.getPlayer().draw(imageContext);
			}
			else{
				panel.createGameEntities();
			}

			if (panel.treasure.isActive()) {
				panel.treasure.draw(imageContext);
			}

			if (panel.swordPirate.isActive()) {
				panel.swordPirate.draw(imageContext);
			}
			else{
				if (panel.knifePirate.isActive()) {
					panel.knifePirate.draw(imageContext);
				}
				if (panel.bird.isActive()) {
					panel.bird.draw(imageContext);
				}
				if (panel.captain.isActive()) {
					panel.captain.draw(imageContext);
				}
			}			

			// ArrayList<Coconut> coconuts;
			// coconuts = panel.getCoconuts();
			// if (coconuts != null && coconuts.size()!=0) {
			// 	for (int i=0; i<coconuts.size(); i++) {
			// 		Coconut c = coconuts.get(i);
			// 		if (c!=null && c.isActive()) {
			// 			c.draw(imageContext);
			// 			System.out.println("coco drawn");
			// 		}
			// 	}
			// }

			Coconut coconut;
			coconut = panel.getCoconut();
			if (coconut!=null && coconut.isActive()) {
				coconut.draw(imageContext);
			}
			
		}
	
		//Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for window
		drawButtons(imageContext);			// draw the buttons
		drawScore(imageContext);
		Graphics2D g2 = (Graphics2D) gScr;

		g2.drawImage(image, 0, 0, pWidth, pHeight, null);

		imageContext.dispose();
		g2.dispose();
	}

	public void endGame(){
		
		screenUpdate();
		gameOverMessage(this.getGraphics());
		try {
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) {}
		isRunning = false;
		
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

		int leftOffset = (pWidth - (2 * 150) - 20) / 2;
		pauseButtonArea = new Rectangle(leftOffset, 60, 150, 40);

		leftOffset = leftOffset + 170;
		
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

		// g.setColor(Color.BLACK);
		// g.drawOval(quitButtonArea.x, quitButtonArea.y, 
		// 	   quitButtonArea.width, quitButtonArea.height);

		// if (isOverQuitButton)
		// 	g.setColor(Color.WHITE);
		// else
		// 	g.setColor(Color.RED);

		// g.drawString("Quit", quitButtonArea.x+60, quitButtonArea.y+25);

		

		// draw the quit button (an actual image that changes when the mouse moves over it)

		// if (isOverQuitButton){
		// 	if(pause2Image == null){
		// 		pause2Image = ImageManager.copyImage(pause1Image);
		// 		pause2Image = makeTransparent(pause2Image);		
		// 	}
		// 	g.drawImage(pause2Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);
		//     	       //quitButtonArea.width, quitButtonArea.height, null);
		// }
		   
				
		// else
		//    g.drawImage(pause1Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);
		//     	       //quitButtonArea.width, quitButtonArea.height, null);

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

// 	public BufferedImage makeTransparent(BufferedImage im){
// 		int imWidth = im.getWidth(null);
// 		int imHeight = im.getHeight(null);
// 		int pixels[] = new int[imWidth * imHeight];
//         int a, red, green, blue, newValue;

//         im.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
         
//         for (int i=0; i<pixels.length; i++) {
// 			a = pixels[i] >> 24;
// 			a = a*2;
// 			red = (pixels[i] >> 16) & 255;			
//             green = (pixels[i] >> 8) & 255;
//             blue = pixels[i] & 255;
			
//         	if(a > 0){
// 				newValue = blue | (green << 8) | (red << 16) | (a << 24);
// 				pixels[i] = newValue;
// 			}
// 		}
		
// 		im.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
// 		Graphics g = this.getGraphics();
// g.drawImage(im, 0, 0, null);
// 		return im;
//     }

	
	private void startGame() { 
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
			else{
				panel = new GamePanel(this, score);
       	 		panel.setPreferredSize(new Dimension(getWidth(), getHeight()));
			}

			gameThread = new Thread(this);
			gameThread.start();			

		}
	}


	// displays a message to the screen when the user stops the game

	private void gameOverMessage(Graphics g) {
		
		Image gameOver = ImageManager.loadImage("images/myimages/gameOver.png");
		int x = (pWidth - gameOver.getWidth(null)) / 2; 
		int y = (pHeight - gameOver.getHeight(null)) / 2;
		g.drawImage(gameOver, x,y, 300,200,null);

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
			else{
				panel.throwCoconut();
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
		else if (isOverPauseButton) {		// mouse click on Pause button
			isPaused = !isPaused;     	// toggle pausing
		}
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
			isOverQuitButton = quitButtonArea.contains(x,y) ? true : false;
		}
	}
}