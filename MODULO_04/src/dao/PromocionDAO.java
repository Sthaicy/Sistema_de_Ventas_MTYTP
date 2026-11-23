/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.Conexion;
import modelo.Promocion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
/**
 *
 * @author Sthaicy08
 */
public class PromocionDAO {

    public int insertar(Promocion promocion) {
        String sql = "INSERT INTO promociones (nombre, descripcion, tipo, valor, monto_minimo, fecha_inicio, fecha_fin, id_live, prioridad, activo, modo_aplicacion, alcance, precio_combo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, promocion.getNombre());
            sentencia.setString(2, promocion.getDescripcion());
            sentencia.setString(3, promocion.getTipo());
            sentencia.setDouble(4, promocion.getValor());
            sentencia.setDouble(5, promocion.getMontoMinimo());
            sentencia.setTimestamp(6, Timestamp.valueOf(promocion.getFechaInicio()));
            sentencia.setTimestamp(7, Timestamp.valueOf(promocion.getFechaFin()));

            if (promocion.getIdLive() == null) {
                sentencia.setNull(8, Types.INTEGER);
            } else {
                sentencia.setInt(8, promocion.getIdLive());
            }

            sentencia.setInt(9, promocion.getPrioridad());
            sentencia.setBoolean(10, promocion.isActivo());
            sentencia.setString(11, promocion.getModoAplicacion());
            sentencia.setString(12, promocion.getAlcance());
            sentencia.setDouble(13, promocion.getPrecioCombo());

            sentencia.executeUpdate();

            ResultSet resultado = sentencia.getGeneratedKeys();
            if (resultado.next()) {
                return resultado.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error al insertar promoción: " + e.getMessage());
        }

        return -1;
    }

