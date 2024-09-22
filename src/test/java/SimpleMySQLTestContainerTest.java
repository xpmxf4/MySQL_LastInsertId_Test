import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleMySQLTestContainerTest {

    @Test
    void simpleMySQLTestContainerTest() {
        System.out.println("Starting MySQL container...");

        try (MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")) {

            mysql.start();
            System.out.println("MySQL container started. JDBC URL: " + mysql.getJdbcUrl());

            // mysql container 에 연결
            try(Connection conn = mysql.createConnection("")) {
                System.out.println("Connected to MySQL container.");

                Statement stmt = conn.createStatement();

                // 테스트 테이블 생성
                System.out.println("Creating test table...");
                stmt.execute("CREATE TABLE IF NOT EXISTS test_table (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "value VARCHAR(255))");
                System.out.println("Test table created.");

                // 테스트 데이터 삽입
                System.out.println("Inserting test data 'Value1'...");
                stmt.execute("INSERT INTO test_table (value) VALUES ('Value1')");
                System.out.println("Inserting test data 'Value2'...");
                stmt.execute("INSERT INTO test_table (value) VALUES ('Value2')");

                // LAST_INSERT_ID() 호출
                System.out.println("Querying LAST_INSERT_ID()...");
                ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                rs.next();
                long lastInsertId = rs.getLong(1);

                // lastInsertId 출력
                System.out.println("LAST_INSERT_ID() returned: " + lastInsertId);

                // lastInsertId 가 2인지 확인
                assertEquals(2, lastInsertId, "LAST_INSERT_ID() 가 2를 반환해야 합니다.");
                System.out.println("Test passed! LAST_INSERT_ID() was correct.");

            } catch (SQLException e) {
                System.out.println("SQLException occurred: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        // when - 동작

        // then - 검증
    }
}
