public class Employee {

    private String empId;
    private String name;
    private double basicSalary;
    private double hra;
    private double da;
    private double tax;
    private double netSalary;
    private int totalDays;
    private int presentDays;

    // Constructor
    public Employee(String empId, String name, double basicSalary, int totalDays, int presentDays) {
        this.empId = empId;
        this.name = name;
        this.basicSalary = basicSalary;
        this.totalDays = totalDays;
        this.presentDays = presentDays;

        calculateSalary();
    }

    // Salary Calculation Logic
    private void calculateSalary() {

        // Allowances
        hra = basicSalary * 0.20;  // 20% HRA
        da  = basicSalary * 0.10;  // 10% DA
        tax = basicSalary * 0.05;  // 5% TAX

        // Attendance-based deduction
        double absentDays = totalDays - presentDays;
        double deduction = (absentDays / totalDays) * basicSalary;

        // Final Net Salary
        netSalary = basicSalary + hra + da - tax - deduction;
    }

    // Getters
    public String getEmpId() { return empId; }
    public String getName() { return name; }
    public double getBasicSalary() { return basicSalary; }
    public double getHra() { return hra; }
    public double getDa() { return da; }
    public double getTax() { return tax; }
    public double getNetSalary() { return netSalary; }
    public int getTotalDays() { return totalDays; }
    public int getPresentDays() { return presentDays; }

    @Override
    public String toString() {
        return empId + "," + name + "," + basicSalary + "," + hra + "," + da + "," + tax + "," + netSalary + "," + totalDays + "," + presentDays;
    }
}
