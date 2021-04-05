package Store;

import model.Account;
import model.Ticket;

import java.util.Collection;

public interface Store {

    Ticket createHall(Ticket ticket);

    Account createAccount(Account account);

    Collection<Ticket> findSeatsFromHalls();

}
