import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataStreamsFrame extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchTextField;
    private JButton loadButton, searchButton, quitButton;

    public DataStreamsFrame() {
        initializeComponents();
        addEventListeners();
    }

    private void initializeComponents() {
        setTitle("DataStreams GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        searchTextField = new JTextField();
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");

        searchTextField.setPreferredSize(new Dimension(150, searchTextField.getPreferredSize().height));

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search String: "));
        topPanel.add(searchTextField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);

        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        add(topPanel, BorderLayout.NORTH);
        add(originalScrollPane, BorderLayout.WEST);
        add(filteredScrollPane, BorderLayout.EAST);
    }

    private void addEventListeners() {
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFile();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path filePath = fileChooser.getSelectedFile().toPath();
                String content = new String(Files.readAllBytes(filePath));
                originalTextArea.setText(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        try {
            String searchString = searchTextField.getText();
            if (searchString.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search string", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String originalText = originalTextArea.getText();
            Stream<String> lines = originalText.lines();

            String filteredText = lines.filter(line -> line.contains(searchString))
                    .reduce((line1, line2) -> line1 + "\n" + line2)
                    .orElse("");

            filteredTextArea.setText(filteredText);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

