package com.mycompany.librarymanagement;

import java.util.*;

/**
 * Lop ReaderManager
 * Quan ly toan bo doc gia cua thu vien.
 * 
 * Chuc nang chinh:
 * - Them, xoa, sua, tim kiem, hien thi danh sach doc gia
 * - Khoa va mo khoa tai khoan doc gia (neu vi pham hoac qua han)
 * - Luu va doc du lieu tu file (thong qua lop Database)
 * 
 * Ap dung:
 * - Dong goi (encapsulation)
 * - Da hinh (thuc thi interface IManager)
 * - Lien ket voi lop Reader (lop cha cua cac loai doc gia)
 */
public class ReaderManager implements IManager {

    private ArrayList<Reader> list = new ArrayList<>();
    private static int totalReader = 0;

    // ====== CONSTRUCTOR ======
    public ReaderManager() {
        list = Database.loadReaders(); // Doc du lieu tu file
        totalReader = list.size();
    }

    // =====================================================
    // ================== CAC HAM HO TRO ===================
    // =====================================================

    /** Tim doc gia theo ma ID */
    public Reader findReaderById(String id) {
        if (id == null || id.isEmpty()) return null;
        for (Reader r : list) {
            if (r.getId().equalsIgnoreCase(id)) return r;
        }
        return null;
    }

    /** In tieu de bang hien thi */
    private void printHeader() { Reader.printHeader(); }

    /** In duong ke ket thuc bang hien thi */
    private void printFooter() { Reader.printFooter(); }

    // =====================================================
    // ============ CAC HAM KHOA / MO KHOA ================
    // =====================================================

    /** Khoa tai khoan doc gia (vi du: muon sach qua han) */
    public void khoaDocGia(String maDocGia) {
        Reader r = findReaderById(maDocGia);
        if (r == null) {
            System.err.println("Khong tim thay doc gia co ID: " + maDocGia);
            return;
        }
        r.khoa();
        System.out.println("Da khoa doc gia [" + r.getName() + "]");
        Database.saveReaders(list);
    }

    /** Mo khoa tai khoan doc gia */
    public void moKhoaDocGia(String maDocGia) {
        Reader r = findReaderById(maDocGia);
        if (r == null) {
            System.err.println("Khong tim thay doc gia co ID: " + maDocGia);
            return;
        }
        r.moKhoa();
        System.out.println("Da mo khoa doc gia [" + r.getName() + "]");
        Database.saveReaders(list);
    }

    // =====================================================
    // ================ TRIEN KHAI IMANAGER ================
    // =====================================================

    /** Them moi doc gia bang doi tuong */
    @Override
    public boolean add(Object o) {
        if (!(o instanceof Reader)) {
            System.out.println("Loi: Doi tuong khong phai Reader!");
            return false;
        }
        Reader r = (Reader) o;
        if (findReaderById(r.getId()) != null) {
            System.out.println("Loi: Ma doc gia '" + r.getId() + "' da ton tai!");
            return false;
        }
        list.add(r);
        totalReader++;
        Database.saveReaders(list);
        return true;
    }

    /** Them doc gia moi tu console (nhap bang tay) */
    public void add(Scanner sc) {
        System.out.println("\n=== THEM DOC GIA ===");
        String id, name, phone, address;

        // --- Nhap ma doc gia ---
        while (true) {
            System.out.print("Nhap ma doc gia: ");
            id = sc.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("Loi: Ma khong duoc trong!");
                continue;
            }
            if (findReaderById(id) != null) {
                System.out.println("Loi: Ma da ton tai!");
                continue;
            }
            break;
        }

