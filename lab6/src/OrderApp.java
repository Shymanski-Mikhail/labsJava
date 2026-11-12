import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.*;

public class OrderApp extends JFrame {

    private ResourceBundle rb;
    private Locale currentLocale;

    private JComboBox<String> langCombo;
    private JLabel titleLabel, langLabel;

    private JCheckBox cbAnimator, cbClown, cbSinger;
    private JButton giftDropdownBtn;
    private JPopupMenu giftMenu;
    private Map<JCheckBoxMenuItem, Integer> giftPrices;
    private Set<JCheckBoxMenuItem> selectedGifts;

    private JRadioButton rbConcertYes, rbConcertNo;
    private JRadioButton rbRegularYes, rbRegularNo;

    private JLabel totalLabel, dateShortLabel, dateLongLabel;
    private JButton btnSave;

    private JPanel panelCong, panelGift, panelConcert, panelRegular, panelLang;

    public OrderApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 750);
        setLayout(new GridLayout(0, 1, 10, 10));

        initComponents();
        setLanguage("ru", Locale.of("ru", "RU"));
        updateUI();

        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> updateDateTime());
        timer.start();
    }

    private void initComponents() {
        panelLang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        langLabel = new JLabel();
        langCombo = new JComboBox<>(new String[]{"ru_RU", "be_BY", "en_US"});
        langCombo.addActionListener(e -> {
            String selected = (String) langCombo.getSelectedItem();
            String[] parts = selected.split("_");
            setLanguage(parts[0], Locale.of(parts[0], parts[1]));
            updateUI();
        });
        panelLang.add(langLabel);
        panelLang.add(langCombo);
        add(panelLang);

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel);

        panelCong = new JPanel(new GridLayout(3, 1));
        panelCong.setBorder(BorderFactory.createTitledBorder(""));
        cbAnimator = new JCheckBox();
        cbClown = new JCheckBox();
        cbSinger = new JCheckBox();

        ActionListener priceUpdater = e -> {
            rebuildGiftsMenu();
            updatePrice();
        };
        cbAnimator.addActionListener(priceUpdater);
        cbClown.addActionListener(priceUpdater);
        cbSinger.addActionListener(priceUpdater);

        panelCong.add(cbAnimator);
        panelCong.add(cbClown);
        panelCong.add(cbSinger);
        add(panelCong);

        panelGift = new JPanel(new GridLayout(1, 2));
        panelGift.setBorder(BorderFactory.createTitledBorder(""));
        giftDropdownBtn = new JButton();
        giftDropdownBtn.addActionListener(e -> giftMenu.show(giftDropdownBtn, 0, giftDropdownBtn.getHeight()));
        giftMenu = new JPopupMenu();
        giftPrices = new HashMap<>();
        selectedGifts = new HashSet<>();
        panelGift.add(giftDropdownBtn);
        add(panelGift);

        panelConcert = new JPanel(new GridLayout(1, 2));
        panelConcert.setBorder(BorderFactory.createTitledBorder(""));
        rbConcertYes = new JRadioButton();
        rbConcertNo = new JRadioButton("", true);
        ButtonGroup bgConcert = new ButtonGroup();
        bgConcert.add(rbConcertYes);
        bgConcert.add(rbConcertNo);
        panelConcert.add(rbConcertYes);
        panelConcert.add(rbConcertNo);
        add(panelConcert);

        panelRegular = new JPanel(new GridLayout(1, 2));
        panelRegular.setBorder(BorderFactory.createTitledBorder(""));
        rbRegularYes = new JRadioButton();
        rbRegularNo = new JRadioButton("", true);
        ButtonGroup bgRegular = new ButtonGroup();
        bgRegular.add(rbRegularYes);
        bgRegular.add(rbRegularNo);
        panelRegular.add(rbRegularYes);
        panelRegular.add(rbRegularNo);
        add(panelRegular);

        rbRegularYes.addActionListener(e -> updatePrice());
        rbConcertYes.addActionListener(e -> updatePrice());
        rbRegularNo.addActionListener(e -> updatePrice());
        rbConcertNo.addActionListener(e -> updatePrice());

        totalLabel = new JLabel("", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 150, 0));
        add(totalLabel);

        dateShortLabel = new JLabel("", SwingConstants.CENTER);
        dateLongLabel = new JLabel("", SwingConstants.CENTER);
        add(dateShortLabel);
        add(dateLongLabel);

        btnSave = new JButton();
        btnSave.addActionListener(e -> saveOrder());
        add(btnSave);
    }

    private void setLanguage(String lang, Locale locale) {
        this.currentLocale = locale;
        Locale.setDefault(locale);
        try {
            this.rb = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            try {
                StringBuilder pathBuilder = new StringBuilder();
                pathBuilder.append("src/messages_").append(lang).append(".properties");
                InputStream is = new FileInputStream(pathBuilder.toString());
                this.rb = new PropertyResourceBundle(new InputStreamReader(is, StandardCharsets.UTF_8));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateUI() {
        if (rb == null) return;

        setTitle(rb.getString("title"));
        titleLabel.setText(rb.getString("title"));
        langLabel.setText(rb.getString("lang"));

        ((TitledBorder) panelCong.getBorder()).setTitle(rb.getString("congratulator"));
        ((TitledBorder) panelGift.getBorder()).setTitle(rb.getString("gift"));
        ((TitledBorder) panelConcert.getBorder()).setTitle(rb.getString("concert"));
        ((TitledBorder) panelRegular.getBorder()).setTitle(rb.getString("regular"));

        cbAnimator.setText(rb.getString("animator"));
        cbClown.setText(rb.getString("clown"));
        cbSinger.setText(rb.getString("singer"));

        rbConcertYes.setText(rb.getString("yes"));
        rbConcertNo.setText(rb.getString("no"));
        rbRegularYes.setText(rb.getString("regular"));
        rbRegularNo.setText(rb.getString("no"));
        btnSave.setText(rb.getString("save"));

        rebuildGiftsMenu();
        updatePrice();
        updateDateTime();
    }

    private void rebuildGiftsMenu() {
        giftMenu.removeAll();
        giftPrices.clear();
        selectedGifts.clear();

        boolean congSelected = cbAnimator.isSelected() || cbClown.isSelected() || cbSinger.isSelected();

        if (!congSelected) {
            giftDropdownBtn.setText(rb.getString("select_cong"));
            giftDropdownBtn.setEnabled(false);
            return;
        }

        giftDropdownBtn.setEnabled(true);
        giftDropdownBtn.setText(rb.getString("select_gift"));

        int basePrice = 100;
        if (cbSinger.isSelected()) basePrice = 300;
        else if (cbClown.isSelected()) basePrice = 150;

        addGiftItem(rb.getString("card"), basePrice);
        addGiftItem(rb.getString("flowers"), basePrice + 50);
        addGiftItem(rb.getString("medal"), basePrice + 100);

        if (cbSinger.isSelected()) {
            addGiftItem(rb.getString("autograph"), basePrice + 200);
        }
    }

    private void addGiftItem(String name, int price) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(price).append(")");

        JCheckBoxMenuItem item = new JCheckBoxMenuItem(sb.toString());
        item.addActionListener(e -> {
            if (item.isSelected()) {
                selectedGifts.add(item);
            } else {
                selectedGifts.remove(item);
            }
            updatePrice();
        });
        giftPrices.put(item, price);
        giftMenu.add(item);
    }

    private void updatePrice() {
        if (rb == null) return;

        int total = 0;

        if (cbAnimator.isSelected()) total += 50;
        if (cbClown.isSelected()) total += 70;
        if (cbSinger.isSelected()) total += 100;

        for (JCheckBoxMenuItem item : selectedGifts) {
            total += giftPrices.get(item);
        }

        if (rbConcertYes.isSelected()) total += 300;

        if (rbRegularYes.isSelected()) {
            total = (int) (total * 0.9);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(rb.getString("total"))
                .append(total)
                .append(" ")
                .append(rb.getString("currency"));

        totalLabel.setText(sb.toString());
    }

    private void updateDateTime() {
        if (rb == null) return;

        Date now = new Date();

        DateFormat shortFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
        DateFormat longFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, currentLocale);

        StringBuilder sbShort = new StringBuilder();
        sbShort.append(rb.getString("short_date")).append(shortFormat.format(now));

        StringBuilder sbLong = new StringBuilder();
        sbLong.append(rb.getString("long_date")).append(longFormat.format(now));

        dateShortLabel.setText(sbShort.toString());
        dateLongLabel.setText(sbLong.toString());
    }

    private void saveOrder() {
        if (rb == null) return;

        try (FileWriter writer = new FileWriter("order.txt", true)) {
            StringBuilder sb = new StringBuilder();
            sb.append("--- ").append(totalLabel.getText()).append(" ---\n");

            if (cbAnimator.isSelected()) sb.append(rb.getString("animator")).append("\n");
            if (cbClown.isSelected()) sb.append(rb.getString("clown")).append("\n");
            if (cbSinger.isSelected()) sb.append(rb.getString("singer")).append("\n");

            for (JCheckBoxMenuItem item : selectedGifts) {
                sb.append(item.getText()).append("\n");
            }

            sb.append(rb.getString("concert")).append(": ")
                    .append(rbConcertYes.isSelected() ? rb.getString("yes") : rb.getString("no"))
                    .append("\n");

            sb.append(dateShortLabel.getText()).append("\n\n");

            writer.write(sb.toString());

            JOptionPane.showMessageDialog(this, rb.getString("saved"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, rb.getString("error_save"));
        }
    }
}