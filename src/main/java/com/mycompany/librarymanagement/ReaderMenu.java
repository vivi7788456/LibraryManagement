package com.mycompany.librarymanagement;

import java.util.Scanner;

public class ReaderMenu {

    public static void run(ReaderManager rm, Scanner sc) {
        int choice;
        do {
            System.out.println("\n===== QUAN LY DOC GIA =====");
            System.out.println("1. Them doc gia moi");
            System.out.println("2. Xoa doc gia");
            System.out.println("3. Sua thong tin doc gia");
            System.out.println("4. Tim kiem doc gia");
            System.out.println("5. Hien thi danh sach doc gia");
            System.out.println("6. Them du lieu mau");
            System.out.println("0. Quay lai");
            System.out.print("Chon (0–6): ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so hop le.");
                choice = -1;
            }

            switch (choice) {
                case 1 -> rm.add(sc);
                case 2 -> {
                    System.out.print("Nhap ID can xoa: ");
                    rm.delete(sc.nextLine(), sc);
                }
                case 3 -> {
                    System.out.print("Nhap ID can sua: ");
                    rm.update(sc.nextLine(), sc);
                }
                case 4 -> {
                    System.out.print("Nhap tu khoa: ");
                    rm.search(sc.nextLine());
                }
                case 5 -> rm.display();
                case 6 -> {
                    rm.getList().clear();
                    rm.add(new StudentReader("DG01", "Nguyen Van A", "0901111111", "HCM"));
                    rm.add(new TeacherReader("DG02", "Tran Thi B", "0902222222", "HN"));
                    rm.add(new NormalReader("DG03", "Le Van C", "0903333333", "DN"));
                    System.out.println("==> Da them du lieu mau thanh cong! <==");
                    rm.display();
                }
                case 0 -> System.out.println("Da quay lai menu chinh.");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }
}
