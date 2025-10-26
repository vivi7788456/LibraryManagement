package com.mycompany.librarymanagement;

/**
 * Lop NormalReader (Doc gia thuong)
 * 
 * Doi tuong nay la doc gia khong thuoc truong (khach le).
 * Co gioi han muon sach thap hon va ty le dat coc cao hon.
 * 
 * Thuoc tinh duoc ke thua tu lop cha Reader:
 * - id, name, phone, address, type, borrowLimit, depositRate
 */
public class NormalReader extends Reader {

    /**
     * Constructor - khoi tao doi tuong doc gia thuong.
     * @param id Ma doc gia
     * @param name Ho ten
     * @param phone So dien thoai
     * @param address Dia chi
     */
    public NormalReader(String id, String name, String phone, String address) {
        super(id, name, phone, address);
        
        // Xac dinh loai doc gia
        this.type = "Doc gia thuong";

        // Gioi han toi da chi duoc muon 3 quyen sach
        this.borrowLimit = 3;

        // Ty le dat coc la 20% gia tri sach muon
        this.depositRate = 0.2;
    }
}
