import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;
public class ToDoListApp extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField taskField, deadlineField;
    private JComboBox<String> priorityCombo;
    private JButton sortButton;
    private boolean sortAscending = false;
    public ToDoListApp() {
        setTitle("To-Do List");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"Task", "Deadline", "Priority", "Status"}, 0);
        table = new JTable(model);

        taskField = new JTextField(20);
        deadlineField = new JTextField(10);
        priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Task:")); inputPanel.add(taskField);
        inputPanel.add(new JLabel("Deadline:")); inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("Priority:")); inputPanel.add(priorityCombo);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton completeButton = new JButton("Mark Complete");
        sortButton = new JButton("Sort: Low to High");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton); buttonPanel.add(editButton);
        buttonPanel.add(completeButton); buttonPanel.add(sortButton); buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> addTask());
        editButton.addActionListener(e -> editTask());
        completeButton.addActionListener(e -> markComplete());
        sortButton.addActionListener(e -> sortByPriority());
        deleteButton.addActionListener(e -> deleteTask());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void addTask() {
        String task = taskField.getText().trim();
        String deadline = deadlineField.getText().trim();
        if (task.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter required fields");
            return;
        }
        if (!task.isEmpty()) {
            model.addRow(new Object[]{task, deadline, priorityCombo.getSelectedItem(), "Pending"});
            saveTasks();
            taskField.setText("");
            deadlineField.setText("");
        }
    }

    private void editTask() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to edit");
            return;
        }
        taskField.setText(model.getValueAt(row, 0).toString());
        deadlineField.setText(model.getValueAt(row, 1).toString());
        priorityCombo.setSelectedItem(model.getValueAt(row, 2));
        model.removeRow(row);
        saveTasks();
    }

    private void markComplete() {
        int row = table.getSelectedRow();
        if (row != -1) {
            model.setValueAt("✅ Completed", row, 3);
            saveTasks();
        }
    }

    private void sortByPriority() {
        sortAscending = !sortAscending;
        Vector<Vector> data = model.getDataVector();
        data.sort((a, b) -> {
            int v1 = priorityValue(a.get(2).toString());
            int v2 = priorityValue(b.get(2).toString());
            return sortAscending ? Integer.compare(v1, v2) : Integer.compare(v2, v1);
        });
        sortButton.setText(sortAscending ? "🔼 Priority" : "🔽 Priority");
        model.fireTableDataChanged();
    }

    private int priorityValue(String p) {
        return switch (p) {
            case "High" -> 1;
            case "Medium" -> 2;
            default -> 3;
        };
    }

    private void deleteTask() {
        int row = table.getSelectedRow();
        if (row != -1 && JOptionPane.showConfirmDialog(this, "Delete this task?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            saveTasks();
        }
    }

    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter("tasks.txt")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.println(model.getValueAt(i, 0) + "|" + model.getValueAt(i, 1) + "|" +
                               model.getValueAt(i, 2) + "|" + model.getValueAt(i, 3));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks.");
        }
    }

    private void loadTasks() {
        File file = new File("tasks.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    model.addRow(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading tasks.");
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToDoListApp app = new ToDoListApp();
            app.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    app.saveTasks();
                }
            });
            app.loadTasks();
            app.setVisible(true);
        });
    }
}
