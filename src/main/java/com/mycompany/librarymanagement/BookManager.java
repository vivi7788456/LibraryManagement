package com.mycompany.librarymanagement;

import java.util.*;

/**
 * Lop BookManager quan ly cac doi tuong sach trong he thong.
 * Thuc hien cac chuc nang:
 * - Them, xoa, sua, tim kiem, hien thi danh sach sach
 * - Kiem tra lien ket voi BorrowManager truoc khi xoa sach
 * - Luu va tai du lieu tu file thong qua lop Database
 */
public class BookManager implements IManager {

    // ================= THUOC TINH =================
    private ArrayList<Book> list = new ArrayList<>();  // Danh sach sach
    private static int totalBook = 0;                  // Tong so loai sach
    private BorrowManager borrowManager;               // Lien ket voi BorrowManager

    // ================= CONSTRUCTOR =================
    /**
     * Khi khoi tao, tu dong doc du lieu sach tu file (neu co)
     */
    public BookManager() {
        list = Database.loadBooks();
        totalBook = list.size();
    }

    // ================= LIEN KET GIUA CAC MANAGER =================
    public void setBorrowManager(BorrowManager br_m) {
        this.borrowManager = br_m;
    }

    // ================= CAC HAM PHU TRO =================
    public ArrayList<Book> getList() {
        return list;
    }

    public static int getTotalBook() {
        return totalBook;
    }

    /**
     * Tim sach theo ma ID
     */
    public Book findBookById(String id) {
        if (id == null || id.isEmpty()) return null;
        for (Book b : list) {
            if (b.getId().equalsIgnoreCase(id)) return b;
        }
        return null;
    }

    /**
     * In phan dau cua bang hien thi
     */
    private void printHeader() {
        System.out.println(String.format(
            "| %-10s | %-30s | %-20s | %-20s | %-4s | %-10s | %-8s | %-15s |",
            "ID", "Title", "Author", "Publisher", "Year", "Price", "So Luong", "Chi tiet"));
        System.out.println(String.format(
            "|-%-10s-|-%-30s-|-%-20s-|-%-20s-|-%-4s-|-%-10s-|-%-8s-|-%-15s-|",
            "----------", "------------------------------", "--------------------",
            "--------------------", "----", "----------", "--------", "---------------"));
    }

    /**
     * In phan cuoi cua bang hien thi
     */
    private void printFooter() {
        System.out.println(String.format(
            "|-%-10s-|-%-30s-|-%-20s-|-%-20s-|-%-4s-|-%-10s-|-%-8s-|-%-15s-|",
            "----------", "------------------------------", "--------------------",
            "--------------------", "----", "----------", "--------", "---------------"));
    }

    // =====================================================
    // ============= CAC HAM TRIEN KHAI INTERFACE ==========
    // =====================================================

    /**
     * Them mot doi tuong (object) vao danh sach (goi lai ham add(Book))
     */
    @Override
    public boolean add(Object o) {
        if (!(o instanceof Book)) {
            System.out.println("Loi: Doi tuong khong phai Book");
            return false;
        }
        return add((Book) o);
    }

    /**
     * Them moi mot quyen sach vao danh sach
     */
    public boolean add(Book b) {
        if (findBookById(b.getId()) != null) {
            System.out.println("Loi: Ma sach " + b.getId() + " da ton tai");
            System.out.println("Neu muon them so luong, vui long chon chuc nang 'Nhap them hang'");
            return false;
        }
        for (Book x : list) {
            if (x.sameBook(b)) {
                System.out.println("Loi: Loai sach nay da ton tai voi ma '" + x.getId() + "'");
                return false;
            }
        }
        list.add(b);
        totalBook++;
        Database.saveBooks(list);
        return true;
    }

    /**
     * Xoa sach theo ma ID (co kiem tra lien ket va so luong ton kho)
     */
    @Override
    public void delete(String id, Scanner sc) {
        Book found = findBookById(id);
        if (found == null) {
            System.out.println("Loi: Khong tim thay ma sach nay");
            return;
        }

        // Kiem tra lien ket voi phieu muon
        if (borrowManager != null && borrowManager.isBookBeingBorrowed(id)) {
            System.out.println("Loi: Khong the xoa sach nay vi dang duoc muon");
            return;
        }

        // Kiem tra so luong ton kho
        if (found.getSoLuong() > 0) {
            System.out.println("Canh bao: Loai sach nay con " + found.getSoLuong() + " cuon trong kho");
            System.out.println("Ban co chac muon xoa (1.Co / 0.Khong)? ");
            String choice = sc.nextLine();
            if (!choice.equals("1")) {
                System.out.println("Da huy thao tac xoa");
                return;
            }
            System.out.println("Da xac nhan xoa va thanh ly kho");
        }

        list.remove(found);
        totalBook--;
        Database.saveBooks(list);
        System.out.println("Da xoa loai sach thanh cong");
    }

