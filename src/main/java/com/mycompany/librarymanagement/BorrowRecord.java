package com.mycompany.librarymanagement;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Lop BorrowRecord luu thong tin cua mot phieu muon sach.
 * - Tu dong sinh ID theo dang "BR001", "BR002", ...
 * - Co tinh nang canh bao qua han tu dong khi in thong tin.
 * 
 * Ap dung tinh dong goi va truu tuong hoa du lieu muon/tra sach.
 */
public class BorrowRecord implements Serializable {
    private static int idCounter = 0;   // Dem tong so phieu muon da tao

    private final String recordID;      // Ma phieu muon
    private String maSach;              // Ma sach duoc muon
    private String maDocGia;            // Ma doc gia muon sach
    private LocalDate ngayMuon;         // Ngay muon
    private LocalDate ngayPhaiTra;      // Ngay phai tra
    private int soLuong;                // So luong sach duoc muon
    private boolean daTra;              // Trang thai: da tra hay chua

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =====================================================
    // ================ TAO ID TU DONG =====================
    // =====================================================

    /**
     * Sinh ID tu dong tang dan theo dang "BR001", "BR002", ...
     * Dong bo (synchronized) de tranh trung lap ID khi ghi file.
     */
    private static synchronized String createRecordID() {
        idCounter++;
        return "BR" + String.format("%03d", idCounter);
    }

    // =====================================================
    // ================= CONSTRUCTOR =======================
    // =====================================================

    /**
     * Tao phieu muon moi voi thong tin sach, doc gia, ngay va so luong.
     */
    public BorrowRecord(String maSach, String maDocGia, LocalDate ngayMuon, LocalDate ngayPhaiTra, int soLuong) {
        this.recordID = createRecordID();
        this.maSach = maSach;
        this.maDocGia = maDocGia;
        this.ngayMuon = ngayMuon;
        this.ngayPhaiTra = ngayPhaiTra;
        this.soLuong = soLuong;
        this.daTra = false; // Mac dinh la chua tra
    }

    // =====================================================
    // ================ GETTER / SETTER ====================
    // =====================================================

    public String getRecordID() { return recordID; }

    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }

    public String getMaDocGia() { return maDocGia; }
    public void setMaDocGia(String maDocGia) { this.maDocGia = maDocGia; }

    public LocalDate getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(LocalDate ngayMuon) { this.ngayMuon = ngayMuon; }

    public LocalDate getNgayPhaiTra() { return ngayPhaiTra; }
    public void setNgayPhaiTra(LocalDate ngayPhaiTra) { this.ngayPhaiTra = ngayPhaiTra; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public boolean isDaTra() { return daTra; }
    public void setDaTra(boolean daTra) { this.daTra = daTra; }

    // =====================================================
    // ================= HAM HO TRO ========================
    // =====================================================

    /**
     * Kiem tra xem phieu nay co dang bi qua han khong.
     */
    public boolean isQuaHan() {
        return !daTra && LocalDate.now().isAfter(ngayPhaiTra);
    }

    /**
     * Tra ve gia tri hien tai cua bien dem idCounter.
     */
    public static int getIdCounter() {
        return idCounter;
    }

    /**
     * Dat lai gia tri bien dem idCounter (phuc vu khi doc du lieu tu file).
     */
    public static void setIdCounter(int value) {
        idCounter = value;
    }

    // =====================================================
    // ================= OVERRIDE HAM CO SAN ===============
    // =====================================================

    /**
     * Hai phieu duoc xem la giong nhau neu trung ma recordID.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BorrowRecord that = (BorrowRecord) obj;
        return recordID.equalsIgnoreCase(that.recordID);
    }

    /**
     * Tra ve ma băm dua tren recordID (phuc vu cho tap hop du lieu).
     */
    @Override
    public int hashCode() {
        return recordID.toLowerCase().hashCode();
    }

    /**
     * Hien thi thong tin phieu muon duoi dang chuoi.
     * Tu dong canh bao neu sach qua han.
     */
    @Override
    public String toString() {
        String trangThai;
        if (daTra) trangThai = "Da tra";
        else if (isQuaHan()) trangThai = "Qua han";
        else trangThai = "Chua tra";

        return String.format(
            "ID: %-5s | Ma sach: %-8s | Ma doc gia: %-8s | Ngay muon: %-10s | Ngay phai tra: %-10s | SL: %-2d | Trang thai: %s",
            recordID,
            maSach,
            maDocGia,
            ngayMuon.format(dtf),
            ngayPhaiTra.format(dtf),
            soLuong,
            trangThai
        );
    }
}
