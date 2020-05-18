package IDS00;

import config.InfrastructureConfig;
import config.WebConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = InfrastructureConfig.class)
@Transactional
public class SqlInjection {

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void setUp(){
        System.out.println("\n");
    }

    @AfterEach
    public void clear(){
        System.out.println("\n");
    }

    /**
     * Ο κώδικας επιτρέπει sql injection επειδή τα inputs δεν είναι sanitized.
     * Στο παράδειγμα,προσπαθούμε να κάνουμε login με λάθος όνομα χρήστη/κωδικό.
     */
    @Test
    public void sqlInjectionByPassingLoginSystem() throws SQLException {
        String[] credentials = getInputsWithInjection();

        String sql_query = "SELECT 1 FROM CREDENTIALS WHERE username = '" + credentials[0]
                + "' AND password = '" + credentials[1] + "'";

        Connection connection = null;

        try {
            connection = DataSourceUtils.getConnection(dataSource);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql_query);

            if (!resultSet.next()) {
                System.err.println("Failed to login with given credentials: username = "
                        + credentials[0] + ",password = " + credentials[1]);
            }else{
                System.out.println("Successful login with given credentials : username = "
                        + credentials[0] + ",password = " + credentials[1]);
            }

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }


    }

    /**
     * Ο κώδικας επιτρέπει sql injection επειδή τα inputs δεν είναι sanitized.
     * Στο παράδειγμα,προσπαθούμε να τροποιήσουμε την βάση δεδομένων.
     * Το πρόγραμμα :
     * 1)Εμφανίζει τον αριθμό των χρηστών πριν την εκτέλεση του sql query,
     * 2)εκτελή ένα sql query,το οποίο περιέχει injection attack,
     * 3)εμφανίζει ξανά τον αριθμό των χρηστών μετά την εκτέλεση του sql query.
     */
    @Test
    public void sqlInjectionModifyingDatabase() throws SQLException {

        String[] credentials = getInputsWithModification();

        String sql_query = "SELECT 1 FROM CREDENTIALS WHERE username = '" + credentials[0]
                + "' AND password = '" + credentials[1] + "'";

        int numOfClientsAfter = 0, numOfClientsBefore = 0;

        Connection connection = null;

        try {
            connection = DataSourceUtils.getConnection(dataSource);

            ResultSet resultSet = connection.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM CREDENTIALS");

            resultSet.next();
            numOfClientsBefore = resultSet.getInt(1);
            System.out.println("Users in the system before executing sql query : "
                    + numOfClientsBefore);

            connection.createStatement()
                    .executeQuery(sql_query);

            ResultSet resultSet2 = connection.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM CREDENTIALS");
            resultSet2.next();
            numOfClientsAfter = resultSet2.getInt(1);
            System.out.println("Users in the system after executing sql injected query : " + numOfClientsAfter);


        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }

        if (numOfClientsAfter != numOfClientsBefore) {
            System.err.println("The number of clients after running sql changed.");
        }

    }

    /**
     * Η βιβλιοθήκη JDBC προσφέρει API για δημιουργία sql command που κάνουν sanitization
     * τα μη αξιόπιστα δεδομένα.Η class java.sql.PreparedStatement κάνει escape τα δεδομένα εισόδου,
     * αποτρέποτανς sql injections όταν χρησιμοποιούνται σωστά.
     */
    @Test
    public void sqlInjectionSolution() throws SQLException {
        String[] credentials = getInputsWithInjection();

        Connection connection = null;

        try{
            connection = DataSourceUtils.getConnection(dataSource);
            PreparedStatement preparedStatement = connection
                    .prepareStatement
                            ("SELECT 1 FROM CREDENTIALS WHERE username = ? AND password = ?");
            preparedStatement.setString(1, credentials[0]);
            preparedStatement.setString(2, credentials[1]);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.err.println("Failed to login with given credentials: username = "
                        + credentials[0] + ",password = " + credentials[1]);
                return;
            }

            System.out.println("Successful login with given credentials : username = "
                    + credentials[0] + ",password = " + credentials[1]);


        }finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }


    }

    private String[] getInputsWithInjection() {
        String wrongUsername = "1201212";

        String sqlInjection = "a' or '1' = '1";

        return new String[]{wrongUsername, sqlInjection};
    }

    private String[] getInputsWithModification() {
        String wrongUsername = "1201212";

        String sqlInjection = "a';DELETE FROM CREDENTIALS;SELECT 1 = '1";

        return new String[]{wrongUsername, sqlInjection};

    }
}
