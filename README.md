# Proyecto °2: Simulador de Memoria

## Integrantes
- Ariana Alvarado Molina
- María Paula Bolaños Apú
- Marco Herrera González

## Descripción

El presente proyecto consiste en una aplicación para simular el funcionamiento de una MMU con memoria virtual y paginación. La aplicación permite al usuario inicialmente escoger un
algoritmo de paginación a utilizar (FIFO, MRU, SC y NRD) y generar o cargar un archivo con las instrucciones que serán ejecutadas por la simulación. Si el usuario escoge generar
el archivo, deberá indicar la cantidad de procesos y el total de instrucciones a generar en el archivo, junto con la semilla de generación, la cual permite repetir escenarios
en la simulación. Al ejecutar la simulación, se ejecutarán dos sesiones con las mismas instrucciones, una sesión utiliza el algoritmo óptimo de paginación y la otra utiliza
el algoritmo seleccionado por el usuario. Mientras se ejecuta la simulación, se podrá visualizar el estado de las dos sesiones de forma comparativa, en la que se muestran datos
como el tiempo de simulación, memoria real utilizada, memoria virtual utiliza, tiempo de thrashing, el estado de la MMU, y entre otros datos relevantes para comparar el
rendimiento de ambos algoritmos. 

## Instalación y Uso
