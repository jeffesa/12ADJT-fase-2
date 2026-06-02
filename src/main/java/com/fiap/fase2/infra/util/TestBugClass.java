package com.fiap.fase2.infra.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Classe com problemas intencionais para testar SonarCloud.
 * REMOVER APÓS TESTE.
 */
public class TestBugClass {

    // Bug: NullPointerException potencial
    public String getLength(String input) {
        return input.toUpperCase();
    }

    // Code Smell: método vazio
    public void doNothing() {
    }

    // Vulnerabilidade: SQL injection
    public void unsafeQuery(String userInput) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE name = '" + userInput + "'");
    }

    // Code Smell: variável não utilizada
    public int calculate(int a, int b) {
        int unused = a * b;
        return a + b;
    }
}
