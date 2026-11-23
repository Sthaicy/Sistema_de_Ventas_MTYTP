/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import modelo.ReglaPromocion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Sthaicy08
 */
public class ReglaPromocionDAO {

    public boolean insertar(ReglaPromocion regla) {
        String sql = "INSERT INTO reglas_promocion (id_promocion, tipo_regla, id_promocion_relacionada, descripcion) VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, regla.getIdPromocion());
            sentencia.setString(2, regla.getTipoRegla());
            sentencia.setInt(3, regla.getIdPromocionRelacionada());
            sentencia.setString(4, regla.getDescripcion());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al registrar regla: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<ReglaPromocion> listar() {
        ArrayList<ReglaPromocion> lista = new ArrayList<>();
        String sql = "SELECT * FROM reglas_promocion ORDER BY id_regla DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                ReglaPromocion regla = crearRegla(resultado);
                lista.add(regla);
            }
        } catch (Exception e) {
            System.out.println("Error al listar reglas: " + e.getMessage());
        }
        return lista;
    }

    public ReglaPromocion buscarPorId(int idRegla) {
        String sql = "SELECT * FROM reglas_promocion WHERE id_regla = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRegla);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                return crearRegla(resultado);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar regla: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(ReglaPromocion regla) {
        String sql = "UPDATE reglas_promocion SET id_promocion = ?, tipo_regla = ?, id_promocion_relacionada = ?, descripcion = ? WHERE id_regla = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, regla.getIdPromocion());
            sentencia.setString(2, regla.getTipoRegla());
            sentencia.setInt(3, regla.getIdPromocionRelacionada());
            sentencia.setString(4, regla.getDescripcion());
            sentencia.setInt(5, regla.getIdRegla());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al modificar regla: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idRegla) {
        String sql = "DELETE FROM reglas_promocion WHERE id_regla = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idRegla);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al eliminar regla: " + e.getMessage());
            return false;
        }
    }

    public boolean existePromocion(int idPromocion) {
        String sql = "SELECT id_promocion FROM promociones WHERE id_promocion = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idPromocion);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next();
        } catch (Exception e) {
            return false;
        }
    }

    private ReglaPromocion crearRegla(ResultSet resultado) throws Exception {
        ReglaPromocion regla = new ReglaPromocion();
        regla.setIdRegla(resultado.getInt("id_regla"));
        regla.setIdPromocion(resultado.getInt("id_promocion"));
        regla.setTipoRegla(resultado.getString("tipo_regla"));
        regla.setIdPromocionRelacionada(resultado.getInt("id_promocion_relacionada"));
        regla.setDescripcion(resultado.getString("descripcion"));
        return regla;
    }
}
