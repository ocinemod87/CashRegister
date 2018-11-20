/**
 * This class is responsible for taking a string line and parse it
 * following the format <barcode>,<category>,<name>,<kr>,<cent>
 */
public class Parser {
    private String barcode;
    private String category;
    private String name;
    private String kr;
    private String cent;
    private String barcodeDiscount;
    private String limitDiscount;
    private String krDiscount;
    private String centDiscount;

    public Parser(){

    }

    /**
     * parcePrice takes a takes a String as parameter and split it into an array of String,
     * it splits the line by the comma ","
     * @param line
     */
    public void parsePrice(String line){
        String [] parts = line.split(",");
        barcode = parts[0];
        category = parts[1];
        name = parts[2];
        kr = parts[3];
        cent = parts[4];
    }

    /**
     * parseDiscount takes a takes a String as parameter and split it into an array of String,
     *  it splits the line by the comma ","
     * @param line
     */
    public void parseDiscount(String line){
        String [] parts = line.split(",");
        barcodeDiscount = parts[0];
        limitDiscount = parts[1];
        krDiscount = parts [2];
        centDiscount = parts[3];
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

    public String getKr() {
        return kr;
    }

    public String getCent() {
        return cent;
    }

    public String getBarcodeDiscount() {
        return barcodeDiscount;
    }

    public String getLimitDiscount() {
        return limitDiscount;
    }

    public String getKrDiscount() {
        return krDiscount;
    }

    public String getCentDiscount() {
        return centDiscount;
    }
}
