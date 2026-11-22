# IN THE MYST

Тактическая стратегия с элементами RPG на Java и OpenGL: процедурная генерация мира, пошаговые бои с ИИ, туман войны, динамическое освещение и тени.

> **Статус:** alpha (`Constants.ENGINE_VERSION = "1.0.1 alpha"`). Проект в активной разработке: часть систем (инвентарь, сохранения) ещё не завершена.

## Об игре

Игрок управляет отрядом на стратегической карте, перемещается между локациями, вступает в тактические бои на сетке и сохраняет прогресс в лагере. В проекте две фракции — **Communis** (Герхард, лучник, партизан) и **Anarchy** (бандит, вор, лучник).

Основные возможности:

- **Процедурная генерация** карт и ландшафта на основе шума Перлина (`game/logic/generator`).
- **Тактический бой** на сетке с поиском пути и простым ИИ (`game/logic/battle`, `game/logic/movement`).
- **Графика:** OpenGL 4.3 core, собственные GLSL-шейдеры (32 файла в `data/shader`), буферы теней и карта тумана войны (`core/frame`, `core/buffer`).
- **Сцены:** логотип, главное меню, катсцены, стратегическая карта, тактический бой, экран загрузки (`game/scene`).
- **Инвентарь и экипировка** — в разработке (`game/content/inventory`, `game/hud/inventory`).
- **Сохранение/загрузка** — Java-сериализация в `data/save/data/*.ser` (`game/datafile`).

## Технологический стек

| Компонент | Версия / описание |
|---|---|
| Java | 17 (source/target compatibility) |
| Сборка | Gradle 8.5 (wrapper в `gradle/`) |
| Графика | LWJGL 3.3.3: `lwjgl`, `lwjgl-glfw`, `lwjgl-opengl`, `lwjgl-stb`, `lwjgl-assimp` |
| Математика | JOML 1.10.0 + собственная библиотека `core/math` |
| OpenGL | 4.3 core profile (задаётся в `Window.java`) |
| IDE | IntelliJ IDEA (проектные файлы `.idea/` в репозитории) |

## Требования

- **JDK 17** или новее (`JAVA_HOME` настроен).
- **Видеокарта с OpenGL 4.3** и актуальный драйвер. На macOS максимум OpenGL 4.1 — игра там не запустится (см. раздел проблем).
- **2 ГБ RAM** минимум (в `build.gradle` для `run` задано `-Xms512m -Xmx2g`).
- Windows / Linux. Нативные библиотеки LWJGL подставляются автоматически по ОС в `build.gradle`.

## Структура проекта

```
InTheMyst/
├── build.gradle                 # зависимости, mainClass, JVM-аргументы
├── gradle/wrapper/              # Gradle Wrapper 8.5
├── data/                        # ВСЕ ресурсы игры (читаются с диска, не из jar)
│   ├── config/window/           # window_config.txt — настройки окна
│   ├── content/                 # текстуры, модели, ассеты блоков
│   ├── shader/{vs,fs,gs}/       # GLSL-шейдеры
│   ├── save/{data,image}/       # сохранения (*.ser) и карты (*.png)
│   └── logo/                    # логотипы и иконка окна
├── src/ru/phoenix/
│   ├── core/                    # движок
│   │   ├── kernel/              # Game, CoreEngine, Window, Render, Camera, Input
│   │   ├── shader/              # Shader, ShaderLoader
│   │   ├── buffer/{fbo,ubo,vbo} # FBO, UBO, VBO/VAO
│   │   ├── frame/               # проходы рендера (base, shadow, map)
│   │   ├── loader/              # загрузка текстур, моделей, шрифтов, спрайтов
│   │   ├── config/              # Constants, Default, Time, WindowConfig
│   │   ├── math/                # Matrix4f, Vector2f/3f/4f, Perlin2D
│   │   ├── debug/               # Logger
│   │   └── util/                # ActionVerification, BufferUtil и др.
│   └── game/                    # игровая логика
│       ├── scene/{logo,menu,cut,strategy,tactic,loading}
│       ├── content/{block,characters,object,stage,inventory}
│       ├── logic/{generator,movement,battle,lighting,element,effects}
│       ├── hud/{elements,instance,assembled,inventory}
│       ├── datafile/            # сохранения
│       ├── property/            # GameController и свойства
│       └── loop/                # SceneControl
├── out/                         # вывод IntelliJ IDEA (в git не нужен)
└── QWEN.md                      # заметки по контексту проекта
```

Объём: ~180 Java-файлов, ~36 тыс. строк кода, 32 шейдера, ~6 МБ ассетов.

## Сборка

Все команды выполняются **из корня проекта** (там, где лежит `gradlew.bat`).

Windows (PowerShell / cmd):

```bat
gradlew.bat clean build
```

Linux / macOS:

```bash
./gradlew clean build
```

Полезные задачи:

