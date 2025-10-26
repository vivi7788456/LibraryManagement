package com.mycompany.librarymanagement;

import java.time.Year;
import java.io.Serializable;

/**
 * Lop Book mo ta thong tin co ban cua mot cuon sach.
 * Ap dung tinh dong goi: cac thuoc tinh duoc khai bao private
 * va chi truy cap thong qua cac phuong thuc getter/setter.
 * 
 * Lop nay duoc dung lam lop cha cho cac loai sach cu the (vi du: TextBook).
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L; // Ho tro doc/ghi file

    // ================= THUOC TINH =================
    private String id;          // Ma sach
    private String title;       // Ten sach
    private String author;      // Tac gia
    private String publisher;   // Nha xuat ban
    private int year;           // Nam xuat ban
    private double price;       // Gia tien
    private int soLuong;        // So luong ton kho

    // ================= CONSTRUCTOR =================

    /** 
     * Constructor rong 
     */
    public Book() {}

    /**
     * Constructor day du
     * @param id ma sach
     * @param title ten sach
     * @param author tac gia
     * @param publisher nha xuat ban
     * @param year nam xuat ban
     * @param price gia sach
     * @param soLuong so luong ton kho
     */
    public Book(String id, String title, String author, String publisher, int year, double price, int soLuong) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID sach khong duoc de trong");
        this.id = id.trim();

        // Goi setter de kiem tra du lieu hop le
        setTitle(title);
        setAuthor(author);
        setPublisher(publisher);
        setYear(year);
        setPrice(price);
        setSoLuong(soLuong);
    }

    // ================= GETTER & SETTER =================

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Ten sach khong duoc de trong");
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Ten tac gia khong duoc de trong");
        this.author = author.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        if (publisher == null || publisher.trim().isEmpty())
            throw new IllegalArgumentException("Nha xuat ban khong duoc de trong");
        this.publisher = publisher.trim();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < 1900 || year > currentYear)
            throw new IllegalArgumentException("Nam xuat ban khong hop le (1900-" + currentYear + ")");
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0)
            throw new IllegalArgumentException("Gia phai lon hon 0");
        this.price = price;
    }

    public int getSoLuong() {
        return soLuong;
    }

    /**
     * Cap nhat so luong ton kho.
     * Khong cho phep gia tri am.
     */
    public void setSoLuong(int soLuong) {
        if (soLuong < 0)
            throw new IllegalArgumentException("So luong ton kho khong duoc am");
        this.soLuong = soLuong;
    }

    // ================= CAC HAM NGHIEP VU =================

    /**
     * Kiem tra sach con hang trong kho hay khong.
     * @return true neu so luong > 0
     */
    public boolean isAvailable() {
        return this.soLuong > 0;
    }

    /**
     * Giam so luong sach trong kho khi co nguoi muon.
     * @param amount so luong can giam
     * @throws IllegalArgumentException neu amount <= 0
     * @throws IllegalStateException neu so luong khong du de tru
     */
    public void decreaseSoLuong(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("So luong giam phai lon hon 0");
        if (this.soLuong < amount)
            throw new IllegalStateException("Khong du so luong trong kho de tru (con " + this.soLuong + ")");
        this.soLuong -= amount;
    }

    /**
     * Tang so luong sach (khi nhap hang hoac tra sach).
     * @param amount so luong can tang
     */
    public void increaseSoLuong(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("So luong them vao phai lon hon 0");
        this.soLuong += amount;
    }

    // ================= HIEN THI =================

    /**
     * Tra ve chuoi mo ta thong tin sach dang bang.
     * Lop con (TextBook) co the ghi de de hien thi chi tiet rieng.
     */
    @Override
    public String toString() {
        return String.format("| %-10s | %-30s | %-20s | %-20s | %-4d | %-10.2f | %-8d | %-15s |",
                id, title, author, publisher, year, price, soLuong, "");
    }

    /**
     * Kiem tra hai cuon sach co cung loai khong
     * (trung ten, tac gia, nam xuat ban).
     * @param other sach can so sanh
     * @return true neu trung, nguoc lai false
     */
    public boolean sameBook(Book other) {
        return this.title.equalsIgnoreCase(other.title)
                && this.author.equalsIgnoreCase(other.author)
                && this.year == other.year;
    }
}
