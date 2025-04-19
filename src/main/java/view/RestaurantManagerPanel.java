
package view;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import controller.*;
import model.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import static view.Modifiers.*;

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
    
    private EditItemPanel editItemPanel;

    
    private int currentSeatNumber;

    public RestaurantManagerPanel(Server server) {
        this.server = server;
        this.controller = new Controller();
        controller.seedFoodItemsIfEmpty();
        cardLayout = new CardLayout();
        screens = new JPanel(cardLayout);

        // Add views to card layout
        screens.add(new MainPOSPanel(this, server), "MainPOS");
        screens.add(new AssignTablePanel(this, server), "AssignTable");

        seatSelectPanel = new SeatSelectPanel(this);
        screens.add(seatSelectPanel, "SeatSelect");

        editItemPanel = new EditItemPanel(this);
        editItemPanel.setName("EditItem");
        screens.add(editItemPanel, "EditItem");


        screens.add(new OrderHistoryPanel(server), "OrderHistory");

        screens.add(new TopItemsPanel(), "TopItems");
        screens.add(new TableOverviewPanel(), "TableOverview");
       // screens.add(new TipsPanel(), "Tips");
        screens.add(new TipsPanel(server), "Tips");


        setLayout(new BorderLayout());

        // Side navigation panel
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] views = {
            "MainPOS", "AssignTable", "SeatSelect", "EditItem",
            "OrderHistory", "TopItems", "TableOverview", "Tips"
        };

        for (String view : views) {
            JButton btn = new JButton(view);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(150, 30));
            btn.addActionListener(e -> switchTo(view));
            navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            navPanel.add(btn);
        }

        navPanel.add(Box.createVerticalGlue());

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(150, 30));
        logoutBtn.addActionListener(e -> {
            SwingUtilities.invokeLater(LoginFrame::new);
            SwingUtilities.getWindowAncestor(this).dispose(); // Close DashboardFrame
        });

        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(logoutBtn);

        // Add to layout
        add(navPanel, BorderLayout.WEST);
        add(screens, BorderLayout.CENTER);

        cardLayout.show(screens, "MainPOS");
    }

  
    
    public void switchTo(String panelName) {
        if (panelName.equals("SeatSelect")) {
            seatSelectPanel.refresh();
        }
        if (panelName.equals("EditItem")) {
            System.out.println("Switching to EditItem");
            editItemPanel.refreshData();  
        }
        if (panelName.equals("OrderHistory")) {
            JPanel panel = getPanel("OrderHistory");
            if (panel instanceof OrderHistoryPanel) {
                ((OrderHistoryPanel) panel).refreshOrderHistory();
            }
        }

        
        
        
        cardLayout.show(screens, panelName);
        screens.revalidate();
        screens.repaint();
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
    
    public void setCurrentSeatNumber(int seat) {
        this.currentSeatNumber = seat;
    }

    public int getCurrentSeatNumber() {
        return currentSeatNumber;
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
    private static final long serialVersionUID = 1L;

    private JTextField tableNumberField;
    private JTextField seatCountField;
    private JLabel statusLabel;
    private RestaurantManagerPanel app;
    private Server server;
    private Controller controller;

    public AssignTablePanel(RestaurantManagerPanel app, Server server) {
        this.app = app;
        this.server = server;
        this.controller = app.getController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Title label
        JLabel titleLabel = new JLabel("Assign Table", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Table number label & field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Table Number:"), gbc);

        gbc.gridx = 1;
        tableNumberField = new JTextField(10);
        formPanel.add(tableNumberField, gbc);

        // Seat count label & field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Seat Count:"), gbc);

        gbc.gridx = 1;
        seatCountField = new JTextField(10);
        formPanel.add(seatCountField, gbc);

        // Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        JButton assignButton = new JButton("Start Session");
        styleButton(assignButton);
        assignButton.addActionListener(e -> startSession());
        formPanel.add(assignButton, gbc);

        // Status
        gbc.gridy = 3;
        statusLabel = new JLabel("", SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(230, 230, 230));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 35));
    }

    private void startSession() {
        try {
            int tableNumber = Integer.parseInt(tableNumberField.getText().trim());
            int seats = Integer.parseInt(seatCountField.getText().trim());

            int sessionId = controller.createSession(server);
            app.setCurrentSessionId(sessionId);

            int orderId = controller.createOrder(tableNumber, sessionId);
            app.setCurrentOrderId(orderId);

            app.setLastAssignedTable(tableNumber);
            app.setSeatCount(seats);
            app.switchTo("SeatSelect");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



class SeatSelectPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private RestaurantManagerPanel app;
    private JPanel seatsPanel;
    private Controller controller;

    public SeatSelectPanel(RestaurantManagerPanel app) {
        this.app = app;
        this.controller = app.getController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Title
        JLabel title = new JLabel("Select a Seat - Table " + app.getLastAssignedTable(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Center grid for seat buttons
        seatsPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        seatsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(seatsPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        seatsPanel.removeAll();

        int seatCount = app.getSeatCount();

        // Update title
        JLabel title = (JLabel) getComponent(0);
        title.setText("Select a Seat - Table " + app.getLastAssignedTable());

        for (int i = 1; i <= seatCount; i++) {
            final int seatNumber = i;
            JButton seatBtn = new JButton("Seat " + seatNumber);
            styleSeatButton(seatBtn);
            seatBtn.addActionListener(e -> {
                app.setCurrentSeatNumber(seatNumber);
                app.switchTo("EditItem");
            });

            seatsPanel.add(seatBtn);
        }

        revalidate();
        repaint();
    }

    private void styleSeatButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setBackground(new Color(220, 240, 255));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 40));
    }
}

class EditItemPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Controller controller;
    private final RestaurantManagerPanel parent;
    private Session currentSession;
    private int selectedSeat;

    private JPanel foodButtonPanel;
    private JTextArea orderArea;
    private JComboBox<Food.Category> categoryComboBox;
    private JTextField tipField; // NEW

    private static final Map<String, Double> BURGER_MODS = new HashMap<String, Double>() {{
        put("No Lettuce", 0.0);
        put("Extra Patty", 1.50);
        put("Add Cheese", 0.75);
        put("No Pickles", 0.0);
        put("Extra Tomato", 0.50);
    }};

    private static final Map<String, Double> FRIES_MODS = new HashMap<String, Double>() {{
        put("Jumbo Size", 1.00);
        put("Animal Style", 1.50);
    }};

    private static final String[] SHAKE_FLAVORS = { "Chocolate", "Vanilla", "Strawberry" };

    public EditItemPanel(RestaurantManagerPanel parent) {
        this.parent = parent;
        this.controller = parent.getController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: Category selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        topPanel.add(new JLabel("Category:"));

        categoryComboBox = new JComboBox<>(Food.Category.values());
        categoryComboBox.addActionListener(e -> refreshFoodButtons());
        topPanel.add(categoryComboBox);

        add(topPanel, BorderLayout.NORTH);

        // Center: food buttons and order preview
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        foodButtonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JScrollPane foodScroll = new JScrollPane(foodButtonPanel);
        centerPanel.add(foodScroll);

        orderArea = new JTextArea(20, 30);
        orderArea.setEditable(false);
        orderArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane orderScroll = new JScrollPane(orderArea);
        centerPanel.add(orderScroll);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Tip field and Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JLabel tipLabel = new JLabel("Tip ($):");
        tipField = new JTextField(6);
        JButton tipUpdateBtn = new JButton("Update Tip");

        tipUpdateBtn.addActionListener(e -> {
            try {
                double tipAmount = Double.parseDouble(tipField.getText().trim());
                controller.setTipForCurrentSession(currentSession.getId(), tipAmount);
                JOptionPane.showMessageDialog(this, "Tip updated to $" + String.format("%.2f", tipAmount));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid tip value. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backBtn = new JButton("Back to Seat Select");
        backBtn.addActionListener(e -> parent.switchTo("SeatSelect"));

        bottomPanel.add(tipLabel);
        bottomPanel.add(tipField);
        bottomPanel.add(tipUpdateBtn);
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        int sessionId = parent.getCurrentSessionId();
        this.selectedSeat = parent.getCurrentSeatNumber();

        Order order = controller.getOrderBySessionId(sessionId);
        if (order == null) {
            orderArea.setText("No order found for this session.");
            return;
        }

        this.currentSession = controller.getSessionById(sessionId);
        
        

        
        
        
        
        
        ////old set tip method

        // Set existing tip
        double currentTip = order != null ? order.getTip() : 0.0;

       // double currentTip = currentSession.getTotalTips(); old way
        tipField.setText(String.format("%.2f", currentTip));

        refreshFoodButtons();
        updateOrderPreview();
    }

    private void refreshFoodButtons() {
        foodButtonPanel.removeAll();
        Food.Category selectedCategory = (Food.Category) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) return;

        List<Food> foodList = controller.getFoodByCategory(selectedCategory.name());
        for (Food food : foodList) {
            if (food.getName().equalsIgnoreCase("fries")) {
                food.setName("Fries");
            } else if (food.getName().equalsIgnoreCase("shakes")) {
                food.setName("Shakes");
            }

            JButton btn = new JButton("<html><center>" + food.getName() + "<br/>($" + food.getCost() + ")</center></html>");
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(200, 230, 250));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> showModifierDialog(food, selectedCategory));
            foodButtonPanel.add(btn);
        }

        foodButtonPanel.revalidate();
        foodButtonPanel.repaint();
    }

    private void showModifierDialog(Food food, Food.Category category) {
        JPanel panel = new JPanel();
        List<String> selectedMods = new ArrayList<>();
        double extraCost = 0.0;

        if (category == Food.Category.SHAKES) {
            panel.setLayout(new GridLayout(SHAKE_FLAVORS.length, 1));
            ButtonGroup shakeGroup = new ButtonGroup();
            JRadioButton[] options = new JRadioButton[SHAKE_FLAVORS.length];

            for (int i = 0; i < SHAKE_FLAVORS.length; i++) {
                options[i] = new JRadioButton(SHAKE_FLAVORS[i]);
                shakeGroup.add(options[i]);
                panel.add(options[i]);
            }

            int result = JOptionPane.showConfirmDialog(this, panel, "Choose Shake Flavor", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                for (JRadioButton option : options) {
                    if (option.isSelected()) {
                        selectedMods.add(option.getText());
                        break;
                    }
                }
            } else {
                return;
            }

        } else {
            Map<String, Double> modifiers;
            switch (category) {
                case BURGERS:
                    modifiers = BURGER_MODS;
                    break;
                case FRIES:
                    modifiers = FRIES_MODS;
                    break;
                default:
                    modifiers = new HashMap<>(); // No modifiers
            }

            panel.setLayout(new GridLayout(modifiers.size(), 1));
            Map<String, JCheckBox> checkboxes = new HashMap<>();

            for (Map.Entry<String, Double> entry : modifiers.entrySet()) {
                String label = entry.getKey() + (entry.getValue() > 0 ? " (+$" + entry.getValue() + ")" : "");
                JCheckBox cb = new JCheckBox(label);
                checkboxes.put(entry.getKey(), cb);
                panel.add(cb);
            }

            int result = JOptionPane.showConfirmDialog(this, panel, "Select Modifications", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                for (Map.Entry<String, JCheckBox> entry : checkboxes.entrySet()) {
                    if (entry.getValue().isSelected()) {
                        selectedMods.add(entry.getKey());
                    }
                }
            } else {
                return;
            }
        }

        controller.addFoodToOrder(currentSession, selectedSeat, food, selectedMods.toArray(new String[0]));
        updateOrderPreview();
    }

    private void updateOrderPreview() {
        if (currentSession == null) {
            orderArea.setText("No active session.");
            return;
        }

        Order order = controller.getOrderBySessionId(currentSession.getId());
        if (order == null) {
            orderArea.setText("No order started for this session.");
            return;
        }

        List<OrderFood> items = controller.getOrderItems(order.getId());
        StringBuilder sb = new StringBuilder();

        for (OrderFood of : items) {
            if (of.getSeat() == selectedSeat) {
                Food food = controller.getFoodById(of.getFoodId());
                double base = food.getCost();
                double modTotal = Arrays.stream(of.getModifications())
                    .mapToDouble(mod -> BURGER_MODS.getOrDefault(mod, FRIES_MODS.getOrDefault(mod, 0.0)))
                    .sum();
                double total = base + modTotal;

                sb.append("1 x ").append(food.getName())
                  .append(" ($").append(String.format("%.2f", total)).append(")");

                if (of.getModifications().length > 0) {
                    sb.append(" [").append(String.join(", ", of.getModifications())).append("]");
                }
                sb.append("\n");
            }
        }

        orderArea.setText(sb.toString());
    }
}



/***************************OHP with problems*****************************/


class OrderHistoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextArea orderHistoryArea;
    private Controller controller;
    private Server server;

    public OrderHistoryPanel(Server server) {
        this.server = server;
        this.controller = new Controller();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel title
        JLabel titleLabel = new JLabel("Order History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Order details display area
        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderHistoryArea);
        add(scrollPane, BorderLayout.CENTER);

        // Load data into text area
        refreshOrderHistory();
    }

    /**
     * Loads all orders for the current server and displays them in the text area.
     */
    public void refreshOrderHistory() {
        List<Order> orders = controller.getOrdersForServer(server.getId());
        StringBuilder sb = new StringBuilder();

        for (Order order : orders) {
            List<OrderFood> items = controller.getOrderItems(order.getId());
            double total = 0.0;

            sb.append("Order ID: ").append(order.getId()).append("\n");
            sb.append("Table: ").append(order.getTableNumber()).append("\n");
            sb.append("Date: ").append(order.getCreatedAt()).append("\n");

            for (OrderFood item : items) {
                Food food = controller.getFoodById(item.getFoodId());
                double itemCost = food.getCost();

                // Add modifier costs
                for (String mod : item.getModifications()) {
                    itemCost += BURGER_MODS.getOrDefault(mod,
                              FRIES_MODS.getOrDefault(mod, 0.0));
                }

                itemCost *= item.getQuantity();
                total += itemCost;

                sb.append("  - ").append(item.getQuantity())
                  .append(" x ").append(food.getName())
                  .append(" ($").append(String.format("%.2f", itemCost)).append(")");

                if (item.getModifications().length > 0) {
                    sb.append(" [").append(String.join(", ", item.getModifications())).append("]");
                }
                sb.append("\n");
            }
            
         // Fetch tip from the session, not order
            Session session = controller.getSessionById(order.getSessionId());
            double tip = session != null ? session.getTotalTips() : 0.0;

            sb.append("Tip: $").append(String.format("%.2f", tip)).append("\n");
            sb.append("Total: $").append(String.format("%.2f", total + tip)).append("\n");
            sb.append("Closed: ").append(order.isClosed() ? "Yes" : "No").append("\n\n");


//            sb.append("Total: $").append(String.format("%.2f", total)).append("\n");
//            sb.append("Closed: ").append(order.isClosed() ? "Yes" : "No").append("\n\n");
        }

        orderHistoryArea.setText(sb.toString());
    }

}




