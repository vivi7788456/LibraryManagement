package com.mycompany.librarymanagement;

import java.io.*;
import java.util.*;

/**
 * Lop Database chiu trach nhiem doc va ghi du lieu cho toan bo he thong.
 * 
 * - Du lieu sach (BookManager)       → luu trong file books.dat
 * - Du lieu doc gia (ReaderManager)  → luu trong file readers.dat
 * - Du lieu phieu muon (BorrowManager) → luu trong file borrows.dat
 * 
 * Muc dich: tap trung cac thao tac I/O vao mot noi duy nhat, tranh lap code.
 */
public class Database {

    // ==================== DUONG DAN CAC FILE ====================
    private static final String BOOK_FILE = "books.dat";
    private static final String READER_FILE = "readers.dat";
    private static final String BORROW_FILE = "borrows.dat";

    // ==================== CAC HAM LUU FILE ====================

    /**
     * Luu danh sach sach vao file books.dat
     */
    public static void saveBooks(ArrayList<Book> list) {
        saveObject(BOOK_FILE, list, "Book");
    }

    /**
     * Luu danh sach doc gia vao file readers.dat
     */
    public static void saveReaders(ArrayList<Reader> list) {
        saveObject(READER_FILE, list, "Reader");
    }

    /**
     * Luu danh sach phieu muon vao file borrows.dat
     */
    public static void saveBorrows(ArrayList<BorrowRecord> list) {
        saveObject(BORROW_FILE, list, "BorrowRecord");
    }

    // ==================== CAC HAM DOC FILE ====================

    /**
     * Doc danh sach sach tu file books.dat
     */
    public static ArrayList<Book> loadBooks() {
        return loadObject(BOOK_FILE);
    }

    /**
     * Doc danh sach doc gia tu file readers.dat
     */
    public static ArrayList<Reader> loadReaders() {
        return loadObject(READER_FILE);
    }

    /**
     * Doc danh sach phieu muon tu file borrows.dat
     */
    public static ArrayList<BorrowRecord> loadBorrows() {
        return loadObject(BORROW_FILE);
    }

    // ==================== HAM CHUNG DUNG CHUNG ====================

    /**
     * Ham doc du lieu tong quat (duoc goi boi cac ham loadBooks, loadReaders,...)
     * @param path Duong dan file can doc
     * @return ArrayList du lieu da doc duoc (neu file khong ton tai tra ve list rong)
     */
    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> loadObject(String path) {
        File file = new File(path);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Loi khi doc file " + path + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ham ghi du lieu tong quat (duoc goi boi cac ham saveBooks, saveReaders,...)
     * @param path Duong dan file
     * @param list Danh sach can luu
     * @param label Nhan de hien thong bao
     */
    private static <T> void saveObject(String path, ArrayList<T> list, String label) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(list);
            System.out.println("Da luu danh sach " + label + " vao file: " + path);
        } catch (IOException e) {
            System.err.println("Loi khi ghi file " + label + ": " + e.getMessage());
        }
    }
}
