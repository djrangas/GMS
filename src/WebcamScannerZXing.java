import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.javacpp.BytePointer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebcamScannerZXing {
    private VideoCapture camera;
    private JLabel cameraLabel;
    private ExecutorService executor;
    private boolean stopFlag = false;

    public WebcamScannerZXing(JPanel cameraPanel, JTextField targetField) {
        cameraLabel = new JLabel();
        cameraLabel.setHorizontalAlignment(JLabel.CENTER);
        cameraLabel.setPreferredSize(new Dimension(300, 300));
        cameraPanel.setLayout(new BorderLayout());
        cameraPanel.add(cameraLabel, BorderLayout.CENTER);

        executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> startCamera(targetField));
    }

    private void startCamera(JTextField targetField) {
        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            JOptionPane.showMessageDialog(null, "Error: Cannot access webcam.");
            return;
        }

        Mat frameMat = new Mat();
        while (!stopFlag) {
            if (camera.read(frameMat)) {
                BufferedImage image = matToBufferedImage(frameMat);
                if (image != null) {
                    Image scaled = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                    cameraLabel.setIcon(new ImageIcon(scaled));
                    String qrText = scanQRCode(image);
                    if (qrText != null) {
                        SwingUtilities.invokeLater(() -> targetField.setText(qrText));
                    }
                }
            }
        }
        camera.release();
        SwingUtilities.invokeLater(() -> {
            cameraLabel.setIcon(null); // Clear the image on stop
        });

    }

    private BufferedImage matToBufferedImage(Mat matrix) {
        BytePointer buffer = new BytePointer();
        opencv_imgcodecs.imencode(".png", matrix, buffer);
        byte[] bytes = new byte[(int) buffer.limit()];
        buffer.get(bytes);
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            return null;
        }
    }

    private String scanQRCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }
    
    public void restartScanner(JTextField targetField) {
        stopFlag = false;
        executor.submit(() -> startCamera(targetField));
    }


    public void stopScanner() {
        stopFlag = true;
    }
}
