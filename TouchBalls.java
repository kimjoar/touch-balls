package spillprosjekt;

import java.awt.*;
import javax.swing.*;

public class TouchBalls extends JFrame 
{	
	private static final long serialVersionUID = 1L;
	
	private static DisplayMode originalDM; //The original display mode
	private static DisplayMode runDM; //The display mode the app will be running in
	private static GraphicsDevice device; //The local screen device
	
	// Alternativer
	private boolean soundOn;
	private boolean musicOn;
	private boolean fxOn;
	
	private boolean isFullscreen = false;
	private int poeng = -1;
	
	public int getPoeng() 
	{
		return poeng;
	}

	public void setPoeng(int poeng) 
	{
		this.poeng = poeng;
	}

	public static void main(String[] args) 
	{
		new TouchBalls(false,true,true,true);
	}
	
	public TouchBalls (Boolean fullscreen, Boolean soundOn, Boolean musicOn, Boolean fxOn) 
	{	
		//Setter instillinger:
		this.soundOn = soundOn;
		this.musicOn = musicOn;
		this.fxOn = fxOn;
		
		//Viser LOADING-SCREEN:
		JFrame loadFrame = new JFrame("Laster...");
		loadFrame.setResizable(false);
		loadFrame.setSize(new Dimension(400,100));
		loadFrame.setLocationRelativeTo(null);
		loadFrame.setUndecorated(true);
		loadFrame.getContentPane().add(new LoadPanel());
		loadFrame.setVisible(true);
		
		// Laste bilder, lyd og musikk:
		Bilder.getInstance();
		Lyder.getInstance();
		Musikk.getInstance();
		
		setTitle("Touch Balls");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setSize(new Dimension(800,600));
		setLocationRelativeTo(null);
		gotoMeny();
		
		findDisplayModes();
		
		loadFrame.setVisible(false);
		
		if (fullscreen) {
			//Setter vinduet til fullskjerm, hvis dette er valgt:
			setUndecorated(true);
			device.setFullScreenWindow(this);
			device.setDisplayMode(runDM);
			isFullscreen = true;
		}
		
		setVisible(true);
	}
	
	public void gotoMeny() 
	{
		//Går fra spillet til menyen:
		getContentPane().removeAll();
		MenyPanel menyPanel = null;
		
		if (poeng == -1 || poeng == 0) {
			//Hvis man ikke har kommet på highscore
			menyPanel = new MenyPanel(this);
		} else {
			//Hvis man har kommet på highscore, skal man skrive inn navnet sitt
			menyPanel = new MenyPanel(this, MenyPanel.MENY_SKRIVNAVN);
		}
		
		addMouseMotionListener(menyPanel);
		addMouseListener(menyPanel);
		getContentPane().add(menyPanel);
		showPanel();
	}
	
	public void gotoSpill() 
	{
		//Bytter ut meny-panelet med spill-panelet og starter spillet.
		getContentPane().removeAll();

		SpillMotor panel = new SpillMotor(this);

		new Thread(panel).start();
		addMouseMotionListener(panel);
		getContentPane().add(panel);

		showPanel();
	}
	
	private void showPanel() 
	{
		//-------------------------------------------------------------------
		//Kode hentet fra http://forum.java.sun.com/thread.jspa?forumID=257&threadID=161435
		//Sørger for at det nye panelet blir tegnet
		getContentPane().invalidate();
		getContentPane().validate();
		getContentPane().update(this.getContentPane().getGraphics());
		//-------------------------------------------------------------------
	}
	
	public void setFullscreen() 
	{
		// Kjører programmet i fullskjerm
		setVisible(false);
		
		new TouchBalls(true, soundOn, musicOn, fxOn);
		
		isFullscreen = true;
	}
	
	public void setWindowed() 
	{
		//Kjører programmet i vindu istedet for i fullskjerm:
		if (isFullscreen) {
			setVisible(false);
			device.setDisplayMode(originalDM);
			new TouchBalls(false,soundOn,musicOn,fxOn);
			isFullscreen = false;
		}
	}
	
	private void findDisplayModes() 
	{
		//-----------------------------------------------------------------------
    // Gets a displaymode with 800x600, and the highest bit depth possible.
		//-----------------------------------------------------------------------
    device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    originalDM = device.getDisplayMode();
    DisplayMode[] displayModes = device.getDisplayModes();
    runDM = null;

    for (int i=0; i < displayModes.length; i++) {
    	if (displayModes[i].getWidth() == 800 &&  displayModes[i].getHeight() == 600) {
    		if (runDM == null || displayModes[i].getBitDepth() > runDM.getBitDepth())
    			runDM = displayModes[i];
    	}
    }
    if (runDM == null) ;//What to do if 800x600 is not available
    //-----------------------------------------------------------------------
	}
	
	//LOADINGSCREEN-panel
	private class LoadPanel extends JPanel 
	{
		private static final long serialVersionUID = 1L;
		Image bilde;
		
		public LoadPanel () {
			ImageIcon ic = new ImageIcon(getClass().getResource("loadingscreen.jpg"));
			bilde = ic.getImage();
		}
		
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(bilde, 0, 0, null);
		}
	}
	
	public boolean isFullscreen() 
	{
		return isFullscreen;
	}
	
	public static DisplayMode getOriginalDM() 
	{
		return originalDM;
	}
	
	public static DisplayMode getRunDM() 
	{
		return runDM;
	}

	public static GraphicsDevice getDevice() 
	{
		return device;
	}

	public boolean isFxOn() 
	{
		return fxOn;
	}

	public void setFxOn(boolean fxOn) 
	{
		this.fxOn = fxOn;
	}

	public boolean isMusicOn() 
	{
		return musicOn;
	}

	public void setMusicOn(boolean musicOn) 
	{
		this.musicOn = musicOn;
	}

	public boolean isSoundOn() 
	{
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) 
	{
		this.soundOn = soundOn;
	}
}