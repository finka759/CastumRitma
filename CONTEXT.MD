# 🎙 Проект: Castum Ritma — Статус "Диплом" (Конец Сессии 3)

## ✅ Текущие достижения (Core)
- **Architecture:** Single Activity, Clean Architecture (Data/Domain/UI), Koin DI.
- **UI:** SearchScreen (Cards), DetailsScreen (Episodes), PlayerScreen (Media3).
- **L10n:** Поддержка RU/EN, темная/светлая темы (M3).
- **Playback:** Работающий стриминг через Media3 Service в фоне.

## 📋 Соответствие дипломным требованиям
- [x] Работа с удаленным сервером (iTunes API).
- [x] Карточки объектов (LazyColumn + Cards).
- [x] Переход к детальному виду.
- [x] Локализация (RU/EN).
- [ ] Экран настроек (Выбор языка/темы) — **В ПЛАНЕ**.
- [ ] Offline режим (Room caching) — **В ПЛАНЕ**.
- [ ] Unit Test — **В ПЛАНЕ**.
- [ ] CI Github Actions — **В ПЛАНЕ**.

## 🚀 План на Сессию 4: "Настройки и Оффлайн"
1. **SettingsScreen:** Реализация экрана настроек (Язык, Тема).
2. **Room Integration:** Кэширование эпизодов для Offline-доступа.
3. **Unit Testing:** Тестирование логики маппинга данных.
4. **CI Setup:** Настройка Github Actions для проверки билда.




🎙 Проект: Castum Ritma — Статус «Дипломный стандарт» (Конец Сессии 3)
✅ Текущий прогресс по критериям
Single Activity: Полная реализация на Jetpack Compose. Навигация через NavHost.
Список и карточки (UI):
SearchScreen: Список подкастов (карточки).
DetailsScreen: Список эпизодов (карточки с метаданными).
Детальный вид: PlayerScreen с отображением подробной информации об эпизоде и управлением Media3.
Удаленный сервер: Интеграция с iTunes Search API через Retrofit2 и GSON.
Локализация: Поддержка RU/EN через strings.xml и locales_config.xml.
DI: Внедрение зависимостей через Koin (модули appModule и playerModule).
Работа со звуком: Фоновое воспроизведение через MediaSessionService (Media3).
🛠 Технологический стек (Session 3)
UI: Compose, Material 3, Coil (картинки).
DI: Koin (Android, Compose, Navigation).
Network: Retrofit, OkHttp (Logging).
Media: Media3 (ExoPlayer + Session).
Compatibility: API Desugaring (поддержка java.time на API 24+).
📁 Структура проекта (Актуальная)
text
ru.mactiva.castumritma
├── data
│   ├── network (DTO, API Service)
│   ├── repository (Impl)
│   ├── mappers (Трансформация данных)
│   └── playback (PlaybackService - Media3)
├── domain (Models: Podcast, Episode)
├── di (AppModule, PlayerModule)
├── ui
│   ├── navigation (Screen, AppNavigation)
│   ├── screens (search, details, player)
│   ├── viewmodels (Search, Details, Player - Single)
│   └── theme (Material 3)
└── utils (Constants)
Используйте код с осторожностью.

📋 План реализации недостающих дипломных требований (Сессия 4)
Экран настроек (SettingsScreen):
Переключатель Темной/Светлой темы (через DataStore или SharedPreferences).
Выбор языка (RU/EN) внутри приложения.
Offline режим (Room):
Создание Entity для Эпизодов.
Кэширование последнего просмотренного списка эпизодов в БД.
Тестирование:
Написание Unit-теста для Mappers.kt (проверка форматирования даты и времени).
CI/CD:
Настройка .github/workflows/android.yml для автоматической сборки проекта при push.



# 🎙 Проект: Castum Ritma — Точка сохранения (Конец Сессии 3)

## ✅ Текущий статус
- **DI (Koin):** Полная настройка `AppModule` и `PlayerModule`. Используются `viewModelOf` и `singleOf` для автоматического внедрения зависимостей. `PlayerViewModel` работает как синглтон, обеспечивая непрерывное воспроизведение.
- **Media3 (Playback):** Реализован `PlaybackService` (MediaSessionService). Настроен `ExoPlayer` с поддержкой Audio Focus. В манифесте разрешен трафик `cleartext` (HTTP) и прописаны Foreground permissions.
- **Navigation:** Настроен `NavHost` с маршрутами `Search`, `Details` и `Player`. Реализован паттерн "Shared State" через `PlayerViewModel` для передачи данных эпизода без сложной сериализации.
- **UI:**
    - `DetailsScreen` отображает список эпизодов и передает клик в навигацию.
    - `PlayerScreen` отображает обложку, название и управляет состоянием Play/Pause.
    - Добавлены расширенные иконки Material (Pause/PlayArrow).

