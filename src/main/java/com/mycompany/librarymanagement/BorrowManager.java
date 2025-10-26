package com.mycompany.librarymanagement;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Lop BorrowManager quan ly cac phieu muon sach trong he thong.
 * Ap dung mo hinh Singleton (chi ton tai 1 instance duy nhat).
 * 
 * Chuc nang chinh:
 * - Them, xoa, gia han, danh dau tra sach
 * - Cap nhat kho sach khi muon / tra
 * - Rang buoc: gioi han 30 ngay muon, khong muon khi het sach, khong muon khi bi khoa
 * - Khoa doc gia neu qua han va tu dong mo khoa khi tra het
 * - Luu tru du lieu qua lop Database
 */
public class BorrowManager {

    // ================= SINGLETON =================
    private static BorrowManager instance;

    public static BorrowManager getInstance() {
        if (instance == null) instance = new BorrowManager();
        return instance;
    }

    // ================= THUOC TINH =================
    private static final int THOI_GIAN_MUON_TOI_DA_NGAY = 30; // 30 ngay toi da
    private final List<BorrowRecord> danhSachPhieuMuon;        // Danh sach phieu muon
    private ReaderManager readerManager;                       // Lien ket doc gia
    private BookManager bookManager;                           // Lien ket sach
    private final Scanner sc = new Scanner(System.in);          // Dung cho nhap console

    // ================= CONSTRUCTOR =================
    private BorrowManager() {
        // Doc du lieu phieu muon tu file
        this.danhSachPhieuMuon = new ArrayList<>(Database.loadBorrows());
        fixBorrowRecordIdCounter(this.danhSachPhieuMuon);

        // Neu main khong truyen vao thi tu tao moi
        this.readerManager = new ReaderManager();
        this.bookManager = new BookManager();
    }

    /**
     * Tiem (gan) san cac manager da co tu ben ngoai
     */
    public void setManagers(BookManager bm, ReaderManager rm) {
        if (bm != null) this.bookManager = bm;
        if (rm != null) this.readerManager = rm;
    }

    // ================= CAC HAM NOI BO =================

    /** 
     * Cap nhat lai bien dem ID phieu muon sau khi doc du lieu tu file
     */
    private static void fixBorrowRecordIdCounter(List<BorrowRecord> list) {
        int max = 0;
        for (BorrowRecord p : list) {
            try {
                int n = Integer.parseInt(p.getRecordID().substring(2));
                if (n > max) max = n;
            } catch (Exception ignored) {}
        }
        try {
            Field f = BorrowRecord.class.getDeclaredField("idCounter");
            f.setAccessible(true);
            f.set(null, max);
        } catch (Exception ignored) {}
    }

    /** Tim phieu muon theo ma ID */
    private BorrowRecord timRecordBangID(String recordID) {
        for (BorrowRecord p : danhSachPhieuMuon) {
            if (p.getRecordID().equalsIgnoreCase(recordID)) return p;
        }
        return null;
    }

    /** Dem tong so sach ma mot doc gia dang muon */
    private int demSoSachDangMuon(String maDocGia) {
        int tong = 0;
        for (BorrowRecord p : danhSachPhieuMuon) {
            if (!p.isDaTra() && p.getMaDocGia().equalsIgnoreCase(maDocGia)) {
                tong += p.getSoLuong();
            }
        }
        return tong;
    }

    /** Luu danh sach phieu muon vao file */
    private void save() {
        Database.saveBorrows(new ArrayList<>(danhSachPhieuMuon));
    }

    // ==================================================
    // ================ CAC CHUC NANG CHINH ==============
    // ==================================================

