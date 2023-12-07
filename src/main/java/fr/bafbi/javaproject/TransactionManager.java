package fr.bafbi.javaproject;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private final List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Transaction getTransaction(int transactionId) {
        return transactions.get(transactionId);
    }

    public List<Transaction> getTransactions(int serveurId) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            if (transaction.getServeurId() == serveurId) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public Transaction createTransaction(int tableId, int clientNumber, int serveurId) {
        Transaction transaction = new Transaction(tableId, clientNumber, serveurId, transactions.size());
        transactions.add(transaction);
        return transaction;
    }

    public List<Command> getCommands() {
        List<Command> commands = new ArrayList<>();
        for (Transaction transaction : transactions) {
            commands.add(transaction.getCommand());
        }
        return commands;
    }

    public List<Pair<Command, Integer>> getCommandsAndId() {
        List<Pair <Command, Integer>> commands = new ArrayList<>();
        for (Transaction transaction : transactions) {
            commands.add(new Pair<>(transaction.getCommand(), transaction.getId()));
        }
        return commands;
    }

    public long getTransactionCount() {
        return transactions.size();
    }


}
