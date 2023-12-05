package fr.bafbi.javaproject;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final int tableId;
    private final int clientNumber;
    private final int serveurId;
    private final List<Command> commands = new ArrayList<>();


    public Transaction(int tableId, int clientNumber, int serveurId) {
        this.tableId = tableId;
        this.clientNumber = clientNumber;
        this.serveurId = serveurId;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public int tableId() {
        return tableId;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public int getServeurId() {
        return serveurId;
    }


}
