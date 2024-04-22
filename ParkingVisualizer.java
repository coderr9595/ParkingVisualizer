import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class ParkingVisualizer extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PARKING_SPOTS = 10;
    private static final int CAR_WIDTH = 50;
    private static final int CAR_HEIGHT = 30;
    private static final int SPACE_BETWEEN_CARS = 20;
    private static final Color[] CAR_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE};

    private JPanel parkingLotPanel;
    private JButton sortButton;
    private JButton resetButton;
    private JComboBox<String> algorithmComboBox;
    private JLabel statusLabel;
    private int[] carSizes;

    public ParkingVisualizer() {
        setTitle("Parking Visualizer");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        parkingLotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCars(g);
            }
        };
        parkingLotPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - 50));
        parkingLotPanel.setBackground(Color.WHITE);
        mainPanel.add(parkingLotPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortCars());
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            generateRandomCarSizes();
            statusLabel.setText("");
        });
        algorithmComboBox = new JComboBox<>(new String[]{"Bubble Sort", "Selection Sort", "Insertion Sort","Merge Sort"});
        controlPanel.add(sortButton);
        controlPanel.add(resetButton);
        controlPanel.add(algorithmComboBox);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        generateRandomCarSizes();

        add(mainPanel);
    }

    private void generateRandomCarSizes() {
        Random random = new Random();
        carSizes = new int[PARKING_SPOTS];
        for (int i = 0; i < PARKING_SPOTS; i++) {
            carSizes[i] = random.nextInt(5) + 1;
        }
        repaint();
    }

    private void drawCars(Graphics g) {
        int x = 50;
        int y = 50;
        for (int i = 0; i < PARKING_SPOTS; i++) {
            if (x + carSizes[i] * CAR_WIDTH > WIDTH - 50) {
                x = 50;
                y += CAR_HEIGHT + SPACE_BETWEEN_CARS;
            }
            g.setColor(CAR_COLORS[carSizes[i] - 1]);
            g.fillRect(x, y, carSizes[i] * CAR_WIDTH, CAR_HEIGHT);
            x += carSizes[i] * CAR_WIDTH + SPACE_BETWEEN_CARS;
        }
    }

    private void sortCars() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        int[] originalCarSizes = Arrays.copyOf(carSizes, carSizes.length); // Create a copy of the original car sizes
        switch (selectedAlgorithm) {
            case "Bubble Sort":
                bubbleSort(originalCarSizes);
                break;
            case "Selection Sort":
                selectionSort(originalCarSizes);
                break;
            case "Insertion Sort":
                insertionSort(originalCarSizes);
                break;
            case"Merge Sort":
                mergeSort(originalCarSizes);
                break;
            default:
                break;
        }
    }

    private void bubbleSort(int[] originalCarSizes) {
        Thread sortingThread = new Thread(() -> {
            for (int i = 0; i < PARKING_SPOTS - 1; i++) {
                for (int j = 0; j < PARKING_SPOTS - i - 1; j++) {
                    if (originalCarSizes[j] > originalCarSizes[j + 1]) {
                        int temp = originalCarSizes[j];
                        originalCarSizes[j] = originalCarSizes[j + 1];
                        originalCarSizes[j + 1] = temp;

                        clearPanel();
                        carSizes = Arrays.copyOf(originalCarSizes, originalCarSizes.length); // Restore the original car sizes
                        repaint();
                        delay(500); // Delay for visualization
                    }
                }
            }
            statusLabel.setText("Sorting completed");
        });
        sortingThread.start();
    }
    private void selectionSort(int[] originalCarSizes) {
        Thread sortingThread = new Thread(() -> {
            int n = originalCarSizes.length;
            for (int i = 0; i < n - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < n; j++) {
                    if (originalCarSizes[j] < originalCarSizes[minIndex]) {
                        minIndex = j;
                    }
                }
                int temp = originalCarSizes[minIndex];
                originalCarSizes[minIndex] = originalCarSizes[i];
                originalCarSizes[i] = temp;

                clearPanel();
                carSizes = Arrays.copyOf(originalCarSizes, originalCarSizes.length);
                repaint();
                delay(500);
            }
            statusLabel.setText("Sorting completed");
        });
        sortingThread.start();
    }

    private void insertionSort(int[] originalCarSizes) {
        Thread sortingThread = new Thread(() -> {
            int n = originalCarSizes.length;
            for (int i = 1; i < n; ++i) {
                int key = originalCarSizes[i];
                int j = i - 1;

                while (j >= 0 && originalCarSizes[j] > key) {
                    originalCarSizes[j + 1] = originalCarSizes[j];
                    j = j - 1;

                    clearPanel();
                    carSizes = Arrays.copyOf(originalCarSizes, originalCarSizes.length);
                    repaint();
                    delay(500);
                }
                originalCarSizes[j + 1] = key;
            }
            statusLabel.setText("Sorting completed");
        });
        sortingThread.start();
    }
    private void mergeSort(int[] originalCarSizes) {
        Thread sortingThread = new Thread(() -> {
            mergeSortHelper(originalCarSizes, 0, originalCarSizes.length - 1);
            statusLabel.setText("Sorting completed");
        });
        sortingThread.start();
    }

    private void mergeSortHelper(int[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSortHelper(arr, l, m);
            mergeSortHelper(arr, m + 1, r);
            merge(arr, l, m, r);

            clearPanel();
            carSizes = Arrays.copyOf(arr, arr.length);
            repaint();
            delay(500);
        }
    }

    private void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }


    private void clearPanel() {
        parkingLotPanel.getGraphics().clearRect(0, 0, WIDTH, HEIGHT - 50);
    }

    private void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ParkingVisualizer();
    }
}
