import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class PayrollApp extends JFrame {

    private JTextField idField, nameField, salaryField, totalDaysField, presentDaysField;
    private DefaultTableModel tableModel;
    private JTable table;

    public PayrollApp() {

        // ===== WINDOW SETTINGS =====
        setTitle("Employee Payroll System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Global font
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 15));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 15));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 15));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.rowHeight", 28);

        // ===== TABBED PANE WITH MODERN UI =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabs.setBackground(Color.WHITE);

        tabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                                              int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? new Color(66, 133, 244) : new Color(230, 230, 230));
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement,
                                          int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
        });

        // ===========================
        // EMPLOYEE REGISTRATION TAB
        // ===========================
        JPanel regPanel = createCardPanel();
        regPanel.setLayout(new GridLayout(7, 2, 12, 12));

        idField = createField();
        nameField = createField();
        salaryField = createField();
        totalDaysField = createField();
        presentDaysField = createField();

        JButton addBtn = createButton("Add Employee");
        JButton editBtn = createButton("Update Employee");
        JButton clearBtn = createButton("Clear Fields");

        regPanel.add(new JLabel("Employee ID:")); regPanel.add(idField);
        regPanel.add(new JLabel("Name:")); regPanel.add(nameField);
        regPanel.add(new JLabel("Basic Salary:")); regPanel.add(salaryField);
        regPanel.add(new JLabel("Total Days:")); regPanel.add(totalDaysField);
        regPanel.add(new JLabel("Present Days:")); regPanel.add(presentDaysField);
        regPanel.add(clearBtn); regPanel.add(addBtn);
        regPanel.add(new JLabel()); regPanel.add(editBtn);

        addBtn.addActionListener(e -> addEmployee());
        clearBtn.addActionListener(e -> clearForm());
        editBtn.addActionListener(e -> updateSelectedEmployee());

        tabs.add("Employee Registration", wrap(regPanel));

        // ===========================
        // EMPLOYEE LIST TAB
        // ===========================
        JPanel listPanel = createCardPanel();
        listPanel.setLayout(new BorderLayout(12,12));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Basic", "HRA", "DA", "Tax", "Net Salary", "Total Days", "Present Days"}, 0) {
            // make table cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        table.setSelectionBackground(new Color(220, 235, 255));
        table.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        // Panel top buttons for list
        JPanel topBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        topBtns.setOpaque(false);
        JButton refreshBtn = createButton("Refresh");
        JButton deleteBtn = createButton("Delete Selected");
        JButton exportBtn = createButton("Export CSV");

        topBtns.add(exportBtn);
        topBtns.add(refreshBtn);
        topBtns.add(deleteBtn);

        refreshBtn.addActionListener(e -> refreshTable());
        deleteBtn.addActionListener(e -> deleteSelectedEmployee());
        exportBtn.addActionListener(e -> exportCSV());

        // when user selects a row, populate the form for easy edit
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                idField.setText(tableModel.getValueAt(r, 0).toString());
                nameField.setText(tableModel.getValueAt(r, 1).toString());
                salaryField.setText(tableModel.getValueAt(r, 2).toString());
                totalDaysField.setText(tableModel.getValueAt(r, 7).toString());
                presentDaysField.setText(tableModel.getValueAt(r, 8).toString());
            }
        });

        loadAllEmployees();
        listPanel.add(topBtns, BorderLayout.NORTH);
        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        tabs.add("Employee List", listPanel);

        // ===========================
        // SEARCH TAB
        // ===========================
        JPanel searchPanel = createCardPanel();
        searchPanel.setLayout(new GridLayout(3, 2, 12, 12));

        JTextField searchField = createField();
        JButton searchBtn = createButton("Search");
        JTextArea searchResult = createTextArea();

        searchPanel.add(new JLabel("Enter Employee ID or Name:")); searchPanel.add(searchField);
        searchPanel.add(searchBtn); searchPanel.add(new JLabel());
        searchPanel.add(new JLabel("Result:")); searchPanel.add(new JScrollPane(searchResult));

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            List<Employee> list = FileStorage.loadEmployees();
            StringBuilder result = new StringBuilder();

            for (Employee emp : list) {
                if (emp.getEmpId().toLowerCase().equals(query)
                        || emp.getName().toLowerCase().contains(query)) {

                    result.append("ID: ").append(emp.getEmpId())
                            .append("\nName: ").append(emp.getName())
                            .append("\nNet Salary: ").append(String.format("%.2f", emp.getNetSalary()))
                            .append("\nAttendance: ").append(emp.getPresentDays())
                            .append("/").append(emp.getTotalDays())
                            .append("\n-------------------\n");
                }
            }

            if (result.length() == 0) result.append("No employee found.");
            searchResult.setText(result.toString());
        });

        tabs.add("Search Employee", wrap(searchPanel));

        // ===========================
        // PAYSLIP TAB
        // ===========================
        JPanel payslipPanel = createCardPanel();
        payslipPanel.setLayout(new GridLayout(3, 2, 12, 12));

        JTextField payslipId = createField();
        JButton generateBtn = createButton("Generate Payslip");
        JTextArea payslipArea = createTextArea();

        payslipPanel.add(new JLabel("Enter Employee ID:"));
        payslipPanel.add(payslipId);
        payslipPanel.add(generateBtn);
        payslipPanel.add(new JLabel());
        payslipPanel.add(new JLabel("Payslip:"));
        payslipPanel.add(new JScrollPane(payslipArea));

        generateBtn.addActionListener(e -> {
            String id = payslipId.getText().trim();
            List<Employee> list = FileStorage.loadEmployees();

            for (Employee emp : list) {
                if (emp.getEmpId().equalsIgnoreCase(id)) {
                    String slip = "------ PAYSLIP ------\n"
                            + "Employee ID: " + emp.getEmpId() + "\n"
                            + "Name: " + emp.getName() + "\n"
                            + "Basic Salary: " + String.format("%.2f", emp.getBasicSalary()) + "\n"
                            + "HRA: " + String.format("%.2f", emp.getHra()) + "\n"
                            + "DA: " + String.format("%.2f", emp.getDa()) + "\n"
                            + "Tax: " + String.format("%.2f", emp.getTax()) + "\n"
                            + "Attendance: " + emp.getPresentDays() + "/" + emp.getTotalDays() + "\n"
                            + "Net Salary: " + String.format("%.2f", emp.getNetSalary()) + "\n"
                            + "-------------------\n";
                    payslipArea.setText(slip);
                    return;
                }
            }
            payslipArea.setText("Employee not found.");
        });

        tabs.add("Payslip", wrap(payslipPanel));

        add(tabs);
    }

    // ======================
    // HELPER UI METHODS
    // ======================

    private JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        return p;
    }

    private JPanel wrap(JPanel p) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(240, 240, 240));
        outer.add(p, BorderLayout.CENTER);
        return outer;
    }

    private JTextField createField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(200, 35));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return f;
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(66, 133, 244));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(46, 113, 214));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(66, 133, 244));
            }
        });

        return b;
    }

    private JTextArea createTextArea() {
        JTextArea a = new JTextArea();
        a.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        a.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return a;
    }

    // ======================
    // BUSINESS LOGIC
    // ======================

    private void addEmployee() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double basic = Double.parseDouble(salaryField.getText().trim());
            int totalDays = Integer.parseInt(totalDaysField.getText().trim());
            int presentDays = Integer.parseInt(presentDaysField.getText().trim());

            // basic validation
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name cannot be empty.");
                return;
            }

            List<Employee> list = FileStorage.loadEmployees();
            // prevent duplicate ID
            for (Employee e : list) {
                if (e.getEmpId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(this, "Employee with this ID already exists. Use Update.");
                    return;
                }
            }

            Employee emp = new Employee(id, name, basic, totalDays, presentDays);
            FileStorage.saveEmployee(emp);

            tableModel.addRow(new Object[]{
                    emp.getEmpId(), emp.getName(), String.format("%.2f", emp.getBasicSalary()),
                    String.format("%.2f", emp.getHra()), String.format("%.2f", emp.getDa()),
                    String.format("%.2f", emp.getTax()), String.format("%.2f", emp.getNetSalary()),
                    emp.getTotalDays(), emp.getPresentDays()
            });

            JOptionPane.showMessageDialog(this, "Employee Added Successfully!");
            clearForm();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter numeric values for salary/days.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        salaryField.setText("");
        totalDaysField.setText("");
        presentDaysField.setText("");
        table.clearSelection();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        loadAllEmployees();
    }

    private void loadAllEmployees() {
        List<Employee> list = FileStorage.loadEmployees();
        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                    emp.getEmpId(), emp.getName(), String.format("%.2f", emp.getBasicSalary()),
                    String.format("%.2f", emp.getHra()), String.format("%.2f", emp.getDa()),
                    String.format("%.2f", emp.getTax()), String.format("%.2f", emp.getNetSalary()),
                    emp.getTotalDays(), emp.getPresentDays()
            });
        }
    }

    private void deleteSelectedEmployee() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.");
            return;
        }
        String id = tableModel.getValueAt(r, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Delete employee " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        List<Employee> list = FileStorage.loadEmployees();
        List<Employee> updated = new ArrayList<>();
        for (Employee e : list) if (!e.getEmpId().equalsIgnoreCase(id)) updated.add(e);
        FileStorage.saveAllEmployees(updated);
        refreshTable();
    }

    private void updateSelectedEmployee() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select an employee from list to update.");
            return;
        }

        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double basic = Double.parseDouble(salaryField.getText().trim());
            int totalDays = Integer.parseInt(totalDaysField.getText().trim());
            int presentDays = Integer.parseInt(presentDaysField.getText().trim());

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name cannot be empty.");
                return;
            }

            List<Employee> list = FileStorage.loadEmployees();
            List<Employee> updated = new ArrayList<>();
            boolean found = false;
            for (Employee e : list) {
                if (e.getEmpId().equalsIgnoreCase(id)) {
                    Employee newE = new Employee(id, name, basic, totalDays, presentDays);
                    updated.add(newE);
                    found = true;
                } else {
                    updated.add(e);
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Employee ID not found. Use Add to create new.");
                return;
            }
            FileStorage.saveAllEmployees(updated);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Employee updated.");
            clearForm();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter numeric values for salary/days.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
        }
    }

    private void exportCSV() {
        List<Employee> list = FileStorage.loadEmployees();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees to export.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save CSV");
        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;
        String path = chooser.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".csv")) path += ".csv";

        try (BufferedWriter bw = new BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(path), java.nio.charset.StandardCharsets.UTF_8))) {
            bw.write("ID,Name,Basic,HRA,DA,Tax,Net,TotalDays,PresentDays");
            bw.newLine();
            for (Employee e : list) {
                bw.write(e.toString());
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exported to " + path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        }
    }
}
