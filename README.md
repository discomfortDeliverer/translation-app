# Описание
Translation app - это приложение на Java, использующее Spring Boot, которое предоставляет API для перевода текста с одного языка на другой. Приложение поддерживает многопоточность и ограничивает количество одновременно работающих потоков до 10.
## Используемые технологии
* Java 17: Основной язык программирования проекта.
* Spring Boot 3.3.2: Фреймворк для создания веб-приложений, упрощающий настройку и разработку.
* Spring Web: Модуль Spring для создания RESTful веб-сервисов.
* Maven: Инструмент для управления зависимостями и сборки проекта.
* JUnit 5: Фреймворк для модульного тестирования.
* JdbcTeamplate: Компонент Spring для упрощенного доступа к реляционным базам данных.
* SQLite: Легковесная встраиваемая база данных.
* Внешнее API для перевода: https://ftapi.pythonanywhere.com/
## Установка
1. Склонируйте проект из репозитория:  
```bash
git clone https://github.com/discomfortDeliverer/translation-app.git
```
2. Перейдите в директорию проекта:
```bash
cd 'translation-app'
```
3. Соберите проект используя Maven:  
```bash
./mvnw clean install
```  
Если отказано в доступе, то введите команду:  
```bash
chmod +x mvnw
```
4. После сборки в директории target создастся JAR-файл, запустите его:  
```bash
cd target
java -jar target/translation-app.0.0.1-SNAPSHOT.jar
```
Приложение будет запущено на http://localhost:8080.  
Или откройте приложение с помощью IDE и запустите.
## Использование
### Перевод текста
* **URL:** /translate
* **Метод:** POST
* **Параметры запроса:** sourceLanguage (язык исходного сообщения) и targetLanguage (язык на который перевести)
* **Тело запроса:** В теле запроса написать текст который нужно перевести
* **Описание:** Переводит переданный текст пословно на выбранный язык.

Пример:
1. Запрос:
```curl
curl -X POST "http://localhost:8080/translate?sourceLanguage=ru&targetLanguage=en" \
-H "Content-Type: text/plain" \
-d "Привет"
```
2. Ответ:
```curl
hello
```
<details>
  <summary>Список поддерживаемых языков</summary>
  <ul>
    <li>af: afrikaans</li>
    <li>sq: albanian</li>
    <li>am: amharic</li>
    <li>ar: arabic</li>
    <li>hy: armenian</li>
    <li>az: azerbaijani</li>
    <li>eu: basque</li>
    <li>be: belarusian</li>
    <li>bn: bengali</li>
    <li>bs: bosnian</li>
    <li>bg: bulgarian</li>
    <li>ca: catalan</li>
    <li>ceb: cebuano</li>
    <li>ny: chichewa</li>
    <li>zh-cn: chinese (simplified)</li>
    <li>zh-tw: chinese (traditional)</li>
    <li>co: corsican</li>
    <li>hr: croatian</li>
    <li>cs: czech</li>
    <li>da: danish</li>
    <li>nl: dutch</li>
    <li>en: english</li>
    <li>eo: esperanto</li>
    <li>et: estonian</li>
    <li>tl: filipino</li>
    <li>fi: finnish</li>
    <li>fr: french</li>
    <li>fy: frisian</li>
    <li>gl: galician</li>
    <li>ka: georgian</li>
    <li>de: german</li>
    <li>el: greek</li>
    <li>gu: gujarati</li>
    <li>ht: haitian creole</li>
    <li>ha: hausa</li>
    <li>haw: hawaiian</li>
    <li>iw: hebrew</li>
    <li>he: hebrew</li>
    <li>hi: hindi</li>
    <li>hmn: hmong</li>
    <li>hu: hungarian</li>
    <li>is: icelandic</li>
    <li>ig: igbo</li>
    <li>id: indonesian</li>
    <li>ga: irish</li>
    <li>it: italian</li>
    <li>ja: japanese</li>
    <li>jw: javanese</li>
    <li>kn: kannada</li>
    <li>kk: kazakh</li>
    <li>km: khmer</li>
    <li>ko: korean</li>
    <li>ku: kurdish (kurmanji)</li>
    <li>ky: kyrgyz</li>
    <li>lo: lao</li>
    <li>la: latin</li>
    <li>lv: latvian</li>
    <li>lt: lithuanian</li>
    <li>lb: luxembourgish</li>
    <li>mk: macedonian</li>
    <li>mg: malagasy</li>
    <li>ms: malay</li>
    <li>ml: malayalam</li>
    <li>mt: maltese</li>
    <li>mi: maori</li>
    <li>mr: marathi</li>
    <li>mn: mongolian</li>
    <li>my: myanmar (burmese)</li>
    <li>ne: nepali</li>
    <li>no: norwegian</li>
    <li>or: odia</li>
    <li>ps: pashto</li>
    <li>fa: persian</li>
    <li>pl: polish</li>
    <li>pt: portuguese</li>
    <li>pa: punjabi</li>
    <li>ro: romanian</li>
    <li>ru: russian</li>
    <li>sm: samoan</li>
    <li>gd: scots gaelic</li>
    <li>sr: serbian</li>
    <li>st: sesotho</li>
    <li>sn: shona</li>
    <li>sd: sindhi</li>
    <li>si: sinhala</li>
    <li>sk: slovak</li>
    <li>sl: slovenian</li>
    <li>so: somali</li>
    <li>es: spanish</li>
    <li>su: sundanese</li>
    <li>sw: swahili</li>
    <li>sv: swedish</li>
    <li>tg: tajik</li>
    <li>ta: tamil</li>
    <li>te: telugu</li>
    <li>th: thai</li>
    <li>tr: turkish</li>
    <li>uk: ukrainian</li>
    <li>ur: urdu</li>
    <li>ug: uyghur</li>
    <li>uz: uzbek</li>
    <li>vi: vietnamese</li>
    <li>cy: welsh</li>
    <li>xh: xhosa</li>
    <li>yi: yiddish</li>
    <li>yo: yoruba</li>
    <li>zu: zulu</li>
  </ul>
</details>

### Получение списка всех сохраненных переводов из базы данных

* **URL:** /translations
* **Метод:** GET
* **Описание:** Возвращает все переведенные тексты и ip с которого был совершен запрос на перевод

Пример:  
1. Запрос:
```curl
curl -X GET "http://localhost:8080/translations"
```
2. Ответ:
```json
[
    {
        "ipAddress": "0:0:0:0:0:0:0:1",
        "sourceText": "Летний ветер мягко качал ветви деревьев в солнечном саду.",
        "translatedText": "Summer wind soft rocked branches trees V sunny garden."
    },
    {
        "ipAddress": "0:0:0:0:0:0:0:1",
        "sourceText": "Sametový večer na pláži, kde moře šumí a hvězdy třpytí se na obloze nad námi.",
        "translatedText": "天鹅绒般的 晚上 在 海滩， 在哪里 海 嘶嘶作响 和 星星 闪光 和 在 天空 超过 我们。"
    }
]
```
