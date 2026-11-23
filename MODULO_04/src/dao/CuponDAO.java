/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import modelo.Cupon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 *
 * @author Sthaicy08
 */
public class CuponDAO {
    public int insertar(Cupon cupon) {
        String sql = "INSERT INTO cupones (id_promocion, codigo, limite_global, limite_por_cliente, usos_actuales, monto_minimo, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setInt(1, cupon.getIdPromocion());
            sentencia.setString(2, cupon.getCodigo());
            sentencia.setInt(3, cupon.getLimiteGlobal());
            sentencia.setInt(4, cupon.getLimitePorCliente());
            sentencia.setInt(5, cupon.getUsosActuales());
            sentencia.setDouble(6, cupon.getMontoMinimo());
            sentencia.setTimestamp(7, Timestamp.valueOf(cupon.getFechaInicio()));
            sentencia.setTimestamp(8, Timestamp.valueOf(cupon.getFechaFin()));
            sentencia.setBoolean(9, cupon.isActivo());

            int filas = sentencia.executeUpdate();

            if (filas > 0) {
                ResultSet resultado = sentencia.getGeneratedKeys();

                if (resultado.next()) {
                    return resultado.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al registrar cupón: " + e.getMessage());
        }

        return -1;
    }

    public ArrayList<Cupon> listar() {
        ArrayList<Cupon> lista = new ArrayList<>();
        String sql = "SELECT * FROM cupones ORDER BY id_cupon DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                Cupon cupon = new Cupon();

                cupon.setIdCupon(resultado.getInt("id_cupon"));
                cupon.setIdPromocion(resultado.getInt("id_promocion"));
                cupon.setCodigo(resultado.getString("codigo"));
                cupon.setLimiteGlobal(resultado.getInt("limite_global"));
                cupon.setLimitePorCliente(resultado.getInt("limite_por_cliente"));
                cupon.setUsosActuales(resultado.getInt("usos_actuales"));
                cupon.setMontoMinimo(resultado.getDouble("monto_minimo"));
                cupon.setFechaInicio(resultado.getTimestamp("fecha_inicio").toString());
                cupon.setFechaFin(resultado.getTimestamp("fecha_fin").toString());
                cupon.setActivo(resultado.getBoolean("activo"));

                lista.add(cupon);
            }
        } catch (Exception e) {
            System.out.println("Error al listar cupones: " + e.getMessage());
        }

        return lista;
    }
    
    public Cupon buscarPorId(int idCupon) {
        String sql = "SELECT * FROM cupones WHERE id_cupon = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idCupon);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                return crearCupon(resultado);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar cupón: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(Cupon cupon) {
        String sql = "UPDATE cupones SET id_promocion = ?, codigo = ?, limite_global = ?, limite_por_cliente = ?, monto_minimo = ?, fecha_inicio = ?, fecha_fin = ?, activo = ? WHERE id_cupon = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, cupon.getIdPromocion());
            sentencia.setString(2, cupon.getCodigo());
            sentencia.setInt(3, cupon.getLimiteGlobal());
            sentencia.setInt(4, cupon.getLimitePorCliente());
            sentencia.setDouble(5, cupon.getMontoMinimo());
            sentencia.setTimestamp(6, Timestamp.valueOf(cupon.getFechaInicio()));
            sentencia.setTimestamp(7, Timestamp.valueOf(cupon.getFechaFin()));
            sentencia.setBoolean(8, cupon.isActivo());
            sentencia.setInt(9, cupon.getIdCupon());

            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al modificar cupón: " + e.getMessage());
            return false;
        }
    }

    public boolean desactivar(int idCupon) {
        String sql = "UPDATE cupones SET activo = FALSE WHERE id_cupon = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idCupon);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al desactivar cupón: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCodigo(String codigo, int idCupon) {
        String sql = "SELECT id_cupon FROM cupones WHERE codigo = ? AND id_cupon <> ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);
            sentencia.setInt(2, idCupon);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next();
        } catch (Exception e) {
            return false;
        }
    }

    private Cupon crearCupon(ResultSet resultado) throws Exception {
        Cupon cupon = new Cupon();
        cupon.setIdCupon(resultado.getInt("id_cupon"));
        cupon.setIdPromocion(resultado.getInt("id_promocion"));
        cupon.setCodigo(resultado.getString("codigo"));
        cupon.setLimiteGlobal(resultado.getInt("limite_global"));
        cupon.setLimitePorCliente(resultado.getInt("limite_por_cliente"));
        cupon.setUsosActuales(resultado.getInt("usos_actuales"));
        cupon.setMontoMinimo(resultado.getDouble("monto_minimo"));
        cupon.setFechaInicio(resultado.getTimestamp("fecha_inicio").toString());
        cupon.setFechaFin(resultado.getTimestamp("fecha_fin").toString());
        cupon.setActivo(resultado.getBoolean("activo"));
        return cupon;
    }
}