    public ArrayList<Promocion> listar() {
        ArrayList<Promocion> lista = new ArrayList<>();

        String sql = "SELECT p.*, p.alcance AS aplicar_a, "
                + "CASE "
                + "WHEN p.alcance = 'PRODUCTO' THEN "
                + "COALESCE((SELECT GROUP_CONCAT(pr.nombre SEPARATOR ', ') "
                + "FROM promocion_productos pp JOIN productos pr "
                + "ON pp.id_producto = pr.id_producto "
                + "WHERE pp.id_promocion = p.id_promocion), 'Sin asignar') "
                + "WHEN p.alcance = 'CATEGORIA' THEN "
                + "COALESCE((SELECT GROUP_CONCAT(c.nombre SEPARATOR ', ') "
                + "FROM promocion_categorias pc JOIN categorias c "
                + "ON pc.id_categoria = c.id_categoria "
                + "WHERE pc.id_promocion = p.id_promocion), 'Sin asignar') "
                + "ELSE 'Toda la compra' END AS aplicado_sobre "
                + "FROM promociones p ORDER BY p.id_promocion DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                Promocion promocion = crearPromocion(resultado);
                promocion.setAplicarA(resultado.getString("aplicar_a"));
                promocion.setAplicadoSobre(resultado.getString("aplicado_sobre"));
                lista.add(promocion);
            }

        } catch (Exception e) {
            System.out.println("Error al listar promociones: " + e.getMessage());
        }

        return lista;
    }

    public Promocion buscarPorId(int idPromocion) {
        String sql = "SELECT * FROM promociones WHERE id_promocion = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                return crearPromocion(resultado);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar promoción: " + e.getMessage());
        }

        return null;
    }

    public boolean actualizar(Promocion promocion) {
        String sql = "UPDATE promociones SET nombre = ?, descripcion = ?, tipo = ?, valor = ?, monto_minimo = ?, fecha_inicio = ?, fecha_fin = ?, id_live = ?, prioridad = ?, activo = ?, modo_aplicacion = ?, alcance = ?, precio_combo = ? WHERE id_promocion = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, promocion.getNombre());
            sentencia.setString(2, promocion.getDescripcion());
            sentencia.setString(3, promocion.getTipo());
            sentencia.setDouble(4, promocion.getValor());
            sentencia.setDouble(5, promocion.getMontoMinimo());
            sentencia.setTimestamp(6, Timestamp.valueOf(promocion.getFechaInicio()));
            sentencia.setTimestamp(7, Timestamp.valueOf(promocion.getFechaFin()));

            if (promocion.getIdLive() == null) {
                sentencia.setNull(8, Types.INTEGER);
            } else {
                sentencia.setInt(8, promocion.getIdLive());
            }

            sentencia.setInt(9, promocion.getPrioridad());
            sentencia.setBoolean(10, promocion.isActivo());
            sentencia.setString(11, promocion.getModoAplicacion());
            sentencia.setString(12, promocion.getAlcance());
            sentencia.setDouble(13, promocion.getPrecioCombo());
            sentencia.setInt(14, promocion.getIdPromocion());

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar promoción: " + e.getMessage());
            return false;
        }
    }

    public boolean desactivar(int idPromocion) {
        String sql = "UPDATE promociones SET activo = FALSE WHERE id_promocion = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al desactivar promoción: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> listarProductosActivos() {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre FROM productos WHERE activo = TRUE ORDER BY nombre";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                lista.add(resultado.getInt("id_producto") + " - " + resultado.getString("nombre"));
            }

        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<String> listarCategoriasActivas() {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre FROM categorias WHERE activo = TRUE ORDER BY nombre";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                lista.add(resultado.getInt("id_categoria") + " - " + resultado.getString("nombre"));
            }

        } catch (Exception e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return lista;
    }

    public boolean asociarProducto(int idPromocion, int idProducto) {
        return asociarProducto(idPromocion, idProducto, 1);
    }

    public boolean asociarProducto(int idPromocion, int idProducto, int cantidad) {
        String sql = "INSERT INTO promocion_productos (id_promocion, id_producto, cantidad_requerida) VALUES (?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            sentencia.setInt(2, idProducto);
            sentencia.setInt(3, cantidad);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al asociar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean asociarCategoria(int idPromocion, int idCategoria) {
        String sql = "INSERT INTO promocion_categorias (id_promocion, id_categoria) VALUES (?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            sentencia.setInt(2, idCategoria);

            return sentencia.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al asociar categoría: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Object[]> obtenerProductosPromocion(int idPromocion) {
        ArrayList<Object[]> lista = new ArrayList<>();

        String sql = "SELECT pr.id_producto, pr.nombre, pp.cantidad_requerida "
                + "FROM promocion_productos pp "
                + "JOIN productos pr ON pp.id_producto = pr.id_producto "
                + "WHERE pp.id_promocion = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Object[] fila = {
                    resultado.getInt("id_producto"),
                    resultado.getString("nombre"),
                    resultado.getInt("cantidad_requerida")
                };
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener productos de promoción: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<Object[]> obtenerCategoriasPromocion(int idPromocion) {
        ArrayList<Object[]> lista = new ArrayList<>();

        String sql = "SELECT c.id_categoria, c.nombre "
                + "FROM promocion_categorias pc "
                + "JOIN categorias c ON pc.id_categoria = c.id_categoria "
                + "WHERE pc.id_promocion = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Object[] fila = {
                    resultado.getInt("id_categoria"),
                    resultado.getString("nombre"),
                    1
                };
                lista.add(fila);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener categorías de promoción: " + e.getMessage());
        }

        return lista;
    }

    public void eliminarRelaciones(int idPromocion) {
        String sqlProductos = "DELETE FROM promocion_productos WHERE id_promocion = ?";
        String sqlCategorias = "DELETE FROM promocion_categorias WHERE id_promocion = ?";

        try (Connection conexion = Conexion.conectar()) {

            try (PreparedStatement sentencia = conexion.prepareStatement(sqlProductos)) {
                sentencia.setInt(1, idPromocion);
                sentencia.executeUpdate();
            }

            try (PreparedStatement sentencia = conexion.prepareStatement(sqlCategorias)) {
                sentencia.setInt(1, idPromocion);
                sentencia.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar relaciones: " + e.getMessage());
        }
    }

    public Integer obtenerProducto(int idPromocion) {
        String sql = "SELECT id_producto FROM promocion_productos WHERE id_promocion = ? LIMIT 1";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                return resultado.getInt("id_producto");
            }

        } catch (Exception e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }

        return null;
    }

    public Integer obtenerCategoria(int idPromocion) {
        String sql = "SELECT id_categoria FROM promocion_categorias WHERE id_promocion = ? LIMIT 1";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idPromocion);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                return resultado.getInt("id_categoria");
            }

        } catch (Exception e) {
            System.out.println("Error al buscar categoría: " + e.getMessage());
        }

        return null;
    }

    public boolean existeProducto(int idProducto) {
        String sql = "SELECT id_producto FROM productos WHERE id_producto = ? AND activo = TRUE";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idProducto);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next();

        } catch (Exception e) {
            System.out.println("Error al verificar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCategoria(int idCategoria) {
        String sql = "SELECT id_categoria FROM categorias WHERE id_categoria = ? AND activo = TRUE";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, idCategoria);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next();

        } catch (Exception e) {
            System.out.println("Error al verificar categoría: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> listarLives() {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT id_live, fecha, estado FROM lives ORDER BY fecha DESC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                lista.add(resultado.getInt("id_live") + " - "
                        + resultado.getString("fecha") + " - "
                        + resultado.getString("estado"));
            }
        } catch (Exception e) {
            System.out.println("Error al consultar Lives: " + e.getMessage());
        }
        return lista;
    }

    public boolean existeLive(int idLive) {
        String sql = "SELECT id_live FROM lives WHERE id_live = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idLive);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next();
        } catch (Exception e) {
            System.out.println("Error al verificar Live: " + e.getMessage());
            return false;
        }
    }

    private Promocion crearPromocion(ResultSet resultado) throws Exception {
        Promocion promocion = new Promocion();

        promocion.setIdPromocion(resultado.getInt("id_promocion"));
        promocion.setNombre(resultado.getString("nombre"));
        promocion.setDescripcion(resultado.getString("descripcion"));
        promocion.setTipo(resultado.getString("tipo"));
        promocion.setValor(resultado.getDouble("valor"));
        promocion.setMontoMinimo(resultado.getDouble("monto_minimo"));
        promocion.setFechaInicio(resultado.getString("fecha_inicio"));
        promocion.setFechaFin(resultado.getString("fecha_fin"));
        promocion.setIdLive((Integer) resultado.getObject("id_live"));
        promocion.setPrioridad(resultado.getInt("prioridad"));
        promocion.setActivo(resultado.getBoolean("activo"));
        promocion.setModoAplicacion(resultado.getString("modo_aplicacion"));
        promocion.setAlcance(resultado.getString("alcance"));
        promocion.setPrecioCombo(resultado.getDouble("precio_combo"));
        promocion.setAplicarA(resultado.getString("alcance"));

        return promocion;
    }
}
