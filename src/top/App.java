/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package top;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

/**
 *
 * @author Shado
 */
public class App {

    final String STR_CONSTANT = "nguoiquaduong25111211";
    final String STR_IS_CHECK = "2511";

    private Result result;

    private boolean suspended;
    private boolean unlocking;
    private boolean peeking;

    private List<String> searchResults;
    private int resultIdx;

    static int countClick = 0;
    static Point startPoint = new Point();
    static Point endPoint = new Point();

    static String text = "";

    public App() {
        EventQueue.invokeLater(() -> {
            App.this.result = new Result();
        });
        this.suspended = true;
        this.unlocking = false;
        this.peeking = false;
    }

    private String getMacAddress() {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }

            return sb.toString();
        } catch (SocketException | java.net.UnknownHostException ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }

    private String readFile(String fileName) throws IOException {
        String code = "";
        Path path = Paths.get(fileName);
        try {
            Scanner scanner = new Scanner(path);
            while (scanner.hasNextLine()) {
                code = scanner.nextLine();
            }
            return code.trim();
        } catch (IOException ex) {
            throw new IOException();
        }
    }

    private String encryptMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isCheckLicense(String license) {
        App app = new App();
        String macAddress = app.getMacAddress();
        String md5 = app.encryptMD5(macAddress + STR_CONSTANT);

        if(license.startsWith(STR_IS_CHECK)) {
            return md5.equalsIgnoreCase(license.substring(4, license.length()));
        }
        return false;
    }

    public static void main(String[] args) {
        final App app = new App();
        
        String license = "";  
        try {
            license = app.readFile("license.dat");
        } catch (IOException ex) {
            System.err.println("Not found license");
        }

        if (app.isCheckLicense(license)) {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException ex) {
                System.err.println("There was a problem registering the native hook.");
                System.err.println(ex.getMessage());
                System.exit(1);
            }

            GlobalScreen.addNativeMouseListener((NativeMouseListener) new NativeMouseListener() {
                @Override
                public void nativeMouseClicked(NativeMouseEvent nme) {

                    if (nme.getButton() == 1) {
                        countClick++;

                        if (countClick == 1) {
                            startPoint = nme.getPoint();
                            System.out.println("start point " + startPoint);
                        }

                        if (countClick == 2) {
                            endPoint = nme.getPoint();
                            text = app.getText(startPoint, endPoint);
                            System.out.println("end point " + endPoint);
                        }
                    }

                    if (nme.getButton() == 2) {
                        countClick = 0;
                    }

                    if (nme.getButton() == 1 && MouseInfo.getPointerInfo().getLocation().getX() <= 25 && MouseInfo.getPointerInfo().getLocation().getY() <= 25) {
                        try {
                            if (text.length() != 0) {
                                final FileProcess fileProc = new FileProcess();

                                app.result.getLblQuery().setText(text);
                                app.searchResults = fileProc.search(text);
                                app.resultIdx = 0;
                                if (!app.searchResults.isEmpty()) {
                                    String searchResult = (String) app.searchResults.get(app.resultIdx);
                                    String[] substring = searchResult.split("\\|", 2);
                                    app.result.getLblQuery().setText(text);
                                    app.result.getLblQ().setText(substring[0]);
                                    app.result.getLblA().setText(substring[1]);
                                } else {
                                    app.result.getLblQ().setText("N/A");
                                    app.result.getLblA().setText("N/A");
                                }
                            } else {
                                app.result.getLblQuery().setText("N/A");
                                app.result.getLblQ().setText("N/A");
                                app.result.getLblA().setText("N/A");
                            }
                        } catch (IOException ex) {
                        }
                        text = "";
                        countClick = 0;
                    }
                }

                @Override
                public void nativeMousePressed(NativeMouseEvent nme) {
                    if (app.suspended && nme.getButton() == 2 && MouseInfo.getPointerInfo().getLocation().getX() >= Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 3.0 && MouseInfo.getPointerInfo().getLocation().getY() >= Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 3.0) {
                        new Thread(() -> {
                            app.unlocking = true;
                            for (int i = 0; i < 30; ++i) {
                                if (app.unlocking && MouseInfo.getPointerInfo().getLocation().getX() >= Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 3.0 && MouseInfo.getPointerInfo().getLocation().getY() >= Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 3.0) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException ex) {
                                    }
                                    continue;
                                }
                                return;
                            }

                            System.out.println("OPENED");
                            app.suspended = false;
                            app.result.setVisible(true);
                            app.result.getLblQ().setText("- - - - - - - - - - - - - - - - - - - - - - - - - READY - - - - - - - - - - - - - - - - - - - - - - - - - ");

                            new Thread(() -> {
                                while (!app.suspended && !app.peeking) {
                                    try {
                                        app.result.toFront();
                                        Thread.sleep(50);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Result.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }).start();
                        }).start();
                    }

                    if (!app.suspended && nme.getButton() == 2) {
                        app.peeking = true;
                        app.result.setVisible(true);
                        app.result.toFront();
                    }
                }

                @Override
                public void nativeMouseReleased(NativeMouseEvent nme) {
                    if (nme.getButton() == 1) {
                        app.unlocking = false;
                    }

                    if (!app.suspended && nme.getButton() == 2) {
                        app.peeking = false;
                        app.result.setVisible(false);
                    }
                }
            });

            GlobalScreen.addNativeMouseMotionListener((NativeMouseMotionListener) new NativeMouseMotionListener() {
                @Override
                public void nativeMouseMoved(NativeMouseEvent nme) {
                }

                @Override
                public void nativeMouseDragged(NativeMouseEvent nme) {
                }
            });

            GlobalScreen.addNativeMouseWheelListener((NativeMouseWheelListener) new NativeMouseWheelListener() {
                @Override
                public void nativeMouseWheelMoved(NativeMouseWheelEvent nmwe) {
                    if (app.searchResults == null || app.searchResults.isEmpty()) {
                        return;
                    }

                    String searchResult = "";
                    if (nmwe.getWheelRotation() > 0) {
                        if (app.resultIdx < app.searchResults.size() - 1) {
                            searchResult = (String) app.searchResults.get(++app.resultIdx);
                            String[] substring = searchResult.split("\\|", 2);
                            app.result.getLblQ().setText(substring[0]);
                            app.result.getLblA().setText(substring[1]);
                        }
                    } else if (nmwe.getWheelRotation() < 0 && app.resultIdx > 0) {
                        searchResult = (String) app.searchResults.get(--app.resultIdx);
                        String[] substring = searchResult.split("\\|", 2);
                        app.result.getLblQ().setText(substring[0]);
                        app.result.getLblA().setText(substring[1]);
                    }
                }
            });

            GlobalScreen.addNativeKeyListener((NativeKeyListener) new NativeKeyListener() {
                @Override
                public void nativeKeyPressed(NativeKeyEvent nke) {
                    if (!app.suspended && nke.getKeyCode() == 29) {
                        app.peeking = true;
                        app.result.setVisible(true);
                        app.result.toFront();
                    }
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent nke) {
                    if (!app.suspended && nke.getKeyCode() == 29) {
                        app.peeking = false;
                        app.result.setVisible(false);
                    }

                    // F10
                    if (nke.getKeyCode() == 68) {
                        try {
                            BufferedImage screenFullImage = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                            new File("Screenshots").mkdirs();
                            ImageIO.write((RenderedImage) screenFullImage, "png", new File("Screenshots/" + System.currentTimeMillis() + ".png"));
                        } catch (AWTException | IOException ex) {
                        }
                    }

                    // F4
                    if (nke.getKeyCode() == 62) {
                        System.exit(0);
                    }
                }

                @Override
                public void nativeKeyTyped(NativeKeyEvent nke) {
                }
            });

        } else {
            System.err.println("Error key");
        }
    }

    private String getText(Point startPoint, Point endPoint) {
        int y;
        int distanceY;
        int x;
        int distanceX;
        String resultText = "";

        try {
            distanceX = Math.abs((int) (endPoint.getX() - startPoint.getX()));
            distanceY = Math.abs((int) (endPoint.getY() - startPoint.getY()));
            x = (int) startPoint.getX();
            y = (int) startPoint.getY();
            if ((double) x > endPoint.getX()) {
                x = (int) endPoint.getX();
            }
            if ((double) y > endPoint.getY()) {
                y = (int) endPoint.getY();
            }
        } catch (Exception e) {
            distanceX = 0;
            distanceY = 0;
            x = 0;
            y = 0;
        }
        if (distanceX != 0 && distanceY != 0) {
            try {
                BufferedImage screenFullImage = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                screenFullImage = screenFullImage.getSubimage(x, y, distanceX, distanceY);
                new File("temp").mkdirs();
                BufferedImage after = new BufferedImage(distanceX * 3, distanceY * 3, 1);
                AffineTransform at = new AffineTransform();
                at.scale(3.0, 3.0);
                AffineTransformOp scaleOp = new AffineTransformOp(at, 2);
                after = scaleOp.filter(screenFullImage, after);
                String filename = "temp/" + System.currentTimeMillis() + ".png";
                ImageIO.write((RenderedImage) after, "png", new File(filename));
                resultText = scanfImgToText(filename);
                new File(filename).delete();
            } catch (AWTException | IOException screenFullImage) {
                resultText = "NA";
            } finally {
                startPoint = null;
                endPoint = null;
                countClick = 0;
            }
        }
        return resultText;
    }

    private static String scanfImgToText(String filename) {
        Tesseract tesseract = new Tesseract();
        try {
            tesseract.setDatapath("tessdata");
            return tesseract.doOCR(new File(filename));
        } catch (TesseractException e) {
            e.printStackTrace(System.out);
        }
        return "NA";
    }

}
