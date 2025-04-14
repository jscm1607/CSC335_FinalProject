package view;


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import controller.*;
import model.*;
import java.text.NumberFormat;

public class RestaurantManagerPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel screens;
    private Server server;
    private int lastAssignedTable;
    private int seatCount;
    private SeatSelectPanel seatSelectPanel;
    private int currentSessionId;
    private int currentOrderId;
    private Controller controller;

    public RestaurantManagerPanel(Server server) {
        this.server = server;
        this.controller = new Controller();
        cardLayout = new CardLayout();
        screens = new JPanel(cardLayout);

        screens.add(new MainPOSPanel(this, server), "MainPOS");
        screens.add(new AssignTablePanel(this, server), "AssignTable");

        seatSelectPanel = new SeatSelectPanel(this);
        screens.add(seatSelectPanel, "SeatSelect");

        EditItemPanel editPanel = new EditItemPanel(this);
        editPanel.setName("EditItem");
        screens.add(editPanel, "EditItem");
        screens.add(new OrderHistoryPanel(), "OrderHistory");
        screens.add(new TopItemsPanel(), "TopItems");
        screens.add(new TableOverviewPanel(), "TableOverview");
        screens.add(new TipsPanel(), "Tips");

        setLayout(new BorderLayout());
        add(screens, BorderLayout.CENTER);
        cardLayout.show(screens, "MainPOS");
    }

    public void switchTo(String panelName) {
        if (panelName.equals("SeatSelect")) {
            seatSelectPanel.refresh();
        }
        if (panelName.equals("EditItem")) {
            ((EditItemPanel) getPanel("EditItem")).updateHeader();
        }
        cardLayout.show(screens, panelName);
    }
    
    public JPanel getPanel(String name) {
        for (Component comp : screens.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(name)) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    public Server getServer() {
        return server;
    }

    public void setLastAssignedTable(int tableNumber) {
        this.lastAssignedTable = tableNumber;
    }

    public int getLastAssignedTable() {
        return lastAssignedTable;
    }

    public void setSeatCount(int count) {
        this.seatCount = count;
    }

    public int getSeatCount() {
        return seatCount;
    }
    
    public void setCurrentSessionId(int sessionId) {
        this.currentSessionId = sessionId;
    }
    
    public int getCurrentSessionId() {
        return currentSessionId;
    }
    
    public void setCurrentOrderId(int orderId) {
        this.currentOrderId = orderId;
    }
    
    public int getCurrentOrderId() {
        return currentOrderId;
    }
    
    public Controller getController() {
        return controller;
    }
}

class MainPOSPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Server server;
    @SuppressWarnings("unused")
	private RestaurantManagerPanel app;
    private Controller controller;
    
    public MainPOSPanel(RestaurantManagerPanel app, Server server) {
        this.app = app;
        this.server = server;
        this.controller = app.getController();
        
        setLayout(new BorderLayout());

        // Get updated server info from database
        Server updatedServer = controller.getServerByUsername(server.getUsername());
        
        JLabel title = new JLabel("Main POS - Logged in as: " + updatedServer.getUsername(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JButton toAssign = new JButton("Assign Table");
        toAssign.addActionListener(e -> app.switchTo("AssignTable"));
        add(toAssign, BorderLayout.SOUTH);
    }
}

class AssignTablePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tableNumberField;
    private JTextField seatCountField;
    private JLabel statusLabel;
    @SuppressWarnings("unused")
	private RestaurantManagerPanel app;
    @SuppressWarnings("unused")
	private Server server;
    private Controller controller;

    public AssignTablePanel(RestaurantManagerPanel app, Server server) {
        this.app = app;
        this.server = server;
        this.controller = app.getController();
        
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        add(new JLabel("Assign Table", SwingConstants.CENTER));
        add(new JLabel(""));

        add(new JLabel("Table Number:"));
        tableNumberField = new JTextField();
        add(tableNumberField);

        add(new JLabel("Seat Count:"));
        seatCountField = new JTextField();
        add(seatCountField);

        JButton assignButton = new JButton("Start Session");
        assignButton.addActionListener((ActionEvent e) -> {
            try {
                int tableNumber = Integer.parseInt(tableNumberField.getText().trim());
                int seats = Integer.parseInt(seatCountField.getText().trim());
                
                // Create session in DB
                int sessionId = controller.createSession(server);
                app.setCurrentSessionId(sessionId);
                
                // Create order in DB
                int orderId = controller.createOrder(tableNumber, sessionId);
                app.setCurrentOrderId(orderId);
                
                app.setLastAssignedTable(tableNumber);
                app.setSeatCount(seats);
                app.switchTo("SeatSelect");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        statusLabel = new JLabel("", SwingConstants.CENTER);

        add(assignButton);
        add(statusLabel);
    }
}

class SeatSelectPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RestaurantManagerPanel app;
    private JPanel seatsPanel;
    @SuppressWarnings("unused")
	private Controller controller;

    public SeatSelectPanel(RestaurantManagerPanel app) {
        this.app = app;
        this.controller = app.getController();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Select a Seat - Table " + app.getLastAssignedTable(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        seatsPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        add(seatsPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        seatsPanel.removeAll();
        int seatCount = app.getSeatCount();
        
        // Update the title to show current table
        JLabel title = (JLabel) getComponent(0);
        title.setText("Select a Seat - Table " + app.getLastAssignedTable());

        for (int i = 1; i <= seatCount; i++) {
            final int seatNumber = i;
            JButton seatBtn = new JButton("Seat " + seatNumber);
            seatBtn.addActionListener(e -> {
                app.switchTo("EditItem");
            });
            seatsPanel.add(seatBtn);
        }

        revalidate();
        repaint();
    }
}

class EditItemPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RestaurantManagerPanel app;
    private Controller controller;
    private JPanel menuPanel;
    private JPanel orderPanel;
    private JLabel orderTotalLabel;
    private JComboBox<String> categoryComboBox;
    private double orderTotal = 0.0;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private JLabel tableInfoLabel;
    
    public EditItemPanel(RestaurantManagerPanel app) {
        this.app = app;
        this.controller = app.getController();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // North panel - Table info and categories
        JPanel northPanel = new JPanel(new BorderLayout());
        tableInfoLabel = new JLabel("Table " + app.getLastAssignedTable() + " - Order #" + app.getCurrentOrderId());
        tableInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        northPanel.add(tableInfoLabel, BorderLayout.WEST);
        
        // Category selector
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        categoryPanel.add(new JLabel("Category: "));
        categoryComboBox = new JComboBox<>(new String[]{"BURGERS", "FRIES", "SHAKES", "BEVERAGES"});
        categoryComboBox.addActionListener(e -> refreshMenuItems());
        categoryPanel.add(categoryComboBox);
        northPanel.add(categoryPanel, BorderLayout.EAST);
        
        add(northPanel, BorderLayout.NORTH);
        
        // Center panel with menu and order
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Menu panel
        JPanel menuContainer = new JPanel(new BorderLayout());
        JLabel menuTitle = new JLabel("Menu Items", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 14));
        menuContainer.add(menuTitle, BorderLayout.NORTH);
        
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JScrollPane menuScrollPane = new JScrollPane(menuPanel);
        menuContainer.add(menuScrollPane, BorderLayout.CENTER);
        
        // Order panel
        JPanel orderContainer = new JPanel(new BorderLayout());
        JLabel orderTitle = new JLabel("Current Order", SwingConstants.CENTER);
        orderTitle.setFont(new Font("Arial", Font.BOLD, 14));
        orderContainer.add(orderTitle, BorderLayout.NORTH);
        
        orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        JScrollPane orderScrollPane = new JScrollPane(orderPanel);
        orderContainer.add(orderScrollPane, BorderLayout.CENTER);
        
        // Total and action buttons
        JPanel orderFooter = new JPanel(new BorderLayout());
        orderTotalLabel = new JLabel("Total: " + currencyFormat.format(orderTotal), SwingConstants.RIGHT);
        orderTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        orderFooter.add(orderTotalLabel, BorderLayout.NORTH);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        JButton closeOrderBtn = new JButton("Close Order");
        JButton backBtn = new JButton("Back to Seats");
        backBtn.addActionListener(e -> app.switchTo("SeatSelect"));
        
        buttonsPanel.add(backBtn);
        buttonsPanel.add(closeOrderBtn);
        orderFooter.add(buttonsPanel, BorderLayout.SOUTH);
        
        orderContainer.add(orderFooter, BorderLayout.SOUTH);
        
        centerPanel.add(menuContainer);
        centerPanel.add(orderContainer);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Load initial menu items
        refreshMenuItems();
        refreshOrderItems();
    }
    
    public void updateHeader() {
    	tableInfoLabel.setText("Table " + app.getLastAssignedTable() + " - Order #" + app.getCurrentOrderId());
        refreshMenuItems();
        refreshOrderItems();
    }

	private void refreshMenuItems() {
        menuPanel.removeAll();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        
        List<Food> foods = controller.getFoodByCategory(selectedCategory);
        
        for (Food food : foods) {
            if (food.isInStock()) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ));
                
                JLabel nameLabel = new JLabel(food.getName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                
                JLabel priceLabel = new JLabel(currencyFormat.format(food.getCost()));
                
                JPanel detailPanel = new JPanel(new BorderLayout());
                detailPanel.add(nameLabel, BorderLayout.WEST);
                detailPanel.add(priceLabel, BorderLayout.EAST);
                
                JButton addButton = new JButton("Add to Order");
                final int foodId = food.getId();
                addButton.addActionListener(e -> {
                    // Add to order
                    controller.addFoodToOrder(app.getCurrentOrderId(), foodId, 1, 1, new String[]{""});
                    refreshOrderItems();
                });
                
                itemPanel.add(detailPanel, BorderLayout.CENTER);
                itemPanel.add(addButton, BorderLayout.SOUTH);
                
                menuPanel.add(itemPanel);
            }
        }
        
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private void refreshOrderItems() {
        orderPanel.removeAll();
        orderTotal = 0.0;
        
        List<OrderFood> orderItems = controller.getOrderItems(app.getCurrentOrderId());
        
        for (OrderFood item : orderItems) {
            Food food = controller.getFoodById(item.getFoodId());
            if (food != null) {
                double itemTotal = food.getCost() * item.getQuantity();
                orderTotal += itemTotal;
                
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(3, 3, 3, 3),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ));
                
                JLabel itemLabel = new JLabel(
                    item.getQuantity() + "x " + food.getName() + 
                    " (Seat " + item.getSeat() + "): " + 
                    currencyFormat.format(itemTotal)
                );
                
                itemPanel.add(itemLabel, BorderLayout.CENTER);
                
                orderPanel.add(itemPanel);
            }
        }
        
        orderTotalLabel.setText("Total: " + currencyFormat.format(orderTotal));
        
        orderPanel.revalidate();
        orderPanel.repaint();
    }
}

class OrderHistoryPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea orderHistoryArea;

    public OrderHistoryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Order History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderHistoryArea);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data for demonstration
        String sampleHistory = "Order #12345 - 07/22/2023 - $45.50\n" +
                               "Order #12346 - 07/22/2023 - $22.00\n" +
                               "Order #12347 - 07/22/2023 - $78.25\n";
        orderHistoryArea.setText(sampleHistory);
    }
}

class TopItemsPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> topItemsList;

    public TopItemsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Top Selling Items", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Burger - 150");
        listModel.addElement("Pizza - 120");
        listModel.addElement("Coffee - 200");
        listModel.addElement("Salad - 80");
        listModel.addElement("Ice Cream - 100");

        topItemsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(topItemsList);
        add(scrollPane, BorderLayout.CENTER);
    }
}

class TableOverviewPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tableStatusTable;

    public TableOverviewPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Table Status Overview", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Table", "Status", "Server", "Guests"};
        Object[][] data = {
            {"1", "Occupied", "John", "4"},
            {"2", "Available", "-", "-"},
            {"3", "Occupied", "Alice", "2"},
            {"4", "Reserved", "-", "-"}
        };

        tableStatusTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(tableStatusTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}

class TipsPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea tipsReportArea;

    public TipsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Tips Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        tipsReportArea = new JTextArea();
        tipsReportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(tipsReportArea);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data for demonstration
        String sampleReport = "Date: 07/22/2023\n" +
                              "Total Tips: $250.00\n" +
                              "Number of Servers: 5\n" +
                              "Average Tips per Server: $50.00";
        tipsReportArea.setText(sampleReport);
    }
}