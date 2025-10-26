package com.mycompany.librarymanagement;

import java.util.Scanner;

/**
 * Lop BookMenu chiu trach nhiem hien thi menu quan ly sach (kho)
 * Va goi cac chuc nang tu BookManager.
 * 
 * Lop nay giup tach rieng phan giao dien console ra khoi lop chinh LibraryManagement.
 */
public class BookMenu {

    /**
     * Ham chay menu quan ly sach.
     * Hien thi cac lua chon va goi cac ham tuong ung trong BookManager.
     */
    public static void run(BookManager bm, Scanner sc) {
        int choice;
        do {
            System.out.println("\n===== QUAN LY SACH (KHO) =====");
            System.out.println("1. Them loai sach moi");
            System.out.println("2. Nhap them hang");
            System.out.println("3. Xoa loai sach");
            System.out.println("4. Sua thong tin");
            System.out.println("5. Tim kiem sach");
            System.out.println("6. Hien thi danh sach");
            System.out.println("0. Quay lai");
            System.out.print("Chon (0–6): ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so hop le");
                choice = -1;
            }

            switch (choice) {
                case 1 -> addBook(bm, sc);      // Them sach moi
                case 2 -> addStock(bm, sc);     // Nhap them hang
                case 3 -> {                     // Xoa sach
                    System.out.print("Nhap ma sach can xoa: ");
                    bm.delete(sc.nextLine(), sc);
                }
                case 4 -> {                     // Cap nhat thong tin
                    System.out.print("Nhap ma sach can sua: ");
                    bm.update(sc.nextLine(), sc);
                }
                case 5 -> searchMenu(bm, sc);   // Tim sach
                case 6 -> bm.display();         // Hien thi danh sach
                case 0 -> System.out.println("Da quay lai menu chinh");
                default -> System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    // =====================================================
    // ================ CAC CHUC NANG PHU ==================
    // =====================================================

    /**
     * Them mot loai sach moi (sach thuong hoac sach giao khoa)
     */
    private static void addBook(BookManager bm, Scanner sc) {
        System.out.println("\n--- THEM SACH ---");
        System.out.println("1. Sach thuong");
        System.out.println("2. Sach giao khoa");
        System.out.println("0. Quay lai");

        int type;
        try {
            type = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Loi: Vui long nhap so hop le");
            return;
        }

        if (type == 0) return;

        // Nhap thong tin sach
        System.out.print("Nhap ma sach: ");
        String id = sc.nextLine().trim();
        System.out.print("Nhap ten sach: ");
        String title = sc.nextLine().trim();
        System.out.print("Nhap tac gia: ");
        String author = sc.nextLine().trim();
        System.out.print("Nhap nha xuat ban: ");
        String publisher = sc.nextLine().trim();
        System.out.print("Nhap nam xuat ban: ");
        int year = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Nhap gia: ");
        double price = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Nhap so luong: ");
        int qty = Integer.parseInt(sc.nextLine().trim());

        // Tao doi tuong sach
        Book book = (type == 1)
            ? new Book(id, title, author, publisher, year, price, qty)
            : new TextBook(id, title, author, publisher, year, price, qty, "Chua ro");

        bm.add(book);
    }

    /**
     * Nhap them hang (tang so luong ton kho)
     */
    private static void addStock(BookManager bm, Scanner sc) {
        System.out.print("Nhap ma sach can nhap them: ");
        String id = sc.nextLine().trim();
        System.out.print("Nhap so luong them: ");
        int amount = Integer.parseInt(sc.nextLine().trim());
        bm.addStock(id, amount);
    }

    /**
     * Menu tim kiem sach (theo ten/tac gia, NXB, hoac khoang gia)
     */
    private static void searchMenu(BookManager bm, Scanner sc) {
        System.out.println("\n--- TIM KIEM SACH ---");
        System.out.println("1. Theo ten / tac gia");
        System.out.println("2. Theo nha xuat ban");
        System.out.println("3. Theo khoang gia");
        System.out.println("0. Quay lai");

        int c;
        try {
            c = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Loi: Vui long nhap so hop le");
            return;
        }

        switch (c) {
            case 1 -> {
                System.out.print("Nhap tu khoa: ");
                bm.search(sc.nextLine());
            }
            case 2 -> {
                System.out.print("Nhap ten NXB: ");
                bm.searchByPublisher(sc.nextLine());
            }
            case 3 -> {
                System.out.print("Gia tu: ");
                double min = Double.parseDouble(sc.nextLine().trim());
                System.out.print("Den: ");
                double max = Double.parseDouble(sc.nextLine().trim());
                bm.searchByPriceRange(min, max);
            }
            case 0 -> System.out.println("Da quay lai menu truoc");
            default -> System.out.println("Lua chon khong hop le");
        }
    }
}
