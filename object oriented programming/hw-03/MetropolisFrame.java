import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MetropolisFrame extends JFrame {
    private JTextField metropolisField;
    private JTextField continentField;
    private JTextField populationField;
    private JTable dataTable;
    private JButton addButton;
    private JButton searchButton;
    private JComboBox<String> populationCombo;
    private JComboBox<String> exactCombo;
    private MetropolisTableModel model;

    public MetropolisFrame() {
        super("Metropolis Applet");
        setLayout(new BorderLayout(4,4));
        buildGUI();
        addActionListeners();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addActionListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String metropolis = metropolisField.getText();
                String continent = continentField.getText();
                String population = populationField.getText();

                try {
                    model.add(metropolis, continent, population);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String metropolis = metropolisField.getText();
                String continent = continentField.getText();
                String population = populationField.getText();
                boolean popLarger = populationCombo.getSelectedItem().equals("Population Larger Than");
                boolean exact = exactCombo.getSelectedItem().equals("Exact Match");

                try {
                    model.search(metropolis, continent, population, popLarger, exact);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void buildGUI() {
        initCriterionRegion();
        initTable();
        initInteractors();
    }

    private void initInteractors() {
        Box interactorsPanel = Box.createVerticalBox();
        Box buttonsPanel = Box.createVerticalBox();
        Box comboPanel = Box.createVerticalBox();
        comboPanel.setBorder(new TitledBorder("Search Options"));

        // Buttons
        addButton = new JButton("Add");
        searchButton = new JButton("Search");
        addButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        searchButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        Dimension dim = new Dimension(100, 30);
        addButton.setMaximumSize(dim);
        searchButton.setMaximumSize(dim);
        buttonsPanel.add(addButton);
        buttonsPanel.add(searchButton);

        //ComboBoxes
        populationCombo = new JComboBox<>();
        populationCombo.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        populationCombo.addItem("Population Larger Than");
        populationCombo.addItem("Population Less Than");
        Dimension size = new Dimension(populationCombo.getMinimumSize());
        populationCombo.setMaximumSize(size);
        exactCombo = new JComboBox<>();
        exactCombo.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        exactCombo.addItem("Exact Match");
        exactCombo.addItem("Similar Match");
        exactCombo.setMaximumSize(size);
        comboPanel.add(populationCombo);
        comboPanel.add(exactCombo);

        interactorsPanel.add(buttonsPanel);
        interactorsPanel.add(comboPanel);
        add(interactorsPanel);
    }

    private void initTable() {
        model = new MetropolisTableModel();
        dataTable = new JTable(model);
        dataTable.setRowHeight(20);
        JScrollPane pane = new JScrollPane(dataTable);

        add(pane, BorderLayout.WEST);
    }
    private void initCriterionRegion() {
        JPanel criterionPanel = new JPanel();
        JLabel metropolisLabel = new JLabel("Metropolis:");
        criterionPanel.add(metropolisLabel);
        metropolisField = new JTextField(10);
        criterionPanel.add(metropolisField);

        JLabel continentLabel = new JLabel("Continent:");
        criterionPanel.add(continentLabel);
        continentField = new JTextField(10);
        criterionPanel.add(continentField);

        JLabel populationLabel = new JLabel("Population:");
        criterionPanel.add(populationLabel);
        populationField = new JTextField(10);
        criterionPanel.add(populationField);

        add(criterionPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        MetropolisFrame frame = new MetropolisFrame();
    }
}
