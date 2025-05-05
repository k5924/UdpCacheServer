package org.example.ucs.client.commands;

import java.nio.ByteBuffer;
import java.util.Scanner;

public final class CommandHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandEncodingFactory commandEncodingFactory;

    public CommandHandler(final CommandEncodingFactory commandEncodingFactory) {
        this.commandEncodingFactory = commandEncodingFactory;
    }

    public InputOption expectInput() {
        System.out.print("Enter command to execute (Get/Put/Delete/Heartbeat/Exit> ");
        final String command = scanner.nextLine();
        return InputOption.getType(command);
    }

    public void executeInput(final ByteBuffer buffer, final InputOption inputOption) {
        switch (inputOption) {
            case GET -> handleGetInput(buffer);
            case PUT -> handlePutInput(buffer);
            case DELETE -> handleDeleteInput(buffer);
            case HEARTBEAT -> handleHeartbeat(buffer);
        }
    }

    private void handleHeartbeat(final ByteBuffer buffer) {
        commandEncodingFactory.encodePing(buffer);
    }

    private void handleDeleteInput(final ByteBuffer buffer) {
        System.out.print("Enter key to delete (case is insensitive)> ");
        final String key = scanner.nextLine();
        final String adjustedKey = key.trim().toLowerCase();
        commandEncodingFactory.encodeDeleteRequest(buffer, adjustedKey);
    }

    private void handlePutInput(final ByteBuffer buffer) {
        System.out.print("Enter key to put (case is insensitive)> ");
        final String key = scanner.nextLine();
        final String adjustedKey = key.trim().toLowerCase();
        System.out.print("Enter value to put (case is insensitive)> ");
        final String value = scanner.nextLine();
        final String adjustedValue = value.trim().toLowerCase();
        commandEncodingFactory.encodePutRequest(buffer, adjustedKey, adjustedValue);
    }

    private void handleGetInput(final ByteBuffer buffer) {
        System.out.print("Enter key to get (case is insensitive)> ");
        final String key = scanner.nextLine();
        final String adjustedKey = key.trim().toLowerCase();
        commandEncodingFactory.encodeGetRequest(buffer, adjustedKey);
    }

    public enum InputOption {
        EXIT("exit"),
        GET("get"),
        PUT("put"),
        DELETE("delete"),
        HEARTBEAT("heartbeat");

        private final String value;

        InputOption(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static InputOption getType(final String value) {
            for (InputOption inputOption : InputOption.values()) {
                if (inputOption.getValue().equalsIgnoreCase(value.trim())) {
                    return inputOption;
                }
            }
            return EXIT;
        }
    }
}
