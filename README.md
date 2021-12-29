**Spustenie klienta**

- z repozitára https://gitlab.fit.cvut.cz/polakemi/bi-tjv_client stiahnite Artifacts/build-gradle
- stiahnutý archív rozbaľte
- pomocou `java -jar <file>` v termináli spustite súbor `build/libs/polakemi_client.jar`, ktorý sa nachádza v rozbalenom archíve
  (testované s openjdk-16)
- klinet by mal fungovať bez problémov, avšak aby sa dokázal pripojiť k serveru, klient a server musia byť spustený v rovnakom prostredí
  (v rovnakom OS, v rovnakom VM...)
- pre všetky možnosti klienta zadajte príkaz `help`