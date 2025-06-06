import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<String> order = new ArrayList<>();
    static ArrayList<Double> prices = new ArrayList<>();
    static ArrayList<Integer> quantities = new ArrayList<>();
    static ArrayList<String> usernames = new ArrayList<>();
    static ArrayList<String> passwords = new ArrayList<>();

    static boolean loggedIn = false;
    static String currentUser = "";

    static String[] mainPasta = {"PASTA ALLA CARBONARA", "PASTA AMATRICIANA", "PASTA AL TONNO", "PASTA CACIO E PEPE"};
    static String[] mainDiscPasta = {
            "Pasta with eggs, pecorino romano cheese, guanciale, and black pepper ",
            "Pasta with tomatoes, pecorino romano cheese, guanciale, olive oil, and chili ",
            "Pasta with tuna, garlic, anchovy fillets, tomato sauce, and olive oil ",
            "Pasta with pecorino romano cheese, and black pepper "
    };
    static double[] mainPastaPrices = {240.00, 250.00, 225.00, 170.00};

    static String[] mainPizza = {"MARGHERITA PIZZA", "QUATTRO FORMAGGI", "PROSCIUTTO E RUCOLA", "PIZZA CON MOZZARELLA DI BUFALA"};
    static String[] mainDiscPizza = {
            "Classic Italian pizza with tomato sauce, mozzarella, olive oil, and fresh basil. ",
            "Italian pizza with four cheese: mozzarella, gorgonzola, fontina, and parmesan cheese ",
            "Italian Pizza combining salty prosciutto, fresh arugula, and Parmesan ",
            "Italian pizza with tomato sauce, buffalo mozzarella, olive oil, and fresh basil."
    };
    static double[] mainPizzaPrices = {200.00, 270.00, 240.00, 240.00};

    static String[] drinks = {"COCA COLA", "SPRITE", "WINE", "BEER", "WATER"};
    static double[] drinkPrices = {25.00, 25.00, 160.00, 50.00, 10.00};

    public static void main(String[] args) {
        while (true) {
            loginChoice();
            if (!loggedIn) {
                continue;
            }
            displayMenu();
            takeOrder();
            displayOrderSummary();
            modifyOrder();
            processPayment();
            finalReceipt();

            String choice;
            while (true) {
                System.out.print("Would you like to do another transaction? (Y/N): ");
                choice = sc.next().trim().toUpperCase();
                sc.nextLine();
                if (choice.equals("Y") || choice.equals("N")) break;
                System.out.println("Invalid input. Please enter Y or N.");
            }

            if (choice.equals("N")) {
                System.out.println("Thank you for visiting Sapori d'Italia! See you again!");
                break;
            }

            order.clear();
            prices.clear();
            quantities.clear();
            System.out.println("\nStarting a new transaction\n");
        }
    }

    public static void loginChoice() {
        while (true) {
            System.out.println("\nLogin to access the menu: ");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                login();
                if (loggedIn) break;
            } else if (choice.equals("2")) {
                register();
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Please pick 1, 2, or 3.");
            }
        }
    }

    public static void login() {
        System.out.print("Enter username: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();

        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(email) && passwords.get(i).equals(password)) {
                System.out.println("Login successful!");
                loggedIn = true;
                currentUser = email;
                return;
            }
        }

        System.out.println("Invalid username or password. Please try again.");
    }

    public static void register() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        Pattern patternUsername = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
        Matcher matcherUsername = patternUsername.matcher(username);

        if (!matcherUsername.matches()) {
            System.out.println("Invalid username. Username must be alphanumeric and 5-15 characters long.");
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();
        Pattern patternPassword = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$");
        Matcher matcherPassword = patternPassword.matcher(password);

        if (!matcherPassword.matches()) {
            System.out.println("Invalid Password. Password must contain at least one uppercase letter, one number, and be 8-20 characters long.");
            return;
        }

        usernames.add(username);
        passwords.add(password);
        System.out.println("Registration successful! You can now log in.");
    }

    public static void displayMenu() {
        System.out.println("----------------------------------------------------------------");
        System.out.println("Welcome to Sapori d'Italia!");
        System.out.println("Menu:");
        System.out.println("----------------------------------------------------------------");

        System.out.println("PASTA DISHES:");
        for (int i = 0; i < mainPasta.length; i++) {
            System.out.println((i + 1) + ". " + mainPasta[i] + " - ₱" + mainPastaPrices[i]);
            System.out.println("   " + mainDiscPasta[i]);
        }

        System.out.println("----------------------------------------------------------------");
        System.out.println("PIZZA DISHES:");
        for (int i = 0; i < mainPizza.length; i++) {
            System.out.println((i + 5) + ". " + mainPizza[i] + " - ₱" + mainPizzaPrices[i]);
            System.out.println("   " + mainDiscPizza[i]);
        }

        System.out.println("----------------------------------------------------------------");
        System.out.println("BEVERAGES:");
        for (int i = 0; i < drinks.length; i++) {
            System.out.println((i + 9) + ". " + drinks[i] + " - ₱" + drinkPrices[i]);
        }
        System.out.println("----------------------------------------------------------------");
    }

    public static void takeOrder() {
        System.out.println("-------------------- ORDER LIST --------------------");
        while (true) {
            System.out.print("Enter the item number 1-13 to add order (0 to finish): ");
            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                sc.nextLine();
                continue;
            }

            if (choice >= 1 && choice <= 13) {
                int quantity = 1;
                System.out.print("Enter quantity: ");
                try {
                    quantity = sc.nextInt();
                    sc.nextLine();
                    if (quantity < 1) {
                        System.out.println("Quantity must be at least 1.");
                        quantity = 1;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input.");
                    sc.nextLine();
                    quantity = 1;
                }

                if (choice >= 1 && choice <= 4) {
                    order.add(mainPasta[choice - 1]);
                    prices.add(mainPastaPrices[choice - 1] * quantity);
                } else if (choice >= 5 && choice <= 8) {
                    order.add(mainPizza[choice - 5]);
                    prices.add(mainPizzaPrices[choice - 5] * quantity);
                } else {
                    order.add(drinks[choice - 9]);
                    prices.add(drinkPrices[choice - 9] * quantity);
                }
                quantities.add(quantity);
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }
    }

    public static void displayOrderSummary() {
        System.out.println("---------------- ORDER SUMMARY ----------------");
        double total = calculateTotal();
        if (order.isEmpty()) {
            System.out.println("No items ordered.");
        } else {
            for (int i = 0; i < order.size(); i++) {
                System.out.println((i + 1) + ". " + order.get(i) + " x" + quantities.get(i) + " - ₱" + prices.get(i));
            }
            System.out.println("Total: ₱" + total);
        }
        System.out.println("-----------------------------------------------");
    }

    public static void modifyOrder() {
        while (true) {
            System.out.println("Would you like to: ");
            System.out.println("1. Add another item");
            System.out.println("2. Remove an item");
            System.out.println("3. Proceed to payment");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    takeOrder();
                    displayOrderSummary();
                    break;
                case 2:
                    removeOrderSummary();
                    displayOrderSummary();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    public static void removeOrderSummary() {
        while (true) {
            System.out.print("Do you want to remove an order? (Y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("Y")) {
                if (order.isEmpty()) {
                    System.out.println("No items in the order to remove.");
                    break;
                }

                System.out.println("Select the order number to remove:");
                for (int i = 0; i < order.size(); i++) {
                    System.out.println((i + 1) + ". " + order.get(i) + " x" + quantities.get(i) + " - ₱" + prices.get(i));
                }

                System.out.print("Enter the item number to remove: ");
                try {
                    int removeIndex = sc.nextInt();
                    sc.nextLine();
                    if (removeIndex > 0 && removeIndex <= order.size()) {
                        System.out.println(order.get(removeIndex - 1) + " has been removed.");
                        order.remove(removeIndex - 1);
                        prices.remove(removeIndex - 1);
                        quantities.remove(removeIndex - 1);
                    } else {
                        System.out.println("Invalid item number, try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Try again.");
                    sc.nextLine();
                }
            } else if (choice.equalsIgnoreCase("N")) {
                break;
            } else {
                System.out.println("Invalid input, please enter Y or N.");
            }
        }
    }

    public static void processPayment() {
        double total = calculateTotal();

        System.out.println("------------------ PAYMENT --------------------");
        while (true) {
            System.out.print("Enter the amount you are paying: ");

            try {
                double amountPaid = sc.nextDouble();
                if (amountPaid >= total) {
                    System.out.printf("Payment Successful! Change: ₱ %.2f\n", (amountPaid - total));
                    logTransaction();
                    break;
                } else {
                    System.out.println("Insufficient amount. Please enter a valid amount.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }

        System.out.println("Thank you for dining at Sapori d'Italia! See you again!");
        System.out.println("-----------------------------------------------");
    }

    public static void finalReceipt() {
        System.out.println("-------------------- RECEIPT --------------------");
        for (int i = 0; i < order.size(); i++) {
            System.out.println(order.get(i) + " x" + quantities.get(i) + " - ₱" + prices.get(i));
        }
        System.out.println("Total: ₱" + calculateTotal());
        System.out.println("-------------------------------------------------");
    }

    public static double calculateTotal() {
        double total = 0;
        for (double price : prices) {
            total += price;
        }
        return total;
    }

    public static void logTransaction() {
        try (FileWriter writer = new FileWriter("transactions.txt", true)) {
            writer.write("--------------------------------------------------\n");
            writer.write("Date: " + java.time.LocalDateTime.now() + "\n");
            writer.write("User: " + currentUser + "\n" );
            writer.write("Items Purchased: \n");

            for (int i = 0; i < order.size(); i++) {
                writer.write("  - " + order.get(i) + " x" + quantities.get(i) + " = ₱" + prices.get(i) + "\n");
            }

            writer.write("TOTAL: ₱" + calculateTotal() + "\n");
            writer.write("--------------------------------------------------\n\n");
        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }
}
