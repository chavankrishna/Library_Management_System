/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;
import static jframe.DBConnection.con;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author krishna chavan
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form HomePage
     */
    
    Color mouseEnterColor=new Color(0,0,0);
    Color mouseExitColor=new Color(51,51,51);
    
    Color mouseEnterColor1=new Color(255,0,0);
    Color mouseExitColor1=new Color(0,255,255);
    
    Color mouseExitColor2=new Color(0,204,204);
    
    Color mouseEnterColor3=new Color(255,51,51);
    Color mouseExitColor3=new Color(0,255,255);
    
    DefaultTableModel model;
    
    public Home() {
        initComponents();
        showPieChart();
        setStudentDeatilsToTable();
        setBookDeatilsToTable();
        setDataToCards();
       // setAdmin();
    } 
    
    // set admin to label
    /* public void setAdmin()
      {
        LoginPage login=new LoginPage();
        String adm=login.admin();
        lbl_admin.setText(adm);
       }
    */
    
   // set record in student details 
   public void setStudentDeatilsToTable()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/liabrary_ms","root","Kr.92@#$");
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from student_details");
            
            while(rs.next())
            {
                int studentId=rs.getInt("student_id");
                String studentName=rs.getString("name");
                String course=rs.getString("course");
                String branch=rs.getString("branch");
                
                Object[] obj={studentId,studentName,course,branch};
                model=(DefaultTableModel) tbl_StudentDetails.getModel();
                model.addRow(obj);
                
            }
                     
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
   // set book details to table
    public void setBookDeatilsToTable()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/liabrary_ms","root","Kr.92@#$");
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from book_details");
            
            while(rs.next())
            {
                String bookId=rs.getString("book_id");
                String bookName=rs.getString("book_name");
                String bookAuthor=rs.getString("author");
                int quantity=rs.getInt("quantity");
                
                Object[] obj={bookId,bookName,bookAuthor,quantity};
                model=(DefaultTableModel) tbl_bookDetails.getModel();
                model.addRow(obj);
                
            }
                     
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public void setDataToCards()
    {
        Statement st=null;
        ResultSet rs=null;
        ResultSet rs1=null;
        
        long l = System.currentTimeMillis();
        Date todaysdate=new Date(l);
        
        try
        {
             Class.forName("com.mysql.jdbc.Driver");
             con=DriverManager.getConnection("jdbc:mysql://localhost:3306/liabrary_ms","root","Kr.92@#$");
            
             st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             
             
             rs1 = st.executeQuery("select SUM(quantity) from book_details");
             rs1.next();
             int totalBooks=rs1.getInt(1);
             lbl_noOfBooks.setText(Integer.toString(totalBooks));
             
             
           /*  rs = st.executeQuery("select * from book_details");
             rs.last();
             lbl_noOfBooks.setText(Integer.toString(rs.getRow()));
           */  
             rs = st.executeQuery("select * from student_details");
             rs.last();
             lbl_noOfStudents.setText(Integer.toString(rs.getRow()));
             
             rs = st.executeQuery("select * from issue_book_details");
             rs.last();
             lbl_issueBooks.setText(Integer.toString(rs.getRow()));
             
             rs = st.executeQuery("select * from issue_book_details where due_date < '"+todaysdate+"' and status='"+"pending"+"'");
             rs.last();
             lbl_defaulterList.setText(Integer.toString(rs.getRow()));
             
             
        }catch(Exception e)
        {
           e.printStackTrace();
        }
        
        
    }
    
   
   // record in pie chart
    public void showPieChart()
    {
        
        //create dataset
      DefaultPieDataset barDataset = new DefaultPieDataset( );
     
      try
      {
          Class.forName("com.mysql.jdbc.Driver");
          con=DriverManager.getConnection("jdbc:mysql://localhost:3306/liabrary_ms","root","Kr.92@#$");
            
          String sql = "select book_name,count(*) as issue_count from issue_book_details group by book_id";
          Statement st=con.createStatement();
          ResultSet rs = st.executeQuery(sql);
          while(rs.next())
          {
              barDataset.setValue(rs.getString("book_name"), Double.valueOf(rs.getDouble("issue_count")));
          }
          
      }catch(Exception e)
      {
          e.printStackTrace();
      }
      
      //create chart
       JFreeChart piechart = ChartFactory.createPieChart("Issue Book Details",barDataset, true,true,true);//explain
      
        PiePlot piePlot =(PiePlot) piechart.getPlot();
      
       //changing pie chart blocks colors
      //   piePlot.setSectionPaint("IPhone 5s", new Color(255,255,102));
      //  piePlot.setSectionPaint("SamSung Grand", new Color(102,255,102));
      //  piePlot.setSectionPaint("MotoG", new Color(255,102,153));
      //  piePlot.setSectionPaint("Nokia Lumia", new Color(0,204,204));
      
       
        piePlot.setBackgroundPaint(Color.white);
        
        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        panelPieChart.removeAll();
        panelPieChart.add(barChartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
    }
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbl_welcome = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        lbl_admin = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        lbl_defaulterList = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lbl_noOfBooks = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lbl_noOfStudents = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lbl_issueBooks = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_StudentDetails = new rojeru_san.complementos.RSTableMetro();
        panelPieChart = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(270, 60));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 5, 50));

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 51));
        jLabel4.setText("Library Management System");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 380, 40));

        lbl_welcome.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 20)); // NOI18N
        lbl_welcome.setForeground(new java.awt.Color(255, 0, 51));
        lbl_welcome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/male_user_50px.png"))); // NOI18N
        lbl_welcome.setText("Welcome , ");
        jPanel1.add(lbl_welcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 0, 160, 70));

        jPanel19.setBackground(new java.awt.Color(0, 255, 255));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 30)); // NOI18N
        jLabel2.setText(" X");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1480, 0, 50, 40));

        jPanel20.setBackground(new java.awt.Color(0, 255, 255));
        jPanel20.setForeground(new java.awt.Color(0, 255, 255));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_admin.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 20)); // NOI18N
        lbl_admin.setForeground(new java.awt.Color(255, 0, 51));
        lbl_admin.setText(" Admin");
        jPanel20.add(lbl_admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 70));

        jPanel1.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 0, 150, 70));

        jPanel3.setBackground(new java.awt.Color(0, 251, 251));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_menu_48px_1.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1530, 70));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 25)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 51));
        jLabel3.setText("Liabrary Management System");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 380, 40));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(0, 204, 204)));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_defaulterList.setFont(new java.awt.Font("Segoe UI Black", 0, 50)); // NOI18N
        lbl_defaulterList.setForeground(new java.awt.Color(102, 102, 102));
        lbl_defaulterList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_List_of_Thumbnails_50px.png"))); // NOI18N
        lbl_defaulterList.setText("  10");
        jPanel14.add(lbl_defaulterList, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 150, 50));

        jPanel16.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 70, 300, 140));

        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_noOfBooks.setFont(new java.awt.Font("Segoe UI Black", 0, 50)); // NOI18N
        lbl_noOfBooks.setForeground(new java.awt.Color(102, 102, 102));
        lbl_noOfBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Book_Shelf_50px.png"))); // NOI18N
        lbl_noOfBooks.setText("  10");
        jPanel15.add(lbl_noOfBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 150, 50));

        jPanel16.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 300, 140));

        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(0, 204, 204)));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_noOfStudents.setFont(new java.awt.Font("Segoe UI Black", 0, 50)); // NOI18N
        lbl_noOfStudents.setForeground(new java.awt.Color(102, 102, 102));
        lbl_noOfStudents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_People_50px.png"))); // NOI18N
        lbl_noOfStudents.setText("  10");
        jPanel17.add(lbl_noOfStudents, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 150, 50));

        jPanel16.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 300, 140));

        jPanel18.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_issueBooks.setFont(new java.awt.Font("Segoe UI Black", 0, 50)); // NOI18N
        lbl_issueBooks.setForeground(new java.awt.Color(102, 102, 102));
        lbl_issueBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Sell_50px.png"))); // NOI18N
        lbl_issueBooks.setText("  10");
        jPanel18.add(lbl_issueBooks, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 150, 50));

        jPanel16.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, 300, 140));

        jLabel22.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setText("Books Details");
        jPanel16.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 240, -1));

        jLabel23.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Number of Students");
        jPanel16.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 240, -1));

        tbl_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book Id", "Book Name", "Author", "Quantity"
            }
        ));
        tbl_bookDetails.setColorBackgoundHead(new java.awt.Color(0, 153, 153));
        tbl_bookDetails.setColorBordeFilas(new java.awt.Color(0, 153, 153));
        tbl_bookDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_bookDetails.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tbl_bookDetails.setFont(new java.awt.Font("Yu Gothic Light", 0, 25)); // NOI18N
        tbl_bookDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_bookDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_bookDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_bookDetails.setRowHeight(30);
        jScrollPane1.setViewportView(tbl_bookDetails);

        jPanel16.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 790, 190));

        jLabel25.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Number of Books");
        jPanel16.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 240, -1));

        jLabel26.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Students Details");
        jPanel16.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 240, -1));

        tbl_StudentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student Id", "Name", "Course", "Branch"
            }
        ));
        tbl_StudentDetails.setColorBackgoundHead(new java.awt.Color(0, 153, 153));
        tbl_StudentDetails.setColorBordeFilas(new java.awt.Color(0, 153, 153));
        tbl_StudentDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_StudentDetails.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        tbl_StudentDetails.setFont(new java.awt.Font("Yu Gothic Light", 0, 25)); // NOI18N
        tbl_StudentDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_StudentDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_StudentDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_StudentDetails.setRowHeight(30);
        jScrollPane2.setViewportView(tbl_StudentDetails);

        jPanel16.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 790, 200));

        panelPieChart.setLayout(new java.awt.BorderLayout());
        jPanel16.add(panelPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 250, 660, 490));

        jLabel24.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Issued Books");
        jPanel16.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 30, 240, -1));

        jLabel17.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Defaulter list");
        jPanel16.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 30, 240, -1));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1530, 750));

        setSize(new java.awt.Dimension(1530, 821));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jPanel19.setBackground(mouseEnterColor1);
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jPanel19.setBackground(mouseExitColor1);
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        HomePage hp=new HomePage();
        hp.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        jPanel3.setBackground(mouseEnterColor3);
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jPanel3.setBackground(mouseExitColor3);
    }//GEN-LAST:event_jLabel1MouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_admin;
    private javax.swing.JLabel lbl_defaulterList;
    private javax.swing.JLabel lbl_issueBooks;
    private javax.swing.JLabel lbl_noOfBooks;
    private javax.swing.JLabel lbl_noOfStudents;
    private javax.swing.JLabel lbl_welcome;
    private javax.swing.JPanel panelPieChart;
    private rojeru_san.complementos.RSTableMetro tbl_StudentDetails;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    // End of variables declaration//GEN-END:variables
}
