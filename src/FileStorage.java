import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final String FILE_NAME = "employees.txt";

    // Append single employee
    public static synchronized void saveEmployee(Employee emp) {
        ensureFileExists();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME, true), StandardCharsets.UTF_8))) {
            bw.write(emp.toString());
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Overwrite entire file from a list (useful for edit/delete operations)
    public static synchronized void saveAllEmployees(List<Employee> employees) {
        ensureFileExists();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME, false), StandardCharsets.UTF_8))) {
            for (Employee emp : employees) {
                bw.write(emp.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load all employees (skips malformed lines)
    public static synchronized List<Employee> loadEmployees() {
        List<Employee> list = new ArrayList<>();
        File f = new File(FILE_NAME);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                // Expecting 9 fields: id,name,basic,hra,da,tax,net,totalDays,presentDays
                if (data.length < 9) {
                    // skip malformed
                    continue;
                }
                try {
                    String id = data[0];
                    String name = data[1];
                    double basic = Double.parseDouble(data[2]);
                    int totalDays = Integer.parseInt(data[7]);
                    int presentDays = Integer.parseInt(data[8]);

                    Employee emp = new Employee(id, name, basic, totalDays, presentDays);
                    list.add(emp);
                } catch (Exception ex) {
                    // skip malformed line
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading employees file: " + e.getMessage());
        }
        return list;
    }

    private static void ensureFileExists() {
        try {
            File f = new File(FILE_NAME);
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
