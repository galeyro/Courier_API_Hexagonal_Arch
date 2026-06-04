# CI Pipeline - GitHub Actions

## Overview

Este proyecto cuenta con un pipeline de integración continua (CI) configurado en GitHub Actions que ejecuta automáticamente la construcción, pruebas y análisis de calidad de código en cada push y pull request a la rama `main`.

## Archivo de configuración

```
.github/workflows/ci.yml
```

## Triggers

| Evento | Rama | Descripción |
|--------|------|-------------|
| `push` | `main` | Ejecuta el pipeline al hacer push directo a main |
| `pull_request` | `main` | Ejecuta el pipeline en cada PR hacia main |

## Configuración del entorno

| Componente | Versión | Detalle |
|------------|---------|---------|
| Runner | `ubuntu-latest` | Entorno Linux actualizado |
| Java | 23 | Distribución Temurin (OpenJDK) |
| Maven | 3.9.14 | Via Maven Wrapper (`./mvnw`) |
| Caché | `~/.m2/repository` | Caché automático de dependencias Maven |

## Etapas del pipeline

### Etapa 1 — Construcción
```bash
./mvnw clean install -DskipTests -B
```
- Compila el código fuente
- Empaqueta el JAR
- **No ejecuta tests** (separación de responsabilidades)
- Descarga y cachea dependencias Maven

### Etapa 2 — Pruebas unitarias
```bash
./mvnw test -B
```
- Ejecuta todos los tests con Surefire
- Genera datos de cobertura JaCoCo (`target/jacoco.exec`)
- Utiliza las clases compiladas de la Etapa 1

### Etapa 3 — JaCoCo (Cobertura de código)
```bash
./mvnw jacoco:report -B
```
- Genera reporte XML desde `target/jacoco.exec`
- **Artifact**: `jacoco-report` → `target/site/jacoco/jacoco.xml`

### Etapa 4 — Checkstyle (Estilo de código)
```bash
./mvnw checkstyle:check -B
```
- Valida estilo de código (indentación, imports, longitud de línea)
- Usa configuración: `checkstyle.xml` + `checkstyle-suppressions.xml`
- **Artifact**: `checkstyle-report` → `target/checkstyle-result.xml`

### Etapa 5 — PMD (Análisis estático)
```bash
./mvnw pmd:check -B
```
- Análisis estático de código (buenas prácticas, diseño, seguridad)
- Usa configuración: `pmd-ruleset.xml`
- **Artifact**: `pmd-report` → `target/pmd.xml`

### Etapa 6 — SpotBugs (Detección de bugs)
```bash
./mvnw spotbugs:check -B
```
- Análisis de bytecode para detectar bugs potenciales
- Usa configuración: `spotbugs-exclusions.xml`
- **Artifact**: `spotbugs-report` → `target/spotbugsXml.xml`

## Artifacts generados

| Nombre | Archivo | Contenido |
|--------|---------|-----------|
| `jacoco-report` | `jacoco.xml` | Reporte de cobertura de código |
| `checkstyle-report` | `checkstyle-result.xml` | Reporte de estilo de código |
| `pmd-report` | `pmd.xml` | Reporte de análisis estático |
| `spotbugs-report` | `spotbugsXml.xml` | Reporte de detección de bugs |

Los artifacts se pueden descargar desde la pestaña **Actions** del repositorio en GitHub.

## Duración típica

| Etapa | Duración aproximada |
|-------|---------------------|
| Build | ~49s (primera vez) / ~15s (con caché) |
| Tests | ~11s |
| JaCoCo | ~3s |
| Checkstyle | ~5s |
| PMD | ~6s |
| SpotBugs | ~9s |
| **Total** | **~1m 41s** (primera ejecución) |

## Modo de ejecución

Todos los plugins de calidad están configurados en modo **warnings-only**:
- `failOnViolation=false` (Checkstyle, PMD)
- `failOnError=false` (SpotBugs)

Esto significa que el pipeline **no falla** si se detectan violaciones de estilo o bugs potenciales. Las advertencias se registran en los reportes XML pero no bloquean el build.

## Versiones de Actions

| Action | Versión | Propósito |
|--------|---------|-----------|
| `actions/checkout` | v6 | Clonar el repositorio |
| `actions/setup-java` | v5 | Configurar Java 23 + caché Maven |
| `actions/upload-artifact` | v7 | Subir reportes XML como artifacts |

## Verificación manual

Para ejecutar el pipeline localmente:

```bash
# Etapa 1 - Build
./mvnw clean install -DskipTests

# Etapa 2 - Tests
./mvnw test

# Etapa 3 - JaCoCo
./mvnw jacoco:report
# Reporte: target/site/jacoco/index.html

# Etapa 4 - Checkstyle
./mvnw checkstyle:check

# Etapa 5 - PMD
./mvnw pmd:check

# Etapa 6 - SpotBugs
./mvnw spotbugs:check
```

O ejecutar todo de una vez:

```bash
./mvnw clean verify
```

## Troubleshooting

### El pipeline no se ejecuta
- Verificar que GitHub Actions está habilitado en **Settings → Actions → General**
- Confirmar que el archivo `.github/workflows/ci.yml` está en la rama `main`

### Error de permisos en mvnw
- El workflow incluye `chmod +x ./mvnw` automáticamente
- Si falla localmente: `chmod +x ./mvnw`

### Dependencias no se cachean
- Verificar que `cache: 'maven'` esté presente en `actions/setup-java`
- El caché se almacena en `~/.m2/repository`

### Artifacts no aparecen
- Verificar que los paths en `upload-artifact` coincidan con los generados
- Los artifacts expiran después de 90 días (retención por defecto)

## Recursos adicionales

- [Documentación de plugins de calidad](README-PLUGINS-CALIDAD.md)
- [Workflow file](.github/workflows/ci.yml)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
