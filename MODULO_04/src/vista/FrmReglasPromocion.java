/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.ReglaPromocionDAO;
import modelo.ReglaPromocion;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sthaicy08
 */
public class FrmReglasPromocion extends javax.swing.JFrame {
    
    private ReglaPromocionDAO reglaDAO = new ReglaPromocionDAO();
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmReglasPromocion.class.getName());

    /**
     * Creates new form FrmReglasPromocion
     */
    public FrmReglasPromocion() {
        initComponents();
        
        configurarFormulario();
        configurarTabla();
        cargarReglas();
        configurarEventos();
    }
    
    private void configurarFormulario() {
        txtIdRegla.setEditable(false);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Promoción");
        modelo.addColumn("Tipo");
        modelo.addColumn("Promoción relacionada");
        modelo.addColumn("Descripción");
        tblReglas.setModel(modelo);
    }
    
    private void cargarReglas() {
        DefaultTableModel modelo = (DefaultTableModel) tblReglas.getModel();
        modelo.setRowCount(0);

        ArrayList<ReglaPromocion> lista = reglaDAO.listar();

        for (ReglaPromocion regla : lista) {
            Object[] fila = {
                regla.getIdRegla(),
                regla.getIdPromocion(),
                regla.getTipoRegla(),
                regla.getIdPromocionRelacionada(),
                regla.getDescripcion()
            };
            modelo.addRow(fila);
        }
    }
    
    private void configurarEventos() {
        tblReglas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarReglaSeleccionada();
            }
        });
    }
    
    private void cargarReglaSeleccionada() {
        int fila = tblReglas.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int idRegla = Integer.parseInt(tblReglas.getValueAt(fila, 0).toString());
        ReglaPromocion regla = reglaDAO.buscarPorId(idRegla);

        if (regla == null) {
            return;
        }

        txtIdRegla.setText(String.valueOf(regla.getIdRegla()));
        txtIdPromocion.setText(String.valueOf(regla.getIdPromocion()));
        cmbTipoRegla.setSelectedItem(regla.getTipoRegla());
        txtIdPromocionRelacionada.setText(String.valueOf(regla.getIdPromocionRelacionada()));
        txtDescripcion.setText(regla.getDescripcion());

        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }
    
    
    private ReglaPromocion obtenerReglaFormulario() {
        ReglaPromocion regla = new ReglaPromocion();

        if (!txtIdRegla.getText().trim().isEmpty()) {
            regla.setIdRegla(Integer.parseInt(txtIdRegla.getText().trim()));
        }

        regla.setIdPromocion(Integer.parseInt(txtIdPromocion.getText().trim()));
        regla.setTipoRegla(cmbTipoRegla.getSelectedItem().toString());
        regla.setIdPromocionRelacionada(Integer.parseInt(txtIdPromocionRelacionada.getText().trim()));
        regla.setDescripcion(txtDescripcion.getText().trim());

        return regla;
    }
    
    private boolean validarFormulario() {
        if (txtIdPromocion.getText().trim().isEmpty() || txtIdPromocionRelacionada.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese las promociones.");
            return false;
        }

        try {
            int idPromocion = Integer.parseInt(txtIdPromocion.getText().trim());
            int idRelacionada = Integer.parseInt(txtIdPromocionRelacionada.getText().trim());

            if (idPromocion == idRelacionada) {
                JOptionPane.showMessageDialog(this, "Una promoción no puede relacionarse consigo misma.");
                return false;
            }

            if (!reglaDAO.existePromocion(idPromocion)) {
                JOptionPane.showMessageDialog(this, "La promoción principal no existe.");
                return false;
            }

            if (!reglaDAO.existePromocion(idRelacionada)) {
                JOptionPane.showMessageDialog(this, "La promoción relacionada no existe.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los ID deben ser números enteros.");
            return false;
        }

        return true;
    }
    
    private void limpiarFormulario() {
        txtIdRegla.setText("");
        txtIdPromocion.setText("");
        txtIdPromocionRelacionada.setText("");
        txtDescripcion.setText("");
        cmbTipoRegla.setSelectedIndex(0);
        tblReglas.clearSelection();
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtIdPromocion.requestFocus();
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
        jLabel2 = new javax.swing.JLabel();
        txtIdPromocion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIdPromocionRelacionada = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        cmbTipoRegla = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtIdRegla = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReglas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("REGLAS DE PROMOCIONES");

        jLabel2.setText("ID Promoción");

        jLabel3.setText("Tipo de Regla");

        jLabel4.setText("Promocion relacionada");

        jLabel5.setText("Descripción");

        txtDescripcion.addActionListener(this::txtDescripcionActionPerformed);

        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        cmbTipoRegla.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PRIORIDAD", "EXCLUSION" }));

        jLabel6.setText("ID Regla");

        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        tblReglas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblReglas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtIdPromocionRelacionada, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbTipoRegla, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtIdPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtIdRegla, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(63, 63, 63))))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtIdRegla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIdPromocion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbTipoRegla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtIdPromocionRelacionada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (!validarFormulario()) {
            return;
        }
        try {
            ReglaPromocion regla = obtenerReglaFormulario();

            if (reglaDAO.insertar(regla)) {
                JOptionPane.showMessageDialog(this, "Regla registrada correctamente.");
                cargarReglas();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la regla.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la regla.");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        if (txtIdRegla.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una regla.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            ReglaPromocion regla = obtenerReglaFormulario();

            if (reglaDAO.actualizar(regla)) {
                JOptionPane.showMessageDialog(this, "Regla modificada correctamente.");
                cargarReglas();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar la regla.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar la regla.");
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        if (txtIdRegla.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una regla.");
            return;
        }

        int idRegla = Integer.parseInt(txtIdRegla.getText().trim());

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Desea eliminar esta regla?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (reglaDAO.eliminar(idRegla)) {
            JOptionPane.showMessageDialog(this, "Regla eliminada correctamente.");
            cargarReglas();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la regla.");
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmReglasPromocion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbTipoRegla;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblReglas;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtIdPromocion;
    private javax.swing.JTextField txtIdPromocionRelacionada;
    private javax.swing.JTextField txtIdRegla;
    // End of variables declaration//GEN-END:variables
}
