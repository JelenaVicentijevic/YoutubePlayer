package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import object.Youtube;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import javax.swing.JTextArea;
import java.awt.ComponentOrientation;
import javax.swing.SwingConstants;



public class Player {

	public JFrame frmYoutubePlayer;
	public static JTextArea txtrTest;
	public static volatile boolean push;
	
	/**
	 * Create the application.
	 */
	public Player() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		// create frame
		frmYoutubePlayer = new JFrame();
		frmYoutubePlayer.setAlwaysOnTop(true);
		frmYoutubePlayer.getContentPane().setBackground(Color.DARK_GRAY);
		frmYoutubePlayer.setTitle("Youtube Player");
		frmYoutubePlayer.setName("Youtube");
		frmYoutubePlayer.getContentPane().setName("Youtube Player");
		frmYoutubePlayer.setBounds(100, 100, 358, 115);
		frmYoutubePlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmYoutubePlayer.getContentPane().setLayout(null);
		
		try {
			frmYoutubePlayer.setIconImage(ImageIO.read(new File("icon/y.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		// add window listener
		frmYoutubePlayer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				push = false;
				try {
					Youtube.driver.quit();
				} catch (NullPointerException en) {
					System.exit(0);
				}

			}
		});

		// create button
		JButton btnNewButton_2 = new JButton(new ImageIcon(Player.class.getResource("/resources/p.png")));
		btnNewButton_2.setBounds(10, 11, 61, 54);
		frmYoutubePlayer.getContentPane().add(btnNewButton_2);

		// add button listener
		btnNewButton_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// if (e.getSource() == btnNewButton_2) {
				push = true;
				// }
			}
		});

		// create label
		JLabel lblNewLabel = new JLabel("Youtube Song");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setVerticalTextPosition(SwingConstants.TOP);
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Segoe Print", Font.PLAIN, 11));
		lblNewLabel.setBounds(250, 0, 82, 24);
		frmYoutubePlayer.getContentPane().add(lblNewLabel);

		// create text area
		txtrTest = new JTextArea();
		txtrTest.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		txtrTest.setBackground(Color.DARK_GRAY);
		txtrTest.setForeground(Color.WHITE);
		txtrTest.setWrapStyleWord(true);
		txtrTest.setLineWrap(true);
		txtrTest.setFont(new Font("Segoe Print", Font.PLAIN, 13));
		txtrTest.setText("None video playing");
		txtrTest.setBounds(81, 22, 261, 54);
		frmYoutubePlayer.getContentPane().add(txtrTest);

	}

	/**
	 * Launch the application.
	 * 
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					Player window = new Player();
					window.frmYoutubePlayer.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
		while (push == false) {
			Thread.sleep(1000);
		}
		;
		Youtube.play(txtrTest);

	}
}
