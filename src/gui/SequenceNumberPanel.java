package gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class SequenceNumberPanel extends JPanel {

	private static final long serialVersionUID = -3116183308181942679L;

	public SequenceNumberPanel(int number, String description) {
		super();
		setLayout(new MigLayout("", "[][]"));
		JLabel numberLabel = new JLabel(String.valueOf(number));
		numberLabel.setFont(numberLabel.getFont().deriveFont(80f));
		numberLabel.setForeground(Color.LIGHT_GRAY);
		numberLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		setOpaque(false);
		add(numberLabel);
		JLabel descriptionLabel = new JLabel(String.valueOf(description));
		descriptionLabel.setForeground(Color.LIGHT_GRAY);
		add(descriptionLabel);
	}
}