    /**
     * Them phieu muon (su dung tham so, dung cho goi tu menu)
     */
    public void themPhieuMuon(String maSach, String maDocGia, LocalDate ngayMuon, LocalDate ngayPhaiTra, int soLuong) {
        // Kiem tra hop le
        if (maSach == null || maSach.isBlank() || maDocGia == null || maDocGia.isBlank()) {
            System.err.println("Loi: Ma sach hoac ma doc gia khong duoc de trong");
            return;
        }
        if (soLuong <= 0) {
            System.err.println("Loi: So luong phai lon hon 0");
            return;
        }
        if (ngayPhaiTra.isBefore(ngayMuon)) {
            System.err.println("Loi: Ngay phai tra phai >= ngay muon");
            return;
        }

        long tongNgay = ChronoUnit.DAYS.between(ngayMuon, ngayPhaiTra);
        if (tongNgay > THOI_GIAN_MUON_TOI_DA_NGAY) {
            System.err.println("Loi: Tong so ngay muon (" + tongNgay + ") vuot " + THOI_GIAN_MUON_TOI_DA_NGAY);
            return;
        }

        // Kiem tra ton tai doc gia va sach
        Reader docGia = readerManager.findById(maDocGia);
        if (docGia == null) {
            System.err.println("Loi: Ma doc gia khong ton tai");
            return;
        }
        if (docGia.isBiKhoa()) {
            System.err.println("Doc gia [" + docGia.getName() + "] dang bi khoa, khong duoc muon sach");
            return;
        }

        Book sach = bookManager.findBookById(maSach);
        if (sach == null) {
            System.err.println("Loi: Ma sach khong ton tai");
            return;
        }

        // Rang buoc: so luong, gioi han, trung phieu
        if (sach.getSoLuong() < soLuong) {
            System.err.println("Loi: Khong du sach trong kho (con " + sach.getSoLuong() + ")");
            return;
        }
        int dangMuon = demSoSachDangMuon(maDocGia);
        if (dangMuon + soLuong > docGia.getBorrowLimit()) {
            System.err.println("Loi: Vuot gioi han muon sach (gioi han " + docGia.getBorrowLimit() + ")");
            return;
        }
        for (BorrowRecord p : danhSachPhieuMuon) {
            if (!p.isDaTra() && p.getMaDocGia().equalsIgnoreCase(maDocGia)
                    && p.getMaSach().equalsIgnoreCase(maSach)) {
                System.err.println("Loi: Da ton tai phieu muon chua tra cho sach nay");
                return;
            }
        }

        // Tao phieu va cap nhat kho
        BorrowRecord phieu = new BorrowRecord(maSach, maDocGia, ngayMuon, ngayPhaiTra, soLuong);
        try {
            sach.decreaseSoLuong(soLuong);
            danhSachPhieuMuon.add(phieu);
            Database.saveBooks(bookManager.getList());
            save();
            System.out.println("Da them phieu thanh cong. ID: " + phieu.getRecordID());
        } catch (IllegalStateException e) {
            System.err.println("Loi: " + e.getMessage());
        }
    }

    /**
     * Them phieu muon bang cach nhap tu ban phim (demo console)
     */
    public void themPhieuMuon() {
        System.out.println("\n=== THEM PHIEU MUON ===");
        System.out.print("Nhap ma sach: ");
        String maSach = sc.nextLine().trim();
        System.out.print("Nhap ma doc gia: ");
        String maDocGia = sc.nextLine().trim();

        int soLuong;
        try {
            System.out.print("Nhap so luong muon: ");
            soLuong = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Loi: So luong khong hop le");
            return;
        }

        int soNgay;
        try {
            System.out.print("Nhap so ngay muon (toi da " + THOI_GIAN_MUON_TOI_DA_NGAY + "): ");
            soNgay = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("Loi: So ngay khong hop le");
            return;
        }
        if (soNgay <= 0 || soNgay > THOI_GIAN_MUON_TOI_DA_NGAY) {
            System.err.println("Loi: So ngay phai trong (1.." + THOI_GIAN_MUON_TOI_DA_NGAY + ")");
            return;
        }

        LocalDate ngayMuon = LocalDate.now();
        LocalDate ngayPhaiTra = ngayMuon.plusDays(soNgay);
        themPhieuMuon(maSach, maDocGia, ngayMuon, ngayPhaiTra, soLuong);
    }

    /**
     * Danh dau phieu da tra + cap nhat kho + mo khoa doc gia neu can
     */
    public void danhDauDaTra(String recordID) {
        BorrowRecord p = timRecordBangID(recordID);
        if (p == null) {
            System.err.println("Khong tim thay phieu: " + recordID);
            return;
        }
        if (p.isDaTra()) {
            System.out.println("Phieu da duoc danh dau la da tra truoc do");
            return;
        }

        p.setDaTra(true);

        // Tra sach vao kho
        Book sach = bookManager.findBookById(p.getMaSach());
        if (sach != null) {
            sach.increaseSoLuong(p.getSoLuong());
            Database.saveBooks(bookManager.getList());
            System.out.println("Da hoan kho " + p.getSoLuong() + " cuon");
        }

        // Mo khoa doc gia neu khong con phieu qua han
        String maDG = p.getMaDocGia();
        LocalDate homNay = LocalDate.now();
        boolean conQuaHan = danhSachPhieuMuon.stream()
            .anyMatch(r -> !r.isDaTra() && r.getMaDocGia().equalsIgnoreCase(maDG)
                    && r.getNgayPhaiTra().isBefore(homNay));

        if (!conQuaHan) {
            Reader dg = readerManager.findById(maDG);
            if (dg != null && dg.isBiKhoa()) {
                dg.moKhoa();
                readerManager.luuVaoFile();
                System.out.println("Da mo khoa doc gia " + dg.getName());
            }
        }

        save();
        System.out.println("Da danh dau phieu [" + recordID + "] la da tra");
    }

    /**
     * Xoa phieu muon (chi cho phep xoa khi da tra)
     */
    public void xoaPhieuMuon(String recordID) {
        BorrowRecord p = timRecordBangID(recordID);
        if (p == null) {
            System.err.println("Khong tim thay phieu: " + recordID);
            return;
        }
        if (!p.isDaTra()) {
            System.err.println("Khong the xoa phieu chua tra");
            return;
        }
        danhSachPhieuMuon.remove(p);
        save();
        System.out.println("Da xoa phieu " + recordID);
    }

