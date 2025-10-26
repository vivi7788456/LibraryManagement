package com.mycompany.librarymanagement;

import java.util.Scanner;

public class LibraryManagement {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Khoi tao cac manager dung chung
        BookManager bookManager = new BookManager();
        ReaderManager readerManager = new ReaderManager();
        BorrowManager borrowManager = BorrowManager.getInstance();
        borrowManager.setManagers(bookManager, readerManager); // Lien ket he thong

        int choice;
        do {
            System.out.println("\n===== MENU CHINH =====");
            System.out.println("1. Quan ly Sach (Kho)");
            System.out.println("2. Quan ly Doc gia");
            System.out.println("3. Quan ly Muon / Tra");
            System.out.println("0. Thoat");
            System.out.print("Chon chuc nang (0-3): ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so hop le.");
                choice = -1;
            }

            switch (choice) {
                case 1 -> BookMenu.run(bookManager, sc);
                case 2 -> ReaderMenu.run(readerManager, sc);
                case 3 -> BorrowMenu.run(borrowManager, sc);
                case 0 -> System.out.println("Tam biet!");
                default -> System.out.println("Lua chon khong hop le!");
            }

        } while (choice != 0);

        sc.close();
    }
}
