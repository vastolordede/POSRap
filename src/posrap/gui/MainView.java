package posrap.gui;

import javax.swing.*;
import java.awt.*;

import posrap.util.SessionContext;
import posrap.util.UiUtil;

public class MainView extends BaseFrame {

    private final CardLayout card = new CardLayout();
    private final JPanel pnlContent = new JPanel(card);

    private static final String KEY_BAN_VE = "ban_ve";
    private static final String KEY_BAN_BAP_NUOC = "ban_bap_nuoc";
    private static final String KEY_QT_RAP = "quan_tri_rap";
    private static final String KEY_QT_BAP_NUOC = "quan_tri_bap_nuoc";
    private static final String KEY_QT_NHAN_SU = "quan_tri_nhan_su";
    private static final String KEY_BAO_CAO = "bao_cao";

    public MainView() {
        super("POS rap chieu phim - " + SessionContext.getVaiTro());
        setContentPane(buildUi());
    }

    private JPanel buildUi() {
        JPanel root = new JPanel(new BorderLayout());
        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildLeftMenu(), BorderLayout.WEST);
        root.add(buildContent(), BorderLayout.CENTER);

        if (SessionContext.isStaff()) card.show(pnlContent, KEY_BAN_VE);
        else card.show(pnlContent, KEY_QT_RAP);

        return root;
    }

    private JComponent buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel lbl = new JLabel("Xin chao: " + SessionContext.getHoTen() + " | Vai tro: " + SessionContext.getVaiTro());
        JButton btnDangXuat = new JButton("Dang xuat");

        btnDangXuat.addActionListener(e -> {
            if (!UiUtil.confirm(this, "Ban muon dang xuat?")) return;
            SessionContext.clear();
            LoginView login = new LoginView();
            login.setVisible(true);
            dispose();
        });

        top.add(lbl, BorderLayout.WEST);
        top.add(btnDangXuat, BorderLayout.EAST);
        return top;
    }

    private JComponent buildLeftMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menu.setPreferredSize(new Dimension(220, 0));

        JButton btnBanVe = new JButton("Ban ve");
        JButton btnBanBapNuoc = new JButton("Ban bap nuoc");
        btnBanVe.addActionListener(e -> card.show(pnlContent, KEY_BAN_VE));
        btnBanBapNuoc.addActionListener(e -> card.show(pnlContent, KEY_BAN_BAP_NUOC));

        JButton btnQtRap = new JButton("Quan tri rap");
        JButton btnQtBapNuoc = new JButton("Quan tri bap nuoc");
        JButton btnQtNhanSu = new JButton("Quan tri nhan su");
        JButton btnBaoCao = new JButton("Bao cao");

        btnQtRap.addActionListener(e -> card.show(pnlContent, KEY_QT_RAP));
        btnQtBapNuoc.addActionListener(e -> card.show(pnlContent, KEY_QT_BAP_NUOC));
        btnQtNhanSu.addActionListener(e -> card.show(pnlContent, KEY_QT_NHAN_SU));
        btnBaoCao.addActionListener(e -> card.show(pnlContent, KEY_BAO_CAO));

        if (SessionContext.isStaff()) {
            menu.add(btnBanVe);
            menu.add(Box.createVerticalStrut(8));
            menu.add(btnBanBapNuoc);
        } else {
            menu.add(btnQtRap);
            menu.add(Box.createVerticalStrut(8));
            menu.add(btnQtBapNuoc);
            menu.add(Box.createVerticalStrut(8));
            menu.add(btnQtNhanSu);
            menu.add(Box.createVerticalStrut(8));
            menu.add(btnBaoCao);
        }

        menu.add(Box.createVerticalGlue());
        return menu;
    }

    private JComponent buildContent() {
        pnlContent.add(new BanVeView(), KEY_BAN_VE);
        pnlContent.add(new BanBapNuocView(), KEY_BAN_BAP_NUOC);
        pnlContent.add(new QuanTriRapView(), KEY_QT_RAP);
        pnlContent.add(new QuanTriBapNuocView(), KEY_QT_BAP_NUOC);
        pnlContent.add(new QuanTriNhanSuView(), KEY_QT_NHAN_SU);
        pnlContent.add(new BaoCaoView(), KEY_BAO_CAO);
        return pnlContent;
    }
}
