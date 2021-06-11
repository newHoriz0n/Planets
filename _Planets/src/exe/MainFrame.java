package exe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import game.Game;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame() {

		Game g = new Game(500, 500, 3);

		MainView mv = new MainView(g);

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		getContentPane().add(mv, gbc);

		setVisible(true);

		pack();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				mv.updateUI();
				g.update();
			}
		};
		t.schedule(tt, 0, 10);
	}
	

	public static void main(String[] args) {
		MainFrame mf = new MainFrame();
		mf.requestFocus();
	}
	

}
