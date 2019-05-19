package Utility;

import java.util.Scanner;

public class PeekableScanner {

    private final Scanner scanner;
    private String next;

    public PeekableScanner(Scanner scanner) {
        this.scanner = scanner;
        this.next = scanner.hasNext() ? scanner.next() : null;
    }

    public boolean hasNext() {
        return next != null;
    }

    public String next() {
        String current = next;
        next = scanner.hasNext() ? scanner.next() : null;
        return current;
    }

    public String peek() {
        return next;
    }
}