        // --- Nhap ten doc gia ---
        while (true) {
            System.out.print("Nhap ten doc gia: ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Loi: Ten khong duoc trong!");
                continue;
            }
            break;
        }

        // --- Nhap so dien thoai ---
        while (true) {
            System.out.print("Nhap SDT (10 so): ");
            phone = sc.nextLine().trim();
            if (!phone.matches("\\d{10}")) {
                System.out.println("Loi: SDT phai co 10 chu so!");
                continue;
            }
            break;
        }

        // --- Nhap dia chi ---
        while (true) {
            System.out.print("Nhap dia chi: ");
            address = sc.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Loi: Dia chi khong duoc trong!");
                continue;
            }
            break;
        }

        // --- Chon loai doc gia ---
        Reader r = null;
        while (r == null) {
            try {
                System.out.println("Chon loai doc gia:");
                System.out.println("1. Sinh vien (gioi han 5 sach, coc 10%)");
                System.out.println("2. Giao vien (gioi han 10 sach, khong coc)");
                System.out.println("3. Doc gia thuong (gioi han 3 sach, coc 20%)");
                System.out.print("Nhap lua chon (1-3): ");
                int loai = Integer.parseInt(sc.nextLine().trim());
                switch (loai) {
                    case 1 -> r = new StudentReader(id, name, phone, address);
                    case 2 -> r = new TeacherReader(id, name, phone, address);
                    case 3 -> r = new NormalReader(id, name, phone, address);
                    default -> System.out.println("Lua chon khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Nhap sai dinh dang so!");
            }
        }

        list.add(r);
        totalReader++;
        Database.saveReaders(list);
        System.out.println("==> Them doc gia thanh cong! <==");
    }

    /** Xoa doc gia theo ID */
    @Override
    public void delete(String id, Scanner sc) {
        Reader r = findReaderById(id);
        if (r == null) {
            System.out.println("Loi: Khong tim thay doc gia!");
            return;
        }

        // Neu dang muon sach thi canh bao
        if (r.getBorrowedCount() > 0) {
            System.out.println("CANH BAO: Doc gia nay dang muon " + r.getBorrowedCount() + " sach!");
            System.out.print("Ban co chac chan muon xoa? (1: Co / 0: Khong): ");
            String choice = sc.nextLine().trim();
            if (!choice.equals("1")) {
                System.out.println("Da huy thao tac xoa.");
                return;
            }
        }

        list.remove(r);
        totalReader--;
        Database.saveReaders(list);
        System.out.println("==> Da xoa doc gia thanh cong! <==");
    }

    /** Cap nhat thong tin doc gia */
    @Override
    public void update(String id, Scanner sc) {
        Reader r = findReaderById(id);
        if (r == null) {
            System.out.println("Loi: Khong tim thay doc gia!");
            return;
        }

        // Luu lai du lieu cu de rollback neu loi
        String oldName = r.getName();
        String oldPhone = r.getPhone();
        String oldAddr = r.getAddress();

        try {
            System.out.println("\n=== CAP NHAT DOC GIA ===");
            System.out.print("Ten moi (bo trong neu giu nguyen): ");
            String newName = sc.nextLine().trim();
            if (!newName.isEmpty()) r.setName(newName);

            System.out.print("SDT moi (bo trong neu giu nguyen): ");
            String newPhone = sc.nextLine().trim();
            if (!newPhone.isEmpty()) {
                if (!newPhone.matches("\\d{10}"))
                    throw new IllegalArgumentException("SDT phai co 10 chu so!");
                r.setPhone(newPhone);
            }

            System.out.print("Dia chi moi (bo trong neu giu nguyen): ");
            String newAddr = sc.nextLine().trim();
            if (!newAddr.isEmpty()) r.setAddress(newAddr);

            Database.saveReaders(list);
            System.out.println("==> Cap nhat thong tin thanh cong! <==");
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage() + " Du lieu giu nguyen.");
            r.setName(oldName);
            r.setPhone(oldPhone);
            r.setAddress(oldAddr);
        }
    }

    /** Tim kiem doc gia theo tu khoa */
    @Override
    public void search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Tu khoa khong duoc trong!");
            return;
        }

        String key = keyword.toLowerCase();
        boolean found = false;
        printHeader();
        for (Reader r : list) {
            if (r.getId().toLowerCase().contains(key)
                    || r.getName().toLowerCase().contains(key)
                    || r.getPhone().toLowerCase().contains(key)
                    || r.getAddress().toLowerCase().contains(key)
                    || r.getType().toLowerCase().contains(key)) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay doc gia phu hop!");
        else printFooter();
    }

    /** Hien thi toan bo danh sach doc gia */
    @Override
    public void display() {
        if (list.isEmpty()) {
            System.out.println("Danh sach doc gia trong!");
            return;
        }

        System.out.println("\n=== DANH SACH DOC GIA ===");
        printHeader();
        for (Reader r : list) System.out.println(r);
        printFooter();
        System.out.println("Tong so doc gia: " + list.size());
    }

    // =====================================================
    // ================= CAC HAM PHU TRO ==================
    // =====================================================

    public ArrayList<Reader> getList() { return list; }

    public Reader findById(String id) { return findReaderById(id); }

    public void luuVaoFile() { Database.saveReaders(list); }
}
