package carRepairShop;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RepairShopView extends JFrame {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton backButton;
    private JButton createAccountButton;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField emailField;
    private JTextField signUpUserNameField;
    private JPasswordField signUpPasswordField;
    private JComboBox<String> roleComboBox;
    private JTextField nrOfCarsField;
    private JTextField moneyField;
    private JTextField dobField;
    private JTextField doeField;
    private JTextField specialtyField;
    private JButton listServicesButton;
    private JButton listPartsButton;
    private JTextField licensePlateField;
    private JTextField brandField;
    private JTextField colorField;
    private JTextField horsepowerField;
    private JTextField mileageField;
    private JButton registerCarButton;
    private JButton completeRegistrationButton;
    private JList<String> selectedServicesList;
    private DefaultListModel<String> selectedServicesModel;
    private JButton repairingProcessButton;
    private JButton nextButton1;
    private JButton nextButton2;


    RepairShopView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Car Repair Shop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        mainPanel.add(createLogInPanel(), "Login");
        mainPanel.add(createSignUpPanel(), "SignUp");
        mainPanel.add(createEmployeePanel(), "EmployeeAccount");
        mainPanel.add(createOwnerMainPanel(), "OwnerMainPanel");
        mainPanel.add(createCarRegistrationPanel(), "CarRegistration");

        frame.add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createCarStatusPanel5(Car car) {
        JPanel carStatusPanel = new JPanel(new GridBagLayout());
        carStatusPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        carStatusPanel.setBackground(new Color(230, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel statusLabel = new JLabel("Your car is ready for pick up!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setForeground(new Color(50, 50, 150));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        int carId = Car.getCarIdByLicensePlate(car.getLicensePlate());
        Notification notification = new Notification(car.getOwnerId(), carId,"Your car is ready for pick up!");
        DatabaseManager.insertNotification(notification);

        JButton pickUpButton = new JButton("Pick Up");
        pickUpButton.setFont(new Font("Arial", Font.PLAIN, 16));
        pickUpButton.setPreferredSize(new Dimension(120, 40));
        pickUpButton.setBackground(new Color(100, 200, 100));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(200, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        carStatusPanel.add(statusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        carStatusPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        carStatusPanel.add(pickUpButton, gbc);

        backButton.addActionListener(e -> {
            this.showOwnerMainScreen();
        });

        pickUpButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(carStatusPanel, "Thank you! You can now pick up your car.",
                    "Pick Up", JOptionPane.INFORMATION_MESSAGE);

            car.updateCarStatus(car.getOwnerId(), CarState.COMPLETED);
            notification.updateIsSentInDatabase(true);
            notification.setIsSent(true);
            this.showOwnerMainScreen();
        });

        this.mainPanel.add(carStatusPanel, "PICK_UP_NOTIFICATION");
        cardLayout.show(this.mainPanel, "PICK_UP_NOTIFICATION");
    }

    private void createCarStatusPanel4(Car car) {
        JPanel carStatusPanel = new JPanel(new GridBagLayout());
        carStatusPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        carStatusPanel.setBackground(new Color(230, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        String carState = String.valueOf(car.getStatus());
        JLabel carStatusLabel = new JLabel("Car Status: " + carState, SwingConstants.CENTER);
        carStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        carStatusLabel.setForeground(new Color(50, 50, 150));
        carStatusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        float serviceCost = 0;
        for (Service service : car.getServices()) {
                serviceCost += service.calculateTotalCostForService();
        }
        JLabel serviceCostLabel = new JLabel("Services Cost: $" + serviceCost, SwingConstants.CENTER);
        serviceCostLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        serviceCostLabel.setForeground(new Color(80, 80, 120));
        serviceCostLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton payButton = new JButton("Pay");
        payButton.setFont(new Font("Arial", Font.PLAIN, 16));
        payButton.setPreferredSize(new Dimension(120, 40));
        payButton.setBackground(new Color(100, 200, 100));

        JButton addMoneyButton = new JButton("Add Money");
        addMoneyButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addMoneyButton.setPreferredSize(new Dimension(120, 40));
        addMoneyButton.setBackground(new Color(100, 150, 200));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(200, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        carStatusPanel.add(carStatusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 30, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        carStatusPanel.add(serviceCostLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        carStatusPanel.add(payButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        carStatusPanel.add(addMoneyButton, gbc);

        backButton.addActionListener(e -> this.showOwnerMainScreen());

        float finalServiceCost = serviceCost;
        payButton.addActionListener(e -> {
            Owner owner = Owner.getOwnerById(car.getOwnerId());
            float ownerMoney = owner.getAmountOfMoney();
            if (ownerMoney >= finalServiceCost) {
                owner.setAmountOfMoney(ownerMoney - finalServiceCost);
                owner.updateAmountOfMoneyInDB(car.getOwnerId(), owner.getAmountOfMoney());
                car.updateCarStatus(car.getOwnerId(), CarState.PICKED_UP);
                int carId = Car.getCarIdByLicensePlate(car.getLicensePlate());
                Payment payment = new Payment(carId, finalServiceCost, Date.valueOf(LocalDate.now()));
                DatabaseManager.insertPayment(payment);
                JOptionPane.showMessageDialog(carStatusPanel, "Payment completed!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                this.showStatusPanel5(car);
            } else {
                JOptionPane.showMessageDialog(carStatusPanel, "Insufficient funds. Please add more money!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addMoneyButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(carStatusPanel, "Enter amount to add:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    float amountToAdd = Float.parseFloat(input);
                    if (amountToAdd > 0) {
                        Owner owner = Owner.getOwnerById(car.getOwnerId());
                        owner.setAmountOfMoney(owner.getAmountOfMoney() + amountToAdd);
                        owner.updateAmountOfMoneyInDB(car.getOwnerId(), owner.getAmountOfMoney());
                        JOptionPane.showMessageDialog(carStatusPanel, "Amount added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(carStatusPanel, "Please enter a positive amount.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(carStatusPanel, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.mainPanel.add(carStatusPanel, "PICKED_UP");
        cardLayout.show(this.mainPanel, "PICKED_UP");
    }

    private void createCarStatusPanel3(Car car) {
        JPanel carStatusPanel = new JPanel(new GridBagLayout());
        carStatusPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        carStatusPanel.setBackground(new Color(230, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        String carState = String.valueOf(car.getStatus());
        JLabel carStatusLabel = new JLabel("Car Status: " + carState, SwingConstants.CENTER);
        carStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        carStatusLabel.setForeground(new Color(50, 50, 150));
        carStatusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        nextButton2 = new JButton("Next");
        nextButton2.setFont(new Font("Arial", Font.PLAIN, 16));
        nextButton2.setPreferredSize(new Dimension(120, 40));
        nextButton2.setBackground(new Color(100, 200, 100));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(200, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        carStatusPanel.add(carStatusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(this.nextButton2, gbc);

        backButton.addActionListener(e -> {
            this.showOwnerMainScreen();
        });

        nextButton2.addActionListener(e -> {
            boolean ok = true;
            for (Service service : car.getServices()) {
                if (service.arePartsAvailable() == false) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                for (Service service : car.getServices()) {
                    for (Part part : service.getAllParts()) {
                        part.setStockQuantity(part.getStockQuantity() - 1);
                        part.updateStockQuantityInDB(part.getStockQuantity());
                    }
                }
                car.updateCarStatus(car.getOwnerId(), CarState.READY);
                this.showStatusPanel4(car);
            }
            else {
                this.showMessage("Not all parts for the services are in stock, please comeback later!",
                        "Problem", JOptionPane.WARNING_MESSAGE);
                this.showOwnerMainScreen();
            }
        });


        this.mainPanel.add(carStatusPanel, "READY");
        cardLayout.show(this.mainPanel, "READY");
    }

    private void createCarStatusPanel2(Car car) {
        JPanel carStatusPanel = new JPanel(new GridBagLayout());
        carStatusPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        carStatusPanel.setBackground(new Color(230, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        String carState = String.valueOf(car.getStatus());
        JLabel carStatusLabel = new JLabel("Car Status: " + carState, SwingConstants.CENTER);
        carStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        carStatusLabel.setForeground(new Color(50, 50, 150));
        carStatusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        nextButton1 = new JButton("Next");
        nextButton1.setFont(new Font("Arial", Font.PLAIN, 16));
        nextButton1.setPreferredSize(new Dimension(120, 40));
        nextButton1.setBackground(new Color(100, 200, 100));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(200, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        carStatusPanel.add(carStatusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(this.nextButton1, gbc);

        backButton.addActionListener(e -> {
            this.showOwnerMainScreen();
        });

        nextButton1.addActionListener(e -> {
            car.updateCarStatus(car.getOwnerId(), CarState.WAITING_PARTS);
            this.showStatusPanel3(car);
        });


        this.mainPanel.add(carStatusPanel, "WAITING_PARTS");
        cardLayout.show(this.mainPanel, "WAITING_PARTS");
    }

    private void createCarStatusPanel1(Car car) {
        JPanel carStatusPanel = new JPanel(new GridBagLayout());
        carStatusPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        carStatusPanel.setBackground(new Color(230, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();

        String carState = String.valueOf(car.getStatus());
        JLabel carStatusLabel = new JLabel("Car Status: " + carState, SwingConstants.CENTER);
        carStatusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        carStatusLabel.setForeground(new Color(50, 50, 150));
        carStatusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        nextButton1 = new JButton("Next");
        nextButton1.setFont(new Font("Arial", Font.PLAIN, 16));
        nextButton1.setPreferredSize(new Dimension(120, 40));
        nextButton1.setBackground(new Color(100, 200, 100));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(200, 100, 100));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 30, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        carStatusPanel.add(carStatusLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 20, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        carStatusPanel.add(this.nextButton1, gbc);

        backButton.addActionListener(e -> {
            this.showOwnerMainScreen();
        });

        nextButton1.addActionListener(e -> {
            car.updateCarStatus(car.getOwnerId(), CarState.WAITING_DIAGNOSIS);
            this.showStatusPanel2(car);
        });


        this.mainPanel.add(carStatusPanel, "WAITING_DIAGNOSIS");
        cardLayout.show(this.mainPanel, "WAITING_DIAGNOSIS");
    }

    private JPanel createCarRegistrationPanel() {
        JPanel carRegistrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        carRegistrationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel licensePlateLabel = new JLabel("License Plate:");
        licensePlateField = new JTextField(15);
        JLabel brandLabel = new JLabel("Brand:");
        brandField = new JTextField(15);
        JLabel colorLabel = new JLabel("Color:");
        colorField = new JTextField(15);
        JLabel horsepowerLabel = new JLabel("Horsepower:");
        horsepowerField = new JTextField(15);
        JLabel mileageLabel = new JLabel("Mileage:");
        mileageField = new JTextField(15);

        JLabel servicesLabel = new JLabel("Choose Services:");
        JComboBox<Service> servicesComboBox = new JComboBox<>();
        JButton addServiceButton = new JButton("Add Service");

        selectedServicesModel = new DefaultListModel<>();
        selectedServicesList = new JList<>(selectedServicesModel);
        selectedServicesList.setBorder(BorderFactory.createTitledBorder("Selected Services"));

        JButton backButton = new JButton("Back");
        completeRegistrationButton = new JButton("Complete Registration");

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        carRegistrationPanel.add(licensePlateLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        carRegistrationPanel.add(licensePlateField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        carRegistrationPanel.add(brandLabel, gbc);
        gbc.gridx = 1;
        carRegistrationPanel.add(brandField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        carRegistrationPanel.add(colorLabel, gbc);
        gbc.gridx = 1;
        carRegistrationPanel.add(colorField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        carRegistrationPanel.add(horsepowerLabel, gbc);
        gbc.gridx = 1;
        carRegistrationPanel.add(horsepowerField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        carRegistrationPanel.add(mileageLabel, gbc);
        gbc.gridx = 1;
        carRegistrationPanel.add(mileageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        carRegistrationPanel.add(servicesLabel, gbc);
        gbc.gridx = 1;
        carRegistrationPanel.add(servicesComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        carRegistrationPanel.add(addServiceButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JScrollPane selectedServicesScrollPane = new JScrollPane(selectedServicesList);
        carRegistrationPanel.add(selectedServicesScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        carRegistrationPanel.add(completeRegistrationButton, gbc);

        gbc.gridy++;
        carRegistrationPanel.add(backButton, gbc);

        List<Service> services = DatabaseManager.getAllServicesFromDB();
        for (Service service : services) {
            servicesComboBox.addItem(service);
        }

        servicesComboBox.setRenderer(new ListCellRenderer<Service>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Service> list, Service value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if (value != null) {
                    float totalPartsCost = 0.0f;
                    for (Part part : value.getAllParts()) {
                        totalPartsCost += part.getCostPerUnit();
                    }

                    label.setText(String.format("%s (Service Price: $%.2f, Parts Price: $%.2f)",
                            value.getName(), value.getCost(), totalPartsCost));
                }

                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                    label.setOpaque(true);
                }
                return label;
            }
        });

        addServiceButton.addActionListener(e -> {
            Service selectedService = (Service) servicesComboBox.getSelectedItem();
            if (selectedService != null && !selectedServicesModel.contains(selectedService.getName())) {
                selectedServicesModel.addElement(selectedService.getName());
            }
        });

        backButton.addActionListener(e -> {
            licensePlateField.setText("");
            brandField.setText("");
            colorField.setText("");
            horsepowerField.setText("");
            mileageField.setText("");
            selectedServicesModel.clear();

            this.showOwnerMainScreen();
        });

        return carRegistrationPanel;
    }

    private JPanel createOwnerMainPanel() {
        JPanel ownerMainPanel = new JPanel(new GridBagLayout());
        ownerMainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        ownerMainPanel.setBackground(new Color(230, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel welcomeLabel = new JLabel("Welcome!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(50, 50, 150));

        registerCarButton = new JButton("Register Car");
        registerCarButton.setFont(new Font("Arial", Font.PLAIN, 16));
        registerCarButton.setBackground(new Color(100, 200, 100));
        registerCarButton.setForeground(Color.WHITE);
        registerCarButton.setFocusPainted(false);
        registerCarButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 150, 75), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        repairingProcessButton = new JButton("Repairing Process");
        repairingProcessButton.setFont(new Font("Arial", Font.PLAIN, 16));
        repairingProcessButton.setBackground(new Color(100, 150, 200));
        repairingProcessButton.setForeground(Color.WHITE);
        repairingProcessButton.setFocusPainted(false);
        repairingProcessButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 100, 150), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logOutButton.setBackground(new Color(200, 100, 100));
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setFocusPainted(false);
        logOutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 75, 75), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        logOutButton.addActionListener(e -> {
            userNameField.setText("");
            passwordField.setText("");
            cardLayout.show(mainPanel, "Login");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ownerMainPanel.add(welcomeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        ownerMainPanel.add(registerCarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        ownerMainPanel.add(repairingProcessButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        ownerMainPanel.add(logOutButton, gbc);

        return ownerMainPanel;
    }

    private void createListPartsPanel(JPanel panel) {
        this.mainPanel.add(panel, "ListServices");
        cardLayout.show(this.mainPanel, "ListServices");
    }

    private void createListServicesPanel(List<Service> services) {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Employee Services",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        if (services.isEmpty()) {
            resultsPanel.add(new JLabel("No services found for this employee."));
        } else {
            for (Service service : services) {
                JPanel servicePanel = new JPanel(new GridLayout(2, 1));
                servicePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                servicePanel.setBackground(new Color(40, 139, 136));
                servicePanel.setMaximumSize(new Dimension(580, 100));

                JLabel serviceLabel = new JLabel(
                        String.format("<html><b>Service Name:</b> %s | <b>Cost:</b> $%.2f</html>",
                                service.getName(), service.getCost())
                );
                serviceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                JLabel descriptionLabel = new JLabel(
                        String.format("<html><b>Description:</b> %s</html>", service.getDescription())
                );
                descriptionLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                servicePanel.add(serviceLabel);
                servicePanel.add(descriptionLabel);

                resultsPanel.add(servicePanel);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showEmployeeScreen());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.removeAll();
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
        this.mainPanel.add(panel, "ListServices");
        cardLayout.show(this.mainPanel, "ListServices");
    }

    private JPanel createEmployeePanel() {
        JPanel personLoginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        personLoginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        listServicesButton = new JButton("List of Services");
        listPartsButton = new JButton("List of Parts");

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        personLoginPanel.add(listServicesButton, gbc);

        gbc.gridy = 1;
        personLoginPanel.add(listPartsButton, gbc);

        JButton logOutButton = new JButton("Log Out");
        gbc.gridy = 2;
        personLoginPanel.add(logOutButton, gbc);

        logOutButton.addActionListener(e -> {
            userNameField.setText("");
            passwordField.setText("");

            cardLayout.show(mainPanel, "Login");
        });

        return personLoginPanel;
    }

    private JPanel createLogInPanel() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("D:\\oop\\poiectCarService\\autorepairworkshop_cover.png");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        JLabel titleLabel = new JLabel("DTD Car Repair Shop");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel userNameLabel = new JLabel("Username:");
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userNameLabel.setForeground(Color.BLACK);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(Color.BLACK);

        userNameField = new JTextField(15);
        userNameField.setFont(new Font("Arial", Font.PLAIN, 16));

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));

        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(173, 216, 230));
        loginButton.setForeground(Color.BLACK);

        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setBackground(new Color(173, 216, 230));
        signUpButton.setForeground(Color.BLACK);

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(userNameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(userNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        backgroundPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        backgroundPanel.add(loginButton, gbc);

        gbc.gridy++;
        backgroundPanel.add(signUpButton, gbc);

        return backgroundPanel;
    }


    private JPanel createSignUpPanel() {
        JPanel signUpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        signUpPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneLabel = new JLabel("Phone Number:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel userNameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        addressField = new JTextField(15);
        emailField = new JTextField(15);
        signUpUserNameField = new JTextField(15);
        signUpPasswordField = new JPasswordField(15);
        roleComboBox = new JComboBox<>(new String[] { "Owner", "Employee" });

        JLabel carsLabel = new JLabel("Number of Cars:");
        nrOfCarsField = new JTextField(15);
        JLabel moneyLabel = new JLabel("Amount of Money:");
        moneyField = new JTextField(15);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobField = new JTextField(15);
        JLabel doeLabel = new JLabel("Date of Employment:");
        doeField = new JTextField(15);
        JLabel specialtyLabel = new JLabel("Specialty:");
        specialtyField = new JTextField(15);

        createAccountButton = new JButton("Create Account");
        backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            nameField.setText("");
            phoneField.setText("");
            addressField.setText("");
            emailField.setText("");
            signUpUserNameField.setText("");
            signUpPasswordField.setText("");
            userNameField.setText("");
            passwordField.setText("");
            nrOfCarsField.setText("");
            moneyField.setText("");
            dobField.setText("");
            doeField.setText("");
            specialtyField.setText("");

            cardLayout.show(mainPanel, "Login");
        });

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        signUpPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        signUpPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        signUpPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        signUpPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(userNameLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(signUpUserNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(signUpPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(carsLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(nrOfCarsField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(moneyLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(moneyField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(dobLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(doeLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(doeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        signUpPanel.add(specialtyLabel, gbc);
        gbc.gridx = 1;
        signUpPanel.add(specialtyField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        signUpPanel.add(createAccountButton, gbc);

        gbc.gridy++;
        signUpPanel.add(backButton, gbc);

        carsLabel.setVisible(false);
        nrOfCarsField.setVisible(false);
        moneyLabel.setVisible(false);
        moneyField.setVisible(false);
        dobLabel.setVisible(false);
        dobField.setVisible(false);
        doeLabel.setVisible(false);
        doeField.setVisible(false);
        specialtyLabel.setVisible(false);
        specialtyField.setVisible(false);

        roleComboBox.addActionListener(e -> {
            boolean isOwner = roleComboBox.getSelectedItem().equals("Owner");
            carsLabel.setVisible(isOwner);
            nrOfCarsField.setVisible(isOwner);
            moneyLabel.setVisible(isOwner);
            moneyField.setVisible(isOwner);

            dobLabel.setVisible(!isOwner);
            dobField.setVisible(!isOwner);
            doeLabel.setVisible(!isOwner);
            doeField.setVisible(!isOwner);
            specialtyLabel.setVisible(!isOwner);
            specialtyField.setVisible(!isOwner);

            signUpPanel.revalidate();
            signUpPanel.repaint();
        });

        return signUpPanel;
    }

    public void showStatusPanel1(Car car) {
        createCarStatusPanel1(car);
    }

    public void showStatusPanel2(Car car) {
        createCarStatusPanel2(car);
    }

    public void showStatusPanel3(Car car) {
        createCarStatusPanel3(car);
    }

    public void showStatusPanel4(Car car) {
        createCarStatusPanel4(car);
    }

    public void showStatusPanel5(Car car) {
        createCarStatusPanel5(car);
    }

    public void showListPartsScreen(JPanel panel) {
        createListPartsPanel(panel);
    }

    public void showListServicesScreen(List<Service> services) {
        createListServicesPanel(services);
    }

    public void showCarRegistrationScreen() {
        cardLayout.show(mainPanel, "CarRegistration");
    }

    public void showOwnerMainScreen() {
        cardLayout.show(mainPanel, "OwnerMainPanel");
    }

    public void showEmployeeScreen() {
        cardLayout.show(mainPanel, "EmployeeAccount");
    }

    public void showLoginScreen() {
        cardLayout.show(mainPanel, "Login");
    }

    public void showSignUpScreen() {
        cardLayout.show(mainPanel, "SignUp");
    }

    public void addRepairingProcessListener(ActionListener listener) {
        repairingProcessButton.addActionListener(listener);
    }

    public void addCompleteRegistrationListener(ActionListener listener) {
        completeRegistrationButton.addActionListener(listener);
    }

    public void addRegisterCarListener(ActionListener listener) {
        registerCarButton.addActionListener(listener);
    }

    public void addListPartsListener(ActionListener listener) {
        listPartsButton.addActionListener(listener);
    }

    public void addListServicesListener(ActionListener listener) {
        listServicesButton.addActionListener(listener);
    }

    public void addSignInListener(ActionListener listener) {
        signUpButton.addActionListener(listener);
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addCreateAccountListener(ActionListener listener) {
        createAccountButton.addActionListener(listener);
    }

    public String getLicensePlateField() {
        return licensePlateField.getText();
    }

    public String getBrandField() {
        return brandField.getText();
    }

    public String getColorField() {
        return colorField.getText();
    }

    public String getHorsepowerField() {
        return horsepowerField.getText();
    }

    public String getMileageField() {
        return mileageField.getText();
    }

    public String getUserNameField() {
        return userNameField.getText() ;
    }

    public String getPasswordField() {
        return passwordField.getText();
    }

    public String getNameField() {
        return nameField.getText();
    }

    public String getPhoneField() {
        return phoneField.getText();
    }

    public String getAddressField() {
        return addressField.getText();
    }

    public String getEmailField() {
        return emailField.getText();
    }

    public String getSignUpUserNameField() {
        return signUpUserNameField.getText();
    }

    public String getSignUpPasswordField() {
        return signUpPasswordField.getText();
    }

    public String getRoleComboBox() {
        return roleComboBox.getSelectedItem().toString();
    }

    public String getNrOfCarsField() {
        return nrOfCarsField.getText();
    }

    public String getMoneyField() {
        return moneyField.getText();
    }

    public String getDobField() {
        return dobField.getText();
    }

    public String getDoeField() {
        return doeField.getText();
    }

    public String getSpecialtyField() {
        return specialtyField.getText();
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }

    public List<String> getSelectedServices() {
        List<String> selectedServices = new ArrayList<>();
        for (int i = 0; i < selectedServicesList.getModel().getSize(); i++) {
            selectedServices.add(selectedServicesList.getModel().getElementAt(i));
        }
        return selectedServices;
    }

    public void setBrandField(String setField) {
        this.brandField.setText(setField);
    }

    public void setLicensePlateField(String setField) {
        this.licensePlateField.setText(setField);
    }

    public void setColorField(String setField) {
        this.colorField.setText(setField);
    }

    public void setHorsepowerField(String setField) {
        this.horsepowerField.setText(setField);
    }

    public void setMileageField(String setField) {
        this.mileageField.setText(setField);
    }

    public void setServicesLModel() {
        this.selectedServicesModel.clear();
    }

}