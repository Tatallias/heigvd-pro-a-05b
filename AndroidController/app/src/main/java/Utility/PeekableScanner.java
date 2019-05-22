package Utility;

import java.util.Scanner;

/**
 * peekable scanner to allow to peek the next element
 */
public class PeekableScanner {

    private final Scanner scanner;
    private String next;

    /**
     * constructs a peekable scanner based on a scanner
     * @param scanner the scanner
     */
    public PeekableScanner(Scanner scanner) {
        this.scanner = scanner;
        this.next = scanner.hasNext() ? scanner.next() : null;
    }

    /**
     * returns whether or not next exists
     * @return
     */
    public boolean hasNext() {
        return next != null;
    }

    /**
     * returns the next element and moves the scanner forward
     * @return next
     */
    public String next() {
        String current = next;
        next = scanner.hasNext() ? scanner.next() : null;
        return current;
    }

    /**
     * returns the next element without moving the scanner
     * @return next
     */
    public String peek() {
        return next;
    }
}