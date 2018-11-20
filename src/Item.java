/**
 * This class is responsible for the item information stored into the Maps of the Cash Register
 */

public class Item {
    private String barcode;
    private String category;
    private String name;
    private int kr;
    private int cent;
    private boolean discount;
    private int krDiscount;
    private int centDiscount;
    private int limit;
    private String price;
    private String discountedPrice;

    public Item(String barcode, String category, String name, String kr, String cent){
        this.barcode = barcode;
        this.category = category;
        this.name = name;
        this.kr = Integer.parseInt(kr);
        this.cent = Integer.parseInt(cent);
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public void setKrDiscount(String krDiscount) {
        this.krDiscount = Integer.parseInt(krDiscount);
    }

    public void setCentDiscount(String centDiscount) {
        this.centDiscount = Integer.parseInt(centDiscount);
    }

    public void setLimit(String limit) {
        this.limit = Integer.parseInt(limit);
    }

    public String getBarcode() {
        return barcode;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getKr() {
        return kr;
    }

    public int getCent() {
        return cent;
    }

    public boolean isDiscount() {
        return discount;
    }

    public int getKrDiscount() {
        return krDiscount;
    }

    public int getCentDiscount() {
        return centDiscount;
    }

    public int getLimit() {
        return limit;
    }

    public String getPrice(){
        price = String.valueOf(kr);
        price += ",";
        if(String.valueOf(cent).length()<2){
            price += String.valueOf(cent);
            price += "0";
        }else {
            price += String.valueOf(cent);
        }

        return price;
    }

    public String getDiscountedPrice(){
        discountedPrice = String.valueOf(krDiscount);
        discountedPrice += ",";
        if(String.valueOf(centDiscount).length()<2){
            discountedPrice += String.valueOf(centDiscount);
            discountedPrice += "0";
        }else {
            discountedPrice += String.valueOf(centDiscount);
        }

        return discountedPrice;
    }
}
