import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenTimeMonitor extends JFrame {
    private JLabel timeLabel;
    private JTextField limitField;
    private JButton setLimitButton;
    private int screenTimeSeconds = 0;
    private int limitSeconds = 0;
    private Timer timer;

    public ScreenTimeMonitor() {
        setTitle("Screen Time Monitor");
        setSize(350, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timeLabel = new JLabel("Screen Time: 00:00:00");
        limitField = new JTextField(5);
        setLimitButton = new JButton("Set Limit (min)");

        add(timeLabel);
        add(limitField);
        add(setLimitButton);

        setLimitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int minutes = Integer.parseInt(limitField.getText());
                    limitSeconds = minutes * 60;
                    JOptionPane.showMessageDialog(null, "Limit set to " + minutes + " minutes");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });

        startTracking();
        setVisible(true);
    }

    private void startTracking() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                screenTimeSeconds++;
                String formattedTime = formatTime(screenTimeSeconds);
                timeLabel.setText("Screen Time: " + formattedTime);

                if (limitSeconds > 0 && screenTimeSeconds >= limitSeconds) {
                    JOptionPane.showMessageDialog(null, "Time limit reached!", "Alert", JOptionPane.WARNING_MESSAGE);
                    limitSeconds = 0;
                }

                if (screenTimeSeconds % 60 == 0) {
                    saveLog();
                }
            }
        }, 1000, 1000);
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void saveLog() {
        String fileName = "screen_time_log.txt";
        String today = LocalDate.now().toString();
        String entry = today + " - Total: " + formatTime(screenTimeSeconds) + "\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(entry);
        } catch (IOException e) {
            System.out.println("Error saving log: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ScreenTimeMonitor();
    }
}
