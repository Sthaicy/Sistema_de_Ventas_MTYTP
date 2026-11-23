/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Sthaicy08
 */
public class Promocion {
    private int idPromocion;
    private String nombre;
    private String descripcion;
    private String tipo;
    private double valor;
    private double montoMinimo;
    private String fechaInicio;
    private String fechaFin;
    private Integer idLive;
    private int prioridad;
    private boolean activo;
    private String aplicarA;
    private String aplicadoSobre;
    private String modoAplicacion;
    private String alcance;
    private double precioCombo;

    public Promocion() {
    }

    public Promocion(int idPromocion, String nombre, String descripcion, String tipo, double valor, double montoMinimo, String fechaInicio, String fechaFin, Integer idLive, int prioridad, boolean activo) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.valor = valor;
        this.montoMinimo = montoMinimo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idLive = idLive;
        this.prioridad = prioridad;
        this.activo = activo;
        this.modoAplicacion = "AUTOMATICA";
        this.alcance = "PRODUCTO";
    }

    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public double getMontoMinimo() { return montoMinimo; }
    public void setMontoMinimo(double montoMinimo) { this.montoMinimo = montoMinimo; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public Integer getIdLive() { return idLive; }
    public void setIdLive(Integer idLive) { this.idLive = idLive; }
    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getAplicarA() { return aplicarA; }
    public void setAplicarA(String aplicarA) { this.aplicarA = aplicarA; }
    public String getAplicadoSobre() { return aplicadoSobre; }
    public void setAplicadoSobre(String aplicadoSobre) { this.aplicadoSobre = aplicadoSobre; }

    public String getModoAplicacion() { return modoAplicacion; }
    public void setModoAplicacion(String modoAplicacion) { this.modoAplicacion = modoAplicacion; }
    public String getAlcance() { return alcance; }
    public void setAlcance(String alcance) { this.alcance = alcance; }
    public double getPrecioCombo() { return precioCombo; }
    public void setPrecioCombo(double precioCombo) { this.precioCombo = precioCombo; }
}
