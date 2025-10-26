package com.mycompany.librarymanagement;

public class TeacherReader extends Reader {

    public TeacherReader(String id, String name, String phone, String address) {
        super(id, name, phone, address);
        this.type = "Giao vien";
        this.borrowLimit = 10;
        this.depositRate = 0.0; // Khong can coc
    }
}
