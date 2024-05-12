import javax.swing.*;

public class application {
    public static void main(String[] args) throws Exception {
        int board_width = 400;
        int board_height = 600;

        JFrame frame = new JFrame("Snap");
        frame.setVisible(true);
	    frame.setSize(board_width, board_height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Snap snap = new Snap(board_width, board_height);
        frame.add(snap);
        frame.pack();
        snap.requestFocus();
    }
}
