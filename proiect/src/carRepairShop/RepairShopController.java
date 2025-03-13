package carRepairShop;

import javax.swing.*;
import java.sql.Date;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepairShopController {
    private final RepairShopView view;

    private Employee currentEmployee;
    private int currentEmployeeId;
    private Owner currentOwner;
    private int currentOwnerId;
    private Car currentCar;

    public RepairShopController(RepairShopView view) {
        this.view = view;
        addListeners();
    }

    private void addListeners() {
        view.addSignInListener(e -> view.showSignUpScreen());
        view.addCreateAccountListener(e -> createAccount());
        view.addLoginListener(e -> handleLogIn());
        view.addListServicesListener(e -> getListOfServices());
        view.addListPartsListener(e -> getListOfParts());
        view.addRegisterCarListener(e -> {
                if (DatabaseManager.getNumberOfCarsNotPickedUp() >= 2) {
                    view.showMessage("Too many cars in the workshop, please come back later!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                 else if(Car.getNumberOfCarsNotPickedUpForOwner(currentOwnerId) > 0) {
                     view.showMessage("You can only have one car in workshop per person!", "Error", JOptionPane.ERROR_MESSAGE);
                     return;
                 }
                 else {
                    view.showCarRegistrationScreen();
                 }
        });
        view.addRepairingProcessListener(e -> firstStateRepair());
        view.addCompleteRegistrationListener(e -> handleCarRegistration());
    }

    private void firstStateRepair() {
        currentCar = Car.getCarByOwnerIdAndStatus(currentOwnerId);
        if (currentCar == null) {
            view.showMessage("You need to introduce your car details first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentCar.getAllServices();

       switch (currentCar.getStatus()) {
           case REGISTERED:
               view.showStatusPanel1(this.currentCar);
               break;

           case WAITING_DIAGNOSIS:
               view.showStatusPanel2(this.currentCar);
                break;

            case WAITING_PARTS:
                view.showStatusPanel3(this.currentCar);
                break;

           case READY:
               view.showStatusPanel4(this.currentCar);
               break;

           case PICKED_UP:
               view.showStatusPanel5(this.currentCar);
               break;

           case COMPLETED:
                view.showMessage("Your car service has been completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                view.showMessage("Unknown car status. Please contact support.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleCarRegistration() {
        String licensePlate = view.getLicensePlateField();
        String brand = view.getBrandField();
        String color = view.getColorField();
        String hp = view.getHorsepowerField();
        String ml = view.getMileageField();

        if (licensePlate.isEmpty() || brand.isEmpty() || color.isEmpty() || hp.isEmpty() || ml.isEmpty()) {
            view.showMessage("All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int horsepower;
        try {
            horsepower = Integer.parseInt(hp);
            if (horsepower <= 0) {
                view.showMessage("Horsepower must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            view.showMessage("Horsepower must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int mileage;
        try {
            mileage = Integer.parseInt(ml);
            if (mileage < 0) {
                view.showMessage("Mileage must be 0 or a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            view.showMessage("Mileage must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseManager.doesCarExist(licensePlate)) {
            view.showMessage("A car with this license plate already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> selectedServices = view.getSelectedServices();
        if (selectedServices.isEmpty()) {
            view.showMessage("Please select at least one service for the car.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Car car = new Car(licensePlate, brand, color, horsepower, mileage, currentOwnerId);
        boolean okCar = DatabaseManager.insertCar(car);

        if (okCar) {
            for (String serviceName : selectedServices) {
                Service service = Service.getServiceByName(serviceName);
                car.addService(service);
            }

            if (!car.insertServicesToDatabase()) {
                view.showMessage("An error occurred while registering the car.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            this.currentCar = car;
            view.showMessage("Car successfully registered with selected services.", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.showOwnerMainScreen();
            view.setLicensePlateField("");
            view.setBrandField("");
            view.setColorField("");
            view.setHorsepowerField("");
            view.setMileageField("");
            view.setServicesLModel();
        } else {
            view.showMessage("An error occurred while registering the car.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void getListOfParts() {
        List<Service> services = this.currentEmployee.getAllServicesForEmployee(this.currentEmployeeId);
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Parts",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        if (services.isEmpty()) {
            resultsPanel.add(new JLabel("No services or parts found for this employee."));
        } else {
            for (Service service : services) {
                JPanel serviceNamePanel = new JPanel(new GridBagLayout());

                GridBagConstraints nameGbc = new GridBagConstraints();
                nameGbc.gridx = 0;
                nameGbc.gridy = 0;
                nameGbc.anchor = GridBagConstraints.CENTER;
                nameGbc.weightx = 1.0;
                nameGbc.insets = new Insets(5, 5, 5, 5);

                JLabel serviceLabel = new JLabel(
                        String.format("<html><b>Service Name:</b> %s</html>", service.getName())
                );

                serviceNamePanel.add(serviceLabel, nameGbc);
                resultsPanel.add(serviceNamePanel);

                List<Part> parts = service.getAllParts();
                if (parts.isEmpty()) {
                    JLabel noPartsLabel = new JLabel("No parts found for this service.");
                    noPartsLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    noPartsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    resultsPanel.add(noPartsLabel);
                } else {
                    for (Part part : parts) {
                        JPanel partPanel = new JPanel(new BorderLayout());
                        partPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                        partPanel.setBackground(new Color(220, 220, 220));
                        partPanel.setMaximumSize(new Dimension(600, 60));

                        JLabel partLabel = new JLabel(
                                String.format("<html><b>Part Name:</b> %s | <b>Stock:</b> %d | <b>Cost:</b> $%.2f</html>",
                                        part.getName(), part.getStockQuantity(), part.getCostPerUnit())
                        );
                        partLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                        partPanel.add(partLabel, BorderLayout.WEST);

                        JPanel updatePanel = new JPanel();
                        updatePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                        JTextField stockField = new JTextField(5);
                        JButton updateButton = new JButton("Update");

                        updatePanel.add(new JLabel("New Stock:"));
                        updatePanel.add(stockField);
                        updatePanel.add(updateButton);

                        partPanel.add(updatePanel, BorderLayout.EAST);
                        updateButton.addActionListener(e -> {
                            String stockText = stockField.getText();
                            try {
                                int newStock = Integer.parseInt(stockText);
                                boolean success = currentEmployee.updatePartsStockForService(service.getName(), newStock, part.getName());
                                if (success) {
                                    part.setStockQuantity(newStock);
                                    partLabel.setText(String.format(
                                            "<html><b>Part Name:</b> %s | <b>Stock:</b> %d | <b>Cost:</b> $%.2f</html>",
                                            part.getName(), part.getStockQuantity(), part.getCostPerUnit()
                                    ));
                                    view.showMessage("Stock successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    view.showMessage("Failed to update stock. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                view.showMessage("Invalid stock number. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });

                        resultsPanel.add(partPanel);
                    }
                }
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        centerPanel.add(scrollPane, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> view.showEmployeeScreen());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.removeAll();
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
        view.showListPartsScreen(panel);
    }

    private void getListOfServices() {
        List<Service> services = this.currentEmployee.getAllServicesForEmployee(this.currentEmployeeId);
        view.showListServicesScreen(services);
    }

    private void handleLogIn() {
        String username = view.getUserNameField();
        String password = view.getPasswordField();

        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage("All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Person person = Person.getPersonByUserName(username);
        if (person != null && person.getPasswordHash().equals(password)) {
            view.showMessage("Log-in successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            if (person.getRole().equals("Employee")) {
                Employee employee = Employee.getEmployeeByUserName(username);

                if (employee == null) {
                    view.showMessage("Employee details could not be retrieved. Please contact support.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                this.currentEmployee = employee;
                this.currentEmployeeId = employee.getEmployeeIdByName(employee.getName());

                Log log = new Log(currentEmployeeId);
                DatabaseManager.insertLog(log);

                view.showEmployeeScreen();
            } else {
                Owner owner = Owner.getOwnerByUserName(username);

                if (owner == null) {
                    view.showMessage("Owner details could not be retrieved. Please contact support.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                this.currentOwner = owner;
                this.currentOwnerId = owner.getOwnerIdByName(owner.getName());

                Log log = new Log(currentOwnerId);
                DatabaseManager.insertLog(log);

                view.showOwnerMainScreen();
            }
        } else {
            view.showMessage("Invalid username or password. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createAccount() {
        String name = view.getNameField();
        String email = view.getEmailField();
        String phone = view.getPhoneField();
        String address = view.getAddressField();
        String userName = view.getSignUpUserNameField();
        String password = view.getSignUpPasswordField();
        String role = view.getRoleComboBox();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || userName.isEmpty() ||
                password.isEmpty() || role.isEmpty()) {
            view.showMessage("All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            view.showMessage("Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phone.length() != 10) {
            view.showMessage("The phone number must contain exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            view.showMessage("The password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (DatabaseManager.doesUsernameExist(userName)) {
            view.showMessage("Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        Person person = new Person(name, phone, address, email, userName, password, role);
        boolean okPerson = DatabaseManager.insertPerson(person);

        if (okPerson) {
            if (role.equals("Owner")) {
                int nrOfCars  = Integer.parseInt(view.getNrOfCarsField());
                float amountOfMoney = Float.parseFloat(view.getMoneyField());

                Owner owner = new Owner(name, phone, address, email, userName, password, role, nrOfCars, amountOfMoney);
                boolean okOwner = DatabaseManager.insertOwner(owner);

                if (okOwner) {
                    view.showMessage("Account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    view.showLoginScreen();
                } else {
                    view.showMessage("An error occurred while creating the account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                Date doe = Date.valueOf(view.getDoeField());
                Date dob = Date.valueOf(view.getDobField());
                String speciality = view.getSpecialtyField();

                Employee employee = new Employee(name, phone, address, email, userName, password, role, 3000, dob, doe, speciality);
                boolean okEmployee = DatabaseManager.insertEmployee(employee);

                if (okEmployee) {
                    view.showMessage("Account created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    view.showLoginScreen();
                } else {
                    view.showMessage("An error occurred while creating the account.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            view.showMessage("An error occurred while creating the account.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}