    /**
     * Gia han phieu muon, tong thoi gian khong vuot 30 ngay
     */
    public void giaHanPhieuMuon(String recordID, int soNgayThem) {
        if (soNgayThem <= 0) {
            System.err.println("So ngay gia han phai > 0");
            return;
        }
        BorrowRecord p = timRecordBangID(recordID);
        if (p == null) {
            System.err.println("Khong tim thay phieu: " + recordID);
            return;
        }
        if (p.isDaTra()) {
            System.err.println("Phieu da tra, khong the gia han");
            return;
        }

        LocalDate moi = p.getNgayPhaiTra().plusDays(soNgayThem);
        long tongNgay = ChronoUnit.DAYS.between(p.getNgayMuon(), moi);
        if (tongNgay > THOI_GIAN_MUON_TOI_DA_NGAY) {
            System.err.println("Tong thoi gian muon (" + tongNgay + " ngay) vuot " + THOI_GIAN_MUON_TOI_DA_NGAY);
            return;
        }

        p.setNgayPhaiTra(moi);
        save();
        System.out.println("Gia han thanh cong. Han moi: " + moi);
    }

    /**
     * Kiem tra cac phieu qua han va khoa doc gia neu can
     */
    public void canhBaoQuaHan() {
        LocalDate homNay = LocalDate.now();
        boolean coQuaHan = false;

        System.out.println("\n=== CANH BAO CAC PHIEU MUON QUA HAN ===");
        for (BorrowRecord p : danhSachPhieuMuon) {
            if (!p.isDaTra() && p.getNgayPhaiTra().isBefore(homNay)) {
                coQuaHan = true;
                long tre = ChronoUnit.DAYS.between(p.getNgayPhaiTra(), homNay);
                System.out.println("Phieu " + p.getRecordID() + " | DG: " + p.getMaDocGia() + " | Sach: "
                        + p.getMaSach() + " | Tre: " + tre + " ngay");

                Reader dg = readerManager.findById(p.getMaDocGia());
                if (dg != null && !dg.isBiKhoa()) {
                    dg.khoa();
                    System.out.println("Da khoa doc gia [" + dg.getName() + "] vi qua han");
                }
            }
        }
        if (coQuaHan) readerManager.luuVaoFile();
        if (!coQuaHan) System.out.println("Khong co phieu muon nao bi qua han");
    }

    /**
     * Thong ke tong so phieu, da tra, chua tra, qua han
     */
    public void thongKe() {
        long tong = danhSachPhieuMuon.size();
        long daTra = danhSachPhieuMuon.stream().filter(BorrowRecord::isDaTra).count();
        long quaHan = danhSachPhieuMuon.stream()
                .filter(p -> !p.isDaTra() && p.getNgayPhaiTra().isBefore(LocalDate.now())).count();

        System.out.println("\n===== THONG KE PHIEU MUON =====");
        System.out.println("Tong phieu: " + tong);
        System.out.println("Da tra    : " + daTra);
        System.out.println("Chua tra  : " + (tong - daTra));
        System.out.println("Qua han   : " + quaHan);
    }

    /**
     * Hien thi tat ca phieu muon
     */
    public void hienThiTatCa() {
        if (danhSachPhieuMuon.isEmpty()) {
            System.out.println("Chua co phieu muon nao");
            return;
        }
        System.out.println("\n=== DANH SACH PHIEU MUON ===");
        danhSachPhieuMuon.sort(Comparator.comparing(BorrowRecord::getNgayMuon).reversed());
        for (BorrowRecord p : danhSachPhieuMuon) System.out.println(p);
    }

    /**
     * Tim kiem phieu muon theo tu khoa
     */
    public void timKiemTongQuat(String tuKhoa) {
        tuKhoa = (tuKhoa == null) ? "" : tuKhoa.toLowerCase();
        boolean timThay = false;

        for (BorrowRecord p : danhSachPhieuMuon) {
            if (p.getRecordID().toLowerCase().contains(tuKhoa)
                    || p.getMaSach().toLowerCase().contains(tuKhoa)
                    || p.getMaDocGia().toLowerCase().contains(tuKhoa)
                    || p.getNgayMuon().toString().contains(tuKhoa)
                    || p.getNgayPhaiTra().toString().contains(tuKhoa)) {
                System.out.println(p);
                timThay = true;
            }
        }
        if (!timThay) System.out.println("Khong tim thay phieu muon phu hop");
    }

    /**
     * Kiem tra sach co dang duoc muon khong (phuc vu BookManager)
     */
    public boolean isBookBeingBorrowed(String bookId) {
        for (BorrowRecord p : danhSachPhieuMuon) {
            if (!p.isDaTra() && p.getMaSach().equalsIgnoreCase(bookId)) return true;
        }
        return false;
    }
}
