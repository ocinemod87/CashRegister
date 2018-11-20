public class Main {
    public static void main(String[] args) {
       // Parser parser = new Parser("0 580524 463272,MEJERI,SKUMMETMÆLK,5,95");
        CashRegister register = new CashRegister("src/prices.txt", "src/discounts.txt");
        register.printReceipt("src/bar5.txt");
    }
}