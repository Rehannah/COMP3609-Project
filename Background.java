import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class Background {
  	private Image bgImage;
  	private int bgImageWidth;   
 	private int bgX;
	private int backgroundX;
	private int backgroundX2;
	private int bgDX;			// size of the background move (in pixels)


	public Background(JFrame window, String imageFile, int bgDX) {

    	this.bgImage = loadImage(imageFile);
    	bgImageWidth = bgImage.getWidth(window);	// get width of the background
    		this.bgDX = bgDX;
		backgroundX = 0;
		backgroundX2 = bgImageWidth;
		bgX = 0;

  	}


  	public void moveRight() {

		if (bgX == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;
					
		}

		bgX = bgX - 2*bgDX;

		backgroundX = backgroundX - 2*bgDX;
		backgroundX2 = backgroundX2 - 2*bgDX;

		if(Math.abs(bgX/bgImageWidth) > 0){
			int amountOver = Math.abs(bgX%bgImageWidth);
			backgroundX = -amountOver;
			backgroundX2 = bgImageWidth-amountOver;
		}

		// if ((bgX + bgImageWidth) % bgImageWidth == 0) {
		// 	backgroundX = 0;
		// 	backgroundX2 = bgImageWidth;
		// }


  	}

    public void moveRightSlower() {
		if (bgX == 0) {
			backgroundX = 0;
			backgroundX2 = bgImageWidth;
					
		}

		bgX = bgX - bgDX;

		backgroundX = backgroundX - bgDX;
		backgroundX2 = backgroundX2 - bgDX;

		if(Math.abs(bgX/bgImageWidth) > 0){
			int amountOver = Math.abs(bgX%bgImageWidth);
			backgroundX = -amountOver;
			backgroundX2 = bgImageWidth-amountOver;
		}
		
		// if ((bgX + bgImageWidth) % bgImageWidth == 0) {
		// 	backgroundX = 0;
		// 	backgroundX2 = bgImageWidth;
		// }
    }

  	public void draw (Graphics2D g2) {
		g2.drawImage(bgImage, backgroundX, 0, 1920, 1080, null);
		g2.drawImage(bgImage, backgroundX2, 0, 1920, 1080, null);
  	}


  	private Image loadImage (String fileName) {
		return new ImageIcon(fileName).getImage();
  	}


}
