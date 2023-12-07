package fr.bafbi.javaproject;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private final int id;
    private final int tableId;
    private final int clientNumber;
    private final int serveurId;
    private final Command command = new Command();


    public Transaction(int tableId, int clientNumber, int serveurId, int id) {
        this.tableId = tableId;
        this.clientNumber = clientNumber;
        this.serveurId = serveurId;
        this.id = id;
    }

    public Command getCommand() {
        return command;
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

    public int getId() {
        return id;
    }


}
