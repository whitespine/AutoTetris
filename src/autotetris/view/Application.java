package autotetris.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import autotetris.controller.HumanInputPieceController;
import autotetris.model.Model;

public class Application extends JFrame {

	private static final long serialVersionUID = 1341549472797373358L;
	public static final Color BACKGROUND_COLOR = new Color(8, 8, 8);
	private JPanel contentPane;
	
	private GamePanel gamePanel;
	private JButton btnPlay, btnPause, btnReset;
	private JLabel lblScore;
	private NextPiecePanel nextPiecePanel;
	Model model;
	
	/**
	 * Create the frame.
	 */
	public Application(Model m) {
		super("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 409);
		
		model = m;
		
		this.addKeyListener(new HumanInputPieceController(model, this));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{354, 70, 0};
		gbl_contentPane.rowHeights = new int[]{360, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		gamePanel = new GamePanel(this);
		GridBagConstraints gbc_gamePanel = new GridBagConstraints();
		gbc_gamePanel.weightx = 0.8;
		gbc_gamePanel.fill = GridBagConstraints.BOTH;
		gbc_gamePanel.insets = new Insets(0, 0, 0, 5);
		gbc_gamePanel.gridx = 0;
		gbc_gamePanel.gridy = 0;
		contentPane.add(gamePanel, gbc_gamePanel);
		
		JPanel infoPanel = new JPanel();
		GridBagConstraints gbc_infoPanel = new GridBagConstraints();
		gbc_infoPanel.weightx = 0.2;
		gbc_infoPanel.fill = GridBagConstraints.BOTH;
		gbc_infoPanel.gridx = 1;
		gbc_infoPanel.gridy = 0;
		contentPane.add(infoPanel, gbc_infoPanel);
		GridBagLayout gbl_infoPanel = new GridBagLayout();
		gbl_infoPanel.columnWidths = new int[]{0, 0};
		gbl_infoPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_infoPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_infoPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		infoPanel.setLayout(gbl_infoPanel);
		
		JLabel lblScoreText = new JLabel("Score");
		GridBagConstraints gbc_lblScoreText = new GridBagConstraints();
		gbc_lblScoreText.insets = new Insets(0, 0, 5, 0);
		gbc_lblScoreText.gridx = 0;
		gbc_lblScoreText.gridy = 0;
		infoPanel.add(lblScoreText, gbc_lblScoreText);
		
		lblScore = new JLabel("0");
		GridBagConstraints gbc_lblScore = new GridBagConstraints();
		gbc_lblScore.insets = new Insets(0, 0, 5, 0);
		gbc_lblScore.gridx = 0;
		gbc_lblScore.gridy = 1;
		infoPanel.add(lblScore, gbc_lblScore);
		
		JLabel lblNext_Text = new JLabel("Next");
		GridBagConstraints gbc_lblNext_Text = new GridBagConstraints();
		gbc_lblNext_Text.insets = new Insets(0, 0, 5, 0);
		gbc_lblNext_Text.gridx = 0;
		gbc_lblNext_Text.gridy = 2;
		infoPanel.add(lblNext_Text, gbc_lblNext_Text);
		
		nextPiecePanel = new NextPiecePanel(this);
		GridBagConstraints gbc_nextPiecePanel = new GridBagConstraints();
		gbc_nextPiecePanel.weighty = 0.7;
		gbc_nextPiecePanel.insets = new Insets(5, 5, 5, 5);
		gbc_nextPiecePanel.fill = GridBagConstraints.BOTH;
		gbc_nextPiecePanel.gridx = 0;
		gbc_nextPiecePanel.gridy = 3;
		infoPanel.add(nextPiecePanel, gbc_nextPiecePanel);
		
		btnPlay = new JButton("Play");
		GridBagConstraints gbc_btnPlay = new GridBagConstraints();
		gbc_btnPlay.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPlay.insets = new Insets(0, 0, 5, 0);
		gbc_btnPlay.gridx = 0;
		gbc_btnPlay.gridy = 4;
		infoPanel.add(btnPlay, gbc_btnPlay);
		
		btnPause = new JButton("Pause");
		GridBagConstraints gbc_btnPause = new GridBagConstraints();
		gbc_btnPause.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPause.insets = new Insets(0, 0, 5, 0);
		gbc_btnPause.gridx = 0;
		gbc_btnPause.gridy = 5;
		infoPanel.add(btnPause, gbc_btnPause);
		
		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnReset.gridx = 0;
		gbc_btnReset.gridy = 6;
		infoPanel.add(btnReset, gbc_btnReset);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setEnabled(false);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.weighty = 0.7;
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 7;
		infoPanel.add(verticalStrut, gbc_verticalStrut);
		
		this.setFocusable(true);
	}

	public void showScore(int score) {
		lblScore.setText("" + score);
	}

}
