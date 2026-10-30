package edu.minibank;

import edu.minibank.model.*;
import edu.minibank.exception.BankException;
import edu.minibank.service.*;
import edu.minibank.util.*;
import java.nio.file.Path;
import java.util.Scanner;
import static java.lang.System.out;

public class MiniBank {
    private static final Path DATA = Path.of("data");

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--lab-examples")) { LabExamples.run(); return; }
        Repository<Account> repository = new Repository<>();
        AccountStore store = new AccountStore();
        try { repository.saveAll(StatePersister.load(DATA.resolve("accounts.dat"))); }
        catch (Exception e) { out.println("Could not load saved accounts: " + e.getMessage()); }
        BankService bank = new BankService(repository, store, DATA);
        out.println(new BankInfo("MiniBank", "Main Branch"));
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt(input, "Choose: ");
            try {
                switch (MenuOption.values()[choice - 1]) {
                    case OPEN_ACCOUNT -> {
                        String owner = readLine(input, "Owner name: ");
                        String email = readLine(input, "Email: ");
                        String mobile = readLine(input, "Mobile: ");
                        String type = readLine(input, "Type (SAVINGS/CURRENT/FIXED): ");
                        long opening = readLong(input, "Opening balance: ");
                        out.println("Opened: " + bank.openAccount(owner, email, mobile, type, opening));
                    }
                    case DEPOSIT -> { bank.deposit(readLine(input, "Account number: "), readLong(input, "Amount: ")); out.println("Deposit successful."); }
                    case WITHDRAW -> { bank.withdraw(readLine(input, "Account number: "), readLong(input, "Amount: ")); out.println("Withdrawal successful."); }
                    case TRANSFER -> { bank.transfer(readLine(input, "From account: "), readLine(input, "To account: "), readLong(input, "Amount: ")); out.println("Transfer successful."); }
                    case STATEMENT -> out.println(bank.statement(readLine(input, "Account number: ")));
                    case LIST_ACCOUNTS -> bank.listAccounts().forEach(out::println);
                    case REPORT -> out.println(bank.report());
                    case EXIT -> { bank.save(); out.println("Goodbye from MiniBank."); running = false; }
                }
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | BankException e) {
                out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            } finally {
                // Each menu operation returns to the prompt, whether it succeeds or fails.
            }
        }
    }

    private static void showMenu() { out.println("\n1. Open account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Statement\n6. List accounts\n7. Report\n8. Exit"); }
    private static int readInt(Scanner input, String prompt) { while (true) try { return Integer.parseInt(readLine(input, prompt)); } catch (NumberFormatException e) { out.println("Enter a number."); } }
    private static long readLong(Scanner input, String prompt) { while (true) try { return Long.parseLong(readLine(input, prompt)); } catch (NumberFormatException e) { out.println("Enter a whole amount."); } }
    private static String readLine(Scanner input, String prompt) { out.print(prompt); return input.nextLine().trim(); }
}
