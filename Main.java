public class Main {
    public static void main(String[] args) {

        // Optional: enable FlatLaf if you add the library.
        // If you want a professional look, add FlatLaf jar and uncomment below:
        //
        // try {
        //     com.formdev.flatlaf.FlatLightLaf.setup();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // Smooth UI rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
