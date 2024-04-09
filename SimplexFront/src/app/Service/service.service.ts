import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http'
import { Ecuacion } from "../Modelo/Ecuacion";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})

export class ServiceService {

    constructor(private http: HttpClient) {}

        Url = 'http://localhost:8080/metodoGrafico';

        graficar(ecuaciones: Ecuacion[]):Observable<Ecuacion[]>{
            console.log("si llego");
            return this.http.post<Ecuacion[]>(this.Url+"/grafica", ecuaciones);
        }

        obtenerImagen(): Observable<any>{
            return this.http.get(this.Url+"/obtenerImagen")
        }

    }