package model;

import java.util.Objects;

public class Ticket {
//    CREATE TABLE ticket (
//            id SERIAL PRIMARY KEY,
//            session_id INT NOT NULL,
//            row INT NOT NULL,
//            cell INT NOT NULL,
//            account_id INT NOT NULL REFERENCES account(id)
//);
    private  int id;
    private  int price;
    private  int row;
    private  int cell;
    private  int account_id;

    public Ticket(int id, int price, int row, int cell, int account_id) {
        this.id = id;
        this.price = price;
        this.row = row;
        this.cell = cell;
        this.account_id = account_id;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && price == ticket.price && row == ticket.row && cell == ticket.cell && account_id == ticket.account_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, row, cell, account_id);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", session_id=" + price +
                ", row=" + row +
                ", cell=" + cell +
                ", account_id=" + account_id +
                '}';
    }
}
