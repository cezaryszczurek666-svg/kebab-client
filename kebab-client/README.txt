KEBAB CLIENT (Fabric, Minecraft 1.21.11)

BUDOWANIE
1. Zainstaluj JDK 21.
2. Wejdz na https://fabricmc.net/develop/template/ , wybierz 1.21.11 i skopiuj
   z niego gradle/wrapper/ oraz gradlew / gradlew.bat do tego folderu
   (albo wygeneruj projekt i wklej do niego folder src/ stad).
3. Sprawdz wartosci w gradle.properties z tymi z generatora.
4. Uruchom: ./gradlew build   -> jar bedzie w build/libs/
5. Test w grze: ./gradlew runClient

PACZKA FPS (wrzuc do mods/ razem z jarem Kebab Client):
Fabric API, Sodium, Lithium, EntityCulling, FerriteCore, ImmediatelyFast, Dynamic FPS, Mod Menu
(pobieraj z modrinth.com)

BUILD BEZ INSTALOWANIA CZEGOKOLWIEK (GitHub Actions)
1. Zaloz konto na github.com i nowe repozytorium (np. kebab-client).
2. Wgraj zawartosc tego folderu (razem z ukrytym folderem .github).
3. Wejdz w zakladke Actions -> build -> poczekaj az sie zazieleni.
4. Na dole strony przebiegu pobierz artefakt "kebab-client-jar" (zip z .jar).
   Wez plik bez "-sources" i wrzuc do .minecraft/mods/
