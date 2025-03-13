package carRepairShop;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        Connection con = db.getConnection();
        /*Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner");
        db.insertPerson(p1);
        Owner o1 = new Owner("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner", 2, 4546);
        db.insertOwner(o1);
        Person p2 = new Person("rares", "1234567", "tgyutu", "rarara", "rareees", "ooop", "Owner");
        db.insertPerson(p2);
        Owner o2 = new Owner("rares", "1234567", "tgyutu", "rarara", "rareees", "ooop", "Owner", 2, 4546);
        db.insertOwner(o2);
        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Employee");
        db.insertPerson(p1);
        Employee e1 = new Employee("alex", "9008778", "tgyutu", "tuytututut",
                "alexx", "ooop", "Employee", 2679, Date.valueOf("2005-11-11"),
                Date.valueOf("2022-12-01"), "Engines");
        db.insertEmployee(e1);

        Service service = new Service("Oil Change", "Full synthetic oil change", 49.99f);
        Part part = new Part("Brake Pad", 50, 29.99f);
        db.insertService(service);
        db.insertPart(part);

        Service s1 = new Service("Oil Change", "Full synthetic oil change", 49.99f);
        Service s2 = new Service("Engine Change", "YEs very good", 45.99f);
        db.insertService(s1);
        db.insertService(s2);
        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Employee");
        db.insertPerson(p1);
        Employee e1 = new Employee("alex", "9008778", "tgyutu", "tuytututut",
                "alexx", "ooop", "Employee", 2679, Date.valueOf("2005-11-11"),
                Date.valueOf("2022-12-01"), "Engines");
        db.insertEmployee(e1);
        e1.addService(s1);
        e1.addService(s2);
        e1.insertServicesToDatabase();

        Part p1 = new Part("Gloves", 70, 10);
        db.insertPart(p1);
        Part p2 = new Part("JK", 3, 0.5f);
        db.insertPart(p2);
        Service s1 = new Service("Oil Change", "Full synthetic oil change", 49.99f);
        db.insertService(s1);
        s1.addRequiredPart(p1);
        s1.addRequiredPart(p2);

        s1.insertPartsToDatabase();

        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner");
        db.insertPerson(p1);
        Owner o1 = new Owner("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner", 2, 4546);
        db.insertOwner(o1);

        o1.updateAmountOfMoneyInDB(1,69);

        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Employee");
        db.insertPerson(p1);
        Employee e1 = new Employee("alex", "9008778", "tgyutu", "tuytututut",
                "alexx", "ooop", "Employee", 2679, Date.valueOf("2005-11-11"),
                Date.valueOf("2022-12-01"), "Engines");
        db.insertEmployee(e1);

        Person p2 = new Person("rares", "123488567", "tgyutu", "rarara", "rareees", "ooop", "Employee");
        db.insertPerson(p2);
        Employee e2 = new Employee("rares", "123488567", "tgyutu", "rarara",
                "rareees", "ooop", "Employee", 25666, Date.valueOf("2005-11-11"),
                Date.valueOf("2022-12-01"), "tyres");
        db.insertEmployee(e2);

        Person p3 = new Person("pol", "0023456", "trt", "polll", "lop", "ooop", "Employee");
        db.insertPerson(p3);
        Employee e3 = new Employee("pol", "0023456", "trt", "polll",
                "lop", "ooop", "Employee", 3466, Date.valueOf("2000-11-11"),
                Date.valueOf("2017-12-01"), "Engines");
        db.insertEmployee(e3);

        Service s1 = new Service("Oil Change", "Full synthetic oil change", 49.99f);
        db.insertService(s1);
        Service s2 = new Service("tyre Change", "New tyres", 239.99f);
        db.insertService(s2);
        Service s3 = new Service("New lights", "Very expensive", 789.99f);
        db.insertService(s3);

        e1.addService(s1);
        e2.addService(s1);
        e1.insertServicesToDatabase();
        e2.insertServicesToDatabase();

        e3.addService(s3);
        e3.insertServicesToDatabase();

        List<Employee> emp1 = s1.getAllEmployees();
        List<Employee> emp2 = s2.getAllEmployees();
        List<Employee> emp3 = s3.getAllEmployees();

        for (Employee emp : emp1) {
            System.out.println(emp.getName());
        }

        for (Employee emp : emp2) {
            System.out.println(emp.getName());
        }

        for (Employee emp : emp3) {
            System.out.println(emp.getName());
        }

        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner");
        db.insertPerson(p1);
        Owner o1 = new Owner("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner", 2, 4546);
        db.insertOwner(o1);
        Car c1 = new Car("CJ09TXA", "Nissan", "black", 150, 160000, 1);
        db.insertCar(c1);

        Service s1 = new Service("Oil Change", "Full synthetic oil change", 49.99f);
        db.insertService(s1);
        Service s2 = new Service("tyre Change", "New tyres", 239.99f);
        db.insertService(s2);

        Part p1 = new Part("oil", 32, 12.09f);
        db.insertPart(p1);
        Part p2 = new Part("engine", 42, 22.09f);
        db.insertPart(p2);

        p2.addService(s1);
        p2.addService(s2);

        List<Service> services = p1.getServices();

        for (Service service : services) {
            System.out.println(service.getName());
        }

        services = p2.getServices();
        for (Service service : services) {
            System.out.println(service.getName());
        }

        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner");
        db.insertPerson(p1);
        Owner o1 = new Owner("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner", 2, 4546);
        db.insertOwner(o1);
        Car c1 = new Car("CJ09TXA", "Nissan", "black", 150, 160000, 1);
        db.insertCar(c1);
        Payment pay = new Payment(1, 66, Date.valueOf(LocalDate.now()));
        db.insertPayment(pay);

        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner");
        db.insertPerson(p1);

        Log log = new Log(1);
        db.insertLog(log);


        Person p1 = new Person("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner");
        db.insertPerson(p1);
        Owner o1 = new Owner("alex", "9008778", "tgyutu", "tuytututut", "alexx", "ooop", "Owner", 2, 4546);
        db.insertOwner(o1);
        Car c1 = new Car("CJ09TXA", "Nissan", "black", 150, 160000, 1);
        db.insertCar(c1);

        Notification n1 = new Notification(1,1,"Your car is ready");
        db.insertNotification(n1);
        n1.updateIsSentInDatabase(true);
        Service s1 = new Service("Oil Change", "Full synthetic oil change", 49.99f);
        db.insertService(s1);
        Service s2 = new Service("tyre Change", "New tyres", 239.99f);
        db.insertService(s2);

        Part p1 = new Part("oil", 32, 12.09f);
        db.insertPart(p1);
        Part p2 = new Part("engine", 42, 22.09f);
        db.insertPart(p2);

        s2.addRequiredPart(p1);
        s2.addRequiredPart(p2);
        s2.insertPartsToDatabase();

        List<Part> parts = s2.getAllParts();
        for (Part part : parts) {
            System.out.println(part.getName());
        }

        db.closeConnection(con);*/
        RepairShopView repairShopView = new RepairShopView();
        RepairShopController repairShopController = new RepairShopController(repairShopView);

//        Person p2 = new Person("rares", "1234885670", "tgyutu", "rar@gmail.com", "rareees", "ooop", "Employee");
//        db.insertPerson(p2);
//        Employee e2 = new Employee("rares", "123488567)", "tgyutu", "rar@gmail.com",
//                "rareees", "ooop", "Employee", 25666, Date.valueOf("2005-11-11"),
//                Date.valueOf("2022-12-01"), "tyres");
//        db.insertEmployee(e2);
//        int id = e2.getEmployeeIdByName(e2.getName());
        db.closeConnection(con);
    }
}
