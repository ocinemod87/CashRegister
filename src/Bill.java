import java.util.*;

/**
 * This class is responsible for taking the items purchased and returning the actual calculation.
 * It takes in consideration the possible discounts depending on the items number and returns:
 * - a subtotal without discount
 * - a total 
 */

public class Bill {
    private Map<String, Item> item;
    private Map<String, Integer> purchasedItems;
    private Map<Item, String>itemsAmount;
    private Map<Item, String>itemsAmountDiscounted;
    private Map<Item, Boolean> isDiscounted;
    private Set<Item> items;
    private int totalKr;
    private int totalCent;
    private int totalDiscountKr;
    private int totalDiscountCent;
    private int totalKrDiscount; //not used probably delete it
    private int totalCentDiscount; //not used probably delete it
    private String totalBill;
    private String totalBillDiscounted;
    private String barcodeIndex;
    private int specialDiscount;

    public Bill(List<Item> items){

        totalKr = 0;
        totalCent = 0;
        totalBill = "";
        specialDiscount = 0;

        itemsAmount = new HashMap<>();
        itemsAmountDiscounted = new HashMap<>();
        purchasedItems = new HashMap<>();
        isDiscounted = new HashMap<>();
        this.items = new HashSet<>();

        //Add the ArrayList to a Set to be used in the other methods when an Item Object is needed
        this.items.addAll(items);
        int numberOfItems = 0;

        //takes all the items into the List and check add it to a Map that keeps the number of how many item purchased
        for(Item i: items){
            if(purchasedItems.containsKey(i.getBarcode())){
                numberOfItems = purchasedItems.get(i.getBarcode()) + 1;
                purchasedItems.put(i.getBarcode(), numberOfItems);
            }else {
                purchasedItems.put(i.getBarcode(), 1);
            }
        }
        calculateBill();
        calculateDiscountedBill();
    }

    //If the number of items purchased exceed the number of limit to get a discount
    //will be calculated the discounted amount
    public void calculateDiscountedBill(){

        totalDiscountKr = 0;
        totalDiscountCent = 0;

        for(Item key: items){

            //Check if the item gives the right to a discount
            if(key.isDiscount()){

                //Check if the item limit for discount has been exceeded
                if(key.getLimit() <= purchasedItems.get(key.getBarcode())){
                    int kr = key.getKrDiscount() * purchasedItems.get(key.getBarcode());
                    int cent = key.getCentDiscount() * purchasedItems.get(key.getBarcode());

                    //Add the single amount to the total bill
                    totalDiscountKr += kr;
                    totalDiscountCent += cent;

                    //if the cent value get over 100 the value of kr raise and the value of cent restart from 0
                    if(totalDiscountCent >= 100){
                        int tick = totalDiscountCent / 100;
                        totalDiscountKr += tick;
                        totalDiscountCent = totalDiscountCent % 100;
                    }


                    //Create the string that will be returned for the receipt
                    String calculatedAmount = String.valueOf(kr);
                    calculatedAmount += ",";
                    calculatedAmount += String.valueOf(cent);

                    //System.out.println(calculatedAmount+"AM");

                    //add to the amount of the single item
                    itemsAmountDiscounted.put(key, calculatedAmount);

                    isDiscounted.put(key, true);
                }
            }
        }
    }

    //If the number of items purchased does not exceed the number of limit to get a discount
    //(or if the item does now allow a discount) the regular amount will be calculated
    public void calculateBill(){

        for(Item key: items){
            int kr = key.getKr() * purchasedItems.get(key.getBarcode());
            int cent = key.getCent() * purchasedItems.get(key.getBarcode());

            //Add the single amount to the total bill
            totalKr += kr;
            totalCent += cent;

            //if the cent value get over 100 the value of kr raise and the value of cent restart from 0
            if(cent >= 100){
                int tick = cent / 100;
                kr += tick;
                cent = cent % 100;
            }

            //Chreate the string that will be returned for the receipt
            String calculatedAmount = String.valueOf(kr);
            calculatedAmount += ",";
            if(String.valueOf(cent).length()==1){
                calculatedAmount += String.valueOf(cent);
                calculatedAmount += "0";
            }else {
                calculatedAmount += String.valueOf(cent);
            }


            //add to the amount of the single item
            itemsAmount.put(key, calculatedAmount);
        }
    }

