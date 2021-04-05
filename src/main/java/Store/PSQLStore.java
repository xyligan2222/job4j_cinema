package Store;

import model.Account;
import model.Ticket;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PSQLStore implements Store{

    private final BasicDataSource pool = new BasicDataSource();

    private static final Logger LOG = LoggerFactory.getLogger(PSQLStore.class.getName());

     /*
     This method connects to the Postgres database
      */

    private PSQLStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader(getClass().getClassLoader()
                        .getResource("db.properties").getFile())
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        LOG.info((cfg.getProperty("jdbc.driver")));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        LOG.info((cfg.getProperty("jdbc.url")));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final PSQLStore INST = new PSQLStore();

    }

    public static PSQLStore instOf() {
        return Lazy.INST;
    }

    @Override
    public Ticket createHall(Ticket ticket) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO ticket ( price, row , cell, account_id ) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getPrice());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getAccount_id());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOG.error("Неверный SQL запрос, Билеты не добавлены в Базу Данных");

        }
        return ticket;
    }

    @Override
    public Account createAccount(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO account ( username, email , phone) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    account.setId(id.getInt(1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOG.error("Неверный SQL запрос, Аккаунт не добавлены в Базу Данных");

        }
        return account;
    }

    @Override
    public Collection<Ticket> findSeatsFromHalls() {
        List<Ticket> ticket = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM ticket ")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    ticket.add(new Ticket(it.getInt("id"),
                                        it.getInt("price"),
                                        it.getInt("row"),
                                        it.getInt("cell"),
                                        it.getInt("account_id")));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOG.error("Неверный SQL запрос, Места в зале не найдены");
        }
        return ticket;
    }

}
