import static java.lang.IO.*;
import static java.lang.IO.println;

import java.util.*;

/**
 * Personal Expense Tracker
 * This program collects user expenses using a Record, calculates category totals,
 * and saves a report to a file.
 */

// record is like a blueprint that holds 3 related pieces of data
// This replaces separate lists
record Expense(double amount, String category) {}

void main() {
    // Create an empty list that will hold all our Expense records
    var expenses = new ArrayList<Expense>();

    while (true) {
        try {
            println("Enter expense amount: ");
            // Read the text typed by user and convert it to a doable
            double amount = Double.parseDouble(readln());

            println("Enter category: ");
            String category = readln();

            // Take those 3 pieces of info, and put it in our list
            expenses.add(new Expense(amount, category));

        } catch (Exception e) {
            println("Invalid input!");
            continue; // Returns to the beginning of the loop
        }

        println("Add another? (y/n): ");

        // If the user types 'n' (or 'N'), stops
        // Compares two strings for equality while ignoring differences in case
        if (readln().equalsIgnoreCase("n")) {
            break;
        }
    }

    // calls 'calculateTotals', catches the returned HashMap into finalTotals
    HashMap<String, Double> finalTotals = calculateTotals(expenses);

    // Read or write information in a file
    try (FileWriter file = new FileWriter("expense_report.txt")) {
        // Write a header at the top of the file
        file.write("Expenses\n-----------------\n");

        int count = 1;
        // Loop through every category-total pair in our Map
        for (var entry : finalTotals.entrySet()) {
            // Write a formatted line
            file.write("%d. %s: $%.2f%n".formatted(count++, entry.getKey(), entry.getValue()));
        }
        // Tell the user everything worked!
        println("Report successfully saved to expense_report.txt");
    } catch (IOException e) { // e is the variable name for the exception
        // If the computer can't write the file
        println("Error writing file: ");
    }
}

/**
 * Sums up expenses by category. Search for every expense inside record and validates if it exists or not
 * - If exists: look for the old amount, add the new amount, and save it back
 * - If does not exists yet: sum for the first time in this category
 * @param list The list of Expense records to process
 * @return A HashMap containing the summed totals per category
 */
HashMap<String, Double> calculateTotals(List<Expense> list) {
    HashMap<String, Double> totals = new HashMap<>();
    // Look at every single expense record in our list, one by one
    for (Expense e : list) { // python: for expense in expenses
        if (totals.containsKey(e.category())) {
            // Get the old total, add the new amount, and save it back
            totals.put(e.category(), totals.get(e.category()) + e.amount());
        } else {
            // Fist time seen this category
            totals.put(e.category(), e.amount());
        }
    }
    return totals;
}
