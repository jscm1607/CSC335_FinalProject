package view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import model.Server;
import model.Session;

public class RestaurantManagerPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel screens;
    private Server server;
    private int lastAssignedTable;
    private int seatCount;
    private SeatSelectPanel seatSelectPanel;

    public RestaurantManagerPanel(Server server) {
        this.server = server;
        cardLayout = new CardLayout();
        screens = new JPanel(cardLayout);

        screens.add(new MainPOSPanel(this, server), "MainPOS");
        screens.add(new AssignTablePanel(this, server), "AssignTable");

        seatSelectPanel = new SeatSelectPanel(this);
        screens.add(seatSelectPanel, "SeatSelect");

        screens.add(new EditItemPanel(), "EditItem");
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
        cardLayout.show(screens, panelName);
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
}

class MainPOSPanel extends JPanel {
    public MainPOSPanel(RestaurantManagerPanel app, Server server) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Main POS - Logged in as: " + server.getUsername(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JButton toAssign = new JButton("Assign Table");
        toAssign.addActionListener(e -> app.switchTo("AssignTable"));
        add(toAssign, BorderLayout.SOUTH);
    }
}

class AssignTablePanel extends JPanel {
    private JTextField tableNumberField;
    private JTextField seatCountField;
    private JLabel statusLabel;

    public AssignTablePanel(RestaurantManagerPanel app, Server server) {
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

                Session session = new Session(LocalDateTime.now(), server, 0.0, true, new int[]{tableNumber});

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
    private RestaurantManagerPanel app;
    private JPanel seatsPanel;

    public SeatSelectPanel(RestaurantManagerPanel app) {
        this.app = app;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Select a Seat", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        seatsPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        add(seatsPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        seatsPanel.removeAll();
        int seatCount = app.getSeatCount();

        for (int i = 1; i <= seatCount; i++) {
            JButton seatBtn = new JButton("Seat " + i);
            seatBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, ((JButton) e.getSource()).getText() + " selected (TODO: go to order entry)");
                app.switchTo("EditItem");
            });
            seatsPanel.add(seatBtn);
        }

        revalidate();
        repaint();
    }
}

class EditItemPanel extends JPanel {
    public EditItemPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Edit Item Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

class OrderHistoryPanel extends JPanel {
    public OrderHistoryPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Order History Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

class TopItemsPanel extends JPanel {
    public TopItemsPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Top Items Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

class TableOverviewPanel extends JPanel {
    public TableOverviewPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Table Overview Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

class TipsPanel extends JPanel {
    public TipsPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Tips Panel", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
