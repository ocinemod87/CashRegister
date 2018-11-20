import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CashRegister {
    private Map<String, Item> items;
    private List<String>barcodeOrder;
    private List<Item>purchasedOrder;
    private Item item;
    private Set<String>printedCategory;


    public CashRegister(String priceFilename, String discountsFilename){

        items = new HashMap<>();
        barcodeOrder = new ArrayList<>();
        purchasedOrder = new ArrayList<>();
        printedCategory = new HashSet<>();
        setItems(priceFilename);
        setDiscount(discountsFilename);
    }

    public void printReceipt(String barcodeFilename){

        try {
            //Take the filename as a string and create a File object, then pass the object to a scanner
            File file = new File(barcodeFilename);
            Scanner scanner = new Scanner(file);

            //scan the file and assign the barcode to an ArrayList to keep track of the order
            while (scanner.hasNextLine()){
                barcodeOrder.add(scanner.nextLine());
            }

            //Assaign the order to a list of Items to be purchased
            for(String s: barcodeOrder){
                if(items.containsKey(s)){
                    purchasedOrder.add(items.get(s));
                }
            }
        }catch (IOException e){
            System.out.println("Couldn't open the file");
        }

        //This method sorts the ArrayList of Item in alphabetical order before for category
        //and the inside each category for product
        purchasedOrder.sort(Comparator.comparing(Item::getCategory).thenComparing(Item::getName));

        Bill calculateBill = new Bill(purchasedOrder);

        //The ArrayList is assigned to a Set that deletes the duplicates,
        //then is reassigned to the ArrayList
        Set<Item> tempItem = new HashSet<>();
        tempItem.addAll(purchasedOrder);
        purchasedOrder.clear();
        purchasedOrder.addAll(tempItem);

        //This method sorts the ArrayList of Item in alphabetical order before for category
        //and the inside each category for product
        purchasedOrder.sort(Comparator.comparing(Item::getCategory).thenComparing(Item::getName));



        for(Item i: purchasedOrder){

            //Print the category before printing the set of items in that category
            if(!printedCategory.contains(i.getCategory())){

                System.out.println();
                String print = "* "+i.getCategory()+" *";
                //This is needed to center the category
                int x = ((40 -print.length())/2)+print.length();
                System.out.printf("%"+x+"s",print);
                System.out.println();
                printedCategory.add(i.getCategory());
            }
            String temp = " x ";

            //Print the items if item is major than 0
            if(calculateBill.getNumberOfItems(i)>0){
                if(calculateBill.getNumberOfItems(i)==1){
                    if(calculateBill.getItemAmount(i).length()<5){
                        System.out.printf("%-29s %s",i.getName(),calculateBill.getItemAmount(i));
                    }else {
                        System.out.printf("%-28s %s",i.getName(),calculateBill.getItemAmount(i));
                    }


                }else{

                    System.out.println(i.getName());

                    //Check the length of the items digit and adapt the printing format
                    if(i.getPrice().length()<5 && calculateBill.getItemAmount(i).length()==5){
                        System.out.printf("%3s %s %1s %21s",calculateBill.getNumberOfItems(i),
                                temp,i.getPrice(),calculateBill.getItemAmount(i));
                    }else if(i.getPrice().length()<5 && calculateBill.getItemAmount(i).length()<5){
                        System.out.printf("%3s %s %s %21s",calculateBill.getNumberOfItems(i),
                                temp,i.getPrice(),calculateBill.getItemAmount(i));
                    }else{
                            System.out.printf("%3s %s %s %20s",calculateBill.getNumberOfItems(i),
                                    temp,i.getPrice(),calculateBill.getItemAmount(i));
                    }

                }

                if(calculateBill.getIsDiscounted(i)){
                    System.out.println();
                    String rabat = "RABAT";
                    String rabatAmount = calculateBill.getItemDiscount(i);
                    rabatAmount+="-";
                    System.out.printf("%3s %29s",rabat,rabatAmount);
                }

                System.out.println();
            }
        }

        System.out.println();
        System.out.println("-------------------------------------");


        //Print SUBTOTAL
        System.out.println();
        String subtotal = "SUBTOT";

        System.out.printf("%s %27s",subtotal,calculateBill.getTotalAmount());

        System.out.println();

        if(!(calculateBill.getTotalDiscount().startsWith("0"))){
            System.out.println();
            String rabat = "RABAT";
            System.out.printf("%s %28s",rabat,calculateBill.getTotalDiscount());
            System.out.println();
        }



        System.out.println();
        System.out.printf("%s %28s","TOTAL", calculateBill.getTotalBillDiscounted());

        System.out.println();
        System.out.println();
        System.out.println("KØBET HAR UDLØST "+calculateBill.getSpecialDiscount()+" MÆRKER");

        System.out.println();
        System.out.printf("%s %23s","MOMS UDGØR",calculateBill.getMoms());
    }

    /**
     * This function takes the file name of a text file, it opens the file, scan it and parse it.
     * Then it assign the values contained into the file to an item object which is kept into the items Map
     * @param priceFilename
     */
    public void setItems(String priceFilename){
        try{
            //Take the filename as a string and create a File object, then pass the object to a scanner
            File file = new File(priceFilename);
            Scanner scanner = new Scanner(file);

            //Take each line of the file an parse it then pass it to an Item object and add to the Set of items
            while (scanner.hasNextLine()){
                Parser parsePrice = new Parser();
                parsePrice.parsePrice(scanner.nextLine());
                item = new Item(parsePrice.getBarcode(),parsePrice.getCategory(),parsePrice.getName(),parsePrice.getKr(),parsePrice.getCent());
                items.put(parsePrice.getBarcode(), item);
            }
        }catch (IOException e){
            System.out.println("Couldn't open the file");
        }
    }

    /**
     * This function takes the file name of a text file which contains the discounts.
     * It opens the file, scan it and parse it.
     * Then it assign the values contained into the file to an item object which is kept into the items Map
     * @param discountsFilename
     */
    public void setDiscount(String discountsFilename){
        try{
            //Take the filename as a string and create a File object, then pass the object to a scanner
            File file = new File(discountsFilename);
            Scanner scanner = new Scanner(file);

            //Take each line of the file an parse it then pass it to an Item object and add to the Set of items
            while (scanner.hasNextLine()){
                Parser parseDiscount = new Parser();
                parseDiscount.parseDiscount(scanner.nextLine());

                //Check if the HashMap contains the item, if true set the discount
                if(items.containsKey(parseDiscount.getBarcodeDiscount())){
                    item = items.get(parseDiscount.getBarcodeDiscount());
                    item.setDiscount(true);
                    item.setKrDiscount(parseDiscount.getKrDiscount());
                    item.setCentDiscount(parseDiscount.getCentDiscount());
                    item.setLimit(parseDiscount.getLimitDiscount());
                    items.put(item.getBarcode(), item);
                }
            }
        }catch (IOException e){
            System.out.println("Couldn't open the file");
        }
    }
}
