# Sainsbury's serverside-test by Stewart Witchalls

See [sainsbury's overview](https://jsainsburyplc.github.io/serverside-test/)

To compile:
```bash
$ mvn clean install
```

To execute:
```bash
$ java -jar target/sainsburys-serverside-test-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

# Strategy

## Select technology

* Read `overview`

* Googled `java html parser` ; found [baeldung article](https://www.baeldung.com/java-with-jsoup)

*Question* - Does `jsoup` fit integrate with junits?

Duration: 15mins

## Prototype / Analysis

* Download and analysed `serverside-test` HTML examples.

* Create junits (see `SainsburysPrototypeTest`) to learn `jsoup` and determine appropriate CSS selectors.

Duration: 2 hours (whilst watching TV with kids)

## Develop solution

* Create project (setup `pom.xml` ; added `SainsburysPrototypeTest`)

* Create parser using downloaded HTML (see `SainsburysPrototypeTest`)

* Execute against https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html

* Fixed issues (missing descriptions and nutrition values)

* Updated readme

Duration: 5h

TODO - Fix missing "Blackcurrents 150g" description

TODO - Refactor code to use Collection.forEach()