```bat
gradlew.bat compileJava    :: только компиляция
gradlew.bat clean          :: очистка build/
gradlew.bat jar            :: только jar (без зависимостей)
```

Результат: `build/libs/InTheMyst.jar`. **Важно:** это «тонкий» jar — в него не входят зависимости LWJGL/JOML и каталог `data/`, поэтому `java -jar` с ним не работает. Для запуска используйте `gradlew run` или IDE (см. ниже и раздел проблем).

## Запуск

### Через Gradle (рекомендуется)

```bat
gradlew.bat run
```

Задача `run` уже настроена: рабочая директория — корень проекта, JVM-аргументы `-Xms512m -Xmx2g -Dfile.encoding=UTF-8`, mainClass — `ru.phoenix.core.kernel.Game`.

### Через IntelliJ IDEA

1. `File → Open` → папка проекта, дождаться импорта Gradle.
2. Run Configuration: главный класс `ru.phoenix.core.kernel.Game`.
3. **Working directory** = корень проекта (иначе не найдутся `data/` и `game.log`).
4. Запустить.

Игра стартует с логотипа (12 секунд или `Space`/`Esc`), затем главное меню.

## Управление

| Ввод | Действие |
|---|---|
| `W` `A` `S` `D` | Перемещение камеры |
| `Q` / `E` | Поворот камеры |
| Мышь | Позиция курсора, выбор целей |
| ЛКМ / ПКМ | Основное / альтернативное действие |
| `Shift` + ЛКМ | Альтернативное действие (стратегическая карта) |
| `I` | Инвентарь (тактическая сцена, лагерь) |
| `Space` | Подтверждение / пропуск логотипа |
| `Tab`, `F` | Игровые действия (см. `GameController`) |

Полноценного экрана управления пока нет — привязки разбросаны по сценам.

## Конфигурация и сохранения

- `data/config/window/window_config.txt` — окно и графика: `full_screen`, `window_width`, `window_height`, `samples`, `gamma`, `contrast`, `z_near`, `z_far`, `langueage` (так в файле). Файл перезаписывается при изменении настроек из игры.
- `data/save/data/*.ser` — сохранения (Java-сериализация), `data/save/image/*.png` — карты высот и скриншоты сохранений.
- `game.log` — лог в корне проекта, создаётся при старте и **только дописывается** (не ротируется).

## Найденные проблемы

Ниже — то, что обнаружено при анализе кода и конфигурации. Отсортировано по критичности.

### Критичные

1. **Собранный jar не запускается.** `jar`-задача в `build.gradle` не включает зависимости (нет Shadow/`shadowJar`), а `sourceSets.main.resources` указывает на `src/res` и `src/shaders`, которых не существует. `java -jar build/libs/InTheMyst.jar` упадёт с `NoClassDefFoundError`. Релизного самодостаточного артефакта сейчас нет.
2. **Ресурсы ищутся по относительному пути от текущей директории.** `ImageLoader.load` использует `new File(path)`, `Shader` — `new FileReader("./data/shader/...")`, сохранения — `new File("./data/save/...")`. Запуск из ярлыка, из другой папки или установленной копии ломается; `data/` не попадает в дистрибутив. Нужен classpath-доступ (`getResourceAsStream`) или копирование `data/` в поставку.
3. **macOS не поддерживается фактически.** `Window` запрашивает OpenGL **4.3** core, а macOS даёт максимум 4.1 — `glfwCreateWindow` вернёт `NULL`, игра упадёт с `RuntimeException("The window was not created!")`. При этом `build.gradle` собирает `natives-macos`, а старый README обещал macOS.

### Высокие

4. **Переключение языка не работает.** `WindowConfig.getLangueage()` сравнивает строки через `==` (`languageDescription == "RUSSIAN"`), поэтому всегда возвращается `ENGLISH`. Нужен `equals`.
5. **Лог растёт без ограничений.** `Logger` открывает `game.log` в режиме append и никогда не ротирует и не очищает файл; в репозитории уже лежит лог на ~244 МБ. У пользователя он будет расти при каждом запуске.
6. **Утечки видеопамяти при переинициализации.** При `SceneControl.setReinit(true)` `CoreEngine.run` создаёт новый `Render`, затем `Render.render` создаёт ещё один набор `Base/Shadow/MapRenderFrame`; старые FBO/текстуры нигде не удаляются (в `core/frame` нет `dispose`). Также `Shader` не вызывает `glDeleteShader`, а `glDeleteProgram` — только в `createNewProgram`.
7. **NPE при отсутствующей текстуре.** `ImageLoader.load(String)` вызывает `ImageIO.read`, который возвращает `null`, если файл не найден, после чего сразу `img.getWidth()` — падение без внятного сообщения. Типичный сценарий: неполный дистрибутив или запуск не из корня.
8. **Сохранения хрупкие и небезопасные.** Java-сериализация с `serialVersionUID = 1L`: любое изменение полей `SaveGame`/`SaveData` ломает старые сейвы (`InvalidClassException`). Десериализация произвольного `.ser` даёт риск выполнения кода. Формат стоит заменить на JSON/бинарный с версионированием.
9. **Конфиг окна может уронить игру.** `getParam` возвращает `null`, если параметра нет (пустой/повреждённый `window_config.txt`), дальше `Integer.parseInt(null)` → `NumberFormatException`. Плюс любое изменение настройки перечитывает и перезаписывает файл целиком.

