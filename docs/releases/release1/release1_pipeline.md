# Release 1: Relatório do Pipeline (CI)


## 1. Configuração
| Item | Valor |
|------|-------|
| Ferramenta | GitHub Actions |
| Arquivo de configuração | .github/workflows/ci.yml |
| Gatilhos | Pull Request e push em main |


## 2. Etapas do pipeline
| Etapa | Comando | Obrigatória para mesclar? |
|-------|---------|---------------------------|
| Build do backend | mvn -B verify | sim                       |
| Testes do backend | mvn -B verify | sim                       |


## 3. Resultados da release
| Métrica | Valor  |
|---------|--------|
| Execuções totais | 15     |
| Aprovadas / falhas | 15 / 0 |
| Taxa de sucesso | 100 %  |
| Tempo médio | 30 seg |
| Maior tempo | 55 seg |


