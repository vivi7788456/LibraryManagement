package com.mycompany.librarymanagement;

public class StudentReader extends Reader {

    public StudentReader(String id, String name, String phone, String address) {
        super(id, name, phone, address);
        this.type = "Sinh vien";
        this.borrowLimit = 5;
        this.depositRate = 0.1; // 10% gia tri sach
    }
}