### Средние

10. **`glEnable(GL_ALPHA)` — невалидный enum.** `Default.init()` каждый запуск генерирует `GL_INVALID_ENUM`; константа не поддерживается в `glEnable`.
11. **Путаница filter/wrap в `Texture2D`.** Параметр `filter` на самом деле задаёт `GL_TEXTURE_WRAP_S/T`, а min/mag жёстко `GL_NEAREST`; мипмапы генерируются (`glGenerateMipmap`), но не используются — лишняя память и алиасинг вдали.
12. **`Texture2D.saveImage` зависит от `imageLine`.** Поле заполняется только в `setup(...)` для карт высот; вызов `saveImage` у обычной текстуры даёт NPE.
13. **Сохранение может молча не произойти.** В `SavePad` запись выполняется только если `saveFile.delete()` вернул `true`; при ошибке удаления — ни записи, ни сообщения. Потоки не закрываются при исключении, код дублируется.
14. **`SceneControl.getCurrentScene` может вернуть `null`.** Если ни одна сцена не активна, возвращается `lastScene` (изначально `null`), а `CoreEngine.run` сразу вызывает `currentScene.reInit()` → NPE.
15. **Клик зависит от FPS.** `ActionVerification` считает кадры удержания (`clickRate = 55`) и сравнивает их с порогом: при 200 FPS «клик» — это удержание меньше ~275 мс, при просадках FPS клики теряются или ложно срабатывают. Нужно время в миллисекундах.
16. **Жёсткие 200 FPS.** `CoreEngine` задаёт `framerate = 200` без привязки к vsync/частоте монитора: лишняя нагрузка на GPU/CPU и нагрев ноутбуков.
17. **Синглтоны не потокобезопасны.** `Window`, `Input`, `Default`, `GameController` используют ленивую инициализацию без синхронизации.
18. **Сгенерированные файлы в репозитории.** `data/save/data/*.ser` и `data/save/image/*.png` (карты, скриншоты сохранений) закоммичены и меняются при каждом запуске — шум в диффах.
19. **`Logger.error` пишет из чужого потока.** Stack trace пишется в тот же `PrintWriter` из вызывающего потока параллельно с логгер-потоком — возможна порча строк, `PrintWriter` не синхронизирован.

### Низкие

20. **`.gitignore` не работает.** Файл состоит из одной строки-описания на английском вместо правил: `build/`, `.gradle/`, `.idea/`, `out/`, `game.log`, `*.iml` не игнорируются (часть уже в индексе).
21. **Мусор в git.** Отслеживаются `.idea/` (compiler.xml, misc.xml и др.), `.gradle/`, файл `InTheMyst.iml` (удаляется), `data/files/img.png` — артефакты IDE и случайные файлы.
22. **README был устаревшим.** Указывал mainClass `ru.phoenix.core.CoreEngine` (не существует), ресурсы в `src/res` и `src/shaders` (их нет), управление и структуру, не совпадающие с кодом.
23. **Нет `LICENSE`.** Раздел лицензии в README оставался заглушкой; для публичного релиза нужен выбранный LICENSE-файл.
24. **Нет `settings.gradle`.** Проект собирается как безымянный root-project: имя проекта и структура подпроектов не заданы, что мешает подключению модулей и CI.
25. **Нет тестов и CI.** `gradlew build` проверяет только компиляцию; регрессии ловятся вручную.
26. **Тяжёлая и почти неиспользуемая зависимость.** `lwjgl-assimp` тянет нативные библиотеки, но используется только в `LoadStaticModel`; стоит проверить, нужна ли она в релизе.
27. **Дублирование имён классов.** `Board` есть и в `game/hud/elements/strucet`, и в `game/hud/instance` — легко перепутать при импорте.
28. **Опечатки в публичном API и конфиге.** `getLangueage`/`setLangueage`, `isFullScrean`, ключ `langueage` в `window_config.txt`; переименование с миграцией конфига желательно до релиза.
29. **Мёртвый код и неиспользуемые параметры.** `SceneControl.isLoading`/`setLoading` (флаг ставится, но не сбрасывается и не читается), `Render.index`, неиспользуемый `pathArray` в `Texture2D.setup`.
30. **Утечки мелких объектов GLFW.** В `Window.setupLogo` `GLFWImage.malloc()` не освобождается; callback'и `Input` не освобождаются в `CoreEngine.cleanUp`.
31. **Смешение языков.** Комментарии и сообщения на русском и английском вперемешку; для международного релиза нужен единый стиль.
