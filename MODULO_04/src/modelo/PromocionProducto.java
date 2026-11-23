/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Sthaicy08
 */
public class PromocionProducto {
    private int idPromocionProducto;
    private int idPromocion;
    private int idProducto;

    public PromocionProducto() {
    }

    public PromocionProducto(int idPromocionProducto, int idPromocion, int idProducto) {
        this.idPromocionProducto = idPromocionProducto;
        this.idPromocion = idPromocion;
        this.idProducto = idProducto;
    }

    public int getIdPromocionProducto() { return idPromocionProducto; }
    public void setIdPromocionProducto(int idPromocionProducto) { this.idPromocionProducto = idPromocionProducto; }
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
}