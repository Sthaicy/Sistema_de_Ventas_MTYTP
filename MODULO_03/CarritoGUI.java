/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package TiendaVirtual;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class CarritoGUI extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CarritoGUI.class.getName());

    private java.util.ArrayList<Integer> idsPuntos = new java.util.ArrayList<>();
    private java.util.ArrayList<Integer> idsHorarios = new java.util.ArrayList<>();
    private CatalogoGUI catalogoPadre;
    private int idClienteActual = 1; 
    private int idCarritoActual = -1;
    
    private String clienteNombreGlobal = "";
    private String clienteCiGlobal = "";
            
    /**
     * Creates new form CarritoGUI
     */
    public CarritoGUI() {
        initComponents();
    }
    public CarritoGUI(CatalogoGUI catalogoPadre) {
        initComponents();
        actualizarCarrito();
        cargarDatosCliente();
        cargarModalidades();
        modalidadComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String modalidadSeleccionada = modalidadComboBox.getSelectedItem() != null ? modalidadComboBox.getSelectedItem().toString() : "";
                if (modalidadSeleccionada.equalsIgnoreCase("RECOJO_EN_TIENDA") || modalidadSeleccionada.equalsIgnoreCase("PUNTO_Y_HORARIO")) {
                    nombreRetiraTextField.setText(clienteNombreGlobal);
                    ciRetiraTextField.setText(clienteCiGlobal);
                    ciRetiraTextField.setEnabled(true);
                    nombreRetiraTextField.setEnabled(true);
                    nombreRetiraTextField.setEditable(false);
                    ciRetiraTextField.setEditable(false);
                    if (modalidadSeleccionada.equalsIgnoreCase("PUNTO_Y_HORARIO")) {
                    puntoComboBox.setEnabled(true);
                    horarioComboBox.setEnabled(true);
                    cargarPuntosEntrega(); // Cargamos los puntos al seleccionar esta modalidad
                    } else {
                        puntoComboBox.setEnabled(false);
                        horarioComboBox.setEnabled(false);
                        puntoComboBox.removeAllItems();
                        horarioComboBox.removeAllItems();
                    }
                } else {
                    nombreRetiraTextField.setText("");
                    ciRetiraTextField.setText("");
                    nombreRetiraTextField.setEnabled(false);
                    ciRetiraTextField.setEnabled(false);
                }
            }
        });
        
        String modalidadSeleccionada = modalidadComboBox.getSelectedItem() != null ? modalidadComboBox.getSelectedItem().toString() : "";

    if (modalidadSeleccionada.equalsIgnoreCase("PUNTO_Y_HORARIO")) {
        puntoComboBox.setEnabled(true);
        horarioComboBox.setEnabled(true);
        cargarPuntosEntrega(); // Cargamos los puntos al seleccionar esta modalidad
    } else {
        puntoComboBox.setEnabled(false);
        horarioComboBox.setEnabled(false);
        puntoComboBox.removeAllItems();
        horarioComboBox.removeAllItems();
    }  
        this.catalogoPadre = catalogoPadre;
        setLocationRelativeTo(null); // Centrar ventana
        
    }
    
    private void cargarDatosCliente() {
        String sql = "SELECT nombre, ci FROM clientes WHERE id_cliente = ?";
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idClienteActual);
            ResultSet rs = pst.executeQuery();    
            if (rs.next()) {
                clienteNombreGlobal = rs.getString("nombre");
                clienteCiGlobal = rs.getString("ci");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar datos del cliente: " + e.getMessage());
        }
    }
    public void actualizarCarrito() {
        String[] titulos = {"ID Detalle", "Nombre Prod.", "Precio (Bs)", "Cantidad", "Subtotal (Bs)"};
        DefaultTableModel modeloCarrito = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        double totalAcumulado = 0.0;
        idCarritoActual = -1;
        String sql = "SELECT dc.id_detalle_carrito, p.nombre, dc.precio_unitario, dc.cantidad, " +
                     "(dc.cantidad * dc.precio_unitario) AS subtotal, c.id_carrito " +
                     "FROM carritos c " +
                     "JOIN detalle_carrito dc ON c.id_carrito = dc.id_carrito " +
                     "JOIN productos p ON dc.id_producto = p.id_producto " +
                     "WHERE c.id_cliente = ? AND c.estado = 'ACTIVO'";

        try (Connection conn = MyConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, idClienteActual);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                idCarritoActual = rs.getInt("id_carrito"); // Guardamos el ID del carrito activo
                int idDetalle = rs.getInt("id_detalle_carrito");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio_unitario");
                int cantidad = rs.getInt("cantidad");
                double subtotal = rs.getDouble("subtotal");

                totalAcumulado += subtotal;

                Object[] fila = {idDetalle, nombre, precio, cantidad, subtotal};
                modeloCarrito.addRow(fila);
            }
            carritoTable.setModel(modeloCarrito);
            totalTextField.setText(String.format("%.2f", totalAcumulado));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar el carrito: " + e.getMessage());
        }
    }
    private void cargarModalidades() {
        javax.swing.DefaultComboBoxModel<String> modeloCombo = new javax.swing.DefaultComboBoxModel<>();
        String SQL = "SELECT id_modalidad_entrega, nombre FROM modalidades_entrega WHERE activo = 1";
        try (Connection conn = MyConnection.getConnection()) {
            PreparedStatement st = conn.prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                modeloCombo.addElement(rs.getString("nombre"));
            }
            modalidadComboBox.setModel(modeloCombo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void cargarPuntosEntrega() {
        if (idsPuntos == null) { idsPuntos = new java.util.ArrayList<>(); }
        idsPuntos.clear();
        javax.swing.DefaultComboBoxModel<String> modeloPunto = new javax.swing.DefaultComboBoxModel<>();
        String SQL = "SELECT id_punto, nombre, direccion FROM puntos_entrega WHERE activo = 1";
        try {
            PreparedStatement st = MyConnection.getConnection().prepareStatement(SQL);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                idsPuntos.add(rs.getInt("id_punto"));
                modeloPunto.addElement(rs.getString("nombre") + " (" + rs.getString("direccion") + ")");
            }
            puntoComboBox.setModel(modeloPunto);
        // Si hay puntos cargados, cargamos los horarios del primer punto automáticamente
            if (!idsPuntos.isEmpty()) {
                cargarHorarios(idsPuntos.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    private void cargarHorarios(int idPunto) {
        if (idsHorarios == null) { idsHorarios = new java.util.ArrayList<>(); }
        idsHorarios.clear();
        javax.swing.DefaultComboBoxModel<String> modeloHorario = new javax.swing.DefaultComboBoxModel<>();
        String SQL = "SELECT id_horario, dia, hora_inicio, hora_fin, capacidad FROM horarios_entrega " +
                     "WHERE id_punto = ? AND activo = 1 AND capacidad > 0";
        try {
            PreparedStatement st = MyConnection.getConnection().prepareStatement(SQL);
            st.setInt(1, idPunto);
            ResultSet rs = st.executeQuery();   
            while (rs.next()) {
                idsHorarios.add(rs.getInt("id_horario"));
                String formatoHorario = "Día: " + rs.getDate("dia") + " | " + rs.getTime("hora_inicio") + " a " + rs.getTime("hora_fin") + " (Cupos: " + rs.getInt("capacidad") + ")";
                modeloHorario.addElement(formatoHorario);
            }
            horarioComboBox.setModel(modeloHorario);    
            } catch (Exception e) {
                e.printStackTrace();
        }  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        carritoTable = new javax.swing.JTable();
        confirmarButton = new javax.swing.JButton();
        eliminarButton = new javax.swing.JButton();
        totalTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        modalidadComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nombreRetiraTextField = new javax.swing.JTextField();
        ciRetiraTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        horarioComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        puntoComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Carrito de Compras");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        carritoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(carritoTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        confirmarButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        confirmarButton.setText("Confirmar pedido");
        confirmarButton.addActionListener(this::confirmarButtonActionPerformed);

        eliminarButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        eliminarButton.setText("Eliminar del carrito");
        eliminarButton.addActionListener(this::eliminarButtonActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Total:");

        modalidadComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        modalidadComboBox.addActionListener(this::modalidadComboBoxActionPerformed);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Modalidad:");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Encargado");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Nombre:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("CI:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nombreRetiraTextField)
                            .addComponent(ciRetiraTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(nombreRetiraTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ciRetiraTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Horario");

        horarioComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Ubicacion");

        puntoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        puntoComboBox.addActionListener(this::puntoComboBoxActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(horarioComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(puntoComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(horarioComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(puntoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(102, 102, 102)
                                        .addComponent(eliminarButton))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(modalidadComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(179, 179, 179)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(confirmarButton))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(modalidadComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(eliminarButton)
                            .addComponent(confirmarButton))
                        .addGap(38, 38, 38))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarButtonActionPerformed
        // TODO add your handling code here:
        int filaSel = carritoTable.getSelectedRow();
        if (filaSel == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del carrito.");
            return;
        }
        int idDetalle = (int) carritoTable.getValueAt(filaSel, 0);
        int cantidadActual = (int) carritoTable.getValueAt(filaSel, 3);
        try (Connection conn = MyConnection.getConnection()) {
            if (cantidadActual > 1) {
                String sqlUpdate = "UPDATE detalle_carrito SET cantidad = cantidad - 1 WHERE id_detalle_carrito = ?";
                try (PreparedStatement pst = conn.prepareStatement(sqlUpdate)) {
                    pst.setInt(1, idDetalle);
                    pst.executeUpdate();
                }
            } else {
                String sqlDelete = "DELETE FROM detalle_carrito WHERE id_detalle_carrito = ?";
                try (PreparedStatement pst = conn.prepareStatement(sqlDelete)) {
                    pst.setInt(1, idDetalle);
                    pst.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Producto actualizado del carrito.");
            actualizarCarrito();
            if (catalogoPadre != null) {
                catalogoPadre.actualizarTabla();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al modificar el carrito: " + e.getMessage());
        }
    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void confirmarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarButtonActionPerformed
        // TODO add your handling code here:
        if (idCarritoActual == -1 || carritoTable.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
        }
        // 1. Obtener y validar la modalidad de entrega seleccionada
        String modalidadElegida = modalidadComboBox.getSelectedItem() != null ? modalidadComboBox.getSelectedItem().toString() : "";
        int idModalidad = 1; // Por defecto RECOJO_EN_TIENDA
        if (modalidadElegida.equalsIgnoreCase("DEPOSITO")) {
            idModalidad = 2;
        } else if (modalidadElegida.equalsIgnoreCase("PUNTO_Y_HORARIO")) {
            idModalidad = 3;
        }
        String nombreRetira = nombreRetiraTextField.getText().trim();
        String ciRetira = ciRetiraTextField.getText().trim();
        Integer idHorarioElegido = null;
        if (modalidadElegida.equalsIgnoreCase("PUNTO_Y_HORARIO")) {
            int indexHorario = horarioComboBox.getSelectedIndex();
            if (indexHorario >= 0 && indexHorario < idsHorarios.size()) {
                idHorarioElegido = idsHorarios.get(indexHorario);
            } else {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un horario válido de entrega.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        // Validar datos de retiro si no es depósito
        if (!modalidadElegida.equalsIgnoreCase("DEPOSITO") && (nombreRetira.equals("") || ciRetira.equals(""))) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre y CI de la persona que retira.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String totalStr = totalTextField.getText().replace(",", ".");
        double totalPedido = Double.parseDouble(totalStr);
        int respuesta = JOptionPane.showConfirmDialog(this,
            "Modalidad: " + modalidadElegida + "\nEl total del pedido es: Bs. " + totalStr + "\n¿Desea confirmar el pedido?",
            "Confirmar Pedido",
            JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            Connection conn = null;
            try {
                conn = MyConnection.getConnection();
                conn.setAutoCommit(false); // Transacción segura
                // 2. Insertar en pedidos incluyendo modalidad, nombre y CI de retiro
                String sqlPedido = "INSERT INTO pedidos (id_cliente, estado, subtotal, costo_envio, total, id_modalidad_entrega, nombre_retira, ci_retira, id_horario) " +
                        "VALUES (?, 'PENDIENTE', ?, 0.00, ?, ?, ?, ?, ?)";
                int idPedidoGenerado = -1;
                try (PreparedStatement pst = conn.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, idClienteActual);
                    pst.setDouble(2, totalPedido);
                    pst.setDouble(3, totalPedido);
                    pst.setInt(4, idModalidad);            
                    if (modalidadElegida.equalsIgnoreCase("DEPOSITO")) {
                        pst.setNull(5, java.sql.Types.VARCHAR);
                        pst.setNull(6, java.sql.Types.VARCHAR);
                    } else {
                        pst.setString(5, nombreRetira);
                        pst.setString(6, ciRetira);
                    }            
                    if (idHorarioElegido != null) {
                        pst.setInt(7, idHorarioElegido);
                    } else {
                        pst.setNull(7, java.sql.Types.INTEGER);
                    }
                    pst.executeUpdate();
                    ResultSet rsKeys = pst.getGeneratedKeys();
                    if (rsKeys.next()) {
                        idPedidoGenerado = rsKeys.getInt(1);
                    }
                }
                // 3. Copiar los ítems del detalle_carrito hacia detalle_pedidos
                String sqlDetalleCarrito = "SELECT id_producto, cantidad, precio_unitario FROM detalle_carrito WHERE id_carrito = ?";
                String sqlInsertDetallePedido = "INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario, subtotal) " +
                                                "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstSel = conn.prepareStatement(sqlDetalleCarrito);
                    PreparedStatement pstIns = conn.prepareStatement(sqlInsertDetallePedido)) {         
                    pstSel.setInt(1, idCarritoActual);
                    ResultSet rsDetalle = pstSel.executeQuery();
                    while (rsDetalle.next()) {
                        int idProd = rsDetalle.getInt("id_producto");
                        int cant = rsDetalle.getInt("cantidad");
                        double precioU = rsDetalle.getDouble("precio_unitario");
                        double subTot = cant * precioU;
                        pstIns.setInt(1, idPedidoGenerado);
                        pstIns.setInt(2, idProd);
                        pstIns.setInt(3, cant);
                        pstIns.setDouble(4, precioU);
                        pstIns.setDouble(5, subTot);
                        pstIns.executeUpdate();
                    }
                }
                // 4. Cambiar el estado del carrito a 'CONVERTIDO'
                String sqlCerrarCarrito = "UPDATE carritos SET estado = 'CONVERTIDO' WHERE id_carrito = ?";
                try (PreparedStatement pst = conn.prepareStatement(sqlCerrarCarrito)) {
                    pst.setInt(1, idCarritoActual);
                    pst.executeUpdate();
                }
                conn.commit(); // Confirmar transacción completa
                JOptionPane.showMessageDialog(this, "¡Pedido confirmado con éxito! Nro de Pedido: " + idPedidoGenerado);    
                this.dispose();
                if (catalogoPadre != null) {
                    catalogoPadre.actualizarTabla();
                }
            } catch (Exception e) {
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al procesar el pedido: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
            }
        }
    }//GEN-LAST:event_confirmarButtonActionPerformed
    
    /*private void modalidadComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {                                                 
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String modalidadSeleccionada = modalidadComboBox.getSelectedItem().toString();      
        // Si es Recojo o Punto y Horario, se exigen los datos de la persona que retira
            if (modalidadSeleccionada.equals("RECOJO_EN_TIENDA") || modalidadSeleccionada.equals("PUNTO_Y_HORARIO")) {
                nombreRetiraTextField.setEnabled(true);
                ciRetiraTextField.setEnabled(true);
            } else {
            // Si es Depósito, se pueden limpiar o desactivar ya que no aplican
                nombreRetiraTextField.setText("");
                ciRetiraTextField.setText("");
                nombreRetiraTextField.setEnabled(false);
                ciRetiraTextField.setEnabled(false);
            }
        }
    }*/
    private void modalidadComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modalidadComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modalidadComboBoxActionPerformed

    private void puntoComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_puntoComboBoxActionPerformed
        // TODO add your handling code here:
        int index = puntoComboBox.getSelectedIndex();
        if (index >= 0 && index < idsPuntos.size()) {
            int idPuntoElegido = idsPuntos.get(index);
            cargarHorarios(idPuntoElegido);
        }
    }//GEN-LAST:event_puntoComboBoxActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void ventanaCarrito(String args[]) {
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CarritoGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable carritoTable;
    private javax.swing.JTextField ciRetiraTextField;
    private javax.swing.JButton confirmarButton;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JComboBox<String> horarioComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> modalidadComboBox;
    private javax.swing.JTextField nombreRetiraTextField;
    private javax.swing.JComboBox<String> puntoComboBox;
    private javax.swing.JTextField totalTextField;
    // End of variables declaration//GEN-END:variables
}
