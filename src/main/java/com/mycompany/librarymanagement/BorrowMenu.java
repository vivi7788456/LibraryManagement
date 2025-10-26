package com.mycompany.librarymanagement;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Lop BorrowMenu chiu trach nhiem hien thi menu quan ly muon/tra sach.
 * Giao tiep voi nguoi dung qua console va goi cac ham tu BorrowManager.
 * 
 * Chuc nang chinh:
 * 1. Them phieu muon
 * 2. Hien thi danh sach phieu
 * 3. Tim kiem phieu
 * 4. Danh dau da tra
 * 5. Xoa phieu muon
 * 6. Gia han phieu
 * 7. Canh bao qua han
 * 8. Thong ke
 */
public class BorrowMenu {

    /**
     * Ham run() hien thi menu va xu ly lua chon nguoi dung.
     */
    public static void run(BorrowManager bm, Scanner sc) {
        int chon;
        do {
            System.out.println("\n===== QUAN LY MUON / TRA =====");
            System.out.println("1. Them phieu muon");
            System.out.println("2. Hien thi tat ca phieu muon");
            System.out.println("3. Tim kiem phieu muon");
            System.out.println("4. Danh dau da tra sach");
            System.out.println("5. Xoa phieu muon");
            System.out.println("6. Gia han phieu muon");
            System.out.println("7. Canh bao phieu qua han");
            System.out.println("8. Thong ke");
            System.out.println("0. Quay lai");
            System.out.print("Chon (0–8): ");

            try {
                chon = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so hop le");
                chon = -1;
            }

            switch (chon) {
                // =================== 1. THEM PHIEU MUON ===================
                case 1 -> {
                    try {
                        System.out.print("Nhap ma sach: ");
                        String maSach = sc.nextLine().trim();
                        System.out.print("Nhap ma doc gia: ");
                        String maDocGia = sc.nextLine().trim();
                        System.out.print("Nhap ngay muon (yyyy-MM-dd): ");
                        LocalDate ngayMuon = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Nhap ngay phai tra (yyyy-MM-dd): ");
                        LocalDate ngayPhaiTra = LocalDate.parse(sc.nextLine().trim());
                        System.out.print("Nhap so luong sach muon: ");
                        int soLuong = Integer.parseInt(sc.nextLine().trim());

                        bm.themPhieuMuon(maSach, maDocGia, ngayMuon, ngayPhaiTra, soLuong);

                    } catch (DateTimeParseException e) {
                        System.out.println("Loi: Dinh dang ngay khong hop le (dung yyyy-MM-dd)");
                    } catch (NumberFormatException e) {
                        System.out.println("Loi: So luong phai la so nguyen");
                    }
                }

                // =================== 2. HIEN THI TAT CA ===================
                case 2 -> bm.hienThiTatCa();

                // =================== 3. TIM KIEM ===================
                case 3 -> {
                    System.out.print("Nhap tu khoa tim kiem: ");
                    bm.timKiemTongQuat(sc.nextLine());
                }

                // =================== 4. DANH DAU DA TRA ===================
                case 4 -> {
                    System.out.print("Nhap ID phieu muon can danh dau da tra: ");
                    bm.danhDauDaTra(sc.nextLine().trim());
                }

                // =================== 5. XOA PHIEU ===================
                case 5 -> {
                    System.out.print("Nhap ID phieu muon can xoa: ");
                    bm.xoaPhieuMuon(sc.nextLine().trim());
                }

                // =================== 6. GIA HAN PHIEU ===================
                case 6 -> {
                    try {
                        System.out.print("Nhap ID phieu muon can gia han: ");
                        String id = sc.nextLine().trim();
                        System.out.print("Nhap so ngay gia han: ");
                        int days = Integer.parseInt(sc.nextLine().trim());
                        bm.giaHanPhieuMuon(id, days);
                    } catch (NumberFormatException e) {
                        System.out.println("Loi: So ngay khong hop le");
                    }
                }

                // =================== 7. CANH BAO QUA HAN ===================
                case 7 -> bm.canhBaoQuaHan();

                // =================== 8. THONG KE ===================
                case 8 -> bm.thongKe();

                // =================== 0. THOAT ===================
                case 0 -> System.out.println("Da quay lai menu chinh");
                default -> System.out.println("Lua chon khong hop le");
            }

        } while (chon != 0);
    }
}
