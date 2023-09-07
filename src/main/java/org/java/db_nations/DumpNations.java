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
            "join continents on continents.continent_id = regions.continent_id \n" +
            "order by countries.name asc;";
    private final static String SQL_stats = "select countries.country_id, countries.name as countries_name,  regions.name  as region_name, continents.name as continent_name, languages.`language` , country_stats.year, country_stats.population, country_stats.gdp \n" +
            "from countries \n" +
            "join country_stats on country_stats.country_id  = countries.country_id\n" +
            "join regions on regions.region_id = countries.region_id\n" +
            "join continents on continents.continent_id = regions.continent_id\n" +
            "join country_languages on country_languages.country_id = countries.country_id \n" +
            "join languages on languages.language_id = country_languages.language_id\n" +
            "where countries.country_id = ? and (country_stats.country_id, country_stats.`year`) in (select country_id, max(`year`) as max_year\n" +
            "from country_stats \n" +
            "group by country_id);\n";

    private final static String SQL_research = "select countries.name as countries_name, country_id , regions.name as region_name, continents.name as continent_name\n" +
            "from countries \n" +
            "join regions on regions.region_id = countries.region_id\n" +
            "join continents on continents.continent_id = regions.continent_id \n" +
            "where countries.name like ?;";

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

            System.out.println("Inserisci la parola che vuoi usare come parametro di ricerca");
            String research = scanner.nextLine();
            System.out.println("Ricerca per " + research);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_research)) {
                preparedStatement.setString(1, "%" + research + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String result = resultSet.getString("countries_name");
                        System.out.println("Il risultato della ricerca è: " + result);
                    }
                }
            }

            System.out.println("Inserisci l'ID del paese");
            int Id = Integer.parseInt(scanner.nextLine());
            System.out.println("Hai selezionato l'ID: " + Id);
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_stats)) {
                preparedStatement.setInt(1, Id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String country_id = resultSet.getString("country_id");
                        String country_name = resultSet.getString("countries_name");
                        String region_name = resultSet.getString("region_name");
                        String continent_name = resultSet.getString("continent_name");
                        String language = resultSet.getString("language");
                        int year = resultSet.getInt("year");
                        int population = resultSet.getInt("population");
                        double gdp = resultSet.getDouble("gdp");
                        System.out.println("Details for country: " + country_name + "\n" + "Languages: " + language + "\n" + "Most recent stats: " + "\n" +
                                "Year: " + year + "\n" + "Population: " + population + "\n" + "GDP: " + gdp);
                    }
                }
            }
            } catch (SQLException e) {
                System.err.println("Errore SQL: " + e.getMessage());
                e.printStackTrace();
            }


scanner.close();
        }

    }


