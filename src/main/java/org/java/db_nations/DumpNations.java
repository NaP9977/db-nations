package org.java.db_nations;

import java.sql.*;
import java.util.Scanner;

public class DumpNations {
    private final static String DB_URL = "jdbc:mysql://localhost:3306/dump_nations";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "psl7711";
    private final static String SQL_countries = "select countries.name as countries_name, country_id , regions.name as region_name, continents.name as continent_name\n" +
            "from countries \n" +
            "join regions on regions.region_id = countries.region_id\n" +
            "join continents where continents.continent_id = regions.continent_id \n" +
            "order by countries.name asc;";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_countries)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String countriesName = resultSet.getString("countries_name");
                        int countries_id = resultSet.getInt("country_id");
                        String regionName = resultSet.getString("region_name");
                        String continent = resultSet.getString("continent_name");
                        System.out.println("Countries_name:\n " + countriesName);
                        System.out.print("ID: \n" + countries_id + "\n");
                        System.out.print("From the region: \n " + regionName + "\n");
                        System.out.print("In the continent: \n" + continent + "\n");
                    }
                }
            }
        } catch (SQLException exception) {
            System.out.println("Errore nella ricerca");
        }

    }
}
