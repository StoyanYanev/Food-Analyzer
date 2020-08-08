# Food Analyzer :hamburger:

В наши дни е особено важно да знаем какво ядем и да следим калориите, които консумираме. Нека си улесним живота като имплементираме приложение, което да ни информира за състава и енергийната стойност на хранителните продукти в менюто ни.

Един от най-изчерпателните и достоверни източници на подобна информация е [FoodData Central](https://fdc.nal.usda.gov/), базата данни за храни и техния състав на [U.S. Department of Agriculture](https://www.usda.gov/). За наше щастие, тази информацията е достъпна и чрез публично безплатно REST API, което е документирано [тук](https://fdc.nal.usda.gov/api-guide.html).

Ще имплементираме Food Analyzer като многонишково клиент-сървър приложение.

## Food Analyzer Server

- Сървърът трябва да може да обслужва множество клиенти едновременно.
- Сървърът получава команди от клиентите и връща подходящ резултат.
- Сървърът извлича необходимите му данни от гореспоменатото *RESTful API* и запазва (кешира) резултата в локалната си файлова система.

    Например, при получаване на командата `get-food raffaello treat`, сървърът прави следната *HTTP GET* заявка: https://api.nal.usda.gov/fdc/v1/search?generalSearchInput=raffaello%20treat&requireAllWords=true&api_key=API_KEY (където API_KEY е валиден API key) и получава *HTTP response* със статус код *200* и с тяло следния *JSON*:

```javascript
{
  "foodSearchCriteria": {
    "generalSearchInput": "raffaello treat",
    "pageNumber": 1,
    "requireAllWords": true
  },
  "totalHits": 1,
  "currentPage": 1,
  "totalPages": 1,
  "foods": [
    {
      "fdcId": 415269,
      "description": "RAFFAELLO, ALMOND COCONUT TREAT",
      "dataType": "Branded",
      "gtinUpc": "009800146130",
      "publishedDate": "2019-04-01",
      "brandOwner": "Ferrero U.S.A., Incorporated",
      "ingredients": "VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.",
      "allHighlightFields": "",
      "score": 247.10071
    }
  ]
}
```

Заявките към REST API-то изискват автентикация с API key, какъвто може да получите като се регистрирате [тук](https://fdc.nal.usda.gov/api-key-signup.html).

От данните за продукта, ни интересува описанието му от полето `description` (`RAFFAELLO, ALMOND COCONUT TREAT`) и уникалния му идентификатор, `fdcId` (`415269`). Някои продукти, по-точно тези с `data type` `Branded`, имат също и GTIN или UPC код, `gtinUpc` (`009800146130`).

**Бележка:** GTIN, или [Global Trade Item Number](https://en.wikipedia.org/wiki/Global_Trade_Item_Number) и UPC, или [Universal Product Code](https://en.wikipedia.org/wiki/Universal_Product_Code), са индентификатори на продукти, кодирани в баркод. С други думи, въпросният код е числото, кодирано в баркода на опаковката на продуктите.

![UPC Barcode](images/upc-barcode.gif)

Сървърът кешира получената информация на локалната файлова система. При получаване на заявка, сървърът първо трябва да провери дали в кеша вече съществува информация за дадения продукт, и ако е така, директно да върне тази информация, вместо да направи нова заявка към REST API-то.

## Food Analyzer Client

Клиентът осъществява връзка с *Food Analyzer Server* на определен порт, чете команди от стандартния вход, изпраща ги към сървъра и извежда получения резултат на стандартния изход в human-readable формат. Клиентът може да изпълнява следните команди:

- `get-food <food_name>` - извежда информацията, описана по-горе, за даден хранителен продукт. Ако сървърът върне множество продукти с даденото име, се извежда информация за всеки от тях. Ако пък липсва информация за продукта, се извежда подходящо съобщение.
- `get-food-report <food_fdcId>` - по даден уникален идентификатор на продукт (`fdcId`) извежда име на продукта, съставки (`ingedients`), енергийна стойност (калории), съдържание на белтъчини, мазнини, въглехидрати и фибри.
- `get-food-by-barcode --code=<gtinUpc_code>|--img=<barcode_image_file>` - извежда информация за продукт по неговия баркод, *ако такава е налична в кеша на сървъра* (обърнете внимание, че REST API-то не поддържа търсене на продукт по `gtinUpc` код или баркод изображение). Задължително е да подадем един от двата параметъра: или `code`, или файл, съдържащ баркод изображение (като пълен път и име на файла на локалната файлова система на клиента). Ако са указани и двата параметъра, `img` параметърът се игнорира.

За да реализирате търсене по баркод изображение, ще имате нужда от Java библиотека или уеб услуга, с които да конвертирате изображение на баркод към числото, което кодира. Може да ползвате open source библиотеката [ZXing "Zebra Crossing"](https://github.com/zxing/zxing) или [ZXing уеб услугата](https://zxing.org/w/decode.jspx).

За тестови цели, може да си генерирате баркод изображения [тук](https://barcode.tec-it.com/en/UPCA), друг вариант е да потърсите в Google изображение или директно да снимате някой баркод на опаковка.

### Пример за валидни входни данни

```bash
get-food beef noodle soup
get-food-report 415269
get-food-by-barcode --img=D:\Photos\BarcodeImage.jpg --code=009800146130
```
