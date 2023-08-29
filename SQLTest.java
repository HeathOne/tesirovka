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
    ArrayList<String> arr = new ArrayList<>();

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
            StringBuilder sb = new StringBuilder();
            for (Object o : arr) {
                sb.append(o);
                res = sb.toString();
            }
        }
        if (table != null) {
            table.close();
        }
        if (statement != null) {
            statement.close();
        } // Закрытие базы данных
        if (connection != null) {
            connection.close();
        }// Закрытие набора данных
    }
//    Connection conn = null;
//    @Before
//    public void setUp() throws SQLException {
//        conn = DriverManager.getConnection("jdbc:postgresql://workbench.lanit.ru:55432/postgres", "lanit", "lanit");
//    }
//    @Test
//    public void testGetUserNameById() throws SQLException {
//        QueryRunner run = new QueryRunner();
//        ResultSetHandler<String> h = new ResultSetHandler<String>() {
//            public String handle(ResultSet rs) throws SQLException {
//                if (!rs.next()) {
//                    return null;
//                }
//                return rs.getString(1);
//            }
//        };
//        String result = run.query(conn, "select count(email) from helpdesk_ticketcc", h, 1);
//        assertEquals("446", result);
//    }
//    @After
//    public void tearDown() {
//        DbUtils.closeQuietly(conn);
//    }

    @DataProvider
    public Object[][] dp1() {
        return new Object[][]{
                {"446"},
                {"455"},
        };
    }

    @Story("Task 01")
    @Test(dataProvider = "dp1")
    public void test01(String value) throws Exception {
        System.out.println("value = " + value);
        String sql = System.getProperty("task1");
        setUp();
        takeQuery(System.getProperty("task1"));
        data();
        assertNotEmpty(sql);
        System.out.println("res = " + res);
        System.out.println("value = " + value);
        Assert.assertEquals(value, res, String.format("В строке '%s' должно быть найдено '%s'", res, value));


        //Assert.assertTrue(compilePattern(sql).matcher(value).find(),
    }


    private void assertNotEmpty(String sql) {
        Assert.assertNotNull(sql, "В файле с ответами отсутствует SQL выражение для этой задачи");
        Assert.assertFalse(sql.isEmpty(), "SQL выражение не должно быть пустым");
    }