    public String getItemAmount(Item i){
        return itemsAmount.get(i);
    }

    public String getItemAmountDiscounted(Item i){

        return itemsAmountDiscounted.get(i);
    }

    /**
     * Calculate the total amount and return a String of it
     * @return String
     */
    public String getTotalAmount(){
        if(totalCent >= 100){
            int tick = totalCent / 100;
            totalKr += tick;
            totalCent = totalCent % 100;
        }

        totalBill = String.valueOf(totalKr);
        totalBill += ",";

        //If the number after the comma has only one digit, add 0 to it
        if(String.valueOf(totalCent).length()==1){
            totalBill += String.valueOf(totalCent);
            totalBill += "0";
        }else {
            totalBill += String.valueOf(totalCent);
        }

        return totalBill;
    }

    public String getTotalBillDiscounted(){


        int kr = totalKr-totalDiscountKr;
        int cent = totalCent - totalDiscountCent;

        if(cent<0){
            kr -= 1;
            cent = 100+cent;
        }

        specialDiscount = kr;


        totalBillDiscounted = String.valueOf(kr);
        totalBillDiscounted += ",";
        totalBillDiscounted += String.valueOf(cent);

        return totalBillDiscounted;
    }

    public String getTotalDiscount(){
        int discKr =0;
        int discCent = 0;
        for(Item key: items){
            if(isDiscounted.containsKey(key)){
//                System.out.println(purchasedItems.get(key));
                int kr = (key.getKr()-key.getKrDiscount())*purchasedItems.get(key.getBarcode());
                int cent = (key.getCent()-key.getCentDiscount())*purchasedItems.get(key.getBarcode());

                if(cent>99){
                    kr += cent/100;
                    cent = cent%100;
                }
                discKr += kr;
                discCent += cent;

            }
        }
        if(discCent>99){
            discKr += discCent/100;
            discCent = discCent%100;
        }

        //FIX HERE
        //Needed for the total amoun discount calculus
        totalDiscountKr = discKr;
        totalDiscountCent = discCent;


        totalBillDiscounted = String.valueOf(discKr);
        totalBillDiscounted += ",";
        totalBillDiscounted += String.valueOf(discCent);

        return totalBillDiscounted;
    }


    public String getItemDiscount(Item i){

        if(isDiscounted.containsKey(i)){

            int krDiscount = (i.getKr()-i.getKrDiscount()) * purchasedItems.get(i.getBarcode());
            int centDiscount = (i.getCent()-i.getCentDiscount()) * purchasedItems.get(i.getBarcode());

            if(centDiscount>=100){
                krDiscount += centDiscount/100;
                centDiscount = centDiscount%100;
            }


            String singleDiscount = String.valueOf(krDiscount);
            singleDiscount += ",";
            if(String.valueOf(centDiscount).length()<2){
                singleDiscount += String.valueOf(centDiscount);
                singleDiscount += "0";
            }else {
                singleDiscount += String.valueOf(centDiscount);
            }
            if(singleDiscount.length()==4){
                return singleDiscount.substring(0,4);
            }else {
                return singleDiscount.substring(0,5);
            }

        }

        return null;
    }

    /**
     * Get the total amount discounted for an item purchased
     * @param i
     * @return
     */
    public boolean getIsDiscounted(Item i){

        if(isDiscounted.containsKey(i)){
            return isDiscounted.get(i);
        }else {
            return false;
        }
    }

    public int getNumberOfItems(Item i){
        return purchasedItems.get(i.getBarcode());
    }

    public int getSpecialDiscount(){
        return specialDiscount/50;
    }

    public String getMoms(){
        String temp = totalBillDiscounted;
        temp = temp.replace(",","");

        int mom = Integer.parseInt(temp);
        mom = (mom*20)/100;
        String finalMom = String.valueOf((mom/100));
        finalMom +=",";
        finalMom += mom%100;
        return finalMom;
    }
}
