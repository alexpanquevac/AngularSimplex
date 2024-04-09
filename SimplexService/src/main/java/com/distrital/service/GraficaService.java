/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distrital.service;

import com.distrital.modelo.Ecuacion;
import com.distrital.modelo.Linea;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

/**
 *
 * @author ALEXANDER
 */
@Service("graficasService")
public class GraficaService {

    public List<Ecuacion> graficar(List<Ecuacion> restricciones) {
        List<Ecuacion> ecuacion = new ArrayList<>();
        try {
            ecuacion = obtenerRectas(restricciones);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ecuacion;
    }

    private List<Ecuacion> obtenerRectas(List<Ecuacion> ecuaciones) throws IOException {
        List<Linea> rectas = new ArrayList<>();
        List<Ecuacion> ecuacionesResultado = new ArrayList<>();
        Ecuacion ecuacionObjetivo = new Ecuacion();
        for (Ecuacion ecuacion : ecuaciones) {
            Ecuacion ecuacionRes = new Ecuacion();
            Linea recta = new Linea();
            if (!"Objetivo".equals(ecuacion.getTipoEcuacion())) {
                recta.setCoordenadaY1(0.0);
                recta.setCoordenadaX2(0.0);
                if (ecuacion.getVariableX1() != 0) {
                    recta.setCoordenadaX1(ecuacion.getResultado() / ecuacion.getVariableX1());
                    ecuacionRes.setVariableX1(recta.getCoordenadaX1());
                } else {
                    recta.setCoordenadaX1(ecuacion.getResultado() + 100);
                    recta.setCoordenadaY1(ecuacion.getResultado());
                }
                if (ecuacion.getVariableX2() != 0) {
                    recta.setCoordenadaY2(ecuacion.getResultado() / ecuacion.getVariableX2());
                    ecuacionRes.setVariableX2(recta.getCoordenadaY2());
                } else {
                    recta.setCoordenadaY2(ecuacion.getResultado() + 100);
                    recta.setCoordenadaX2(ecuacion.getResultado());
                }
                ecuacionesResultado.add(ecuacionRes);
                rectas.add(recta);
            } else {
                ecuacionObjetivo.setVariableX1(ecuacion.getVariableX1());
                ecuacionObjetivo.setVariableX2(ecuacion.getVariableX2());
                recta.setCoordenadaY1(0.0);
                recta.setCoordenadaX2(0.0);
                if (ecuacion.getVariableX1() != 0) {
                    recta.setCoordenadaX1(10000 / (ecuacion.getVariableX1()));
                } else {
                    recta.setCoordenadaX1(500.0);
                    recta.setCoordenadaY1(500.0);
                }
                if (ecuacion.getVariableX2() != 0) {
                    recta.setCoordenadaY2(10000 / (ecuacion.getVariableX2()));
                } else {
                    recta.setCoordenadaY2(500.0);
                    recta.setCoordenadaX2(500.0);
                }
                recta.setTipo("Objetivo");
                rectas.add(recta);

            }
        }

        List<XYSeries> listaObjectosSeries = new ArrayList<>();
        XYSeries oSeries;

        XYSeriesCollection oDataset = new XYSeriesCollection();
        int contador = 0;
        for (int i = 0; i < rectas.size(); i++) {
            if (rectas.get(i).getTipo() != "Objetivo") {
                contador++;
                oSeries = new XYSeries("Restricción " + contador);
                oSeries.add(rectas.get(i).getCoordenadaX1(), rectas.get(i).getCoordenadaY1());
                oSeries.add(rectas.get(i).getCoordenadaX2(), rectas.get(i).getCoordenadaY2());
                oDataset.addSeries(oSeries);
                listaObjectosSeries.add(oSeries);
            } else {
                oSeries = new XYSeries("Función Objetivo");
                oSeries.add(rectas.get(i).getCoordenadaX1(), rectas.get(i).getCoordenadaY1());
                oSeries.add(rectas.get(i).getCoordenadaX2(), rectas.get(i).getCoordenadaY2());
                oDataset.addSeries(oSeries);
                listaObjectosSeries.add(oSeries);
            }
        }

        JFreeChart oChart = ChartFactory.createXYLineChart("MÉTODO GRÁFICO", "EJE X", "EJE Y ", oDataset, PlotOrientation.VERTICAL, true, false, false);
        ChartPanel oPanel = new ChartPanel(oChart);

        String ruta = "D:\\Desarrollo\\SimplexAngular\\SimplexService\\src\\webapp\\images\\image.png";
        ChartUtilities.saveChartAsPNG(new File(ruta), oChart, 500, 500);

        Ecuacion ecuacion = simplex(ecuaciones, ecuaciones.size(), 2);
        
        for(Ecuacion ecuacionIt : ecuacionesResultado){
            ecuacionIt.setResultado((ecuacionIt.getVariableX1()*ecuacionObjetivo.getVariableX1())+(ecuacionIt.getVariableX2()*ecuacionObjetivo.getVariableX2()));
        }
        ecuacion.setTipoEcuacion("Objetivo");
        ecuacionesResultado.add(ecuacion);

        return ecuacionesResultado;
    }

    public Ecuacion simplex(List<Ecuacion> ecuaciones, int filas, int columnas) {

        for (Ecuacion ecuacion : ecuaciones) {
            if ("Objetivo".equals(ecuacion.getTipoEcuacion())) {
                ecuacion.setVariableX1(ecuacion.getVariableX1() * -1);
                ecuacion.setVariableX2(ecuacion.getVariableX2() * -1);
            }
        }

        Collections.reverse(ecuaciones);

        String resultados = "";
        int columnaTotal = filas + columnas;
        int columna1 = columnas;
        DecimalFormat decimales = new DecimalFormat("0.000000");
        double[][] matriz = new double[filas][columnaTotal];

        for (int j = columna1; j < columnaTotal; j++) {
            matriz[0][j] = 0.0;
        }
        int i1 = 1;
        int j1 = columna1;
        for (int i = 1; i < filas; i++) {
            for (int j = columna1; j < columnaTotal - 1; j++) {
                if (i1 == i && j1 == j) {
                    matriz[i][j] = 1.0;
                } else {
                    matriz[i][j] = 0.0;
                }
            }
            j1++;
            i1++;
        }

        for (int i = 0; i < ecuaciones.size(); i++) {
            matriz[i][0] = ecuaciones.get(i).getVariableX1();
            matriz[i][1] = ecuaciones.get(i).getVariableX2();
            matriz[i][columnaTotal - 1] = ecuaciones.get(i).getResultado();
        }
        String v2[] = new String[filas];
        int v3[] = new int[filas];
        int v4[] = new int[filas];

        int co = 0;
        int co2 = 0;
        while (true) {
            if (co == columna1) {
                boolean vax1 = false, vx2 = false;
                for (int i = 0; i < v2.length; i++) {
                    if ("x2".equals(v2[i])) {
                        vx2 = true;
                    }
                    if ("x1".equals(v2[i])) {
                        vax1 = true;
                    }
                }
                if (vax1 && vx2) {
                    break;
                } else {
                    co--;
                }
            }

            int f = 0;
            int c = 0;
            double negativo = 0;
            //Encontramos la columna de mayor negatividad
            for (int i = 0; i < columna1; i++) {
                if (matriz[0][i] < negativo) {
                    negativo = matriz[0][i];
                    c = i;
                }
            }
            double menor = 0;
            double v1[] = new double[filas - 1];
            //Encontramos la fila de menor positividad
            int h = 0;
            for (int i = 1; i < filas; i++) {
                if (matriz[i][c] != 0.0) {
                    v1[h] = matriz[i][columnaTotal - 1] / matriz[i][c];
                } else {
                    v1[h] = 0.0;
                }
                h++;
            }

            //Se halan las razones
            menor = v1[0];
            for (int i = 0; i < filas - 1; i++) {

                if (v1[i] <= menor && v1[i] > 0.0) {
                    menor = v1[i];
                    f = i + 1;
                }
            }

            //Se divide la razones entre el pivote
            double pivo = matriz[f][c];
            for (int i = 0; i < columnaTotal; i++) {
                if (pivo != 0.0) {
                    matriz[f][i] = matriz[f][i] / pivo;
                } else {
                    matriz[f][i] = 0.0;
                }
            }

            //Se generan los resultados 
            for (int i = 1; i < filas; i++) {
                if (i == f) {
                    v2[i] = "x" + (c + 1);
                    v3[i] = c + 1;
                    v4[i] = i;
                }
                if (v3[i] <= 0) {

                    v2[i] = "d" + (i);
                }
            }

            //convertir los ceros de la fila
            for (int i = 0; i < filas; i++) {
                if (i != f) {
                    double guar = 0;
                    guar = -matriz[i][c];
                    for (int j = 0; j < columnaTotal; j++) {
                        matriz[i][j] = (guar * matriz[f][j]) + matriz[i][j];
                    }
                }
            }

            co = 0;
            for (int i = 0; i < columna1; i++) {
                if (matriz[0][i] >= 0) {
                    co++;
                }
            }

        }

        //Mostrando el arreglo
        for (int x = 0; x < matriz.length; x++) {
            System.out.print("|");
            for (int y = 0; y < matriz[x].length; y++) {
                System.out.println("\t " + matriz[x][y]);

                if (y != matriz[x].length - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println("|");
        }

        System.out.println("RESULTADO\n");

        System.out.println("z = " + matriz[0][columnaTotal - 1] + "\n");
        resultados = resultados + "z = " + decimales.format(matriz[0][columnaTotal - 1]) + "\n";
        Ecuacion ecuacionResultado = new Ecuacion();
        ecuacionResultado.setResultado(matriz[0][columnaTotal - 1]);
        for (int i = 1; i < filas; i++) {
            char m1[] = v2[i].toCharArray();
            if (m1[0] == 'x') {
                System.out.println(" " + v2[i] + " = " + decimales.format(matriz[v4[i]][columnaTotal - 1]) + "\n");
                resultados = resultados + v2[i] + " = " + decimales.format(matriz[v4[i]][columnaTotal - 1]) + "\n";

                if ("x1".equals(v2[i])) {
                    ecuacionResultado.setVariableX1(matriz[v4[i]][columnaTotal - 1]);
                }
                if ("x2".equals(v2[i])) {
                    ecuacionResultado.setVariableX2(matriz[v4[i]][columnaTotal - 1]);
                }
            }
        }
        return ecuacionResultado;
    }
}