## 📁 Ключевые файлы (Session 3)
- `di/AppModule.kt` — Репозитории и ВьюМодели.
- `data/playback/PlaybackService.kt` — Сервис фонового звука.
- `ui/navigation/AppNavigation.kt` — Логика переходов и связи VM.
- `ui/viewmodels/PlayerViewModel.kt` — Центр управления ExoPlayer.

## 🛠 Код-стейт (PlayerViewModel.kt)
```kotlin
class PlayerViewModel(private val player: ExoPlayer) : ViewModel() {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentEpisode = MutableStateFlow<Episode?>(null)
    val currentEpisode = _currentEpisode.asStateFlow()

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
        })
    }

    fun loadAndPlay(episode: Episode) {
        if (player.currentMediaItem?.mediaId == episode.audioUrl) return
        _currentEpisode.value = episode
        val mediaItem = MediaItem.Builder()
            .setMediaId(episode.audioUrl)
            .setUri(Uri.parse(episode.audioUrl))
            .setMediaMetadata(MediaMetadata.Builder().setTitle(episode.title).build())
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
    }
}



# 🎙 Проект: Castum Ritma — Точка сохранения (Конец Сессии 2)

## ✅ Текущий статус
- **UI:** DetailsScreen отображает список эпизодов. Заглушки обложек реализованы через Compose Box ("CR"), исключая краши XML-парсера.
- **Data:** Настроен маппинг в `toEpisodeDomain()` с поддержкой локализации дат (Locale.getDefault) и красивым форматированием времени.
- **L10n:** Поддержка RU/EN через `strings.xml` и `locales_config.xml`.
- **DI:** В `build.gradle` добавлены зависимости **Koin** (android, compose, navigation).
- **Compatibility:** Включен **API Desugaring** для работы `java.time` на Android 7.0+ (minSdk 24).

## 📁 Файловая структура (Data Layer)
- `data/network/dto/PodcastDto.kt` — модели iTunes.
- `data/mappers/Mappers.kt` — логика трансформации в Domain.
- `data/repository/PodcastRepositoryImpl.kt` — логика получения данных.

## 🚀 План на Сессию 3: "Звук и DI"
1. **Koin Setup:** Создание `AppModule.kt` и инициализация Koin в классе `Application`.
2. **Media3 Service:** Реализация `PlaybackService` для фонового воспроизведения.
3. **MiniPlayer:** Создание плавающей панели управления в UI.
4. **Playback Logic:** Запуск эпизода по клику из `DetailsScreen`.









🎙 Проект: Castum Ritma — Контекст завершения Сессии 2
📝 Текущее состояние
Приложение полностью готово к внедрению мультимедийного функционала. Список эпизодов отображается корректно, данные проходят через мапперы с поддержкой локализации (RU/EN), а архитектура разделена на четкие слои. Решена проблема с ресурсами через Compose-заглушки.
✅ Что реализовано
UI & UX:
DetailsScreen с LazyColumn и обработкой пустых состояний.
EpisodeItem с брендированной заглушкой "CR" (фикс ошибки viewportWidth).
Полная поддержка Dark/Light тем через Material 3.
Data & Network:
Retrofit настроен на iTunes API.
Реализован маппинг в toEpisodeDomain() с использованием java.time и Locale.getDefault().
Поддержка API Desugaring для работы дат на старых Android (от API 24).
Локализация (L10n):
Подготовлены strings.xml для RU и EN.
Настроен locales_config.xml для выбора языка внутри приложения (Android 13+).
Build System:
Разделение на debug (с суффиксом) и release (с оптимизацией R8).
Настроены фильтры ресурсов для уменьшения размера APK.
📂 Структура проекта (Target)
data/network/dto/: Сетевые модели (PodcastDto).
data/mappers/: Логика трансформации (Mappers.kt).
domain/: Чистые модели бизнес-логики (Episode.kt).
ui/screens/details/: Экран эпизодов.
🛠 Код для Сессии 3 (Mappers.kt)
kotlin
fun PodcastDto.toEpisodeDomain(): Episode {
return Episode(
id = this.trackId ?: 0L,
title = this.trackName ?: "",
description = this.description ?: "",
audioUrl = this.previewUrl ?: "",
date = formatDate(this.releaseDate),
duration = formatDuration(this.trackTimeMillis ?: 0L),
imageUrl = this.artworkUrl600 ?: this.artworkUrl100
)
}

private fun formatDate(isoDate: String?): String {
if (isoDate.isNullOrEmpty()) return ""
return try {
val parsedDate = ZonedDateTime.parse(isoDate)
val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
parsedDate.format(formatter)
} catch (e: Exception) { isoDate.take(10) }
}
Используйте код с осторожностью.

🚀 План на Сессию 3: "Инъекция и Звук"
Koin DI: Быстрая настройка внедрения зависимостей. Избавляемся от ручных Factory.
Media3 Service: Создание PlaybackService для воспроизведения в фоне.
Player UI: Реализация клика по эпизоду и создание MiniPlayer внизу экрана.
State Management: Синхронизация состояния плеера с UI через MediaController.


















🎙 Проект: Castum Ritma (Android Podcast Hub) — Контекст после Сессии 2
📝 Текущий статус
Приложение успешно загружает список эпизодов подкаста из iTunes API, отображает их в DetailsScreen с использованием Jetpack Compose и Coil. Архитектура построена на принципах Clean Architecture (MVVM).
✅ Реализованный функционал
Network Layer: Настроен Retrofit с ITunesApiService. Корректно обрабатываются сущности podcast (для поиска) и podcastEpisode (для деталей).
Domain Layer:
Podcast: Модель основной коллекции.
Episode: Модель эпизода (ID, заголовок, описание, URL аудио, дата, длительность, обложка).
Data Layer:
PodcastDto: Универсальный объект для парсинга iTunes (использует wrapperType).
Mappers.kt: Логика преобразования DTO в доменные модели с форматированием времени (MM:SS).
UI Layer:
SearchScreen: Поиск подкастов.
DetailsScreen: Список эпизодов с LazyColumn и Card.
UI-фикс: Заглушки обложек реализованы через Compose Box с текстом "CR", что предотвращает краши из-за ошибок XML-парсера.
🛠 Технический стек
UI: Jetpack Compose, Material 3, Coil (картинки).
Navigation: Compose Navigation (передача collectionId).
Network: Retrofit 2 + Gson.
DI: Manual Dependency Injection (через ViewModelProvider.Factory).
📁 Ключевые фрагменты кода
Episode Model & Mapper
kotlin
data class Episode(
val id: Long,
val title: String,
val description: String,
val audioUrl: String,
val date: String,
val duration: String?,
val imageUrl: String?
)

// Маппер в Mappers.kt
fun PodcastDto.toEpisode(): Episode {
return Episode(
id = this.trackId ?: 0L,
title = this.trackName ?: "Без названия",
description = this.description ?: "Описание отсутствует",
audioUrl = this.previewUrl ?: "",
date = this.releaseDate ?: "",
duration = formatDuration(this.trackTimeMillis ?: 0L),
imageUrl = this.artworkUrl600 ?: this.artworkUrl100
)
}
Используйте код с осторожностью.

UI: EpisodeItem (с безопасной заглушкой)
kotlin
@Composable
fun EpisodeItem(episode: Episode) {
Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
Text("CR", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
AsyncImage(model = episode.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
}
Spacer(modifier = Modifier.width(16.dp))
Column(modifier = Modifier.weight(1f)) {
Text(text = episode.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
if (!episode.duration.isNullOrEmpty()) {
Text(text = "Длительность: ${episode.duration}", style = MaterialTheme.typography.bodySmall)
}
}
}
}
}
Используйте код с осторожностью.

🚀 План на Сессию 3: "Media3 & Playback"
Зависимости: Добавить androidx.media3:media3-exoplayer и media3-session.
Service: Создать PlaybackService для фонового воспроизведения.
ViewModel: Интегрировать MediaController в DetailsViewModel для запуска аудио по клику.
MiniPlayer: Реализовать плавающую панель управления плеером в нижней части экрана.









✅ Реализовано (Итоги Сессии 2)
Network Layer:
Настроен RetrofitClient с базовым URL iTunes.
Реализован метод getEpisodes в ITunesApiService с корректными параметрами (entity=podcastEpisode).
Data & Domain:
Создана модель Episode и DTO PodcastDto с поддержкой специфичных полей (trackId, previewUrl, trackTimeMillis).
Реализован маппер toEpisode() с форматированием длительности (MM:SS).
В PodcastRepository настроена фильтрация для отделения заголовка коллекции от списка эпизодов.
UI & Navigation:
DetailsScreen: Экран деталей теперь принимает collectionId, загружает данные через DetailsViewModel и отображает их в LazyColumn.
Navigation: Настроен NavHost с передачей аргументов и внедрением DetailsViewModel через DetailsViewModelFactory.
MainActivity: Полностью настроена инициализация репозитория и навигации.
🛠 Технический стек
UI: Jetpack Compose, Material 3.
Navigation: Compose Navigation.
Network: Retrofit + Gson.
Architecture: MVVM + Clean Architecture (Domain/Data/UI).
DI: Ручное внедрение через Factory (подготовлено к переходу на Koin/Hilt).
📁 Ключевые файлы
DetailsViewModel: Управляет состоянием списка эпизодов.
PodcastRepository: Логика получения и фильтрации данных.
Mappers.kt: Преобразование DTO -> Domain.
🚀 План на Сессию 3: "Оживление звука"
Media3 Setup: Добавление зависимостей androidx.media3 (exoplayer, session).
Playback Service: Создание MediaSessionService для фонового воспроизведения.
Player Integration:
Реализация клика по эпизоду для запуска audioUrl.
Создание мини-плеера (MiniPlayer) внизу экрана.
UX Improvement: Обработка ошибок сети и пустых состояний (shimmer/заглушки).





# 🎙 Проект: Castum Ritma (Android Podcast & Audiobook Hub)

## 📌 Общая концепция
**Название:** Castum Ritma
**Суть:** Интеллектуальный клиент для прослушивания подкастов и локальных аудиокниг.
**Киллер-фича:** «Radio Mode» (музыкальные вставки) и умная фильтрация разговорного контента.

## ✅ Текущий статус (Сессия 1 завершена)
- **UI:** Реализован `SearchScreen` (Compose + Material 3) с поддержкой кириллицы.
- **Network:** Интеграция с iTunes API через Retrofit.
- **Architecture:** Полное разделение на **Data** (Network DTO) и **Domain** (Clean Kotlin Models).
- **Images:** Асинхронная загрузка обложек через Coil.

## 🛠 Архитектурные особенности (Для диплома)
1. **Multi-Source Ready:** Архитектура через `PodcastSource` (Enum) и мапперы позволяет подключать Spotify, RSS и локальные файлы без изменения UI.
2. **KMP Vision:** Использование чистых Kotlin-моделей в Domain-слое подготавливает проект к переходу на **Kotlin Multiplatform (iOS)**.
3. **Smart Local Filter:** Алгоритм отделения аудиокниг от музыки в памяти устройства:
    - Фильтрация по длительности (файлы > 10 минут).
    - Анализ метаданных (ID3-теги: Genre, Album Artist).
    - Сканирование специализированных папок (`/Podcasts`, `/Audiobooks`).

## 📁 Структура проекта
- `ru.mactiva.castumritma.data`: Сетевые модели (`PodcastDto`), API-сервис и Retrofit-клиент.
- `ru.mactiva.castumritma.domain`: Бизнес-логика (`Podcast.kt`) — сердце приложения.
- `ru.mactiva.castumritma.ui`: Экраны (`SearchScreen`), ViewModel и тема Material 3.

## 🚀 План на Сессию 2
1. **Navigation:** Реализация переходов между поиском и деталями.
2. **Details Screen:** Получение списка эпизодов (Lookup по `collectionId`).
3. **Media3 (ExoPlayer):** Создание базового сервиса воспроизведения.
4. **UX:** Добавление **Filter Chips** для быстрого переключения между iTunes и Локальными файлами.







# 🎙 Проект: Castum Ritma (Android Podcast Client) — Контекст разработки

## 📌 Общая концепция
**Название:** Castum Ritma
**Суть:** Современный клиент для поиска и прослушивания подкастов и аудиокниг с фокусом на офлайн-режим и расширяемую архитектуру источников.
**Киллер-фича (задел):** «Radio Mode» — автоматические музыкальные вставки в поток подкаста.

## 🛠 Технологический стек (MVP)
- **Язык:** Kotlin + Coroutines + Flow.
- **UI:** Jetpack Compose + Material 3 (Dynamic Colors).
- **Network:** Retrofit 2 + Gson.
- **Images:** Coil-Compose.
- **Architecture:** MVVM + Clean Architecture (разделение на Data и Domain слои).

## ✅ Реализовано на Текущий Момент (Сессия 1)
1. **Базовая активность:** `MainActivity` настроена на запуск `SearchScreen` внутри `PodCastScoutTheme`.
2. **Экран Поиска:**
    - Поле ввода `OutlinedTextField` с поддержкой кириллицы.
    - Список `LazyColumn` с карточками `PodcastItem`.
    - Реактивное обновление поиска при вводе более 2-х символов.
3. **Слой Данных (Data Layer):**
    - `ITunesApiService` для работы с Apple Search API.
    - `PodcastDto` — модель для парсинга JSON.
    - `NetworkModule` — синглтон для инициализации Retrofit.
4. **Слой Бизнес-логики (Domain Layer):**
    - `Podcast` — чистый Kotlin-класс (ID, Title, Author, ImageUrl, Source).
    - `PodcastSource` (Enum) — задел для выбора источников (ITUNES, SPOTIFY, LOCAL, RSS).
    - **Mapper:** Функция-расширение `toDomain()` для конвертации сетевых моделей в модели приложения.

## 📐 Архитектурные решения
- **Инверсия зависимостей:** UI-слой (Compose) и ViewModel работают только с `Podcast` (Domain), не зная о деталях реализации API. Это позволяет добавлять новые источники без переписки экранов.
- **UX-выбор:** В будущем выбор источников будет реализован через **Filter Chips** (Чипсы) под строкой поиска для мгновенной фильтрации (Все / iTunes / Локальные).

## 📂 Структура файлов
- `data/PodcastDto.kt` — Сетевые модели и маппер.
- `domain/Podcast.kt` — Бизнес-модель и перечисление источников.
- `ui/screens/SearchScreen.kt` — Верстка экрана поиска.
- `ui/screens/SearchViewModel.kt` — Логика поиска и управления состоянием (StateFlow).

## 🚀 План на Сессию 2
1. **Navigation:** Подключение `androidx.navigation:navigation-compose`.
2. **Details Screen:** Экран эпизодов (запрос `lookup` в iTunes по `collectionId`).
3. **Player Setup:** Первичная настройка Media3 (ExoPlayer) для воспроизведения потока.





# 🎯 Проект: PodCast Scout (Диплом)

## 🛠 Текущий статус (Session 1)
- [x] Настроен проект: Compose + Material 3 + Kotlin DSL.
- [x] Реализована сеть: Retrofit + iTunes API.
- [x] Создан экран поиска: `SearchScreen` с поддержкой кириллицы.
- [x] Настроена загрузка обложек: Coil Image Loading.
- [x] Архитектура: MVVM (ViewModel + StateFlow).

## 📂 Структура данных (Data Layer)
- `PodcastDto`: содержит ID, названия и логику выбора лучшей обложки (`bestArtworkUrl`).
- `ITunesApiService`: эндпоинт `/search` с фильтром по подкастам.

## 🚀 План на Session 2
1. **Navigation:** Добавить `NavHost` для переходов.
2. **Details Screen:** Экран со списком эпизодов (запрос по `collectionId`).
3. **Media3:** Подготовка сервиса плеера.

## 🔗 Полезные ссылки
- API Base: `https://itunes.apple.com`
- Библиотеки: Retrofit, Coil, Room, Media3.




