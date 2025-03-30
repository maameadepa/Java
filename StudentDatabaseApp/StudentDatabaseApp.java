import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class StudentDatabaseApp extends JFrame {
    private static final String FILE_PATH = "students.txt";
    
    private JTextField idField, nameField;
    private JTextArea outputArea;
    
    public StudentDatabaseApp() {
        setTitle("Student Database Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        
        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        
        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> insertStudent());
        inputPanel.add(addButton);
        
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> deleteStudent());
        inputPanel.add(deleteButton);
        
        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    
     private void insertStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(id + "," + name);
                writer.newLine();
                outputArea.append("✓ Student " + name + " (ID: " + id + ") added successfully\n");
            }
        } catch (NumberFormatException e) {
            outputArea.append("✗ Invalid ID format. Please enter a number.\n");
        } catch (IOException e) {
            outputArea.append("✗ File error: " + e.getMessage() + "\n");
        }
    }
    
    private void deleteStudent() {
        try {
            int idToDelete = Integer.parseInt(idField.getText());
            List<String> remainingStudents = new ArrayList<>();
            boolean found = false;
            
            // Read all students and keep those that don't match the ID
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int currentId = Integer.parseInt(parts[0]);
                    if (currentId != idToDelete) {
                        remainingStudents.add(line);
                    } else {
                        found = true;
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (String student : remainingStudents) {
                    writer.write(student);
                    writer.newLine();
                }
            }
            
            if (found) {
                outputArea.append("✓ Student with ID " + idToDelete + " deleted successfully\n");
            } else {
                outputArea.append("✗ No student found with ID " + idToDelete + "\n");
            }
            
        } catch (NumberFormatException e) {
            outputArea.append("✗ Invalid ID format. Please enter a number.\n");
        } catch (IOException e) {
            outputArea.append("✗ File error: " + e.getMessage() + "\n");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentDatabaseApp();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}