/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Sthaicy08
 */
public class PromocionCategoria {
    private int idPromocionCategoria;
    private int idPromocion;
    private int idCategoria;

    public PromocionCategoria() {
    }

    public PromocionCategoria(int idPromocionCategoria, int idPromocion, int idCategoria) {
        this.idPromocionCategoria = idPromocionCategoria;
        this.idPromocion = idPromocion;
        this.idCategoria = idCategoria;
    }

    public int getIdPromocionCategoria() { return idPromocionCategoria; }
    public void setIdPromocionCategoria(int idPromocionCategoria) { this.idPromocionCategoria = idPromocionCategoria; }
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
}
