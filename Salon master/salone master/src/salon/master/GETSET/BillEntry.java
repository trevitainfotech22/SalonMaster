package salon.master.GETSET;

import javafx.beans.property.*;

public class BillEntry {
    private final StringProperty invoiceNo;
    private final StringProperty custNum;
    private final StringProperty item;
    private final StringProperty paymentMethod;
    private final StringProperty date;
    private final IntegerProperty stock;
    private final DoubleProperty price;

    public BillEntry(String invoiceNo, String custNum, String item, String paymentMethod, String date, int stock, double price) {
        this.invoiceNo = new SimpleStringProperty(invoiceNo);
        this.custNum = new SimpleStringProperty(custNum);
        this.item = new SimpleStringProperty(item);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.date = new SimpleStringProperty(date);
        this.stock = new SimpleIntegerProperty(stock);
        this.price = new SimpleDoubleProperty(price);
    }

    // Getters and Setters for invoiceNo
    public String getInvoiceNo() {
        return invoiceNo.get();
    }

    public StringProperty invoiceNoProperty() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo.set(invoiceNo);
    }

    // Getters and Setters for custNum
    public String getCustNum() {
        return custNum.get();
    }

    public StringProperty custNumProperty() {
        return custNum;
    }

    public void setCustNum(String custNum) {
        this.custNum.set(custNum);
    }

    // Getters and Setters for item
    public String getItem() {
        return item.get();
    }

    public StringProperty itemProperty() {
        return item;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    // Getters and Setters for paymentMethod
    public String getPaymentMethod() {
        return paymentMethod.get();
    }

    public StringProperty paymentMethodProperty() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod.set(paymentMethod);
    }

    // Getters and Setters for date
    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    // Getters and Setters for stock
    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    // Getters and Setters for price
    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
}
