// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {
	private final JTextField textField;
	private final JLabel label;
	private WorkerThread worker;
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// YOUR CODE HERE
		textField = new JTextField("1000000", 10);
		label = new JLabel("0");
		JButton startButton = new JButton("Start");
		JButton stopButton = new JButton("Stop");

		// Action listeners
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (worker != null) worker.interrupt();
				worker = new WorkerThread(Integer.parseInt(textField.getText()));
				worker.start();
			}
		});

		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (worker != null) {
					try {
						worker.interrupt();
						worker.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				worker = null;
			}
		});

		// Add elements to canvas
		add(textField);
		add(label);
		add(startButton);
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0,40)));
	}

	private class WorkerThread extends Thread {
		private static final int INTERVAL = 10000;
		private static final int SLEEP_INTERVAL = 100; // milliseconds
		private final int UpperBound;
		public WorkerThread(int upperBound) {
			this.UpperBound = upperBound;
		}
		@Override
		public void run() {
			int i = 0;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					label.setText("0");
				}
			});
			while (i < UpperBound) {
				i++;
				if (i % INTERVAL == 0) {
					try {
						sleep(SLEEP_INTERVAL);
					} catch (InterruptedException e) {
						return; // sleep was interrupted by main thread
					}

					String finalI = String.valueOf(i);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							label.setText(finalI);
						}
					});

					if (this.isInterrupted()) break; // thread was interrupted during code executing (not sleep())
				}
			}
		}
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}

