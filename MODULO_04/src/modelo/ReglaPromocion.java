/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Sthaicy08
 */
public class ReglaPromocion {
    private int idRegla;
    private int idPromocion;
    private String tipoRegla;
    private int idPromocionRelacionada;
    private String descripcion;

    public ReglaPromocion() {
    }

    public int getIdRegla() {
        return idRegla;
    }

    public void setIdRegla(int idRegla) {
        this.idRegla = idRegla;
    }

    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getTipoRegla() {
        return tipoRegla;
    }

    public void setTipoRegla(String tipoRegla) {
        this.tipoRegla = tipoRegla;
    }

    public int getIdPromocionRelacionada() {
        return idPromocionRelacionada;
    }

    public void setIdPromocionRelacionada(int idPromocionRelacionada) {
        this.idPromocionRelacionada = idPromocionRelacionada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
