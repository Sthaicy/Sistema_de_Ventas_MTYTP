package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class IntegracionDAO {

    public ArrayList<Object[]> listarPedidosTienda() {
        ArrayList<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_pedido, fecha, id_cliente, total, estado "
                   + "FROM pedidos ORDER BY fecha DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                Object[] pedido = {
                    resultado.getInt("id_pedido"),
                    resultado.getTimestamp("fecha"),
                    resultado.getInt("id_cliente"),
                    resultado.getDouble("total"),
                    resultado.getString("estado")
                };
                lista.add(pedido);
            }
        } catch (Exception e) {
            System.out.println("Error al consultar pedidos: " + e.getMessage());
        }
        return lista;
    }
}
