package fr.bafbi.javaproject;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionManager {

    private final Map<Integer,Transaction> transactions = new HashMap<>();
    private int lastId = 0;

    public List<Transaction> getTransactions() {
        return transactions.values().stream().toList();
    }

    public Transaction getTransaction(int transactionId) {
        return transactions.get(transactionId);
    }

    public List<Transaction> getTransactions(int serveurId) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : this.transactions.values()) {
            if (transaction.getServeurId() == serveurId) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public Transaction createTransaction(int tableId, int clientNumber, int serveurId) {
        Transaction transaction = new Transaction(tableId, clientNumber, serveurId, lastId++);
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            commands.add(transaction.getCommand());
        }
        return commands;
    }

    public List<Pair<Command, Integer>> getCommandsAndId() {
        List<Pair <Command, Integer>> commands = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            commands.add(new Pair<>(transaction.getCommand(), transaction.getId()));
        }
        return commands;
    }

    public long getTransactionCount() {
        return transactions.size();
    }


    public void removeTransaction(int transactionId) {
        transactions.remove(transactionId);
    }
}
