package org.lanitok;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


@Listeners(BaseTest.MethodOrder.class)
@Feature("Тестовое задание по SQL(PostgreSQL)")
public class SQLTest extends BaseTest {

    public Statement statement;
    public Connection connection;
    public ResultSet table;
    public String res;
    public StringBuilder sb;
    ArrayList<String> arr = new ArrayList<>();
    @BeforeClass(description = "Загрузка ответов")
    public void ans(){
        loadProperties("answer.properties");
    }

    @BeforeClass(description = "Загрузка файла sql-base.properties")
    public void init() {
        // loadProperties("regexp-basic-auto.properties");
        loadProperties("sql-base.properties");
    }
    @BeforeClass(description = "Подключение к БД")
    public void setUp() throws Exception {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://workbench.lanit.ru:55432/postgres";

        // Создание свойств соединения с базой данных
        Properties authorization = new Properties();
        authorization.put("user", "lanit"); // Зададим имя пользователя БД
        authorization.put("password", "lanit"); // Зададим пароль доступа в БД

        // Создание соединения с базой данных
        connection = DriverManager.getConnection(url, authorization);

        // Создание оператора доступа к базе данных
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        //executeQuery(statement);
    }

    public void takeQuery(String input) throws SQLException {
        // Выполнение запроса к базе данных, получение набора данных
        table = statement.executeQuery(input);
    }
    public void data () throws SQLException{
        table.getMetaData().getColumnName(table.getMetaData().getColumnCount());

        table.beforeFirst(); // Выведем записи таблицы
        while (table.next()) {
            for (int j = 1; j <= table.getMetaData().getColumnCount(); j++) {
                //System.out.print(table.getString(j) + "\t\t");
                arr.add(table.getString(j));
            }
            sb = new StringBuilder();
            for (Object o : arr) {
                sb.append(o);
                res = sb.toString();
            }
        }
        // Закрытие набора данных
        if (table != null) {
            table.close();
        }
        if (statement != null) {
            statement.close();
        } // Закрытие базы данных
        if (connection != null) {
            connection.close();
        }
    }

    @DataProvider
    public Object[][] dp1() throws Exception {
        setUp();
        takeQuery(System.getProperty("task1"));
        data();
        arr.clear();
        if (res.equals(System.getProperty("answer2g1"))) {
            return new Object[][]{
                    {System.getProperty("answer2g1")},
            };
        }
        if (res.equals(System.getProperty("answer1g1"))){
            return new Object[][]{
                    {System.getProperty("answer1g1")},
            };
        }
        Assert.fail("Ответ не соответствует ни одному ответу из группы");
        return new Object[0][];
    }

    @Story("Task 01")
    @Test(dataProvider = "dp1")
    public void test01(String value) throws Exception {
        //System.out.println("value = " + value);
        String sql = System.getProperty("task1");
        setUp();
        takeQuery(System.getProperty("task1"));
        data();
        arr.clear();
        assertNotEmpty(sql);
        Assert.assertEquals(value, res);
    }

    private void assertNotEmpty(String sql) {
        Assert.assertNotNull(sql, "В файле с ответами отсутствует SQL выражение для этой задачи");
        Assert.assertFalse(sql.isEmpty(), "SQL выражение не должно быть пустым");
    }
}
