package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import object.Youtube;
import util.ExcelUtils;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import java.awt.Font;

import java.awt.event.WindowAdapter;
import javax.swing.JTextArea;
import java.awt.ComponentOrientation;
import javax.swing.SwingConstants;

public class Player {

	JFrame frmYoutubePlayer;
	private static volatile boolean push;
	private static WebDriver driver;
	private static JTextArea txtrTest;
	static Object o = new Object();

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
		play();

		/*
		 * // elsewhere, when waiting for playback to complete: synchronized (o) { while
		 * (push==false) { try { o.wait(); } catch (InterruptedException x) { // abort
		 * or ignore, up to you } } } synchronized (o) { push = true; o.notifyAll(); }
		 */
	}

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
		frmYoutubePlayer = new JFrame();
		
		frmYoutubePlayer.setAlwaysOnTop(true);
		frmYoutubePlayer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				push = false;
				try {
					driver.quit();
				} catch (NullPointerException en) {
					System.exit(0);
				}

			}
		});
		frmYoutubePlayer.getContentPane().setBackground(Color.DARK_GRAY);
		frmYoutubePlayer.setTitle("Youtube Player");
		frmYoutubePlayer.setName("Youtube");
		frmYoutubePlayer.getContentPane().setName("Youtube Player");
		frmYoutubePlayer.setBounds(100, 100, 358, 115);
		frmYoutubePlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmYoutubePlayer.getContentPane().setLayout(null);
		

		JButton btnNewButton_2 = new JButton(new ImageIcon(Player.class.getResource("/resources/p.png")));

		btnNewButton_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// if (e.getSource() == btnNewButton_2) {
				push = true;
				// }
			}
		});

		btnNewButton_2.setBounds(10, 11, 61, 54);
		frmYoutubePlayer.getContentPane().add(btnNewButton_2);

		JLabel lblNewLabel = new JLabel("Youtube Song");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setVerticalTextPosition(SwingConstants.TOP);
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Segoe Print", Font.PLAIN, 11));
		lblNewLabel.setBounds(250, 0, 82, 24);
		frmYoutubePlayer.getContentPane().add(lblNewLabel);

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

	public static void play() {
		txtrTest.setText("Playlist loading...");
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();

		driver.get(Youtube.URL);
		driver.manage().window().maximize();

		ExcelUtils.setExcell("SongLinks.xlsx");
		ExcelUtils.setWorkSheet(0);
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		for (int i = 1; i < ExcelUtils.getRowNumber(); i++) {
			try {
				driver.navigate().to(ExcelUtils.getDataAt(i, 0));
				String title = Youtube.songTitle(driver);
				txtrTest.setText(title);
				Youtube.skip(driver);
				System.out.println(title);
				Youtube.duration(driver);
			} catch (InvalidArgumentException e) {
				txtrTest.setText("No more songs in playlist");
				push = false;
				driver.quit();
			} catch (NoSuchSessionException e) {
				txtrTest.setText("No more songs in playlist");
				push = false;
				driver.close();
			} catch (NullPointerException e) {
				txtrTest.setText("No more songs in playlist");
				push = false;
				driver.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		push = false;
		driver.quit();
	}

}
