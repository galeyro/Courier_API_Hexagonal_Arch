# Quality Analysis Plugins - Courier API

## Overview

This project has 4 quality analysis plugins configured in the Maven build, all set to **warnings-only** mode (the build never fails from quality violations).

## Plugins

| Plugin | Version | Phase | Report Location |
|--------|---------|-------|-----------------|
| JaCoCo | 0.8.12 | test | `target/site/jacoco/jacoco.xml` |
| Checkstyle | 3.6.0 | validate | `target/checkstyle-result.xml` |
| PMD | 3.26.0 (engine 7.7.0) | validate | `target/pmd.xml` |
| SpotBugs | 4.9.3.0 (engine 4.9.3) | verify | `target/spotbugsXml.xml` |

## Running

```bash
mvn clean verify
```

All reports are generated in `target/`. Open `quality-dashboard.html` (from the `target/` directory after a build) for a consolidated dashboard.

## JaCoCo (Code Coverage)

- **Phase**: test (prepare-agent + report)
- **Config**: Default thresholds, generates HTML + XML reports
- **Report**: `target/site/jacoco/index.html`

## Checkstyle (Code Style)

- **Phase**: validate
- **Config**: Custom `checkstyle.xml` (4-space indent, 120-char line length, import order)
- **Suppressions**: `checkstyle-suppressions.xml` (Lombok, Controllers, Configuration, DTOs, exceptions, models, Application class)
- **Checkstyle version**: 10.20.2 (overridden for token compatibility)
- **Report**: `target/checkstyle-result.xml`

## PMD (Static Analysis)

- **Phase**: validate
- **Config**: `pmd-ruleset.xml` (best practices, code style, design, error prone, security)
- **Plugin version**: 3.26.0, PMD engine overridden to 7.7.0 (Java 23 support)
- **Requires**: `maven-site-plugin` 3.21.0 + `doxia-site-renderer` for site context (PMD report generation)
- **Report**: `target/pmd.xml`

Excluded rules:
- **bestpractices**: UseVarargs, LooseCoupling, GuardLogStatement
- **codestyle**: LocalVariableCouldBeFinal, MethodArgumentCouldBeFinal, OnlyOneReturn, ShortVariable, LongVariable, ShortMethodName, ShortClassName, TooManyStaticImports, CallSuperInConstructor, CommentDefaultAccessModifier, LinguisticNaming, UnnecessaryModifier, FieldNamingConventions, UnnecessaryAnnotationValueElement, AtLeastOneConstructor
- **design**: GodClass, TooManyMethods, UseUtilityClass, DataClass, CouplingBetweenObjects, LawOfDemeter, AbstractClassWithoutAbstractMethod, AvoidCatchingGenericException, LoosePackageCoupling
- **errorprone**: AvoidLiteralsInIfCondition, MissingSerialVersionUID

## SpotBugs (Bug Detection)

- **Phase**: verify (requires compiled bytecode)
- **Config**: Medium threshold, `spotbugs-exclusions.xml`
- **Exclusions**: EI_EXPOSE_REP, EI_EXPOSE_REP2, DTO packages, events domain model

## Site Context (PMD Dependency)

The `maven-site-plugin` (3.21.0) with `doxia-site-renderer` (1.11.1) is required in `<build>` because PMD's report goal extends `AbstractMavenReport`, which needs a Maven site skin context. Without it, `pmd:check` fails with "skin cannot be null".

The `src/site/site.xml` configures `maven-fluido-skin` 2.0.1 as the site skin, and the `<skin>` element satisfies the PMD report mojo's requirement.

## Key Decisions

- All plugins use `failOnViolation=false` / `failOnError=false` — warnings only
- PMD engine overridden from default 7.0.0 to 7.7.0 for latest Java 23 support
- Checkstyle engine overridden to 10.20.2 for token compatibility with the Maven plugin
- SpotBugs engine overridden to 4.9.3 for Java 23 support