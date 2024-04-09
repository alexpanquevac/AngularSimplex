import { Component, OnInit, OnDestroy } from '@angular/core';
import { Ecuacion } from './Modelo/Ecuacion';
import { MessageService, SelectItem } from 'primeng/api';
import { ServiceService } from './Service/service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})




export class AppComponent implements OnInit {
  date14!: Date;
  ecuaciones: Ecuacion[] = [];
  ecuacion: Ecuacion;
  ecuacionesResultados: Ecuacion[] = [];
  dialogo: boolean;
  variableX1: number = 0;
  objetivoX1: number;
  objetivoX2: number;
  indicePestana: number = 0;

  constructor(private service: ServiceService) {
  }

  public signos: SelectItem[] = [
    { label: '>=', value: '>=' },
    { label: '<=', value: '<=' },
    { label: '=', value: '=' },
  ];

  public tipoProblema: SelectItem[] = [
    { label: 'Maximización', value: 'MAX' },
    { label: 'Minimización', value: 'MIN' },
  ];

  public images: any = [];

  ngOnInit() {


    this.ecuacion = new Ecuacion();
  }

  agregar() {
    this.ecuacion = new Ecuacion();
    this.dialogo = true;
  }

  cerrarDialogo() {
    this.dialogo = false;
  }

  guardarRestriccion() {
    this.dialogo = false;
    this.ecuaciones.push(this.ecuacion);
  }

  limpiar(){
    this.ecuacion = new Ecuacion();
    this.ecuaciones = [];
    this.ecuacionesResultados = [];
    this.images = [];
    this.objetivoX1 = null;
    this.objetivoX2 = null;
  }

  graficar() {

    this.ecuacion = new Ecuacion();
    this.ecuacion.variableX1 = this.objetivoX1;
    this.ecuacion.variableX2 = this.objetivoX2;
    this.ecuacion.tipoEcuacion = "Objetivo";
    let verifica;
    for (let i = 0; i < this.ecuaciones.length; i++) {
      if ("Objetivo" === this.ecuaciones[i].tipoEcuacion) {
        verifica = 1;
      }
    }

    if (verifica != 1) {
      this.ecuaciones.push(this.ecuacion);
    }


    this.service.graficar(this.ecuaciones)
      .subscribe(data => {
        this.ecuacionesResultados = data;
        this.indicePestana = 1;
        this.service.obtenerImagen().subscribe(response => {
          this.images = response;
        })
      })
  }


}


