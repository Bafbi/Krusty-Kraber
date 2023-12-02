package fr.bafbi.javaproject;

import fr.bafbi.javaproject.jobs.Serveur;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final int tableNumber;
    private final int clientNumber;
    private final Serveur serveur;
    private final List<Command> commands = new ArrayList<>();


    public Transaction(int tableNumber, int clientNumber, Serveur serveur) {
        this.tableNumber = tableNumber;
        this.clientNumber = clientNumber;
        this.serveur = serveur;
    }

    public List<Command> getCommands() {
        return commands;
    }


}
