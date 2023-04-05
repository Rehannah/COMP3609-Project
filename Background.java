import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class Background {
  	private Image bgImage;
  	private int bgImageWidth;      		

	private Dimension dimension;

 	private int bgX;
	private int backgroundX;
	private int backgroundX2;
	private int bgDX;			// size of the background move (in pixels)


	public Background(JFrame window, String imageFile, int bgDX) {

    	this.bgImage = loadImage(imageFile);
		dimension = window.getSize();
    	bgImageWidth = bgImage.getWidth(window);	// get width of the background
    		this.bgDX = bgDX;
		backgroundX = 0;
		backgroundX2 = bgImageWidth;

  	}


  	public void moveRight() {

		if (bgX == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;
					
		}

		bgX = bgX - bgDX;

		backgroundX = backgroundX - bgDX;
		backgroundX2 = backgroundX2 - bgDX;

		if ((bgX + bgImageWidth) % bgImageWidth == 0) {
			System.out.println ("Background change: bgX = " + bgX); 
			backgroundX = 0;
			backgroundX2 = bgImageWidth;
		}


  	}


  	public void moveLeft() {
	
		if (bgX == 0) {
			backgroundX = bgImageWidth * -1;
			backgroundX2 = 0;			
		}

		bgX = bgX + bgDX;
				
		backgroundX = backgroundX + bgDX;	
		backgroundX2 = backgroundX2 + bgDX;

		if ((bgX + bgImageWidth) % bgImageWidth == 0) {
			//System.out.println ("Background change: bgX = " + bgX); 
			backgroundX = bgImageWidth * -1;
			backgroundX2 = 0;
		}			
   	}
 

  	public void draw (Graphics2D g2) {
		System.out.println("back"+bgImageWidth+" "+backgroundX+" "+backgroundX2);	
		g2.drawImage(bgImage, backgroundX, 0, 1920, 1080, null);
		g2.drawImage(bgImage, backgroundX2, 0, 1920, 1080, null);
  	}


  	public Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
  	}

}