    /**
     * Cap nhat thong tin sach
     */
    @Override
    public void update(String id, Scanner sc) {
        Book bookToUpdate = findBookById(id);
        if (bookToUpdate == null) {
            System.out.println("Loi: Khong tim thay ma sach nay");
            return;
        }

        try {
            System.out.print("Ten moi: ");
            String newTitle = sc.nextLine();
            System.out.print("Tac gia moi: ");
            String newAuthor = sc.nextLine();
            System.out.print("Nha xuat ban moi: ");
            String newPublisher = sc.nextLine();
            System.out.print("Nam moi: ");
            String newYearStr = sc.nextLine();
            System.out.print("Gia moi: ");
            String newPriceStr = sc.nextLine();

            int newYear = -1;
            if (!newYearStr.isEmpty()) newYear = Integer.parseInt(newYearStr);
            double newPrice = -1.0;
            if (!newPriceStr.isEmpty()) newPrice = Double.parseDouble(newPriceStr);

            if (!newTitle.isEmpty()) bookToUpdate.setTitle(newTitle);
            if (!newAuthor.isEmpty()) bookToUpdate.setAuthor(newAuthor);
            if (!newPublisher.isEmpty()) bookToUpdate.setPublisher(newPublisher);
            if (newYear != -1) bookToUpdate.setYear(newYear);
            if (newPrice != -1.0) bookToUpdate.setPrice(newPrice);

            Database.saveBooks(list);
            System.out.println("Da cap nhat thong tin sach thanh cong");

        } catch (NumberFormatException e) {
            System.out.println("Loi: Nhap sai dinh dang so");
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    /**
     * Tim sach theo ten hoac tac gia
     */
    @Override
    public void search(String keyword) {
        boolean found = false;
        printHeader();
        String key = keyword.toLowerCase();
        for (Book b : list) {
            if (b.getTitle().toLowerCase().contains(key) ||
                b.getAuthor().toLowerCase().contains(key)) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) System.out.println("Khong co sach nao phu hop");
        else printFooter();
    }

    /**
     * Hien thi toan bo danh sach sach trong kho
     */
    @Override
    public void display() {
        if (list.isEmpty()) {
            System.out.println("Trong kho khong co sach nao");
            return;
        }
        System.out.println("--- DANH SACH KHO SACH ---");
        printHeader();
        for (Book b : list) System.out.println(b);
        printFooter();
        System.out.println("Tong so loai sach: " + list.size());
    }

    // ================= CAC CHUC NANG MO RONG =================

    /**
     * Nhap them hang (tang so luong sach trong kho)
     */
    public void addStock(String bookId, int amountToAdd) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Loi: Khong tim thay ma sach " + bookId);
            return;
        }
        try {
            book.increaseSoLuong(amountToAdd);
            Database.saveBooks(list);
            System.out.println("Nhap hang thanh cong");
            System.out.println("Sach: " + book.getTitle());
            System.out.println("Ton kho moi: " + book.getSoLuong());
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    /**
     * Tim sach theo nha xuat ban
     */
    public void searchByPublisher(String keyword) {
        boolean found = false;
        printHeader();
        String key = keyword.toLowerCase();
        for (Book b : list) {
            if (b.getPublisher().toLowerCase().contains(key)) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) System.out.println("Khong co sach nao tu NXB nay");
        else printFooter();
    }

    /**
     * Tim sach theo khoang gia
     */
    public void searchByPriceRange(double min, double max) {
        boolean found = false;
        printHeader();
        for (Book b : list) {
            double p = b.getPrice();
            if (p >= min && p <= max) {
                System.out.println(b);
                found = true;
            }
        }
        if (!found) System.out.println("Khong co sach nao trong khoang gia nay");
        else printFooter();
    }
}