/**
 * Displays the most frequently ordered food items across all sessions.
 * Pulls live order data from the Controller and sorts by quantity.
 */

    class TopItemsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private Controller controller;

    public TopItemsPanel() {
        this.controller = new Controller();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Top Selling Items", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("By Orders", createOrdersListPanel());
        tabs.addTab("By Revenue", createRevenueListPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createOrdersListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> orderList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(orderList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Get data from controller
        Map<Food, Integer> topOrderedItems = controller.getTopOrderedItems();

        // Sort by quantity
        List<Map.Entry<Food, Integer>> sorted = new ArrayList<>(topOrderedItems.entrySet());
        sorted.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        DefaultListModel<String> model = new DefaultListModel<>();
        int rank = 1;
        for (Map.Entry<Food, Integer> entry : sorted) {
            String line = rank + ". " + entry.getKey().getName() + " – " + entry.getValue() + " orders";
            model.addElement(line);
            rank++;
        }

        orderList.setModel(model);
        return panel;
    }

    private JPanel createRevenueListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> revenueList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(revenueList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Get data from controller
        Map<String, Double> profitMap = controller.getFoodDAO().getTotalProfitByFoodName();

        // Sort by revenue descending
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(profitMap.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        DefaultListModel<String> model = new DefaultListModel<>();
        int rank = 1;
        for (Map.Entry<String, Double> entry : sorted) {
            String line = rank + ". " + entry.getKey() + " – $" + String.format("%.2f", entry.getValue());
            model.addElement(line);
            rank++;
        }

        revenueList.setModel(model);
        return panel;
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
    private static final long serialVersionUID = 1L;
    private JTextArea tipsReportArea;
    private Controller controller;
    private Server server;

    public TipsPanel(Server server) {
        this.server = server;
        this.controller = new Controller();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Tips Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        tipsReportArea = new JTextArea();
        tipsReportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(tipsReportArea);
        add(scrollPane, BorderLayout.CENTER);

        refreshTipsReport();
    }

    private void refreshTipsReport() {
        List<Session> sessions = controller.getSessionsForServer(server.getId());

        double totalTips = controller.getTotalTipsForServer(server.getId());
        int sessionCount = sessions.size();
        double avgTip = sessionCount > 0 ? totalTips / sessionCount : 0.0;

        StringBuilder sb = new StringBuilder();
        sb.append("Server: ").append(server.getUsername()).append("\n");
        sb.append("Total Tips: $").append(String.format("%.2f", totalTips)).append("\n");
        sb.append("Sessions: ").append(sessionCount).append("\n");
        sb.append("Average Tip per Session: $").append(String.format("%.2f", avgTip)).append("\n\n");

        sb.append("Session Details:\n");
        for (Session s : sessions) {
            sb.append("- Session ID: ").append(s.getId())
              .append(", Tip: $").append(String.format("%.2f", s.getTotalTips()))
              .append("\n");
        }

        tipsReportArea.setText(sb.toString());
    }
}

