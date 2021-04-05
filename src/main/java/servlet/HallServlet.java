package servlet;

import Store.PSQLStore;
import com.google.gson.Gson;
import model.Account;
import model.Ticket;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class HallServlet extends HttpServlet {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HallServlet.class.getName());


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.debug("in doGet ");
        List<Ticket> list = new ArrayList<>(PSQLStore.instOf().findSeatsFromHalls());
        String json = new Gson().toJson(list);
        LOG.debug(json);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        out.println(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String userName = req.getParameter("name");
        LOG.info("Servlet doPost: userName - " + userName);
        String phone = req.getParameter("phone");
        LOG.info("Servlet doPost: phone - " + phone);
        String email = req.getParameter("email");
        LOG.info("Servlet doPost: email - " + email);
        String row = req.getParameter("row");
        LOG.info("Servlet doPost: row - : " + row);
        String cell = req.getParameter("cell");
        LOG.info("Servlet doPost: cell - : " + cell);
        String sum = req.getParameter("sum");
        LOG.info("Servlet doPost: sum - : " + sum);
        Account account = PSQLStore.instOf().createAccount(
                new Account(0, userName, phone, email)
        );

        Ticket ticket = PSQLStore.instOf().createHall(new Ticket(0, Integer.parseInt(sum),
                                                                        Integer.parseInt(row),
                                                                        Integer.parseInt(cell),
                                                                        account.getId()));
        LOG.debug("Account id is" + account.getId());

    }
}