//    @DataProvider
//    public Object[][] dp2() {
//        return new Object[][]{
//                {"abc123xyz"},
//                {"define \"123\""},
//                {"var g = 123;"},
//        };
//    }
//
//    @Story("Task 02")
//    @Test(dataProvider = "dp2")
//    public void test02(String value) {
//        String regex = System.getProperty("task2");
//        assertNotEmpty(regex);
//        Assert.assertTrue(compilePattern(regex).matcher(value).find(),
//                String.format("В строке '%s' должно быть найдено регулярное выражение '%s'", value, regex));
//    }
//
//    @DataProvider
//    public Object[][] dp3() {
//        return new Object[][]{
//                {"cats.", true},
//                {"8967.", true},
//                {"?=+!.", true},
//                {"abcd1", false},
//        };
//    }
//
//    @Story("Task 03")
//    @Test(dataProvider = "dp3")
//    public void test03(String value, boolean isMatch) {
//        String regex = System.getProperty("task3");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp4() {
//        return new Object[][]{
//                {"can", true},
//                {"man", true},
//                {"fan", true},
//                {"dan", false},
//                {"ran", false},
//                {"pan", false},
//        };
//    }
//
//    @Story("Task 04")
//    @Test(dataProvider = "dp4")
//    public void test04(String value, boolean isMatch) {
//        String regex = System.getProperty("task4");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp5() {
//        return new Object[][]{
//                {"hog", true},
//                {"dog", true},
//                {"bog", false},
//        };
//    }
//
//    @Story("Task 05")
//    @Test(dataProvider = "dp5")
//    public void test05(String value, boolean isMatch) {
//        String regex = System.getProperty("task5");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp6() {
//        return new Object[][]{
//                {"Ana", true},
//                {"Bob", true},
//                {"Cpc", true},
//                {"aax", false},
//                {"bby", false},
//                {"ccz", false},
//        };
//    }
//
//    @Story("Task 06")
//    @Test(dataProvider = "dp6")
//    public void test06(String value, boolean isMatch) {
//        String regex = System.getProperty("task6");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp7() {
//        return new Object[][]{
//                {"wazzzzzup", true},
//                {"wazzzup", true},
//                {"wazup", false},
//        };
//    }
//
//    @Story("Task 07")
//    @Test(dataProvider = "dp7")
//    public void test07(String value, boolean isMatch) {
//        String regex = System.getProperty("task7");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp8() {
//        return new Object[][]{
//                {"aaaabcc", true},
//                {"aabbbbc", true},
//                {"aacc", true},
//                {"a", false},
//        };
//    }
//
//    @Story("Task 08")
//    @Test(dataProvider = "dp8")
//    public void test08(String value, boolean isMatch) {
//        String regex = System.getProperty("task8");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp9() {
//        return new Object[][]{
//                {"1 file found?", true},
//                {"2 files found?", true},
//                {"24 files found?", true},
//                {"No files found.", false},
//        };
//    }
//
//    @Story("Task 09")
//    @Test(dataProvider = "dp9")
//    public void test09(String value, boolean isMatch) {
//        String regex = System.getProperty("task9");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp10() {
//        return new Object[][]{
//                {"1.   abc", true},
//                {"2.\tabc", true},
//                {"3.           abc", true},
//                {"4.abc", false},
//        };
//    }
//
//    @Story("Task 10")
//    @Test(dataProvider = "dp10")
//    public void test10(String value, boolean isMatch) {
//        String regex = System.getProperty("task10");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp11() {
//        return new Object[][]{
//                {"Mission: successful", true},
//                {"Last Mission: unsuccessful", false},
//                {"Next Mission: successful upon capture of target", false},
//        };
//    }
//
//    @Story("Task 11")
//    @Test(dataProvider = "dp11")
//    public void test11(String value, boolean isMatch) {
//        String regex = System.getProperty("task11");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp12() {
//        return new Object[][]{
//                {"file_record_transcript.pdf", "file_record_transcript", true},
//                {"file_07241999.pdf", "file_07241999", true},
//                {"testfile_fake.pdf.tmp", null, false},
//        };
//    }
//
//    @Story("Task 12")
//    @Test(dataProvider = "dp12")
//    public void test12(String value, String group, boolean isMatch) {
//        String regex = System.getProperty("task12");
//        assertNotEmpty(regex);
//        Matcher m = compilePattern(regex).matcher(value);
//        Assert.assertEquals(m.find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//        if (isMatch) {
//            try {
//                Assert.assertEquals(m.group(1), group, "Захваченная группа символов не соответствует ожидаемой.");
//            } catch (IndexOutOfBoundsException e) {
//                throw new AssertionError("Не найдена группа символов");
//            }
//        }
//    }
//
//    @DataProvider
//    public Object[][] dp13() {
//        return new Object[][]{
//                {"Jan 1987", "1987", true},
//                {"May 1969", "1969", true},
//                {"Aug 2011", "2011", true},
//        };
//    }
//
//    @Story("Task 13")
//    @Test(dataProvider = "dp13")
//    public void test13(String value, String group, boolean isMatch) {
//        String regex = System.getProperty("task13");
//        assertNotEmpty(regex);
//        Matcher m = compilePattern(regex).matcher(value);
//        Assert.assertEquals(m.find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//        if (isMatch) {
//            try {
//                Assert.assertEquals(m.group(1), value, "Захваченная группа символов не соответствует ожидаемой.");
//            } catch (IndexOutOfBoundsException e) {
//                throw new AssertionError("Не найдена группа символов: " + value);
//            }
//            try {
//                Assert.assertEquals(m.group(2), group, "Захваченная группа символов не соответствует ожидаемой.");
//            } catch (IndexOutOfBoundsException e) {
//                throw new AssertionError("Не найдена группа символов " + group);
//            }
//        }
//    }
//
//    @DataProvider
//    public Object[][] dp14() {
//        return new Object[][]{
//                {"1280x720", "1280", "720"},
//                {"1920x1600", "1920", "1600"},
//                {"1024x768", "1024", "768"},
//        };
//    }
//
//    @Story("Task 14")
//    @Test(dataProvider = "dp14")
//    public void test14(String value, String group1, String group2) {
//        String regex = System.getProperty("task14");
//        assertNotEmpty(regex);
//        Matcher m = compilePattern(regex).matcher(value);
//        Assert.assertTrue(m.find(), String.format("В строке '%s' должно быть найдено регулярное выражение '%s'", value, regex));
//        try {
//            Assert.assertEquals(m.group(1), group1, "Захваченная группа символов не соответствует ожидаемой.");
//        } catch (IndexOutOfBoundsException e) {
//            throw new AssertionError("Не найдена группа символов: " + group1);
//        }
//        try {
//            Assert.assertEquals(m.group(2), group2, "Захваченная группа символов не соответствует ожидаемой.");
//        } catch (IndexOutOfBoundsException e) {
//            throw new AssertionError("Не найдена группа символов " + group2);
//        }
//    }
//
//    @DataProvider
//    public Object[][] dp15() {
//        return new Object[][]{
//                {"I love cats", true},
//                {"I love dogs", true},
//                {"I love logs", false},
//                {"I love cogs", false},
//        };
//    }
//
//    @Story("Task 15")
//    @Test(dataProvider = "dp15")
//    public void test15(String value, boolean isMatch) {
//        String regex = System.getProperty("task15");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    @DataProvider
//    public Object[][] dp16() {
//        return new Object[][]{
//                {"The quick brown fox jumps over the lazy dog.", true},
//                {"There were 614 instances of students getting 90.0% or above.", true},
//                {"The FCC had to censor the network for saying &$#*@!.", true},
//        };
//    }
//
//    @Story("Task 16")
//    @Test(dataProvider = "dp16")
//    public void test16(String value, boolean isMatch) {
//        String regex = System.getProperty("task16");
//        assertNotEmpty(regex);
//        Assert.assertEquals(compilePattern(regex).matcher(value).find(), isMatch,
//                String.format("В строке '%s' %sдолжно быть найдено регулярное выражение '%s'", value, (isMatch ? "" : "не "), regex));
//    }
//
//    private Pattern compilePattern(String regex) {
//        try {
//            return Pattern.compile(regex);
//        } catch (PatternSyntaxException e) {
//            throw new AssertionError("Ошибка в синтаксисе регулярного выражения: " + e.getMessage(), e);
//        }
//    }
}
