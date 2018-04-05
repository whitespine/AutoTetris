package autotetris.view;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.border.TitledBorder;

import autotetris.controller.AIConfiguratorController;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AIConfigurator extends JFrame {

	private static final long serialVersionUID = 5376626948600966282L;
	private JPanel contentPane;
	private JSpinner iterSpinner;
	private JTextField totalHeight_wtfield;
	private JTextField rows_wtfield;
	private JTextField holes_wtfield;
	private JTextField heightvar_wtfield;
	Application parent;
	
	public AIConfigurator(Application application) {
		this.parent = application;
		setTitle("Options");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{424, 0};
		gbl_contentPane.rowHeights = new int[]{81, 81, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Training Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblIterations_Name = new JLabel("Iterations:");
		GridBagConstraints gbc_lblIterations_Name = new GridBagConstraints();
		gbc_lblIterations_Name.insets = new Insets(0, 0, 0, 5);
		gbc_lblIterations_Name.gridx = 0;
		gbc_lblIterations_Name.gridy = 0;
		panel.add(lblIterations_Name, gbc_lblIterations_Name);
		
		iterSpinner = new JSpinner();
		iterSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 10));
		GridBagConstraints gbc_iterSpinner = new GridBagConstraints();
		gbc_iterSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_iterSpinner.gridx = 1;
		gbc_iterSpinner.gridy = 0;
		panel.add(iterSpinner, gbc_iterSpinner);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Evaluation Weights", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblTotalHeight = new JLabel("Total Height:");
		GridBagConstraints gbc_lblTotalHeight = new GridBagConstraints();
		gbc_lblTotalHeight.anchor = GridBagConstraints.EAST;
		gbc_lblTotalHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalHeight.gridx = 0;
		gbc_lblTotalHeight.gridy = 0;
		panel_1.add(lblTotalHeight, gbc_lblTotalHeight);
		
		totalHeight_wtfield = new JTextField();
		GridBagConstraints gbc_totalHeight_wtfield = new GridBagConstraints();
		gbc_totalHeight_wtfield.insets = new Insets(0, 0, 5, 0);
		gbc_totalHeight_wtfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_totalHeight_wtfield.gridx = 1;
		gbc_totalHeight_wtfield.gridy = 0;
		panel_1.add(totalHeight_wtfield, gbc_totalHeight_wtfield);
		totalHeight_wtfield.setColumns(10);
		
		JLabel lblCompleteRows = new JLabel("Complete Rows:");
		GridBagConstraints gbc_lblCompleteRows = new GridBagConstraints();
		gbc_lblCompleteRows.anchor = GridBagConstraints.EAST;
		gbc_lblCompleteRows.insets = new Insets(0, 0, 5, 5);
		gbc_lblCompleteRows.gridx = 0;
		gbc_lblCompleteRows.gridy = 1;
		panel_1.add(lblCompleteRows, gbc_lblCompleteRows);
		
		rows_wtfield = new JTextField();
		GridBagConstraints gbc_rows_wtfield = new GridBagConstraints();
		gbc_rows_wtfield.insets = new Insets(0, 0, 5, 0);
		gbc_rows_wtfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_rows_wtfield.gridx = 1;
		gbc_rows_wtfield.gridy = 1;
		panel_1.add(rows_wtfield, gbc_rows_wtfield);
		rows_wtfield.setColumns(10);
		
		JLabel lblHoles = new JLabel("Holes:");
		GridBagConstraints gbc_lblHoles = new GridBagConstraints();
		gbc_lblHoles.anchor = GridBagConstraints.EAST;
		gbc_lblHoles.insets = new Insets(0, 0, 5, 5);
		gbc_lblHoles.gridx = 0;
		gbc_lblHoles.gridy = 2;
		panel_1.add(lblHoles, gbc_lblHoles);
		
		holes_wtfield = new JTextField();
		GridBagConstraints gbc_holes_wtfield = new GridBagConstraints();
		gbc_holes_wtfield.insets = new Insets(0, 0, 5, 0);
		gbc_holes_wtfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_holes_wtfield.gridx = 1;
		gbc_holes_wtfield.gridy = 2;
		panel_1.add(holes_wtfield, gbc_holes_wtfield);
		holes_wtfield.setColumns(10);
		
		JLabel lblHeightVariance = new JLabel("Height Variance:");
		GridBagConstraints gbc_lblHeightVariance = new GridBagConstraints();
		gbc_lblHeightVariance.anchor = GridBagConstraints.EAST;
		gbc_lblHeightVariance.insets = new Insets(0, 0, 0, 5);
		gbc_lblHeightVariance.gridx = 0;
		gbc_lblHeightVariance.gridy = 3;
		panel_1.add(lblHeightVariance, gbc_lblHeightVariance);
		
		heightvar_wtfield = new JTextField();
		GridBagConstraints gbc_heightvar_wtfield = new GridBagConstraints();
		gbc_heightvar_wtfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_heightvar_wtfield.gridx = 1;
		gbc_heightvar_wtfield.gridy = 3;
		panel_1.add(heightvar_wtfield, gbc_heightvar_wtfield);
		heightvar_wtfield.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (new AIConfiguratorController(parent.model, parent).writeConfiguration(iterSpinner.getValue(), 
						totalHeight_wtfield.getText(), rows_wtfield.getText(), holes_wtfield.getText(), heightvar_wtfield.getText()))
					parent.getAIConfigurator().setVisible(false);
			}
		});
		panel_2.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getAIConfigurator().setVisible(false);
			}
		});
		panel_2.add(btnCancel);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AIConfiguratorController aicc = new AIConfiguratorController(parent.model, parent);
				aicc.writeConfiguration(iterSpinner.getValue(), 
						totalHeight_wtfield.getText(), rows_wtfield.getText(), holes_wtfield.getText(), heightvar_wtfield.getText());
				aicc.fillConfigurator();
			}
		});
		panel_2.add(btnApply);
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

	public JTextField getHoles_wtfield() {
		return holes_wtfield;
	}

	public JTextField getHeightvar_wtfield() {
		return heightvar_wtfield;
	}
}
