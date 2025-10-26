package com.mycompany.librarymanagement;

import java.io.Serializable;

/**
 * Lop cha truu tuong Reader (Doc gia)
 * 
 * Dung de luu thong tin chung cua moi doc gia trong thu vien.
 * Cac lop con cu the (StudentReader, TeacherReader, NormalReader, ...) se ke thua lop nay.
 * 
 * Ap dung:
 * - Tinh truu tuong (abstract)
 * - Tinh dong goi (cac thuoc tinh private/protected)
 * - Tinh ke thua (cac lop con mo rong chuc nang)
 */
public abstract class Reader implements Serializable {

    private static final long serialVersionUID = 1L;

    // ================== THUOC TINH ==================
    protected String id;             // Ma doc gia
    protected String name;           // Ho ten
    protected String phone;          // So dien thoai (10 chu so)
    protected String address;        // Dia chi
    protected int borrowLimit;       // Gioi han muon toi da
    protected int borrowedCount;     // So sach dang muon hien tai
    protected double depositRate;    // Ty le dat coc (theo phan tram)
    protected String type;           // Loai doc gia (Sinh vien, Giao vien, Doc gia thuong,...)
    protected boolean biKhoa;        // Trang thai tai khoan (true = bi khoa, false = binh thuong)


    // ================== CONSTRUCTOR ==================
    /**
     * Khoi tao mot doi tuong Reader co ID, Ten, SDT, Dia chi.
     * @param id Ma doc gia
     * @param name Ten doc gia
     * @param phone So dien thoai (phai 10 chu so)
     * @param address Dia chi
     */
    public Reader(String id, String name, String phone, String address) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID doc gia khong duoc de trong!");
        this.id = id.trim();

        setName(name);
        setPhone(phone);
        setAddress(address);
        this.borrowedCount = 0;
        this.biKhoa = false; // Mac dinh chua bi khoa
    }

    // ================== GETTER & SETTER ==================
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public int getBorrowLimit() { return borrowLimit; }
    public int getBorrowedCount() { return borrowedCount; }
    public double getDepositRate() { return depositRate; }
    public String getType() { return type; }
    public boolean isBiKhoa() { return biKhoa; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Ten doc gia khong duoc de trong!");
        this.name = name.trim();
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}"))
            throw new IllegalArgumentException("So dien thoai phai co 10 chu so!");
        this.phone = phone.trim();
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty())
            throw new IllegalArgumentException("Dia chi khong duoc de trong!");
        this.address = address.trim();
    }

    // ================== CAC HANH VI CHUNG ==================
    /**
     * Kiem tra xem doc gia con duoc phep muon sach khong.
     * @return true neu chua dat gioi han va khong bi khoa
     */
    public boolean canBorrow() {
        return borrowedCount < borrowLimit && !biKhoa;
    }

    /**
     * Dang ky muon them 1 cuon sach.
     * Neu da dat gioi han hoac bi khoa thi nem loi.
     */
    public void borrowOne() {
        if (!canBorrow())
            throw new IllegalStateException("Doc gia da dat gioi han hoac da bi khoa!");
        borrowedCount++;
    }

    /**
     * Tra lai 1 cuon sach.
     * Neu khong con sach nao dang muon thi nem loi.
     */
    public void returnOne() {
        if (borrowedCount <= 0)
            throw new IllegalStateException("Doc gia khong co sach de tra!");
        borrowedCount--;
    }

    /**
     * Khoa tai khoan doc gia (vi du: muon qua han).
     */
    public void khoa() {
        this.biKhoa = true;
    }

    /**
     * Mo khoa tai khoan doc gia (sau khi da tra sach qua han).
     */
    public void moKhoa() {
        this.biKhoa = false;
    }

    // ================== HIEN THI DANG BANG ==================
    @Override
    public String toString() {
        return String.format(
            "| %-6s | %-16s | %-10s | %-10s | %-15s | %-8d | %-4.0f%% | %-10d | %-12s |",
            id, name, phone, address, type, borrowLimit, depositRate * 100,
            borrowedCount, biKhoa ? "Khoa" : "Binh thuong"
        );
    }

    /**
     * In tieu de bang hien thi danh sach doc gia.
     */
    public static void printHeader() {
        System.out.println(String.format(
            "| %-6s | %-16s | %-10s | %-10s | %-15s | %-8s | %-4s | %-10s | %-12s |",
            "ID", "Ten", "SDT", "Dia Chi", "Loai", "Gioi Han", "Coc", "Dang Muon", "Trang Thai"));
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }

    /**
     * In duong ke ket thuc bang hien thi doc gia.
     */
    public static void printFooter() {
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }
}
