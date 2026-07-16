# F1 Teams

App Android que lista as escuderias atuais da Fórmula 1 e, ao selecionar uma,
mostra os dados do time e seus pilotos. Funciona offline: os dados ficam salvos
localmente em SQLite (Room) e a internet só serve pra atualizar o banco.

Feito como desafio técnico usando a API pública https://f1api.dev.

## O que dá pra fazer

- Ver a lista de times ordenada por nome, com os favoritos aparecendo no topo.
- Favoritar/desfavoritar um time (o estado é salvo e não se perde ao atualizar).
- Abrir um time e ver pontos, posição, vitórias e a temporada, além dos pilotos
  ordenados por pontuação (nome, número, nacionalidade, idade, pontos e posição).
- Usar tudo sem internet depois da primeira carga.

## Como rodar

Precisa do Android Studio (versão compatível com AGP 9.3) e um emulador ou
aparelho com Android 7.0 (API 24) ou superior.

1. Abrir o projeto no Android Studio e deixar o Gradle sincronizar.
2. Rodar no emulador/aparelho (botão Run).

Pra gerar o APK de debug pela linha de comando:

```
./gradlew assembleDebug
```

O arquivo sai em `app/build/outputs/apk/debug/app-debug.apk`.

## Stack

- Kotlin
- Views + ViewBinding
- MVVM com Repository
- Room (persistência em SQLite)
- Retrofit + Moshi + OkHttp
- Coroutines / Flow
- Navigation Component (Safe Args)

## Algumas decisões

Usei Room em vez de mexer no SQLite na mão porque ele já resolve a persistência
com bem menos código e é o caminho recomendado pelo próprio Android — no fim das
contas continua sendo SQLite embaixo.

A tela sempre lê do banco (Room como fonte única). A rede só atualiza o banco,
então a UI se comporta igual estando online ou offline. Quando a lista de times
carrega, os pilotos de todos os times são baixados em segundo plano, assim
qualquer escuderia abre offline sem precisar ter sido visitada antes.

A idade do piloto é calculada a partir da data de nascimento, já que a API não
manda a idade pronta. A API mistura os formatos `dd/MM/yyyy` e `yyyy-MM-dd`, por
isso o cálculo aceita os dois.

As cores seguem a identidade da F1 (vermelho, preto e branco).
