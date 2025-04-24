package brm;

public class Book2 {
    private int bookId;
    private String title;
    private double price;
    private String author;
    private String publisher;

    public Book2(int bookId, String title, double price, String author, String publisher) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
    }

    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }

    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setTitle(String title) { this.title = title; }
    public void setPrice(double price) { this.price = price; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
}
