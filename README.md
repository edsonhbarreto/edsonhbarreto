# Meu Roteiro — app de viagem (Android)

App Android nativo (Kotlin + Jetpack Compose) que conecta um mapa aos lugares
da sua viagem: cada lugar tem fotos, e você acompanha lembretes/reservas com
checkbox até marcar como concluído.

## Funcionalidades

- **Mapa** — segure o dedo em qualquer ponto do mapa para adicionar um lugar
  da viagem ali. Cada lugar ganha uma cor própria (marcador e cartão).
- **Lugares** — grade colorida com foto de capa de cada lugar; toque para
  abrir o detalhe.
- **Detalhe do lugar** — carrossel de fotos (escolhidas direto da galeria do
  celular, sem precisar de internet) e os lembretes/reservas vinculados a
  esse lugar.
- **Checklist** — lista geral de lembretes e reservas, com checkbox para
  marcar como concluído, filtro por tipo, e vínculo opcional a um lugar.
- Tudo é salvo localmente (Room/SQLite), então os checkboxes marcados e as
  fotos adicionadas persistem entre sessões.

## Stack

- Kotlin, Jetpack Compose, Material 3 (tema colorido "pôr do sol")
- Navigation Compose (mapa / lugares / checklist)
- Room (persistência local)
- Coil (carregamento de imagens da galeria)
- Maps Compose (`com.google.maps.android:maps-compose`) para o mapa

## Como rodar

1. Abra a pasta do projeto no **Android Studio** (Koala ou mais recente).
2. Copie `local.properties.example` para `local.properties` e:
   - confirme o `sdk.dir` (o Android Studio geralmente preenche sozinho);
   - gere uma **chave de API do Google Maps** em
     https://console.cloud.google.com/google/maps-apis/credentials (habilite
     a API "Maps SDK for Android" no seu projeto do Google Cloud) e cole em
     `MAPS_API_KEY`.
3. Deixe o Android Studio sincronizar o Gradle e rode no emulador ou em um
   celular físico (Android 8.0 / API 26 ou superior).

Sem a chave do Maps o app compila, mas a aba "Mapa" fica em branco — as
outras abas (Lugares, Checklist) funcionam normalmente.

## Ideias para evoluir

- Notificações push para lembretes com data/hora.
- Compartilhar o roteiro com outras pessoas (sincronização em nuvem).
- Rota entre os lugares marcados no mapa.
- Backup/exportação do roteiro em PDF.
