package com.mycompany.librarymanagement;

import java.util.Scanner;

public interface IManager {
    boolean add(Object o);      // dùng Object để ReaderManager cũng xài được
    void delete(String id, Scanner sc);
    void update(String id, Scanner sc);
    void search(String keyword);
    void display();
}
//BorrowManager KHÔNG kế thừa IManager