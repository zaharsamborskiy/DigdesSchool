package com.digdes.main;

import com.digdes.school.JavaSchoolStarter;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        Scanner scanner = new Scanner(System.in);
        List<Map<String, Object>> mapList;
        boolean start = true;
        while (start) {
            mapList = starter.execute(scanner.nextLine());
            if (scanner.nextLine().equals("exit")) {
                start = false;
            }
        }





//        String request = "INSERT VALUES 'LASTNAME' = 'Федоров' , ID=2, cost = '19.0', ACTIVE=true, Age = 25";
//        String request2 = "INSERT VALUES 'LASTNAME' = 'петров', ID=2, 'AGE'=25, cost = 19.0, ACTIVE='false'";
//
//
//        List<Map<String, Object>> mapList = starter.execute(request);
//        mapList = starter.execute(request2);
//        System.out.println(mapList + " - добавление элементов");
//
//
//        String reqUpdate = "UPDATE VALUES id=3, cost=60.0 where lastname like пЕтров% ";
//        String reqUpdate2 = "UPDATE VALUES ‘active’=true";
//
//        mapList = starter.execute(reqUpdate);
//        System.out.println(mapList + " - изменение объекта");
//
//
//        String reqSelect = "SELECT  lastname like ковалев";
//        String reqSelect2 = "SELECT where lastname like пЕтров%";
//        String reqSelect3 = "SELECT WHERE age<=35 and lastname like %п%";
//
//        mapList = starter.execute(reqSelect2);
//        System.out.println(mapList + " - выбор элементов");
//
//        String reqDelete = "DELETE WHERE ‘id’=3";
//        String reqDelete3 = "DELETE where lastname like %петров and id=2";
//        String reqDelete4 = "DELETE WHERE ‘id’=3 or cost=19.0";
//        String reqDelete2 = "DELETE lastname like петров";
//        mapList = starter.execute(reqDelete2);
//        System.out.println(mapList + " - удаление элементов");
    }
}