📝 Контекст проекта: PodCast Scout (MVP)
Текущее состояние:
Стек: Kotlin, Jetpack Compose, Material 3, Retrofit + Gson, Coil (для картинок).
Реализовано: Основная Activity, SearchScreen с полем ввода и списком LazyColumn. Настроен SearchViewModel с логикой сетевого запроса к iTunes API.
API: Используется ITunesApiService с базовым URL https://itunes.apple.com.
Структура данных:
PodcastResponse: Содержит resultCount и results (List).
PodcastDto: Поля collectionId, artistName, collectionName, artworkUrl100, artworkUrl600. Есть вспомогательное свойство bestArtworkUrl.
Файловая структура:
data/: PodcastDto.kt, ITunesApiService.kt, NetworkModule.kt.
ui/screens/: SearchScreen.kt, SearchViewModel.kt.
ui/theme/: Стандартные файлы Material 3 (Theme.kt, Color.kt, Type.kt).
Зависимости в build.gradle.kts:
Retrofit + Gson Converter.
Coil-Compose.
Lifecycle ViewModel Compose.
Что сделано на текущем шаге:
Исправлена проблема с вводом кириллицы в эмуляторе, настроены карточки PodcastItem с отображением обложек через Coil. Приложение успешно получает данные по поисковому запросу.
План на следующую сессию:
Добавить навигацию (Jetpack Navigation) между экранами.
Создать Screen Detail (экран эпизодов подкаста).
Настроить запрос списка эпизодов по collectionId.