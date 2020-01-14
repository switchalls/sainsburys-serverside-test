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

## Develop solution (phase 1)

Objectives:
  * Coding approach, eg. TDD
  * Coding style, eg. readability
  * Testing (including regression)
  * Demonstrate code `progression` in git history

Simple architecture:
  * `Build what you need, not what you fear you may need` ... Uncle Bob
  * Basic console application
  * Minimal dependencies

1) Create project (setup `pom.xml` ; added `SainsburysPrototypeTest`)

2) Develop parser against downloaded HTML (see `SainsburysPrototypeTest`)

3) Execute against https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html

4) Fixed issues (missing descriptions and nutrition values)

5) Refactored parser to use forEach() and streams()

Duration: 5h

## Develop solution (phase 2)

Address review comments:

> We would have liked to have seen more evidence of
> separation of concerns and Inversion of control

Objectives for rework:
  * Simple objects, eg. single purpose / self contained
  * Inversion of control, eg. injection of dependencies created outside objects

1) Added spring boot 2

## Testing notes

The following products do not have kcal values:

* Sainsbury's Mixed Berries 300g
* Sainsbury's Mixed Berry Twin Pack 200g
* Sainsbury's Blackcurrants 150g
* Sainsbury's British Cherry & Strawberry Pack 600g

Above list verified by manually viewing product details in browser

*TODO* - Why is Jsoup converting absolute href links (`https://jsainsburyplc.github.io`)
to relative ones (`../shop`) when running via main but not junits ?

*TODO* - Should "Blackcurrents 150g" description be "Union Flag" ?
