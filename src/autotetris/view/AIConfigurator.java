package autotetris.view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.border.TitledBorder;

import autotetris.controller.AIConfiguratorController;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;

public class AIConfigurator extends JFrame {

	private static final long serialVersionUID = 5376626948600966282L;
	private JPanel contentPane;
	private JSpinner iterSpinner;
	private JTextField totalHeight_wtfield;
	private JTextField rows_wtfield;
	private JTextField holes_wtfield;
	private JTextField heightDev_wtfield;
	private JTextField maxHeight_wtfield;
	Application parent;
	
	public AIConfigurator(Application application) {
		this.parent = application;
		setTitle("Options");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{424, 0};
		gbl_contentPane.rowHeights = new int[]{81, 81, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new TitledBorder(null, "Training Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(topPanel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		topPanel.setLayout(gbl_panel);
		
		JLabel lblIterations_Name = new JLabel("Iterations:");
		GridBagConstraints gbc_lblIterations_Name = new GridBagConstraints();
		gbc_lblIterations_Name.insets = new Insets(0, 0, 0, 5);
		gbc_lblIterations_Name.gridx = 0;
		gbc_lblIterations_Name.gridy = 0;
		topPanel.add(lblIterations_Name, gbc_lblIterations_Name);
		
		iterSpinner = new JSpinner();
		iterSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 10));
		GridBagConstraints gbc_iterSpinner = new GridBagConstraints();
		gbc_iterSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_iterSpinner.gridx = 1;
		gbc_iterSpinner.gridy = 0;
		topPanel.add(iterSpinner, gbc_iterSpinner);
		
		JPanel weightsPanel = new JPanel();
		weightsPanel.setBorder(new TitledBorder(null, "Evaluation Weights", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(weightsPanel, gbc_panel_1);
		weightsPanel.setLayout(new GridLayout(0,2));

		//////////////////////////////////////////////////////////////

		JLabel lblTotalHeight = new JLabel("Total Height:");
		weightsPanel.add(lblTotalHeight);

		totalHeight_wtfield = new JTextField();
		weightsPanel.add(totalHeight_wtfield);

		//////////////////////////////////////////////////////////////
		
		JLabel lblCompleteRows = new JLabel("Complete Rows:");
		weightsPanel.add(lblCompleteRows);
		
		rows_wtfield = new JTextField();
		weightsPanel.add(rows_wtfield);

		//////////////////////////////////////////////////////////////
		
		JLabel lblHoles = new JLabel("Holes:");
		weightsPanel.add(lblHoles);
		
		holes_wtfield = new JTextField();
		weightsPanel.add(holes_wtfield);

		//////////////////////////////////////////////////////////////

		JLabel lblHeightVariance = new JLabel("Height Variance (Squared):");
		weightsPanel.add(lblHeightVariance);

		heightDev_wtfield = new JTextField();
		weightsPanel.add(heightDev_wtfield);

		//////////////////////////////////////////////////////////////

		JLabel lblMaxHeight = new JLabel("Max Height (squared)");
		weightsPanel.add(lblMaxHeight);

		maxHeight_wtfield = new JTextField();
		weightsPanel.add(maxHeight_wtfield);

		//////////////////////////////////////////////////////////////

		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(buttonPanel, gbc_panel_2);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(e -> {
            if (new AIConfiguratorController(parent).writeConfiguration(
                    iterSpinner.getValue(),
                    totalHeight_wtfield.getText(),
                    rows_wtfield.getText(),
                    holes_wtfield.getText(),
                    heightDev_wtfield.getText(),
                    maxHeight_wtfield.getText()))
                parent.getAIConfigurator().setVisible(false);
        });
		buttonPanel.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> parent.getAIConfigurator().setVisible(false));
		buttonPanel.add(btnCancel);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(e -> {
            AIConfiguratorController aicc = new AIConfiguratorController(parent);
            aicc.writeConfiguration(iterSpinner.getValue(),
                    totalHeight_wtfield.getText(),
                    rows_wtfield.getText(),
                    holes_wtfield.getText(),
                    heightDev_wtfield.getText(),
                    maxHeight_wtfield.getText());
            aicc.fillConfigurator();
        });
		buttonPanel.add(btnApply);
	}

	public JSpinner getIterSpinner() {
		return iterSpinner;
	}

	public JTextField getTotalHeight_wtfield() {
		return totalHeight_wtfield;
	}

	public JTextField getRows_wtfield() {
		return rows_wtfield;
	}

	public JTextField getMaxheight_wtfield() {
		return maxHeight_wtfield;
	}

	public JTextField getHoles_wtfield() {
		return holes_wtfield;
	}

	public JTextField getHeightDev_wtfield() {
		return heightDev_wtfield;
	}
}
