package com.mycompany.librarymanagement;

import java.io.Serializable;

/**
 * Lop TextBook (Sach giao khoa)
 * 
 * Ke thua tu lop Book.
 * Mo rong them thuoc tinh rieng la "subject" (mon hoc).
 * 
 * Ap dung tinh da hinh (Override ham toString)
 * va ke thua toan bo thuoc tinh cua Book (id, title, author, publisher, year, price, soLuong).
 */
public class TextBook extends Book {

    private static final long serialVersionUID = 1L;

    // ===== THUOC TINH RIENG =====
    private String subject; // Mon hoc

    // ===== CONSTRUCTOR =====
    /**
     * Khoi tao doi tuong TextBook voi du lieu day du.
     * @param id Ma sach
     * @param title Ten sach
     * @param author Tac gia
     * @param publisher Nha xuat ban
     * @param year Nam xuat ban
     * @param price Gia tien
     * @param soLuong So luong ton kho
     * @param subject Mon hoc (vi du: Toan, Van, Anh,...)
     */
    public TextBook(String id, String title, String author, String publisher,
                    int year, double price, int soLuong, String subject) {
        super(id, title, author, publisher, year, price, soLuong);
        this.subject = subject;
    }

    // ===== GETTER / SETTER =====
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    // ===== TO STRING (DA HINH) =====
    /**
     * Ghi de ham toString cua lop cha.
     * Hien thi thong tin sach giao khoa theo dinh dang bang,
     * co them cot "Chi tiet" hien mon hoc.
     */
    @Override
    public String toString() {
        return String.format("| %-10s | %-30s | %-20s | %-20s | %-4d | %-10.2f | %-8d | %-15s |",
                getId(),
                getTitle(),
                getAuthor(),
                getPublisher(),
                getYear(),
                getPrice(),
                getSoLuong(),
                "Mon: " + subject);
    }
}
