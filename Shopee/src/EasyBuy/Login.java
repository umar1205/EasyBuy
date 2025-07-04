package EasyBuy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.io.*;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


/**
 *
 * @author User
 */
public class Login extends javax.swing.JFrame {
    CardLayout cardLayout;
    int barang = 1;
    int hargaBaru = 0;
    int harga = 0;
    int IDTransaksi = 1;
    int ID_Akun_Pembeli = 1;
    int ID_Akun_Penjual = 1;    
    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        cardLayout = (CardLayout)(jPanel3.getLayout());
        cardLayout.show(jPanel3, "cardLogin");
    }
    
    public void tambahUser(int ID_Akun_Pembeli, String username, String password, Object role) {
    try {
        java.sql.Connection conn = KoneksiDB.getConnection();
        String sql;
        PreparedStatement stmt = null;

        if (role.equals("User")) {
            sql = "INSERT INTO Akun_EasyBuyPembeli (ID_Akun_Pembeli, Username, Password) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ID_Akun_Pembeli);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
            stmt.close();
        }

        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menyimpan akun: " + e.getMessage());
    }
}
    
    public void tambahUserToko(int ID_Akun_Penjual, String username, String password, Object role) {
    try {
        java.sql.Connection conn = KoneksiDB.getConnection();
        String sql;
        PreparedStatement stmt = null;

        if (role.equals("Toko")) {
            sql = "INSERT INTO Akun_EasyBuyPenjual (ID_Akun_Penjual, Username, Password, List_Barang_Dijual) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ID_Akun_Penjual);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, "");
            stmt.executeUpdate();
            stmt.close();
        }

        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menyimpan akun: " + e.getMessage());
        }
    }
    
    public void cekUserLogin(String username, String password) {
    try {
        Connection conn = KoneksiDB.getConnection();

        // Cek ke tabel pembeli dulu
        String sqlPembeli = "SELECT * FROM Akun_EasyBuyPembeli WHERE Username=? AND Password=?";
        PreparedStatement stmtPembeli = conn.prepareStatement(sqlPembeli);
        stmtPembeli.setString(1, username);
        stmtPembeli.setString(2, password);

        ResultSet rsPembeli = stmtPembeli.executeQuery();

        if (rsPembeli.next()) {
            JOptionPane.showMessageDialog(this, "Login berhasil sebagai Pembeli!");
            cardLayout.show(jPanel3, "cardBeranda");
            return;
        }

        // Kalau tidak ditemukan di pembeli, cek ke penjual
        String sqlPenjual = "SELECT * FROM Akun_EasyBuyPenjual WHERE Username=? AND Password=?";
        PreparedStatement stmtPenjual = conn.prepareStatement(sqlPenjual);
        stmtPenjual.setString(1, username);
        stmtPenjual.setString(2, password);

        ResultSet rsPenjual = stmtPenjual.executeQuery();

        if (rsPenjual.next()) {
            JOptionPane.showMessageDialog(this, "Login berhasil sebagai Penjual!");
            cardLayout.show(jPanel3, "cardBerandaToko");
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah!");
        }

        // Tutup semua koneksi
        rsPembeli.close();
        rsPenjual.close();
        stmtPembeli.close();
        stmtPenjual.close();
        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal login: " + e.getMessage());
    }
}
    
    public boolean cekUserAvail(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = KoneksiDB.getConnection();
            String sql = "SELECT 1 FROM Akun_EasyBuyPenjual WHERE Username = ? " + "UNION " + "SELECT 1 FROM Akun_EasyBuyPembeli WHERE Username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, username);
            
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Gagal cek username: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    
    public int tambahIDAkunPembeli(Connection conn) {
        int idBaru = 1;
        try {
            String sql = "SELECT MAX(ID_Akun_Pembeli) AS max_id FROM Akun_EasyBuyPembeli";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                idBaru = rs.getInt("max_id") + 1;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idBaru;
    }
    
    public int tambahIDAkunPenjual(Connection conn) {
        int idBaru = 1;
        try {
            String sql = "SELECT MAX(ID_Akun_Penjual) AS max_id FROM Akun_EasyBuyPenjual";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                idBaru = rs.getInt("max_id") + 1;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idBaru;
    }
    
    public int tambahBarang() {
        return barang++;
    }
    
    public int kurangBarang() {
        return barang--;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        panelLogin = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jUsername2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jIsiUsernamefield2 = new javax.swing.JTextField();
        jPasswordField3 = new javax.swing.JPasswordField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        panelBeranda = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton27 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        panelRegister = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel113 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        panelBerandaToko = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        panelListBarang = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        panelPenghasilan = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        panelTemplatePembayaran = new javax.swing.JPanel();
        jButton43 = new javax.swing.JButton();
        jLabel132 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jButton44 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        panelTemplateDetail = new javax.swing.JPanel();
        jButton33 = new javax.swing.JButton();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        panelKeranjang = new javax.swing.JPanel();
        jLabel149 = new javax.swing.JLabel();
        jButton47 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        panelTransaksi = new javax.swing.JPanel();
        jLabel150 = new javax.swing.JLabel();
        jButton48 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton16 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        panelPengiriman = new javax.swing.JPanel();
        jLabel151 = new javax.swing.JLabel();
        jButton49 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setPreferredSize(new java.awt.Dimension(545, 535));
        jPanel3.setLayout(new java.awt.CardLayout());

        panelLogin.setBackground(new java.awt.Color(204, 204, 255));
        panelLogin.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel11.setFont(new java.awt.Font("Segoe Print", 1, 48)); // NOI18N
        jLabel11.setText("Welcome");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("Please insert your Username and Password");

        jUsername2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jUsername2.setText("Username");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Password");

        jIsiUsernamefield2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jIsiUsernamefield2.setPreferredSize(new java.awt.Dimension(65, 35));

        jPasswordField3.setPreferredSize(new java.awt.Dimension(65, 35));

        jButton5.setText("Login");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Register");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("If you don't have an account, please register");

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLoginLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jIsiUsernamefield2, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                            .addComponent(jPasswordField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jUsername2)
                            .addComponent(jButton6)
                            .addComponent(jLabel14)
                            .addComponent(jLabel13)
                            .addComponent(jButton5)))
                    .addGroup(panelLoginLayout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jLabel11))
                    .addGroup(panelLoginLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(jLabel12)))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(26, 26, 26)
                .addComponent(jUsername2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jIsiUsernamefield2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addGap(79, 79, 79))
        );

        jPanel3.add(panelLogin, "cardLogin");

        panelBeranda.setBackground(new java.awt.Color(204, 204, 255));
        panelBeranda.setPreferredSize(new java.awt.Dimension(545, 535));

        jButton2.setText("Log Out");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/kok.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mouse2.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mouse.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/Screenshot 2025-06-02 024816.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mousepad1.png"))); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mousepad.png"))); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Keyboard");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Keyboard");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Mouse");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Mouse");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Mousepad");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Mousepad");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BERANDA");

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/shopping-cart (1).png"))); // NOI18N
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/express-delivery.png"))); // NOI18N
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBerandaLayout = new javax.swing.GroupLayout(panelBeranda);
        panelBeranda.setLayout(panelBerandaLayout);
        panelBerandaLayout.setHorizontalGroup(
            panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBerandaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBerandaLayout.createSequentialGroup()
                        .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBerandaLayout.createSequentialGroup()
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBerandaLayout.createSequentialGroup()
                                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBerandaLayout.createSequentialGroup()
                                        .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel6))
                                        .addGap(82, 82, 82)
                                        .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel9)))
                                    .addGroup(panelBerandaLayout.createSequentialGroup()
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBerandaLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16))
                                    .addGroup(panelBerandaLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel15))))))
                        .addGap(0, 27, Short.MAX_VALUE))
                    .addGroup(panelBerandaLayout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(70, 70, 70)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelBerandaLayout.setVerticalGroup(
            panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBerandaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton27)
                    .addComponent(jButton30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel15)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBerandaLayout.createSequentialGroup()
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(jLabel16))
                .addGap(5, 5, 5)
                .addGroup(panelBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jPanel3.add(panelBeranda, "cardBeranda");

        panelRegister.setBackground(new java.awt.Color(204, 204, 255));
        panelRegister.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hello..");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Name");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Please, insert your data!");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Username");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Password");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Confirm Password");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jPasswordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField2ActionPerformed(evt);
            }
        });

        jButton1.setText("Done");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 204, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("X");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel113.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel113.setText("Tujuan Akun");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User", "Toko" }));

        javax.swing.GroupLayout panelRegisterLayout = new javax.swing.GroupLayout(panelRegister);
        panelRegister.setLayout(panelRegisterLayout);
        panelRegisterLayout.setHorizontalGroup(
            panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRegisterLayout.createSequentialGroup()
                .addGap(0, 136, Short.MAX_VALUE)
                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegisterLayout.createSequentialGroup()
                        .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelRegisterLayout.createSequentialGroup()
                                    .addGap(81, 81, 81)
                                    .addComponent(jLabel1))
                                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4)
                                    .addComponent(jPasswordField1)
                                    .addComponent(jPasswordField2)
                                    .addComponent(jLabel113)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRegisterLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(42, 42, 42)))
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelRegisterLayout.setVerticalGroup(
            panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegisterLayout.createSequentialGroup()
                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegisterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegisterLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel113)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanel3.add(panelRegister, "cardRegister");
        panelRegister.getAccessibleContext().setAccessibleParent(jPanel3);

        panelBerandaToko.setBackground(new java.awt.Color(204, 204, 255));
        panelBerandaToko.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Beranda Toko");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/product.png"))); // NOI18N
        jButton14.setText("List Barang Toko");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/bar-chart.png"))); // NOI18N
        jButton15.setText("Penghasilan");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton12.setText("LOG OUT");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBerandaTokoLayout = new javax.swing.GroupLayout(panelBerandaToko);
        panelBerandaToko.setLayout(panelBerandaTokoLayout);
        panelBerandaTokoLayout.setHorizontalGroup(
            panelBerandaTokoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBerandaTokoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBerandaTokoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelBerandaTokoLayout.createSequentialGroup()
                        .addGroup(panelBerandaTokoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBerandaTokoLayout.createSequentialGroup()
                                .addComponent(jButton12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                            .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        panelBerandaTokoLayout.setVerticalGroup(
            panelBerandaTokoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBerandaTokoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton12)
                .addGap(22, 22, 22)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(299, Short.MAX_VALUE))
        );

        jPanel3.add(panelBerandaToko, "cardBerandaToko");

        panelListBarang.setBackground(new java.awt.Color(204, 204, 255));
        panelListBarang.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("List Barang");

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/kok.png"))); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton19.setText("BACK");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mousepad1.png"))); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mouse.png"))); // NOI18N
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mousepad.png"))); // NOI18N
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/Screenshot 2025-06-02 024816.png"))); // NOI18N
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/mouse2.png"))); // NOI18N
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelListBarangLayout = new javax.swing.GroupLayout(panelListBarang);
        panelListBarang.setLayout(panelListBarangLayout);
        panelListBarangLayout.setHorizontalGroup(
            panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelListBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelListBarangLayout.createSequentialGroup()
                        .addComponent(jButton19)
                        .addGap(125, 125, 125)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelListBarangLayout.createSequentialGroup()
                        .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))))
        );
        panelListBarangLayout.setVerticalGroup(
            panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListBarangLayout.createSequentialGroup()
                .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelListBarangLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jButton19))
                    .addGroup(panelListBarangLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21)))
                .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelListBarangLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelListBarangLayout.createSequentialGroup()
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1, Short.MAX_VALUE))
                            .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelListBarangLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelListBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelListBarangLayout.createSequentialGroup()
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addGap(35, 35, 35))
        );

        jPanel3.add(panelListBarang, "cardListBarang");
        panelListBarang.getAccessibleContext().setAccessibleName("");

        panelPenghasilan.setBackground(new java.awt.Color(204, 204, 255));
        panelPenghasilan.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Penghasilan");

        jButton24.setText("BACK");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Barang terjual : 1");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Penghasilan : 300000");

        javax.swing.GroupLayout panelPenghasilanLayout = new javax.swing.GroupLayout(panelPenghasilan);
        panelPenghasilan.setLayout(panelPenghasilanLayout);
        panelPenghasilanLayout.setHorizontalGroup(
            panelPenghasilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPenghasilanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPenghasilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addGroup(panelPenghasilanLayout.createSequentialGroup()
                        .addGroup(panelPenghasilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelPenghasilanLayout.createSequentialGroup()
                                .addComponent(jButton24)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        panelPenghasilanLayout.setVerticalGroup(
            panelPenghasilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPenghasilanLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jButton24)
                .addGap(22, 22, 22)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(337, Short.MAX_VALUE))
        );

        jPanel3.add(panelPenghasilan, "cardPenghasilan");

        panelTemplatePembayaran.setBackground(new java.awt.Color(204, 204, 255));
        panelTemplatePembayaran.setPreferredSize(new java.awt.Dimension(545, 535));

        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/add-to-cart.png"))); // NOI18N
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jLabel132.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel132.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/kok.png"))); // NOI18N
        jLabel132.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel133.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel133.setText("A1");

        jLabel134.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel134.setText("Rainbow Keyboard");

        jLabel136.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel136.setText("ID Barang      :");

        jLabel137.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel137.setText("Total Harga   :");

        jLabel138.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel138.setText("Rp. 50.000");

        jLabel140.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel140.setText("Total Barang :");

        jLabel141.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel141.setText("1");

        jButton44.setText("Back");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton13.setText("Tambah");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton45.setText("Kurang");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTemplatePembayaranLayout = new javax.swing.GroupLayout(panelTemplatePembayaran);
        panelTemplatePembayaran.setLayout(panelTemplatePembayaranLayout);
        panelTemplatePembayaranLayout.setHorizontalGroup(
            panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTemplatePembayaranLayout.createSequentialGroup()
                .addGap(0, 155, Short.MAX_VALUE)
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel136, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel137, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel133)
                    .addComponent(jLabel138)
                    .addComponent(jLabel141))
                .addGap(151, 151, 151))
            .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton44)
                        .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(jLabel132, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTemplatePembayaranLayout.setVerticalGroup(
            panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                        .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTemplatePembayaranLayout.createSequentialGroup()
                        .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jLabel132, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel140)
                    .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel137)
                    .addComponent(jLabel138))
                .addGap(39, 39, 39)
                .addGroup(panelTemplatePembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelTemplatePembayaranLayout.createSequentialGroup()
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton45))
                    .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        jPanel3.add(panelTemplatePembayaran, "cardTemplatePembayaran");

        panelTemplateDetail.setBackground(new java.awt.Color(204, 204, 255));
        panelTemplateDetail.setPreferredSize(new java.awt.Dimension(545, 535));

        jButton33.setText("Back");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/EasyBuy/kok.png"))); // NOI18N
        jLabel63.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel64.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel64.setText("A1");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel65.setText("Rainbow Keyboard");

        jLabel66.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel66.setText("Nama Barang:");

        jLabel67.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel67.setText("ID Barang      :");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel68.setText("Harga            :");

        jLabel69.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel69.setText("Rp. 50.000");

        jLabel70.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setText("Detail Barang");

        javax.swing.GroupLayout panelTemplateDetailLayout = new javax.swing.GroupLayout(panelTemplateDetail);
        panelTemplateDetail.setLayout(panelTemplateDetailLayout);
        panelTemplateDetailLayout.setHorizontalGroup(
            panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTemplateDetailLayout.createSequentialGroup()
                .addGap(0, 110, Short.MAX_VALUE)
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel66)
                    .addComponent(jLabel67, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65)
                    .addComponent(jLabel69))
                .addGap(124, 124, 124))
            .addGroup(panelTemplateDetailLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton33)
                .addGap(100, 100, 100)
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTemplateDetailLayout.setVerticalGroup(
            panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemplateDetailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelTemplateDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(jLabel69))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jPanel3.add(panelTemplateDetail, "cardTemplateDetail");

        panelKeranjang.setBackground(new java.awt.Color(204, 204, 255));
        panelKeranjang.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel149.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel149.setText("Keranjang");

        jButton47.setText("Back");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Barang", "Nama Barang", "Total Barang", "Total Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton28.setText("Check Out");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setText("Hapus");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKeranjangLayout = new javax.swing.GroupLayout(panelKeranjang);
        panelKeranjang.setLayout(panelKeranjangLayout);
        panelKeranjangLayout.setHorizontalGroup(
            panelKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKeranjangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKeranjangLayout.createSequentialGroup()
                        .addComponent(jButton29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton28)
                        .addGap(32, 32, 32))
                    .addGroup(panelKeranjangLayout.createSequentialGroup()
                        .addGroup(panelKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                            .addGroup(panelKeranjangLayout.createSequentialGroup()
                                .addComponent(jButton47)
                                .addGap(91, 91, 91)
                                .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        panelKeranjangLayout.setVerticalGroup(
            panelKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKeranjangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel149, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKeranjangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton28)
                    .addComponent(jButton29))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.add(panelKeranjang, "cardKeranjang");

        panelTransaksi.setBackground(new java.awt.Color(204, 204, 255));
        panelTransaksi.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel150.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel150.setText("Transaksi");

        jButton48.setText("Back");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Jumlah yang harus dibayar : Rp. 50000");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Ketentuan Pembayaran :\n1. Pembayaran dilakukan secara langsung setelah mengkonfirmasi untuk checkout barang\n2. Pastikan sudah sesuai dengan nominal yang harus dibayarkan\n3. Jika pembayaran kurang dari nominal yang harus dibayarkan, maka pembayaran di cancel\n4. Jika pembayaran lebih dari nominal yang harus dibayarkan, maka langsung dikembalikan\n5. Jika sudah bayar, tidak dapat di cancel\n");
        jScrollPane2.setViewportView(jTextArea1);

        jButton16.setText("Bayar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Total Barang : 1");

        javax.swing.GroupLayout panelTransaksiLayout = new javax.swing.GroupLayout(panelTransaksi);
        panelTransaksi.setLayout(panelTransaksiLayout);
        panelTransaksiLayout.setHorizontalGroup(
            panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransaksiLayout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(jButton16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelTransaksiLayout.createSequentialGroup()
                        .addComponent(jButton48)
                        .addGap(91, 91, 91)
                        .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 163, Short.MAX_VALUE))
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelTransaksiLayout.setVerticalGroup(
            panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton16)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel3.add(panelTransaksi, "cardTransaksi");

        panelPengiriman.setBackground(new java.awt.Color(204, 204, 255));
        panelPengiriman.setPreferredSize(new java.awt.Dimension(545, 535));

        jLabel151.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel151.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel151.setText("Pengiriman");

        jButton49.setText("Back");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Transaksi", "Total Harga", "Alamat Tujuan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout panelPengirimanLayout = new javax.swing.GroupLayout(panelPengiriman);
        panelPengiriman.setLayout(panelPengirimanLayout);
        panelPengirimanLayout.setHorizontalGroup(
            panelPengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPengirimanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                    .addGroup(panelPengirimanLayout.createSequentialGroup()
                        .addComponent(jButton49)
                        .addGap(91, 91, 91)
                        .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPengirimanLayout.setVerticalGroup(
            panelPengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPengirimanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel151, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(panelPengiriman, "cardPengiriman");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String username = jIsiUsernamefield2.getText();
        String password = new String(jPasswordField3.getPassword());
        Object role = "";
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            jIsiUsernamefield2.setText("");
            jPasswordField3.setText("");
        } else {
            cekUserLogin(username,password);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardRegister");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Connection conn = null;
        try {
            conn = KoneksiDB.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        String name = jTextField3.getText();
        String username = jTextField4.getText();
        String password = new String(jPasswordField1.getPassword());
        String confPassword = new String(jPasswordField2.getPassword());
        Object role = jComboBox1.getSelectedItem();
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data belum selesai di input", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField3.setText("");
            jTextField4.setText("");
            jPasswordField1.setText("");
            jPasswordField2.setText("");
        } else if (cekUserAvail(username)) {
            JOptionPane.showMessageDialog(this, "Username telah terdaftar", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField4.setText("");
        } else if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username harus lebih dari 3 huruf atau sama dengan 3 huruf!", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField4.setText("");
        } else if (username.contains(" ")) {
            JOptionPane.showMessageDialog(this, "Username tidak boleh ada spasi", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField4.setText("");
        } else if (username.equals(password)) {
            JOptionPane.showMessageDialog(this, "Username dan password tidak boleh sama", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField4.setText("");
            jPasswordField1.setText("");
            jPasswordField2.setText("");
        } else if (!password.equals(confPassword)) {
            JOptionPane.showMessageDialog(this, "Password tidak sama", "Error", JOptionPane.ERROR_MESSAGE);
            jPasswordField1.setText("");
            jPasswordField2.setText("");
        } else if (password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password harus lebih dari 8 karakter", "Error", JOptionPane.ERROR_MESSAGE);
            jPasswordField1.setText("");
            jPasswordField2.setText("");
        } else {
            if (role.equals("User")) {
                int ID_Akun_Pembeli = tambahIDAkunPembeli(conn);
                tambahUser(ID_Akun_Pembeli, username, password, role);
                JOptionPane.showMessageDialog(this, "Akun berhasil dibuat", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(jPanel3, "cardLogin");
            } else if (role.equals("Toko")) {
                int ID_Akun_Penjual = tambahIDAkunPenjual(conn);
                tambahUserToko(ID_Akun_Penjual, username, password, role);
                JOptionPane.showMessageDialog(this, "Akun berhasil dibuat", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(jPanel3, "cardLogin");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardLogin");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jIsiUsernamefield2.setText("");
        jPasswordField3.setText("");
        JOptionPane.showMessageDialog(this, "Log out berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(jPanel3, "cardLogin");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        jLabel132.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/Screenshot 2025-06-02 024816.png")));
        jLabel132.setPreferredSize(new Dimension(184,204));
        jLabel134.setText("White Keyboard");
        jLabel133.setText("A2");
        jLabel138.setText("Rp. 40000");
        harga = 40000;
        cardLayout.show(jPanel3, "cardTemplatePembayaran");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        jLabel132.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mouse.png")));
        jLabel132.setPreferredSize(new Dimension(184,204));
        jLabel134.setText("Black Mouse");
        jLabel133.setText("B1");
        jLabel138.setText("Rp. 45000");
        harga = 45000;
        cardLayout.show(jPanel3, "cardTemplatePembayaran");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        jLabel132.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mousepad1.png")));
        jLabel132.setPreferredSize(new Dimension(184,204));
        jLabel134.setText("Pikachu Mousepad");
        jLabel133.setText("C2");
        jLabel138.setText("Rp. 25000");
        harga = 25000;
        cardLayout.show(jPanel3, "cardTemplatePembayaran");
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        jIsiUsernamefield2.setText("");
        jPasswordField3.setText("");
        JOptionPane.showMessageDialog(this, "Log out berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(jPanel3, "cardLogin");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardListBarang");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardBerandaToko");
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        jLabel63.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mousepad1.png")));
        jLabel63.setPreferredSize(new Dimension(184,204));
        jLabel65.setText("Pikachu Mousepad");
        jLabel64.setText("C2");
        jLabel69.setText("Rp. 25000");
        cardLayout.show(jPanel3, "cardTemplateDetail");
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        jLabel63.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mouse.png")));
        jLabel63.setPreferredSize(new Dimension(184,204));
        jLabel65.setText("Black Mouse");
        jLabel64.setText("B1");
        jLabel69.setText("Rp. 45000");
        cardLayout.show(jPanel3, "cardTemplateDetail");
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        jLabel63.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mousepad.png")));
        jLabel63.setPreferredSize(new Dimension(184,204));
        jLabel65.setText("White Mousepad");
        jLabel64.setText("C1");
        jLabel69.setText("Rp. 20000");
        cardLayout.show(jPanel3, "cardTemplateDetail");
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        jLabel63.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/Screenshot 2025-06-02 024816.png")));
        jLabel63.setPreferredSize(new Dimension(184,204));
        jLabel65.setText("White Keyboard");
        jLabel64.setText("A2");
        jLabel69.setText("Rp. 40000");
        cardLayout.show(jPanel3, "cardTemplateDetail");
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        jLabel63.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mouse2.png")));
        jLabel63.setPreferredSize(new Dimension(184,204));
        jLabel65.setText("Black Mouse Razer");
        jLabel64.setText("B2");
        jLabel69.setText("Rp. 55000");
        cardLayout.show(jPanel3, "cardTemplateDetail");
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        jLabel63.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/kok.png")));
        jLabel63.setPreferredSize(new Dimension(184,204));
        jLabel65.setText("Rainbow Keyboard");
        jLabel64.setText("A1");
        jLabel69.setText("Rp. 50000");
        cardLayout.show(jPanel3, "cardTemplateDetail");
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jLabel132.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/kok.png")));
        jLabel132.setPreferredSize(new Dimension(184,204));
        jLabel134.setText("Rainbow Keyboard");
        jLabel133.setText("A1");
        jLabel138.setText("Rp. 50000");
        harga = 50000;
        cardLayout.show(jPanel3, "cardTemplatePembayaran");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        jLabel132.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mouse2.png")));
        jLabel132.setPreferredSize(new Dimension(184,204));
        jLabel134.setText("Black Mouse Razer");
        jLabel133.setText("B2");
        jLabel138.setText("Rp. 55000");
        harga = 55000;
        cardLayout.show(jPanel3, "cardTemplatePembayaran");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        jLabel132.setIcon(new ImageIcon(getClass().getResource("/EasyBuy/mousepad.png")));
        jLabel132.setPreferredSize(new Dimension(184,204));
        jLabel134.setText("White Mousepad");
        jLabel133.setText("C1");
        jLabel138.setText("Rp. 20000");
        harga = 20000;
        cardLayout.show(jPanel3, "cardTemplatePembayaran");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardPenghasilan");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardBerandaToko");
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // TODO add your handling code here:
        barang = 1;
        String IDBarang = jLabel133.getText();
        String namaBarang = jLabel134.getText();
        int jumlahBaru = Integer.parseInt(jLabel141.getText());

        String hargaString = jLabel138.getText().replace("Rp. ", "").replace(".", "").trim();
        int hargaBaru = Integer.parseInt(hargaString);

        DefaultTableModel listKeranjang = (DefaultTableModel) jTable1.getModel();
        boolean ditemukan = false;

        for (int i = 0; i < listKeranjang.getRowCount(); i++) {
            String idTabel = listKeranjang.getValueAt(i, 0).toString();

            if (idTabel.equals(IDBarang)) {
            int jumlahLama = Integer.parseInt(listKeranjang.getValueAt(i, 2).toString());

            String hargaLamaStr = listKeranjang.getValueAt(i, 3).toString().replace("Rp. ", "").replace(".", "").trim();
            int hargaLama = Integer.parseInt(hargaLamaStr);

            int jumlahTotal = jumlahLama + jumlahBaru;
            int hargaTotal = hargaLama + hargaBaru;

            listKeranjang.setValueAt(jumlahTotal, i, 2);
            listKeranjang.setValueAt("Rp. " + hargaTotal, i, 3); // tampilkan kembali dalam format Rp.
            ditemukan = true;
            break;
        }
    }

    if (!ditemukan) {
        listKeranjang.addRow(new Object[] {IDBarang, namaBarang, jumlahBaru, "Rp. " + hargaBaru});
    }

        JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        jLabel141.setText(String.valueOf(barang));
        cardLayout.show(jPanel3, "cardBeranda");
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        // TODO add your handling code here:
        barang = 1;
        jLabel141.setText(String.valueOf(barang));
        cardLayout.show(jPanel3, "cardBeranda");
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        tambahBarang();
        hargaBaru = harga * barang;
        jLabel141.setText(String.valueOf(barang));
        jLabel138.setText("Rp. " + String.valueOf(hargaBaru));
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        // TODO add your handling code here:
        if (barang <= 1) {
            JOptionPane.showMessageDialog(this, "Tidak bisa kurang dari 1", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            kurangBarang();
            hargaBaru = harga * barang;
        }
        jLabel141.setText(String.valueOf(barang));
        jLabel138.setText("Rp. " + String.valueOf(hargaBaru));
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardListBarang");
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardBeranda");
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardKeranjang");
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardPengiriman");
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardKeranjang");
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Barang berhasil dihapus", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih dulu baris yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int kolomHarga = -1;
        int kolomBarang = -1;

        // Cari index kolom "Total Harga"
        int pilihan = JOptionPane.showConfirmDialog(this, "Apakah ingin melakukan checkout sekarang?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (pilihan == JOptionPane.YES_OPTION) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equalsIgnoreCase("Total Harga")) {
                kolomHarga = i;
            }
            if (model.getColumnName(i).equalsIgnoreCase("Total Barang")) {
                kolomBarang = i;
            }
        }

        if (kolomHarga == -1) {
            JOptionPane.showMessageDialog(this, "Kolom 'Total Harga' tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (kolomBarang == -1) {
            JOptionPane.showMessageDialog(this, "Kolom 'Total Barang' tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int totalHarga = 0;
        int totalBarang = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String hargaStr = model.getValueAt(i, kolomHarga).toString().replace("Rp. ", "").replace(".", "").trim();
            String jmlBarang = model.getValueAt(i, kolomBarang).toString();
            int barang = Integer.parseInt(jmlBarang);
            totalBarang += barang;
            int harga = Integer.parseInt(hargaStr);
            totalHarga += harga;
        }
        
        jLabel24.setText("Jumlah yang harus dibayar : Rp. " + totalHarga);
        jLabel25.setText("Total Barang : " + totalBarang);
        cardLayout.show(jPanel3, "cardTransaksi");
        } else {
            JOptionPane.showMessageDialog(this, "Checkout dibatalkan", "Batal", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        String totalHargaStr = jLabel24.getText().replace("Jumlah yang harus dibayar : Rp. ", "").trim();
        String totalBarangStr = jLabel25.getText().replace("Total Barang : ", "").trim();
        String alamatTujuan = JOptionPane.showInputDialog(this, "Masukkan alamat tujuan", "Konfirmasi alamat", JOptionPane.QUESTION_MESSAGE);
        if (alamatTujuan == null) {
            JOptionPane.showMessageDialog(this, "Dibatalkan", "Batal", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String jumlahBayarStr = JOptionPane.showInputDialog(this, "Masukkan jumlah yang ingin dibayar", "Konfirmasi pembayaran", JOptionPane.QUESTION_MESSAGE);
        if (jumlahBayarStr == null) {
            JOptionPane.showMessageDialog(this, "Dibatalkan", "Batal", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int totalHarga = Integer.parseInt(totalHargaStr);
            int totalBarang = Integer.parseInt(totalBarangStr);
            int jumlahBayar = Integer.parseInt(jumlahBayarStr);
            int jumlahTotalHarga = 0;
            int jumlahTotalBarang = 0;
            if(jumlahBayar == totalHarga || jumlahBayar > totalHarga) {
                DefaultTableModel keranjang = (DefaultTableModel) jTable1.getModel();
                keranjang.setRowCount(0);
                DefaultTableModel listPengiriman = (DefaultTableModel) jTable2.getModel();
                listPengiriman.addRow(new Object[] {IDTransaksi, totalHarga, alamatTujuan});
                IDTransaksi++;
                jumlahTotalHarga = jumlahTotalHarga + totalHarga;
                jumlahTotalBarang = jumlahTotalBarang + totalBarang;
                jLabel19.setText("Penghasilan : " + jumlahTotalHarga);
                jLabel18.setText("Barang terjual : " + jumlahTotalBarang);
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(jPanel3, "cardBeranda");
            } else if(jumlahBayar < totalHarga) {
                JOptionPane.showMessageDialog(this, "Nominal kurang. \nPembayaran dibatalkan", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid! \nMasukkan angka saja", "Error", JOptionPane.ERROR_MESSAGE);   
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(jPanel3, "cardBeranda");
    }//GEN-LAST:event_jButton49ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JTextField jIsiUsernamefield2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel jUsername2;
    private javax.swing.JPanel panelBeranda;
    private javax.swing.JPanel panelBerandaToko;
    private javax.swing.JPanel panelKeranjang;
    private javax.swing.JPanel panelListBarang;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JPanel panelPenghasilan;
    private javax.swing.JPanel panelPengiriman;
    private javax.swing.JPanel panelRegister;
    private javax.swing.JPanel panelTemplateDetail;
    private javax.swing.JPanel panelTemplatePembayaran;
    private javax.swing.JPanel panelTransaksi;
    // End of variables declaration//GEN-END:variables
}
