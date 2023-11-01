import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class WebFrame extends JPanel {
    private static final String urlFile = "links3.txt";
    private final DefaultTableModel model;
    private final JTable table;
    private Launcher workerLauncher;
    private final JButton singleFetch;
    private final JButton concurrentFetch;
    private final JButton stopFetch;
    private final JLabel runningLabel;
    private final JLabel completedLabel;
    private final AtomicInteger workingThreads;
    private final AtomicInteger completedThreads;
    private final JLabel elapsedLabel;
    private Semaphore activeWorkers;
    private final WebFrame frame;
    private final JProgressBar progressBar;
    private long startTime;

    public WebFrame() {
        frame = this;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        model = new DefaultTableModel(new String[] { "url", "status"}, 0);
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600,300));
        populateTable();

        // Constructors
        workingThreads = new AtomicInteger(0);
        completedThreads = new AtomicInteger(0);

        singleFetch = new JButton("Single Thread Fetch");
        concurrentFetch = new JButton("Concurrent Fetch");
        JTextField numThreadsField = new JTextField("1");
        numThreadsField.setMaximumSize(new Dimension(80, 25));
        runningLabel = new JLabel("Running:0");
        completedLabel = new JLabel("Completed:0");
        elapsedLabel = new JLabel("Elapsed:0ms");
        progressBar = new JProgressBar(0, model.getRowCount());
        stopFetch = new JButton("Stop");
        stopFetch.setEnabled(false);

        // Action listeners
        singleFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                workerLauncher = new Launcher(1);
                workerLauncher.start();
            }
        });
        concurrentFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // IMPLEMENT
                workerLauncher = new Launcher(Integer.parseInt(numThreadsField.getText()));
                workerLauncher.start();
            }
        });
        stopFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    workerLauncher.interrupt();
                    workerLauncher.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        // Finally, add all the elements to the canvas
        add(scrollPane);
        add(singleFetch);
        add(concurrentFetch);
        add(numThreadsField);
        add(runningLabel);
        add(completedLabel);
        add(elapsedLabel);
        add(progressBar);
        add(stopFetch);
    }

    public void workerFinished(String dataReceived, int rowId) {
        model.setValueAt(dataReceived, rowId, 1);
        runningLabel.setText("Running:" + workingThreads.decrementAndGet());
        completedLabel.setText("Completed:" + completedThreads.incrementAndGet());
        activeWorkers.release();
        progressBar.setValue(completedThreads.get());
        if (completedThreads.get() >= model.getRowCount()) {
            switchGuiState(true);
        }
    }
    // True = can search // False = can stop
    private void switchGuiState(boolean b) {
        singleFetch.setEnabled(b);
        concurrentFetch.setEnabled(b);
        stopFetch.setEnabled(!b);
        if (!b) startTime = System.currentTimeMillis();
        else { elapsedLabel.setText("Elapsed:" + (System.currentTimeMillis() - startTime) + "ms"); }
    }
    private class Launcher extends Thread {
        private final int workersNum;
        List<WebWorker> workers;
        public Launcher(int workers) {
            this.workersNum = workers;
            activeWorkers = new Semaphore(workersNum, false);
        }
        @Override
        public void run() {
            // IMPLEMENT
            progressBar.setValue(0);
            elapsedLabel.setText("Elapsed:0ms");
            runningLabel.setText("Running:" + workingThreads.incrementAndGet());
            completedThreads.set(0);
            completedLabel.setText("Completed:" + completedThreads.get());
            switchGuiState(false);

            workers = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                try {
                    startWorker(i);
                } catch (InterruptedException e) {
                    // IMPLEMENT STOP BUTTON LOGIC
                    stopWorkers();
                    switchGuiState(true);
                    runningLabel.setText("Running:" + workingThreads.decrementAndGet());
                    return;
                }
            }

            if (completedThreads.get() == model.getRowCount()) {
                switchGuiState(true);
            }
            runningLabel.setText("Running:" + workingThreads.decrementAndGet());
        }
        private void startWorker(int rowId) throws InterruptedException {
            activeWorkers.acquire();
            runningLabel.setText("Running:" + workingThreads.incrementAndGet());
            WebWorker worker = new WebWorker(model.getValueAt(rowId,0).toString(), rowId, frame);
            workers.add(worker);
            worker.start();
        }
        private void stopWorkers() {
            for (WebWorker worker : workers) {
                worker.interrupt();
            }
        }
    }
    private void populateTable() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(urlFile));
            String line;
            while((line = reader.readLine()) != null) {
                model.addRow(new String[] {line, ""});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void buildGUI() {
        JFrame frame = new JFrame("Web Frame");
        frame.add(new WebFrame());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                buildGUI();
            }
        });
    }
}
