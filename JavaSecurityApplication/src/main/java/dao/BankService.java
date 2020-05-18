package dao;

public interface BankService {

    long getAmount(String username);

    void transfer(String from, String to, long amount);

    boolean accountExists(String username);
}
