package com.example.keepalive;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log
@SpringBootApplication
public class KeepaliveApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(KeepaliveApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    private final JFrame frame = new JFrame("Keepalive");

    @Override
    public void run(String... args) throws Exception {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton();
        button.addActionListener((event)->{
            Point location = MouseInfo.getPointerInfo().getLocation();
//            log.info(location.toString());
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        frame.getContentPane().add(button);
        Point startLocation = MouseInfo.getPointerInfo().getLocation();
        frame.setBounds(startLocation.x-50, startLocation.y-10, 500,60);
        frame.setVisible(true);
        Robot r = new Robot();
        CompletableFuture.runAsync(()->{
            Point lastLocation = MouseInfo.getPointerInfo().getLocation();
            int direction = 1;
            while (true){
                // check the last mouse position
                Point location = MouseInfo.getPointerInfo().getLocation();
                // if we have moved since the last check, do nothing
                if (location.equals(lastLocation)) {// otherwise, move it one pixel
                    // set the new value for the last mouse position
                    lastLocation = new Point(location.x + direction, location.y);
                    moveTo(r, location.x + direction, location.y);

                    // toggle direction
                    direction = -direction;
                } else {
//                    log.info(String.format("Current location:%s; Last location: %s. Skipping", location, lastLocation));
                    lastLocation = location;
                }
                sleep(60*2000);
            }

        }, executor);
    }

    private void moveTo(Robot r, int x, int y) {
        r.mouseMove(x, y);
//        log.info(String.format("Moving to %d,%d", x, y));
//            r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//            r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
