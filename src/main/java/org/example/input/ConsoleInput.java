package org.example.input;

import org.example.interfaces.Input;

import java.util.Locale;
import java.util.Scanner;

public class ConsoleInput implements Input {
    private final Scanner scanner;

    public ConsoleInput() {
        this.scanner = new Scanner(System.in).useLocale(Locale.US);

    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public Double nextDouble() {
        return scanner.nextDouble();
    }

    @Override
    public int nextInt() {
        return scanner.nextInt();
    }
}
