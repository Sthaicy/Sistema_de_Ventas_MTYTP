/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Sthaicy08
 */
    public class Cupon {
    private int idCupon;
    private int idPromocion;
    private String codigo;
    private int limiteGlobal;
    private int limitePorCliente;
    private int usosActuales;
    private double montoMinimo;
    private String fechaInicio;
    private String fechaFin;
    private boolean activo;
    
    public Cupon() {
    }
    
    public int getIdCupon() { return idCupon; }
    public void setIdCupon(int idCupon) { this.idCupon = idCupon; }
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public int getLimiteGlobal() { return limiteGlobal; }
    public void setLimiteGlobal(int limiteGlobal) { this.limiteGlobal = limiteGlobal; }
    public int getLimitePorCliente() { return limitePorCliente; }
    public void setLimitePorCliente(int limitePorCliente) { this.limitePorCliente = limitePorCliente; }
    public int getUsosActuales() { return usosActuales; }
    public void setUsosActuales(int usosActuales) { this.usosActuales = usosActuales; }
    public double getMontoMinimo() { return montoMinimo; }
    public void setMontoMinimo(double montoMinimo) { this.montoMinimo = montoMinimo; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
}
