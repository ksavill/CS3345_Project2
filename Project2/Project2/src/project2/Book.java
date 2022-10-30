/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.util.ArrayList;
import java.util.Scanner;


public class Book implements Comparable<Book>{
    String isbn;
    String title;
    String author;
    
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
    
    @Override
    public int compareTo(Book other) {
        return this.isbn.compareTo(other.isbn);
    }
    
    @Override
    public String toString() {
        return isbn;
    }
    
    public static String insertBooks(String bookString) {
        String results;
        results = "";
        AVLTree<Book> tree = new AVLTree<>();
        tree.resetResults();
        Scanner scanner = new Scanner(bookString);
        while (scanner.hasNextLine()) {
            String isbn = scanner.nextLine();
            String title = scanner.nextLine();
            String author = scanner.nextLine();
            Book book = new Book(isbn, title, author);
            
            tree.insert(book);
//            System.out.println("Book Inserted."); //debugging
            
        }
        results = tree.getResults();
        return results;
    }
}
