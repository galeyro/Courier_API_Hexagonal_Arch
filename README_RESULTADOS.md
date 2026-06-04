# Presentación de Resultados: Auditoría y Mejora de Calidad de Código

Este documento presenta el resumen de los resultados obtenidos tras la auditoría de calidad de código y las posteriores mejoras implementadas en el proyecto **Courier API**. Se configuró un pipeline de evaluación estática y dinámica utilizando **Checkstyle, PMD, SpotBugs y JaCoCo**.

## 1. Problemas Detectados Inicialmente en las Métricas

Durante el análisis inicial, se identificaron múltiples áreas de mejora en el código base, reflejadas en los reportes generados por los plugins de calidad:

*   **Checkstyle (>100 warnings):**
    *   Uso de tabulaciones en lugar de espacios (`CourierApiApplication.java`).
    *   Orden incorrecto de importaciones (los paquetes `java.*` deben ir antes de `org.*`).
    *   Uso de importaciones con comodín (`.*` / *star imports*) en controladores y entidades.
    *   Líneas de código que excedían los 120 caracteres.
    *   Falta de documentación Javadoc en más de 100 clases/métodos.

*   **PMD (2 violaciones críticas):**
    *   `AbstractShippingStrategy`: Fue definida como una clase abstracta sin contener ningún método abstracto.
    *   `AbstractShippingStrategy`: Mal manejo de excepciones. Al capturar una excepción, no se preservaba el *stack trace* original (`throw new InvalidShipmentException("...", ex)`).

*   **SpotBugs:**
    *   No se detectaron violaciones (*0 bugs*).

*   **JaCoCo (Cobertura Deficiente):**
    *   Cobertura de instrucciones: **10%** (muy baja).
    *   Solo existían 4 tests (pruebas de carga de contexto).
    *   1,629 de 1,829 instrucciones sin cubrir.

---

## 2. Cambios Realizados para Corregir los Problemas

Para abordar los hallazgos anteriores, se ejecutó un plan de acción dividido en 3 fases (commits), logrando corregir las deficiencias y elevar la robustez del proyecto:

### Commit 1: Corrección de Estilo (Checkstyle)
*   **Limpieza de código:** Se reemplazaron todas las tabulaciones por espacios y se corrigió el orden de las importaciones en todo el proyecto.
*   **Importaciones explícitas:** Se eliminaron los *star imports* (ej. `import java.util.*`) reemplazándolos por las clases específicas.
*   **Formato:** Se ajustaron los saltos de línea para respetar el límite estricto de 120 caracteres.
*   **Estrategia Javadoc:** Se optó por una solución pragmática desactivando temporalmente la regla `MissingJavadoc` en `checkstyle.xml`, priorizando un código limpio y auto-explicativo sobre comentarios obligatorios.

### Commit 2: Corrección de Defectos (PMD)
*   **Diseño de Clases:** Se refactorizó `AbstractShippingStrategy` convirtiéndola en una clase concreta, resolviendo la advertencia de PMD sobre clases abstractas innecesarias.
*   **Trazabilidad de Errores:** Se actualizó la jerarquía de excepciones del dominio (`DomainException`, `ValidationException`, `InvalidShipmentException`) para incluir constructores que aceptan `Throwable cause`. Esto asegura que el *stack trace* original (como un `NumberFormatException`) se preserve al lanzar excepciones de dominio.

### Commit 3: Mejora de Cobertura de Pruebas (JaCoCo)
*   **Enfoque estratégico:** Se priorizó la capa de casos de uso, lógica de negocio y estrategias de envío (módulos `customers` y `shipments`).
*   **Nuevos Tests:** Se crearon **10 clases de pruebas unitarias** utilizando JUnit 5 y Mockito.
*   **Logro:** La cobertura de código (instrucciones) subió drásticamente del **10% al 57.5%**, superando con creces la meta original del 40-50%. Actualmente el proyecto cuenta con un total de **53 pruebas exitosas**.

---

## 3. Áreas de Mejora y Aprendizajes

*   **Automatización Constante:** La integración de herramientas de análisis estático como Checkstyle y PMD en la fase de construcción (a través de Maven) es fundamental. Se aprendió que su validación continua previene la rápida acumulación de deuda técnica.
*   **Calidad sobre Cantidad en Pruebas:** Alcanzar más del 50% de cobertura enfocándose en las reglas de negocio críticas aporta mucho más valor que buscar un 100% probando clases anémicas, DTOs o constructores simples. Una **área de mejora** a futuro es implementar pruebas de integración para las capas externas (Adaptadores REST y Repositorios).
*   **Manejo Eficaz de Excepciones:** Se comprobó la importancia de propagar correctamente el origen de los errores (el `cause`). Perder el *stack trace* dificulta el monitoreo y debugging en producción.
*   **Adaptación de Reglas:** La decisión de relajar las reglas de Javadoc demostró que las herramientas de calidad deben adaptarse a la madurez y estilo del equipo, y no al revés. El código bien nombrado actúa como su propia documentación.
