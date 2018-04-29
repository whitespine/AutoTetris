package autotetris.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

import autotetris.controller.AIConfiguratorController;
import autotetris.controller.ExecutionController;
import autotetris.controller.HumanInputPieceController;
import autotetris.model.Model;
import autotetris.model.TetrisSolver;

public class Application extends JFrame {

	private static final long serialVersionUID = 1341549472797373358L;
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	private JPanel contentPane;

	// Sim state
    private TetrisSolver solver;
    public Model model;

	// UI Garbage
	private GamePanel gamePanel;
	private JLabel lblScore;
	private NextPiecePanel nextPiecePanel;
	private JMenuBar menuBar;
	private JMenuItem mntmPlay;
	private JMenuItem mntmPause;
	private JMenuItem mntmReset;
	private JMenu mnAiOptions;
	private JMenuItem mntmTrain;
	private JMenuItem mntmAIPlay;
	private JMenuItem mntmPreferences;
	private AIConfigurator aic;
	
	/**
	 * Create the frame.
	 */
	public Application(Model m) {
		super("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 252, 449);
		
		model = m;
		solver = new TetrisSolver();
		Application self = this;
		this.addKeyListener(new HumanInputPieceController(model, this));
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnGame = new JMenu("Game");
		menuBar.add(mnGame);
		
		mntmPlay = new JMenuItem("Play");
		mnGame.add(mntmPlay);
		
		mntmPause = new JMenuItem("Pause");
		mnGame.add(mntmPause);
		
		mntmReset = new JMenuItem("Reset");
		mnGame.add(mntmReset);
		
		JSeparator separator = new JSeparator();
		mnGame.add(separator);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnGame.add(mntmQuit);
		
		mnAiOptions = new JMenu("AI Options");
		menuBar.add(mnAiOptions);
		
		mntmTrain = new JMenuItem("Train");
		mntmTrain.addActionListener(e -> {
            // TODO: Maybe actually do something here
        });
		mnAiOptions.add(mntmTrain);

		mnAiOptions.add(new JSeparator());

		mntmAIPlay = new JMenuItem("Play Self");
		mntmAIPlay.addActionListener(e -> {
            solver.execute(this, model);
        });
		mnAiOptions.add(mntmAIPlay);

		mntmAIPlay.add(new JSeparator());
		
		mntmPreferences = new JMenuItem("Preferences...");
		mntmPreferences.addActionListener(arg0 -> {
            new AIConfiguratorController(self).fillConfigurator();
            aic.setVisible(true);
        });
		mnAiOptions.add(mntmPreferences);
		
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
		gbl_infoPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_infoPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_infoPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		gbc_nextPiecePanel.insets = new Insets(5, 5, 5, 0);
		gbc_nextPiecePanel.fill = GridBagConstraints.BOTH;
		gbc_nextPiecePanel.gridx = 0;
		gbc_nextPiecePanel.gridy = 3;
		infoPanel.add(nextPiecePanel, gbc_nextPiecePanel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setEnabled(false);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.weighty = 0.7;
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 10;
		infoPanel.add(verticalStrut, gbc_verticalStrut);
		
		this.setFocusable(true);
		
		aic = new AIConfigurator(this);
		aic.setVisible(false);
	}

	public void showScore(int score) {
		lblScore.setText("" + score);
	}

	public void bindExecutionController(ExecutionController ec) {
		mntmPlay.addActionListener(e -> ec.play());
		mntmPause.addActionListener(e -> ec.pause());
		mntmReset.addActionListener(e -> ec.reset());
	}

	public AIConfigurator getAIConfigurator() {
		return aic;
	}

    public TetrisSolver getSolver() {
        return solver;
    }
}
